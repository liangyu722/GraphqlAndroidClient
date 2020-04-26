package com.liang.android.clientgqlapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.cache.CacheHeaders
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.liang.android.clientgqlapp.ui.UserRecyclerViewAdapter
import com.liang.android.clientgqlapp.util.ApolloClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var job: Job
    private val adapter = UserRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_users_Id.adapter = adapter
        rv_users_Id.layoutManager = LinearLayoutManager(this)
        getUsers()
    }

    private fun getUsers() {
        job = CoroutineScope(Dispatchers.Main).launch {
            val usersResponse = withContext(Dispatchers.IO) {
                return@withContext ApolloClient.instance.query(UsersQuery()).toDeferred().await()
            }
            val users = usersResponse.data?.users?.filterNotNull() ?: emptyList()
            users.let {
                adapter.setUsers(it)
            }
            progressBarId.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}
