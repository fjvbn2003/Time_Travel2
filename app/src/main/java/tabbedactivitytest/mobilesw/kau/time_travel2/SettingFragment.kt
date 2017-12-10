package tabbedactivitytest.mobilesw.kau.time_travel2

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import tabbedactivitytest.mobilesw.kau.time_travel2.R

/**
 * Created by USER on 2017-11-26.
 */
class SettingFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.setting_tab3,container,false)
        val list_view = rootView.findViewById<ListView>(R.id.list_view_setting)
        val setting_item = ArrayList<String>()
        setting_item.add("My Acoount")
        setting_item.add("Add Friends")
        setting_item.add("Widgets")
        setting_item.add("Q&A")
        val arrayAdapter = ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,setting_item)
        list_view.adapter = arrayAdapter


        return rootView
    }
}