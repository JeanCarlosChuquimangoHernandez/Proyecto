package com.example.proyecto

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import okhttp3.Call
import java.io.IOException
import okhttp3.*
import org.json.JSONObject
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton



class MapsFragment : Fragment() {
    private lateinit var mapView: MapView
    private lateinit var confirmButton: Button
    private lateinit var addressTextView: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout que contiene el MapView
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Inicializar MapView y otros elementos
        mapView = view.findViewById(R.id.mapView)
        confirmButton = view.findViewById(R.id.confirm_button)
        addressTextView = view.findViewById(R.id.address_text)
        val myLocationButton = view.findViewById<Button>(R.id.my_location_button)

        // Configurar el estilo del mapa
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
        myLocationButton.setOnClickListener {
            obtenerUbicacionActual()
        }
        // Actualizar la dirección al mover el mapa
        mapView.gestures.addOnMoveListener(object : OnMoveListener {
            override fun onMoveBegin(detector: com.mapbox.android.gestures.MoveGestureDetector) {
                // Vaciar el texto mientras se está moviendo el mapa
                addressTextView.text = "Buscando dirección..."
            }
            override fun onMove(detector: com.mapbox.android.gestures.MoveGestureDetector): Boolean {
                // No es necesario hacer nada mientras se mueve
                return false // Indica que el evento ha sido manejado
            }

            override fun onMoveEnd(detector: com.mapbox.android.gestures.MoveGestureDetector) {
                // Obtener la ubicación del centro del mapa
                val centerPoint = mapView.getMapboxMap().cameraState.center
                val latitude = centerPoint.latitude()
                val longitude = centerPoint.longitude()

                // Actualizar el TextView con las coordenadas (luego podrías agregar una función para obtener una dirección legible)
                obtenerDireccionEnTiempoReal(latitude, longitude)
            }
        })

        // Configurar acción del botón de confirmación
        // Dentro de MapsFragment
        confirmButton.setOnClickListener {
            val centerPoint = mapView.getMapboxMap().cameraState.center
            val latitude = centerPoint.latitude()
            val longitude = centerPoint.longitude()

            obtenerDireccion(latitude, longitude) { address ->
                val resultIntent = Intent().apply {
                    putExtra("selectedAddress", address)
                }
                requireActivity().setResult(AppCompatActivity.RESULT_OK, resultIntent)
                requireActivity().finish()
            }
        }


    }
    private fun obtenerDireccion(lat: Double, lng: Double, callback: (String) -> Unit) {
        val client = OkHttpClient()
        val url = "https://api.mapbox.com/geocoding/v5/mapbox.places/$lng,$lat.json?access_token=pk.eyJ1IjoiamVhbmhlcm5hbmRlejE1MyIsImEiOiJjbTNmZGpscmQwbmJiMmxvbW43M3N3dXhjIn0.MlDZZDHlIUw85lKkAw60vg"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback("No se pudo obtener la dirección")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val jsonData = responseBody.string()
                    val jsonObject = JSONObject(jsonData)
                    val features = jsonObject.getJSONArray("features")

                    val address = if (features.length() > 0) {
                        features.getJSONObject(0).getString("place_name")
                    } else {
                        "Dirección desconocida"
                    }
                    callback(address)
                }
            }
        })
    }
    private fun obtenerDireccionEnTiempoReal(lat: Double, lng: Double) {
        val client = OkHttpClient()
        val url = "https://api.mapbox.com/geocoding/v5/mapbox.places/$lng,$lat.json?access_token=pk.eyJ1IjoiamVhbmhlcm5hbmRlejE1MyIsImEiOiJjbTNmZGpscmQwbmJiMmxvbW43M3N3dXhjIn0.MlDZZDHlIUw85lKkAw60vg"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Geocodificación", "Error al obtener la dirección", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val jsonData = responseBody.string()
                    val jsonObject = JSONObject(jsonData)
                    val features = jsonObject.getJSONArray("features")

                    if (features.length() > 0) {
                        val placeName = features.getJSONObject(0).getString("place_name")
                        requireActivity().runOnUiThread {
                            // Actualizar el TextView con la dirección
                            addressTextView.text = "Ubicación: $placeName"
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            addressTextView.text = "No se pudo encontrar la dirección" }
                        }

            }
        }
        })

    }
    private fun obtenerUbicacionActual() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            // Obtener la última ubicación conocida
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = com.mapbox.geojson.Point.fromLngLat(location.longitude, location.latitude)
                    mapView.getMapboxMap().setCamera(com.mapbox.maps.CameraOptions.Builder()
                        .center(latLng)
                        .zoom(14.0)
                        .build())

                    obtenerDireccionEnTiempoReal(location.latitude, location.longitude)
                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Solicitar permisos de ubicación
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }


}
