# Topic
* id
* Topic: Lesson3, ZH2, RoomDatabase
* ParentFk: Topic
* Description: RoomDatabase is a library that provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.

# Point
* id
* Point: 2,4
* Type: Plus/Minus, Plus
* GoodAnswer: +2,+1
* BadAnswer: -1,-2

# Type
* id
* Type: MultipleChoiceQuestion, TrueFalseQuestion

# TrueFalseQuestion
* id
* Question: Is RoomDatabase a library?
* CorrectAnswer: True
* PointFk
* TopicFk
* TypeFk

# MultipleChoiceQuestion
* id
* Question: What is the main purpose of RoomDatabase?
* Answers: To create a layer over SQLite¤To create a layer over Firebase¤To create a layer over Realm¤To create a layer over MongoDB
* CorrectAnswer: 0
* PointFk
* TopicFk
* TypeFk

# Exam
* id
* ExamName: Lesson3
* QuestionList: 0-12¤0-13¤1-3
* TopicFk

## Links
* https://developer.android.com/training/data-storage/room/accessing-data
* https://developer.android.com/training/data-storage/room/relationships#kotlin
* https://developer.android.com/codelabs/basic-android-kotlin-compose-persisting-data-room
* https://developer.android.com/build/migrate-to-ksp
* https://github.com/google/ksp/releases?page=2
