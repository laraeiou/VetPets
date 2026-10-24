package com.example.vetpets

import com.google.firebase.firestore.FirebaseFirestore

object UsuarioDAO {

    private val db = FirebaseFirestore.getInstance()
    private const val COLECCION = "usuarios"

    // =========================================================
    // VALIDAR USUARIO (LOGIN)
    // =========================================================
    fun validarUsuario(
        usuario: String,
        contrasena: String,
        onResult: (Int?) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("usuario", usuario)
            .whereEqualTo("contrasena", contrasena)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val doc = documents.documents[0]
                    val idUsuario = doc.getLong("idUsuario")?.toInt() ?: 1
                    onResult(idUsuario)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    // =========================================================
    // VERIFICAR SI EL USUARIO YA EXISTE
    // =========================================================
    fun usuarioExiste(
        usuario: String,
        onResult: (Boolean) -> Unit
    ) {
        db.collection(COLECCION)
            .whereEqualTo("usuario", usuario)
            .get()
            .addOnSuccessListener { documents ->
                onResult(!documents.isEmpty)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    // =========================================================
    // REGISTRAR NUEVO USUARIO
    // =========================================================
    fun registrarUsuario(
        nombre: String,
        usuario: String,
        contrasena: String,
        onResult: (Boolean) -> Unit
    ) {
        val idGenerado = System.currentTimeMillis().toInt()

        val nuevoUsuario = hashMapOf(
            "idUsuario" to idGenerado,
            "nombre" to nombre,
            "usuario" to usuario,
            "contrasena" to contrasena
        )

        db.collection(COLECCION)
            .add(nuevoUsuario)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
}