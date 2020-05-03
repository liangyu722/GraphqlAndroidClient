package com.liang.android.clientgqlapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.coroutines.toDeferred
import com.liang.android.clientgqlapp.GetuserQuery
import com.liang.android.clientgqlapp.R
import com.liang.android.clientgqlapp.util.ApolloClient
import kotlinx.android.synthetic.main.detail_main.*
import kotlinx.coroutines.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val USERID_INTENT = "USERID_INTENT"
    }

    private lateinit var job: Job
    private val postAdapter: PostAdapter by lazy {
        PostAdapter()
    }
    private val hobbyAdapter: HobbyAdapter by lazy {
        HobbyAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_main)
        val id = intent.getStringExtra(USERID_INTENT)
        id?.let {
            getUser(it)
        }

        dets_post_rv.adapter = postAdapter
        dets_post_rv.layoutManager = LinearLayoutManager(this).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }

        dets_hobby_rv.adapter = hobbyAdapter
        dets_hobby_rv.layoutManager = LinearLayoutManager(this).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }

        dets_hobby_btn.setOnClickListener {
            dets_post_rv.visibility = View.GONE
            dets_hobby_rv.visibility = View.VISIBLE
        }

        dets_post_btn.setOnClickListener {
            dets_post_rv.visibility = View.VISIBLE
            dets_hobby_rv.visibility = View.GONE
        }
    }

    private fun getUser(id: String) {
        job = CoroutineScope(Dispatchers.Main).launch {
            val userResponse = withContext(Dispatchers.IO) {
                return@withContext ApolloClient.instance.query(GetuserQuery(id)).toDeferred()
                    .await()
            }
            userResponse.data?.user?.let {
                bindUser(it)
                it.posts?.let { posts ->
                    postAdapter.setList(posts.filterNotNull())
                }

                it.hobbies?.let { hobbies ->
                    hobbyAdapter.setList(hobbies.filterNotNull())
                }
            }
        }
    }

    private fun bindUser(user: GetuserQuery.User) {
        dets_name_tv.setText(user.name)
        dets_age_tv.setText(user.age.toString())
        dets_profession_tv.setText(user.profession)
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}