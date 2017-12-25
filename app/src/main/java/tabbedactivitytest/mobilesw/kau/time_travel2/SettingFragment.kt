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
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.baoyz.swipemenulistview.SwipeMenu
import com.baoyz.swipemenulistview.SwipeMenuCreator




/**
 * Created by USER on 2017-11-26.
 */
class SettingFragment : Fragment() {
    private lateinit var expandableListView: ExpandableListView
    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mDatabase: FirebaseDatabase
    var mUser : FirebaseUser? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.i("fragmentLifeCycle", "onAttach")
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("MUser")
        mDatabaseReference.keepSynced(true)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.setting_tab3,container,false)
        val view = rootView.findViewById<LinearLayout>(R.id.list_main)
        val view1 : LinearLayout = rootView.findViewById(R.id.sub1)
        val view2 : LinearLayout = rootView.findViewById(R.id.sub2)
        val view3 : LinearLayout = rootView.findViewById(R.id.sub3)
        val view4 : LinearLayout = rootView.findViewById(R.id.sub4)

        //--------- fragment backPress ----------//
        rootView.isFocusableInTouchMode = true
        rootView.requestFocus()
        rootView.setOnKeyListener(object : View.OnKeyListener {

            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (view.visibility == View.GONE) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                        refresh()
                    }

                    return true
                }

                return false
            }
        })

        //--------- setting main ----------//
        val list_view : ListView = rootView.findViewById(R.id.list_view_setting)
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
        val userImg : ImageView = rootView.findViewById(R.id.accountImage)
        val photoUrl : Uri? = mUser!!.photoUrl
        Picasso.with(context).load(photoUrl).into(userImg)

        val nameText1 : TextView = rootView.findViewById(R.id.accountName1)
        val nameText2 : TextView = rootView.findViewById(R.id.accountName2)
        val emailText1 : TextView = rootView.findViewById(R.id.accountEmail1)
        val emailText2 : TextView = rootView.findViewById(R.id.accountEmail2)

        nameText1.text = mUser!!.displayName.toString()
        nameText2.text = mUser!!.displayName.toString()
        emailText1.text = mUser!!.email.toString()
        emailText2.text = mUser!!.email.toString()


        val logoutBtn : LinearLayout = rootView.findViewById(R.id.logout)
        logoutBtn.setOnClickListener {
            Toast.makeText(context, "로그아웃", Toast.LENGTH_SHORT).show()
            FirebaseAuth.getInstance().signOut()
            activity?.finish()
        }

        //--------- setting sub menu 2 ----------//
        val friendsEdit : EditText = rootView.findViewById<EditText>(R.id.friendsEdit)
        val friendsBtn : Button = rootView.findViewById(R.id.friendBtn)
        val friendsList : SwipeMenuListView = rootView.findViewById(R.id.friendsList)

        var list : ArrayList<String> = ArrayList()


        val adapter : ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_list_item_1, list)
        friendsList.adapter = adapter


        //--------- setting sub menu 3 ----------//




        //--------- setting sub menu 4 ----------//
        expandableListView = rootView.findViewById(R.id.expandableListView)

        val answerList = ArrayList<String>()
        answerList.add("Q. 이 어플은 어떤 용도인가요?")
        answerList.add("Q. 개발자는 누구인가요?")

        expandableListView.setAdapter(ExpandableListViewAdapter(context!!, answerList))

        return rootView
    }

    fun refresh() {
        val ft = fragmentManager!!.beginTransaction()
        ft.detach(this).attach(this).commit()
    }

}