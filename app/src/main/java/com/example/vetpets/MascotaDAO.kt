package com.example.vetpets

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object MascotaDAO {

    private val db = FirebaseFirestore.getInstance()
    private const val COLECCION = "mascotas"

    // =========================================================
    // OBTENER MASCOTAS DE UN USUARIO
    // =========================================================
    fun obtenerMascotas(
        idUsuario: Int,
        onResult: (List<Mascota>) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("idUsuario", idUsuario)
            .get()
            .addOnSuccessListener { documents ->
                val listaMascotas = mutableListOf<Mascota>()
                for (doc in documents) {
                    val mascota = Mascota(
                        idMascota = doc.getLong("idMascota")?.toInt() ?: 0,
                        idUsuario = doc.getLong("idUsuario")?.toInt() ?: 0,
                        nombre = doc.getString("nombre") ?: "",
                        especie = doc.getString("especie") ?: "",
                        raza = doc.getString("raza") ?: "",
                        sexo = doc.getString("sexo") ?: "",
                        fechaNacimiento = doc.getString("fechaNacimiento") ?: "",
                        foto = doc.getString("foto") ?: ""
                    )
                    listaMascotas.add(mascota)
                }
                onResult(listaMascotas)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    // =========================================================
    // OBTENER MASCOTA POR ID
    // =========================================================
    fun obtenerMascotaPorId(
        idMascota: Int,
        onResult: (Mascota?) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("idMascota", idMascota)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val doc = documents.documents[0]
                    val mascota = Mascota(
                        idMascota = doc.getLong("idMascota")?.toInt() ?: 0,
                        idUsuario = doc.getLong("idUsuario")?.toInt() ?: 0,
                        nombre = doc.getString("nombre") ?: "",
                        especie = doc.getString("especie") ?: "",
                        raza = doc.getString("raza") ?: "",
                        sexo = doc.getString("sexo") ?: "",
                        fechaNacimiento = doc.getString("fechaNacimiento") ?: "",
                        foto = doc.getString("foto") ?: ""
                    )
                    onResult(mascota)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    // =========================================================
    // REGISTRAR MASCOTA
    // =========================================================
    fun registrarMascota(
        idUsuario: Int,
        nombre: String,
        especie: String,
        raza: String,
        sexo: String,
        fechaNacimiento: String,
        onResult: (Boolean) -> Unit
    ) {
        val idGenerado = System.currentTimeMillis().toInt()

        val nuevaMascota = hashMapOf(
            "idMascota" to idGenerado,
            "idUsuario" to idUsuario,
            "nombre" to nombre,
            "especie" to especie,
            "raza" to raza,
            "sexo" to sexo,
            "fechaNacimiento" to fechaNacimiento,
            "foto" to ""
        )

        db.collection(COLECCION)
            .add(nuevaMascota)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // =========================================================
    // ACTUALIZAR MASCOTA
    // =========================================================
    fun actualizarMascota(
        idMascota: Int,
        nombre: String,
        especie: String,
        raza: String,
        sexo: String,
        fechaNacimiento: String,
        onResult: (Boolean) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("idMascota", idMascota)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val docId = documents.documents[0].id
                    val datosActualizados = mapOf<String, Any>(
                        "nombre" to nombre,
                        "especie" to especie,
                        "raza" to raza,
                        "sexo" to sexo,
                        "fechaNacimiento" to fechaNacimiento
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
    // ELIMINAR MASCOTA
    // =========================================================
    fun eliminarMascota(
        idMascota: Int,
        onResult: (Boolean) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("idMascota", idMascota)
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