package com.example.vetpets

import java.sql.Connection

object MedicamentoDAO {

    fun obtenerMedicamentos(
        idMascota: Int
    ): List<Medicamento> {

        val lista =
            mutableListOf<Medicamento>()

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return lista
            }

            val sql = """
                SELECT
                    IdMedicamento,
                    IdMascota,
                    NombreMedicamento,
                    Dosis,
                    Frecuencia,
                    FechaInicio,
                    FechaFin,
                    Indicaciones
                FROM Medicamentos
                WHERE IdMascota = ?
                ORDER BY FechaInicio DESC
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

                    Medicamento(

                        idMedicamento =
                            resultado.getInt(
                                "IdMedicamento"
                            ),

                        idMascota =
                            resultado.getInt(
                                "IdMascota"
                            ),

                        nombreMedicamento =
                            resultado.getString(
                                "NombreMedicamento"
                            ),

                        dosis =
                            resultado.getString(
                                "Dosis"
                            ) ?: "",

                        frecuencia =
                            resultado.getString(
                                "Frecuencia"
                            ) ?: "",

                        fechaInicio =
                            resultado.getString(
                                "FechaInicio"
                            ) ?: "",

                        fechaFin =
                            resultado.getString(
                                "FechaFin"
                            ) ?: "",

                        indicaciones =
                            resultado.getString(
                                "Indicaciones"
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


    fun registrarMedicamento(

        idMascota: Int,

        nombreMedicamento: String,

        dosis: String,

        frecuencia: String,

        fechaInicio: String,

        fechaFin: String,

        indicaciones: String

    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            val sql = """
                INSERT INTO Medicamentos
                (
                    IdMascota,
                    NombreMedicamento,
                    Dosis,
                    Frecuencia,
                    FechaInicio,
                    FechaFin,
                    Indicaciones
                )
                VALUES
                (
                    ?,
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
                idMascota
            )

            statement.setString(
                2,
                nombreMedicamento
            )

            statement.setString(
                3,
                dosis
            )

            statement.setString(
                4,
                frecuencia
            )

            if (fechaInicio.isBlank()) {

                statement.setNull(
                    5,
                    java.sql.Types.DATE
                )

            } else {

                statement.setString(
                    5,
                    fechaInicio
                )
            }

            if (fechaFin.isBlank()) {

                statement.setNull(
                    6,
                    java.sql.Types.DATE
                )

            } else {

                statement.setString(
                    6,
                    fechaFin
                )
            }

            statement.setString(
                7,
                indicaciones
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


    fun eliminarMedicamento(
        idMedicamento: Int
    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            val sql = """
                DELETE FROM Medicamentos
                WHERE IdMedicamento = ?
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setInt(
                1,
                idMedicamento
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