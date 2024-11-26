package com.example.proyecto

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity2 : AppCompatActivity(){

    private lateinit var payview: Payview


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.correo2pasos)

        payview = findViewById(R.id.payview)

        payview.setOnDataChangedListener(object : Payview.OnChangelistener{
            override fun onChangelistener(payModel: PayModel?, isFillAllComponents:Boolean) {
                Log.d("PayView", "data : ${payModel?.cardOwnerName} \n " +
                        "is Fill all form component : $isFillAllComponents")

            }

        })

        payview.setPayOnclickListener(View.OnClickListener {
            Log.d("PayView "," clicked. iss Fill all form Component : ${payview.isFillAllComponents}")
        })

    }


}