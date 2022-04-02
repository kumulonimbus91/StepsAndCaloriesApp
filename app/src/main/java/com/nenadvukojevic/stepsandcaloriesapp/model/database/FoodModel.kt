package com.nenadvukojevic.stepsandcaloriesapp.model.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.nenadvukojevic.stepsandcaloriesapp.doublesToIntOrOne
import kotlinx.parcelize.IgnoredOnParcel
import kotlin.math.roundToInt
import retrofit2.Call
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "food_table")
data class FoodModel(
    @PrimaryKey(autoGenerate = true)


    @ColumnInfo var id: Long = 0L,

    @ColumnInfo var name: String?,

    @ColumnInfo var grams: Double,

    @ColumnInfo var carbs: Double,

    @ColumnInfo var kcal: Int,

    @ColumnInfo var proteins: Double,

    @ColumnInfo var fats: Double,

    var date: String?


    ) : Parcelable {
    @IgnoredOnParcel
    var totalNutrients: Int = doublesToIntOrOne(carbs, proteins, fats)

    @IgnoredOnParcel
    var carbsPercent: Int = (100 * carbs.roundToInt()) / totalNutrients

    @IgnoredOnParcel
    var proteinPercent: Int = (100 * proteins.roundToInt()) / totalNutrients

    @IgnoredOnParcel
    var fatPercent: Int = (100 * fats.roundToInt()) / totalNutrients

    @IgnoredOnParcel
    var sumPercent: Int = carbsPercent + proteinPercent + fatPercent




}