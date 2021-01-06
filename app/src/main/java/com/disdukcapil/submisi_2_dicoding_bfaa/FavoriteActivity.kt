package com.disdukcapil.submisi_2_dicoding_bfaa

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.disdukcapil.submisi_2_dicoding_bfaa.adapter.FavoriteAdapter
import com.disdukcapil.submisi_2_dicoding_bfaa.databinding.ActivityFavoriteBinding
import com.disdukcapil.submisi_2_dicoding_bfaa.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.disdukcapil.submisi_2_dicoding_bfaa.db.UserHelper
import com.disdukcapil.submisi_2_dicoding_bfaa.entity.User
import com.disdukcapil.submisi_2_dicoding_bfaa.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var userHelper: UserHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        binding.rcFavorite.layoutManager = LinearLayoutManager(this)
        binding.rcFavorite.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding.rcFavorite.adapter = adapter

        Log.d("sqlite", adapter.toString())

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUserAsnyc()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)


        if (savedInstanceState == null) {
            loadUserAsnyc()
        } else {
            savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)?.also { adapter.listFavorite = it }
        }

    }

    private fun loadUserAsnyc() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                // CONTENT_URI = content://com.dicoding.picodiploma.mynotesapp/note
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val notes = deferredNotes.await()
            binding.progressBar.visibility = View.INVISIBLE
            if (notes.size > 0) {
                adapter.listFavorite = notes
            } else {
                adapter.listFavorite = ArrayList()
                Toast.makeText(this@FavoriteActivity,"Tidak ada user favorite",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

}