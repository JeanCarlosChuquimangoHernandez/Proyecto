package com.example.proyecto

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class ProductDetailFragment : Fragment() {

    companion object {
        private const val ARG_PRODUCT = "product"
        private const val TAG = "ProductDetailFragment"

        fun newInstance(product: Product): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_PRODUCT, product)  // El objeto Product debe implementar Parcelable
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView iniciado")
        val view = inflater.inflate(R.layout.fragment_product_detail, container, false)

        // Recupera el producto del argumento
        val product = arguments?.getParcelable<Product>(ARG_PRODUCT)

        if (product != null) {
            // Inicializa las vistas con los detalles del producto
            val titleTextView: TextView = view.findViewById(R.id.product_title)
            val descriptionTextView: TextView = view.findViewById(R.id.product_description)
            val priceTextView: TextView = view.findViewById(R.id.product_price)
            val stockTextView: TextView = view.findViewById(R.id.product_stock)
            val productImageView: ImageView = view.findViewById(R.id.product_image)
            val qrCodeImageView: ImageView = view.findViewById(R.id.product_qr_code)
            titleTextView.text = product.title
            descriptionTextView.text = product.translated_descripcion
            priceTextView.text = "Precio: S/ ${product.price}"
            stockTextView.text = "Stock disponible: ${product.stock}"


            // Carga la imagen usando Glide o desde drawable

            val imageName = product.image_Path?.replace(".jpg", "")
            val imageResId = context?.resources?.getIdentifier(
                imageName, "drawable", context?.packageName
            )

            if (imageResId != null && imageResId != 0) {
                productImageView.setImageResource(imageResId)
            } else {
                Log.e(TAG, "Recurso de imagen no encontrado: ${product.image_Path}")
            }
            generateQRCode(product.title, qrCodeImageView)
            Log.d(TAG, "Detalles del producto configurados para: ${product.title}")
        } else {
            Log.e(TAG, "Producto no encontrado en los argumentos")
        }

        return view
    }
    private fun generateQRCode(text: String, imageView: ImageView) {
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) -0x1000000 else -0x1) // Colores: negro y blanco
                }
            }
            imageView.setImageBitmap(bmp)
        } catch (e: WriterException) {
            Log.e(TAG, "Error al generar el código QR: ${e.message}")
        }
    }
}


