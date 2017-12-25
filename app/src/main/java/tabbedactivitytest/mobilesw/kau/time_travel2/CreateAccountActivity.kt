package tabbedactivitytest.mobilesw.kau.time_travel2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.FirebaseUser



class CreateAccountActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var registerButton: Button
    lateinit var emailField: EditText
    lateinit var nameField: EditText
    lateinit var passwordField: EditText
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mDatabase: FirebaseDatabase
    lateinit var mProfileImage: ImageView
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
        mProfileImage = findViewById(R.id.imageButton_Create)
        emailField = findViewById(R.id.Email_create)
        nameField = findViewById(R.id.name_create)
        passwordField = findViewById(R.id.password_create)
        registerButton = findViewById(R.id.button_create)
        mImageUri = Uri.EMPTY
        mAuth.signOut()
        registerButton.setOnClickListener {
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
                                    val dataToSave = HashMap<String,String>()
                                    dataToSave.put("userID", emailField.text.toString())
                                    dataToSave.put("userName", nameField.text.toString())
                                    dataToSave.put("password",passwordField.text.toString())
                                    dataToSave.put("image",downLoadUrl.toString())
                                    mDatabase.reference.child("MUser").child(encodeString(emailField.text.toString())).setValue(dataToSave)
                                    if (downLoadUrl != null) {
                                        setUserProfile(downLoadUrl)
                                    }

                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(this,"회원가입을 성공하였습니다.", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, TabbedActivity::class.java)
                                    startActivity(intent)
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i( "Register failure", task.exception.toString())
                                Toast.makeText(this,"이미 존재하는 이메일입니다.", Toast.LENGTH_LONG).show()

                            }

                            // ...
                        }
            }

        }
        mProfileImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.setType("image/*")
            startActivityForResult(galleryIntent,GALLERY_CODE)
        }



    }

    private fun encodeString(string: String): String {
        return string.replace(".", ",")
    }

    private fun decodeString(string: String): String {
        return string.replace(",", ".")
    }

    private fun setUserProfile(downloadUrl: Uri) {
        val user = mAuth.currentUser

        if (user != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(nameField.text.toString())
                    .setPhotoUri(downloadUrl)
                    .build()
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(task: Task<Void>) {
                            if (task.isSuccessful) {

                            }
                        }
                    }).addOnFailureListener { e ->
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK && data != null){
            mImageUri = data.data
            CropImage.activity(mImageUri)
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setRequestedSize(400,400,CropImageView.RequestSizeOptions.RESIZE_EXACT)
                    .start(this)
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result = CropImage.getActivityResult(data)
            if(resultCode == Activity.RESULT_OK){
                val resultUri = result.uri
                mProfileImage.setImageURI(resultUri)
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val error = result.error
            }
        }
    }

}
