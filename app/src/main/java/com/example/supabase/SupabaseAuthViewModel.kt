package com.example.supabase

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class SupabaseAuthViewModel : ViewModel(){
    private val _userState = mutableStateOf<UserState>(UserState.Loading)
    val userState: State<UserState> = _userState

    fun signUp(
        context: Context,
        userEmail: String,
        userPassword: String
    ){
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                SupabaseClient.client.auth.signUpWith(Email){
                    email = userEmail
                    password = userPassword
                }
                saveToken(context)
                _userState.value = UserState.Success("Registered user successfully!")
            } catch (e : Exception){
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }

    fun logIn(
        context: Context,
        userEmail: String,
        userPassword: String
    ){
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                SupabaseClient.client.auth.signInWith(Email){
                    email = userEmail
                    password = userPassword
                }
                saveToken(context)
                _userState.value = UserState.Success("Logged In successfully!")
            } catch (e : Exception){
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }

    fun logOut(context: Context){
        viewModelScope.launch {
            val sharedPref = SharedPreferenceHelper(context)
            _userState.value = UserState.Loading
            try {
                SupabaseClient.client.auth.signOut()
                sharedPref.clearPreferences()
                _userState.value = UserState.Success("Logged Out successfully!")
            } catch (e : Exception){
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }


    fun isLoggedIn(
        context: Context,
    ){
        viewModelScope.launch {
            try {
                val token = getToken(context)
                if (token.isNullOrEmpty()){
                    _userState.value = UserState.Error("User not Logged In!")
                }
                else{
                    SupabaseClient.client.auth.retrieveUser(token)
                    SupabaseClient.client.auth.refreshCurrentSession()
                    saveToken(context)
                    _userState.value = UserState.Success("User is already Logged in!")
                }
            } catch (e : Exception){
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }

    private fun saveToken(context: Context){
        viewModelScope.launch {
            val accessToken = SupabaseClient.client.auth.currentAccessTokenOrNull() ?: ""
            val sharedPref = SharedPreferenceHelper(context)
            sharedPref.saveStringData("accessToken",accessToken)
        }
    }

    private fun getToken(context: Context): String?{
        val sharedPref = SharedPreferenceHelper(context)
        return sharedPref.getStringData("accessToken")

    }

    fun insertData(newText: String){
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                SupabaseClient.client.postgrest.from("workshop").insert(
                    Workshop(workshopText = newText)
                )
                _userState.value = UserState.Success("Inserted data successfully")
            } catch (e : Exception){
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }
    fun readData(){
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val data = SupabaseClient.client.postgrest.from("workshop").select().decodeList<Workshop>()
                _userState.value = UserState.Success("Data: $data")
            } catch (e : Exception){
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }
    fun updateData( id:Int,newUpdateText: String){
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                SupabaseClient.client.postgrest.from("workshop").update(
                    { Workshop::workshopText setTo newUpdateText}
                ){
                    filter {
                        Workshop::id eq id
                    }
                }
                _userState.value = UserState.Success("Update data successfully")
            } catch (e : Exception){
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }
    fun deleteData(id: Int){
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                SupabaseClient.client.postgrest.from("workshop").delete {
                    filter {
                        Workshop::id eq id
                    }
                }
                _userState.value = UserState.Success("Delete data successfully")
            } catch (e : Exception){
                _userState.value = UserState.Error("Error: ${e.message}")
            }
        }
    }






}