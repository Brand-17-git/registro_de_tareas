package com.example.gestortareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.unit.height
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.tooling.preview.Preview

data class Tarea(
    val id: Int,
    val texto: String,
    val completada: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PantallaPerfil()
            }
        }
    }
}

// ---------- PANTALLA PRINCIPAL ----------
@Composable
fun PantallaPerfil() {
    var tareas by remember {
        mutableStateOf(
            listOf(
                Tarea(1, "Estudiar Jetpack Compose"),
                Tarea(2, "Hacer la investigación #2"),
                Tarea(3, "Practicar con ejemplos", true)
            )
        )
    }

    var textoNuevo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        EncabezadoPerfil()
        Spacer(modifier = Modifier.height(24.dp))

        ContadorTareas(tareas = tareas)

        Spacer(modifier = Modifier.height(16.dp))

        CampoAgregarTarea(
            texto = textoNuevo,
            onTextoChange = { textoNuevo = it },
            onAgregar = {
                if (textoNuevo.isNotBlank()) {
                    val nuevoId = (tareas.maxOfOrNull { it.id } ?: 0) + 1
                    tareas = tareas + Tarea(nuevoId, textoNuevo.trim())
                    textoNuevo = ""
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        ListaTareas(
            tareas = tareas,
            onToggle = { id ->
                tareas = tareas.map {
                    if (it.id == id) it.copy(completada = !it.completada) else it
                }
            },
            onEliminar = { id ->
                tareas = tareas.filterNot { it.id == id }
            }
        )
    }
}

// ---------- ENCABEZADO DE PERFIL ----------
@Composable
fun EncabezadoPerfil() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFF6200EE)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "B",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "Brandon Salvador",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )
            Text(
                text = "brandon.salvador@udb.edu.sv",
                fontSize = 14.sp,
                color = Color(0xFF757575)
            )
        }
    }
}

// ---------- CONTADOR DE TAREAS ----------
@Composable
fun ContadorTareas(tareas: List<Tarea>) {
    val pendientes = tareas.count { !it.completada }
    Text(
        text = "Tareas pendientes: $pendientes de ${tareas.size}",
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF6200EE)
    )
}

@Composable
fun CampoAgregarTarea(
    texto: String,
    onTextoChange: (String) -> Unit,
    onAgregar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = texto,
            onValueChange = onTextoChange,
            modifier = Modifier.weight(1f),
            label = { Text("Nueva tarea") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onAgregar,
            modifier = Modifier.height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar"
            )
        }
    }
}


@Composable
fun ListaTareas(
    tareas: List<Tarea>,
    onToggle: (Int) -> Unit,
    onEliminar: (Int) -> Unit
) {
    if (tareas.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay tareas. ¡Agrega una!",
                color = Color(0xFF9E9E9E),
                fontSize = 14.sp
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tareas, key = { it.id }) { tarea ->
                ItemTarea(
                    tarea = tarea,
                    onToggle = { onToggle(tarea.id) },
                    onEliminar = { onEliminar(tarea.id) }
                )
            }
        }
    }
}

@Composable
fun ItemTarea(
    tarea: Tarea,
    onToggle: () -> Unit,
    onEliminar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = tarea.completada,
            onCheckedChange = { onToggle() }
        )

        Text(
            text = tarea.texto,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            color = if (tarea.completada) Color(0xFF9E9E9E) else Color(0xFF212121),
            textDecoration = if (tarea.completada) TextDecoration.LineThrough else null
        )

        IconButton(onClick = onEliminar) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar",
                tint = Color(0xFFE53935)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPantallaPerfil() {
    MaterialTheme {
        PantallaPerfil()
    }
}
