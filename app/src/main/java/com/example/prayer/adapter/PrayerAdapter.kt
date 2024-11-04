package com.example.prayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prayer.R
import com.example.prayer.model.Day


class PrayerAdapter(
    private var daysList: List<Day>,
    private var listener : OnClickDayListener
) :
    RecyclerView.Adapter<PrayerAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.num.text=daysList[position].date
        holder.day.text=daysList[position].day_en.substring(0,3)
        if (daysList[position].selected){
            holder.itemView.setBackgroundResource(R.drawable.selected_background)
            daysList[position].selected = false
        }
        else{
            holder.itemView.setBackgroundResource(R.drawable.background)
        }
        if (daysList[position].today){
            holder.day.text="Today"
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(
            R.layout.item_calender
            , parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener{

        var num : TextView = view.findViewById(R.id.date)
        var day: TextView = view.findViewById(R.id.day)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            when(p0){

                itemView->{
                    listener.onDayClick(daysList[adapterPosition])
                }

            }
        }

    }
    interface OnClickDayListener
    {
        fun onDayClick(item: Day)

    }

    override fun getItemCount(): Int {
        return daysList.size
    }

    fun setData(list: List<Day>) {
        daysList = emptyList()
        daysList = list
        notifyDataSetChanged()
    }

}