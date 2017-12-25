package tabbedactivitytest.mobilesw.kau.time_travel2

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot



/**
 * Created by USER on 2017-11-26.
 */
class HistoryFragment : Fragment() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mDatabase: FirebaseDatabase
    var mUser : FirebaseUser? = null
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var mrecyclerView : RecyclerView
    lateinit var historyRecyclerAdapter : HistoryRecyclerAdapter
    var historyList = ArrayList<History>()


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.i("fragmentLifeCycle","onAttach")
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("MHistory")
        mDatabaseReference.keepSynced(true)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("fragmentLifeCycle","onCreate")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.history_tab1,container,false)
        mDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(data in dataSnapshot.children){
                    if(data.child("userID").value.toString() == mUser!!.email.toString()){
                        Log.i("data", data.toString())
                        val history = History()
                        history.userID = data.child("userID").value.toString()
                        history.historyTitle = data.child("historyTitle").value.toString()
                        history.desc = data.child("desc").value.toString()
                        history.location = data.child("location").value.toString()
                        history.timeStamp = data.child("timeStamp").value.toString()
                        history.image = data.child("image").value.toString()
                        historyList.add(0,history)
                    }

                }
                mrecyclerView = rootView.findViewById(R.id.recyclerView)
                mrecyclerView.setHasFixedSize(true)
                val context = activity as Context
                mLayoutManager = LinearLayoutManager(context)
                mrecyclerView.layoutManager = mLayoutManager
                historyRecyclerAdapter = HistoryRecyclerAdapter(context, historyList)
                mrecyclerView.adapter = historyRecyclerAdapter
                historyRecyclerAdapter.notifyDataSetChanged()
                Log.i("fragmentLifeCycle","onCreateView")
                // Read from the database

            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.i("Failed to read value.", error.toException().toString())
            }
        })
        return rootView
    }


    override fun onStart() {
        super.onStart()
        Log.i("fragmentLifeCycle","onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("fragmentLifeCycle","onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("fragmentLifeCycle","onPause")

    }

    override fun onStop() {
        super.onStop()
        Log.i("fragmentLifeCycle","onStop")
    }


}