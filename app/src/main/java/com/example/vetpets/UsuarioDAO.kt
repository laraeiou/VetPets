package com.example.vetpets

import java.sql.Connection

object UsuarioDAO {

    fun validarUsuario(
        usuario: String,
        contrasena: String
    ): Int? {

        var conexion: Connection? = null

        try {

            conexion = ConexionSQL.conectar()

            if (conexion == null) {
                return null
            }

            val sql = """
                SELECT IdUsuario
                FROM Usuarios
                WHERE Usuario = ?
                AND Contrasena = ?
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setString(
                1,
                usuario
            )

            statement.setString(
                2,
                contrasena
            )

            val resultado =
                statement.executeQuery()

            if (resultado.next()) {

                return resultado.getInt(
                    "IdUsuario"
                )
            }

        } catch (e: Exception) {

            e.printStackTrace()

        } finally {

            try {
                conexion?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return null
    }


    fun usuarioExiste(
        usuario: String
    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            val sql = """
                SELECT COUNT(*)
                FROM Usuarios
                WHERE Usuario = ?
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setString(
                1,
                usuario
            )

            val resultado =
                statement.executeQuery()

            if (resultado.next()) {

                return resultado.getInt(1) > 0
            }

        } catch (e: Exception) {

            e.printStackTrace()

        } finally {

            try {
                conexion?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return false
    }


    fun registrarUsuario(

        nombre: String,

        usuario: String,

        contrasena: String

    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            if (usuarioExiste(usuario)) {
                return false
            }

            val sql = """
                INSERT INTO Usuarios
                (
                    Nombre,
                    Usuario,
                    Contrasena
                )
                VALUES
                (
                    ?,
                    ?,
                    ?
                )
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setString(
                1,
                nombre
            )

            statement.setString(
                2,
                usuario
            )

            statement.setString(
                3,
                contrasena
            )

            return statement.executeUpdate() > 0

        } catch (e: Exception) {

            e.printStackTrace()

        } finally {

            try {
                conexion?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return false
    }
}