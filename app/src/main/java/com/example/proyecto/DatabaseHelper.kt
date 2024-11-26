package com.example.proyecto

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "productos.db"
        private const val DATABASE_VERSION = 1
        private const val TAG = "DatabaseHelper"
    }

    init {
        // Intenta copiar la base de datos al inicializar la clase
        try {
            copyDatabaseIfNeeded()
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing database: ${e.message}", e)
        }
    }

    /**
     * Copia la base de datos desde res/raw si aún no existe en el almacenamiento interno.
     */
    private fun copyDatabaseIfNeeded() {
        val dbFile = context.getDatabasePath(DATABASE_NAME)

        if (!dbFile.exists()) {
            // Crear directorio si no existe
            dbFile.parentFile?.apply {
                if (!exists() && !mkdirs()) {
                    throw IOException("Failed to create database directory")
                }
            }

            try {
                // Copiar la base de datos desde res/raw
                context.resources.openRawResource(R.raw.productos).use { input ->
                    FileOutputStream(dbFile).use { output ->
                        input.copyTo(output)
                    }
                }
                Log.d(TAG, "Database copied successfully to ${dbFile.absolutePath}")
            } catch (e: IOException) {
                Log.e(TAG, "Error copying database: ${e.message}", e)
                // Eliminar el archivo si la copia falla
                if (dbFile.exists()) dbFile.delete()
                throw e
            }
        }
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        return try {
            super.getWritableDatabase()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting writable database: ${e.message}", e)
            // Intentar recuperar la base de datos
            context.getDatabasePath(DATABASE_NAME).delete()
            copyDatabaseIfNeeded()
            super.getWritableDatabase()
        }
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        return try {
            super.getReadableDatabase()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting readable database: ${e.message}", e)
            // Intentar recuperar la base de datos
            context.getDatabasePath(DATABASE_NAME).delete()
            copyDatabaseIfNeeded()
            super.getReadableDatabase()
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // No se necesita implementar, ya que la base de datos ya contiene las tablas
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Implementar lógica de actualización si es necesario
    }

    /**
     * Obtiene la lista de categorías desde la base de datos.
     */
    fun getCategories(): List<String> {
        val categories = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT DISTINCT nombre FROM categorias", null)
        if (cursor.moveToFirst()) {
            do {
                val category = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                categories.add(category)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return categories
    }

    /**
     * Obtiene los productos según la categoría seleccionada.
     */
    fun getProductsForCategory(category: String): List<Product> {
        Log.d("DatabaseHelper", "Obteniendo productos para la categoría: $category")
        val products = mutableListOf<Product>()
        val db = readableDatabase
        val query = """
        SELECT p.id, p.title, p.translated_descripcion, p.price, p.stock, p.image_path, p.qr_path
        FROM productos p
        INNER JOIN categorias c ON p.categoria_id = c.id
        WHERE c.nombre = ?
    """
        val cursor = db.rawQuery(query, arrayOf(category))
        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    id = cursor.getInt(0),
                    title = cursor.getString(1),
                    translated_descripcion  = cursor.getString(2),
                    price = cursor.getDouble(3),
                    stock = cursor.getInt(4),
                    image_Path = cursor.getString(5),
                    qr_Path = cursor.getString(6)
                )
                products.add(product)
            } while (cursor.moveToNext())
        } else {
            Log.d("DatabaseHelper", "No se encontraron productos para la categoría: $category")
        }
        cursor.close()
        Log.d("DatabaseHelper", "Productos obtenidos: ${products.size}")
        return products
    }



    fun getProducts(): List<Product> {

        val products = mutableListOf<Product>()


        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, title, translated_descripcion, price, stock, image_path, qr_Path FROM productos", null)
        if (cursor.moveToFirst()) {
            do {
                products.add(
                    Product(
                        id = cursor.getInt(0),
                        title = cursor.getString(1),
                        translated_descripcion = cursor.getString(2),
                        price = cursor.getDouble(3),
                        stock = cursor.getInt(4),
                        image_Path = cursor.getString(5),
                        qr_Path = cursor.getString(6)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return products
    }

    fun updateImagePaths() {
        val db = writableDatabase
        try {
            val sql = "UPDATE productos SET image_path = REPLACE(image_path, 'imagenes/', 'imagen');"
            db.execSQL(sql)
            println("Rutas de imagen actualizadas correctamente.")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error al actualizar las rutas de imagen: ${e.message}")
        } finally {
            db.close()
        }
    }

}

