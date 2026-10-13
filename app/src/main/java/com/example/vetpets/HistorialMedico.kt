package com.example.vetpets

data class HistorialMedico(

    val idHistorial: Int,

    val idMascota: Int,

    val fecha: String,

    val motivo: String,

    val diagnostico: String,

    val tratamiento: String,

    val observaciones: String,

    val idVeterinario: Int?,

    val nombreVeterinario: String
)