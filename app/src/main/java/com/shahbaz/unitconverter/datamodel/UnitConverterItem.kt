package com.shahbaz.unitconverter.datamodel

import androidx.annotation.DrawableRes

data class UnitConverterItem(
    val id: Int,
    val name: String,
    @DrawableRes val image: Int,
)
