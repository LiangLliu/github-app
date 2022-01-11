package com.edwin.github_app.view.fragments.subfragments

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.carbs.android.avatarimageview.library.AppCompatAvatarImageView

import com.edwin.github_app.view.common.CommonListAdapter

import com.edwin.github_app.R
import com.edwin.github_app.network.entities.Repository
import com.edwin.github_app.utils.loadWithGlide
import com.edwin.github_app.utils.toKilo

class RepoListAdapter : CommonListAdapter<Repository>(R.layout.item_repo) {
    override fun onBindData(viewHolder: RecyclerView.ViewHolder, repository: Repository) {
        viewHolder.itemView.apply {
            findViewById<AppCompatAvatarImageView>(R.id.avatarView).loadWithGlide(
                repository.owner.avatar_url,
                repository.owner.login.first()
            )

            findViewById<TextView>(R.id.repoNameView).text = repository.name
            findViewById<TextView>(R.id.descriptionView).text = repository.description
            findViewById<TextView>(R.id.langView).text = repository.language ?: "Unknown"
            findViewById<TextView>(R.id.starView).text = repository.stargazers_count.toKilo()
            findViewById<TextView>(R.id.forkView).text = repository.forks_count.toKilo()
        }
    }

    override fun onItemClicked(itemView: View, item: Repository) {
//        itemView.startRepoDetailActivity(item)
    }

}