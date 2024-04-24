package hu.bme.aut.android.examapp.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import hu.bme.aut.android.examapp.api.dto.ExamDto
import hu.bme.aut.android.examapp.api.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.api.dto.NameDto
import hu.bme.aut.android.examapp.api.dto.PointDto
import hu.bme.aut.android.examapp.api.dto.TopicDto
import hu.bme.aut.android.examapp.api.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.api.dto.UserDto
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

private const val BASE_URL = "http://152.66.182.116:46258"
private const val token =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJodHRwOi8vMC4wLjAuMDo0NjI1OC8iLCJpc3MiOiJleGFtLWFwcC1iYWNrZW5kIiwidXNlcm5hbWUiOiJ1c2VyIiwiZXhwIjoxNzE0MDA0MTU3fQ.6cWK6mSudymc8K0ETUyBXUNvl8i11Rm-8dzrTW7edYI"

//TODO Firebase login system + analytics
//TODO Login screen + register screen + store users in Room
@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .client(
        OkHttpClient.Builder()
            .addInterceptor(AuthenticationInterceptor(token))
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build())
    .baseUrl(BASE_URL)
    .build()

object ExamAppApi {
    val retrofitService: ExamAppApiService by lazy {
        retrofit.create(ExamAppApiService::class.java)
    }
}

interface ExamAppApiService {
    @GET("/user")
    suspend fun getAllUser(): Response<List<UserDto>>


    @GET("/point")
    suspend fun getAllPoint() : List<PointDto>

    @GET("/point/name")
    suspend fun getAllPointName() : List<NameDto>

    @GET("/point/{id}")
    suspend fun getPoint(@Path("id") string: String) : PointDto

    @DELETE("/point/{id}")
    suspend fun deletePoint(@Path("id") string: String): Response<Unit>

    @POST("/point")
    suspend fun postPoint(@Body body: PointDto): PointDto?

    @PUT("/point")
    suspend fun updatePoint(@Body body: PointDto): Response<Unit>


    @GET("/topic")
    suspend fun getAllTopic() : List<TopicDto>

    @GET("/topic/name")
    suspend fun getAllTopicName() : List<NameDto>

    @GET("/topic/{id}")
    suspend fun getTopic(@Path("id") string: String) : TopicDto

    @GET("/topic/name/{topic}")
    suspend fun getTopicByTopic(@Path("topic") string: String) : TopicDto?

    @DELETE("/topic/{id}")
    suspend fun deleteTopic(@Path("id") string: String): Response<Unit>

    @POST("/topic")
    suspend fun postTopic(@Body body: TopicDto): TopicDto?

    @PUT("/topic")
    suspend fun updateTopic(@Body body: TopicDto): Response<Unit>


    @GET("/trueFalse")
    suspend fun getAllTrueFalse() : List<TrueFalseQuestionDto>

    @GET("/trueFalse/name")
    suspend fun getAllTrueFalseName() : List<NameDto>

    @GET("/trueFalse/{id}")
    suspend fun getTrueFalse(@Path("id") string: String) : TrueFalseQuestionDto

    @DELETE("/trueFalse/{id}")
    suspend fun deleteTrueFalse(@Path("id") string: String): Response<Unit>

    @POST("/trueFalse")
    suspend fun postTrueFalse(@Body body: TrueFalseQuestionDto): TrueFalseQuestionDto?

    @PUT("/trueFalse")
    suspend fun updateTrueFalse(@Body body: TrueFalseQuestionDto): Response<Unit>


    @GET("/multipleChoice")
    suspend fun getAllMultipleChoice() : List<MultipleChoiceQuestionDto>

    @GET("/multipleChoice/name")
    suspend fun getAllMultipleChoiceName() : List<NameDto>

    @GET("/multipleChoice/{id}")
    suspend fun getMultipleChoice(@Path("id") string: String) : MultipleChoiceQuestionDto

    @DELETE("/multipleChoice/{id}")
    suspend fun deleteMultipleChoice(@Path("id") string: String): Response<Unit>

    @POST("/multipleChoice")
    suspend fun postMultipleChoice(@Body body: MultipleChoiceQuestionDto): MultipleChoiceQuestionDto?

    @PUT("/multipleChoice")
    suspend fun updateMultipleChoice(@Body body: MultipleChoiceQuestionDto): Response<Unit>


    @GET("/exam")
    suspend fun getAllExam() : List<ExamDto>

    @GET("/exam/name")
    suspend fun getAllExamName() : List<NameDto>

    @GET("/exam/{id}")
    suspend fun getExam(@Path("id") string: String) : ExamDto

    @GET("/exam/{id}/question")
    suspend fun getExamQuestions(@Path("id") string: String) : ExamDto

    @DELETE("/exam/{id}")
    suspend fun deleteExam(@Path("id") string: String): Response<Unit>

    @POST("/exam")
    suspend fun postExam(@Body body: ExamDto): ExamDto?

    @PUT("/exam")
    suspend fun updateExam(@Body body: ExamDto): Response<Unit>

}