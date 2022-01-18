package com.example.karigorit.activity

import android.app.Dialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.karigorit.GetUserResponse
import com.example.karigorit.R
import com.example.karigorit.Results
import com.example.karigorit.adapter.UserListAdapter
import com.example.karigorit.http.ApiClient
import com.example.karigorit.http.GetService
import com.example.karigorit.utils.isNetworkAvailable
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UserListAdapter.onuserCallback {

    lateinit var progressDialog: ProgressDialog
    lateinit var layout: LinearLayout
    lateinit var rv: RecyclerView
    lateinit var adapter :UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Loading ....")
        progressDialog!!.setCancelable(false)

        var btn = findViewById<Button>(R.id.button)
        var edTxt = findViewById<EditText>(R.id.editTextNumber)
        layout = findViewById<LinearLayout>(R.id.insertLayout)
        rv = findViewById<RecyclerView>(R.id.user_list)


        rv?.layoutManager = LinearLayoutManager(this)
        adapter = UserListAdapter(context = this, this)
        rv?.adapter = adapter

        layout.visibility = View.VISIBLE
        rv.visibility = View.GONE

        btn.setOnClickListener {

            if (isNetworkAvailable(this)) {
                if (edTxt.text.toString() != "0" && edTxt.text.toString() != "") {
                    getUserMenual(this, edTxt.text.toString())
                    edTxt.text.clear()
                } else {
                    Toast.makeText(this, "Must enter a number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }

    }


// api call
    fun getUserMenual(context: Context, count: String) {
        progressDialog!!.show()
        adapter?.clearAll()
        try {
            val service: GetService = ApiClient.getClient().create()
            val call: Call<GetUserResponse> = service.getUser("?results=" + count)
            call.enqueue(object : Callback<GetUserResponse?> {
                override fun onResponse(
                    call: Call<GetUserResponse?>,
                    response: Response<GetUserResponse?>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            context,
                            response.body()!!.results.size.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog!!.dismiss()

                        layout.visibility = View.GONE
                        rv.visibility = View.VISIBLE

                        val imm =
                            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(layout.windowToken, 0)

                        adapter?.addAll(response.body()!!.results)

                    }
                }

                override fun onFailure(call: Call<GetUserResponse?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    t.printStackTrace()
                }

            })
        } catch (e: Exception) {
            progressDialog!!.dismiss()
            e.printStackTrace()
        }
    }

    override fun onuserClick(userData: Results) {
//        TODO("Not yet implemented")
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_layout)
        val name = dialog.findViewById(R.id.tvName) as TextView
        val address = dialog.findViewById(R.id.tvAddress) as TextView
        val image = dialog.findViewById(R.id.view_image) as ImageView
        val phone = dialog.findViewById(R.id.tvPhone) as TextView
        val email = dialog.findViewById(R.id.tvEmail) as TextView

        Glide.with(this).load(userData.picture!!.large).diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(image)
        name.text = userData.name!!.title.toString() + " " + userData.name!!.first.toString() + " " + userData.name!!.last.toString()
        address.text = userData.location!!.street!!.number.toString() +", "+
                userData.location!!.street!!.name.toString() +", "+
                userData.location!!.city!!.toString() + ", "+
                userData.location!!.state!!.toString() + ", "+
                userData.location!!.postcode!!.toString() + ", "+
                userData.location!!.country!!.toString()
        phone.text = userData.phone.toString()
        email.text = userData.email.toString()
        val yesBtn = dialog.findViewById(R.id.btn_yes) as Button
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
        email.setOnClickListener {

            // send email
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + userData.email.toString()))
                intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject")
                intent.putExtra(Intent.EXTRA_TEXT, "your_text")
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                //TODO smth
            }
        }
        dialog.show()

    }


}
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}