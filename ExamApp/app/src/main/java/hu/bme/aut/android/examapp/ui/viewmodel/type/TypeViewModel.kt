package hu.bme.aut.android.examapp.ui.viewmodel.type

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TypeRepository
import hu.bme.aut.android.examapp.data.room.dto.TypeDto
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TypeViewModel(
    private val typeRepository: TypeRepository,
    private val types: List<String> = listOf("trueFalseQuestion", "multipleChoiceQuestion"),
) : ViewModel() {


    init {
        viewModelScope.launch {
           val storedTypes = typeRepository.getAllTypeType().filterNotNull().first()
           for(type in types) {
                if(!storedTypes.contains(type)) typeRepository.insertType(TypeDto( id = 0, type = type))
           }
        }
    }
}