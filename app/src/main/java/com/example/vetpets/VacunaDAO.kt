package com.example.vetpets

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object VacunaDAO {

    private val db = FirebaseFirestore.getInstance()
    private const val COLECCION = "vacunas"

    // =========================================================
    // OBTENER VACUNAS DE UNA MASCOTA (CON STRING)
    // =========================================================
    fun obtenerVacunas(
        idMascota: String,
        onResult: (List<Vacuna>) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("idMascota", idMascota)
            .orderBy("fechaAplicacion", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val lista = mutableListOf<Vacuna>()
                for (doc in documents) {
                    val vacuna = Vacuna(
                        idVacuna = doc.id, // Usamos el ID de documento de Firestore
                        idMascota = doc.getString("idMascota") ?: "",
                        nombreVacuna = doc.getString("nombreVacuna") ?: "",
                        fechaAplicacion = doc.getString("fechaAplicacion") ?: "",
                        proximaDosis = doc.getString("proximaDosis") ?: "",
                        observaciones = doc.getString("observaciones") ?: ""
                    )
                    lista.add(vacuna)
                }
                onResult(lista)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    // =========================================================
    // REGISTRAR VACUNA
    // =========================================================
    fun registrarVacuna(
        idMascota: String,
        nombreVacuna: String,
        fechaAplicacion: String,
        proximaDosis: String,
        observaciones: String,
        onResult: (Boolean) -> Unit
    ) {
        val nuevaVacuna = hashMapOf(
            "idMascota" to idMascota,
            "nombreVacuna" to nombreVacuna,
            "fechaAplicacion" to fechaAplicacion,
            "proximaDosis" to proximaDosis,
            "observaciones" to observaciones
        )

        db.collection(COLECCION)
            .add(nuevaVacuna)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // =========================================================
    // ELIMINAR VACUNA
    // =========================================================
    fun eliminarVacuna(
        idVacuna: String,
        onResult: (Boolean) -> Unit
    ) {
        db.collection(COLECCION)
            .document(idVacuna)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
}