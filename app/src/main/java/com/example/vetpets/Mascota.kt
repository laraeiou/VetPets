package com.example.vetpets

data class Mascota(

    val idMascota: Int,

    val idUsuario: Int,

    val nombre: String,

    val especie: String,

    val raza: String,

    val sexo: String,

    val fechaNacimiento: String,

    val foto: String
)