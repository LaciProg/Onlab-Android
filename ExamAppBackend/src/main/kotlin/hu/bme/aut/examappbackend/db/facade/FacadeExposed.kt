package hu.bme.aut.examappbackend.db.facade

object FacadeExposed {
    val examDao: ExamFacade = ExamFacadeExposed()
    val multipleChoiceQuestionDao: MultipleChoiceQuestionFacade = MultipleChoiceQuestionFacadeExposed()
    val pointDao: PointFacade = PointFacadeExposed()
    val topicDao: TopicFacade = TopicFacadeExposed()
    val trueFalseQuestionDao: TrueFalseQuestionFacade = TrueFalseQuestionFacadeExposed()
    val typeDao: TypeFacade = TypeFacadeExposed()
    val userDao: UserFacade = UserFacadeExposed()
}