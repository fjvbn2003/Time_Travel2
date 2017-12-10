package tabbedactivitytest.mobilesw.kau.time_travel2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class AddPostActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mDatabase: FirebaseDatabase
    lateinit var mStorage : StorageReference
    var mUser : FirebaseUser? = null
    lateinit var mPostImage : ImageButton
    lateinit var mPostTitle: EditText
    lateinit var mDesc : EditText
    lateinit var mLocation: TextView
    lateinit var mPostButton: Button
    lateinit var mImageUri: Uri
    val GALLERY_CODE:Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("MHistory")
        mDatabaseReference.keepSynced(true)
        mStorage = FirebaseStorage.getInstance().reference
        mPostImage = findViewById(R.id.imageButtonAdd)
        mPostTitle = findViewById(R.id.postTitleAdd)
        mDesc = findViewById(R.id.PostTextAdd)
        mLocation = findViewById(R.id.postLocationAdd)
        mPostButton = findViewById(R.id.btn_postAdd)

        mPostButton.setOnClickListener {
            startPosting()
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


    fun startPosting(){
        val title = mPostTitle.text.toString().trim()
        val desc = mDesc.text.toString().trim()
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && mImageUri != null){
            val filePath : StorageReference = mStorage.child("MHistory_images").child(mImageUri.lastPathSegment)
            filePath.putFile(mImageUri).addOnSuccessListener {
                taskSnapshot ->
                val downLoadUrl :Uri? = taskSnapshot.downloadUrl
                val newPost: DatabaseReference = mDatabaseReference.push()
                val dataToSave = HashMap<String,String>()
                dataToSave.put("userID", mUser!!.email.toString())
                dataToSave.put("historyTitle",title)
                dataToSave.put("desc", desc)
                dataToSave.put("location","경기도 고양시 화전동")
                dataToSave.put("timeStamp",java.lang.System.currentTimeMillis().toString())
                dataToSave.put("image",downLoadUrl.toString())

                newPost.setValue(dataToSave)
                finish()
            }

        }
    }



}
