package com.edwin.github_app.view.fragments.subfragments

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.edwin.github_app.network.entities.User
import com.edwin.github_app.view.common.CommonListAdapter
import kotlinx.android.synthetic.main.item_user.view.*

class PeopleListAdapter : CommonListAdapter<User>(layout.item_user) {
    override fun onItemClicked(itemView: View, item: User) {
        // todo
    }

    override fun onBindData(viewHolder: RecyclerView.ViewHolder, user: User) {
        viewHolder.itemView.apply {
            avatarView.loadWithGlide(user.avatar_url, user.login.first())
            nameView.text = user.login
        }
    }
}