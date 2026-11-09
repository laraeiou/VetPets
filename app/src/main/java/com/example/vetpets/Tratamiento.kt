package com.example.vetpets

data class Tratamiento(
    val id: String = "",
    val idMascota: String = "",
    val nombreTratamiento: String = "",
    val fechaInicio: String = "",
    val comentario: String = "",
    val lugar: String = "Casa (A domicilio)",
    val veterinario: String = "",
    val recordatorioMinutos: Int = 15,
    val medicamentos: List<SubMedicamento> = emptyList(),
    val estado: String = "En proceso"
)