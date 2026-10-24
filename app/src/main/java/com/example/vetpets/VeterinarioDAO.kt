package com.example.vetpets

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object VeterinarioDAO {

    private val db = FirebaseFirestore.getInstance()
    private const val COLECCION = "veterinarios"

    // =========================================================
    // OBTENER TODOS LOS VETERINARIOS
    // =========================================================
    fun obtenerVeterinarios(onResult: (List<Veterinario>) -> Unit) {
        db.collection(COLECCION)
            .orderBy("nombre", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val lista = mutableListOf<Veterinario>()
                for (doc in documents) {
                    val veterinario = Veterinario(
                        idVeterinario = doc.getLong("idVeterinario")?.toInt() ?: 0,
                        nombre = doc.getString("nombre") ?: "",
                        especialidad = doc.getString("especialidad") ?: "",
                        telefono = doc.getString("telefono") ?: "",
                        correo = doc.getString("correo") ?: ""
                    )
                    lista.add(veterinario)
                }
                onResult(lista)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    // =========================================================
    // REGISTRAR VETERINARIO
    // =========================================================
    fun registrarVeterinario(
        nombre: String,
        especialidad: String,
        telefono: String,
        correo: String,
        onResult: (Boolean) -> Unit
    ) {
        val idGenerado = System.currentTimeMillis().toInt()

        val nuevoVeterinario = hashMapOf(
            "idVeterinario" to idGenerado,
            "nombre" to nombre,
            "especialidad" to especialidad,
            "telefono" to telefono,
            "correo" to correo
        )

        db.collection(COLECCION)
            .add(nuevoVeterinario)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // =========================================================
    // ACTUALIZAR VETERINARIO
    // =========================================================
    fun actualizarVeterinario(
        idVeterinario: Int,
        nombre: String,
        especialidad: String,
        telefono: String,
        correo: String,
        onResult: (Boolean) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("idVeterinario", idVeterinario)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val docId = documents.documents[0].id
                    val datosActualizados = mapOf<String, Any>(
                        "nombre" to nombre,
                        "especialidad" to especialidad,
                        "telefono" to telefono,
                        "correo" to correo
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
    // ELIMINAR VETERINARIO
    // =========================================================
    fun eliminarVeterinario(
        idVeterinario: Int,
        onResult: (Boolean) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("idVeterinario", idVeterinario)
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