package com.example.vetpets

import java.sql.Connection

object VeterinarioDAO {

    fun obtenerVeterinarios(): List<Veterinario> {

        val lista =
            mutableListOf<Veterinario>()

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return lista
            }

            val sql = """
                SELECT
                    IdVeterinario,
                    Nombre,
                    Especialidad,
                    Telefono,
                    Correo
                FROM Veterinarios
                ORDER BY Nombre
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            val resultado =
                statement.executeQuery()

            while (resultado.next()) {

                lista.add(

                    Veterinario(

                        idVeterinario =
                            resultado.getInt(
                                "IdVeterinario"
                            ),

                        nombre =
                            resultado.getString(
                                "Nombre"
                            ),

                        especialidad =
                            resultado.getString(
                                "Especialidad"
                            ) ?: "",

                        telefono =
                            resultado.getString(
                                "Telefono"
                            ) ?: "",

                        correo =
                            resultado.getString(
                                "Correo"
                            ) ?: ""
                    )
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

        return lista
    }


    fun registrarVeterinario(

        nombre: String,

        especialidad: String,

        telefono: String,

        correo: String

    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            val sql = """
                INSERT INTO Veterinarios
                (
                    Nombre,
                    Especialidad,
                    Telefono,
                    Correo
                )
                VALUES
                (
                    ?,
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
                especialidad
            )

            statement.setString(
                3,
                telefono
            )

            statement.setString(
                4,
                correo
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


    fun actualizarVeterinario(

        idVeterinario: Int,

        nombre: String,

        especialidad: String,

        telefono: String,

        correo: String

    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            val sql = """
                UPDATE Veterinarios
                SET
                    Nombre = ?,
                    Especialidad = ?,
                    Telefono = ?,
                    Correo = ?
                WHERE IdVeterinario = ?
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setString(
                1,
                nombre
            )

            statement.setString(
                2,
                especialidad
            )

            statement.setString(
                3,
                telefono
            )

            statement.setString(
                4,
                correo
            )

            statement.setInt(
                5,
                idVeterinario
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


    fun eliminarVeterinario(
        idVeterinario: Int
    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            val sql = """
                DELETE FROM Veterinarios
                WHERE IdVeterinario = ?
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setInt(
                1,
                idVeterinario
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