package com.nenadvukojevic.stepsandcaloriesapp.model.entities

import android.os.Parcelable
import com.nenadvukojevic.stepsandcaloriesapp.foodNameForLayout
import com.nenadvukojevic.stepsandcaloriesapp.foodNameToShortString
import kotlinx.parcelize.Parcelize


@Parcelize
data class Food (
    val foodId: String = "",
    val label: String = "",
    val nutrients: Nutrients,
    val category: String = "",
    val categoryLabel: String = "",
    val image: String = ""

) : Parcelable {
    val shortName
        get() = label.foodNameToShortString()

    val layoutName
        get() = label.foodNameForLayout()

}
