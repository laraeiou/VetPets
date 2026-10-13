package com.example.vetpets

import java.sql.Connection
import java.sql.DriverManager

object ConexionSQL {

    private const val URL =
        "jdbc:jtds:sqlserver://192.168.1.4:1433/VetPetDB"

    private const val USUARIO = "vetpets"
    private const val CLAVE = "VetPets123!"

    fun conectar(): Connection? {

        return try {

            Class.forName("net.sourceforge.jtds.jdbc.Driver")

            println("VETPETS: Driver JTDS cargado")
            println("VETPETS: Intentando conectar...")

            DriverManager.setLoginTimeout(10)

            val conexion = DriverManager.getConnection(
                URL,
                USUARIO,
                CLAVE
            )

            println("VETPETS: CONEXION EXITOSA")

            conexion

        } catch (e: Exception) {

            println("VETPETS: ERROR")
            println("VETPETS: TIPO = ${e.javaClass.name}")
            println("VETPETS: MENSAJE = ${e.message}")

            e.printStackTrace()

            throw e
        }
    }
}