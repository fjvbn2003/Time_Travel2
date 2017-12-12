package tabbedactivitytest.mobilesw.kau.time_travel2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import android.util.Log
import android.widget.EditText
import android.content.Intent


class MainActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    var mUser :FirebaseUser? = null
    lateinit var loginButton: Button
    lateinit var registerButoon: Button
    lateinit var emailField: EditText
    lateinit var passwordField: EditText
    // 로그인 되어있는지 확인
    override fun onStart() {
        super.onStart()
        mUser = mAuth.currentUser
        if(mUser != null){
            Toast.makeText(this, "Signed in",Toast.LENGTH_LONG).show()
            val intent = Intent(this, TabbedActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginButton = findViewById(R.id.btn_login)
        registerButoon = findViewById(R.id.btn_register)
        emailField = findViewById(R.id.login_email)
        passwordField = findViewById(R.id.login_password)
        mAuth = FirebaseAuth.getInstance()
        //로그인 이벤트
        loginButton.setOnClickListener {
            mAuth.signInWithEmailAndPassword(emailField.text.toString(), passwordField.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("login","success")
                            Toast.makeText(this,"로그인 성공",
                                    Toast.LENGTH_SHORT).show()
                            mUser = mAuth.currentUser
                            intent = Intent(this, TabbedActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i( "signInWithEmail:failure", task.exception.toString())
                            Toast.makeText(this,"Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        }
        //회원가입 이벤트
        registerButoon.setOnClickListener{
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }


    }

}
