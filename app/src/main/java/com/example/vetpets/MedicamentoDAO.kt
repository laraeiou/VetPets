package com.example.vetpets

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object MedicamentoDAO {

    private val db = FirebaseFirestore.getInstance()
    private const val COLECCION = "medicamentos"

    // =========================================================
    // OBTENER MEDICAMENTOS DE UNA MASCOTA (CON STRING)
    // =========================================================
    fun obtenerMedicamentos(
        idMascota: String,
        onResult: (List<Medicamento>) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("idMascota", idMascota)
            .orderBy("fechaInicio", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val lista = mutableListOf<Medicamento>()
                for (doc in documents) {
                    val medicamento = Medicamento(
                        idMedicamento = doc.id, // Usamos el ID de documento de Firestore
                        idMascota = doc.getString("idMascota") ?: "",
                        nombreMedicamento = doc.getString("nombreMedicamento") ?: "",
                        dosis = doc.getString("dosis") ?: "",
                        frecuencia = doc.getString("frecuencia") ?: "",
                        fechaInicio = doc.getString("fechaInicio") ?: "",
                        fechaFin = doc.getString("fechaFin") ?: "",
                        indicaciones = doc.getString("indicaciones") ?: ""
                    )
                    lista.add(medicamento)
                }
                onResult(lista)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    // =========================================================
    // REGISTRAR MEDICAMENTO
    // =========================================================
    fun registrarMedicamento(
        idMascota: String,
        nombreMedicamento: String,
        dosis: String,
        frecuencia: String,
        fechaInicio: String,
        fechaFin: String,
        indicaciones: String,
        onResult: (Boolean) -> Unit
    ) {
        val nuevoMedicamento = hashMapOf(
            "idMascota" to idMascota,
            "nombreMedicamento" to nombreMedicamento,
            "dosis" to dosis,
            "frecuencia" to frecuencia,
            "fechaInicio" to fechaInicio,
            "fechaFin" to fechaFin,
            "indicaciones" to indicaciones
        )

        db.collection(COLECCION)
            .add(nuevoMedicamento)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // =========================================================
    // ELIMINAR MEDICAMENTO
    // =========================================================
    fun eliminarMedicamento(
        idMedicamento: String,
        onResult: (Boolean) -> Unit
    ) {
        db.collection(COLECCION)
            .document(idMedicamento)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
}