package com.example.vetpets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.vetpets.ui.theme.VetPetsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            VetPetsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppVetPets()
                }
            }
        }
    }
}

@Composable
fun AppVetPets() {

    var pantallaActual by remember {
        mutableStateOf("inicio")
    }

    when (pantallaActual) {

        "inicio" -> {
            PantallaInicio(
                irALogin = {
                    pantallaActual = "login"
                },
                irARegistro = {
                    pantallaActual = "registro"
                }
            )
        }

        "login" -> {
            PantallaLogin(
                volverInicio = {
                    pantallaActual = "inicio"
                }
            )
        }

        "registro" -> {
            PantallaRegistro(
                volverInicio = {
                    pantallaActual = "inicio"
                }
            )
        }
    }
}


@Composable
fun PantallaInicio(
    irALogin: () -> Unit,
    irARegistro: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F8F6))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "🐾",
                fontSize = 70.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "VetPets",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D6B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Sistema digital de historial médico para mascotas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color(0xFF444444)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Administra la información de tus mascotas,\nconsultas médicas y tratamientos.",
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    irALogin()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D6B)
                )
            ) {
                Text(
                    text = "Iniciar sesión",
                    fontSize = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedButton(
                onClick = {
                    irARegistro()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Crear una cuenta",
                    fontSize = 17.sp,
                    color = Color(0xFF2E7D6B)
                )
            }
        }
    }
}


@Composable
fun PantallaLogin(
    volverInicio: () -> Unit
) {

    var usuario by remember {
        mutableStateOf("")
    }

    var contrasena by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F8F6))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "🐾",
                fontSize = 60.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Iniciar sesión",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D6B)
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = usuario,
                onValueChange = {
                    usuario = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Usuario")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Contraseña")
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    // Aquí posteriormente conectaremos el Login con la API
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D6B)
                )
            ) {
                Text(
                    text = "Iniciar sesión",
                    fontSize = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedButton(
                onClick = {
                    volverInicio()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Volver",
                    fontSize = 17.sp
                )
            }
        }
    }
}


@Composable
fun PantallaRegistro(
    volverInicio: () -> Unit
) {

    var nombre by remember {
        mutableStateOf("")
    }

    var correo by remember {
        mutableStateOf("")
    }

    var usuario by remember {
        mutableStateOf("")
    }

    var contrasena by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F8F6))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Crear cuenta",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D6B)
            )

            Spacer(modifier = Modifier.height(25.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Nombre completo")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Correo electrónico")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = usuario,
                onValueChange = {
                    usuario = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Usuario")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Contraseña")
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    // Aquí posteriormente registraremos el usuario en la base de datos
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D6B)
                )
            ) {
                Text(
                    text = "Crear cuenta",
                    fontSize = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    volverInicio()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Volver",
                    fontSize = 17.sp
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PantallaInicioPreview() {
    VetPetsTheme {
        PantallaInicio(
            irALogin = {},
            irARegistro = {}
        )
    }
}