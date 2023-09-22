package br.senai.sp.jandira.upload_firebase

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import br.senai.sp.jandira.upload_firebase.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

//  DECLARAÇÃO DOS ATRIBUTOS :
//  ACTIVITYMAINBINDING - MANIPULAÇÃO DOS ELEMENTOS GRAFICOS DO MATERIAL DESIGN
    private lateinit var  binding: ActivityMainBinding
//  STORAGEREFERENCE PERMITE A MANIPULAÇÃO DO CLOUD STORE(ARMAZENA ARQUIVOS)
    private lateinit var storageref: StorageReference
//  FIREBASEFIRESTORE - PERMITE A MANIPULAÇÃO DO BANCO DE DADOS NOSQL
    private lateinit var firebaseFireStorage: FirebaseFirestore

//  URI - PERMITE A MANIPULAÇÃO DE ARQUIVOSATRAVÉS DO SEU ENDEREÇAMENTO
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initiVars()
        registerClickEvents()
    }

//    Funçao de inicialização dos recursos do firebase
    private fun initiVars(){
        storageref = FirebaseStorage.getInstance().reference.child("imagens")
        firebaseFireStorage = FirebaseFirestore.getInstance()
    }

//    Funçao para o lancador de recuperação de imagens da galeria
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it
        binding.imageView.setImageURI(it)
    }

    private fun registerClickEvents(){
//      trata o evento de click do componente imageview
        binding.imageView.setOnClickListener{
            resultLauncher.launch("image/*")

//          trata o evento de click do botao upload
            binding.uploadBtn.setOnClickListener{
                uploadImage()
            }
        }
    }
//  Função de upload
    private fun uploadImage(){

    binding.progressBar.visibility = View.VISIBLE
//    Define um nome unico para a imagem com uso de um valor timestamp
        storageref = storageref.child(System.currentTimeMillis().toString())

        imageUri?.let{
            storageref.putFile(it).addOnCompleteListener{
                task->
                    if (task.isSuccessful){
                        Toast.makeText(this,"UPLOAD CONCLUIDO!", Toast.LENGTH_LONG).show()

                    } else {
                        Toast.makeText(this,"ERRO AO REALIZAR O UPLOAD!", Toast.LENGTH_LONG).show()
                    }

                    binding.progressBar.visibility = View.GONE
            }
        }
    }
}