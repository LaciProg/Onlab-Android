package hu.bme.aut.android.examapp.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import hu.bme.aut.android.examapp.api.dto.ExamDto
import hu.bme.aut.android.examapp.api.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.api.dto.NameDto
import hu.bme.aut.android.examapp.api.dto.PointDto
import hu.bme.aut.android.examapp.api.dto.Token
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

//TODO Firebase login system + analytics

object ExamAppApi {
    private const val BASE_URL = "http://152.66.182.116:46258"
    private var token: String = ""
    private var userDto: UserDto? = null
    @OptIn(ExperimentalSerializationApi::class)
    private var retrofit = Retrofit.Builder()
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


    private fun retrofitServiceCreator(): ExamAppApiService  =
        retrofit.create(ExamAppApiService::class.java)

    var retrofitService: ExamAppApiService
    init{
        retrofitService = retrofitServiceCreator()
    }


    @OptIn(ExperimentalSerializationApi::class)
    private fun buildRetrofit() {
        retrofit = Retrofit.Builder()
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

        retrofitService = retrofitServiceCreator()
    }

    suspend fun authenticate(user: UserDto? = null) {
        userDto = user ?: userDto
        try{
            token = retrofitService.authenticate(userDto!!).token
        } catch (e: Exception) {
            throw Exception("Authentication failed")
        }
        buildRetrofit()
    }
}

interface ExamAppApiService {
    @POST("/auth")
    suspend fun authenticate(@Body body: UserDto): Token

    @GET("/user")
    suspend fun getAllUser(): Response<List<UserDto>>

    @GET("/user/{id}")
    suspend fun getUser(@Path("id") string: String) : UserDto

    @GET("/user/name/{userName}")
    suspend fun getUserByName(@Path("userName") string: String) : UserDto?


    @POST("/user")
    suspend fun postUser(@Body body: UserDto): UserDto?

    @PUT("/user")
    suspend fun updateUser(@Body body: UserDto): Response<Unit>

    @DELETE("/user/{id}")
    suspend fun deleteUser(@Path("id") string: String): Response<Unit>


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