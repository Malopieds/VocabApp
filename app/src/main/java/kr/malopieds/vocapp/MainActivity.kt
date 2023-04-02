package kr.malopieds.vocapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings.Global
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexstyl.swipeablecard.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kr.malopieds.vocapp.models.Word
import kr.malopieds.vocapp.ui.theme.KoreanVocabTheme
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoreanVocabTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    WordScreen()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    DelicateCoroutinesApi::class, ExperimentalSwipeableCardApi::class
)
@Composable
fun WordScreen(
    viewModel: WordViewModel = hiltViewModel(),
){
    val words by viewModel.words.collectAsState(initial = emptyList())

    var search by remember {
        mutableStateOf(false)
    }

    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Improve")
    val itemsIcon = listOf(Icons.Filled.Home, Icons.Filled.ThumbUp)

    val input = remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }

    if (search) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
    
    Scaffold (
        topBar = {
                Row(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Row(
                        modifier = Modifier.padding(
                            start = 12.dp,
                            end = 12.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        ),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (search) {
                            TextField(
                                value = input.value,
                                onValueChange = { input.value = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        search = false
                                        keyboardController?.hide()
                                    }) {
                                        Icon(
                                            Icons.Filled.Close,
                                            "",
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = MaterialTheme.colorScheme.onPrimary,
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                                ),
                                label = {
                                    Text(
                                        "Enter your search",
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                },
                            )
                        }
                        else {
                            Text(text = "VocApp", color = MaterialTheme.colorScheme.onPrimary)
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                search = true
                                input.value = TextFieldValue("")
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "search",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    }
                }
        },
        floatingActionButton = {FloatingActionButton(onClick = { viewModel.openDialog() }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Word"
            )
        }},
//        content = {
//            Column(Modifier.padding(top = 60.dp)) {
//                LazyColumn{
//                    if (input.value.text.isEmpty() || !search) {
//                        items(words.size) {
//                            WordCard(word = words[it], deleteWord = { /*TODO*/ })
//                        }
//                    }else {
//                        val resultList = ArrayList<Word>()
//                        for (word in words) {
//                            if (
//                                word.word?.lowercase(Locale.getDefault())?.contains(input.value.text.lowercase(Locale.getDefault())) == true ||
//                                word.translation?.lowercase(Locale.KOREAN)?.contains(input.value.text.lowercase(Locale.KOREAN)) == true ||
//                                word.desc?.lowercase(Locale.getDefault())?.contains(input.value.text.lowercase(Locale.getDefault())) == true
//                            ) {
//                                resultList.add(word)
//                            }
//                        }
//                        items(resultList.size) {
//                            WordCard(word = resultList[it], deleteWord = { /*TODO*/ })
//                        }
//                    }
//                }
//            }
//            AddWordAlertDialog(openDialog = viewModel.openDialog, closeDialog = { viewModel.closeDialog() }, addWord = { word -> viewModel.addWord(word)})
//
//        },
        bottomBar = {


            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(itemsIcon[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ){
        when(selectedItem){
            0 -> {
                Column(Modifier.padding(top = 60.dp)) {
                    LazyColumn{
                        if (input.value.text.isEmpty() || !search) {
                            items(words.size) {
                                WordCard(word = words[it], deleteWord = { viewModel.deleteWord(words[it]) })
                            }
                        }else {
                            val resultList = ArrayList<Word>()
                            for (word in words) {
                                if (
                                    word.word?.lowercase(Locale.getDefault())?.contains(input.value.text.lowercase(Locale.getDefault())) == true ||
                                    word.translation?.lowercase(Locale.KOREAN)?.contains(input.value.text.lowercase(Locale.KOREAN)) == true ||
                                    word.desc?.lowercase(Locale.getDefault())?.contains(input.value.text.lowercase(Locale.getDefault())) == true
                                ) {
                                    resultList.add(word)
                                }
                            }
                            items(resultList.size) {
                                WordCard(word = resultList[it], deleteWord = { viewModel.deleteWord(words[it]) })
                            }
                        }
                    }
                }
                AddWordAlertDialog(openDialog = viewModel.openDialog, closeDialog = { viewModel.closeDialog() }, addWord = { word -> viewModel.addWord(word)})
            }
            1 -> {
                val listColors = listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.tertiaryContainer)
                val listColorsContainer = listOf(MaterialTheme.colorScheme.onPrimaryContainer, MaterialTheme.colorScheme.onSecondaryContainer, MaterialTheme.colorScheme.onTertiaryContainer)

                val answer = remember { mutableStateOf(TextFieldValue("")) }

                Column(
                    modifier = Modifier
                        .padding(top = 60.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    val states = words.reversed()
                        .map { it to rememberSwipeableCardState() }

                    var answered by remember { mutableStateOf(false) }

                    var answeredColor by remember { mutableStateOf(Color.Green) }

                    val scope = rememberCoroutineScope()

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp, bottom = 150.dp)
                            .aspectRatio(1f)
                    ) {
                        var clicked by remember { mutableStateOf(false) }

                        for (i in 0 until states.size) {
                            val (word, state) = states[i]
                            if (state.swipedDirection == null) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
//                                        .padding(top = if (i < 3) (20*i).dp else 0.dp)
                                        .swipableCard(
                                            state = state,
                                            onSwiped = { direction ->
                                                println("The card was swiped to $direction")
                                            },
                                            onSwipeCancel = {
                                                println("The swiping was cancelled")
                                            }
                                        )
                                        .clickable {
                                            clicked = !clicked
                                        }
                                        .height(350.dp),
                                    shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(containerColor =  if (answered) answeredColor else listColors[i%3]),
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = if (clicked) word.translation.toString() else word.word.toString(),
                                            style = TextStyle(fontSize = 40.sp)
                                            )
                                        Spacer(modifier = Modifier.height(40.dp))
                                        TextField(
                                            value = answer.value,
                                            onValueChange = {answer.value = it},
                                            maxLines = 1,
                                            keyboardOptions = KeyboardOptions(
                                                imeAction = ImeAction.Done,
                                            ),
                                            keyboardActions = KeyboardActions(
                                                onDone = {
                                                    if (answer.value.text == word.translation.toString()){
                                                        answered = true
                                                        answeredColor = Color.Green
//                                                        scope.launch {
//                                                            state.swipe(Direction.Right)
//                                                        }
                                                        println("good")
                                                    }
                                                    else {
                                                        answered = true
                                                        answeredColor = Color.Red
//                                                        GlobalScope.launch {
//                                                            state.swipe(Direction.Right)
//                                                        }
                                                        println("bad")
                                                    }
//                                                    focusManager.moveFocus(FocusDirection.Right)
                                                }
                                            ),
                                            colors = TextFieldDefaults.textFieldColors(
                                                textColor = listColorsContainer[i%3],
                                                containerColor = if (answered) answeredColor else listColors[i%3],
                                                cursorColor = listColorsContainer[i%3],
                                            ),
                                            label = {
                                                Text(
                                                    "Enter your answer",
                                                    color = listColorsContainer[i%3]
                                                )
                                            },
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
}
@Composable
fun AddWordAlertDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    addWord: (word: Word) -> Unit
) {
    if (openDialog) {
        var word by remember { mutableStateOf("") }
        var translation by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        val focusRequester = FocusRequester()

        AlertDialog(
            onDismissRequest = closeDialog,
            title = {
                Text(
                    text = "Add a word"
                )
            },
            text = {
                Column {
                    TextField(
                        value = word,
                        onValueChange = { word = it },
                        placeholder = {
                            Text(
                                text = "Word"
                            )
                        },
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                    LaunchedEffect(Unit) {
                        coroutineContext.job.invokeOnCompletion {
                            focusRequester.requestFocus()
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
                    TextField(
                        value = translation,
                        onValueChange = { translation = it },
                        placeholder = {
                            Text(
                                text = "Translation"
                            )
                        }
                    )
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
                    TextField(
                        value = desc,
                        onValueChange = { desc = it },
                        placeholder = {
                            Text(
                                text = "Description"
                            )
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (word != "" && translation != "") {
                            closeDialog()
                            val book = Word(0, word, translation, desc)
                            addWord(book)
                        }
                    }
                ) {
                    Text(
                        text = "Add"
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = closeDialog
                ) {
                    Text(
                        text = "Dismiss"
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordCard(
    word: Word,
    deleteWord: () -> Unit,
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 4.dp
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { deleteWord.invoke() }
                )
            }
            .fillMaxWidth(),
//        elevation = 3.dp,
//        onClick = {
//            deleteWord.invoke()
//        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(
                        text = word.word.toString()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = word.translation.toString()
                    )
                }
                if (word.desc.toString() != ""){
                    Text(
                        text = word.desc.toString(),
                        style = TextStyle(color = MaterialTheme.colorScheme.onSurface)
                    )
                }

            }
            Spacer(
                modifier = Modifier.weight(1f)
            )
//            Button(onClick = { /*TODO*/ }) {
//                Icon(
//                    imageVector = Icons.Default.Delete,
//                    contentDescription = "Add Word"
//                )
//            }
        }
    }
}