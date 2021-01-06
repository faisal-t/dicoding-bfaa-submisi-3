package com.disdukcapil.submisi_2_dicoding_bfaa


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

import android.widget.ProgressBar

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.disdukcapil.submisi_2_dicoding_bfaa.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<DataModel>()
    lateinit var recyclerView : RecyclerView
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.rcView)
        val searchView : SearchView = findViewById(R.id.user_search)



        binding.rcView.setHasFixedSize(true)
        list.addAll(getlistData())

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = DataAdapter(list)
        recyclerView.adapter = adapter


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
               if (query.isEmpty()){
                   return true
               }else{
                   showLoading(true)
                   list.clear()
                   getDataGitSearch(query)
               }
                showLoading(true)
                return true
            }

            override fun onQueryTextChange(p0: String): Boolean {
                return false
            }

        })


    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun getDataGitSearch(query: String) {

        showLoading(true)
        val asyncClient = AsyncHttpClient()

        asyncClient.addHeader("Authorization", "7566b6bf7f8448fcc6c2b2c4977a3c3c8ccb2f40")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get("https://api.github.com/search/users?q=$query", object : TextHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseString: String?
            ) {
                showLoading(false)
                val result = responseString
                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()){
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        val dataModel = DataModel()
                        dataModel.username = username
                        dataModel.avatar = avatar

                        list.add(dataModel)
                        recyclerView.adapter?.notifyDataSetChanged()

                    }

                }catch (e : Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseString: String,
                throwable: Throwable?
            ) {

                showLoading(state = false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : eror"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getlistData(): ArrayList<DataModel> {
        val dataName = resources.getStringArray(R.array.name)
        val dataUsername = resources.getStringArray(R.array.username)
        val dataLocation = resources.getStringArray(R.array.location)
        val dataFolowing = resources.getStringArray(R.array.following)
        val dataFolower = resources.getStringArray(R.array.followers)
        val dataCompany = resources.getStringArray(R.array.company)
        val dataRepository = resources.getStringArray(R.array.repository)
        val dataPhoto = resources.getStringArray(R.array.avatar)

        val listDataModel = ArrayList<DataModel>()

        for (position in dataName.indices) {
            val dataModel = DataModel(
                dataUsername[position],
                dataRepository[position],
                dataCompany[position],
                dataName[position],
                dataLocation[position],
                dataFolowing[position],
                dataFolower[position],
                dataPhoto[position]
            )
            listDataModel.add(dataModel)
        }
        return listDataModel
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_form,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_favorite -> startActivity(Intent(this,FavoriteActivity::class.java))

            R.id.menu_setting -> startActivity(Intent(this,NotificationActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }






}