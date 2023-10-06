package br.senai.sp.jandira.upload_firebase

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
//          trata o evento de click do botao listar
            binding.showAllBtn.setOnClickListener{
                startActivity(Intent(this, ImagesFeed::class.java))
            }
        }
    }
//  Função de upload
    private fun uploadImage(){
  
    binding.progressBar.visibility = View.VISIBLE
//    Define um nome unico para a imagem com uso de um valor timestamp
        storageref = storageref.child(System.currentTimeMillis().toString())
//    Executa o processo de upload da imagem - v1 - upload no storage
//        imageUri?.let{
//            storageref.putFile(it).addOnCompleteListener{
//                task->
//                    if (task.isSuccessful){
//                        Toast.makeText(this,"UPLOAD CONCLUIDO!", Toast.LENGTH_LONG).show()
//
//                    } else {
//                        Toast.makeText(this,"ERRO AO REALIZAR O UPLOAD!", Toast.LENGTH_LONG).show()
//                    }
//
//                    binding.progressBar.visibility = View.GONE
//                    binding.imageView.setImageResource(R.drawable.upload)
//
//            }
//        }

//    Executa o processo de upload da imagem - v2 - upload no storage e gravaçao no firestore
    imageUri?.let {
        storageref.putFile(it).addOnCompleteListener { task->

            if (task.isSuccessful) {

                storageref.downloadUrl.addOnSuccessListener { uri ->

                    val map = HashMap<String, Any>()
                    map["pic"] = uri.toString()

                    firebaseFireStorage.collection("images").add(map).addOnCompleteListener { firestoreTask ->

                        if (firestoreTask.isSuccessful){
                            Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()

                        }else{
                            Toast.makeText(this, firestoreTask.exception?.message, Toast.LENGTH_SHORT).show()

                        }
                        binding.progressBar.visibility = View.GONE
                        binding.imageView.setImageResource(R.drawable.upload)

                    }
                }

            }else{

                Toast.makeText(this,  task.exception?.message, Toast.LENGTH_SHORT).show()

            }

            //BARRA DE PROGRESSO DO UPLOAD
            binding.progressBar.visibility = View.GONE

            //TROCA A IMAGEM PARA A IMAGEM PADRÃO
            binding.imageView.setImageResource(R.drawable.upload)

        }
    }
    }
}