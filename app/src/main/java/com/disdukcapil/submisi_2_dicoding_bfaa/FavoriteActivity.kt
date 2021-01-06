package com.disdukcapil.submisi_2_dicoding_bfaa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.disdukcapil.submisi_2_dicoding_bfaa.adapter.FavoriteAdapter
import com.disdukcapil.submisi_2_dicoding_bfaa.databinding.ActivityFavoriteBinding
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

        if (savedInstanceState == null) {
            loadUserAsnyc()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }
    }

    private fun loadUserAsnyc() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val notes = deferredNotes.await()
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