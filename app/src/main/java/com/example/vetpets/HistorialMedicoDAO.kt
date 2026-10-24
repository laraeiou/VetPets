package com.example.vetpets

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object HistorialMedicoDAO {

    private val db = FirebaseFirestore.getInstance()
    private const val COLECCION = "historial_medico"

    // =========================================================
    // OBTENER HISTORIAL DE UNA MASCOTA
    // =========================================================
    fun obtenerHistorial(
        idMascota: Int,
        onResult: (List<HistorialMedico>) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("idMascota", idMascota)
            .orderBy("fecha", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val lista = mutableListOf<HistorialMedico>()
                for (doc in documents) {
                    val historial = HistorialMedico(
                        idHistorial = doc.getLong("idHistorial")?.toInt() ?: 0,
                        idMascota = doc.getLong("idMascota")?.toInt() ?: 0,
                        fecha = doc.getString("fecha") ?: "",
                        motivo = doc.getString("motivo") ?: "",
                        diagnostico = doc.getString("diagnostico") ?: "",
                        tratamiento = doc.getString("tratamiento") ?: "",
                        observaciones = doc.getString("observaciones") ?: "",
                        idVeterinario = doc.getLong("idVeterinario")?.toInt(),
                        nombreVeterinario = doc.getString("nombreVeterinario") ?: "No asignado"
                    )
                    lista.add(historial)
                }
                onResult(lista)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    // =========================================================
    // OBTENER HISTORIAL POR ID
    // =========================================================
    fun obtenerHistorialPorId(
        idHistorial: Int,
        onResult: (HistorialMedico?) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("idHistorial", idHistorial)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val doc = documents.documents[0]
                    val historial = HistorialMedico(
                        idHistorial = doc.getLong("idHistorial")?.toInt() ?: 0,
                        idMascota = doc.getLong("idMascota")?.toInt() ?: 0,
                        fecha = doc.getString("fecha") ?: "",
                        motivo = doc.getString("motivo") ?: "",
                        diagnostico = doc.getString("diagnostico") ?: "",
                        tratamiento = doc.getString("tratamiento") ?: "",
                        observaciones = doc.getString("observaciones") ?: "",
                        idVeterinario = doc.getLong("idVeterinario")?.toInt(),
                        nombreVeterinario = doc.getString("nombreVeterinario") ?: "No asignado"
                    )
                    onResult(historial)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    // =========================================================
    // REGISTRAR HISTORIAL
    // =========================================================
    fun registrarHistorial(
        idMascota: Int,
        fecha: String,
        motivo: String,
        diagnostico: String,
        tratamiento: String,
        observaciones: String,
        idVeterinario: Int?,
        nombreVeterinario: String,
        onResult: (Boolean) -> Unit
    ) {
        val idGenerado = System.currentTimeMillis().toInt()

        val nuevoHistorial = hashMapOf(
            "idHistorial" to idGenerado,
            "idMascota" to idMascota,
            "fecha" to fecha,
            "motivo" to motivo,
            "diagnostico" to diagnostico,
            "tratamiento" to tratamiento,
            "observaciones" to observaciones,
            "idVeterinario" to (idVeterinario ?: 0),
            "nombreVeterinario" to nombreVeterinario
        )

        db.collection(COLECCION)
            .add(nuevoHistorial)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // =========================================================
    // ACTUALIZAR HISTORIAL
    // =========================================================
    fun actualizarHistorial(
        idHistorial: Int,
        fecha: String,
        motivo: String,
        diagnostico: String,
        tratamiento: String,
        observaciones: String,
        idVeterinario: Int?,
        nombreVeterinario: String,
        onResult: (Boolean) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("idHistorial", idHistorial)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val docId = documents.documents[0].id
                    val datosActualizados = mapOf<String, Any>(
                        "fecha" to fecha,
                        "motivo" to motivo,
                        "diagnostico" to diagnostico,
                        "tratamiento" to tratamiento,
                        "observaciones" to observaciones,
                        "idVeterinario" to (idVeterinario ?: 0),
                        "nombreVeterinario" to nombreVeterinario
                    )

                    db.collection(COLECCION)
                        .document(docId)
                        .update(datosActualizados)
                        .addOnSuccessListener { onResult(true) }
                        .addOnFailureListener { onResult(false) }
                } else {
                    onResult(false)
                }
            }
            .addOnFailureListener { onResult(false) }
    }

    // =========================================================
    // ELIMINAR HISTORIAL
    // =========================================================
    fun eliminarHistorial(
        idHistorial: Int,
        onResult: (Boolean) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("idHistorial", idHistorial)
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