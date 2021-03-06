package com.liang.android.clientgqlapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.toDeferred
import com.liang.android.clientgqlapp.DeleteUserMutation
import com.liang.android.clientgqlapp.GetuserQuery
import com.liang.android.clientgqlapp.R
import com.liang.android.clientgqlapp.UpdateUserMutation
import com.liang.android.clientgqlapp.util.ApolloClient
import kotlinx.android.synthetic.main.detail_main.*
import kotlinx.coroutines.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val USERID_INTENT = "USERID_INTENT"
    }

    private lateinit var job: Job
    private var updateJob: Job? = null
    private var deleteJob: Job? = null
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

        dets_update_user_btn.setOnClickListener {
            val name = dets_name_tv.text.toString()
            val age = dets_age_tv.text.toString().toInt()
            val profession = dets_profession_tv.text.toString()
            updateUser(id, name, age, profession)
        }

        dets_deleteuser_btn.setOnClickListener {
            deleteUser(id)
        }
    }

    private fun updateUser(id: String, name: String, age: Int, profession: String) {
        updateJob = CoroutineScope(Dispatchers.Main).launch {
            val userResponse = withContext(Dispatchers.IO) {
                return@withContext ApolloClient
                    .instance
                    .mutate(UpdateUserMutation(id, name, age, Input.fromNullable(profession)))
                    .toDeferred()
                    .await()
            }
            finish()
        }
    }

    private fun deleteUser(id: String) {
        updateJob = CoroutineScope(Dispatchers.Main).launch {
            val userResponse = withContext(Dispatchers.IO) {
                return@withContext ApolloClient
                    .instance
                    .mutate(DeleteUserMutation(id))
                    .toDeferred()
                    .await()
            }
            finish()
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
        updateJob?.cancel()
        deleteJob?.cancel()
        super.onDestroy()
    }
}