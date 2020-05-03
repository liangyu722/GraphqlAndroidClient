package com.liang.android.clientgqlapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.toDeferred
import com.liang.android.clientgqlapp.R
import com.liang.android.clientgqlapp.*
import com.liang.android.clientgqlapp.util.ApolloClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_user_activity.*
import kotlinx.coroutines.*

class AddUserActivity : AppCompatActivity() {

    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_user_activity)
        save_user_btn.setOnClickListener {
            addUser()
        }
    }

    private fun addUser() {
        val name = name_user_et.text.toString()
        val age = age_user_et.text.toString()
        val profession = profession_user_et.text.toString()
        job = CoroutineScope(Dispatchers.Main).launch {

            val newUser = ApolloClient
                .instance
                .mutate(AdduserMutation(name, Integer.parseInt(age), Input.fromNullable(profession)))
                .toDeferred()
                .await()

            val id = newUser.data?.createUser?.id
            id?.let {
                val comment = post_comment_et.text.toString()
                if (comment.isNotBlank()) {
                    ApolloClient.instance.mutate(AddPostMutation(comment, id)).toDeferred().await()
                }
                val title = hobby_title_et.text.toString()
                val description = hobby_description_et.text.toString()
                if (title.isNotBlank()) {
                    ApolloClient.instance.mutate(AddHobbyMutation(title, Input.fromNullable(description), id)).toDeferred().await()
                }
            } ?: run {
                Toast.makeText(applicationContext, "No user created", Toast.LENGTH_LONG).show()
            }
            finish()
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}