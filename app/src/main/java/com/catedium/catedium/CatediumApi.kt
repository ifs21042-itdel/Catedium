package com.catedium.catedium

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CatediumApi {
    @GET("b/666ba7faad19ca34f878bc46")
    @Headers(
        "X-Master-Key: \$2a\$10\$ieX9epwEazOyv0JHxgrP6OVgvlR62/tmo8pBlcWnIiu1T2wqbaSWe",
        "X-Access-Key: \$2a\$10\$g9zPkKw05KoIwkwntvuZweRY4X/NEMyv3u2jtFaJoLXOhF6pPETSi"
    )
    fun getCats(): Call<CatApiResponse>

    @Multipart
    @POST("api/predict")
    fun uploadPhoto(@Part file: MultipartBody.Part): Call<PhotoApiResponse>

    companion object {
        private const val BASE_URL = "https://flask-api-v2-zchoaqtzcq-et.a.run.app/"

        val retrofitService: CatediumApi by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CatediumApi::class.java)
        }
    }
}

data class PhotoApiResponse(
    val data: Data,
    val requestId: String
)

data class Data(
    val `class`: String
)

data class CatApiResponse(val record: List<CatRecord>)
data class CatRecord(
    val name: String,
    val scientific_name: String,
    val distribution: String,
    val habitat: List<String>,
    val characteristics: List<String>,
    val description: String
)

