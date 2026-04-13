package com.example.kotlin_libreria

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlin_libreria.databinding.ActivityPagoBinding
import com.example.kotlin_libreria.model.CarritoManager

class PagoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPagoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupResumen()
        setupListeners()
    }

    private fun setupResumen() {
        val cantidad = CarritoManager.getCantidadTotal()
        val total = CarritoManager.getTotal()

        binding.tvCantidadProductos.text = cantidad.toString()
        binding.tvTotalPagar.text = "S/. ${String.format("%.2f", total)}"
    }

    private fun setupListeners() {
        binding.etFechaVencimiento.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            private var cursorPosition = 0

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                cursorPosition = binding.etFechaVencimiento.selectionStart
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                val input = s?.toString()?.replace("/", "") ?: ""
                val formatted = StringBuilder()

                for (i in input.indices) {
                    if (i == 2) formatted.append("/")
                    formatted.append(input[i])
                }

                binding.etFechaVencimiento.setText(formatted.toString())

                val newCursor = when {
                    cursorPosition <= 2 -> cursorPosition
                    cursorPosition == 3 -> 3
                    else -> cursorPosition + 1
                }
                if (newCursor <= binding.etFechaVencimiento.text?.length ?: 0) {
                    binding.etFechaVencimiento.setSelection(newCursor)
                }

                isFormatting = false
            }
        })

        binding.btnConfirmarPago.setOnClickListener {
            if (validarCampos()) {
                procesarPago()
            }
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun validarCampos(): Boolean {
        val nombre = binding.etNombreTarjeta.text.toString().trim()
        val numero = binding.etNumeroTarjeta.text.toString().trim()
        val fecha = binding.etFechaVencimiento.text.toString().trim()
        val cvv = binding.etCvv.text.toString().trim()

        var esValido = true

        if (nombre.isEmpty()) {
            binding.etNombreTarjeta.error = "Ingrese el nombre"
            esValido = false
        } else {
            binding.etNombreTarjeta.error = null
        }

        if (numero.isEmpty() || numero.length < 16) {
            binding.etNumeroTarjeta.error = "Ingrese 16 dígitos"
            esValido = false
        } else {
            binding.etNumeroTarjeta.error = null
        }

        if (fecha.isEmpty() || !fecha.matches(Regex("\\d{2}/\\d{2}"))) {
            binding.etFechaVencimiento.error = "Formato: MM/YY"
            esValido = false
        } else {
            binding.etFechaVencimiento.error = null
        }

        if (cvv.isEmpty() || cvv.length < 3) {
            binding.etCvv.error = "CVV inválido"
            esValido = false
        } else {
            binding.etCvv.error = null
        }

        return esValido
    }

    private fun procesarPago() {
        binding.btnConfirmarPago.isEnabled = false
        binding.btnConfirmarPago.text = "Procesando..."

        binding.root.postDelayed({
            CarritoManager.limpiarCarrito()

            Toast.makeText(
                this,
                "¡Pago exitoso! Gracias por tu compra",
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }, 1500)
    }
}
