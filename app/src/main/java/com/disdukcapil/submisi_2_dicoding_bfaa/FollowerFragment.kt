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


/**
 * A simple [Fragment] subclass.
 * Use the [FollowerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowerFragment : Fragment() {
    val list = ArrayList<DataModel>()
    lateinit var recyclerView : RecyclerView
    lateinit var progressBar: ProgressBar




    companion object {
        private val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(username: String,) : FollowerFragment{
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME,username)
            fragment.arguments = bundle
            return fragment
        }
    }

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

        getFollower(username!!)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }


    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun getFollower(username : String){
        showLoading(true)
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "7566b6bf7f8448fcc6c2b2c4977a3c3c8ccb2f40")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get("https://api.github.com/users/$username/followers", object : TextHttpResponseHandler() {
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

                    recyclerView.adapter = DataAdapter(list)

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