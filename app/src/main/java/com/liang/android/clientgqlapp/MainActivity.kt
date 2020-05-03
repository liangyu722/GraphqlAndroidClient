package com.liang.android.clientgqlapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.coroutines.toDeferred
import com.liang.android.clientgqlapp.ui.AddUserActivity
import com.liang.android.clientgqlapp.ui.DetailActivity
import com.liang.android.clientgqlapp.ui.UserRecyclerViewAdapter
import com.liang.android.clientgqlapp.util.ApolloClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var job: Job
    private val adapter = UserRecyclerViewAdapter {
        onClick(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_users_Id.adapter = adapter
        rv_users_Id.layoutManager = LinearLayoutManager(this)
        fab.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
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

    private fun onClick(user: UsersQuery.User) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.USERID_INTENT, user.id)
        startActivity(intent)
    }
}
