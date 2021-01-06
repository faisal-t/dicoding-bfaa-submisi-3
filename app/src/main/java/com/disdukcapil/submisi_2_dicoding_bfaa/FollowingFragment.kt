package com.disdukcapil.submisi_2_dicoding_bfaa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray


class FollowingFragment : Fragment() {

    companion object {
        private val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(username: String,) : FollowingFragment{
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME,username)
            fragment.arguments = bundle
            return fragment
        }
    }

    val list = ArrayList<DataModel>()
    lateinit var recyclerView : RecyclerView
    lateinit var progressBar: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)

        recyclerView = view.findViewById(R.id.rcView)
        progressBar = view.findViewById(R.id.progressBar)


        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        getFollowing(username!!)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }


    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }


    private fun getFollowing(username : String){
        showLoading(true)
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "7566b6bf7f8448fcc6c2b2c4977a3c3c8ccb2f40")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get("https://api.github.com/users/$username/following", object : TextHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                showLoading(false)
                val result = responseString
                try {

                    val items = JSONArray(result)

                    for (i in 0 until items.length()){
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        val dataModel = DataModel()
                        dataModel.username = username
                        dataModel.avatar = avatar

                        list.add(dataModel)


                    }

                    val adapter = DataAdapter(list)
                    recyclerView.adapter = adapter

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
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })

    }
}