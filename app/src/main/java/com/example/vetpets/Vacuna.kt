package com.example.vetpets

data class Vacuna(
    val idVacuna: String,
    val idMascota: String,
    val nombreVacuna: String,
    val fechaAplicacion: String,
    val proximaDosis: String,
    val observaciones: String
)