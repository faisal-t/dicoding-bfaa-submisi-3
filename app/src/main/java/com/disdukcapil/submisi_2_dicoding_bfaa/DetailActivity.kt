package com.disdukcapil.submisi_2_dicoding_bfaa

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.disdukcapil.submisi_2_dicoding_bfaa.databinding.ActivityDetailBinding
import com.disdukcapil.submisi_2_dicoding_bfaa.db.DatabaseContract
import com.disdukcapil.submisi_2_dicoding_bfaa.db.UserHelper
import com.disdukcapil.submisi_2_dicoding_bfaa.entity.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailBinding
    private lateinit var  txtName : TextView
    private lateinit var  txtLocation : TextView
    private lateinit var  txtUsername : TextView
    private lateinit var  txtFollower : TextView
    private lateinit var  txtFollowing : TextView
    private lateinit var  txtRepository : TextView
    private lateinit var  txtCompany : TextView
    private lateinit var  imgDetail : ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var fabFavorite : FloatingActionButton
    private var isFavorite = false
    private lateinit var userhelper: UserHelper
    private var user : User ?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_detail)

        userhelper = UserHelper.getInstance(applicationContext)
        userhelper.open()


        val sectionPagerAdapter = SectionPagerAdapter(this,supportFragmentManager)
        progressBar = findViewById(R.id.progressBar)
        viewPager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tabs)


        txtName = findViewById(R.id.txt_name)
        txtLocation = findViewById(R.id.txt_location)
        txtUsername = findViewById(R.id.txt_username)
        txtFollower = findViewById(R.id.txt_followers)
        txtFollowing = findViewById(R.id.txt_following)
        txtRepository = findViewById(R.id.txt_repositories)
        txtCompany = findViewById(R.id.txt_company)
        imgDetail = findViewById(R.id.avatars)
        fabFavorite = findViewById(R.id.fab_favorite)




        val dataModel : DataModel ?= intent.getParcelableExtra("data")
        sectionPagerAdapter.username = dataModel!!.username!!
        viewPager.adapter = sectionPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        supportActionBar?.elevation = 0f
        getDetail(dataModel!!.username!!)

        print(dataModel.username)


        
        val favoriteuser = userhelper.queryById(dataModel.username!!)
        Log.d("kkk", "Total Row : ${favoriteuser.count}");
        if (favoriteuser.count > 0){
            isFavorite = true
        }

        statusFavorite(isFavorite)
        fabFavorite.setOnClickListener {
            isFavorite = !isFavorite

            val dataUsername = dataModel.username
            val dataAvatar = dataModel.avatar

            if(isFavorite == true){
                //kode insert ke database



                userhelper.deleteById(dataUsername.toString())

                val values = ContentValues()
                values.put(DatabaseContract.UserColumns.USERNAME,dataUsername)
                values.put(DatabaseContract.UserColumns.AVATAR_URL,dataAvatar)

                //akhir kode insert
                userhelper.insert(values)
                Log.d("insert",userhelper.toString())
                Toast.makeText(this,"berhasil Tambah favorite",Toast.LENGTH_SHORT).show()
            }
            else{
                userhelper.deleteById(dataUsername.toString())

                Log.d("username",userhelper.toString())
                Toast.makeText(this,"berhasil Hapus favorite",Toast.LENGTH_SHORT).show()
            }

            statusFavorite(isFavorite)
        }





    }

    private fun statusFavorite(state: Boolean){
        if (state){
            fabFavorite.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
        }else{
            fabFavorite.setImageResource(R.drawable.ic_baseline_person_add_24)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    fun getDetail(username : String){
        showLoading(true)
        val asyncClient = AsyncHttpClient()

        asyncClient.addHeader("Authorization", "7566b6bf7f8448fcc6c2b2c4977a3c3c8ccb2f40")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get("https://api.github.com/users/$username", object : TextHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                showLoading(false)
                val result = responseString
                try {
                    val responseObject = JSONObject(result)
                    val name = responseObject.getString("name")
                    val location = responseObject.getString("location")
                    val username = username
                    val follower = responseObject.getString("followers")
                    val following = responseObject.getString("following")
                    val repository = responseObject.getString("public_repos")
                    val company = responseObject.getString("company")
                    val avatar = responseObject.getString("avatar_url")

                    txtName.text = name
                    txtLocation.text = location
                    txtUsername.text = username
                    txtFollower.text = follower
                    txtFollowing.text = following
                    txtRepository.text = repository
                    txtCompany.text = company
                    Glide.with(this@DetailActivity.applicationContext)
                            .load(avatar)
                            .into(imgDetail)


                }catch (e : Exception){
                    e.printStackTrace()
                }




            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                showLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : eror"
                }
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }


}