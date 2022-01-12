package com.edwin.github_app.view

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import cn.carbs.android.avatarimageview.library.AppCompatAvatarImageView
import com.edwin.annotations.Required
import com.edwin.experimental.coroutines.awaitOrError
import com.edwin.experimental.coroutines.launchUI
import com.edwin.github_app.R
import com.edwin.github_app.network.entities.Repository
import com.edwin.github_app.network.services.ActivityService
import com.edwin.github_app.network.services.RepositoryService
import com.edwin.github_app.utils.*
import com.edwin.github_app.view.common.BaseDetailSwipeFinishableActivity
import com.edwin.github_app.view.widget.DetailItemView
import com.google.android.material.appbar.CollapsingToolbarLayout
import retrofit2.Response
import rx.Subscriber

class RepoDetailActivity : BaseDetailSwipeFinishableActivity() {

    @Required
    lateinit var repository: Repository
    private lateinit var toolBar: Toolbar
    private lateinit var avatarView: AppCompatAvatarImageView
    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var descriptionView: TextView
    private lateinit var bodyView: TextView
    private lateinit var detailContainer: LinearLayout
    private lateinit var stars: DetailItemView
    private lateinit var watches: DetailItemView

    private lateinit var loadingView: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_details)

        toolBar = findViewById(R.id.toolBar)
        avatarView = findViewById(R.id.avatarView)
        collapsingToolbar = findViewById(R.id.collapsingToolbar)
        descriptionView = findViewById(R.id.descriptionView)
        bodyView = findViewById(R.id.bodyView)
        detailContainer = findViewById(R.id.detailContainer)
        stars = findViewById(R.id.stars)
        watches = findViewById(R.id.watches)

        toolBar = findViewById(R.id.toolBar)

        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initDetails()
        reloadDetails()
    }

    private fun initDetails() {
        avatarView.loadWithGlide(repository.owner.avatar_url, repository.owner.login.first())
        collapsingToolbar.title = repository.name

        descriptionView.markdownText = getString(
            R.string.repo_description_template,
            repository.owner.login,
            repository.owner.html_url,
            repository.name,
            repository.html_url,
            repository.owner.login,
            repository.owner.html_url,
            githubTimeToDate(repository.created_at).view()
        )

        bodyView.text = repository.description

        detailContainer.alpha = 0f

        stars.checkEvent = { isChecked ->
            if (isChecked) {
                ActivityService.unstar(repository.owner.login, repository.name)
                    .map { false }
            } else {
                ActivityService.star(repository.owner.login, repository.name)
                    .map { true }
            }.doOnNext { reloadDetails(true) }
        }

        watches.checkEvent = { isChecked ->
            if (isChecked) {
                ActivityService.unwatch(repository.owner.login, repository.name)
                    .map { false }
            } else {
                ActivityService.watch(repository.owner.login, repository.name)
                    .map { true }
            }.doOnNext { reloadDetails(true) }
        }

        ActivityService.isStarred(repository.owner.login, repository.name)
            .onErrorReturn {
                if (it is retrofit2.HttpException) {
                    it.response() as Response<Any>
                } else {
                    throw it
                }
            }
            .subscribeIgnoreError {
                stars.isChecked = it.isSuccessful
            }

        ActivityService.isWatched(repository.owner.login, repository.name)
                .subscribeIgnoreError {
                    watches.isChecked = it.subscribed
                }

//        launchUI {
//            try {
//                watches.isChecked = ActivityService.isWatchedDeferred(repository.owner.login, repository.name).await().subscribed
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }

//        launchUI {
//            val (subscriptionResponse, error) = ActivityService.isWatchedDeferred(repository.owner.login, repository.name).awaitOrError()
//            error?.printStackTrace() ?: run {
//                watches.isChecked = subscriptionResponse!!.subscribed
//            }
//        }

        launchUI {
            val (subscriptionResponse, error) = ActivityService.isWatchedDeferred(
                repository.owner.login,
                repository.name
            ).awaitOrError()
            error?.printStackTrace() ?: run {
                watches.isChecked = subscriptionResponse.subscribed
            }
        }

    }

    private fun reloadDetails(forceNetwork: Boolean = false) {
        RepositoryService.getRepository(repository.owner.login, repository.name, forceNetwork)
            .subscribe(object : Subscriber<Repository>() {
                override fun onStart() {
                    super.onStart()
                    loadingView.animate().alpha(1f).start()
                }

                override fun onNext(t: Repository) {
                    repository = t

                    findViewById<DetailItemView>(R.id.owner).content = repository.owner.login
                    stars.content = repository.stargazers_count.toString()
                    watches.content = repository.subscribers_count.toString()
                    findViewById<DetailItemView>(R.id.forks).content =
                        repository.forks_count.toString()
                    //issues.content = repository.open_issues_count.toString()

                    loadingView.animate().alpha(0f).start()
                    detailContainer.animate().alpha(1f).start()
                }

                override fun onCompleted() = Unit

                override fun onError(e: Throwable) {
                    loadingView.animate().alpha(0f).start()
                    e.printStackTrace()
                }

            })

//        val watcher = apolloClient.query(RepositoryIssueCountQuery(repository.name, repository.owner.login)).watcher()
//        RxApollo.from(watcher)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    it.data()?.let{
//                        issues.content = "open: ${it.repository()?.openIssues()?.totalCount()?: 0} closed: ${it.repository()?.closedIssues()?.totalCount()?: 0}"
//                    }
//                }


//        GraphQLService.repositoryIssueCount(repository.owner.login, repository.name)
//                .subscribeIgnoreError {
//                    data ->
//                    issues.content = "open: ${data.repository()?.openIssues()?.totalCount()?: 0} closed: ${data.repository()?.closedIssues()?.totalCount()?: 0}"
//                }

//        launchUI {
//            val (data, error) = GraphQLService.repositoryIssueCount3(
//                repository.owner.login,
//                repository.name
//            ).awaitOrError()
//            error?.printStackTrace() ?: kotlin.run {
//                findViewById<DetailItemView>(R.id.issues).content = "open: ${
//                    data.repository()?.openIssues()?.totalCount() ?: 0
//                } closed: ${data.repository()?.closedIssues()?.totalCount() ?: 0}"
//            }
//        }

//        GraphQLService.repositoryIssueCount2(repository.owner.login, repository.name)
//                        .enqueue(object : ApolloCall.Callback<RepositoryIssueCountQuery.Data>(){
//                    override fun onFailure(e: ApolloException) {
//                        e.printStackTrace()
//                    }
//
//                    override fun onResponse(response: com.apollographql.apollo.api.Response<Data>) {
//                        runOnUiThread {
//                            response.data()?.let{
//                                issues.content = "open: ${it.repository()?.openIssues()?.totalCount()?: 0} closed: ${it.repository()?.closedIssues()?.totalCount()?: 0}"
//                            }
//                        }
//                    }
//
//                })

//        apolloClient.query(RepositoryIssueCountQuery(repository.name, repository.owner.login))
//                .enqueue(object : ApolloCall.Callback<RepositoryIssueCountQuery.Data>(){
//                    override fun onFailure(e: ApolloException) {
//                        e.printStackTrace()
//                    }
//
//                    override fun onResponse(response: com.apollographql.apollo.api.Response<Data>) {
//                        runOnUiThread {
//                            response.data()?.let{
//                                issues.content = "open: ${it.repository()?.openIssues()?.totalCount()?: 0} closed: ${it.repository()?.closedIssues()?.totalCount()?: 0}"
//                            }
//                        }
//                    }
//
//                })

    }

}