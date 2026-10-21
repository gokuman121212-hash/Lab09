package com.cafq.lab09

import retrofit2.http.GET

interface AmphibianApiService {
    @GET("amphibians.json")
    suspend fun getAmphibians(): List<AmphibianModel>
}
