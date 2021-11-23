package com.example.setelmobiletechnicalassessment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recycleView = findViewById<RecyclerView>(R.id.recycler_view)


        val service = Retrofit.Builder()
            .baseUrl("https://setel.axzae.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(UserService::class.java)

        service.getRestaurants().enqueue(object : Callback<RestaurantsResponse> {

            override fun onFailure(call: Call<RestaurantsResponse>, t: Throwable) {
                Log.e("TAG_", "An error happened!")
                t.printStackTrace()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<RestaurantsResponse>, response: Response<RestaurantsResponse>) {
                if (response.isSuccessful) {
                    recycleView.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = RestaurantsAdapter(response.body()!!.restaurants)
                    }


                    Log.e("TAG_", response.body().toString())
                }
            }
        })
    }

}


data class RestaurantsResponse(val restaurants: List<Restaurant>) {

    operator fun get(i: Int): Restaurant {
        return restaurants[i]
    }
}

data class Restaurant(val name: String, val operatingHours: String)

interface UserService {
    @GET("/homework")
    fun getRestaurants(): Call<RestaurantsResponse>
}