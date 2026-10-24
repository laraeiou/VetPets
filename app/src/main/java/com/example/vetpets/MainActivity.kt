package com.example.vetpets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vetpets.ui.theme.VetPetsTheme
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
        var idMascotaSeleccionada by remember { mutableStateOf(0) }
        var idHistorialSeleccionado by remember { mutableStateOf(0) }

        when (pantallaActual) {
            "inicio" -> {
                PantallaInicio(
                    irLogin = { pantallaActual = "login" },
                    irRegistro = { pantallaActual = "registro" }
                )
            }

            "login" -> {
                PantallaLogin(
                    iniciarSesionCorrecto = { id ->
                        idUsuario = id
                        pantallaActual = "principal"
                    },
                    volver = { pantallaActual = "inicio" }
                )
            }

            "registro" -> {
                PantallaRegistro(
                    registroCorrecto = { pantallaActual = "login" },
                    volver = { pantallaActual = "inicio" }
                )
            }

            "principal" -> {
                PantallaPrincipal(
                    irMascotas = { pantallaActual = "mascotas" },
                    irVeterinarios = { pantallaActual = "veterinarios" },
                    cerrarSesion = {
                        idUsuario = 0
                        idMascotaSeleccionada = 0
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
                PantallaRegistrarMascota(
                    idUsuario = idUsuario,
                    mascotaRegistrada = { pantallaActual = "mascotas" },
                    volver = { pantallaActual = "mascotas" }
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
                    editarHistorial = { id ->
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

            "veterinarios" -> {
                PantallaVeterinarios(
                    volver = { pantallaActual = "principal" }
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
        }
    }

    @Composable
    fun PantallaInicio(
        irLogin: () -> Unit,
        irRegistro: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F7FA)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(30.dp)
            ) {
                Text(text = "🐾", fontSize = 70.sp)
                Text(text = "VETPETS", fontSize = 32.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Sistema digital de historial médico para mascotas",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = irLogin,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar sesión")
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = irRegistro,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Crear cuenta")
                }
            }
        }
    }

    @Composable
    fun PantallaLogin(
        iniciarSesionCorrecto: (Int) -> Unit,
        volver: () -> Unit
    ) {
        var usuario by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }
        var mensaje by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(text = "🔐 Iniciar sesión", fontSize = 28.sp)
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (usuario.isBlank() || contrasena.isBlank()) {
                        mensaje = "Complete todos los campos"
                    } else {
                        UsuarioDAO.validarUsuario(usuario, contrasena) { id ->
                            if (id != null) {
                                iniciarSesionCorrecto(id)
                            } else {
                                mensaje = "Usuario o contraseña incorrectos"
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar")
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = volver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = mensaje)
        }
    }

    @Composable
    fun PantallaRegistro(
        registroCorrecto: () -> Unit,
        volver: () -> Unit
    ) {
        var nombre by remember { mutableStateOf("") }
        var usuario by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }
        var confirmar by remember { mutableStateOf("") }
        var mensaje by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "👤 Crear cuenta", fontSize = 28.sp)
            Spacer(modifier = Modifier.height(25.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = confirmar,
                onValueChange = { confirmar = it },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (nombre.isBlank() || usuario.isBlank() || contrasena.isBlank()) {
                        mensaje = "Complete todos los campos"
                    } else if (contrasena != confirmar) {
                        mensaje = "Las contraseñas no coinciden"
                    } else {
                        UsuarioDAO.usuarioExiste(usuario) { existe ->
                            if (!existe) {
                                UsuarioDAO.registrarUsuario(nombre, usuario, contrasena) { registrado ->
                                    if (registrado) {
                                        registroCorrecto()
                                    } else {
                                        mensaje = "No se pudo registrar el usuario"
                                    }
                                }
                            } else {
                                mensaje = "El usuario ya existe"
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarme")
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = volver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(mensaje)
        }
    }

    @Composable
    fun PantallaPrincipal(
        irMascotas: () -> Unit,
        irVeterinarios: () -> Unit,
        cerrarSesion: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()), // Habilita el desplazamiento vertical
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "🐾 VETPETS", fontSize = 30.sp)
            Text(text = "Panel principal", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(25.dp))

            TarjetaMenu(
                titulo = "🐾 Mis mascotas",
                descripcion = "Administra la información de tus mascotas",
                accion = irMascotas
            )

            Spacer(modifier = Modifier.height(12.dp))

            TarjetaMenu(
                titulo = "🩺 Veterinarios",
                descripcion = "Consulta los veterinarios registrados",
                accion = irVeterinarios
            )

            Spacer(modifier = Modifier.height(12.dp))

            TarjetaMenu(
                titulo = "💉 Historial de vacunas",
                descripcion = "Las vacunas se administran desde cada mascota",
                accion = irMascotas
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedButton(
                onClick = cerrarSesion,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🚪 Cerrar sesión")
            }
        }
    }

    @Composable
    fun TarjetaMenu(
        titulo: String,
        descripcion: String,
        accion: () -> Unit
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(text = titulo, fontSize = 21.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = descripcion)
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = accion) {
                    Text("Abrir")
                }
            }
        }
    }

    @Composable
    fun PantallaMascotas(
        idUsuario: Int,
        seleccionarMascota: (Int) -> Unit,
        registrarMascota: () -> Unit,
        volver: () -> Unit
    ) {
        var mascotas by remember { mutableStateOf(emptyList<Mascota>()) }
        var textoBusqueda by remember { mutableStateOf("") }

        LaunchedEffect(idUsuario) {
            MascotaDAO.obtenerMascotas(idUsuario) { lista ->
                mascotas = lista
            }
        }

        val mascotasFiltradas = mascotas.filter { mascota ->
            mascota.nombre.contains(textoBusqueda, ignoreCase = true) ||
                    mascota.especie.contains(textoBusqueda, ignoreCase = true) ||
                    mascota.raza.contains(textoBusqueda, ignoreCase = true)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(text = "🐾 Mis mascotas", fontSize = 28.sp)
            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = registrarMascota,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("➕ Registrar mascota")
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Botón de Volver al menú principal
            OutlinedButton(
                onClick = volver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("⬅️ Volver")
            }

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                label = { Text("Buscar mascota por nombre, especie...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(15.dp))

            if (mascotas.isEmpty()) {
                Text(text = "No tienes mascotas registradas.")
            } else if (mascotasFiltradas.isEmpty()) {
                Text(text = "No se encontraron mascotas con ese nombre o especie.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(mascotasFiltradas) { mascota ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(text = "🐾 ${mascota.nombre}", fontSize = 22.sp)
                                Text(text = "${mascota.especie} - ${mascota.raza}")
                                Text(text = "Sexo: ${mascota.sexo}")
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { seleccionarMascota(mascota.idMascota) }
                                ) {
                                    Text("Ver mascota")
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    @Composable
    fun PantallaRegistrarMascota(
        idUsuario: Int,
        mascotaRegistrada: () -> Unit,
        volver: () -> Unit
    ) {
        var nombre by remember { mutableStateOf("") }
        var especie by remember { mutableStateOf("") }
        var raza by remember { mutableStateOf("") }
        var sexo by remember { mutableStateOf("") }
        var fechaNacimiento by remember { mutableStateOf("") }
        var mensaje by remember { mutableStateOf("") }

        FormularioMascota(
            titulo = "➕ Registrar mascota",
            nombre = nombre,
            especie = especie,
            raza = raza,
            sexo = sexo,
            fechaNacimiento = fechaNacimiento,
            cambiarNombre = { nombre = it },
            cambiarEspecie = { especie = it },
            cambiarRaza = { raza = it },
            cambiarSexo = { sexo = it },
            cambiarFecha = { fechaNacimiento = it },
            guardar = {
                if (
                    nombre.isBlank() ||
                    especie.isBlank() ||
                    raza.isBlank() ||
                    sexo.isBlank() ||
                    fechaNacimiento.isBlank()
                ) {
                    mensaje = "Complete todos los campos"
                } else {
                    MascotaDAO.registrarMascota(
                        idUsuario,
                        nombre,
                        especie,
                        raza,
                        sexo,
                        fechaNacimiento
                    ) { resultado ->
                        if (resultado) {
                            mascotaRegistrada()
                        } else {
                            mensaje = "No se pudo registrar la mascota"
                        }
                    }
                }
            },
            volver = volver,
            mensaje = mensaje
        )
    }

    @Composable
    fun PantallaEditarMascota(
        idMascota: Int,
        mascotaActualizada: () -> Unit,
        volver: () -> Unit
    ) {
        var cargando by remember { mutableStateOf(true) }
        var nombre by remember { mutableStateOf("") }
        var especie by remember { mutableStateOf("") }
        var raza by remember { mutableStateOf("") }
        var sexo by remember { mutableStateOf("") }
        var fechaNacimiento by remember { mutableStateOf("") }
        var mensaje by remember { mutableStateOf("") }

        LaunchedEffect(idMascota) {
            MascotaDAO.obtenerMascotaPorId(idMascota) { mascota ->
                if (mascota != null) {
                    nombre = mascota.nombre
                    especie = mascota.especie
                    raza = mascota.raza
                    sexo = mascota.sexo
                    fechaNacimiento = mascota.fechaNacimiento
                }
                cargando = false
            }
        }

        if (cargando) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Cargando...")
            }
            return
        }

        FormularioMascota(
            titulo = "✏️ Editar mascota",
            nombre = nombre,
            especie = especie,
            raza = raza,
            sexo = sexo,
            fechaNacimiento = fechaNacimiento,
            cambiarNombre = { nombre = it },
            cambiarEspecie = { especie = it },
            cambiarRaza = { raza = it },
            cambiarSexo = { sexo = it },
            cambiarFecha = { fechaNacimiento = it },
            guardar = {
                MascotaDAO.actualizarMascota(
                    idMascota,
                    nombre,
                    especie,
                    raza,
                    sexo,
                    fechaNacimiento
                ) { resultado ->
                    if (resultado) {
                        mascotaActualizada()
                    } else {
                        mensaje = "No se pudo actualizar"
                    }
                }
            },
            volver = volver,
            mensaje = mensaje
        )
    }

    @Composable
    fun FormularioMascota(
        titulo: String,
        nombre: String,
        especie: String,
        raza: String,
        sexo: String,
        fechaNacimiento: String,
        cambiarNombre: (String) -> Unit,
        cambiarEspecie: (String) -> Unit,
        cambiarRaza: (String) -> Unit,
        cambiarSexo: (String) -> Unit,
        cambiarFecha: (String) -> Unit,
        guardar: () -> Unit,
        volver: () -> Unit,
        mensaje: String
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            item {
                Text(text = titulo, fontSize = 28.sp)
                Spacer(modifier = Modifier.height(20.dp))

                Campo(valor = nombre, cambio = cambiarNombre, etiqueta = "Nombre")
                Campo(valor = especie, cambio = cambiarEspecie, etiqueta = "Especie")
                Campo(valor = raza, cambio = cambiarRaza, etiqueta = "Raza")
                Campo(valor = sexo, cambio = cambiarSexo, etiqueta = "Sexo")
                Campo(valor = fechaNacimiento, cambio = cambiarFecha, etiqueta = "Fecha de nacimiento (YYYY-MM-DD)")

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    onClick = guardar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar")
                }

                Spacer(modifier = Modifier.height(5.dp))

                OutlinedButton(
                    onClick = volver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(mensaje)
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

    @Composable
    fun PantallaMascotaSeleccionada(
        idMascota: Int,
        irHistorial: () -> Unit,
        editarMascota: () -> Unit,
        eliminarMascota: () -> Unit,
        irVacunas: () -> Unit,
        irMedicamentos: () -> Unit,
        volver: () -> Unit
    ) {
        var mascota by remember { mutableStateOf<Mascota?>(null) }
        var mostrarConfirmacion by remember { mutableStateOf(false) }

        LaunchedEffect(idMascota) {
            MascotaDAO.obtenerMascotaPorId(idMascota) { resultado ->
                mascota = resultado
            }
        }

        if (mostrarConfirmacion) {
            AlertDialog(
                onDismissRequest = { mostrarConfirmacion = false },
                title = { Text("Eliminar mascota") },
                text = { Text("¿Desea eliminar esta mascota?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            MascotaDAO.eliminarMascota(idMascota) {
                                mostrarConfirmacion = false
                                eliminarMascota()
                            }
                        }
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarConfirmacion = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            item {
                Text(text = "🐾 Información de mascota", fontSize = 28.sp)
                Spacer(modifier = Modifier.height(15.dp))

                if (mascota == null) {
                    Text("Cargando mascota...")
                } else {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(text = "🐾 ${mascota!!.nombre}", fontSize = 25.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Especie: ${mascota!!.especie}")
                            Text("Raza: ${mascota!!.raza}")
                            Text("Sexo: ${mascota!!.sexo}")
                            Text("Fecha de nacimiento: ${mascota!!.fechaNacimiento}")
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Button(
                        onClick = irHistorial,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🩺 Historial médico")
                    }

                    Button(
                        onClick = irVacunas,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("💉 Vacunas")
                    }

                    Button(
                        onClick = irMedicamentos,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("💊 Medicamentos")
                    }

                    OutlinedButton(
                        onClick = editarMascota,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("✏️ Editar mascota")
                    }

                    OutlinedButton(
                        onClick = { mostrarConfirmacion = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🗑️ Eliminar mascota")
                    }
                }

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

    @Composable
    fun PantallaHistorialMedico(
        idMascota: Int,
        registrarHistorial: () -> Unit,
        editarHistorial: (Int) -> Unit,
        volver: () -> Unit
    ) {
        var historial by remember { mutableStateOf(emptyList<HistorialMedico>()) }
        var idEliminar by remember { mutableStateOf(0) }

        LaunchedEffect(idMascota) {
            HistorialMedicoDAO.obtenerHistorial(idMascota) { lista ->
                historial = lista
            }
        }

        if (idEliminar != 0) {
            AlertDialog(
                onDismissRequest = { idEliminar = 0 },
                title = { Text("Eliminar consulta") },
                text = { Text("¿Desea eliminar esta consulta médica?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val id = idEliminar
                            idEliminar = 0
                            HistorialMedicoDAO.eliminarHistorial(id) {
                                HistorialMedicoDAO.obtenerHistorial(idMascota) { nuevaLista ->
                                    historial = nuevaLista
                                }
                            }
                        }
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { idEliminar = 0 }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(text = "🩺 Historial médico", fontSize = 28.sp)
            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = registrarHistorial,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("➕ Registrar consulta")
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (historial.isEmpty()) {
                Text("No existen consultas registradas.")
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(historial) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(text = "🩺 Consulta", fontSize = 21.sp)
                                Text("Fecha: ${item.fecha}")
                                Text("Motivo: ${item.motivo}")
                                Text("Diagnóstico: ${item.diagnostico}")
                                Text("Tratamiento: ${item.tratamiento}")
                                Text("Veterinario: ${item.nombreVeterinario}")

                                if (item.observaciones.isNotBlank()) {
                                    Text("Observaciones: ${item.observaciones}")
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row {
                                    Button(
                                        onClick = { editarHistorial(item.idHistorial) }
                                    ) {
                                        Text("Editar")
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    OutlinedButton(
                                        onClick = { idEliminar = item.idHistorial }
                                    ) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = volver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }

    @Composable
    fun PantallaRegistrarHistorial(
        idMascota: Int,
        historialRegistrado: () -> Unit,
        volver: () -> Unit
    ) {
        var fecha by remember { mutableStateOf("") }
        var motivo by remember { mutableStateOf("") }
        var diagnostico by remember { mutableStateOf("") }
        var tratamiento by remember { mutableStateOf("") }
        var observaciones by remember { mutableStateOf("") }
        var veterinarios by remember { mutableStateOf(emptyList<Veterinario>()) }
        var veterinarioSeleccionado by remember { mutableStateOf<Int?>(null) }
        var nombreVetSeleccionado by remember { mutableStateOf("No asignado") }
        var mensaje by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            VeterinarioDAO.obtenerVeterinarios { lista ->
                veterinarios = lista
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            item {
                Text("➕ Registrar consulta", fontSize = 28.sp)
                Spacer(modifier = Modifier.height(20.dp))

                Campo(fecha, { fecha = it }, "Fecha (YYYY-MM-DD)")
                Campo(motivo, { motivo = it }, "Motivo")
                Campo(diagnostico, { diagnostico = it }, "Diagnóstico")
                Campo(tratamiento, { tratamiento = it }, "Tratamiento")
                Campo(observaciones, { observaciones = it }, "Observaciones")

                Spacer(modifier = Modifier.height(10.dp))

                Text("Veterinario")

                veterinarios.forEach { veterinario ->
                    OutlinedButton(
                        onClick = {
                            veterinarioSeleccionado = veterinario.idVeterinario
                            nombreVetSeleccionado = veterinario.nombre
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (veterinarioSeleccionado == veterinario.idVeterinario) {
                                "✓ ${veterinario.nombre}"
                            } else {
                                veterinario.nombre
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    onClick = {
                        if (
                            fecha.isBlank() ||
                            motivo.isBlank() ||
                            diagnostico.isBlank() ||
                            tratamiento.isBlank()
                        ) {
                            mensaje = "Complete los campos obligatorios"
                        } else {
                            HistorialMedicoDAO.registrarHistorial(
                                idMascota,
                                fecha,
                                motivo,
                                diagnostico,
                                tratamiento,
                                observaciones,
                                veterinarioSeleccionado,
                                nombreVetSeleccionado
                            ) { resultado ->
                                if (resultado) {
                                    historialRegistrado()
                                } else {
                                    mensaje = "No se pudo registrar"
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar consulta")
                }

                Spacer(modifier = Modifier.height(5.dp))

                OutlinedButton(
                    onClick = volver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(mensaje)
            }
        }
    }

    @Composable
    fun PantallaEditarHistorial(
        idHistorial: Int,
        historialActualizado: () -> Unit,
        volver: () -> Unit
    ) {
        var cargando by remember { mutableStateOf(true) }
        var fecha by remember { mutableStateOf("") }
        var motivo by remember { mutableStateOf("") }
        var diagnostico by remember { mutableStateOf("") }
        var tratamiento by remember { mutableStateOf("") }
        var observaciones by remember { mutableStateOf("") }
        var veterinarios by remember { mutableStateOf(emptyList<Veterinario>()) }
        var veterinarioSeleccionado by remember { mutableStateOf<Int?>(null) }
        var nombreVetSeleccionado by remember { mutableStateOf("No asignado") }
        var mensaje by remember { mutableStateOf("") }

        LaunchedEffect(idHistorial) {
            HistorialMedicoDAO.obtenerHistorialPorId(idHistorial) { historial ->
                if (historial != null) {
                    fecha = historial.fecha
                    motivo = historial.motivo
                    diagnostico = historial.diagnostico
                    tratamiento = historial.tratamiento
                    observaciones = historial.observaciones
                    veterinarioSeleccionado = historial.idVeterinario
                    nombreVetSeleccionado = historial.nombreVeterinario
                }
                cargando = false
            }
        }

        LaunchedEffect(Unit) {
            VeterinarioDAO.obtenerVeterinarios { lista ->
                veterinarios = lista
            }
        }

        if (cargando) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Cargando...")
            }
            return
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            item {
                Text("✏️ Editar consulta", fontSize = 28.sp)
                Spacer(modifier = Modifier.height(20.dp))

                Campo(fecha, { fecha = it }, "Fecha")
                Campo(motivo, { motivo = it }, "Motivo")
                Campo(diagnostico, { diagnostico = it }, "Diagnóstico")
                Campo(tratamiento, { tratamiento = it }, "Tratamiento")
                Campo(observaciones, { observaciones = it }, "Observaciones")

                Spacer(modifier = Modifier.height(10.dp))

                Text("Veterinario")

                veterinarios.forEach { veterinario ->
                    OutlinedButton(
                        onClick = {
                            veterinarioSeleccionado = veterinario.idVeterinario
                            nombreVetSeleccionado = veterinario.nombre
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (veterinarioSeleccionado == veterinario.idVeterinario) {
                                "✓ ${veterinario.nombre}"
                            } else {
                                veterinario.nombre
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    onClick = {
                        HistorialMedicoDAO.actualizarHistorial(
                            idHistorial,
                            fecha,
                            motivo,
                            diagnostico,
                            tratamiento,
                            observaciones,
                            veterinarioSeleccionado,
                            nombreVetSeleccionado
                        ) { resultado ->
                            if (resultado) {
                                historialActualizado()
                            } else {
                                mensaje = "No se pudo actualizar"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar cambios")
                }

                Spacer(modifier = Modifier.height(5.dp))

                OutlinedButton(
                    onClick = volver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(mensaje)
            }
        }
    }

    @Composable
    fun PantallaVeterinarios(
        volver: () -> Unit
    ) {
        var veterinarios by remember { mutableStateOf(emptyList<Veterinario>()) }

        LaunchedEffect(Unit) {
            VeterinarioDAO.obtenerVeterinarios { lista ->
                veterinarios = lista
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text("🩺 Veterinarios", fontSize = 28.sp)
            Spacer(modifier = Modifier.height(15.dp))

            if (veterinarios.isEmpty()) {
                Text("No hay veterinarios registrados.")
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(veterinarios) { veterinario ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text("🩺 ${veterinario.nombre}", fontSize = 21.sp)
                                Text("Especialidad: ${veterinario.especialidad}")
                                Text("Teléfono: ${veterinario.telefono}")
                                Text("Correo: ${veterinario.correo}")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = volver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }

    @Composable
    fun PantallaVacunas(
        idMascota: Int,
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

    @Composable
    fun PantallaMedicamentos(
        idMascota: Int,
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

@Composable
fun PantallaRegistrarVeterinario(
    onGuardadoExitoso: () -> Unit,
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(text = "👨‍⚕️ Registrar Veterinario", fontSize = 26.sp)
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del veterinario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = especialidad,
            onValueChange = { especialidad = it },
            label = { Text("Especialidad (ej. Cirugía, General)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(15.dp))

        if (mensajeError.isNotEmpty()) {
            Text(text = mensajeError, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Botón para Guardar usando tu VeterinarioDAO
        Button(
            onClick = {
                if (nombre.isBlank() || especialidad.isBlank() || telefono.isBlank() || correo.isBlank()) {
                    mensajeError = "Por favor completa todos los campos"
                } else {
                    VeterinarioDAO.registrarVeterinario(
                        nombre = nombre,
                        especialidad = especialidad,
                        telefono = telefono,
                        correo = correo
                    ) { exito ->
                        if (exito) {
                            onGuardadoExitoso() // Regresa a la lista o menú
                        } else {
                            mensajeError = "Error al guardar en Firebase"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Veterinario")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}