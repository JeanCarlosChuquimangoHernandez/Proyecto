package com.example.proyecto

import android.os.Parcel
import android.os.Parcelable

data class Product(
    val id: Int = 0,
    val title: String = "",
    val translated_descripcion: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val image_Path: String? = null,
    val qr_Path: String? = null,
    var quantity: Int =1
) : Parcelable {

    constructor() : this(0, "", "", 0.0, 0, null, null, 1)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(translated_descripcion)
        parcel.writeDouble(price)
        parcel.writeInt(stock)
        parcel.writeString(image_Path)
        parcel.writeString(qr_Path)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}


