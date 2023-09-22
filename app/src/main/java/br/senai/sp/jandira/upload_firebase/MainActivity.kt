package br.senai.sp.jandira.upload_firebase

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.senai.sp.jandira.upload_firebase.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

//  DECLARAÇÃO DOS ATRIBUTOS :
//  ACTIVITYMAINBINDING - MANIPULAÇÃO DOS ELEMENTOS GRAFICOS DO MATERIAL DESIGN
    private lateinit var  binding: ActivityMainBinding
//  STORAGEREFERENCE PERMITE A MANIPULAÇÃO DO CLOUD STORE(ARMAZENA ARQUIVOS)
    private lateinit var storageref: StorageReference
//  FIREBASEFIRESTORE - PERMITE A MANIPULAÇÃO DO BANCO DE DADOS NOSQL
    private lateinit var firebaseFireStorage: FirebaseStorage

//  URI - PERMITE A MANIPULAÇÃO DE ARQUIVOSATRAVÉS DO SEU ENDEREÇAMENTO
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
    }
}