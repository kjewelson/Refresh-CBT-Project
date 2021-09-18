package com.example.recharge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


/*class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}*/
class MainActivity : AppCompatActivity() {

//    FirebaseApp.initializeApp()
    private val RC_SIGN_IN: Int=0
    val ref= FirebaseAuth.getInstance()
    private val auth: FirebaseAuth= FirebaseAuth.getInstance()
    lateinit var gso: GoogleSignInOptions
    lateinit var googleSignInClient:GoogleSignInClient

    //database var 
    private lateinit var database : DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hide the status bar.
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the // status bar is hidden, so hide that too if necessary.
//        actionBar?.hide()
//        actionBar?.hide()

        checkUser()
//        val googleSignIn= findViewById<SignInButton>(R.id.register_google)
        val googleSignIn: Button = findViewById(R.id.register_google)


        googleSignIn.setOnClickListener {
            signIn()
        }


        // Configure Google Sign In
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()



        googleSignInClient=GoogleSignIn.getClient(this,gso)


    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this,"Authentication Failed.",Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {


        if(firebaseUser!=null) {
            val userDName = firebaseUser.displayName

            if(userDName!="jewelson samuel"){
                startActivity(Intent(this@MainActivity, PatientHomeActivity::class.java))}
            else{
                startActivity(Intent(this@MainActivity, DocDash::class.java))
            }

            Log.w("TAG", "UpdateUI:Success")
            createUserProfileDetail(firebaseUser)
            //Toast.makeText(this, "Profile has been registered as $userDName", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun createUserProfileDetail(firebaseUser: FirebaseUser) {

        database= FirebaseDatabase.getInstance().getReference("UserProfile")

        val userName=firebaseUser.displayName
        val userId=firebaseUser.uid
        val userMail=firebaseUser.email

        val userProfile=UserProfile(userName,userId,userMail)

        Log.w("TAG", "$userName create profile start")
        database.child(userId).setValue(userProfile).addOnSuccessListener {
            Toast.makeText(this, "Submitting Create Profile",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Profile Creation Failed",Toast.LENGTH_SHORT).show()
        }

    }

    private fun checkUser(){
        val firebaseUser=auth.currentUser
        if(firebaseUser!=null){
            updateUI(firebaseUser)
        }
    }


}
