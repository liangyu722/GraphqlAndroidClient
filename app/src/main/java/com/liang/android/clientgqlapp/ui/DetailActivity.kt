package com.liang.android.clientgqlapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_main)
        val id = intent.getStringExtra(USERID_INTENT)
        id?.let {
            getUser(it)
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

                //Verified post and hobby is returning from graphql, cannot be bothered to write adapter.
            }
        }
    }

    private fun bindUser(user: GetuserQuery.User) {
        dets_name_tv.text = user.name
        dets_age_tv.text = user.age.toString()
        dets_profession_tv.text = user.profession
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}