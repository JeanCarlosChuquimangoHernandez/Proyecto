package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class QRScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scan)

        // Iniciar el escáner
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea el código QR del producto")
        integrator.setCameraId(0) // Cámara trasera
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                // El usuario canceló el escaneo
                finish()
            } else {
                // Código QR escaneado exitosamente
                val scannedProductName = result.contents
                // Aquí puedes pasar el nombre escaneado para buscar el producto
                searchProductByName(scannedProductName)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun searchProductByName(productName: String) {
        // Aquí puedes implementar la lógica para buscar el producto en tu base de datos
        // Puedes navegar a otro fragmento o actividad para mostrar los resultados
    }
}
