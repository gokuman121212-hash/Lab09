package com.cafq.lab09

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("products") val products: List<ProductModel>
)

data class ProductModel(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("price") val price: Double,
    @SerializedName("rating") val rating: Double,
    @SerializedName("brand") val brand: String?,
    @SerializedName("thumbnail") val thumbnail: String
)
