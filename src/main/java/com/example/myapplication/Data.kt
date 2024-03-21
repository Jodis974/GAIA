package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class DataActivity : AppCompatActivity() {
    private val handler = Handler()
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        fetchAndUpdateData()

        runnable = Runnable {
            fetchAndUpdateData()
            handler.postDelayed(runnable, 30000)
        }
        handler.postDelayed(runnable, 30000)
    }

    private fun fetchAndUpdateData() {
        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val nom = prefs.getString("Nom", "")
        val numClient = prefs.getString("NumClient", "")

        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2:5000/data?Nom=$nom&numClient=$numClient"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                if (response.getBoolean("status")) {
                    val data = response.getJSONObject("data")
                    // Mettre à jour l'UI avec les nouvelles données
                    updateUI(data)
                }
            },
            Response.ErrorListener { error ->
                Log.d("DataActivity", "Erreur de récupération des données: ${error.toString()}")
            })

        queue.add(jsonObjectRequest)
    }

    private fun updateUI(data: JSONObject) {
        try {
            findViewById<TextView>(R.id.nom_prenom_data).text = "${data.getString("Nom")} ${data.getString("Prenom")}"
        } catch (e: JSONException) {
            Log.e("DataActivity", "Erreur lors de la mise à jour du Nom/Prenom: ${e.message}")
        }

        try {
            findViewById<TextView>(R.id.dce).text = data.getString("DCE")
        } catch (e: JSONException) {
            Log.e("DataActivity", "Erreur lors de la mise à jour de DCE: ${e.message}")
        }

        try {
            findViewById<TextView>(R.id.tceam).text = data.getString("TCEAM")
        } catch (e: JSONException) {
            Log.e("DataActivity", "Erreur lors de la mise à jour de TCEAM: ${e.message}")
        }

        try {
            findViewById<TextView>(R.id.tceamb).text = data.getString("TCEAMB")
        } catch (e: JSONException) {
            Log.e("DataActivity", "Erreur lors de la mise à jour de TCEAMB: ${e.message}")
        }

        try {
            findViewById<TextView>(R.id.tceav).text = data.getString("TCEAV")
        } catch (e: JSONException) {
            Log.e("DataActivity", "Erreur lors de la mise à jour de TCEAV: ${e.message}")
        }

        try {
            findViewById<TextView>(R.id.ens).text = data.getString("ENS")
        } catch (e: JSONException) {
            Log.e("DataActivity", "Erreur lors de la mise à jour de ENS: ${e.message}")
        }

        try {
            findViewById<TextView>(R.id.eec).text = data.getString("EEC")
        } catch (e: JSONException) {
            Log.e("DataActivity", "Erreur lors de la mise à jour de EEC: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}
