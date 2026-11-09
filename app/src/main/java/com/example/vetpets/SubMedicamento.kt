package com.example.vetpets

data class SubMedicamento(
    val tipo: String = "Medicamento",
    val via: String = "Oral",
    val nombre: String = "",
    val dosis: String = "",
    val unidad: String = "ml",
    val frecuenciaHoras: String = "8",
    val hora: String = "8:00 AM"
)