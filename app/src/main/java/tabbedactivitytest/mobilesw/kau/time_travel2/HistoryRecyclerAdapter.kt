package tabbedactivitytest.mobilesw.kau.time_travel2

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

/**
 * Created by USER on 2017-12-09.
 */

class HistoryRecyclerAdapter(private var context: Context, private val historyList: List<History>) : RecyclerView.Adapter<HistoryRecyclerAdapter.mViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.post_row, parent, false)

        val returnView = mViewHolder(view, context)
        return returnView
    }

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        val history = historyList[position]
        var imageUrl: String? = null
        holder.historyTitle.text = history.historyTitle
        holder.desc.text = history.desc
        holder.location.text = history.location
        holder.timeStamp.text = history.timeStamp
        holder.timeStamp.text = "17/12/09"
        imageUrl = history.image
        Picasso.with(context)
                .load(imageUrl)
                .into(holder.image)

    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    inner class mViewHolder(view: View, ctx: Context) : RecyclerView.ViewHolder(view) {
        var historyTitle: TextView
        var desc: TextView
        var timeStamp: TextView
        var location: TextView
        var image: ImageView
        internal var userID: String? = null

        init {
            context = ctx
            historyTitle = view.findViewById<View>(R.id.postTitleList) as TextView
            desc = view.findViewById<View>(R.id.postTextList) as TextView
            timeStamp = view.findViewById<View>(R.id.postTimeList) as TextView
            location = view.findViewById<View>(R.id.postLocationList) as TextView
            image = view.findViewById<View>(R.id.postImageView) as ImageView
            userID = "test"

            view.setOnClickListener {

            }

        }
    }
}
