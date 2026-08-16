package com.example.tp4

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val CasinoGreen = Color(0xFF0F4D2F)
private val CasinoGreenDark = Color(0xFF09341F)
private val Gold = Color(0xFFFFD54F)
private val GoldDark = Color(0xFFC9A227)
private val PanelDark = Color(0xFF123C29)
private val InfoWhite = Color(0xFFF8F6F0)
private val LoseRed = Color(0xFFD32F2F)
private val WinGreen = Color(0xFF4CAF50)
private val ButtonBlue = Color(0xFF0D83C7)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color(0xFF0F4D2F), Color(0xFF0A3A24), Color(0xFF041A10))
                            )
                        )
                ) {
                    VideoPokerScreen()
                }
            }
        }
    }
}

@Composable
fun VideoPokerScreen() {
    val poker = remember { Poker() }

    var gameState by remember { mutableStateOf(GameState.CONFIG) }
    var inputSolde by remember { mutableStateOf("") }
    var cagnotte by remember { mutableIntStateOf(0) }
    var mise by remember { mutableIntStateOf(1) }

    var cards by remember { mutableStateOf<List<Card>>(emptyList()) }
    var selectedCards by remember { mutableStateOf(setOf<Int>()) }
    var gainMessage by remember { mutableStateOf("Choisissez un solde de départ") }

    var currentDeck by remember { mutableStateOf<Deck?>(null) }

    var history by remember { mutableStateOf<List<GameHistoryEntry>>(emptyList()) }
    var showHistory by remember { mutableStateOf(false) }
    var roundCounter by remember { mutableIntStateOf(0) }

    var showJackpot by remember { mutableStateOf(false) }
    var lastResultLabel by remember { mutableStateOf("") }

    fun resetPartie(nouveauSolde: Int) {
        cagnotte = nouveauSolde
        mise = 1
        cards = emptyList()
        selectedCards = emptySet()
        currentDeck = null
        gainMessage = "Appuie sur Jouer pour commencer"
        history = emptyList()
        roundCounter = 0
        showHistory = false
        showJackpot = false
        lastResultLabel = ""
        gameState = GameState.MISE
    }

    val infiniteTransition = rememberInfiniteTransition(label = "jackpot")
    val jackpotColor by infiniteTransition.animateColor(
        initialValue = Gold,
        targetValue = Color.White,
        animationSpec = infiniteRepeatable(
            animation = tween(550),
            repeatMode = RepeatMode.Reverse
        ),
        label = "jackpotColor"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Wooden table rail wrapping the felt play area, mirrors web's .rail
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(14.dp, RoundedCornerShape(32.dp))
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF8A5A2F), Color(0xFF5C3A1C), Color(0xFF3D2510))
                    )
                )
                .padding(14.dp)
        ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CasinoGreen,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(2.dp, GoldDark)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "♠ ROYAL VIDEO POKER ♠",
                    color = Gold,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Style casino • animation • session tracking",
                    color = InfoWhite,
                    fontSize = 13.sp
                )

                AnimatedVisibility(
                    visible = showJackpot,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Text(
                            text = "🔥 JACKPOT 🔥",
                            color = jackpotColor,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Quinte flush royale",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = PanelDark,
            shape = RoundedCornerShape(22.dp),
            border = BorderStroke(2.dp, GoldDark)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoChip(label = "Solde", value = "${cagnotte}€")
                    InfoChip(label = "Mise", value = "${mise}€")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = gainMessage,
                    color = when {
                        "perdu" in gainMessage.lowercase() -> Color(0xFFFFB3B3)
                        "gagné" in gainMessage.lowercase() -> Color(0xFFB9F6CA)
                        else -> Gold
                    },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (lastResultLabel.isNotEmpty() && gameState == GameState.GAIN) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Dernière combinaison : $lastResultLabel",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                when (gameState) {
                    GameState.CONFIG -> {
                        Text(
                            text = "Choisissez votre solde de départ",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = inputSolde,
                            onValueChange = { inputSolde = it },
                            label = { Text("Entrez un montant (€)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val montant = inputSolde.toIntOrNull()
                                if (montant != null && montant > 0) {
                                    resetPartie(montant)
                                    inputSolde = ""
                                } else {
                                    gainMessage = "Montant invalide"
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldDark,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Valider", fontWeight = FontWeight.Bold)
                        }
                    }

                    else -> {
                        // Felt betting strip: darker inset surface where the hand is dealt, mirrors web's .felt-strip
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(Color(0xFF0D3D26), Color(0xFF082818))
                                    )
                                )
                                .border(1.dp, GoldDark.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                                .padding(vertical = 14.dp, horizontal = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                when (gameState) {
                                    GameState.MISE -> {
                                        repeat(5) { i -> CardBack(dealIndex = i) }
                                    }

                                    GameState.CHOIX -> {
                                        cards.forEachIndexed { index, card ->
                                            SelectableCardView(
                                                card = card,
                                                isSelected = index in selectedCards,
                                                canSelect = true,
                                                dealIndex = index,
                                                onClick = {
                                                    selectedCards =
                                                        if (index in selectedCards) {
                                                            selectedCards - index
                                                        } else {
                                                            selectedCards + index
                                                        }
                                                }
                                            )
                                        }
                                    }

                                    GameState.GAIN -> {
                                        cards.forEachIndexed { index, card ->
                                            SelectableCardView(
                                                card = card,
                                                isSelected = false,
                                                canSelect = false,
                                                dealIndex = index,
                                                onClick = {}
                                            )
                                        }
                                    }

                                    GameState.CONFIG -> {}
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(
                                modifier = Modifier.padding(end = 12.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Button(
                                    onClick = { showHistory = !showHistory },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF424242),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(18.dp)
                                ) {
                                    Text(if (showHistory) "Masquer" else "Historique")
                                }

                                if (showHistory) {
                                    Spacer(modifier = Modifier.height(10.dp))

                                    val partiesJouees = history.size
                                    val victoires = history.count { it.isWin }
                                    val defaites = partiesJouees - victoires
                                    val gainTotal = history.sumOf { it.gainNet }
                                    val meilleurGain = history.maxOfOrNull { it.gainNet } ?: 0

                                    HistoryLine("Parties", partiesJouees.toString())
                                    HistoryLine("Victoires", victoires.toString())
                                    HistoryLine("Défaites", defaites.toString())
                                    HistoryLine(
                                        "Gain net",
                                        "${if (gainTotal >= 0) "+" else ""}${gainTotal}€",
                                        if (gainTotal >= 0) WinGreen else LoseRed
                                    )
                                    HistoryLine(
                                        "Best",
                                        "${if (meilleurGain >= 0) "+" else ""}${meilleurGain}€",
                                        Gold
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    if (history.isEmpty()) {
                                        Text(
                                            text = "Aucune manche",
                                            color = Color.White,
                                            fontSize = 12.sp
                                        )
                                    } else {
                                        history.takeLast(5).reversed().forEach { entry ->
                                            Text(
                                                text = "M${entry.roundNumber} : ${entry.combinaison} (${if (entry.gainNet >= 0) "+" else ""}${entry.gainNet}€)",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(
                                    onClick = {
                                        if (gameState == GameState.MISE && mise < cagnotte) {
                                            mise++
                                        }
                                    },
                                    enabled = gameState == GameState.MISE && cagnotte > 0,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ButtonBlue,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(18.dp)
                                ) {
                                    Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        if (gameState == GameState.MISE && mise > 1) {
                                            mise--
                                        }
                                    },
                                    enabled = gameState == GameState.MISE && cagnotte > 0,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ButtonBlue,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(18.dp)
                                ) {
                                    Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Column(
                                modifier = Modifier.padding(start = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(
                                    onClick = {
                                        when (gameState) {
                                            GameState.MISE -> {
                                                showJackpot = false
                                                lastResultLabel = ""

                                                if (cagnotte <= 0) {
                                                    gameState = GameState.CONFIG
                                                    gainMessage = "Choisissez un nouveau solde"
                                                    return@Button
                                                }

                                                if (mise > cagnotte) {
                                                    mise = cagnotte
                                                }

                                                val deck = Deck()
                                                deck.Shuffle()
                                                currentDeck = deck

                                                val hand = deck.drawCards(5)
                                                if (hand != null) {
                                                    cagnotte -= mise
                                                    cards = hand.cards.toList()
                                                    selectedCards = emptySet()
                                                    gainMessage = "Choisissez les cartes à changer"
                                                    gameState = GameState.CHOIX
                                                }
                                            }

                                            GameState.CHOIX -> {
                                                val deck = currentDeck
                                                val newCards = cards.toMutableList()

                                                if (deck != null) {
                                                    for (i in selectedCards) {
                                                        val newCard = deck.drawCard()
                                                        if (newCard != null) {
                                                            newCards[i] = newCard
                                                        }
                                                    }
                                                }

                                                cards = newCards.toList()

                                                val finalHand = Hand(cards.toMutableList())
                                                val result = poker.evaluateHand(finalHand)
                                                val gainBrut = mise * result.multiplier
                                                val gainNet = gainBrut - mise

                                                cagnotte += gainBrut
                                                roundCounter++

                                                val combinaison = handLabel(result)
                                                lastResultLabel = combinaison

                                                history = history + GameHistoryEntry(
                                                    roundNumber = roundCounter,
                                                    mise = mise,
                                                    combinaison = combinaison,
                                                    gainBrut = gainBrut,
                                                    gainNet = gainNet,
                                                    soldeApres = cagnotte
                                                )

                                                showJackpot = result == Poker.PokerHandValue.ROYAL_FLUSH

                                                gainMessage =
                                                    when {
                                                        result == Poker.PokerHandValue.ROYAL_FLUSH ->
                                                            "Jackpot absolu ! +${gainNet}€ net"
                                                        gainNet > 0 ->
                                                            "Tu as gagné ${gainNet}€ net avec $combinaison"
                                                        gainNet == 0 && gainBrut > 0 ->
                                                            "Tu récupères ta mise avec $combinaison"
                                                        else ->
                                                            "Tu as perdu cette manche"
                                                    }

                                                gameState = GameState.GAIN
                                            }

                                            GameState.GAIN -> {
                                                selectedCards = emptySet()
                                                cards = emptyList()
                                                currentDeck = null

                                                if (cagnotte <= 0) {
                                                    mise = 1
                                                    gainMessage = "Tu n'as plus d'argent. Choisis un nouveau solde."
                                                    gameState = GameState.CONFIG
                                                } else {
                                                    if (mise > cagnotte) {
                                                        mise = cagnotte
                                                    }
                                                    gainMessage = "Appuie sur Jouer pour recommencer"
                                                    gameState = GameState.MISE
                                                }
                                            }

                                            GameState.CONFIG -> {}
                                        }
                                    },
                                    enabled = gameState != GameState.CONFIG,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = LoseRed,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(22.dp)
                                ) {
                                    Text(
                                        text = if (gameState == GameState.GAIN) "Rejouer" else "Jouer",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (cagnotte <= 0 && gameState != GameState.CONFIG) {
                            Text(
                                text = "Vous avez perdu tout votre argent.",
                                color = Color(0xFFFFCDD2),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        }
        }
    }
}

@Composable
fun InfoChip(label: String, value: String) {
    Surface(
        color = CasinoGreen,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, GoldDark)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                color = Gold,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = value,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun HistoryLine(label: String, value: String, valueColor: Color = Color.White) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label : ",
            color = Gold,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CardBack(dealIndex: Int = 0) {
    val dealProgress = remember(dealIndex) { Animatable(0f) }
    LaunchedEffect(dealIndex) {
        delay(dealIndex * 70L)
        dealProgress.animateTo(1f, animationSpec = tween(320))
    }

    Surface(
        modifier = Modifier
            .size(width = 72.dp, height = 104.dp)
            .graphicsLayer {
                alpha = dealProgress.value
                translationY = (1f - dealProgress.value) * (-22).dp.toPx()
                val s = 0.85f + 0.15f * dealProgress.value
                scaleX = s
                scaleY = s
            },
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF061C3A), RoundedCornerShape(12.dp))
                .border(2.dp, GoldDark, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.dos),
                contentDescription = "Dos de carte",
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Composable
fun CardSprite(row: Int, col: Int) {
    val bitmap = ImageBitmap.imageResource(R.drawable.cartes)

    val cardWidth = bitmap.width / 13
    val cardHeight = bitmap.height / 4

    val left = col * cardWidth
    val top = row * cardHeight

    val croppedImage = Bitmap.createBitmap(
        bitmap.asAndroidBitmap(),
        left,
        top,
        cardWidth,
        cardHeight
    ).asImageBitmap()

    Image(
        bitmap = croppedImage,
        contentDescription = "Carte",
        modifier = Modifier.size(width = 62.dp, height = 92.dp),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun CardView(card: Card) {
    val row = when (card.color) {
        Card.Color.COEUR -> 0
        Card.Color.CARREAU -> 1
        Card.Color.PIQUE -> 2
        Card.Color.TREFLE -> 3
    }

    val col = when (card.value) {
        2 -> 0
        3 -> 1
        4 -> 2
        5 -> 3
        6 -> 4
        7 -> 5
        8 -> 6
        9 -> 7
        10 -> 8
        11 -> 9
        12 -> 10
        13 -> 11
        1 -> 12
        else -> 0
    }

    Surface(
        modifier = Modifier.size(width = 72.dp, height = 104.dp),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CardSprite(row, col)
        }
    }
}

@Composable
fun SelectableCardView(
    card: Card,
    isSelected: Boolean,
    canSelect: Boolean,
    dealIndex: Int = 0,
    onClick: () -> Unit
) {
    val offsetY by animateDpAsState(
        targetValue = if (isSelected) (-12).dp else 0.dp,
        animationSpec = tween(220),
        label = "cardOffset"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = tween(220),
        label = "cardScale"
    )

    val borderColor by animateColorAsStateCompat(
        targetValue = if (isSelected) Gold else Color.Transparent,
        label = "cardBorderColor"
    )

    val dealProgress = remember(dealIndex) { Animatable(0f) }
    LaunchedEffect(dealIndex) {
        delay(dealIndex * 70L)
        dealProgress.animateTo(1f, animationSpec = tween(320))
    }

    Box(
        modifier = Modifier
            .padding(4.dp)
            .offset(y = offsetY)
            .scale(scale)
            .graphicsLayer {
                alpha = dealProgress.value
                translationY = (1f - dealProgress.value) * (-22).dp.toPx()
                val s = 0.85f + 0.15f * dealProgress.value
                scaleX = s
                scaleY = s
            }
            .then(if (canSelect) Modifier.clickable { onClick() } else Modifier)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(
                width = if (isSelected) 3.dp else 0.dp,
                color = borderColor
            ),
            color = Color.Transparent
        ) {
            Box(modifier = Modifier.padding(2.dp)) {
                CardView(card)
            }
        }
    }
}

@Composable
fun animateColorAsStateCompat(
    targetValue: Color,
    label: String
): androidx.compose.runtime.State<Color> {
    return androidx.compose.animation.animateColorAsState(
        targetValue = targetValue,
        animationSpec = tween(220),
        label = label
    )
}