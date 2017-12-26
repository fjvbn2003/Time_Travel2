package tabbedactivitytest.mobilesw.kau.time_travel2

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.KeyEvent
import android.widget.*
import com.baoyz.swipemenulistview.SwipeMenuListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.setting_menu1.*
import org.w3c.dom.Text
import android.R.drawable.ic_delete
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.baoyz.swipemenulistview.SwipeMenu
import com.baoyz.swipemenulistview.SwipeMenuCreator
import android.widget.Toast
import android.content.DialogInterface
import android.content.Intent
import android.support.constraint.ConstraintLayout
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.vision.text.Line
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.setting_menu1_2.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 * Created by USER on 2017-11-26.
 */
class SettingFragment : Fragment() {
    private lateinit var expandableListView: ExpandableListView
    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mDatabase: FirebaseDatabase
    lateinit var mStorage: StorageReference
    var mUser : FirebaseUser? = null

    private lateinit var adapter: ArrayAdapter<String>
    var friendsList : ArrayList<String> = ArrayList<String>()

    lateinit var imageUpdate: ImageView
    var mImageUri: Uri? = null
    val GALLERY_CODE : Int = 1

    lateinit var nameText1 : TextView
    lateinit var nameText2 : TextView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.i("fragmentLifeCycle", "onAttach")
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser
        mDatabase = FirebaseDatabase.getInstance()
        mStorage = FirebaseStorage.getInstance().reference
        mDatabaseReference = mDatabase.reference.child("MUser")
        mDatabaseReference.keepSynced(true)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refresh()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.setting_tab3,container,false)
        val view = rootView.findViewById<LinearLayout>(R.id.list_main)
        val view1 : LinearLayout = rootView.findViewById(R.id.sub1)
        val view1_2 : LinearLayout = rootView.findViewById(R.id.sub1_2)
        val view2 : LinearLayout = rootView.findViewById(R.id.sub2)
        val view3 : LinearLayout = rootView.findViewById(R.id.sub3)
        val view4 : LinearLayout = rootView.findViewById(R.id.sub4)

        //--------- menu1 variable ----------//
        val userImg : ImageView = rootView.findViewById(R.id.accountImage)
        val photoUrl : Uri? = mUser!!.photoUrl
        nameText1 = rootView.findViewById(R.id.accountName1)
        nameText2 = rootView.findViewById(R.id.accountName2)
        val emailText1 : TextView = rootView.findViewById(R.id.accountEmail1)
        val emailText2 : TextView = rootView.findViewById(R.id.accountEmail2)

        //--------- fragment backPress ----------//
        rootView.isFocusableInTouchMode = true
        rootView.requestFocus()
        rootView.setOnKeyListener(object : View.OnKeyListener {

            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (view1_2.visibility == View.VISIBLE) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                        view1_2.visibility = View.GONE
                        view1.visibility = View.VISIBLE
                    }

                    return true
                } else if (view.visibility == View.GONE) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                        mainRefresh()
                    }

                    return true
                }

                return false
            }
        })

        //--------- setting main ----------//
        val list_view : ListView = rootView.findViewById<ListView>(R.id.list_view_setting)
        val setting_item : ArrayList<String> = ArrayList<String>()
        setting_item.add("계정관리")
        setting_item.add("친구관리")
        setting_item.add("위젯관리")
        setting_item.add("도움말")
        val arrayAdapter = ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,setting_item)
        list_view.adapter = arrayAdapter

        list_view.setOnItemClickListener({ adapter, v, position, arg3 ->
            val value = adapter.getItemAtPosition(position) as String

            when (value) {
                "계정관리" -> {
                    view.visibility = View.GONE
                    view1.visibility = View.VISIBLE
                }
                "친구관리" -> {
                    view.visibility = View.GONE
                    view2.visibility = View.VISIBLE
                }
                "위젯관리" -> {
                    view.visibility = View.GONE
                    view3.visibility = View.VISIBLE
                }
                "도움말" -> {
                    view.visibility = View.GONE
                    view4.visibility = View.VISIBLE
                }
            }
        })

        //--------- setting sub menu 1 ----------//
        Picasso.with(context).load(photoUrl).into(userImg)
        nameText1.text = mUser!!.displayName.toString()
        nameText2.text = mUser!!.displayName.toString()
        emailText1.text = mUser!!.email.toString()
        emailText2.text = mUser!!.email.toString()

        val accountUpdateBtn : LinearLayout = rootView.findViewById(R.id.accountUpdateBtn)
        accountUpdateBtn.setOnClickListener {
            view1.visibility = View.GONE
            view1_2.visibility = View.VISIBLE
        }

        val logoutBtn : LinearLayout = rootView.findViewById(R.id.logout)
        logoutBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("알림")
            builder.setMessage("정말 로그아웃 하시겠습니까?")
            builder.setPositiveButton("예",
                    { dialog, which ->
                        Toast.makeText(context, "로그아웃", Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().signOut()
                        activity?.finish()
                    })
            builder.setNegativeButton("아니오",
                    DialogInterface.OnClickListener { dialog, which -> })
            builder.show()
        }


        //--------- setting sub menu 1_2 ----------//
        imageUpdate = rootView.findViewById(R.id.imageUpdate)
        val nameUpdate : EditText = rootView.findViewById(R.id.nameUpdate)
        val updateBtn : Button = rootView.findViewById(R.id.updateBtn)

        Picasso.with(context).load(photoUrl).into(imageUpdate)
        nameUpdate.hint = mUser!!.displayName.toString()

        imageUpdate.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.setType("image/*")
            startActivityForResult(galleryIntent, GALLERY_CODE)
        }

        updateBtn.setOnClickListener {
            if (mImageUri != null) {
                mStorage.child("Muser_images").child(mUser!!.email.toString()).putFile(mImageUri!!).addOnSuccessListener { taskSnapshot ->
                    val downLoadUrl: Uri? = taskSnapshot.downloadUrl
                    if (downLoadUrl != null) {
                        setUserProfile(nameUpdate.text.toString(), downLoadUrl)
                    }
                }
            } else {
                setUserProfile(nameUpdate.text.toString())
            }

            updateView()

            mainRefresh()
        }
        //--------- setting sub menu 2 ----------//
        val friendsEdit : EditText = rootView.findViewById<EditText>(R.id.friendsEdit)
        val friendsBtn : Button = rootView.findViewById(R.id.friendBtn)

        adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, friendsList)
        val list : ListView = rootView.findViewById(R.id.friendsList)
        list.adapter = adapter

        list.setOnItemClickListener { parent, view, position, id ->
            val friendID = parent.getItemAtPosition(position) as String

            val builder = AlertDialog.Builder(context)
            builder.setTitle("알림")
            builder.setMessage("친구 ${friendID}를 삭제하시겠습니까?")
            builder.setPositiveButton("예",
                    { dialog, which ->
                        delFriend(friendID)
                        friendsList.remove(friendID)
                        adapter.notifyDataSetChanged()
                    })
            builder.setNegativeButton("아니오",
                    DialogInterface.OnClickListener { dialog, which -> })
            builder.show()
        }


        friendsBtn.setOnClickListener {
            val friendID = friendsEdit.text.toString()
            friendsEdit.text = null

            validate(friendID)

        }


        //--------- setting sub menu 3 ----------//
        val optionSwitch : Switch = rootView.findViewById(R.id.optSwitch)
        var switchState : Boolean = optionSwitch.isChecked
        if (switchState) {

        } else {

        }


        //--------- setting sub menu 4 ----------//
        expandableListView = rootView.findViewById(R.id.expandableListView)

        val answerList = ArrayList<String>()
        answerList.add("Q. 이 어플은 어떤 용도인가요?")
        answerList.add("Q. 개발자는 누구인가요?")

        expandableListView.setAdapter(ExpandableListViewAdapter(context!!, answerList))

        return rootView
    }

    fun refresh() {
        val responseListener = Response.Listener<String> { response ->
            try {
                friendsList.clear()
                Log.i("response", response.toString())
                val jsonObject = JSONObject(response)
                val jsonArray: JSONArray = jsonObject.getJSONArray("response")
                var count = 0
                var userID: String
                var friendID: String
                while (count < jsonArray.length()) {
                    val jobject: JSONObject = jsonArray.getJSONObject(count)
                    userID = jobject.getString("userID")

                    if (userID == mUser!!.email.toString()) {
                        friendID = jobject.getString("friendID")
                        friendsList.add(friendID)
                    }

                    count++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Log.i("userID", mUser!!.email.toString())
        val friendRequest = FriendRequest(mUser!!.email.toString(), responseListener)
        val queue = Volley.newRequestQueue(activity)
        queue.add(friendRequest)
    }

    private fun mainRefresh() {
        val ft = fragmentManager!!.beginTransaction()
        ft.detach(this).attach(this).commit()
    }

    private fun validate(friendID: String) {
        val responseListener = Response.Listener<String> { response ->
            try {
                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getBoolean("success")
                if (success) {
                    addFriend(friendID)
                } else {
                    Toast.makeText(activity, "가입하지 않은 유저입니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val validateRequest = ValidateRequest(friendID, responseListener)
        val queue = Volley.newRequestQueue(context)
        queue.add(validateRequest)
    }

    private fun addFriend(friendID: String) {
        val responseListener = Response.Listener<String> { response ->
            try {
                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getBoolean("success")
                if (success) {
                    Toast.makeText(activity, "친구 추가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    refresh()
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(activity, "서버 오류입니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val addRequest = AddFriendRequest(mUser!!.email.toString(), friendID, responseListener)
        val queue = Volley.newRequestQueue(context)
        queue.add(addRequest)
    }

    private fun getFriend(userID: String) {
        val responseListener = Response.Listener<String> { response ->
            try {
                val jsonResponse = JSONObject(response)
                val friendsList = jsonResponse.getJSONArray("friendID")
                Toast.makeText(activity, friendsList.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val delRequest = GetFriendListRequest(userID, responseListener)
        val queue = Volley.newRequestQueue(context)
        queue.add(delRequest)
    }

    private fun delFriend(friendID: String) {
        val responseListener = Response.Listener<String> { response ->
            try {
                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getBoolean("success")
                if (success) {
                    Toast.makeText(activity, "친구 삭제 완료되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "서버 오류입니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val delRequest = DelFriendRequest(mUser!!.email.toString(), friendID, responseListener)
        val queue = Volley.newRequestQueue(context)
        queue.add(delRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK && data != null){
            mImageUri = data.data
            CropImage.activity(mImageUri)
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setRequestedSize(400,400, CropImageView.RequestSizeOptions.RESIZE_EXACT)
                    .start(context!!, this)
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result = CropImage.getActivityResult(data)
            if(resultCode == Activity.RESULT_OK){
                val resultUri = result.uri
                imageUpdate.setImageURI(resultUri)
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val error = result.error
            }
        }
    }

    private fun setUserProfile(nameStr: String) {
        val user = mAuth.currentUser
        var userName = mUser!!.displayName.toString()

        if (nameStr != "") {
            userName = nameStr
        }

        if (user != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build()
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(task: Task<Void>) {
                            if (task.isSuccessful) {

                            }
                        }
                    }).addOnFailureListener { e ->
            }
        }
    }

    private fun setUserProfile(nameStr: String, downloadUrl: Uri) {
        val user = mAuth.currentUser
        var userName = mUser!!.displayName.toString()

        if (nameStr != "") {
            userName = nameStr
        }

        if (user != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .setPhotoUri(downloadUrl)
                    .build()
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(task: Task<Void>) {
                            if (task.isSuccessful) {

                            }
                        }
                    }).addOnFailureListener { e ->
            }
        }
    }

    private fun updateView() {
        Picasso.with(context).load(mImageUri).into(imageUpdate)
        nameText1.text = mUser!!.displayName.toString()
        nameText2.text = mUser!!.displayName.toString()
    }
}