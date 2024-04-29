package hu.bme.aut.android.examapp.ui.viewmodel.type

/*
class TypeViewModel(
    private val typeRepository: TypeRepository,
    private val types: List<String> = listOf(Type.trueFalseQuestion.name, Type.multipleChoiceQuestion.name),
) : ViewModel() {


    init {
        viewModelScope.launch {
           val storedTypes = typeRepository.getAllTypeType().filterNotNull().first()
           for(type in types) {
                if(!storedTypes.contains(type)) typeRepository.insertType(TypeDto(id = 0, type = type))
           }
        }
    }
}*/

enum class Type {
    trueFalseQuestion,
    multipleChoiceQuestion
}