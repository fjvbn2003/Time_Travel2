package tabbedactivitytest.mobilesw.kau.time_travel2

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.vipulasri.timelineview.TimelineView
import com.squareup.picasso.Picasso

/**
 * Created by USER on 2017-12-09.
 */

class FriendHistoryRecyclerAdapter(private var context: Context, private val friendHistoryList: List<FriendHistory>) : RecyclerView.Adapter<FriendHistoryRecyclerAdapter.mViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.post_friend_row, parent, false)

        val returnView = mViewHolder(view, context, viewType)
        return returnView
    }

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        val history = friendHistoryList[position]
        var imageUrl: String? = null
        var profile_imageUrl: String? =null
        holder.profile_email.text = history.userID
        holder.historyTitle.text = history.historyTitle
        holder.desc.text = history.desc
        holder.location.text = history.location
        holder.timeStamp.text = history.timeStamp
        imageUrl = history.image
        Picasso.with(context)
                .load(imageUrl)
                .into(holder.image)

        profile_imageUrl = history.profile_image
        Picasso.with(context)
                .load(profile_imageUrl)
                .into(holder.profile_image)
    }

    override fun getItemCount(): Int {
        return friendHistoryList.size
    }

    inner class mViewHolder(view: View, ctx: Context, viewType: Int) : RecyclerView.ViewHolder(view) {
        var historyTitle: TextView
        var desc: TextView
        var timeStamp: TextView
        var location: TextView
        var image: ImageView
        var profile_image: ImageView
        var profile_email: TextView
        internal var userID: String? = null

        init {
            context = ctx
            profile_image = view.findViewById<View>(R.id.friend_profile_image) as ImageView
            profile_email = view.findViewById<View>(R.id.friend_email_text) as TextView
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
