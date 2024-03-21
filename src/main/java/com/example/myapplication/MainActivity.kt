package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nomPrenomClient = findViewById<TextView>(R.id.nom_prenom_client)
        findViewById<TextView>(R.id.temperature_actuelle)
        val batteryValue = findViewById<TextView>(R.id.batteryValue)
        val networkValue = findViewById<TextView>(R.id.networkValue)
        val chargerValue = findViewById<TextView>(R.id.chargerValue)

        val data = intent.getStringExtra("data")
        data?.let {
            val jsonObject = JSONObject(it)
            val nom = jsonObject.getString("Nom")
            val prenom = jsonObject.getString("Prenom")
            val tceam = jsonObject.getString("TCEAM")
            val ens = jsonObject.getString("ENS")
            val eec = jsonObject.getString("EEC")

            nomPrenomClient.text = "$nom $prenom"
            batteryValue.text = tceam + " kW"
            networkValue.text = ens + " kW"
            chargerValue.text = eec + " kW"
        }
        Log.d("MainActivity", "Data received: $data")

        findViewById<Button>(R.id.login_data).setOnClickListener {
            val intent = Intent(this, DataActivity::class.java)
            startActivity(intent)
        }
    }
}