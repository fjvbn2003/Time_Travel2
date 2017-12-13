package tabbedactivitytest.mobilesw.kau.time_travel2

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ExpandableListView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import android.text.method.TextKeyListener.clear
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK



/**
 * Created by USER on 2017-11-26.
 */
class SettingFragment : Fragment() {
    lateinit var expandableListView: ExpandableListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.setting_tab3,container,false)
        val view = rootView.findViewById<LinearLayout>(R.id.list_main)
        val view4 = rootView.findViewById<LinearLayout>(R.id.sub4)

        //--------- fragment backPress ----------//
        rootView.isFocusableInTouchMode = true
        rootView.requestFocus()
        rootView.setOnKeyListener(object : View.OnKeyListener {

            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (view.visibility == View.GONE) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                        view.visibility = View.VISIBLE
                        view4.visibility = View.GONE
                    }

                    return true
                }

                return false
            }
        })

        //--------- setting main ----------//
        val list_view = rootView.findViewById<ListView>(R.id.list_view_setting)
        val setting_item = ArrayList<String>()
        setting_item.add("My Account")
        setting_item.add("Add Friends")
        setting_item.add("Widgets")
        setting_item.add("Q&A")
        val arrayAdapter = ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,setting_item)
        list_view.adapter = arrayAdapter

        list_view.setOnItemClickListener({ adapter, v, position, arg3 ->
            val value = adapter.getItemAtPosition(position) as String

            when (value) {
                "My Account" -> {
                    view.visibility = View.GONE
                }
                "Add Friends" -> {
                    view.visibility = View.GONE
                }
                "Widgets" -> {
                    view.visibility = View.GONE
                }
                "Q&A" -> {
                    view.visibility = View.GONE
                    view4.visibility = View.VISIBLE
                }
            }
        })

        //--------- setting sub menu 4 ----------//
        expandableListView = rootView.findViewById(R.id.expandableListView)

        val modelList = ArrayList<String>()
        modelList.add("Q. 이 어플은 어떤 용도인가요?")
        modelList.add("Q. 개발자는 누구인가요?")

        expandableListView.setAdapter(ExpandableListViewAdapter(context!!, modelList))


        return rootView
    }


}