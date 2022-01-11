package com.edwin.github_app.view.fragments.subfragments

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.carbs.android.avatarimageview.library.AppCompatAvatarImageView
import com.edwin.github_app.R
import com.edwin.github_app.network.entities.User
import com.edwin.github_app.utils.loadWithGlide
import com.edwin.github_app.view.common.CommonListAdapter

class PeopleListAdapter : CommonListAdapter<User>(R.layout.item_user) {
    override fun onItemClicked(itemView: View, item: User) {
        // todo
    }

    override fun onBindData(viewHolder: RecyclerView.ViewHolder, user: User) {
        viewHolder.itemView.apply {
            findViewById<AppCompatAvatarImageView>(R.id.avatarView).loadWithGlide(
                user.avatar_url,
                user.login.first()
            )
            findViewById<TextView>(R.id.nameView).text = user.login
        }
    }
}