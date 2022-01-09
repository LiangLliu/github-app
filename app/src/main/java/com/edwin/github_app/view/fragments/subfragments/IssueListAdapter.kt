package com.edwin.github_app.view.fragments.subfragments

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.edwin.github_app.network.entities.Issue
import com.edwin.github_app.R
import com.edwin.github_app.utils.githubTimeToDate
import com.edwin.github_app.utils.view
import com.edwin.github_app.view.common.CommonListAdapter

/**
 * Created by benny on 7/9/17.
 */
open class IssueListAdapter : CommonListAdapter<Issue>(R.layout.item_issue) {
    override fun onItemClicked(itemView: View, issue: Issue) {
        // todo
    }

    override fun onBindData(viewHolder: RecyclerView.ViewHolder, issue: Issue) {
        viewHolder.itemView.apply {
            iconView.imageResource = if (issue.state == "open") R.drawable.ic_issue_open else R.drawable.ic_issue_closed
            titleView.text = issue.title
            timeView.text = githubTimeToDate(issue.created_at).view()
            bodyView.htmlText = issue.body_html
            commentCount.text = issue.comments.toString()
        }
    }
}