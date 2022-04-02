package com.nenadvukojevic.stepsandcaloriesapp.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nenadvukojevic.stepsandcaloriesapp.R
import com.nenadvukojevic.stepsandcaloriesapp.databinding.ItemBinding
import com.nenadvukojevic.stepsandcaloriesapp.model.entities.Food
import com.nenadvukojevic.stepsandcaloriesapp.foodNameToShortString
import com.nenadvukojevic.stepsandcaloriesapp.toKcalString
import com.nenadvukojevic.stepsandcaloriesapp.view.activities.MainActivity
import com.nenadvukojevic.stepsandcaloriesapp.view.fragments.AddFoodFragment
import com.nenadvukojevic.stepsandcaloriesapp.view.fragments.CaloriesFragment
import com.nenadvukojevic.stepsandcaloriesapp.view.fragments.CaloriesFragmentDirections
import kotlin.coroutines.coroutineContext

class FoodAdapter(private val fragment: Fragment, val onClickListener: OnClickListener) :


    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    var data = listOf<Food>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.bind(item)
        // Load the dish image in the ImageView.
        Glide.with(fragment)
            .load(item.image)
            .skipMemoryCache(true)
            .format(DecodeFormat.PREFER_ARGB_8888)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontTransform()
            .priority(Priority.IMMEDIATE)
            .placeholder(R.drawable.avocado_icon_dark_03)
            .encodeFormat(Bitmap.CompressFormat.PNG)
            .into(holder.binding.ivDishImage)



        holder.itemView.setOnClickListener {
         onClickListener.onClick(item)



        }
        holder.bind(item)






    }



    override fun getItemCount(): Int { //getItemCount returns the total number of items to show in the recyclerview
        return data.size
    }


    class ViewHolder private constructor(val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
//                val view = layoutInflater
//                    .inflate(R.layout.list_item_search, parent, false)
                val binding = ItemBinding.inflate(layoutInflater, parent, false)


                return ViewHolder(binding)

            }
        }

        fun bind(item: Food) {




            binding.text1.text = item.label.foodNameToShortString()



        }

    }

    class OnClickListener(val clickListener: (food: Food) -> Unit) {
        fun onClick(food: Food) = clickListener(food)
    }


}