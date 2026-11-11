package com.example.ud05cp01

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ud05cp01.databinding.ActivityMainBinding
import java.util.Calendar
import kotlin.math.roundToLong

class MainActivity : AppCompatActivity() {

    // ViewBinding: acceso seguro a las vistas, sin findViewById
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TextView 1: fecha de hoy
        val hoy = Calendar.getInstance()
        binding.tvHoy.text = getString(
            R.string.hoy_es,
            hoy.get(Calendar.DAY_OF_MONTH),
            hoy.get(Calendar.MONTH) + 1,   // MONTH va de 0 (enero) a 11 (diciembre)
            hoy.get(Calendar.YEAR)
        )

        // Evento: al pulsar el botón se abre el diálogo de fecha
        binding.btnFechaNacimiento.setOnClickListener {
            mostrarDialogoFecha()
        }
    }

    private fun mostrarDialogoFecha() {
        val hoy = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calcularEdad(year, month, dayOfMonth)
            },
            hoy.get(Calendar.YEAR),
            hoy.get(Calendar.MONTH),
            hoy.get(Calendar.DAY_OF_MONTH)
        )

        // No se puede elegir una fecha futura
        datePicker.datePicker.maxDate = hoy.timeInMillis
        datePicker.show()
    }

    private fun calcularEdad(year: Int, month: Int, day: Int) {
        // TextView 2: fecha de nacimiento elegida
        binding.tvNaciste.text = getString(R.string.naciste_el, day, month + 1, year)

        val hoy = soloFecha(Calendar.getInstance())
        val nacimiento = Calendar.getInstance()
        nacimiento.clear()
        nacimiento.set(year, month, day)

        // Años cumplidos (se resta uno si este año aún no ha sido el cumpleaños)
        var anios = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)
        if (cumpleanios(nacimiento, anios).after(hoy)) {
            anios--
        }

        // Días desde el último cumpleaños
        val ultimoCumple = cumpleanios(nacimiento, anios)
        val dias = ((hoy.timeInMillis - ultimoCumple.timeInMillis) / MILIS_DIA).roundToLong()

        // TextView 3: resultado
        binding.tvResultado.text = getString(R.string.tienes, anios, dias)
    }

    // Fecha del cumpleaños número "anios"
    private fun cumpleanios(nacimiento: Calendar, anios: Int): Calendar {
        val c = nacimiento.clone() as Calendar
        c.add(Calendar.YEAR, anios)
        return c
    }

    // Copia de la fecha sin horas, minutos ni segundos
    private fun soloFecha(c: Calendar): Calendar {
        val f = Calendar.getInstance()
        f.clear()
        f.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
        return f
    }

    companion object {
        private const val MILIS_DIA = 24 * 60 * 60 * 1000.0
    }
}