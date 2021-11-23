package com.example.setelmobiletechnicalassessment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar


class RestaurantsAdapter(private val response: List<Restaurant>) :
    RecyclerView.Adapter<RestaurantViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        //CardView inflated as RecyclerView list item
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return RestaurantViewHolder(v)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = response[position]
        holder.bindView(restaurant)
        holder.cardView.setOnClickListener { v ->
            //set back to itemView for students
            Snackbar.make(
                v,
                "Book " + restaurant.name + " is clicked",
                Snackbar.LENGTH_LONG
            ).setAction("Action", null).show()
        }
    }

    override fun getItemCount(): Int {
        return response.size
    }

}

class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cardView: View = itemView
    private val nameTV: TextView = itemView.findViewById(R.id.nameTV)
    private val operatingHrsTV: TextView = itemView.findViewById(R.id.operatingTV)

    @SuppressLint("SetTextI18n")
    fun bindView(restaurant: Restaurant) {
        nameTV.text = restaurant.name
        // separate the days that have different opening and close time
        val sameOprtHrsArray = restaurant.operatingHours.split(" / ").toTypedArray()

        val dayArray = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val oprtHrs = OperatingHours()

        // to separate out the day and time
        for (i in sameOprtHrsArray) {
            val oprtDaysTime = OperatingDaysTime("", "")
            for (j in i.split(" ")) {

                if (j.length >= 3 && dayArray.contains(j.substring(0, 3))) {
                    oprtDaysTime.day += j
                } else {
                    oprtDaysTime.time += "$j "
                }
            }
            for (k in oprtDaysTime.day.split(",")) {
                val a = k.split('-')
                if (a.size == 2) {
                    var n = dayArray.indexOf(a[0])
                    while (n <= dayArray.indexOf(a[1])) {
                        arrangeTime(n, oprtHrs, oprtDaysTime)
                        n += 1

                    }
                } else {
                    arrangeTime(dayArray.indexOf(a[0]), oprtHrs, oprtDaysTime)
                }
            }
        }
        println(oprtHrs.toString())

        operatingHrsTV.text =
            "${oprtHrs.Mon} \n${oprtHrs.Tue} \n${oprtHrs.Wed} \n${oprtHrs.Thu} \n${oprtHrs.Fri} \n${oprtHrs.Sat} \n${oprtHrs.Sun} \n"
    }

    private fun arrangeTime(
        n: Int,
        operatingHours: OperatingHours,
        oprtDaysTime: OperatingDaysTime
    ) {
        when (n + 1) {
            1 -> operatingHours.Mon = "Mon: " + oprtDaysTime.time
            2 -> operatingHours.Tue = "Tue: " + oprtDaysTime.time
            3 -> operatingHours.Wed = "Wed: " + oprtDaysTime.time
            4 -> operatingHours.Thu = "Thu: " + oprtDaysTime.time
            5 -> operatingHours.Fri = "Fri: " + oprtDaysTime.time
            6 -> operatingHours.Sat = "Sat: " + oprtDaysTime.time
            7 -> operatingHours.Sun = "Sun: " + oprtDaysTime.time
        }
    }
}

data class OperatingDaysTime(var day: String, var time: String)
data class OperatingHours(
    var Mon: String = "Closed",
    var Tue: String = "Closed",
    var Wed: String = "Closed",
    var Thu: String = "Closed",
    var Fri: String = "Closed",
    var Sat: String = "Closed",
    var Sun: String = "Closed"
)