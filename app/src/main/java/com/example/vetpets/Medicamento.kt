package com.example.vetpets

data class Medicamento(

    val idMedicamento: Int,

    val idMascota: Int,

    val nombreMedicamento: String,

    val dosis: String,

    val frecuencia: String,

    val fechaInicio: String,

    val fechaFin: String,

    val indicaciones: String
)