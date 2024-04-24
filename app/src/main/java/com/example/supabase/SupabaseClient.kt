package com.example.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest


object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://ctqulljkmqmvqrnutsrx.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN0cXVsbGprbXFtdnFybnV0c3J4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTA4MDI3MTQsImV4cCI6MjAyNjM3ODcxNH0.y9mcONjumDk6wTkt7Bjce4_F-zm0oMzLF5lXy41Rb7M"
    ){
        install(Auth)
        install(Postgrest)
    }
}