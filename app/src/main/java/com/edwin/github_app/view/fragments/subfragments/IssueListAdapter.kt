package com.edwin.github_app.view.fragments.subfragments

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edwin.github_app.network.entities.Issue
import com.edwin.github_app.R
import com.edwin.github_app.utils.githubTimeToDate
import com.edwin.github_app.utils.htmlText
import com.edwin.github_app.utils.view
import com.edwin.github_app.view.common.CommonListAdapter
import org.jetbrains.anko.imageResource

/**
 * Created by benny on 7/9/17.
 */
open class IssueListAdapter : CommonListAdapter<Issue>(R.layout.item_issue) {
    override fun onItemClicked(itemView: View, issue: Issue) {
        // todo
    }

    override fun onBindData(viewHolder: RecyclerView.ViewHolder, issue: Issue) {
        viewHolder.itemView.apply {
            findViewById<ImageView>(R.id.iconView).imageResource =
                if (issue.state == "open") R.drawable.ic_issue_open else R.drawable.ic_issue_closed

            findViewById<TextView>(R.id.titleView).text = issue.title
            findViewById<TextView>(R.id.timeView).text = githubTimeToDate(issue.created_at).view()
            findViewById<TextView>(R.id.bodyView).htmlText = issue.body_html
            findViewById<TextView>(R.id.commentCount).text = issue.comments.toString()
        }
    }
}