package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val nom = prefs.getString("Nom", null)
        val numClient = prefs.getString("NumClient", null)

        if (nom != null && numClient != null) {
            navigateToDataActivity()
            return
        }

        emailEditText = findViewById(R.id.login_email)
        passwordEditText = findViewById(R.id.login_password)
        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener { login() }
    }

    private fun login() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Les champs ne peuvent pas être vides", Toast.LENGTH_SHORT).show()
            return
        }

        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2:5000/login"

        val jsonBody = JSONObject()
        jsonBody.put("Nom", email)
        jsonBody.put("numClient", password)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonBody,
            Response.Listener { response ->
                if (response.getBoolean("status")) {
                    // Sauvegarder les détails de l'utilisateur localement
                    getSharedPreferences("UserPrefs", Context.MODE_PRIVATE).edit().apply {
                        putString("Nom", email)
                        putString("NumClient", password)
                        apply()
                    }


                    navigateToDataActivity()
                    Log.d("LoginActivity", "Tentative de connexion pour Nom: $email, NumClient: $password")

                } else {
                    Toast.makeText(this@LoginActivity, response.getString("message"), Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Log.d("LoginActivity", "Échec de la connexion: ${error.toString()}")
            })

        queue.add(jsonObjectRequest)
    }

    private fun navigateToDataActivity() {
        val intent = Intent(this@LoginActivity, DataActivity::class.java)
        startActivity(intent)
        finish()
    }
}
