package com.example.supabase

import android.content.ClipData.Item
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.supabase.ui.theme.SupabaseTheme
import io.github.jan.supabase.postgrest.postgrest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupabaseTheme {

                MainScreen()

            }
        }
    }
}

@Composable
fun MainScreen(viewModel: SupabaseAuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()){
    val context = LocalContext.current
    val userState by viewModel.userState

    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    var userText by remember { mutableStateOf("")}
    var userUpdateText by remember { mutableStateOf("")}
    var userId by remember { mutableStateOf("") }
    var userDeleteId by remember { mutableStateOf("") }


    var currentUserState by remember { mutableStateOf("") }

    LaunchedEffect(Unit){
        viewModel.isLoggedIn(context)
    }
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {

        OutlinedTextField(value = userEmail, onValueChange = { userEmail = it }, placeholder = {
            Text(
                text = "Enter Email"
            )
        })
        Spacer(modifier = Modifier.padding(8.dp))
        OutlinedTextField(
            value = userPassword,
            onValueChange = { userPassword = it },
            placeholder = {
                Text(
                    text = "Enter Password"
                )
            })
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = {
            viewModel.signUp(
                context, userEmail, userPassword
            )
        }) {
            Text(text = "Sign up")
        }
        Button(onClick = {
            viewModel.logIn(
                context, userEmail, userPassword
            )

        }) {
            Text(text = "Login")
        }

        Button(
            onClick = { viewModel.logOut(context) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = "Logout")
        }
        Spacer(modifier = Modifier.padding(8.dp))

        if (currentUserState.isNotEmpty()) {
            Text(text = currentUserState)
        }
        OutlinedTextField(
            value = userText,
            onValueChange = { userText = it },
            placeholder = {
                Text(
                    text = "Enter anything"
                )
            })
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = {
            viewModel.insertData(userText)
        }) {
            Text(text = "Insert")
        }
        Button(onClick = {
            viewModel.readData()
        }) {
            Text(text = "Read")
        }
        OutlinedTextField(
            value = userUpdateText,
            onValueChange = { userUpdateText = it },
            placeholder = {
                Text(
                    text = "Update Message"
                )
            })
        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            placeholder = {
                Text(
                    text = "Update Id"
                )
            })
        Button(onClick = {
            viewModel.updateData(userId.toInt(),userUpdateText)
        }) {
            Text(text = "Update")
        }

        OutlinedTextField(
            value = userDeleteId,
            onValueChange = { userDeleteId = it },
            placeholder = {
                Text(
                    text = "Delete Id"
                )
            })
        Button(onClick = {
            viewModel.deleteData(userDeleteId.toInt())
        }) {
            Text(text = "Delete")
        }

        when (userState) {
            is UserState.Loading -> {
                LoadingComponent()
            }
            is UserState.Success -> {
                val message = (userState as UserState.Success).message
                currentUserState = message
            }

            is UserState.Error -> {
                val message = (userState as UserState.Error).message
                currentUserState = message
            }
        }




    }
}




@Composable
fun LoadingComponent(){
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Loading...")
    }
}