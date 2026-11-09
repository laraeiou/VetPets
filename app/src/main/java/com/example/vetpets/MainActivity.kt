package com.example.vetpets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vetpets.ui.theme.VetPetsTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.foundation.border
import androidx.compose.material3.FabPosition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.CircularProgressIndicator
import android.app.DatePickerDialog
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import com.google.apps.card.v1.Divider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import android.app.DatePickerDialog as AndroidDatePickerDialog
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VetPetsTheme {
                AppVetPets()
            }
        }
    }

    @Composable
    fun AppVetPets() {
        var pantallaActual by remember { mutableStateOf("inicio") }
        var idUsuario by remember { mutableStateOf(0) }
        var idMascotaSeleccionada by remember { mutableStateOf<String>("") }
        var idHistorialSeleccionado by remember { mutableStateOf("") }

        when (pantallaActual) {
            "inicio" -> {
                PantallaInicio(
                    irLogin = { pantallaActual = "login" },
                    irRegistro = { pantallaActual = "registro" }
                )
            }

            "login" -> {
                PantallaLogin(
                    iniciarSesionCorrecto = { id: Int ->
                        idUsuario = id
                        pantallaActual = "principal"
                    },
                    volver = { pantallaActual = "inicio" },
                    irRegistro = { pantallaActual = "registro" }
                )
            }

            "registro" -> {
                FlujoRegistroCompleto(
                    onFinalizarRegistro = { pantallaActual = "principal" },
                    onVolverLogin = { pantallaActual = "login" }
                )
            }

            "principal" -> {
                PantallaPrincipal(
                    irMascotas = { pantallaActual = "mascotas" },
                    irVeterinarios = { pantallaActual = "agenda" },
                    irPerfil = { pantallaActual = "perfil" },
                    irHistoriales = { pantallaActual = "historial" },
                    irTratamientos = { pantallaActual = "tratamientos" },
                    cerrarSesion = {
                        idUsuario = 0
                        idMascotaSeleccionada = ""
                        pantallaActual = "inicio"
                    }
                )
            }

            "perfil" -> {
                PantallaPerfil(
                    irInicio = { pantallaActual = "principal" },
                    irMascotas = { pantallaActual = "mascotas" },
                    irAgenda = { pantallaActual = "agenda" },
                    cerrarSesion = {
                        idUsuario = 0
                        idMascotaSeleccionada = ""
                        pantallaActual = "inicio"
                    }
                )
            }

            "mascotas" -> {
                PantallaMascotas(
                    idUsuario = idUsuario,
                    seleccionarMascota = { id ->
                        idMascotaSeleccionada = id
                        pantallaActual = "mascotaSeleccionada"
                    },
                    registrarMascota = { pantallaActual = "registrarMascota" },
                    volver = { pantallaActual = "principal" }
                )
            }

            "registrarMascota" -> {
                FlujoCrearMascota(
                    idUsuario = idUsuario,
                    alFinalizar = { pantallaActual = "mascotas" },
                    alCancelar = { pantallaActual = "mascotas" }
                )
            }

            "editarMascota" -> {
                PantallaEditarMascota(
                    idMascota = idMascotaSeleccionada,
                    mascotaActualizada = { pantallaActual = "mascotaSeleccionada" },
                    volver = { pantallaActual = "mascotaSeleccionada" }
                )
            }

            "mascotaSeleccionada" -> {
                PantallaMascotaSeleccionada(
                    idMascota = idMascotaSeleccionada,
                    irHistorial = { pantallaActual = "historial" },
                    editarMascota = { pantallaActual = "editarMascota" },
                    eliminarMascota = { pantallaActual = "mascotas" },
                    irVacunas = { pantallaActual = "vacunas" },
                    irMedicamentos = { pantallaActual = "medicamentos" },
                    volver = { pantallaActual = "mascotas" }
                )
            }

            "historial" -> {
                PantallaHistorialMedico(
                    idMascota = idMascotaSeleccionada,
                    registrarHistorial = { pantallaActual = "registrarHistorial" },
                    editarHistorial = { id: String ->
                        idHistorialSeleccionado = id
                        pantallaActual = "editarHistorial"
                    },
                    volver = { pantallaActual = "mascotaSeleccionada" }
                )
            }

            "registrarHistorial" -> {
                PantallaRegistrarHistorial(
                    idMascota = idMascotaSeleccionada,
                    historialRegistrado = { pantallaActual = "historial" },
                    volver = { pantallaActual = "historial" }
                )
            }

            "editarHistorial" -> {
                PantallaEditarHistorial(
                    idHistorial = idHistorialSeleccionado,
                    historialActualizado = { pantallaActual = "historial" },
                    volver = { pantallaActual = "historial" }
                )
            }

            "agenda" -> {
                PantallaAgenda(
                    irInicio = { pantallaActual = "principal" },
                    irMascotas = { pantallaActual = "mascotas" },
                    irPerfil = { pantallaActual = "perfil" },
                    volver = { pantallaActual = "principal" },
                    idMascota = idMascotaSeleccionada,
                    irCrearTratamiento = { pantallaActual = "crearTratamiento" }
                )
            }

            "vacunas" -> {
                PantallaVacunas(
                    idMascota = idMascotaSeleccionada,
                    volver = { pantallaActual = "mascotaSeleccionada" }
                )
            }

            "medicamentos" -> {
                PantallaMedicamentos(
                    idMascota = idMascotaSeleccionada,
                    volver = { pantallaActual = "mascotaSeleccionada" }
                )
            }

            "tratamientos" -> {
                PantallaTratamientos(
                    idMascota = idMascotaSeleccionada,
                    irCrearTratamiento = { pantallaActual = "crearTratamiento" },
                    volver = { pantallaActual = "principal" }
                )
            }

            "crearTratamiento" -> {
                PantallaCrearTratamiento(
                    idMascota = idMascotaSeleccionada,
                    tratamientoCreado = { pantallaActual = "tratamientos" },
                    volver = { pantallaActual = "tratamientos" }
                )
            }
        }
    }


    // ==========================================
    // PANTALLA PRINCIPAL
    // ==========================================
    @Composable
    fun PantallaPrincipal(
        irMascotas: () -> Unit,
        irVeterinarios: () -> Unit,
        irPerfil: () -> Unit,
        cerrarSesion: () -> Unit,
        irHistoriales: () -> Unit = {},
        irTratamientos: () -> Unit = {},
        irPeluqueria: () -> Unit = {}
    ) {
        val azulEncabezado = Color(0xFF00B4D8)
        val textoOscuro = Color(0xFF1E293B)
        val grisTexto = Color(0xFF64748B)

        var mostrarDialogoNotificaciones by remember { mutableStateOf(false) }

        // Usamos una lista simple de objetos de datos seguros para evitar errores de tipo
        data class MascotaSimple(val nombre: String, val tipo: String, val genero: String)
        var listaMascotas by remember { mutableStateOf(listOf<MascotaSimple>()) }

        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        val uidActual = auth.currentUser?.uid ?: ""

        LaunchedEffect(uidActual) {
            if (uidActual.isNotEmpty()) {
                db.collection("mascotas")
                    .whereEqualTo("uidUsuario", uidActual)
                    .get()
                    .addOnSuccessListener { result ->
                        val temporal = mutableListOf<MascotaSimple>()
                        for (document in result) {
                            val nombre = document.getString("nombre") ?: "Sin nombre"
                            val tipo = document.getString("tipo") ?: "Mascota"
                            val genero = document.getString("genero") ?: "Desconocido"
                            temporal.add(MascotaSimple(nombre, tipo, genero))
                        }
                        listaMascotas = temporal
                    }
            }
        }

        Scaffold(
            containerColor = Color(0xFFF8FAFC),
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = true,
                        onClick = { },
                        icon = { Text("🏠", fontSize = 18.sp) },
                        label = { Text("Inicio", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = azulEncabezado) }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = irMascotas,
                        icon = { Text("🐾", fontSize = 18.sp) },
                        label = { Text("Mascotas", fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = irVeterinarios,
                        icon = { Text("📅", fontSize = 18.sp) },
                        label = { Text("Agenda", fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = irPerfil,
                        icon = { Text("👤", fontSize = 18.sp) },
                        label = { Text("Perfil", fontSize = 11.sp) }
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF8FAFC))
                    .verticalScroll(rememberScrollState())
            ) {
                // --- ENCABEZADO CELESTE HASTA ARRIBA ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(azulEncabezado)
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Vetpets",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        IconButton(
                            onClick = { mostrarDialogoNotificaciones = true },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Text("🔔", fontSize = 20.sp)
                        }
                    }
                }

                // --- CONTENIDO INFERIOR ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(4.dp))

                    // --- SERVICIOS EN CUADROS GRANDES ---
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TarjetaCuadroServicio(
                                titulo = "Mis mascotas",
                                icono = "🐾",
                                modifier = Modifier.weight(1f),
                                onClick = irMascotas
                            )
                            TarjetaCuadroServicio(
                                titulo = "Historial médico",
                                icono = "📁",
                                modifier = Modifier.weight(1f),
                                onClick = irHistoriales
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TarjetaCuadroServicio(
                                titulo = "Tratamientos",
                                icono = "💊",
                                modifier = Modifier.weight(1f),
                                onClick = irTratamientos
                            )
                            TarjetaCuadroServicio(
                                titulo = "Citas / Veterinarios",
                                icono = "📅",
                                modifier = Modifier.weight(1f),
                                onClick = irVeterinarios
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- SECCIÓN MIS MASCOTAS ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mis Mascotas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textoOscuro
                        )
                        Text(
                            text = "Ver todas >",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = azulEncabezado,
                            modifier = Modifier.clickable { irMascotas() }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // --- RENDERIZADO DINÁMICO DE MASCOTAS ---
                    if (listaMascotas.isEmpty()) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("🐶", fontSize = 24.sp)
                                    Text("🐱", fontSize = 24.sp)
                                    Text("🐰", fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No hay mascotas creadas",
                                    fontSize = 14.sp,
                                    color = grisTexto,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = irMascotas,
                                    colors = ButtonDefaults.buttonColors(containerColor = azulEncabezado),
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier.height(40.dp)
                                ) {
                                    Text("+ Agregar mascota", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            listaMascotas.forEach { mascota ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(45.dp)
                                                    .background(Color(0xFFE0F2FE), CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = when {
                                                        mascota.tipo.lowercase().contains("gato") -> "🐱"
                                                        mascota.tipo.lowercase().contains("conejo") -> "🐰"
                                                        else -> "🐶"
                                                    },
                                                    fontSize = 22.sp
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(14.dp))
                                            Column {
                                                Text(
                                                    text = mascota.nombre,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = textoOscuro
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "${mascota.tipo} • ${mascota.genero}",
                                                    fontSize = 13.sp,
                                                    color = grisTexto
                                                )
                                            }
                                        }
                                        Text("›", fontSize = 20.sp, color = grisTexto, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }

        // --- MODAL DE NOTIFICACIONES ---
        if (mostrarDialogoNotificaciones) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoNotificaciones = false },
                title = {
                    Text(text = "Notificaciones", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                text = {
                    Text(
                        text = "No tienes notificaciones nuevas por el momento. ¡Todo va excelente con tus mascotas!",
                        fontSize = 14.sp,
                        color = grisTexto
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { mostrarDialogoNotificaciones = false },
                        colors = ButtonDefaults.buttonColors(containerColor = azulEncabezado),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Entendido", color = Color.White)
                    }
                },
                shape = RoundedCornerShape(20.dp),
                containerColor = Color.White
            )
        }
    }


    @Composable
    fun TarjetaCuadroServicio(titulo: String, icono: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
        Card(
            onClick = onClick,
            colors = cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = cardElevation(defaultElevation = 1.dp),
            modifier = modifier.height(100.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = icono, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = titulo,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            }
        }
    }


    @Composable
    fun ItemServicio(titulo: String, icono: String, colorFondo: Color, onClick: () -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onClick() }
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(colorFondo, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icono, fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = titulo, fontSize = 12.sp, color = Color(0xFF1E293B), fontWeight = FontWeight.Medium)
        }
    }

    @Composable
    fun ItemPaso(completado: Boolean, texto: String, tieneFlecha: Boolean = false) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            if (completado) Color(0xFF00B4D8) else Color(0xFFCBD5E1),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (completado) "✓" else "", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = texto,
                    fontSize = 14.sp,
                    color = if (completado) Color(0xFF64748B) else Color(0xFF1E293B),
                    fontWeight = FontWeight.Medium
                )
            }
            if (tieneFlecha) {
                Text("›", fontSize = 18.sp, color = Color(0xFF64748B))
            }
        }
    }




    // ===================
    // PANTALLA MASCOTAS
    // ===================
    @Composable
    fun PantallaMascotas(
        idUsuario: Int,
        seleccionarMascota: (String) -> Unit,
        registrarMascota: () -> Unit,
        volver: () -> Unit
    ) {
        val azulEncabezado = Color(0xFF00B4D8)
        val textoOscuro = Color(0xFF1E293B)
        val grisTexto = Color(0xFF64748B)

        var textoBusqueda by remember { mutableStateOf("") }
        var pestañaSeleccionada by remember { mutableStateOf(0) } // 0: Mis mascotas, 1: Otras mas..., 2: Invitaciones

        // --- ESTADO Y CARGA DE MASCOTAS DESDE FIRESTORE ---
        data class MascotaItem(val idMascota: String, val nombre: String, val tipo: String, val genero: String)
        var listaMascotas by remember { mutableStateOf(listOf<MascotaItem>()) }

        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        val uidActual = auth.currentUser?.uid ?: ""

        LaunchedEffect(uidActual) {
            if (uidActual.isNotEmpty()) {
                db.collection("mascotas")
                    .whereEqualTo("uidUsuario", uidActual)
                    .get()
                    .addOnSuccessListener { result ->
                        val temporal = mutableListOf<MascotaItem>()
                        for (document in result) {
                            val id = document.getString("idMascota") ?: ""
                            val nombre = document.getString("nombre") ?: "Sin nombre"
                            val tipo = document.getString("tipo") ?: "Mascota"
                            val genero = document.getString("genero") ?: "Desconocido"
                            temporal.add(MascotaItem(id, nombre, tipo, genero))
                        }
                        listaMascotas = temporal
                    }
            }
        }

        // Filtro de búsqueda opcional
        val mascotasFiltradas = if (textoBusqueda.isBlank()) {
            listaMascotas
        } else {
            listaMascotas.filter { it.nombre.contains(textoBusqueda, ignoreCase = true) }
        }

        Scaffold(
            bottomBar = {
                // --- BARRA DE NAVEGACIÓN INFERIOR FIJA ---
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = false,
                        onClick = volver,
                        icon = { Text("🏠", fontSize = 18.sp) },
                        label = { Text("Inicio", fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = true,
                        onClick = { },
                        icon = { Text("🐾", fontSize = 18.sp) },
                        label = { Text("Mascotas", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = azulEncabezado) }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = { Text("📅", fontSize = 18.sp) },
                        label = { Text("Agenda", fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = { Text("👤", fontSize = 18.sp) },
                        label = { Text("Perfil", fontSize = 11.sp) }
                    )
                }
            },
            floatingActionButton = {
                // --- BOTÓN FLOTANTE CON EL SÍMBOLO + ---
                FloatingActionButton(
                    onClick = registrarMascota,
                    containerColor = azulEncabezado,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Text("+", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8FAFC))
                    .padding(paddingValues)
            ) {
                // --- ENCABEZADO SUPERIOR AZUL ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(azulEncabezado)
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "‹",
                                fontSize = 32.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { volver() }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Mascotas",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(text = "⋮", fontSize = 20.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // --- BARRA DE BÚSQUEDA Y FILTRO ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = textoBusqueda,
                            onValueChange = { textoBusqueda = it },
                            placeholder = { Text("Busca tu mascota", color = grisTexto.copy(alpha = 0.6f)) },
                            leadingIcon = { Text("🔍", fontSize = 16.sp) },
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                focusedBorderColor = azulEncabezado
                            )
                        )

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color.White, RoundedCornerShape(24.dp))
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⚙️", fontSize = 18.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- PESTAÑAS (Mis mascotas, Otras mas..., Invitaciones) ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Pestaña Mis mascotas dinamica (ej. cant/5)
                        Surface(
                            onClick = { pestañaSeleccionada = 0 },
                            color = if (pestañaSeleccionada == 0) Color(0xFFE0F2FE) else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Mis mascotas",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (pestañaSeleccionada == 0) azulEncabezado else grisTexto
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "${listaMascotas.size}/5",
                                    fontSize = 11.sp,
                                    color = if (pestañaSeleccionada == 0) azulEncabezado else grisTexto
                                )
                            }
                        }

                        // Pestaña Otras mas... 0/2
                        Surface(
                            onClick = { pestañaSeleccionada = 1 },
                            color = if (pestañaSeleccionada == 1) Color(0xFFE0F2FE) else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Otras mas...",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (pestañaSeleccionada == 1) azulEncabezado else grisTexto
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "0/2",
                                    fontSize = 11.sp,
                                    color = if (pestañaSeleccionada == 1) azulEncabezado else grisTexto
                                )
                            }
                        }

                        // Pestaña Invitaciones
                        Surface(
                            onClick = { pestañaSeleccionada = 2 },
                            color = if (pestañaSeleccionada == 2) Color(0xFFE0F2FE) else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("✉️", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "Invitaciones",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (pestañaSeleccionada == 2) azulEncabezado else azulEncabezado
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // --- CONTENIDO: LISTA O MENSAJE VACÍO ---
                    if (pestañaSeleccionada == 0) {
                        if (mascotasFiltradas.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 80.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No hay mascotas creadas",
                                    fontSize = 15.sp,
                                    color = grisTexto,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        } else {
                            // Lista vertical de mascotas registradas
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                mascotasFiltradas.forEach { mascota ->
                                    Card(
                                        onClick = {
                                            // Opcional: si deseas manejar la selección por ID numérico o pasar el objeto
                                            seleccionarMascota(mascota.idMascota)
                                        },
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        shape = RoundedCornerShape(16.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(45.dp)
                                                        .background(Color(0xFFE0F2FE), CircleShape),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = when {
                                                            mascota.tipo.lowercase().contains("gato") -> "🐱"
                                                            mascota.tipo.lowercase().contains("conejo") -> "🐰"
                                                            else -> "🐶"
                                                        },
                                                        fontSize = 22.sp
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(14.dp))
                                                Column {
                                                    Text(
                                                        text = mascota.nombre,
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = textoOscuro
                                                    )
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = "${mascota.tipo} • ${mascota.genero}",
                                                        fontSize = 13.sp,
                                                        color = grisTexto
                                                    )
                                                }
                                            }
                                            Text("›", fontSize = 20.sp, color = grisTexto, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(80.dp)) // Espacio para que el botón flotante no tape el último elemento
                            }
                        }
                    } else if (pestañaSeleccionada == 1) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 80.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No hay otras mascotas compartidas",
                                fontSize = 15.sp,
                                color = grisTexto,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 80.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No tienes invitaciones pendientes",
                                fontSize = 15.sp,
                                color = grisTexto,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }



    // ==========================================
    // FLUJO CREAR MASCOTA
    // ==========================================
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FlujoCrearMascota(
        idUsuario: Int,
        alFinalizar: () -> Unit = {},
        alCancelar: () -> Unit = {}
    ) {
        val azulEncabezado = Color(0xFF00B4D8)
        val grisTexto = Color(0xFF64748B)
        val textoOscuro = Color(0xFF1E293B)
        val context = LocalContext.current

        // --- ESTADOS DEL FORMULARIO ---
        var fotoUri by remember { mutableStateOf("") }
        var nombre by remember { mutableStateOf("") }
        var alias by remember { mutableStateOf("") }
        var tipoMascota by remember { mutableStateOf("Perro") }
        var raza by remember { mutableStateOf("") }
        var fechaNacimiento by remember { mutableStateOf("Seleccionar fecha") }
        var genero by remember { mutableStateOf("Macho") }
        var padre by remember { mutableStateOf("") }
        var madre by remember { mutableStateOf("") }

        var cargando by remember { mutableStateOf(false) }
        var mensajeError by remember { mutableStateOf("") }

        // Desplegables
        var expandirTipos by remember { mutableStateOf(false) }
        var expandirGenero by remember { mutableStateOf(false) }

        val listaTipos = listOf("Perro", "Gato", "Conejo", "Hámster", "Reptil", "Pájaro", "Pez", "Anfibio", "Hurón")
        val listaGeneros = listOf("Macho", "Hembra")

        // Selector de foto
        val launcherFoto = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: android.net.Uri? ->
            if (uri != null) {
                fotoUri = uri.toString()
            }
        }

        // Calendario real de Android
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val mesReal = month + 1
                fechaNacimiento = "$dayOfMonth/$mesReal/$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Crea tu mascota", color = Color.White, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = azulEncabezado),
                    navigationIcon = {
                        IconButton(onClick = alCancelar) {
                            Text("‹", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF8FAFC))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- FOTO DE PERFIL ---
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0F2FE))
                        .clickable { launcherFoto.launch("image/*") }
                ) {
                    if (fotoUri.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(fotoUri),
                            contentDescription = "Foto Mascota",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when {
                                    tipoMascota.contains("Gato", ignoreCase = true) -> "🐱"
                                    tipoMascota.contains("Conejo", ignoreCase = true) -> "🐰"
                                    tipoMascota.contains("Anfibio", ignoreCase = true) -> "🐸"
                                    else -> "🐶"
                                },
                                fontSize = 40.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(azulEncabezado, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📷", fontSize = 14.sp)
                    }
                }

                Text(
                    text = "Agregar foto",
                    fontSize = 12.sp,
                    color = azulEncabezado,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // --- CAMPOS ---
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = alias,
                    onValueChange = { alias = it },
                    label = { Text("@usuario de la mascota") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tipo de mascota
                ExposedDropdownMenuBox(
                    expanded = expandirTipos,
                    onExpandedChange = { expandirTipos = !expandirTipos }
                ) {
                    OutlinedTextField(
                        value = tipoMascota,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de mascota *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirTipos) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandirTipos,
                        onDismissRequest = { expandirTipos = false }
                    ) {
                        listaTipos.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    tipoMascota = opcion
                                    expandirTipos = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Raza (Con placeholder Desconocido)
                OutlinedTextField(
                    value = raza,
                    onValueChange = { raza = it },
                    label = { Text("Raza") },
                    placeholder = { Text("Desconocido", color = grisTexto.copy(alpha = 0.5f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Género
                ExposedDropdownMenuBox(
                    expanded = expandirGenero,
                    onExpandedChange = { expandirGenero = !expandirGenero }
                ) {
                    OutlinedTextField(
                        value = genero,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Género") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirGenero) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandirGenero,
                        onDismissRequest = { expandirGenero = false }
                    ) {
                        listaGeneros.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    genero = opcion
                                    expandirGenero = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Fecha de Nacimiento (Abre el calendario al pulsar)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() }
                ) {
                    OutlinedTextField(
                        value = fechaNacimiento,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        label = { Text("Fecha de nacimiento") },
                        trailingIcon = { Text("📅", fontSize = 16.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = textoOscuro,
                            disabledBorderColor = Color(0xFFE2E8F0),
                            disabledLabelColor = grisTexto,
                            disabledTrailingIconColor = grisTexto
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Padre
                OutlinedTextField(
                    value = padre,
                    onValueChange = { padre = it },
                    label = { Text("Padre") },
                    placeholder = { Text("Desconocido", color = grisTexto.copy(alpha = 0.5f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Madre
                OutlinedTextField(
                    value = madre,
                    onValueChange = { madre = it },
                    label = { Text("Madre") },
                    placeholder = { Text("Desconocido", color = grisTexto.copy(alpha = 0.5f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                if (mensajeError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(mensajeError, color = Color.Red, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- BOTÓN GUARDAR ---
                Button(
                    onClick = {
                        if (nombre.isBlank()) {
                            mensajeError = "Por favor ingresa el nombre de la mascota"
                            return@Button
                        }
                        cargando = true
                        mensajeError = ""

                        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
                        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                        val uidActual = auth.currentUser?.uid ?: ""
                        val idMascotaGenerado = System.currentTimeMillis().toString()

                        val datosMascota = mapOf(
                            "idMascota" to idMascotaGenerado,
                            "uidUsuario" to uidActual,
                            "nombre" to nombre,
                            "alias" to alias,
                            "tipo" to tipoMascota,
                            "raza" to if (raza.isBlank()) "Desconocido" else raza,
                            "genero" to genero,
                            "fechaNacimiento" to if (fechaNacimiento == "Seleccionar fecha") "Desconocido" else fechaNacimiento,
                            "padre" to if (padre.isBlank()) "Desconocido" else padre,
                            "madre" to if (madre.isBlank()) "Desconocido" else madre,
                            "fotoUri" to fotoUri
                        )

                        db.collection("mascotas")
                            .document(idMascotaGenerado)
                            .set(datosMascota)
                            .addOnSuccessListener {
                                cargando = false
                                alFinalizar() // ¡Aquí ejecuta la salida automática!
                            }
                            .addOnFailureListener { e ->
                                cargando = false
                                mensajeError = "Error al guardar: ${e.message}"
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = azulEncabezado),
                    shape = RoundedCornerShape(25.dp),
                    enabled = !cargando
                ) {
                    if (cargando) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Guardar Mascota", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = alCancelar) {
                    Text("Cancelar / Volver", color = grisTexto, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }



    // ==========================================
    // PantallaEditarMascota
    // ==========================================
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PantallaEditarMascota(
        idMascota: Any,
        mascotaActualizada: () -> Unit,
        volver: () -> Unit
    ) {
        val azulEncabezado = Color(0xFF00B4D8)
        val docId = idMascota.toString()
        val db = FirebaseFirestore.getInstance()

        var nombre by remember { mutableStateOf("") }
        var tipo by remember { mutableStateOf("Perro") }
        var raza by remember { mutableStateOf("") }
        var genero by remember { mutableStateOf("Macho") }
        var fechaNacimiento by remember { mutableStateOf("") }
        var padre by remember { mutableStateOf("") }
        var madre by remember { mutableStateOf("") }

        var cargando by remember { mutableStateOf(true) }
        var mensajeError by remember { mutableStateOf("") }

        var expandirTipos by remember { mutableStateOf(false) }
        var expandirGenero by remember { mutableStateOf(false) }
        val listaTipos = listOf("Perro", "Gato", "Conejo", "Hámster", "Reptil", "Pájaro", "Pez", "Anfibio", "Hurón")
        val listaGeneros = listOf("Macho", "Hembra")

        // Cargar datos actuales para editarlos
        LaunchedEffect(docId) {
            db.collection("mascotas").document(docId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        nombre = document.getString("nombre") ?: ""
                        tipo = document.getString("tipo") ?: "Perro"
                        raza = document.getString("raza") ?: ""
                        genero = document.getString("genero") ?: "Macho"
                        fechaNacimiento = document.getString("fechaNacimiento") ?: ""
                        padre = document.getString("padre") ?: ""
                        madre = document.getString("madre") ?: ""
                    }
                    cargando = false
                }
                .addOnFailureListener {
                    cargando = false
                }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Editar Mascota", color = Color.White, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = azulEncabezado),
                    navigationIcon = {
                        IconButton(onClick = volver) {
                            Text("‹", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        ) { paddingValues ->
            if (cargando) {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = azulEncabezado)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color(0xFFF8FAFC))
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ExposedDropdownMenuBox(
                        expanded = expandirTipos,
                        onExpandedChange = { expandirTipos = !expandirTipos }
                    ) {
                        OutlinedTextField(
                            value = tipo,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de mascota") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirTipos) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(expanded = expandirTipos, onDismissRequest = { expandirTipos = false }) {
                            listaTipos.forEach { opcion ->
                                DropdownMenuItem(text = { Text(opcion) }, onClick = { tipo = opcion; expandirTipos = false })
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = raza,
                        onValueChange = { raza = it },
                        label = { Text("Raza") },
                        placeholder = { Text("Desconocido") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ExposedDropdownMenuBox(
                        expanded = expandirGenero,
                        onExpandedChange = { expandirGenero = !expandirGenero }
                    ) {
                        OutlinedTextField(
                            value = genero,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Género") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirGenero) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(expanded = expandirGenero, onDismissRequest = { expandirGenero = false }) {
                            listaGeneros.forEach { opcion ->
                                DropdownMenuItem(text = { Text(opcion) }, onClick = { genero = opcion; expandirGenero = false })
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = fechaNacimiento,
                        onValueChange = { fechaNacimiento = it },
                        label = { Text("Fecha de nacimiento") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = padre,
                        onValueChange = { padre = it },
                        label = { Text("Padre") },
                        placeholder = { Text("Desconocido") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = madre,
                        onValueChange = { madre = it },
                        label = { Text("Madre") },
                        placeholder = { Text("Desconocido") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (mensajeError.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(mensajeError, color = Color.Red, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (nombre.isBlank()) {
                                mensajeError = "El nombre no puede estar vacío"
                                return@Button
                            }

                            val datosActualizados = mapOf(
                                "nombre" to nombre,
                                "tipo" to tipo,
                                "raza" to if (raza.isBlank()) "Desconocido" else raza,
                                "genero" to genero,
                                "fechaNacimiento" to if (fechaNacimiento.isBlank()) "Desconocido" else fechaNacimiento,
                                "padre" to if (padre.isBlank()) "Desconocido" else padre,
                                "madre" to if (madre.isBlank()) "Desconocido" else madre
                            )

                            db.collection("mascotas").document(docId)
                                .update(datosActualizados)
                                .addOnSuccessListener {
                                    mascotaActualizada()
                                }
                                .addOnFailureListener { e ->
                                    mensajeError = "Error al actualizar: ${e.message}"
                                }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = azulEncabezado),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text("Guardar Cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = volver) {
                        Text("Cancelar", color = Color(0xFF64748B))
                    }
                }
            }
        }
    }

    @Composable
    fun Campo(
        valor: String,
        cambio: (String) -> Unit,
        etiqueta: String
    ) {
        OutlinedTextField(
            value = valor,
            onValueChange = cambio,
            label = { Text(etiqueta) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
        )
    }


    // ==========================================
    // PantallaMascotaSeleccionada
    // ==========================================
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PantallaMascotaSeleccionada(
        idMascota: Any,
        irHistorial: () -> Unit,
        editarMascota: () -> Unit,
        eliminarMascota: () -> Unit,
        irVacunas: () -> Unit,
        irMedicamentos: () -> Unit,
        volver: () -> Unit
    ) {
        val azulEncabezado = Color(0xFF00B4D8)
        val rojoEliminar = Color(0xFFEF4444)
        val textoOscuro = Color(0xFF1E293B)
        val grisTexto = Color(0xFF64748B)

        var nombre by remember { mutableStateOf("Cargando...") }
        var tipo by remember { mutableStateOf("") }
        var raza by remember { mutableStateOf("Desconocido") }
        var genero by remember { mutableStateOf("") }
        var fechaNacimiento by remember { mutableStateOf("Desconocido") }
        var padre by remember { mutableStateOf("Desconocido") }
        var madre by remember { mutableStateOf("Desconocido") }
        var fotoUri by remember { mutableStateOf("") }
        var cargando by remember { mutableStateOf(true) }

        val db = FirebaseFirestore.getInstance()
        val docId = idMascota.toString()

        // Cargar directamente usando el ID del documento de Firestore
        LaunchedEffect(docId) {
            if (docId.isBlank() || docId == "0") {
                cargando = false
                return@LaunchedEffect
            }
            db.collection("mascotas").document(docId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        nombre = document.getString("nombre") ?: ""
                        tipo = document.getString("tipo") ?: ""
                        raza = document.getString("raza") ?: "Desconocido"
                        genero = document.getString("genero") ?: ""
                        fechaNacimiento = document.getString("fechaNacimiento") ?: "Desconocido"
                        padre = document.getString("padre") ?: "Desconocido"
                        madre = document.getString("madre") ?: "Desconocido"
                        fotoUri = document.getString("fotoUri") ?: ""
                    }
                    cargando = false
                }
                .addOnFailureListener {
                    cargando = false
                }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Perfil de la Mascota", color = Color.White, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = azulEncabezado),
                    navigationIcon = {
                        IconButton(onClick = volver) {
                            Text("‹", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        ) { paddingValues ->
            if (cargando) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = azulEncabezado)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color(0xFFF8FAFC))
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (fotoUri.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(fotoUri),
                                contentDescription = "Foto Mascota",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = when {
                                    tipo.contains("Gato", ignoreCase = true) -> "🐱"
                                    tipo.contains("Conejo", ignoreCase = true) -> "🐰"
                                    else -> "🐶"
                                },
                                fontSize = 45.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = textoOscuro)
                    Text(text = "$tipo • $genero", fontSize = 14.sp, color = grisTexto)

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = irHistorial,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                        ) {
                            Text("📋 Historial", color = textoOscuro, fontSize = 12.sp)
                        }
                        Button(
                            onClick = irVacunas,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                        ) {
                            Text("💉 Vacunas", color = textoOscuro, fontSize = 12.sp)
                        }
                        Button(
                            onClick = irMedicamentos,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                        ) {
                            Text("💊 Meds", color = textoOscuro, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Información Genealógica", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = azulEncabezado)
                            Spacer(modifier = Modifier.height(10.dp))
                            ItemDetalle("Raza", raza)
                            ItemDetalle("Nacimiento", fechaNacimiento)
                            ItemDetalle("Padre", padre)
                            ItemDetalle("Madre", madre)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = editarMascota,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = azulEncabezado),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text("Editar Mascota", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // ELIMINAR REAL DE FIRESTORE Y DE LA LISTA
                    OutlinedButton(
                        onClick = {
                            db.collection("mascotas").document(docId)
                                .delete()
                                .addOnSuccessListener {
                                    eliminarMascota()
                                }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text("Eliminar Mascota", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = rojoEliminar)
                    }
                }
            }
        }
    }


    @Composable
    fun ItemDetalle(titulo: String, valor: String) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = titulo, color = Color(0xFF64748B), fontSize = 13.sp)
            Text(text = valor, color = Color(0xFF1E293B), fontWeight = FontWeight.Medium, fontSize = 13.sp)
        }
    }

    // Componente auxiliar para alinear los textos de información
    @Composable
    fun ItemInfo(label: String, valor: String) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = Color(0xFF64748B), fontSize = 14.sp)
            Text(text = valor, color = Color(0xFF1E293B), fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }
    }



    // ==========================================
    // PantallaRegistroHistorial
    // ==========================================
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PantallaRegistrarHistorial(
        idMascota: Any,
        historialRegistrado: () -> Unit,
        volver: () -> Unit
    ) {
        val azulEncabezado = Color(0xFF00B4D8)
        val context = LocalContext.current
        val db = FirebaseFirestore.getInstance()
        val mascotaIdStr = idMascota.toString()

        var fecha by remember { mutableStateOf("") }
        var motivo by remember { mutableStateOf("") }
        var diagnostico by remember { mutableStateOf("") }
        var tratamiento by remember { mutableStateOf("") }
        var observaciones by remember { mutableStateOf("") }
        var veterinario by remember { mutableStateOf("") }
        var mensajeError by remember { mutableStateOf("") }

        // Función para abrir el selector de calendario
        fun mostrarCalendario() {
            val calendar = Calendar.getInstance()
            val anio = calendar.get(Calendar.YEAR)
            val mes = calendar.get(Calendar.MONTH)
            val dia = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(context, { _, year, month, dayOfMonth ->
                val mesFormateado = String.format("%02d", month + 1)
                val diaFormateado = String.format("%02d", dayOfMonth)
                fecha = "$year-$mesFormateado-$diaFormateado"
            }, anio, mes, dia).show()
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Registrar consulta", color = Color.White, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = azulEncabezado),
                    navigationIcon = {
                        IconButton(onClick = volver) {
                            Text("‹", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF8FAFC))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo de Fecha con Calendario Interactivo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { mostrarCalendario() }
                ) {
                    OutlinedTextField(
                        value = fecha,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false, // Evita que se abra el teclado del celular
                        label = { Text("Fecha (AAAA-MM-DD) *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            IconButton(onClick = { mostrarCalendario() }) {
                                Text("📅", fontSize = 18.sp)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color(0xFF1E293B),
                            disabledBorderColor = Color(0xFFCBD5E1),
                            disabledLabelColor = Color(0xFF64748B),
                            disabledContainerColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = motivo,
                    onValueChange = { motivo = it },
                    label = { Text("Motivo *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = diagnostico,
                    onValueChange = { diagnostico = it },
                    label = { Text("Diagnóstico") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = tratamiento,
                    onValueChange = { tratamiento = it },
                    label = { Text("Tratamiento") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = observaciones,
                    onValueChange = { observaciones = it },
                    label = { Text("Observaciones") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = veterinario,
                    onValueChange = { veterinario = it },
                    label = { Text("Veterinario") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (mensajeError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(mensajeError, color = Color.Red, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (fecha.isBlank() || motivo.isBlank()) {
                            mensajeError = "Fecha y motivo son obligatorios"
                            return@Button
                        }

                        val nuevaConsulta = mapOf(
                            "idMascota" to mascotaIdStr,
                            "fecha" to fecha,
                            "motivo" to motivo,
                            "diagnostico" to diagnostico,
                            "tratamiento" to tratamiento,
                            "observaciones" to observaciones,
                            "veterinario" to veterinario
                        )

                        db.collection("historial_medico")
                            .add(nuevaConsulta)
                            .addOnSuccessListener {
                                historialRegistrado()
                            }
                            .addOnFailureListener { e ->
                                mensajeError = "Error al guardar: ${e.message}"
                            }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = azulEncabezado),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("Guardar consulta", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = volver) {
                    Text("Cancelar", color = Color(0xFF64748B))
                }
            }
        }
    }



    // ==========================================
    // PanatallaEditarHistorial
    // ==========================================
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PantallaEditarHistorial(
        idHistorial: Any, // Acepta el ID del documento de Firestore en formato String
        historialActualizado: () -> Unit,
        volver: () -> Unit
    ) {
        val azulEncabezado = Color(0xFF00B4D8)
        val db = FirebaseFirestore.getInstance()
        val historialIdStr = idHistorial.toString()

        var fecha by remember { mutableStateOf("") }
        var motivo by remember { mutableStateOf("") }
        var diagnostico by remember { mutableStateOf("") }
        var tratamiento by remember { mutableStateOf("") }
        var observaciones by remember { mutableStateOf("") }
        var veterinario by remember { mutableStateOf("") }

        var cargando by remember { mutableStateOf(true) }
        var mensajeError by remember { mutableStateOf("") }

        // Cargar los datos actuales de la consulta desde Firestore
        LaunchedEffect(historialIdStr) {
            if (historialIdStr.isBlank()) {
                cargando = false
                return@LaunchedEffect
            }
            db.collection("historial_medico").document(historialIdStr)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        fecha = document.getString("fecha") ?: ""
                        motivo = document.getString("motivo") ?: ""
                        diagnostico = document.getString("diagnostico") ?: ""
                        tratamiento = document.getString("tratamiento") ?: ""
                        observaciones = document.getString("observaciones") ?: ""
                        veterinario = document.getString("veterinario") ?: ""
                    }
                    cargando = false
                }
                .addOnFailureListener {
                    cargando = false
                }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Editar consulta", color = Color.White, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = azulEncabezado),
                    navigationIcon = {
                        IconButton(onClick = volver) {
                            Text("‹", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        ) { paddingValues ->
            if (cargando) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = azulEncabezado)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color(0xFFF8FAFC))
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { fecha = it },
                        label = { Text("Fecha (AAAA-MM-DD) *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = motivo,
                        onValueChange = { motivo = it },
                        label = { Text("Motivo *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = diagnostico,
                        onValueChange = { diagnostico = it },
                        label = { Text("Diagnóstico") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = tratamiento,
                        onValueChange = { tratamiento = it },
                        label = { Text("Tratamiento") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = observaciones,
                        onValueChange = { observaciones = it },
                        label = { Text("Observaciones") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = veterinario,
                        onValueChange = { veterinario = it },
                        label = { Text("Veterinario") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (mensajeError.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(mensajeError, color = Color.Red, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (fecha.isBlank() || motivo.isBlank()) {
                                mensajeError = "Fecha y motivo son obligatorios"
                                return@Button
                            }

                            val datosActualizados = mapOf(
                                "fecha" to fecha,
                                "motivo" to motivo,
                                "diagnostico" to diagnostico,
                                "tratamiento" to tratamiento,
                                "observaciones" to observaciones,
                                "veterinario" to veterinario
                            )

                            db.collection("historial_medico").document(historialIdStr)
                                .update(datosActualizados)
                                .addOnSuccessListener {
                                    historialActualizado()
                                }
                                .addOnFailureListener { e ->
                                    mensajeError = "Error al actualizar: ${e.message}"
                                }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = azulEncabezado),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text("Guardar cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = volver) {
                        Text("Cancelar", color = Color(0xFF64748B))
                    }
                }
            }
        }
    }



    // ==========================================
    // PantallaVacuna
    // ==========================================
    @Composable
    fun PantallaVacunas(
        idMascota: String,
        volver: () -> Unit
    ) {
        var vacunas by remember { mutableStateOf(emptyList<Vacuna>()) }
        var nombreVacuna by remember { mutableStateOf("") }
        var fechaAplicacion by remember { mutableStateOf("") }
        var proximaDosis by remember { mutableStateOf("") }
        var observaciones by remember { mutableStateOf("") }

        LaunchedEffect(idMascota) {
            VacunaDAO.obtenerVacunas(idMascota) { lista ->
                vacunas = lista
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text("💉 Vacunas", fontSize = 28.sp)
            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn {
                item {
                    Campo(nombreVacuna, { nombreVacuna = it }, "Nombre de vacuna")
                    Campo(fechaAplicacion, { fechaAplicacion = it }, "Fecha de aplicación")
                    Campo(proximaDosis, { proximaDosis = it }, "Próxima dosis")
                    Campo(observaciones, { observaciones = it }, "Observaciones")

                    Button(
                        onClick = {
                            if (nombreVacuna.isNotBlank() && fechaAplicacion.isNotBlank()) {
                                VacunaDAO.registrarVacuna(
                                    idMascota,
                                    nombreVacuna,
                                    fechaAplicacion,
                                    proximaDosis,
                                    observaciones
                                ) {
                                    VacunaDAO.obtenerVacunas(idMascota) { nuevaLista ->
                                        vacunas = nuevaLista
                                        nombreVacuna = ""
                                        fechaAplicacion = ""
                                        proximaDosis = ""
                                        observaciones = ""
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("➕ Registrar vacuna")
                    }

                    Spacer(modifier = Modifier.height(15.dp))
                }

                items(vacunas) { vacuna ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(15.dp)) {
                            Text("💉 ${vacuna.nombreVacuna}", fontSize = 20.sp)
                            Text("Aplicación: ${vacuna.fechaAplicacion}")
                            Text("Próxima dosis: ${vacuna.proximaDosis}")
                            Text("Observaciones: ${vacuna.observaciones}")

                            TextButton(
                                onClick = {
                                    VacunaDAO.eliminarVacuna(vacuna.idVacuna) {
                                        VacunaDAO.obtenerVacunas(idMascota) { nuevaLista ->
                                            vacunas = nuevaLista
                                        }
                                    }
                                }
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = volver,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Volver")
                    }
                }
            }
        }
    }



    // ==========================================
    // PantallaMedicamentos
    // ==========================================
    @Composable
    fun PantallaMedicamentos(
        idMascota: String,
        volver: () -> Unit
    ) {
        var medicamentos by remember { mutableStateOf(emptyList<Medicamento>()) }
        var nombre by remember { mutableStateOf("") }
        var dosis by remember { mutableStateOf("") }
        var frecuencia by remember { mutableStateOf("") }
        var fechaInicio by remember { mutableStateOf("") }
        var fechaFin by remember { mutableStateOf("") }
        var indicaciones by remember { mutableStateOf("") }

        LaunchedEffect(idMascota) {
            MedicamentoDAO.obtenerMedicamentos(idMascota) { lista ->
                medicamentos = lista
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text("💊 Medicamentos", fontSize = 28.sp)
            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn {
                item {
                    Campo(nombre, { nombre = it }, "Medicamento")
                    Campo(dosis, { dosis = it }, "Dosis")
                    Campo(frecuencia, { frecuencia = it }, "Frecuencia")
                    Campo(fechaInicio, { fechaInicio = it }, "Fecha de inicio")
                    Campo(fechaFin, { fechaFin = it }, "Fecha de finalización")
                    Campo(indicaciones, { indicaciones = it }, "Indicaciones")

                    Button(
                        onClick = {
                            if (nombre.isNotBlank()) {
                                MedicamentoDAO.registrarMedicamento(
                                    idMascota,
                                    nombre,
                                    dosis,
                                    frecuencia,
                                    fechaInicio,
                                    fechaFin,
                                    indicaciones
                                ) {
                                    MedicamentoDAO.obtenerMedicamentos(idMascota) { nuevaLista ->
                                        medicamentos = nuevaLista
                                        nombre = ""
                                        dosis = ""
                                        frecuencia = ""
                                        fechaInicio = ""
                                        fechaFin = ""
                                        indicaciones = ""
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("➕ Registrar medicamento")
                    }

                    Spacer(modifier = Modifier.height(15.dp))
                }

                items(medicamentos) { medicamento ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(15.dp)) {
                            Text("💊 ${medicamento.nombreMedicamento}", fontSize = 20.sp)
                            Text("Dosis: ${medicamento.dosis}")
                            Text("Frecuencia: ${medicamento.frecuencia}")
                            Text("Inicio: ${medicamento.fechaInicio}")
                            Text("Fin: ${medicamento.fechaFin}")
                            Text("Indicaciones: ${medicamento.indicaciones}")

                            TextButton(
                                onClick = {
                                    MedicamentoDAO.eliminarMedicamento(medicamento.idMedicamento) {
                                        MedicamentoDAO.obtenerMedicamentos(idMascota) { nuevaLista ->
                                            medicamentos = nuevaLista
                                        }
                                    }
                                }
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = volver,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Volver")
                    }
                }
            }
        }
    }
}


// ==========================================
// PantallaPerfil
// ==========================================
@Composable
fun PantallaPerfil(
    irInicio: () -> Unit,
    irMascotas: () -> Unit,
    irAgenda: () -> Unit,
    cerrarSesion: () -> Unit
) {
    val azulEncabezado = Color(0xFF00B4D8)
    val textoOscuro = Color(0xFF1E293B)
    val grisTexto = Color(0xFF64748B)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = irInicio,
                    icon = { Text("🏠", fontSize = 18.sp) },
                    label = { Text("Inicio", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = irMascotas,
                    icon = { Text("🐾", fontSize = 18.sp) },
                    label = { Text("Mascotas", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = irAgenda,
                    icon = { Text("📅", fontSize = 18.sp) },
                    label = { Text("Agenda", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Text("👤", fontSize = 18.sp) },
                    label = { Text("Perfil", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = azulEncabezado) }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(azulEncabezado)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "‹",
                            fontSize = 32.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { irInicio() }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Perfil",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(text = "⋮", fontSize = 20.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFF7C3AED), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "K", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(24.dp)
                            .background(Color(0xFF10B981), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📁", fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Kimberly", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textoOscuro)
                Text(text = "@kimy", fontSize = 14.sp, color = grisTexto)
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(text = "Dueño", fontSize = 13.sp, color = grisTexto)
                    Text(text = " • ", fontSize = 13.sp, color = grisTexto)
                    Text(text = "Gratuita", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "2 KB de 20 MB de almacenamiento", fontSize = 12.sp, color = grisTexto)
                        Text(text = "0%", fontSize = 12.sp, color = grisTexto)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { 0.01f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = azulEncabezado,
                        trackColor = Color(0xFFE2E8F0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                colors = cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ItemPerfilOpcion(icono = "👤", titulo = "Perfil")
                    ItemPerfilOpcion(icono = "⚙️", titulo = "Cuenta")
                    ItemPerfilOpcion(icono = "🛡️", titulo = "Seguridad")
                    ItemPerfilOpcion(icono = "🎖️", titulo = "Insignias")
                    ItemPerfilOpcion(icono = "👥", titulo = "Cuentas")
                    ItemPerfilOpcion(icono = "💳", titulo = "Pagos y planes")
                    ItemPerfilOpcion(icono = "📄", titulo = "Facturación")
                    ItemPerfilOpcion(icono = "🚪", titulo = "Cerrar sesión", esRojo = true, onClick = cerrarSesion)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Accesibilidad",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = grisTexto,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            Card(
                colors = cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ItemPerfilOpcion(icono = "🌐", titulo = "Lenguaje", flecha = "›")
                    ItemPerfilOpcion(icono = "🎨", titulo = "Apariencia", flecha = "›")
                    ItemPerfilOpcion(icono = "🔔", titulo = "Notificaciones", flecha = "›")
                    ItemPerfilOpcion(icono = "❓", titulo = "Ayuda", flecha = "↗")
                    ItemPerfilOpcion(icono = "🎓", titulo = "Onboarding", flecha = "›")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sobre la app",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = grisTexto,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            Card(
                colors = cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ItemPerfilOpcion(icono = "ℹ️", titulo = "Sobre la app", flecha = "›")
                    ItemPerfilOpcion(icono = "❤️", titulo = "Apoya a Petic", flecha = "↗")
                    ItemPerfilOpcion(icono = "⭐", titulo = "Califica nuestra app", flecha = "›")
                    ItemPerfilOpcion(icono = "🎟️", titulo = "Soporte", flecha = "›")
                    ItemPerfilOpcion(icono = "🔒", titulo = "Sugerir Mejoras (Pro)", flecha = "🔒")
                    // Se removió "Compartir" de aquí
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Se removieron los iconos de redes sociales de abajo
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "v0.28.1", fontSize = 12.sp, color = grisTexto)
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}


@Composable
fun ItemPerfilOpcion(
    icono: String,
    titulo: String,
    flecha: String = "›",
    esRojo: Boolean = false,
    onClick: () -> Unit = {}
) {
    val textoColor = if (esRojo) Color(0xFFEF4444) else Color(0xFF1E293B)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icono, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(14.dp))
            Text(text = titulo, fontSize = 15.sp, color = textoColor, fontWeight = FontWeight.Medium)
        }
        Text(text = flecha, fontSize = 16.sp, color = if (esRojo) Color(0xFFEF4444) else Color(0xFF94A3B8))
    }
}



// ==========================================
// PANTALLA DE INICIO
// ==========================================
@Composable
fun PantallaInicio(
    irLogin: () -> Unit,
    irRegistro: () -> Unit
) {
    val verdePrincipal = Color(0xFF00B4D8) // Tu color celeste principal
    val colorInactivo = Color(0xFFCBD5E1)  // Gris clarito para las líneas no seleccionadas
    val textoOscuro = Color(0xFF1E293B)
    val grisTexto = Color(0xFF64748B)

    // Estado para controlar en cuál de los pasos va el usuario (0, 1 o 2)
    var pasoActual by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- BARRA SUPERIOR DE PROGRESO DINÁMICA ---
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Indicador 1
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                color = if (pasoActual >= 0) verdePrincipal else colorInactivo,
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                    // Indicador 2
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                color = if (pasoActual >= 1) verdePrincipal else colorInactivo,
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                    // Indicador 3
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                color = if (pasoActual >= 2) verdePrincipal else colorInactivo,
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }

            // --- CONTENIDO DINÁMICO SEGÚN EL PASO ---
            when (pasoActual) {
                0 -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.perrito),
                            contentDescription = "Logo",
                            modifier = Modifier.size(250.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "VetPets habla",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = textoOscuro
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "¿Estás listo para cuidar a tu mascota de la mejor manera?",
                            fontSize = 14.sp,
                            color = grisTexto,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                1 -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_mascotas),
                            contentDescription = "Mascotas",
                            modifier = Modifier.size(220.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Todo para tu mascota en un solo lugar",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = textoOscuro,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Salud, vacunas, citas y tratamientos. VetPets lo tiene todo para cuidar lo que más amas.",
                            fontSize = 14.sp,
                            color = grisTexto,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                2 -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logito),
                            contentDescription = "Logo",
                            modifier = Modifier.size(120.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "¡Únete a VetPets!",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = textoOscuro
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Regístrate gratis y cuida a tu mascota como se merece.",
                            fontSize = 14.sp,
                            color = grisTexto,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // --- BOTONES DE NAVEGACIÓN INFERIORES ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                if (pasoActual < 2) {
                    Button(
                        onClick = { pasoActual++ },
                        colors = ButtonDefaults.buttonColors(containerColor = verdePrincipal),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Continuar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = irRegistro,
                        colors = ButtonDefaults.buttonColors(containerColor = verdePrincipal),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Crear mi cuenta", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "¿Ya tienes cuenta? ", color = grisTexto, fontSize = 13.sp)
                        Text(
                            text = "Iniciar sesión",
                            color = verdePrincipal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable { irLogin() }
                        )
                    }
                }
            }
        }
    }
}


// ==========================================
// PANTALLA DE LOGIN
// ==========================================
@Composable
fun PantallaLogin(
    iniciarSesionCorrecto: (Int) -> Unit,
    volver: () -> Unit,
    irRegistro: () -> Unit
) {
    val azulCeleste = Color(0xFF00B4D8)
    val textoOscuro = Color(0xFF1E293B)
    val grisTexto = Color(0xFF64748B)

    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // --- FRANJA SUPERIOR CELESTE ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(azulCeleste)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "‹",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.clickable { volver() }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(text = "⋮", fontSize = 20.sp, color = Color.White)
            }
        }

        // --- CONTENIDO INFERIOR BLANCO ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Logotipo central
            Image(
                painter = painterResource(id = R.drawable.logito),
                contentDescription = "Logo VetPets",
                modifier = Modifier.size(90.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo de Nombre de Usuario
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text("Nombre de Usuario", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = grisTexto)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it; mensaje = "" },
                    placeholder = { Text("Escribe tu nombre de usuario", color = Color(0xFFA0AEC0)) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Contraseña con icono de Ojo
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text("Contraseña", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = grisTexto)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it; mensaje = "" },
                    placeholder = { Text("Escribe tu contraseña", color = Color(0xFFA0AEC0)) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val imagenIcono = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = imagenIcono, contentDescription = "Ver contraseña", tint = grisTexto)
                        }
                    }
                )
            }

            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = mensaje, color = Color.Red, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón principal de Iniciar Sesión
            Button(
                onClick = {
                    if (usuario.isBlank() || contrasena.isBlank()) {
                        mensaje = "Completa todos los campos"
                    } else {
                        try {
                            UsuarioDAO.validarUsuario(usuario.trim(), contrasena.trim()) { id ->
                                if (id != null) {
                                    iniciarSesionCorrecto(id)
                                } else {
                                    mensaje = "Usuario o contraseña incorrectos"
                                }
                            }
                        } catch (e: Exception) {
                            mensaje = "Error de conexión con la base de datos"
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = azulCeleste),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Iniciar sesión", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Enlace inferior de Registro
            Row(
                modifier = Modifier.padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "¿No tienes una cuenta? ", color = grisTexto, fontSize = 13.sp)
                Text(
                    text = "Regístrate",
                    color = azulCeleste,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { irRegistro() }
                )
            }
        }
    }
}


// ==========================================
// FlujoRegistroCompleto
// ==========================================
@Composable
fun FlujoRegistroCompleto(
    onFinalizarRegistro: () -> Unit,
    onVolverLogin: () -> Unit
) {
    var etapaActual by remember { mutableStateOf(0) }
    val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
    val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

    when (etapaActual) {
        0 -> PantallaRegistroLimpia(
            onRegistroExitoso = { usuarioRegistrado, contrasenaRegistrada ->
                val correoAutomatico = "${usuarioRegistrado.trim().lowercase().replace(" ", "")}@vetpets.com"

                auth.createUserWithEmailAndPassword(correoAutomatico, contrasenaRegistrada.trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.sendEmailVerification()?.addOnCompleteListener {
                                val idGenerado = System.currentTimeMillis().toInt()
                                val nuevoUsuario = hashMapOf(
                                    "idUsuario" to idGenerado,
                                    "uid" to (user?.uid ?: ""),
                                    "usuario" to usuarioRegistrado.trim(),
                                    "correo" to correoAutomatico
                                )

                                if (user != null) {
                                    db.collection("usuarios")
                                        .document(user.uid)
                                        .set(nuevoUsuario)
                                        .addOnSuccessListener {
                                            android.os.Handler(android.os.Looper.getMainLooper()).post {
                                                etapaActual = 1
                                            }
                                        }
                                        .addOnFailureListener {
                                            android.os.Handler(android.os.Looper.getMainLooper()).post {
                                                etapaActual = 1
                                            }
                                        }
                                } else {
                                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                                        etapaActual = 1
                                    }
                                }
                            }
                        } else {
                            // Si falla la creación en Auth
                        }
                    }
            },
            onVolver = onVolverLogin
        )
        1 -> PantallaRegistroExitoso(
            onIngresarApp = onFinalizarRegistro
        )
    }
}

// ==========================================
// PANTALLA DE REGISTRO Limpia
// ==========================================
@Composable
fun PantallaRegistroLimpia(
    onRegistroExitoso: (String, String) -> Unit,
    onVolver: () -> Unit
) {
    val azulCeleste = Color(0xFF00B4D8)
    val textoOscuro = Color(0xFF1E293B)
    val grisTexto = Color(0xFF64748B)

    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var repetirContrasena by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    // Estados para controlar la visibilidad de las contraseñas (el icono del ojo)
    var passwordVisible by remember { mutableStateOf(false) }
    var repetirPasswordVisible by remember { mutableStateOf(false) }

    var paginaActual by remember { mutableStateOf(0) }

    val textosCarrusel = listOf(
        Pair("Todo para tu mascota en un solo lugar", "Salud, vacunas, citas y tratamientos. VetPets lo tiene todo."),
        Pair("Nunca te pierdas nada importante", "Recordatorios automáticos de vacunas y citas médicas para tu mascota."),
        Pair("Comparte con toda la familia", "Invita a familiares y veterinarios a colaborar en el cuidado.")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // --- FRANJA SUPERIOR CELESTE INTERACTIVA ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(azulCeleste)
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .clickable { paginaActual = (paginaActual + 1) % 3 }
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "‹",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.clickable { onVolver() }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Registro",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(text = "⋮", fontSize = 20.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = textosCarrusel[paginaActual].first,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = textosCarrusel[paginaActual].second,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Image(
                        painter = painterResource(id = R.drawable.registro),
                        contentDescription = "Mascota Registro",
                        modifier = Modifier
                            .size(75.dp)
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(6.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { index ->
                        val esSeleccionado = paginaActual == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .size(width = if (esSeleccionado) 24.dp else 6.dp, height = 4.dp)
                                .background(
                                    color = if (esSeleccionado) Color.White else Color.White.copy(
                                        alpha = 0.5f
                                    ),
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }
            }
        }

        // --- CONTENEDOR DE CAMPOS DE TEXTO ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Nombre de Usuario
            Text("Nombre de Usuario", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = grisTexto)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it; mensajeError = "" },
                placeholder = { Text("Escribe tu nombre de usuario", color = Color(0xFFA0AEC0)) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Contraseña con botón de ver/ocultar (Ojo)
            Text("Contraseña", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = grisTexto)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it; mensajeError = "" },
                placeholder = { Text("Escribe tu contraseña", color = Color(0xFFA0AEC0)) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val imagenIcono = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = imagenIcono, contentDescription = "Cambiar visibilidad", tint = grisTexto)
                    }
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Repite tu Contraseña con botón de ver/ocultar (Ojo)
            Text("Repite tu Contraseña", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = grisTexto)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = repetirContrasena,
                onValueChange = { repetirContrasena = it; mensajeError = "" },
                placeholder = { Text("Repite tu contraseña", color = Color(0xFFA0AEC0)) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (repetirPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val imagenIcono = if (repetirPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { repetirPasswordVisible = !repetirPasswordVisible }) {
                        Icon(imageVector = imagenIcono, contentDescription = "Cambiar visibilidad", tint = grisTexto)
                    }
                }
            )

            if (mensajeError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = mensajeError, color = Color.Red, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (usuario.isBlank() || contrasena.isBlank() || repetirContrasena.isBlank()) {
                        mensajeError = "Completa todos los campos"
                    } else if (contrasena != repetirContrasena) {
                        mensajeError = "Las contraseñas no coinciden"
                    } else {
                        onRegistroExitoso(usuario, contrasena)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = azulCeleste),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Crear cuenta", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "¿Ya tienes cuenta? ", color = grisTexto, fontSize = 13.sp)
                Text(
                    text = "Iniciar sesión",
                    color = azulCeleste,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { onVolver() }
                )
            }
        }
    }
}




// ==========================================
// PANTALLA DE REGISTRO EXITOSO
// ==========================================
@Composable
fun PantallaRegistroExitoso(
    onIngresarApp: () -> Unit
) {
    val verdeExito = Color(0xFF10B981)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Espaciador superior limpio
        Spacer(modifier = Modifier.height(20.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("¡Completado!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(verdeExito, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_mascotas),
                contentDescription = "Mascota Feliz",
                modifier = Modifier.size(140.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("¡Iniciemos la app!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        }

        Button(
            onClick = onIngresarApp,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1B4B)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(bottom = 12.dp)
        ) {
            Text("¡Ingresa a la app!", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}



// ==========================================
// PantallaAgenda
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgenda(
    irInicio: () -> Unit,
    irMascotas: () -> Unit,
    irPerfil: () -> Unit,
    volver: () -> Unit,
    idMascota: String = "",
    irCrearTratamiento: () -> Unit = {}
) {
    val azulEncabezado = Color(0xFF00B4D8)
    val textoOscuro = Color(0xFF1E293B)
    val grisTexto = Color(0xFF64748B)
    val db = FirebaseFirestore.getInstance()

    var textoBusqueda by remember { mutableStateOf("") }
    var pestañaAgenda by remember { mutableStateOf(0) } // 0: Hoy, 1: Próximos, 2: Histórico
    var diaSeleccionado by remember { mutableStateOf(8) } // Día por defecto (8 de Noviembre)
    var mesAnioActual by remember { mutableStateOf("2026 Noviembre") }
    var mostrarMenuOpciones by remember { mutableStateOf(false) }
    var mostrarModalAgregar by remember { mutableStateOf(false) } // Modal de + Agregar recordatorio
    var tipoVistaCalendario by remember { mutableStateOf("mensual") }

    var listaTratamientos by remember { mutableStateOf(listOf<Tratamiento>()) }
    var cargando by remember { mutableStateOf(true) }

    // Cargar tratamientos reales desde Firestore
    LaunchedEffect(idMascota) {
        val query = if (idMascota.isNotEmpty()) {
            db.collection("tratamientos").whereEqualTo("idMascota", idMascota)
        } else {
            db.collection("tratamientos")
        }

        query.get()
            .addOnSuccessListener { result ->
                val temporal = mutableListOf<Tratamiento>()
                for (doc in result) {
                    val medsList = mutableListOf<SubMedicamento>()
                    val rawMeds = doc.get("medicamentos") as? List<*>
                    rawMeds?.let { list ->
                        for (m in list) {
                            val map = m as? Map<*, *>
                            if (map != null) {
                                medsList.add(
                                    SubMedicamento(
                                        tipo = map["tipo"] as? String ?: "Medicamento",
                                        via = map["via"] as? String ?: "Oral",
                                        nombre = map["nombre"] as? String ?: "",
                                        dosis = map["dosis"] as? String ?: "",
                                        unidad = map["unidad"] as? String ?: "ml",
                                        frecuenciaHoras = map["frecuenciaHoras"] as? String ?: "8",
                                        hora = map["hora"] as? String ?: "8:00 AM"
                                    )
                                )
                            }
                        }
                    }

                    temporal.add(
                        Tratamiento(
                            id = doc.id,
                            idMascota = doc.getString("idMascota") ?: "",
                            nombreTratamiento = doc.getString("nombreTratamiento") ?: "Sin nombre",
                            fechaInicio = doc.getString("fechaInicio") ?: "08/11/2026",
                            comentario = doc.getString("comentario") ?: "",
                            lugar = doc.getString("lugar") ?: "Casa",
                            veterinario = doc.getString("veterinario") ?: "",
                            recordatorioMinutos = doc.getLong("recordatorioMinutos")?.toInt() ?: 15,
                            medicamentos = medsList,
                            estado = doc.getString("estado") ?: "En proceso"
                        )
                    )
                }
                listaTratamientos = temporal
                cargando = false
            }
            .addOnFailureListener {
                cargando = false
            }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = irInicio,
                    icon = { Text("🏠", fontSize = 18.sp) },
                    label = { Text("Inicio", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = irMascotas,
                    icon = { Text("🐾", fontSize = 18.sp) },
                    label = { Text("Mascotas", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Text("📅", fontSize = 18.sp) },
                    label = { Text("Agenda", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = azulEncabezado) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = irPerfil,
                    icon = { Text("👤", fontSize = 18.sp) },
                    label = { Text("Perfil", fontSize = 11.sp) }
                )
            }
        },
        floatingActionButton = {
            Button(
                onClick = { mostrarModalAgregar = true }, // Abre el menú flotante
                colors = ButtonDefaults.buttonColors(containerColor = azulEncabezado),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .height(52.dp)
                    .padding(horizontal = 4.dp)
            ) {
                Text("+ Agregar recordatorio", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // --- ENCABEZADO SUPERIOR AZUL ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(azulEncabezado)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "‹",
                            fontSize = 32.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { volver() }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Calendario",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Box {
                        Text(
                            text = "⋮",
                            fontSize = 22.sp,
                            color = Color.White,
                            modifier = Modifier
                                .clickable { mostrarMenuOpciones = true }
                                .padding(4.dp)
                        )

                        // --- MENÚ DESPLEGABLE DE OPCIONES (3 PUNTOS) ---
                        DropdownMenu(
                            expanded = mostrarMenuOpciones,
                            onDismissRequest = { mostrarMenuOpciones = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Vista semanal", color = textoOscuro) },
                                onClick = { tipoVistaCalendario = "semanal"; mostrarMenuOpciones = false },
                                leadingIcon = { Text("📅", fontSize = 16.sp) }
                            )
                            DropdownMenuItem(
                                text = { Text("Vista mensual", color = azulEncabezado, fontWeight = FontWeight.Bold) },
                                onClick = { tipoVistaCalendario = "mensual"; mostrarMenuOpciones = false },
                                leadingIcon = { Text("🗓️", fontSize = 16.sp) }
                            )
                            DropdownMenuItem(
                                text = { Text("Vista diaria", color = textoOscuro) },
                                onClick = { tipoVistaCalendario = "diaria"; mostrarMenuOpciones = false },
                                leadingIcon = { Text("📆", fontSize = 16.sp) }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Google Calendar", color = textoOscuro) },
                                onClick = { mostrarMenuOpciones = false },
                                leadingIcon = { Text("🔗", fontSize = 16.sp) }
                            )
                        }
                    }
                }
            }

            // --- SELECTOR DE MES Y CUADRÍCULA DE CALENDARIO MENSUAL ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "< Oct",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = azulEncabezado,
                        modifier = Modifier.clickable { }
                    )
                    Text(
                        text = mesAnioActual,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textoOscuro
                    )
                    Text(
                        text = "Dic >",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = azulEncabezado,
                        modifier = Modifier.clickable { }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val nombresDias = listOf("Dom", "Lun", "Mar", "Mie", "Jue", "Vie", "Sab")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    nombresDias.forEach { nombre ->
                        Text(
                            text = nombre,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (nombre == "Dom" || nombre == "Sab") grisTexto else Color(0xFF0E947A)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val semanasNoviembre = listOf(
                    listOf(1, 2, 3, 4, 5, 6, 7),
                    listOf(8, 9, 10, 11, 12, 13, 14),
                    listOf(15, 16, 17, 18, 19, 20, 21),
                    listOf(22, 23, 24, 25, 26, 27, 28),
                    listOf(29, 30, 0, 0, 0, 0, 0)
                )

                semanasNoviembre.forEach { semana ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        semana.forEach { dia ->
                            if (dia != 0) {
                                val esSeleccionado = (dia == diaSeleccionado)
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(
                                            color = if (esSeleccionado) azulEncabezado else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable { diaSeleccionado = dia }, // Cambia el día seleccionado al tocarlo
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = dia.toString(),
                                        fontSize = 14.sp,
                                        color = if (esSeleccionado) Color.White else textoOscuro,
                                        fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(36.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // --- BARRA DE BÚSQUEDA ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = textoBusqueda,
                        onValueChange = { textoBusqueda = it },
                        placeholder = { Text("Buscar...", color = grisTexto.copy(alpha = 0.6f)) },
                        leadingIcon = { Text("🔍", fontSize = 16.sp) },
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedBorderColor = azulEncabezado
                        )
                    )

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.White, RoundedCornerShape(24.dp))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⚙️", fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- PESTAÑAS (Hoy, Próximos, Histórico) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val pestanas = listOf("Hoy", "Próximos", "Histórico")
                    pestanas.forEachIndexed { index, texto ->
                        val activa = (pestañaAgenda == index)
                        Surface(
                            onClick = { pestañaAgenda = index },
                            color = if (activa) azulEncabezado else Color.White,
                            shape = RoundedCornerShape(20.dp),
                            border = if (!activa) BorderStroke(1.dp, Color(0xFFE2E8F0)) else null
                        ) {
                            Text(
                                text = texto,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (activa) Color.White else grisTexto,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- FILTRADO DE TRATAMIENTOS POR DÍA SELECCIONADO ---
                val fechaBusquedaStr = String.format("%02d/11/2026", diaSeleccionado)
                val tratamientosFiltrados = listaTratamientos.filter { t ->
                    t.fechaInicio == fechaBusquedaStr &&
                            t.nombreTratamiento.contains(textoBusqueda, ignoreCase = true)
                }

                if (cargando) {
                    Box(modifier = Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = azulEncabezado)
                    }
                } else if (tratamientosFiltrados.isEmpty()) {
                    // --- ESTADO VACÍO ---
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .background(Color(0xFFE0F2FE), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📅", fontSize = 32.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Sin eventos para el día $diaSeleccionado de Noviembre",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textoOscuro
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Toca + para agregar un recordatorio",
                            fontSize = 13.sp,
                            color = grisTexto
                        )
                    }
                } else {
                    // --- LISTA DE TRATAMIENTOS ENCONTRADOS PARA ESE DÍA ---
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        tratamientosFiltrados.forEach { tratamiento ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("💊", fontSize = 18.sp)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = tratamiento.nombreTratamiento,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = textoOscuro
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "📅 ${tratamiento.fechaInicio} • Lugar: ${tratamiento.lugar}",
                                            fontSize = 13.sp,
                                            color = grisTexto
                                        )
                                        if (tratamiento.medicamentos.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            val m = tratamiento.medicamentos[0]
                                            Text(
                                                text = "Medicamento: ${m.nombre} (${m.dosis} ${m.unidad}, cada ${m.frecuenciaHoras}h)",
                                                fontSize = 12.sp,
                                                color = azulEncabezado,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFFEF3C7), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = tratamiento.estado,
                                            color = Color(0xFFD97706),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }

    // --- MENÚ FLOTANTE AL PRESIONAR "+ Agregar recordatorio" ---
    if (mostrarModalAgregar) {
        AlertDialog(
            onDismissRequest = { mostrarModalAgregar = false },
            title = { Text("Agregar a la agenda", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        onClick = {
                            mostrarModalAgregar = false
                            irCrearTratamiento() // Te manda directo a la pantalla de crear tratamiento
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("💊", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Nuevo tratamiento", fontSize = 15.sp, color = textoOscuro, fontWeight = FontWeight.Bold)
                        }
                    }
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    TextButton(
                        onClick = { mostrarModalAgregar = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("📅", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Nueva cita / veterinario", fontSize = 15.sp, color = textoOscuro)
                        }
                    }
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    TextButton(
                        onClick = { mostrarModalAgregar = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("✂️", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Nueva peluquería", fontSize = 15.sp, color = textoOscuro)
                        }
                    }
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    TextButton(
                        onClick = { mostrarModalAgregar = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("💉", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Nueva vacuna", fontSize = 15.sp, color = textoOscuro)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { mostrarModalAgregar = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }
}


// ==========================================
// PantallaHistorialMedico
// ==========================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHistorialMedico(
    idMascota: Any,
    registrarHistorial: () -> Unit,
    editarHistorial: (String) -> Unit,
    volver: () -> Unit
) {
    val azulEncabezado = Color(0xFF00B4D8)
    val rojoEliminar = Color(0xFFEF4444)
    val textoOscuro = Color(0xFF1E293B)
    val grisTexto = Color(0xFF64748B)

    data class HistorialItem(
        val id: String,
        val fecha: String,
        val motivo: String,
        val diagnostico: String,
        val tratamiento: String,
        val observaciones: String,
        val veterinario: String
    )

    var listaConsultas by remember { mutableStateOf(listOf<HistorialItem>()) }
    var cargando by remember { mutableStateOf(true) }
    var idsExpandidos by remember { mutableStateOf(setOf<String>()) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var idConsultaAEliminar by remember { mutableStateOf<String?>(null) }

    val db = FirebaseFirestore.getInstance()
    val mascotaIdStr = idMascota.toString()

    LaunchedEffect(mascotaIdStr) {
        db.collection("historial_medico")
            .whereEqualTo("idMascota", mascotaIdStr)
            .get()
            .addOnSuccessListener { result ->
                val temporal = mutableListOf<HistorialItem>()
                for (doc in result) {
                    temporal.add(
                        HistorialItem(
                            id = doc.id,
                            fecha = doc.getString("fecha") ?: "Sin fecha",
                            motivo = doc.getString("motivo") ?: "Sin motivo",
                            diagnostico = doc.getString("diagnostico") ?: "Sin diagnóstico",
                            tratamiento = doc.getString("tratamiento") ?: "Sin tratamiento",
                            observaciones = doc.getString("observaciones") ?: "Ninguna",
                            veterinario = doc.getString("veterinario") ?: "No asignado"
                        )
                    )
                }
                listaConsultas = temporal
                cargando = false
            }
            .addOnFailureListener {
                cargando = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial Médico", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = azulEncabezado),
                navigationIcon = {
                    IconButton(onClick = volver) {
                        Text("‹", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = registrarHistorial,
                containerColor = azulEncabezado,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Text("+", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { paddingValues ->
        if (cargando) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = azulEncabezado)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF8FAFC))
                    .padding(16.dp)
            ) {
                if (listaConsultas.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🩺", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No existen consultas registradas", fontSize = 15.sp, color = grisTexto, fontWeight = FontWeight.Medium)
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(listaConsultas) { consulta ->
                            val estaExpandido = idsExpandidos.contains(consulta.id)

                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        idsExpandidos = if (estaExpandido) {
                                            idsExpandidos - consulta.id
                                        } else {
                                            idsExpandidos + consulta.id
                                        }
                                    }
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(text = consulta.fecha, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = azulEncabezado)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = "Motivo: ${consulta.motivo}", fontSize = 14.sp, color = textoOscuro, fontWeight = FontWeight.Medium)
                                        }
                                        Text(
                                            text = if (estaExpandido) "▲" else "▼",
                                            fontSize = 12.sp,
                                            color = grisTexto
                                        )
                                    }

                                    if (estaExpandido) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .background(Color(0xFFE2E8F0))
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "Diagnóstico: ${consulta.diagnostico}", fontSize = 13.sp, color = grisTexto)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "Tratamiento: ${consulta.tratamiento}", fontSize = 13.sp, color = grisTexto)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "Observaciones: ${consulta.observaciones}", fontSize = 13.sp, color = grisTexto)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "Veterinario: ${consulta.veterinario}", fontSize = 13.sp, color = grisTexto)

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            TextButton(onClick = { editarHistorial(consulta.id) }) {
                                                Text("Editar", color = azulEncabezado, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            TextButton(onClick = {
                                                idConsultaAEliminar = consulta.id
                                                mostrarDialogoEliminar = true
                                            }) {
                                                Text("Eliminar", color = rojoEliminar, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Eliminar consulta", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas eliminar esta consulta médica? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        idConsultaAEliminar?.let { id ->
                            db.collection("historial_medico").document(id)
                                .delete()
                                .addOnSuccessListener {
                                    listaConsultas = listaConsultas.filter { it.id != id }
                                    mostrarDialogoEliminar = false
                                    idConsultaAEliminar = null
                                }
                        }
                    }
                ) {
                    Text("Sí, eliminar", color = rojoEliminar, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar", color = grisTexto)
                }
            }
        )
    }
}



// ==========================================
// PantallaTratamiento
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTratamientos(
    idMascota: String,
    irCrearTratamiento: () -> Unit,
    volver: () -> Unit
) {
    val azulEncabezado = Color(0xFF00B4D8)
    val grisTexto = Color(0xFF64748B)
    val textoOscuro = Color(0xFF1E293B)
    val db = FirebaseFirestore.getInstance()

    var listaTratamientos by remember { mutableStateOf(listOf<Tratamiento>()) }
    var cargando by remember { mutableStateOf(true) }
    var pestañaSeleccionada by remember { mutableStateOf(0) }
    var textoBusqueda by remember { mutableStateOf("") }

    // Estados para los diálogos de Editar y Eliminar
    var tratamientoAEliminar by remember { mutableStateOf<Tratamiento?>(null) }
    var tratamientoAEditar by remember { mutableStateOf<Tratamiento?>(null) }

    fun cargarDatos() {
        cargando = true
        db.collection("tratamientos")
            .whereEqualTo("idMascota", idMascota)
            .get()
            .addOnSuccessListener { result ->
                val temporal = mutableListOf<Tratamiento>()
                for (doc in result) {
                    val medsList = mutableListOf<SubMedicamento>()
                    val rawMeds = doc.get("medicamentos") as? List<*>
                    rawMeds?.let { list ->
                        for (m in list) {
                            val map = m as? Map<*, *>
                            if (map != null) {
                                medsList.add(
                                    SubMedicamento(
                                        tipo = map["tipo"] as? String ?: "Medicamento",
                                        via = map["via"] as? String ?: "Oral",
                                        nombre = map["nombre"] as? String ?: "",
                                        dosis = map["dosis"] as? String ?: "",
                                        unidad = map["unidad"] as? String ?: "ml",
                                        frecuenciaHoras = map["frecuenciaHoras"] as? String ?: "8",
                                        hora = map["hora"] as? String ?: "8:00 AM"
                                    )
                                )
                            }
                        }
                    }

                    temporal.add(
                        Tratamiento(
                            id = doc.id,
                            idMascota = doc.getString("idMascota") ?: "",
                            nombreTratamiento = doc.getString("nombreTratamiento") ?: "Sin nombre",
                            fechaInicio = doc.getString("fechaInicio") ?: "",
                            comentario = doc.getString("comentario") ?: "",
                            lugar = doc.getString("lugar") ?: "Casa",
                            veterinario = doc.getString("veterinario") ?: "",
                            recordatorioMinutos = doc.getLong("recordatorioMinutos")?.toInt() ?: 15,
                            medicamentos = medsList,
                            estado = doc.getString("estado") ?: "En proceso"
                        )
                    )
                }
                listaTratamientos = temporal
                cargando = false
            }
            .addOnFailureListener {
                cargando = false
            }
    }

    LaunchedEffect(idMascota) {
        cargarDatos()
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(azulEncabezado)) {
                TopAppBar(
                    title = { Text("Tratamientos", color = Color.White, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = azulEncabezado),
                    navigationIcon = {
                        IconButton(onClick = volver) {
                            Text("‹", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    listOf("Todos", "Veterinaria", "Propios").forEachIndexed { index, texto ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { pestañaSeleccionada = index }
                        ) {
                            Text(
                                text = texto,
                                color = if (pestañaSeleccionada == index) Color.White else Color.White.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            if (pestañaSeleccionada == index) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(modifier = Modifier.width(30.dp).height(3.dp).background(Color.White, RoundedCornerShape(2.dp)))
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = irCrearTratamiento,
                containerColor = azulEncabezado,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Text("+", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8FAFC))
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                placeholder = { Text("Buscar...", color = grisTexto) },
                leadingIcon = { Text("🔍", fontSize = 16.sp) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedBorderColor = azulEncabezado
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (cargando) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = azulEncabezado)
                }
            } else if (listaTratamientos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💊", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No hay tratamientos registrados", color = grisTexto, fontSize = 15.sp)
                    }
                }
            } else {
                val filtrados = listaTratamientos.filter {
                    it.nombreTratamiento.contains(textoBusqueda, ignoreCase = true)
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filtrados) { tratamiento ->
                        var expandedMenu by remember { mutableStateOf(false) }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = tratamiento.nombreTratamiento,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = textoOscuro,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFFEF3C7), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(text = tratamiento.estado, color = Color(0xFFD97706), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Botón de menú (Tres puntos)
                                    Box {
                                        IconButton(onClick = { expandedMenu = true }, modifier = Modifier.size(30.dp)) {
                                            Text("⋮", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = grisTexto)
                                        }
                                        DropdownMenu(
                                            expanded = expandedMenu,
                                            onDismissRequest = { expandedMenu = false }
                                        ) {
                                            DropdownMenuItem(
                                                text = { Text("Editar") },
                                                onClick = {
                                                    expandedMenu = false
                                                    tratamientoAEditar = tratamiento
                                                }
                                            )
                                            DropdownMenuItem(
                                                text = { Text("Eliminar", color = Color.Red) },
                                                onClick = {
                                                    expandedMenu = false
                                                    tratamientoAEliminar = tratamiento
                                                }
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "📅 ${tratamiento.fechaInicio}", fontSize = 13.sp, color = grisTexto)

                                if (tratamiento.medicamentos.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    for (med in tratamiento.medicamentos) {
                                        Text(text = "• ${med.nombre} (${med.dosis} ${med.unidad}, cada ${med.frecuenciaHoras}h)", fontSize = 13.sp, color = textoOscuro, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- DIÁLOGO DE CONFIRMACIÓN PARA ELIMINAR ---
    if (tratamientoAEliminar != null) {
        AlertDialog(
            onDismissRequest = { tratamientoAEliminar = null },
            title = { Text("Eliminar Tratamiento", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas eliminar este tratamiento?") },
            confirmButton = {
                Button(
                    onClick = {
                        val idDoc = tratamientoAEliminar!!.id
                        db.collection("tratamientos").document(idDoc)
                            .delete()
                            .addOnSuccessListener {
                                android.os.Handler(android.os.Looper.getMainLooper()).post {
                                    tratamientoAEliminar = null
                                    cargarDatos() // Refresca la lista automáticamente
                                }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { tratamientoAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // --- DIÁLOGO PARA EDITAR ---
    if (tratamientoAEditar != null) {
        var nuevoNombre by remember { mutableStateOf(tratamientoAEditar!!.nombreTratamiento) }
        var nuevoEstado by remember { mutableStateOf(tratamientoAEditar!!.estado) }

        AlertDialog(
            onDismissRequest = { tratamientoAEditar = null },
            title = { Text("Editar Tratamiento", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = nuevoNombre,
                        onValueChange = { nuevoNombre = it },
                        label = { Text("Nombre del tratamiento") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = nuevoEstado,
                        onValueChange = { nuevoEstado = it },
                        label = { Text("Estado (ej. En proceso, Finalizado)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val idDoc = tratamientoAEditar!!.id
                        val actualizacion = mapOf(
                            "nombreTratamiento" to nuevoNombre,
                            "estado" to nuevoEstado
                        )
                        db.collection("tratamientos").document(idDoc)
                            .update(actualizacion)
                            .addOnSuccessListener {
                                android.os.Handler(android.os.Looper.getMainLooper()).post {
                                    tratamientoAEditar = null
                                    cargarDatos() // Refresca la lista automáticamente
                                }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = azulEncabezado)
                ) {
                    Text("Guardar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { tratamientoAEditar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearTratamiento(
    idMascota: String,
    tratamientoCreado: () -> Unit,
    volver: () -> Unit
) {
    val azulEncabezado = Color(0xFF00B4D8)
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    var nombreTratamiento by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("08/11/2026") }
    var comentario by remember { mutableStateOf("") }
    var lugar by remember { mutableStateOf("Casa (A domicilio)") }
    var veterinario by remember { mutableStateOf("") }
    var recordatorioMinutos by remember { mutableStateOf(15) }

    var listaMedicamentosTemp by remember { mutableStateOf(listOf<SubMedicamento>()) }
    var mostrarModalMedicamento by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    fun abrirCalendario() {
        val cal = java.util.Calendar.getInstance()
        AndroidDatePickerDialog(context, { _, year, month, day ->
            fechaInicio = String.format("%02d/%02d/%d", day, month + 1, year)
        }, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH)).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Tratamiento", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = azulEncabezado),
                navigationIcon = {
                    IconButton(onClick = volver) {
                        Text("✕", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8FAFC))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nombreTratamiento,
                onValueChange = { nombreTratamiento = it },
                label = { Text("Nombre del tratamiento *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = fechaInicio,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = { Text("Fecha de inicio") },
                modifier = Modifier.fillMaxWidth().clickable { abrirCalendario() },
                shape = RoundedCornerShape(12.dp),
                trailingIcon = { Text("📅", fontSize = 18.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color(0xFF1E293B),
                    disabledBorderColor = Color(0xFFCBD5E1),
                    disabledLabelColor = Color(0xFF64748B),
                    disabledContainerColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Comentario / Observaciones") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = lugar,
                onValueChange = { lugar = it },
                label = { Text("Lugar") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = veterinario,
                onValueChange = { veterinario = it },
                label = { Text("Veterinario (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Medicamentos / Tratamientos", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { mostrarModalMedicamento = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = azulEncabezado)
            ) {
                Text("+ Agregar medicamento/tratamiento", fontWeight = FontWeight.Bold)
            }

            listaMedicamentosTemp.forEachIndexed { index, med ->
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = med.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = "${med.dosis} ${med.unidad} - Cada ${med.frecuenciaHoras}h", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                        TextButton(onClick = {
                            listaMedicamentosTemp = listaMedicamentosTemp.filterIndexed { i, _ -> i != index }
                        }) {
                            Text("Eliminar", color = Color.Red, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Avisar antes de cada dosis", fontSize = 13.sp, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(5, 10, 15).forEach { min ->
                    Button(
                        onClick = { recordatorioMinutos = min },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (recordatorioMinutos == min) azulEncabezado else Color(0xFFE2E8F0)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("$min min", color = if (recordatorioMinutos == min) Color.White else Color(0xFF1E293B), fontSize = 13.sp)
                    }
                }
            }

            if (mensajeError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(mensajeError, color = Color.Red, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombreTratamiento.isBlank()) {
                        mensajeError = "El nombre del tratamiento es obligatorio"
                        return@Button
                    }

                    val medsMapList = listaMedicamentosTemp.map {
                        mapOf(
                            "tipo" to it.tipo,
                            "via" to it.via,
                            "nombre" to it.nombre,
                            "dosis" to it.dosis,
                            "unidad" to it.unidad,
                            "frecuenciaHoras" to it.frecuenciaHoras,
                            "hora" to it.hora
                        )
                    }

                    val nuevoMap = mapOf(
                        "idMascota" to idMascota,
                        "nombreTratamiento" to nombreTratamiento,
                        "fechaInicio" to fechaInicio,
                        "comentario" to comentario,
                        "lugar" to lugar,
                        "veterinario" to veterinario,
                        "recordatorioMinutos" to recordatorioMinutos,
                        "medicamentos" to medsMapList,
                        "estado" to "En proceso"
                    )

                    db.collection("tratamientos")
                        .add(nuevoMap)
                        .addOnSuccessListener { tratamientoCreado() }
                        .addOnFailureListener { mensajeError = "Error al guardar en Firestore" }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = azulEncabezado),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Crear", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}



@Composable
fun DialogoAgregarSubMedicamento(
    onDismiss: () -> Unit,
    onAgregar: (SubMedicamento) -> Unit
) {
    var tipoSeleccionado by remember { mutableStateOf("Medicamento") }
    var viaSeleccionada by remember { mutableStateOf("Oral") }
    var nombreMed by remember { mutableStateOf("Medicamento 1") }
    var dosisMed by remember { mutableStateOf("1") }
    var unidadMed by remember { mutableStateOf("ml") }
    var frecuencia by remember { mutableStateOf("8") }
    var horaInicio by remember { mutableStateOf("8:00 AM") }

    val azulEncabezado = Color(0xFF00B4D8)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar medicamento/tratamiento", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Tipo", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Medicamento", "Vacuna", "Desparasita...", "Suplemento").forEach { tipo ->
                        Button(
                            onClick = { tipoSeleccionado = tipo },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (tipoSeleccionado == tipo) azulEncabezado else Color(0xFFF1F5F9)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(tipo, color = if (tipoSeleccionado == tipo) Color.White else Color(0xFF1E293B), fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("Vía de administración", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Oral", "Inyectable", "Tópico", "Nebulización").forEach { via ->
                        Button(
                            onClick = { viaSeleccionada = via },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (viaSeleccionada == via) azulEncabezado else Color(0xFFF1F5F9)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(via, color = if (viaSeleccionada == via) Color.White else Color(0xFF1E293B), fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = nombreMed,
                    onValueChange = { nombreMed = it },
                    label = { Text("Nombre *") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = dosisMed,
                        onValueChange = { dosisMed = it },
                        label = { Text("Dosis") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = unidadMed,
                        onValueChange = { unidadMed = it },
                        label = { Text("Unidad") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = frecuencia,
                        onValueChange = { frecuencia = it },
                        label = { Text("Cada (horas)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = horaInicio,
                        onValueChange = { horaInicio = it },
                        label = { Text("Hora") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombreMed.isNotBlank()) {
                        onAgregar(
                            SubMedicamento(
                                tipo = tipoSeleccionado,
                                via = viaSeleccionada,
                                nombre = nombreMed,
                                dosis = dosisMed,
                                unidad = unidadMed,
                                frecuenciaHoras = frecuencia,
                                hora = horaInicio
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = azulEncabezado)
            ) {
                Text("Añadir", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}