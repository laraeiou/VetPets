package com.example.vetpets

import java.sql.Connection

object MascotaDAO {

    fun obtenerMascotas(
        idUsuario: Int
    ): List<Mascota> {

        val listaMascotas =
            mutableListOf<Mascota>()

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return listaMascotas
            }

            val sql = """
                SELECT
                    IdMascota,
                    IdUsuario,
                    Nombre,
                    Especie,
                    Raza,
                    Sexo,
                    FechaNacimiento,
                    Foto
                FROM Mascotas
                WHERE IdUsuario = ?
                ORDER BY Nombre
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setInt(
                1,
                idUsuario
            )

            val resultado =
                statement.executeQuery()

            while (resultado.next()) {

                val mascota =
                    Mascota(

                        idMascota =
                            resultado.getInt(
                                "IdMascota"
                            ),

                        idUsuario =
                            resultado.getInt(
                                "IdUsuario"
                            ),

                        nombre =
                            resultado.getString(
                                "Nombre"
                            ),

                        especie =
                            resultado.getString(
                                "Especie"
                            ),

                        raza =
                            resultado.getString(
                                "Raza"
                            ),

                        sexo =
                            resultado.getString(
                                "Sexo"
                            ),

                        fechaNacimiento =
                            resultado.getString(
                                "FechaNacimiento"
                            ),

                        foto =
                            resultado.getString(
                                "Foto"
                            ) ?: ""
                    )

                listaMascotas.add(mascota)
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

        return listaMascotas
    }


    fun obtenerMascotaPorId(
        idMascota: Int
    ): Mascota? {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return null
            }

            val sql = """
                SELECT
                    IdMascota,
                    IdUsuario,
                    Nombre,
                    Especie,
                    Raza,
                    Sexo,
                    FechaNacimiento,
                    Foto
                FROM Mascotas
                WHERE IdMascota = ?
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setInt(
                1,
                idMascota
            )

            val resultado =
                statement.executeQuery()

            if (resultado.next()) {

                return Mascota(

                    idMascota =
                        resultado.getInt(
                            "IdMascota"
                        ),

                    idUsuario =
                        resultado.getInt(
                            "IdUsuario"
                        ),

                    nombre =
                        resultado.getString(
                            "Nombre"
                        ),

                    especie =
                        resultado.getString(
                            "Especie"
                        ),

                    raza =
                        resultado.getString(
                            "Raza"
                        ),

                    sexo =
                        resultado.getString(
                            "Sexo"
                        ),

                    fechaNacimiento =
                        resultado.getString(
                            "FechaNacimiento"
                        ),

                    foto =
                        resultado.getString(
                            "Foto"
                        ) ?: ""
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


    fun registrarMascota(

        idUsuario: Int,

        nombre: String,

        especie: String,

        raza: String,

        sexo: String,

        fechaNacimiento: String

    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            val sql = """
                INSERT INTO Mascotas
                (
                    IdUsuario,
                    Nombre,
                    Especie,
                    Raza,
                    Sexo,
                    FechaNacimiento
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?
                )
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setInt(
                1,
                idUsuario
            )

            statement.setString(
                2,
                nombre
            )

            statement.setString(
                3,
                especie
            )

            statement.setString(
                4,
                raza
            )

            statement.setString(
                5,
                sexo
            )

            statement.setString(
                6,
                fechaNacimiento
            )

            val filasAfectadas =
                statement.executeUpdate()

            return filasAfectadas > 0

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


    fun actualizarMascota(

        idMascota: Int,

        nombre: String,

        especie: String,

        raza: String,

        sexo: String,

        fechaNacimiento: String

    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            val sql = """
                UPDATE Mascotas
                SET
                    Nombre = ?,
                    Especie = ?,
                    Raza = ?,
                    Sexo = ?,
                    FechaNacimiento = ?
                WHERE IdMascota = ?
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setString(
                1,
                nombre
            )

            statement.setString(
                2,
                especie
            )

            statement.setString(
                3,
                raza
            )

            statement.setString(
                4,
                sexo
            )

            statement.setString(
                5,
                fechaNacimiento
            )

            statement.setInt(
                6,
                idMascota
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


    fun eliminarMascota(
        idMascota: Int
    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            val sql = """
                DELETE FROM Mascotas
                WHERE IdMascota = ?
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setInt(
                1,
                idMascota
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