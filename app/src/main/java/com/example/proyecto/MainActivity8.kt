package com.example.proyecto

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity


class MainActivity8 : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)

        if (savedInstanceState == null) {
            val fragment = MapsFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // 'fragment_container' es un contenedor definido en activity_main.xml
                .commit()
        }

    }
}
