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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.vetpets.ui.theme.VetPetsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {

            VetPetsTheme {

                AppVetPets()
            }
        }
    }


    @androidx.compose.runtime.Composable
    fun AppVetPets() {

        var pantallaActual by remember {
            mutableStateOf("inicio")
        }

        var idUsuario by remember {
            mutableStateOf(0)
        }

        var idMascotaSeleccionada by remember {
            mutableStateOf(0)
        }

        var idHistorialSeleccionado by remember {
            mutableStateOf(0)
        }


        when (pantallaActual) {

            "inicio" -> {

                PantallaInicio(

                    irLogin = {
                        pantallaActual = "login"
                    },

                    irRegistro = {
                        pantallaActual = "registro"
                    }
                )
            }


            "login" -> {

                PantallaLogin(

                    iniciarSesionCorrecto = { id ->

                        idUsuario = id

                        pantallaActual =
                            "principal"
                    },

                    volver = {
                        pantallaActual =
                            "inicio"
                    }
                )
            }


            "registro" -> {

                PantallaRegistro(

                    registroCorrecto = {

                        pantallaActual =
                            "login"
                    },

                    volver = {

                        pantallaActual =
                            "inicio"
                    }
                )
            }


            "principal" -> {

                PantallaPrincipal(

                    irMascotas = {

                        pantallaActual =
                            "mascotas"
                    },

                    irVeterinarios = {

                        pantallaActual =
                            "veterinarios"
                    },

                    cerrarSesion = {

                        idUsuario = 0

                        idMascotaSeleccionada = 0

                        pantallaActual =
                            "inicio"
                    }
                )
            }


            "mascotas" -> {

                PantallaMascotas(

                    idUsuario = idUsuario,

                    seleccionarMascota = { id ->

                        idMascotaSeleccionada =
                            id

                        pantallaActual =
                            "mascotaSeleccionada"
                    },

                    registrarMascota = {

                        pantallaActual =
                            "registrarMascota"
                    },

                    volver = {

                        pantallaActual =
                            "principal"
                    }
                )
            }


            "registrarMascota" -> {

                PantallaRegistrarMascota(

                    idUsuario = idUsuario,

                    mascotaRegistrada = {

                        pantallaActual =
                            "mascotas"
                    },

                    volver = {

                        pantallaActual =
                            "mascotas"
                    }
                )
            }


            "editarMascota" -> {

                PantallaEditarMascota(

                    idMascota =
                        idMascotaSeleccionada,

                    mascotaActualizada = {

                        pantallaActual =
                            "mascotaSeleccionada"
                    },

                    volver = {

                        pantallaActual =
                            "mascotaSeleccionada"
                    }
                )
            }


            "mascotaSeleccionada" -> {

                PantallaMascotaSeleccionada(

                    idMascota =
                        idMascotaSeleccionada,

                    irHistorial = {

                        pantallaActual =
                            "historial"
                    },

                    editarMascota = {

                        pantallaActual =
                            "editarMascota"
                    },

                    eliminarMascota = {

                        pantallaActual =
                            "mascotas"
                    },

                    irVacunas = {

                        pantallaActual =
                            "vacunas"
                    },

                    irMedicamentos = {

                        pantallaActual =
                            "medicamentos"
                    },

                    volver = {

                        pantallaActual =
                            "mascotas"
                    }
                )
            }


            "historial" -> {

                PantallaHistorialMedico(

                    idMascota =
                        idMascotaSeleccionada,

                    registrarHistorial = {

                        pantallaActual =
                            "registrarHistorial"
                    },

                    editarHistorial = { id ->

                        idHistorialSeleccionado =
                            id

                        pantallaActual =
                            "editarHistorial"
                    },

                    volver = {

                        pantallaActual =
                            "mascotaSeleccionada"
                    }
                )
            }


            "registrarHistorial" -> {

                PantallaRegistrarHistorial(

                    idMascota =
                        idMascotaSeleccionada,

                    historialRegistrado = {

                        pantallaActual =
                            "historial"
                    },

                    volver = {

                        pantallaActual =
                            "historial"
                    }
                )
            }


            "editarHistorial" -> {

                PantallaEditarHistorial(

                    idHistorial =
                        idHistorialSeleccionado,

                    historialActualizado = {

                        pantallaActual =
                            "historial"
                    },

                    volver = {

                        pantallaActual =
                            "historial"
                    }
                )
            }


            "veterinarios" -> {

                PantallaVeterinarios(

                    volver = {

                        pantallaActual =
                            "principal"
                    }
                )
            }


            "vacunas" -> {

                PantallaVacunas(

                    idMascota =
                        idMascotaSeleccionada,

                    volver = {

                        pantallaActual =
                            "mascotaSeleccionada"
                    }
                )
            }


            "medicamentos" -> {

                PantallaMedicamentos(

                    idMascota =
                        idMascotaSeleccionada,

                    volver = {

                        pantallaActual =
                            "mascotaSeleccionada"
                    }
                )
            }
        }
    }


    @androidx.compose.runtime.Composable
    fun PantallaInicio(
        irLogin: () -> Unit,
        irRegistro: () -> Unit
    ) {

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Color(
                            0xFFF4F7FA
                        )
                    ),
            contentAlignment =
                Alignment.Center
        ) {

            Column(

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.Center,

                modifier =
                    Modifier.padding(30.dp)

            ) {

                Text(
                    text = "🐾",
                    fontSize = 70.sp
                )

                Text(
                    text = "VETPETS",
                    fontSize = 32.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Text(
                    text =
                        "Sistema digital de historial médico para mascotas",
                    fontSize = 16.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(30.dp)
                )

                Button(
                    onClick = irLogin,
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("Iniciar sesión")
                }

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                OutlinedButton(
                    onClick = irRegistro,
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("Crear cuenta")
                }
            }
        }
    }


    @androidx.compose.runtime.Composable
    fun PantallaLogin(
        iniciarSesionCorrecto: (Int) -> Unit,
        volver: () -> Unit
    ) {

        var usuario by remember {
            mutableStateOf("")
        }

        var contrasena by remember {
            mutableStateOf("")
        }

        var mensaje by remember {
            mutableStateOf("")
        }

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(25.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally

        ) {

            Spacer(
                modifier =
                    Modifier.height(40.dp)
            )

            Text(
                text = "🔐 Iniciar sesión",
                fontSize = 28.sp
            )

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )

            OutlinedTextField(
                value = usuario,
                onValueChange = {
                    usuario = it
                },
                label = {
                    Text("Usuario")
                },
                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                },
                label = {
                    Text("Contraseña")
                },
                visualTransformation =
                    PasswordVisualTransformation(),
                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            Button(
                onClick = {

                    if (
                        usuario.isBlank() ||
                        contrasena.isBlank()
                    ) {

                        mensaje =
                            "Complete todos los campos"

                    } else {

                        lifecycleScope.launch(
                            Dispatchers.IO
                        ) {

                            val id =
                                UsuarioDAO.validarUsuario(
                                    usuario,
                                    contrasena
                                )

                            withContext(
                                Dispatchers.Main
                            ) {

                                if (id != null) {

                                    iniciarSesionCorrecto(
                                        id
                                    )

                                } else {

                                    mensaje =
                                        "Usuario o contraseña incorrectos"
                                }
                            }
                        }
                    }
                },
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text("Ingresar")
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            OutlinedButton(
                onClick = volver,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text("Volver")
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            Text(
                text = mensaje
            )
        }
    }


    @androidx.compose.runtime.Composable
    fun PantallaRegistro(
        registroCorrecto: () -> Unit,
        volver: () -> Unit
    ) {

        var nombre by remember {
            mutableStateOf("")
        }

        var usuario by remember {
            mutableStateOf("")
        }

        var contrasena by remember {
            mutableStateOf("")
        }

        var confirmar by remember {
            mutableStateOf("")
        }

        var mensaje by remember {
            mutableStateOf("")
        }

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(25.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally

        ) {

            Text(
                text = "👤 Crear cuenta",
                fontSize = 28.sp
            )

            Spacer(
                modifier =
                    Modifier.height(25.dp)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                },
                label = {
                    Text("Nombre")
                },
                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            OutlinedTextField(
                value = usuario,
                onValueChange = {
                    usuario = it
                },
                label = {
                    Text("Usuario")
                },
                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                },
                label = {
                    Text("Contraseña")
                },
                visualTransformation =
                    PasswordVisualTransformation(),
                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            OutlinedTextField(
                value = confirmar,
                onValueChange = {
                    confirmar = it
                },
                label = {
                    Text("Confirmar contraseña")
                },
                visualTransformation =
                    PasswordVisualTransformation(),
                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            Button(
                onClick = {

                    if (
                        nombre.isBlank() ||
                        usuario.isBlank() ||
                        contrasena.isBlank()
                    ) {

                        mensaje =
                            "Complete todos los campos"

                    } else if (
                        contrasena != confirmar
                    ) {

                        mensaje =
                            "Las contraseñas no coinciden"

                    } else {

                        lifecycleScope.launch(
                            Dispatchers.IO
                        ) {

                            val existe =
                                UsuarioDAO.usuarioExiste(
                                    usuario
                                )

                            val registrado =
                                if (!existe) {

                                    UsuarioDAO.registrarUsuario(
                                        nombre,
                                        usuario,
                                        contrasena
                                    )

                                } else {

                                    false
                                }

                            withContext(
                                Dispatchers.Main
                            ) {

                                if (registrado) {

                                    registroCorrecto()

                                } else {

                                    mensaje =
                                        "El usuario ya existe o no se pudo registrar"
                                }
                            }
                        }
                    }
                },
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text("Registrarme")
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            OutlinedButton(
                onClick = volver,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text("Volver")
            }

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            Text(mensaje)
        }
    }


    @androidx.compose.runtime.Composable
    fun PantallaPrincipal(
        irMascotas: () -> Unit,
        irVeterinarios: () -> Unit,
        cerrarSesion: () -> Unit
    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)

        ) {

            Text(
                text = "🐾 VETPETS",
                fontSize = 30.sp
            )

            Text(
                text = "Panel principal",
                fontSize = 18.sp
            )

            Spacer(
                modifier =
                    Modifier.height(25.dp)
            )

            TarjetaMenu(
                titulo = "🐾 Mis mascotas",
                descripcion =
                    "Administra la información de tus mascotas",
                accion = irMascotas
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            TarjetaMenu(
                titulo = "🩺 Veterinarios",
                descripcion =
                    "Consulta los veterinarios registrados",
                accion = irVeterinarios
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            TarjetaMenu(
                titulo = "💉 Historial de vacunas",
                descripcion =
                    "Las vacunas se administran desde cada mascota",
                accion = irMascotas
            )

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )

            OutlinedButton(
                onClick = cerrarSesion,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text("🚪 Cerrar sesión")
            }
        }
    }


    @androidx.compose.runtime.Composable
    fun TarjetaMenu(
        titulo: String,
        descripcion: String,
        accion: () -> Unit
    ) {

        Card(

            modifier =
                Modifier.fillMaxWidth(),

            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                )

        ) {

            Column(
                modifier =
                    Modifier.padding(18.dp)
            ) {

                Text(
                    text = titulo,
                    fontSize = 21.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                Text(
                    text = descripcion
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Button(
                    onClick = accion
                ) {

                    Text("Abrir")
                }
            }
        }
    }


    @androidx.compose.runtime.Composable
    fun PantallaMascotas(
        idUsuario: Int,
        seleccionarMascota: (Int) -> Unit,
        registrarMascota: () -> Unit,
        volver: () -> Unit
    ) {

        var mascotas by remember {
            mutableStateOf(
                emptyList<Mascota>()
            )
        }

        LaunchedEffect(idUsuario) {

            mascotas =
                withContext(
                    Dispatchers.IO
                ) {

                    MascotaDAO.obtenerMascotas(
                        idUsuario
                    )
                }
        }

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
        ) {

            Text(
                text = "🐾 Mis mascotas",
                fontSize = 28.sp
            )

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            Button(
                onClick = registrarMascota,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text("➕ Registrar mascota")
            }

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            if (mascotas.isEmpty()) {

                Text(
                    text =
                        "No tienes mascotas registradas."
                )

            } else {

                LazyColumn {

                    items(mascotas) { mascota ->

                        Card(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 6.dp
                                    )
                        ) {

                            Column(
                                modifier =
                                    Modifier.padding(18.dp)
                            ) {

                                Text(
                                    text =
                                        "🐾 ${mascota.nombre}",
                                    fontSize = 22.sp
                                )

                                Text(
                                    text =
                                        "${mascota.especie} - ${mascota.raza}"
                                )

                                Text(
                                    text =
                                        "Sexo: ${mascota.sexo}"
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(8.dp)
                                )

                                Button(
                                    onClick = {
                                        seleccionarMascota(
                                            mascota.idMascota
                                        )
                                    }
                                ) {

                                    Text("Ver mascota")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            OutlinedButton(
                onClick = volver,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text("Volver")
            }
        }
    }


    @androidx.compose.runtime.Composable
    fun PantallaRegistrarMascota(
        idUsuario: Int,
        mascotaRegistrada: () -> Unit,
        volver: () -> Unit
    ) {

        var nombre by remember {
            mutableStateOf("")
        }

        var especie by remember {
            mutableStateOf("")
        }

        var raza by remember {
            mutableStateOf("")
        }

        var sexo by remember {
            mutableStateOf("")
        }

        var fechaNacimiento by remember {
            mutableStateOf("")
        }

        var mensaje by remember {
            mutableStateOf("")
        }

        FormularioMascota(

            titulo = "➕ Registrar mascota",

            nombre = nombre,

            especie = especie,

            raza = raza,

            sexo = sexo,

            fechaNacimiento = fechaNacimiento,

            cambiarNombre = {
                nombre = it
            },

            cambiarEspecie = {
                especie = it
            },

            cambiarRaza = {
                raza = it
            },

            cambiarSexo = {
                sexo = it
            },

            cambiarFecha = {
                fechaNacimiento = it
            },

            guardar = {

                if (
                    nombre.isBlank() ||
                    especie.isBlank() ||
                    raza.isBlank() ||
                    sexo.isBlank() ||
                    fechaNacimiento.isBlank()
                ) {

                    mensaje =
                        "Complete todos los campos"

                } else {

                    lifecycleScope.launch(
                        Dispatchers.IO
                    ) {

                        val resultado =
                            MascotaDAO.registrarMascota(
                                idUsuario,
                                nombre,
                                especie,
                                raza,
                                sexo,
                                fechaNacimiento
                            )

                        withContext(
                            Dispatchers.Main
                        ) {

                            if (resultado) {

                                mascotaRegistrada()

                            } else {

                                mensaje =
                                    "No se pudo registrar la mascota"
                            }
                        }
                    }
                }
            },

            volver = volver,

            mensaje = mensaje
        )
    }


    @androidx.compose.runtime.Composable
    fun PantallaEditarMascota(
        idMascota: Int,
        mascotaActualizada: () -> Unit,
        volver: () -> Unit
    ) {

        var cargando by remember {
            mutableStateOf(true)
        }

        var nombre by remember {
            mutableStateOf("")
        }

        var especie by remember {
            mutableStateOf("")
        }

        var raza by remember {
            mutableStateOf("")
        }

        var sexo by remember {
            mutableStateOf("")
        }

        var fechaNacimiento by remember {
            mutableStateOf("")
        }

        var mensaje by remember {
            mutableStateOf("")
        }

        LaunchedEffect(idMascota) {

            val mascota =
                withContext(
                    Dispatchers.IO
                ) {

                    MascotaDAO.obtenerMascotaPorId(
                        idMascota
                    )
                }

            if (mascota != null) {

                nombre =
                    mascota.nombre

                especie =
                    mascota.especie

                raza =
                    mascota.raza

                sexo =
                    mascota.sexo

                fechaNacimiento =
                    mascota.fechaNacimiento
            }

            cargando = false
        }

        if (cargando) {

            Box(
                modifier =
                    Modifier.fillMaxSize(),
                contentAlignment =
                    Alignment.Center
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

            fechaNacimiento =
                fechaNacimiento,

            cambiarNombre = {
                nombre = it
            },

            cambiarEspecie = {
                especie = it
            },

            cambiarRaza = {
                raza = it
            },

            cambiarSexo = {
                sexo = it
            },

            cambiarFecha = {
                fechaNacimiento = it
            },

            guardar = {

                lifecycleScope.launch(
                    Dispatchers.IO
                ) {

                    val resultado =
                        MascotaDAO.actualizarMascota(
                            idMascota,
                            nombre,
                            especie,
                            raza,
                            sexo,
                            fechaNacimiento
                        )

                    withContext(
                        Dispatchers.Main
                    ) {

                        if (resultado) {

                            mascotaActualizada()

                        } else {

                            mensaje =
                                "No se pudo actualizar"
                        }
                    }
                }
            },

            volver = volver,

            mensaje = mensaje
        )
    }


    @androidx.compose.runtime.Composable
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
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
        ) {

            item {

                Text(
                    text = titulo,
                    fontSize = 28.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Campo(
                    valor = nombre,
                    cambio = cambiarNombre,
                    etiqueta = "Nombre"
                )

                Campo(
                    valor = especie,
                    cambio = cambiarEspecie,
                    etiqueta = "Especie"
                )

                Campo(
                    valor = raza,
                    cambio = cambiarRaza,
                    etiqueta = "Raza"
                )

                Campo(
                    valor = sexo,
                    cambio = cambiarSexo,
                    etiqueta = "Sexo"
                )

                Campo(
                    valor = fechaNacimiento,
                    cambio = cambiarFecha,
                    etiqueta =
                        "Fecha de nacimiento (YYYY-MM-DD)"
                )

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                Button(
                    onClick = guardar,
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("Guardar")
                }

                OutlinedButton(
                    onClick = volver,
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("Cancelar")
                }

                Text(mensaje)
            }
        }
    }


    @androidx.compose.runtime.Composable
    fun Campo(
        valor: String,
        cambio: (String) -> Unit,
        etiqueta: String
    ) {

        OutlinedTextField(
            value = valor,
            onValueChange = cambio,
            label = {
                Text(etiqueta)
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 5.dp
                    )
        )
    }


    @androidx.compose.runtime.Composable
    fun PantallaMascotaSeleccionada(

        idMascota: Int,

        irHistorial: () -> Unit,

        editarMascota: () -> Unit,

        eliminarMascota: () -> Unit,

        irVacunas: () -> Unit,

        irMedicamentos: () -> Unit,

        volver: () -> Unit

    ) {

        var mascota by remember {
            mutableStateOf<Mascota?>(null)
        }

        var mostrarConfirmacion by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(idMascota) {

            mascota =
                withContext(
                    Dispatchers.IO
                ) {

                    MascotaDAO.obtenerMascotaPorId(
                        idMascota
                    )
                }
        }

        if (mostrarConfirmacion) {

            AlertDialog(

                onDismissRequest = {
                    mostrarConfirmacion =
                        false
                },

                title = {
                    Text("Eliminar mascota")
                },

                text = {
                    Text(
                        "¿Desea eliminar esta mascota?"
                    )
                },

                confirmButton = {

                    TextButton(
                        onClick = {

                            lifecycleScope.launch(
                                Dispatchers.IO
                            ) {

                                MascotaDAO.eliminarMascota(
                                    idMascota
                                )

                                withContext(
                                    Dispatchers.Main
                                ) {

                                    mostrarConfirmacion =
                                        false

                                    eliminarMascota()
                                }
                            }
                        }
                    ) {

                        Text("Eliminar")
                    }
                },

                dismissButton = {

                    TextButton(
                        onClick = {

                            mostrarConfirmacion =
                                false
                        }
                    ) {

                        Text("Cancelar")
                    }
                }
            )
        }

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
        ) {

            item {

                Text(
                    text =
                        "🐾 Información de mascota",
                    fontSize = 28.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                if (mascota == null) {

                    Text("Cargando mascota...")

                } else {

                    Card(
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(20.dp)
                        ) {

                            Text(
                                text =
                                    "🐾 ${mascota!!.nombre}",
                                fontSize = 25.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(10.dp)
                            )

                            Text(
                                "Especie: ${mascota!!.especie}"
                            )

                            Text(
                                "Raza: ${mascota!!.raza}"
                            )

                            Text(
                                "Sexo: ${mascota!!.sexo}"
                            )

                            Text(
                                "Fecha de nacimiento: ${mascota!!.fechaNacimiento}"
                            )
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(15.dp)
                    )

                    Button(
                        onClick = irHistorial,
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text("🩺 Historial médico")
                    }

                    Button(
                        onClick = irVacunas,
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text("💉 Vacunas")
                    }

                    Button(
                        onClick = irMedicamentos,
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text("💊 Medicamentos")
                    }

                    OutlinedButton(
                        onClick = editarMascota,
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text("✏️ Editar mascota")
                    }

                    OutlinedButton(
                        onClick = {
                            mostrarConfirmacion =
                                true
                        },
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text("🗑️ Eliminar mascota")
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                OutlinedButton(
                    onClick = volver,
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("Volver")
                }
            }
        }
    }


    @androidx.compose.runtime.Composable
    fun PantallaHistorialMedico(

        idMascota: Int,

        registrarHistorial: () -> Unit,

        editarHistorial: (Int) -> Unit,

        volver: () -> Unit

    ) {

        var historial by remember {
            mutableStateOf(
                emptyList<HistorialMedico>()
            )
        }

        var idEliminar by remember {
            mutableStateOf(0)
        }

        LaunchedEffect(idMascota) {

            historial =
                withContext(
                    Dispatchers.IO
                ) {

                    HistorialMedicoDAO.obtenerHistorial(
                        idMascota
                    )
                }
        }

        if (idEliminar != 0) {

            AlertDialog(

                onDismissRequest = {
                    idEliminar = 0
                },

                title = {
                    Text("Eliminar consulta")
                },

                text = {
                    Text(
                        "¿Desea eliminar esta consulta médica?"
                    )
                },

                confirmButton = {

                    TextButton(
                        onClick = {

                            val id =
                                idEliminar

                            idEliminar = 0

                            lifecycleScope.launch(
                                Dispatchers.IO
                            ) {

                                HistorialMedicoDAO
                                    .eliminarHistorial(id)

                                val nuevaLista =
                                    HistorialMedicoDAO
                                        .obtenerHistorial(
                                            idMascota
                                        )

                                withContext(
                                    Dispatchers.Main
                                ) {

                                    historial =
                                        nuevaLista
                                }
                            }
                        }
                    ) {

                        Text("Eliminar")
                    }
                },

                dismissButton = {

                    TextButton(
                        onClick = {
                            idEliminar = 0
                        }
                    ) {

                        Text("Cancelar")
                    }
                }
            )
        }

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
        ) {

            Text(
                text = "🩺 Historial médico",
                fontSize = 28.sp
            )

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            Button(
                onClick = registrarHistorial,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text("➕ Registrar consulta")
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            if (historial.isEmpty()) {

                Text(
                    "No existen consultas registradas."
                )

            } else {

                LazyColumn {

                    items(historial) { item ->

                        Card(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 6.dp
                                    )
                        ) {

                            Column(
                                modifier =
                                    Modifier.padding(18.dp)
                            ) {

                                Text(
                                    text =
                                        "🩺 Consulta",
                                    fontSize = 21.sp
                                )

                                Text(
                                    "Fecha: ${item.fecha}"
                                )

                                Text(
                                    "Motivo: ${item.motivo}"
                                )

                                Text(
                                    "Diagnóstico: ${item.diagnostico}"
                                )

                                Text(
                                    "Tratamiento: ${item.tratamiento}"
                                )

                                Text(
                                    "Veterinario: ${item.nombreVeterinario}"
                                )

                                if (
                                    item.observaciones.isNotBlank()
                                ) {

                                    Text(
                                        "Observaciones: ${item.observaciones}"
                                    )
                                }

                                Spacer(
                                    modifier =
                                        Modifier.height(10.dp)
                                )

                                Row {

                                    Button(
                                        onClick = {
                                            editarHistorial(
                                                item.idHistorial
                                            )
                                        }
                                    ) {

                                        Text("Editar")
                                    }

                                    Spacer(
                                        modifier =
                                            Modifier.width(8.dp)
                                    )

                                    OutlinedButton(
                                        onClick = {
                                            idEliminar =
                                                item.idHistorial
                                        }
                                    ) {

                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = volver,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text("Volver")
            }
        }
    }


    @androidx.compose.runtime.Composable
    fun PantallaRegistrarHistorial(

        idMascota: Int,

        historialRegistrado: () -> Unit,

        volver: () -> Unit

    ) {

        var fecha by remember {
            mutableStateOf("")
        }

        var motivo by remember {
            mutableStateOf("")
        }

        var diagnostico by remember {
            mutableStateOf("")
        }

        var tratamiento by remember {
            mutableStateOf("")
        }

        var observaciones by remember {
            mutableStateOf("")
        }

        var veterinarios by remember {
            mutableStateOf(
                emptyList<Veterinario>()
            )
        }

        var veterinarioSeleccionado by remember {
            mutableStateOf<Int?>(null)
        }

        var mensaje by remember {
            mutableStateOf("")
        }

        LaunchedEffect(Unit) {

            veterinarios =
                withContext(
                    Dispatchers.IO
                ) {

                    VeterinarioDAO
                        .obtenerVeterinarios()
                }
        }

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
        ) {

            item {

                Text(
                    "➕ Registrar consulta",
                    fontSize = 28.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Campo(
                    fecha,
                    { fecha = it },
                    "Fecha (YYYY-MM-DD)"
                )

                Campo(
                    motivo,
                    { motivo = it },
                    "Motivo"
                )

                Campo(
                    diagnostico,
                    { diagnostico = it },
                    "Diagnóstico"
                )

                Campo(
                    tratamiento,
                    { tratamiento = it },
                    "Tratamiento"
                )

                Campo(
                    observaciones,
                    { observaciones = it },
                    "Observaciones"
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Text(
                    "Veterinario"
                )

                veterinarios.forEach { veterinario ->

                    OutlinedButton(

                        onClick = {

                            veterinarioSeleccionado =
                                veterinario.idVeterinario
                        },

                        modifier =
                            Modifier.fillMaxWidth()

                    ) {

                        Text(
                            if (
                                veterinarioSeleccionado ==
                                veterinario.idVeterinario
                            ) {

                                "✓ ${veterinario.nombre}"

                            } else {

                                veterinario.nombre
                            }
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                Button(
                    onClick = {

                        if (
                            fecha.isBlank() ||
                            motivo.isBlank() ||
                            diagnostico.isBlank() ||
                            tratamiento.isBlank()
                        ) {

                            mensaje =
                                "Complete los campos obligatorios"

                        } else {

                            lifecycleScope.launch(
                                Dispatchers.IO
                            ) {

                                val resultado =
                                    HistorialMedicoDAO
                                        .registrarHistorial(
                                            idMascota,
                                            fecha,
                                            motivo,
                                            diagnostico,
                                            tratamiento,
                                            observaciones,
                                            veterinarioSeleccionado
                                        )

                                withContext(
                                    Dispatchers.Main
                                ) {

                                    if (resultado) {

                                        historialRegistrado()

                                    } else {

                                        mensaje =
                                            "No se pudo registrar"
                                    }
                                }
                            }
                        }
                    },
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("Guardar consulta")
                }

                OutlinedButton(
                    onClick = volver,
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("Cancelar")
                }

                Text(mensaje)
            }
        }
    }


    @androidx.compose.runtime.Composable
    fun PantallaEditarHistorial(

        idHistorial: Int,

        historialActualizado: () -> Unit,

        volver: () -> Unit

    ) {

        var cargando by remember {
            mutableStateOf(true)
        }

        var historial by remember {
            mutableStateOf<HistorialMedico?>(null)
        }

        var fecha by remember {
            mutableStateOf("")
        }

        var motivo by remember {
            mutableStateOf("")
        }

        var diagnostico by remember {
            mutableStateOf("")
        }

        var tratamiento by remember {
            mutableStateOf("")
        }

        var observaciones by remember {
            mutableStateOf("")
        }

        var veterinarios by remember {
            mutableStateOf(
                emptyList<Veterinario>()
            )
        }

        var veterinarioSeleccionado by remember {
            mutableStateOf<Int?>(null)
        }

        var mensaje by remember {
            mutableStateOf("")
        }

        LaunchedEffect(idHistorial) {

            val resultado =
                withContext(
                    Dispatchers.IO
                ) {

                    val lista =
                        HistorialMedicoDAO
                            .obtenerHistorial(
                                idHistorial
                            )

                    null
                }

            cargando = false
        }

        LaunchedEffect(Unit) {

            veterinarios =
                withContext(
                    Dispatchers.IO
                ) {

                    VeterinarioDAO
                        .obtenerVeterinarios()
                }
        }

        if (cargando) {

            Box(
                modifier =
                    Modifier.fillMaxSize(),
                contentAlignment =
                    Alignment.Center
            ) {

                Text("Cargando...")
            }

            return
        }

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
        ) {

            item {

                Text(
                    "✏️ Editar consulta",
                    fontSize = 28.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Campo(
                    fecha,
                    { fecha = it },
                    "Fecha"
                )

                Campo(
                    motivo,
                    { motivo = it },
                    "Motivo"
                )

                Campo(
                    diagnostico,
                    { diagnostico = it },
                    "Diagnóstico"
                )

                Campo(
                    tratamiento,
                    { tratamiento = it },
                    "Tratamiento"
                )

                Campo(
                    observaciones,
                    { observaciones = it },
                    "Observaciones"
                )

                Text(
                    "Veterinario"
                )

                veterinarios.forEach { veterinario ->

                    OutlinedButton(

                        onClick = {

                            veterinarioSeleccionado =
                                veterinario.idVeterinario
                        },

                        modifier =
                            Modifier.fillMaxWidth()

                    ) {

                        Text(
                            if (
                                veterinarioSeleccionado ==
                                veterinario.idVeterinario
                            ) {

                                "✓ ${veterinario.nombre}"

                            } else {

                                veterinario.nombre
                            }
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                Button(
                    onClick = {

                        lifecycleScope.launch(
                            Dispatchers.IO
                        ) {

                            val resultado =
                                HistorialMedicoDAO
                                    .actualizarHistorial(
                                        idHistorial,
                                        fecha,
                                        motivo,
                                        diagnostico,
                                        tratamiento,
                                        observaciones,
                                        veterinarioSeleccionado
                                    )

                            withContext(
                                Dispatchers.Main
                            ) {

                                if (resultado) {

                                    historialActualizado()

                                } else {

                                    mensaje =
                                        "No se pudo actualizar"
                                }
                            }
                        }
                    },
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("Guardar cambios")
                }

                OutlinedButton(
                    onClick = volver,
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("Cancelar")
                }

                Text(mensaje)
            }
        }
    }


    @androidx.compose.runtime.Composable
    fun PantallaVeterinarios(
        volver: () -> Unit
    ) {

        var veterinarios by remember {
            mutableStateOf(
                emptyList<Veterinario>()
            )
        }

        LaunchedEffect(Unit) {

            veterinarios =
                withContext(
                    Dispatchers.IO
                ) {

                    VeterinarioDAO
                        .obtenerVeterinarios()
                }
        }

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
        ) {

            Text(
                "🩺 Veterinarios",
                fontSize = 28.sp
            )

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            LazyColumn {

                items(veterinarios) { veterinario ->

                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = 6.dp
                                )
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(18.dp)
                        ) {

                            Text(
                                "🩺 ${veterinario.nombre}",
                                fontSize = 21.sp
                            )

                            Text(
                                "Especialidad: ${veterinario.especialidad}"
                            )

                            Text(
                                "Teléfono: ${veterinario.telefono}"
                            )

                            Text(
                                "Correo: ${veterinario.correo}"
                            )
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = volver,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text("Volver")
            }
        }
    }


    @androidx.compose.runtime.Composable
    fun PantallaVacunas(

        idMascota: Int,

        volver: () -> Unit

    ) {

        var vacunas by remember {
            mutableStateOf(
                emptyList<Vacuna>()
            )
        }

        var nombreVacuna by remember {
            mutableStateOf("")
        }

        var fechaAplicacion by remember {
            mutableStateOf("")
        }

        var proximaDosis by remember {
            mutableStateOf("")
        }

        var observaciones by remember {
            mutableStateOf("")
        }

        LaunchedEffect(idMascota) {

            vacunas =
                withContext(
                    Dispatchers.IO
                ) {

                    VacunaDAO.obtenerVacunas(
                        idMascota
                    )
                }
        }

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
        ) {

            Text(
                "💉 Vacunas",
                fontSize = 28.sp
            )

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            LazyColumn {

                item {

                    Campo(
                        nombreVacuna,
                        { nombreVacuna = it },
                        "Nombre de vacuna"
                    )

                    Campo(
                        fechaAplicacion,
                        { fechaAplicacion = it },
                        "Fecha de aplicación"
                    )

                    Campo(
                        proximaDosis,
                        { proximaDosis = it },
                        "Próxima dosis"
                    )

                    Campo(
                        observaciones,
                        { observaciones = it },
                        "Observaciones"
                    )

                    Button(
                        onClick = {

                            if (
                                nombreVacuna.isNotBlank() &&
                                fechaAplicacion.isNotBlank()
                            ) {

                                lifecycleScope.launch(
                                    Dispatchers.IO
                                ) {

                                    VacunaDAO.registrarVacuna(
                                        idMascota,
                                        nombreVacuna,
                                        fechaAplicacion,
                                        proximaDosis,
                                        observaciones
                                    )

                                    val nuevaLista =
                                        VacunaDAO
                                            .obtenerVacunas(
                                                idMascota
                                            )

                                    withContext(
                                        Dispatchers.Main
                                    ) {

                                        vacunas =
                                            nuevaLista

                                        nombreVacuna =
                                            ""

                                        fechaAplicacion =
                                            ""

                                        proximaDosis =
                                            ""

                                        observaciones =
                                            ""
                                    }
                                }
                            }
                        },
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text("➕ Registrar vacuna")
                    }

                    Spacer(
                        modifier =
                            Modifier.height(15.dp)
                    )
                }

                items(vacunas) { vacuna ->

                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = 6.dp
                                )
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(15.dp)
                        ) {

                            Text(
                                "💉 ${vacuna.nombreVacuna}",
                                fontSize = 20.sp
                            )

                            Text(
                                "Aplicación: ${vacuna.fechaAplicacion}"
                            )

                            Text(
                                "Próxima dosis: ${vacuna.proximaDosis}"
                            )

                            Text(
                                "Observaciones: ${vacuna.observaciones}"
                            )

                            TextButton(
                                onClick = {

                                    lifecycleScope.launch(
                                        Dispatchers.IO
                                    ) {

                                        VacunaDAO
                                            .eliminarVacuna(
                                                vacuna.idVacuna
                                            )

                                        val nuevaLista =
                                            VacunaDAO
                                                .obtenerVacunas(
                                                    idMascota
                                                )

                                        withContext(
                                            Dispatchers.Main
                                        ) {

                                            vacunas =
                                                nuevaLista
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

                    OutlinedButton(
                        onClick = volver,
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text("Volver")
                    }
                }
            }
        }
    }


    @androidx.compose.runtime.Composable
    fun PantallaMedicamentos(

        idMascota: Int,

        volver: () -> Unit

    ) {

        var medicamentos by remember {
            mutableStateOf(
                emptyList<Medicamento>()
            )
        }

        var nombre by remember {
            mutableStateOf("")
        }

        var dosis by remember {
            mutableStateOf("")
        }

        var frecuencia by remember {
            mutableStateOf("")
        }

        var fechaInicio by remember {
            mutableStateOf("")
        }

        var fechaFin by remember {
            mutableStateOf("")
        }

        var indicaciones by remember {
            mutableStateOf("")
        }

        LaunchedEffect(idMascota) {

            medicamentos =
                withContext(
                    Dispatchers.IO
                ) {

                    MedicamentoDAO
                        .obtenerMedicamentos(
                            idMascota
                        )
                }
        }

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
        ) {

            Text(
                "💊 Medicamentos",
                fontSize = 28.sp
            )

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            LazyColumn {

                item {

                    Campo(
                        nombre,
                        { nombre = it },
                        "Medicamento"
                    )

                    Campo(
                        dosis,
                        { dosis = it },
                        "Dosis"
                    )

                    Campo(
                        frecuencia,
                        { frecuencia = it },
                        "Frecuencia"
                    )

                    Campo(
                        fechaInicio,
                        { fechaInicio = it },
                        "Fecha de inicio"
                    )

                    Campo(
                        fechaFin,
                        { fechaFin = it },
                        "Fecha de finalización"
                    )

                    Campo(
                        indicaciones,
                        { indicaciones = it },
                        "Indicaciones"
                    )

                    Button(
                        onClick = {

                            if (
                                nombre.isNotBlank()
                            ) {

                                lifecycleScope.launch(
                                    Dispatchers.IO
                                ) {

                                    MedicamentoDAO
                                        .registrarMedicamento(
                                            idMascota,
                                            nombre,
                                            dosis,
                                            frecuencia,
                                            fechaInicio,
                                            fechaFin,
                                            indicaciones
                                        )

                                    val nuevaLista =
                                        MedicamentoDAO
                                            .obtenerMedicamentos(
                                                idMascota
                                            )

                                    withContext(
                                        Dispatchers.Main
                                    ) {

                                        medicamentos =
                                            nuevaLista

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
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text(
                            "➕ Registrar medicamento"
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(15.dp)
                    )
                }

                items(medicamentos) { medicamento ->

                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = 6.dp
                                )
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(15.dp)
                        ) {

                            Text(
                                "💊 ${medicamento.nombreMedicamento}",
                                fontSize = 20.sp
                            )

                            Text(
                                "Dosis: ${medicamento.dosis}"
                            )

                            Text(
                                "Frecuencia: ${medicamento.frecuencia}"
                            )

                            Text(
                                "Inicio: ${medicamento.fechaInicio}"
                            )

                            Text(
                                "Fin: ${medicamento.fechaFin}"
                            )

                            Text(
                                "Indicaciones: ${medicamento.indicaciones}"
                            )

                            TextButton(
                                onClick = {

                                    lifecycleScope.launch(
                                        Dispatchers.IO
                                    ) {

                                        MedicamentoDAO
                                            .eliminarMedicamento(
                                                medicamento.idMedicamento
                                            )

                                        val nuevaLista =
                                            MedicamentoDAO
                                                .obtenerMedicamentos(
                                                    idMascota
                                                )

                                        withContext(
                                            Dispatchers.Main
                                        ) {

                                            medicamentos =
                                                nuevaLista
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

                    OutlinedButton(
                        onClick = volver,
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text("Volver")
                    }
                }
            }
        }
    }
}