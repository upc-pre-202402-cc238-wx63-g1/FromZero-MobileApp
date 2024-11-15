package com.cursokotlin.appfromzero.data

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.header
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.statement.bodyAsText
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.ContentType.Application.OctetStream
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File

object SupabaseStorageClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private const val SUPABASE_URL = "https://hwqkibwyspmfwkzjlumy.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3cWtpYnd5c3BtZndrempsdW15Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzE1NDA0NDIsImV4cCI6MjA0NzExNjQ0Mn0.By3x97OAlfZ26rRIwFC2GDblhpc6p7fTxUY-83QO4cY"

    // Función para subir imagen
    suspend fun uploadFileToSupabase(file: File, bucketName: String): String? {
        Log.e("SupabaseStorage", "Subiendo archivo al bucket $bucketName")
        return withContext(Dispatchers.IO) {
            try {
                // Subir archivo al Storage
                Log.e("SupabaseStorage", "$SUPABASE_URL/storage/v1/object/$bucketName/${file.name}")
                val response: HttpResponse = client.post("$SUPABASE_URL/storage/v1/object/$bucketName/${file.name}") {
                    header(HttpHeaders.Authorization, "Bearer $SUPABASE_KEY")
                    header(HttpHeaders.ContentType, "image/png")
                    setBody(file.readBytes())
                }

                // Verificar si la carga fue exitosa
                if (response.status == HttpStatusCode.OK) {
                    // Si la respuesta es exitosa, genera la URL pública
                    val publicUrl = "$SUPABASE_URL/storage/v1/object/public/$bucketName/${file.name}"
                    Log.d("SupabaseStorage", "Imagen subida correctamente. URL: $publicUrl")
                    return@withContext publicUrl
                } else {
                    // Si hubo error, registrar el error
                    Log.e("SupabaseStorage", "Error al subir la imagen, status: ${response.status}")
                    return@withContext null
                }
            } catch (e: Exception) {
                // Manejar posibles errores durante la solicitud HTTP
                Log.e("SupabaseStorage", "Error al subir la imagen, catch: ${e.message}")
                e.printStackTrace()
                return@withContext null
            }
        }
    }
}
