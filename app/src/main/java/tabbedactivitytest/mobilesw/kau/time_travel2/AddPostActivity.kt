package tabbedactivitytest.mobilesw.kau.time_travel2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*


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
    lateinit var locationManager: LocationManager
    lateinit var locationListener : LocationListener
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
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object: LocationListener{
            override fun onLocationChanged(location: Location?) {
                if(location !=null) {
                    var myLocation = LatLng(location.latitude, location.longitude)
                  /*  Log.d("My Location", myLocation.toString())
                    Toast.makeText(this@AddPostActivity, "location" + myLocation.toString(), Toast.LENGTH_LONG).show()*/
                    var geogoder: Geocoder = Geocoder(applicationContext, Locale.getDefault())
                    try {
                        var addressList: List<Address> = geogoder.getFromLocation(location.latitude, location.longitude, 1)
                        var fullAddress = ""
                        if (addressList != null && addressList.size > 0) {
                            Log.d("Address: ", addressList[0].toString());


                            if (addressList[0].getAddressLine(0) != null) {
                                fullAddress += addressList[0].getAddressLine(0) + " "

                            }
                            if (addressList[0].getSubAdminArea() != null) {
                                fullAddress += addressList[0].subAdminArea + " "
                            }
                            Toast.makeText(applicationContext, "Address+" + fullAddress, Toast.LENGTH_LONG).show();
                            mLocation.setText(fullAddress)
                        } else {
                            Log.d("Address:", "Couldn't find Address");
                        }

                    }catch (e : IOException){
                        e.printStackTrace()
                    }
                }
                else{
                    /*Log.d("My Location", "is null")
                    Toast.makeText(this@AddPostActivity, "location is null" , Toast.LENGTH_LONG).show()*/
                }
            }

            override fun onProviderDisabled(provider: String?) {

            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }
        }



        mPostButton.setOnClickListener {
            startPosting()
        }
        mPostImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.setType("image/*")
            startActivityForResult(galleryIntent,GALLERY_CODE)

        }
        if(Build.VERSION.SDK_INT <23){
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
            }
        }else{
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)

            }
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
        val location = mLocation.text.toString().trim()
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
                dataToSave.put("location",location)
                dataToSave.put("timeStamp",java.lang.System.currentTimeMillis().toString())
                dataToSave.put("image",downLoadUrl.toString())

                newPost.setValue(dataToSave)
                finish()
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
        }
    }


}
