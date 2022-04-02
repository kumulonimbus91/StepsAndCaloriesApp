package com.nenadvukojevic.stepsandcaloriesapp.model.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
data class Nutrients(
    @Json(name = "ENERC_KCAL") val kcal: Double = 0.0,
    @Json(name = "PROCNT") val protein: Double = 0.0,
    @Json(name = "CHOCDF") val carbs: Double = 0.0,
    @Json(name = "FAT") val fat: Double = 0.0,
    @Json(name = "FIBTG") val fiber: Double = 0.0
) : Parcelable {

    @IgnoredOnParcel
    val totalNutrients = (protein + fat + carbs).roundToInt()
    @IgnoredOnParcel
    val carbsPercent = (100 * carbs.roundToInt()) / totalNutrients
    @IgnoredOnParcel
    val proteinPercent = (100 * protein.roundToInt()) / totalNutrients
    @IgnoredOnParcel
    val fatPercent = (100 * fat.roundToInt()) / totalNutrients
    @IgnoredOnParcel
    val sumPercent = carbsPercent + proteinPercent + fatPercent
}