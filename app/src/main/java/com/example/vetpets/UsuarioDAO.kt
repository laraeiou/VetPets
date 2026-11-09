package com.example.vetpets

import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object UsuarioDAO {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private const val COLECCION = "usuarios"

    // Helper para garantizar que la respuesta responda en el hilo principal de la UI
    private fun responderEnUI(onResult: (Int?) -> Unit, resultado: Int?) {
        Handler(Looper.getMainLooper()).post {
            onResult(resultado)
        }
    }

    private fun responderBooleanoEnUI(onResult: (Boolean) -> Unit, resultado: Boolean) {
        Handler(Looper.getMainLooper()).post {
            onResult(resultado)
        }
    }

    // =========================================================
    // VALIDAR USUARIO (PERMITE ENTRAR CON USUARIO O CORREO - SEGURO)
    // =========================================================
    fun validarUsuario(
        usuarioOEmail: String,
        contrasena: String,
        onResult: (Int?) -> Unit
    ) {
        val input = usuarioOEmail.trim()
        val password = contrasena.trim()

        if (input.isEmpty() || password.isEmpty()) {
            responderEnUI(onResult, null)
            return
        }

        val esCorreo = input.contains("@")

        if (esCorreo) {
            auth.signInWithEmailAndPassword(input, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        responderEnUI(onResult, 1)
                    } else {
                        responderEnUI(onResult, null)
                    }
                }
                .addOnFailureListener {
                    responderEnUI(onResult, null)
                }
        } else {
            db.collection(COLECCION)
                .whereEqualTo("usuario", input)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val doc = documents.documents[0]
                        val correoReal = doc.getString("correo") ?: ""
                        // Obtenemos el idUsuario manejando si está guardado como Long o Int o String en Firestore
                        val idUsuario = when (val idObj = doc.get("idUsuario")) {
                            is Long -> idObj.toInt()
                            is Int -> idObj
                            is String -> idObj.toIntOrNull() ?: 1
                            else -> 1
                        }

                        if (correoReal.isNotEmpty()) {
                            auth.signInWithEmailAndPassword(correoReal, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        responderEnUI(onResult, idUsuario)
                                    } else {
                                        responderEnUI(onResult, null)
                                    }
                                }
                                .addOnFailureListener {
                                    responderEnUI(onResult, null)
                                }
                        } else {
                            responderEnUI(onResult, null)
                        }
                    } else {
                        responderEnUI(onResult, null)
                    }
                }
                .addOnFailureListener {
                    responderEnUI(onResult, null)
                }
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
            .whereEqualTo("usuario", usuario.trim())
            .get()
            .addOnSuccessListener { documents ->
                responderBooleanoEnUI(onResult, !documents.isEmpty)
            }
            .addOnFailureListener {
                responderBooleanoEnUI(onResult, false)
            }
    }

    // =========================================================
    // REGISTRAR NUEVO USUARIO CON FIREBASE AUTH Y CORREO
    // =========================================================
    fun registrarUsuario(
        usuario: String,
        correo: String,
        contrasena: String,
        onResult: (Boolean) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(correo.trim(), contrasena.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                        if (emailTask.isSuccessful) {
                            val idGenerado = System.currentTimeMillis().toInt()
                            val nuevoUsuario = hashMapOf(
                                "idUsuario" to idGenerado,
                                "uid" to user.uid,
                                "usuario" to usuario.trim(),
                                "correo" to correo.trim(),
                                "contrasena" to contrasena.trim()
                            )

                            db.collection(COLECCION)
                                .document(user.uid)
                                .set(nuevoUsuario)
                                .addOnSuccessListener { responderBooleanoEnUI(onResult, true) }
                                .addOnFailureListener { responderBooleanoEnUI(onResult, false) }
                        } else {
                            responderBooleanoEnUI(onResult, false)
                        }
                    }
                } else {
                    responderBooleanoEnUI(onResult, false)
                }
            }
    }
}