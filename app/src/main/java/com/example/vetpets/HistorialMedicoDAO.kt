package com.example.vetpets

import java.sql.Connection
import java.sql.Types

object HistorialMedicoDAO {

    // =========================================================
    // OBTENER TODO EL HISTORIAL DE UNA MASCOTA
    // =========================================================

    fun obtenerHistorial(
        idMascota: Int
    ): List<HistorialMedico> {

        val lista =
            mutableListOf<HistorialMedico>()

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return lista
            }

            val sql = """
                SELECT
                    h.IdHistorial,
                    h.IdMascota,
                    h.Fecha,
                    h.Motivo,
                    h.Diagnostico,
                    h.Tratamiento,
                    h.Observaciones,
                    h.IdVeterinario,
                    ISNULL(v.Nombre, 'No asignado') AS NombreVeterinario
                FROM HistorialMedico h
                LEFT JOIN Veterinarios v
                    ON h.IdVeterinario = v.IdVeterinario
                WHERE h.IdMascota = ?
                ORDER BY h.Fecha DESC
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

                val historial =
                    HistorialMedico(

                        idHistorial =
                            resultado.getInt(
                                "IdHistorial"
                            ),

                        idMascota =
                            resultado.getInt(
                                "IdMascota"
                            ),

                        fecha =
                            resultado.getString(
                                "Fecha"
                            ),

                        motivo =
                            resultado.getString(
                                "Motivo"
                            ),

                        diagnostico =
                            resultado.getString(
                                "Diagnostico"
                            ),

                        tratamiento =
                            resultado.getString(
                                "Tratamiento"
                            ),

                        observaciones =
                            resultado.getString(
                                "Observaciones"
                            ) ?: "",

                        idVeterinario =
                            resultado.getObject(
                                "IdVeterinario"
                            ) as? Int,

                        nombreVeterinario =
                            resultado.getString(
                                "NombreVeterinario"
                            )
                    )

                lista.add(
                    historial
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


    // =========================================================
    // OBTENER UNA CONSULTA POR ID
    // =========================================================

    fun obtenerHistorialPorId(
        idHistorial: Int
    ): HistorialMedico? {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return null
            }

            val sql = """
                SELECT
                    h.IdHistorial,
                    h.IdMascota,
                    h.Fecha,
                    h.Motivo,
                    h.Diagnostico,
                    h.Tratamiento,
                    h.Observaciones,
                    h.IdVeterinario,
                    ISNULL(v.Nombre, 'No asignado') AS NombreVeterinario
                FROM HistorialMedico h
                LEFT JOIN Veterinarios v
                    ON h.IdVeterinario = v.IdVeterinario
                WHERE h.IdHistorial = ?
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setInt(
                1,
                idHistorial
            )

            val resultado =
                statement.executeQuery()

            if (resultado.next()) {

                return HistorialMedico(

                    idHistorial =
                        resultado.getInt(
                            "IdHistorial"
                        ),

                    idMascota =
                        resultado.getInt(
                            "IdMascota"
                        ),

                    fecha =
                        resultado.getString(
                            "Fecha"
                        ),

                    motivo =
                        resultado.getString(
                            "Motivo"
                        ),

                    diagnostico =
                        resultado.getString(
                            "Diagnostico"
                        ),

                    tratamiento =
                        resultado.getString(
                            "Tratamiento"
                        ),

                    observaciones =
                        resultado.getString(
                            "Observaciones"
                        ) ?: "",

                    idVeterinario =
                        resultado.getObject(
                            "IdVeterinario"
                        ) as? Int,

                    nombreVeterinario =
                        resultado.getString(
                            "NombreVeterinario"
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

        return null
    }


    // =========================================================
    // REGISTRAR CONSULTA MÉDICA
    // =========================================================

    fun registrarHistorial(

        idMascota: Int,

        fecha: String,

        motivo: String,

        diagnostico: String,

        tratamiento: String,

        observaciones: String,

        idVeterinario: Int?

    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            val sql = """
                INSERT INTO HistorialMedico
                (
                    IdMascota,
                    Fecha,
                    Motivo,
                    Diagnostico,
                    Tratamiento,
                    Observaciones,
                    IdVeterinario
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
                fecha
            )

            statement.setString(
                3,
                motivo
            )

            statement.setString(
                4,
                diagnostico
            )

            statement.setString(
                5,
                tratamiento
            )

            statement.setString(
                6,
                observaciones
            )

            if (idVeterinario != null) {

                statement.setInt(
                    7,
                    idVeterinario
                )

            } else {

                statement.setNull(
                    7,
                    Types.INTEGER
                )
            }

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


    // =========================================================
    // ACTUALIZAR CONSULTA MÉDICA
    // =========================================================

    fun actualizarHistorial(

        idHistorial: Int,

        fecha: String,

        motivo: String,

        diagnostico: String,

        tratamiento: String,

        observaciones: String,

        idVeterinario: Int?

    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            val sql = """
                UPDATE HistorialMedico
                SET
                    Fecha = ?,
                    Motivo = ?,
                    Diagnostico = ?,
                    Tratamiento = ?,
                    Observaciones = ?,
                    IdVeterinario = ?
                WHERE IdHistorial = ?
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setString(
                1,
                fecha
            )

            statement.setString(
                2,
                motivo
            )

            statement.setString(
                3,
                diagnostico
            )

            statement.setString(
                4,
                tratamiento
            )

            statement.setString(
                5,
                observaciones
            )

            if (idVeterinario != null) {

                statement.setInt(
                    6,
                    idVeterinario
                )

            } else {

                statement.setNull(
                    6,
                    Types.INTEGER
                )
            }

            statement.setInt(
                7,
                idHistorial
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


    // =========================================================
    // ELIMINAR CONSULTA MÉDICA
    // =========================================================

    fun eliminarHistorial(
        idHistorial: Int
    ): Boolean {

        var conexion: Connection? = null

        try {

            conexion =
                ConexionSQL.conectar()

            if (conexion == null) {
                return false
            }

            val sql = """
                DELETE FROM HistorialMedico
                WHERE IdHistorial = ?
            """.trimIndent()

            val statement =
                conexion.prepareStatement(sql)

            statement.setInt(
                1,
                idHistorial
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
}