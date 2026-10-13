package com.example.vetpets

import java.sql.Connection

object VacunaDAO {

    fun obtenerVacunas(
        idMascota: Int
    ): List<Vacuna> {

        val lista =
            mutableListOf<Vacuna>()

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return lista
            }

            val sql = """
                SELECT
                    IdVacuna,
                    IdMascota,
                    NombreVacuna,
                    FechaAplicacion,
                    ProximaDosis,
                    Observaciones
                FROM Vacunas
                WHERE IdMascota = ?
                ORDER BY FechaAplicacion DESC
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setInt(
                1,
                idMascota
            )

            val resultado =
                statement.executeQuery()

            while (resultado.next()) {

                lista.add(

                    Vacuna(

                        idVacuna =
                            resultado.getInt(
                                "IdVacuna"
                            ),

                        idMascota =
                            resultado.getInt(
                                "IdMascota"
                            ),

                        nombreVacuna =
                            resultado.getString(
                                "NombreVacuna"
                            ),

                        fechaAplicacion =
                            resultado.getString(
                                "FechaAplicacion"
                            ),

                        proximaDosis =
                            resultado.getString(
                                "ProximaDosis"
                            ) ?: "",

                        observaciones =
                            resultado.getString(
                                "Observaciones"
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


    fun registrarVacuna(

        idMascota: Int,

        nombreVacuna: String,

        fechaAplicacion: String,

        proximaDosis: String,

        observaciones: String

    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            val sql = """
                INSERT INTO Vacunas
                (
                    IdMascota,
                    NombreVacuna,
                    FechaAplicacion,
                    ProximaDosis,
                    Observaciones
                )
                VALUES
                (
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
                idMascota
            )

            statement.setString(
                2,
                nombreVacuna
            )

            statement.setString(
                3,
                fechaAplicacion
            )

            if (proximaDosis.isBlank()) {

                statement.setNull(
                    4,
                    java.sql.Types.DATE
                )

            } else {

                statement.setString(
                    4,
                    proximaDosis
                )
            }

            statement.setString(
                5,
                observaciones
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


    fun eliminarVacuna(
        idVacuna: Int
    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            val sql = """
                DELETE FROM Vacunas
                WHERE IdVacuna = ?
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setInt(
                1,
                idVacuna
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