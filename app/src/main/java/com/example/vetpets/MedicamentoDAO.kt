package com.example.vetpets

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object MedicamentoDAO {

    private val db = FirebaseFirestore.getInstance()
    private const val COLECCION = "medicamentos"

    // =========================================================
    // OBTENER MEDICAMENTOS DE UNA MASCOTA
    // =========================================================
    fun obtenerMedicamentos(
        idMascota: Int,
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
                        idMedicamento = doc.getLong("idMedicamento")?.toInt() ?: 0,
                        idMascota = doc.getLong("idMascota")?.toInt() ?: 0,
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
        idMascota: Int,
        nombreMedicamento: String,
        dosis: String,
        frecuencia: String,
        fechaInicio: String,
        fechaFin: String,
        indicaciones: String,
        onResult: (Boolean) -> Unit
    ) {
        val idGenerado = System.currentTimeMillis().toInt()

        val nuevoMedicamento = hashMapOf(
            "idMedicamento" to idGenerado,
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
        idMedicamento: Int,
        onResult: (Boolean) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("idMedicamento", idMedicamento)
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