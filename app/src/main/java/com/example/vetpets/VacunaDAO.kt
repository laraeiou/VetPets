package com.example.vetpets

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object VacunaDAO {

    private val db = FirebaseFirestore.getInstance()
    private const val COLECCION = "vacunas"

    // =========================================================
    // OBTENER VACUNAS DE UNA MASCOTA
    // =========================================================
    fun obtenerVacunas(
        idMascota: Int,
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
                        idVacuna = doc.getLong("idVacuna")?.toInt() ?: 0,
                        idMascota = doc.getLong("idMascota")?.toInt() ?: 0,
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
        idMascota: Int,
        nombreVacuna: String,
        fechaAplicacion: String,
        proximaDosis: String,
        observaciones: String,
        onResult: (Boolean) -> Unit
    ) {
        val idGenerado = System.currentTimeMillis().toInt()

        val nuevaVacuna = hashMapOf(
            "idVacuna" to idGenerado,
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
        idVacuna: Int,
        onResult: (Boolean) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("idVacuna", idVacuna)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val docId = documents.documents[0].id
                    db.collection(COLECCION)
                        .document(docId)
                        .delete()
                        .addOnSuccessListener { onResult(true) }
                        .addOnFailureListener { onResult(false) }
                } else {
                    onResult(false)
                }
            }
            .addOnFailureListener { onResult(false) }
    }
}