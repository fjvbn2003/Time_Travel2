package tabbedactivitytest.mobilesw.kau.time_travel2

/**
 * Created by AHYEON on 2017-12-12.
 */

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView


class ExpandableListViewAdapter(private val context: Context, private val listGroup: List<String>) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return listGroup.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getGroup(groupPosition: Int): Any {
        return listGroup[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {
        return null
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val infalInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.group_layout, null)
        }

        (convertView as TextView).text = getGroup(groupPosition) as String

        return convertView
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val infalInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val itemType = getChildType(groupPosition, childPosition)

            convertView = infalInflater.inflate(R.layout.item_answer, null)

            if (itemType == 0) {
                val txtListChild : TextView =  convertView.findViewById(R.id.textView)
                txtListChild.text = "A. 일종의 다이어리 어플입니다."
            } else if (itemType == 1) {
                val txtListChild : TextView =  convertView.findViewById(R.id.textView)
                txtListChild.text = "A. 한국항공대학교 소프트웨어학과"
            }


        }
        return convertView as View
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

    override fun getChildTypeCount(): Int {
        return 11
    }

    override fun getChildType(groupPosition: Int, childPosition: Int): Int {
        return groupPosition
    }

}