package com.example.kotlin_libreria

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kotlin_libreria.databinding.ActivityHomeBinding
import com.example.kotlin_libreria.model.SessionManager

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateUserUI()

        if (savedInstanceState == null) {
            loadFragment(LibroFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_productos -> {
                    loadFragment(LibroFragment())
                    true
                }
                R.id.nav_carrito -> {
                    if (SessionManager.isLoggedIn) {
                        loadFragment(CarritoFragment())
                        true
                    } else {
                        Toast.makeText(this, "Inicia sesión para ver tu carrito", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        false
                    }
                }
                else -> false
            }
        }

        binding.btnLoginLogout.setOnClickListener {
            if (SessionManager.isLoggedIn) {
                SessionManager.logout()
                updateUserUI()
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUserUI()
    }

    private fun updateUserUI() {
        if (SessionManager.isLoggedIn) {
            binding.tvUserName.text = SessionManager.userNombre ?: "Usuario"
            binding.btnLoginLogout.setImageResource(R.drawable.ic_logout)
        } else {
            binding.tvUserName.text = "Invitado"
            binding.btnLoginLogout.setImageResource(R.drawable.ic_person)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
