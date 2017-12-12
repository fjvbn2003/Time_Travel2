package tabbedactivitytest.mobilesw.kau.time_travel2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CreateAccountActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var registerButoon: Button
    lateinit var emailField: EditText
    lateinit var passwordField: EditText
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mDatabase: FirebaseDatabase
    lateinit var mPostImage: ImageButton
    lateinit var mImageUri: Uri
    lateinit var mStorage : StorageReference
    val GALLERY_CODE:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("MUser")
        mDatabaseReference.keepSynced(true)
        mStorage = FirebaseStorage.getInstance().reference
        mPostImage = findViewById(R.id.imageButton_Create)
        emailField = findViewById(R.id.Email_create)
        passwordField = findViewById(R.id.password_create)
        registerButoon = findViewById(R.id.button_create)
        mImageUri = Uri.EMPTY
        mAuth.signOut()
        registerButoon.setOnClickListener {
            if(mImageUri == Uri.EMPTY){
                Toast.makeText(this, "이미지를 추가해 주세요",Toast.LENGTH_LONG).show()
            }
            else{
                mAuth.createUserWithEmailAndPassword(emailField.text.toString(), passwordField.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val filePath : StorageReference = mStorage.child("Muser_images").child(emailField.text.toString())
                                filePath.putFile(mImageUri).addOnSuccessListener {
                                    taskSnapshot ->
                                    val downLoadUrl :Uri? = taskSnapshot.downloadUrl
                                    val newPost: DatabaseReference = mDatabaseReference.push()
                                    val dataToSave = HashMap<String,String>()
                                    dataToSave.put("userID", emailField.text.toString())
                                    dataToSave.put("password",passwordField.text.toString())
                                    dataToSave.put("image",downLoadUrl.toString())
                                    newPost.setValue(dataToSave)

                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(this,"회원가입을 성공하였습니다.", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, TabbedActivity::class.java)
                                    startActivity(intent)
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i( "Register failure", task.exception.toString())
                                Toast.makeText(this,"회원가입 실패", Toast.LENGTH_LONG).show()

                            }

                            // ...
                        }
            }

        }
        mPostImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.setType("image/*")
            startActivityForResult(galleryIntent,GALLERY_CODE)
        }



    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK && data != null){
            mImageUri = data.data
            mPostImage.setImageURI(mImageUri)
        }
    }

}
