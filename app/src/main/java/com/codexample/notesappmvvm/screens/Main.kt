package com.codexample.notesappmvvm.screens

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.codexample.notesappmvvm.MainViewModel
import com.codexample.notesappmvvm.MainViewModelFactory
import com.codexample.notesappmvvm.model.Note
import com.codexample.notesappmvvm.navigation.NavRoute
import com.codexample.notesappmvvm.ui.theme.NotesAppMVVMTheme
import com.codexample.notesappmvvm.utils.Constants
import com.codexample.notesappmvvm.utils.DB_TYPE
import com.codexample.notesappmvvm.utils.TYPE_FIREBASE
import com.codexample.notesappmvvm.utils.TYPE_ROOM

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel) {
    val notes = viewModel.readAllNotes().observeAsState(listOf()).value
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NavRoute.Add.route)
                }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Icons",
                    tint = Color.White
                )
            }
        }
    ) {
        LazyColumn {
            items(notes) { note ->
                NoteItem(note = note, navController = navController)
            }
        }
    }
}

@Composable
fun NoteItem(note: Note, navController: NavHostController) {
    val noteId = when(DB_TYPE) {
        TYPE_FIREBASE -> note.firebaseId
        TYPE_ROOM -> note.id
        else -> Constants.Keys.EMPTY
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 24.dp)
            .clickable {
                navController.navigate(NavRoute.Note.route + "/${noteId}")
            },
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = note.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = note.subtitle)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrevMainScreen() {
    NotesAppMVVMTheme {
        val context = LocalContext.current
        val mViewModel: MainViewModel =
            viewModel(factory = MainViewModelFactory(context.applicationContext as Application))
        MainScreen(navController = rememberNavController(), viewModel = mViewModel)

    }
}
