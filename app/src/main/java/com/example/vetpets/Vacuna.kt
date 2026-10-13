package com.example.vetpets

data class Vacuna(

    val idVacuna: Int,

    val idMascota: Int,

    val nombreVacuna: String,

    val fechaAplicacion: String,

    val proximaDosis: String,

    val observaciones: String
)