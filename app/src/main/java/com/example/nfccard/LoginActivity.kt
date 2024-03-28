package com.example.nfccard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nfccard.databinding.ActivityLoginBinding
import com.example.nfccard.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {


    lateinit var binding: ActivityLoginBinding
    lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(Preferences(this).userId!=""){ goToHome()}
        else{
            initLogin()
        }


    }

    private fun initLogin(){
        db=FirebaseFirestore.getInstance()
        binding.loginBtn.setOnClickListener {
            val mName=binding.nameBox.text.toString()
            val mPass=binding.passBox.text.toString()

            if(mName.isNotEmpty()&&mPass.isNotEmpty()){

                db.collection("Employee").whereEqualTo("first_name",mName)
                    .whereEqualTo("employeeid",mPass).get().addOnSuccessListener {
                        if(it.documents.isEmpty()){
                            Toast.makeText(this@LoginActivity,"Invalid Credentials!", Toast.LENGTH_LONG).show()
                            return@addOnSuccessListener
                        }
                        val doc=it.documents[0]
                        if(doc.exists()){
                            val configId=doc.getString("config_id")
                            val id=doc.getString("id")
                            if(configId==null){
                                Toast.makeText(this@LoginActivity,"Config Id not found", Toast.LENGTH_LONG).show()
                                return@addOnSuccessListener
                            }

                            Preferences(this).userId= "$mName#$id#$configId"
                            goToHome()
                        }else{
                            Toast.makeText(this@LoginActivity,"Invalid Credentials!", Toast.LENGTH_LONG).show()
                        }

                    }.addOnFailureListener {
                        Toast.makeText(this@LoginActivity,"Something wrong try again!", Toast.LENGTH_LONG).show()

                    }

            }else{
                Toast.makeText(this@LoginActivity,"Name and Password Required", Toast.LENGTH_LONG).show()
            }




        }
    }

    private fun goToHome(){
        startActivity(Intent(this,MainActivity::class.java))
    }
}