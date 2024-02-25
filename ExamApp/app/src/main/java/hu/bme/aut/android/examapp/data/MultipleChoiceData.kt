package hu.bme.aut.android.examapp.data

data class MultipleChoiceData(
    val question: String,
    val answers: List<String>,
    val correctAnswer: Int
)