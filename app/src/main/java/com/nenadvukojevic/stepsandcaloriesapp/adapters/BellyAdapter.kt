package com.nenadvukojevic.stepsandcaloriesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nenadvukojevic.stepsandcaloriesapp.ApplicationStepsAndCalories
import com.nenadvukojevic.stepsandcaloriesapp.R
import com.nenadvukojevic.stepsandcaloriesapp.databinding.ItemDishLayoutBinding
import com.nenadvukojevic.stepsandcaloriesapp.foodNameToShortString
import com.nenadvukojevic.stepsandcaloriesapp.model.database.FoodModel

class BellyAdapter (val onBtnDeleteListener: OnBtnDeleteListener) : RecyclerView.Adapter<BellyAdapter.ViewHolder>()   {
    var data = listOf<FoodModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BellyAdapter.ViewHolder {
        return ViewHolder.from(parent, onBtnDeleteListener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BellyAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.click(item)


    }


    class ViewHolder constructor(val binding: ItemDishLayoutBinding, val onBtnDeleteListener: OnBtnDeleteListener)
        : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup, onBtnDeleteListener: OnBtnDeleteListener): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemDishLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, onBtnDeleteListener)
            }
        }

        fun bind(item: FoodModel) {
            binding.tvItemOverviewName.text = item.name?.foodNameToShortString()
            binding.tvItemOverviewGrams.text = ApplicationStepsAndCalories.instance.getString(R.string.format_grams, item.grams)
            binding.tvItemOverviewKcal.text = item.kcal.toString()
            binding.tvItemOverviewCarbs.text = ApplicationStepsAndCalories.instance.getString(R.string.format_grams, item.carbs)
            binding.tvItemOverviewProteins.text = ApplicationStepsAndCalories.instance.getString(R.string.format_grams, item.proteins)
            binding.tvItemOverviewFats.text = ApplicationStepsAndCalories.instance.getString(R.string.format_grams, item.fats)
        }

        fun click(item: FoodModel) {
            binding.btnDeleteItem.setOnClickListener {
                onBtnDeleteListener.onClick(item)
            }
        }


    }


    class OnBtnDeleteListener(val clickListener: (foodModel: FoodModel) -> Unit) {
        fun onClick(foodModel: FoodModel) = clickListener(foodModel)
    }

}