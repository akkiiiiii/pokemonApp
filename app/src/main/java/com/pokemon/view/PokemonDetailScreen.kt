package com.pokemon.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pokemon.model.Pokemon
import com.pokemon.viewModel.PokemonDetailViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    onBackClick: () -> Unit,
    viewModel: PokemonDetailViewModel = koinViewModel()
) {
    val pokemon by viewModel.pokemon.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(pokemonId) {
        viewModel.loadPokemon(pokemonId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),  // ADDED: Fill entire screen
        topBar = {
            TopAppBar(
                title = {
                    Text(pokemon?.name?.replaceFirstChar { it.uppercase() } ?: "Pokemon Details")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)  // This properly handles top bar
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error!!,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadPokemon(pokemonId) }) {
                            Text("Retry")
                        }
                    }
                }
                pokemon != null -> {
                    PokemonDetailContent(pokemon = pokemon!!)
                }
            }
        }
    }
}

@Composable
fun PokemonDetailContent(pokemon: Pokemon) {
    LazyColumn(  // CHANGED: Using LazyColumn instead of Column with scroll
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),  // ADDED: Content padding for better layout
        verticalArrangement = Arrangement.spacedBy(16.dp),  // ADDED: Consistent spacing
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Pokemon Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.sprites.other?.`official-artwork`?.front_default  // FIXED: Removed backslashes
                        ?: pokemon.sprites.front_default)  // FIXED: Removed backslashes
                    .crossfade(true)
                    .build(),
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Gray.copy(alpha = 0.1f)),
                contentScale = ContentScale.Crop
            )
        }

        item {
            // Pokemon Name and ID
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "#${pokemon.id.toString().padStart(3, '0')}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            // Basic Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoCard(
                    title = "Height",
                    value = "${pokemon.height / 10.0} m"
                )
                InfoCard(
                    title = "Weight",
                    value = "${pokemon.weight / 10.0} kg"
                )
            }
        }

        item {
            // Types Section
            Column {
                Text(
                    text = "Types",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    pokemon.types.forEach { type ->
                        TypeChip(typeName = type.type.name)
                    }
                }
            }
        }

        item {
            // Stats Section
            Column {
                Text(
                    text = "Base Stats",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    pokemon.stats.forEach { stat ->
                        StatBar(
                            statName = stat.stat.name.replace("-", " ").replaceFirstChar { it.uppercase() },
                            statValue = stat.base_stat  // FIXED: Removed backslashes
                        )
                    }
                }
            }
        }

        item {
            // Abilities Section
            Column {
                Text(
                    text = "Abilities",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    pokemon.abilities.forEach { ability ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = ability.ability.name.replace("-", " ").replaceFirstChar { it.uppercase() },
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }

        // ADDED: Extra spacing at the bottom
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun InfoCard(title: String, value: String) {
    Card(
        modifier = Modifier.width(120.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TypeChip(typeName: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = getTypeColor(typeName)
        )
    ) {
        Text(
            text = typeName.replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StatBar(statName: String, statValue: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = statName)
            Text(text = statValue.toString(), fontWeight = FontWeight.Bold)
        }

        LinearProgressIndicator(
            progress = { (statValue / 255f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

fun getTypeColor(type: String): Color {
    return when (type.lowercase()) {
        "fire" -> Color(0xFFFF5722)
        "water" -> Color(0xFF2196F3)
        "grass" -> Color(0xFF4CAF50)
        "electric" -> Color(0xFFFFEB3B)
        "psychic" -> Color(0xFFE91E63)
        "ice" -> Color(0xFF00BCD4)
        "dragon" -> Color(0xFF673AB7)
        "dark" -> Color(0xFF424242)
        "fairy" -> Color(0xFFFF69B4)
        "normal" -> Color(0xFF9E9E9E)
        "fighting" -> Color(0xFFD32F2F)
        "poison" -> Color(0xFF9C27B0)
        "ground" -> Color(0xFFFF9800)
        "flying" -> Color(0xFF03DAC5)
        "bug" -> Color(0xFF8BC34A)
        "rock" -> Color(0xFF795548)
        "ghost" -> Color(0xFF7C4DFF)
        "steel" -> Color(0xFF607D8B)
        else -> Color(0xFF757575)
    }
}
