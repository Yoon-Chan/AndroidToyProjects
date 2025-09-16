package com.example.listswipeex

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun ContactScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val contacts = remember {
        mutableStateListOf((1..100).map {
            ContactUi(
                id = it,
                name = "Contact $it",
                isOptionsRevealed = false
            )
        }.toTypedArray())
    }

//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        itemsIndexed(
//            items = contacts,
//            key = { _, contact -> contact.id }
//        ) {
//
//        }
//    }
}