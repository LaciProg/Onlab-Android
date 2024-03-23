/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.bme.aut.android.examapp.ui.exam

import android.util.Log
import android.widget.ExpandableListAdapter
import hu.bme.aut.android.examapp.R
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.examapp.data.room.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.data.room.dto.PointDto
import hu.bme.aut.android.examapp.data.room.dto.Question
import hu.bme.aut.android.examapp.data.room.dto.TopicDto
import hu.bme.aut.android.examapp.data.room.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.components.DropDownList
import hu.bme.aut.android.examapp.ui.multiplechoicequestion.MultipleChoiceQuestionDetailsBody
import hu.bme.aut.android.examapp.ui.theme.ExamAppTheme
import hu.bme.aut.android.examapp.ui.theme.Green
import hu.bme.aut.android.examapp.ui.theme.GreenLight
import hu.bme.aut.android.examapp.ui.theme.PaleDogwood
import hu.bme.aut.android.examapp.ui.theme.Seashell
import hu.bme.aut.android.examapp.ui.truefalsequestion.TrueFalseQuestionDetails
import hu.bme.aut.android.examapp.ui.multiplechoicequestion.MultipleChoiceQuestionDetails
import hu.bme.aut.android.examapp.ui.truefalsequestion.DeleteConfirmationDialog
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamDetailsViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionDetails
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionDetailsUiState
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionDetailsViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.toMultipleChoiceQuestionDetails
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicDetails
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicDetailsViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicListViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionDetailsViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.type.Type
import kotlinx.coroutines.launch
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.toTrueFalseQuestionDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamDetailsScreen(
    navigateToEditMultipleChoiceQuestion: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    examViewModel: ExamDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val  examUiState = examViewModel.uiState.collectAsState()
    var tabPage by remember { mutableStateOf(Type.trueFalseQuestion) }
    val coroutineScope = rememberCoroutineScope()
    val backgroundColor by animateColorAsState(
        if (tabPage == Type.trueFalseQuestion) Seashell else GreenLight,
        label = "background color"
    )
    Scaffold(
        topBar = {
            SearchBar(
                backgroundColor = backgroundColor,
                tabPage = tabPage,
                onTabSelected = { tabPage = it },
                topics = examViewModel.topicList.collectAsState().value,
                trueFalse =  examViewModel.trueFalseList.collectAsState().value,
                multipleChoice =  examViewModel.multipleChoiceList.collectAsState().value,
                questions = examUiState.value.examDetails.questionList,
                examTopic = examUiState.value.examDetails.topicId,
            ){
                coroutineScope.launch {
                    when (tabPage) {
                        Type.trueFalseQuestion -> examViewModel.saveQuestion(tabPage.ordinal, it)
                        Type.multipleChoiceQuestion -> examViewModel.saveQuestion(tabPage.ordinal, it)
                    }
                }
            }
        },
        //containerColor = backgroundColor,
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = { navigateToEditMultipleChoiceQuestion(examUiState.value.examDetails.id) },
                shape = MaterialTheme.shapes.medium,
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.exam_info),
                )
            }
        },
        bottomBar = {
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)))
        },
        modifier = modifier
    ) { innerPadding ->
        ExamDetailsBody(
            questions = examUiState.value.examDetails.questionList,
            topicList = examViewModel.topicList.collectAsState().value,
            pointList = examViewModel.pointList.collectAsState().value,
            modifier = Modifier.padding(innerPadding),
            tabPage = tabPage
        )
    }
}


@Composable
private fun SearchBar(
    backgroundColor: Color,
    tabPage: Type,
    examTopic: Int,
    onTabSelected: (tabPage: Type) -> Unit,
    topics: List<TopicDto>,
    trueFalse: List<TrueFalseQuestionDto>,
    multipleChoice: List<MultipleChoiceQuestionDto>,
    questions: List<Question>,
    onAddQuestion: (String) -> Unit,
) {
    Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        TabRow(
            selectedTabIndex = tabPage.ordinal,
            containerColor = backgroundColor,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            indicator = { tabPositions ->
                QuestionTabIndicator(tabPositions, tabPage)
            }
        ) {
            QuestionTab(
                icon = Icons.Default.Check,
                title = stringResource(R.string.true_false),
                onClick = { onTabSelected(Type.trueFalseQuestion) }
            )
            QuestionTab(
                icon = Icons.AutoMirrored.Filled.List,
                title = stringResource(R.string.multiple),
                onClick = { onTabSelected(Type.multipleChoiceQuestion) }
            )
        }

        var topicFilter by rememberSaveable { mutableStateOf("") }
        var topicId by rememberSaveable { mutableIntStateOf(-1) }
        DropDownList(
            name = stringResource(R.string.topic),
            items = topics.map { it.topic },
            onChoose = { topicName ->
                topicFilter = topicName
                topicId = topics.filter { it.topic.contains(topicFilter) }.map { it.id }.first()
            }
        )

        var question by rememberSaveable { mutableStateOf("") }
        when(tabPage){
            Type.trueFalseQuestion -> {
            val tFQuestions: List<TrueFalseQuestionDto> = questions.filterIsInstance<TrueFalseQuestionDto>()
                DropDownList(
                    name = stringResource(R.string.question),
                    items = trueFalse
                        .filterNot{ tFQuestions.contains(it) }
                        .filter{ it.topic == if(topicId==-1) examTopic else topicId }
                        .map { it.question},
                    onChoose = { question = it }
                )
            }
            Type.multipleChoiceQuestion -> {
                val mCQuestions: List<MultipleChoiceQuestionDto> = questions.filterIsInstance<MultipleChoiceQuestionDto>()
                DropDownList(
                    name = stringResource(R.string.question),
                    items = multipleChoice
                        .filterNot { mCQuestions.contains(it) }
                        .filter{ it.topic == if(topicId==-1) examTopic else topicId }
                        .map { it.question },
                    onChoose = { question = it }
                )
            }
        }

        OutlinedButton(
            onClick = {
                onAddQuestion(question)
                question = ""
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            enabled = question.isNotEmpty()
        ) {
            Text(text = stringResource(R.string.add_question))
        }

    }

}

@Composable
private fun QuestionTabIndicator(
    tabPositions: List<TabPosition>,
    tabPage: Type
) {
    val transition = updateTransition(
        tabPage,
        label = "Tab indicator"
    )
    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            if (Type.trueFalseQuestion isTransitioningTo Type.multipleChoiceQuestion) {
                // Indicator moves to the right.
                // Low stiffness spring for the left edge so it moves slower than the right edge.
                spring(stiffness = Spring.StiffnessVeryLow)
            } else {
                // Indicator moves to the left.
                // Medium stiffness spring for the left edge so it moves faster than the right edge.
                spring(stiffness = Spring.StiffnessMedium)
            }
        },
        label = "Indicator left"
    ) { page ->
        tabPositions[page.ordinal].left
    }
    val indicatorRight by transition.animateDp(
        transitionSpec = {
            if (Type.trueFalseQuestion isTransitioningTo Type.multipleChoiceQuestion) {
                // Indicator moves to the right
                // Medium stiffness spring for the right edge so it moves faster than the left edge.
                spring(stiffness = Spring.StiffnessMedium)
            } else {
                // Indicator moves to the left.
                // Low stiffness spring for the right edge so it moves slower than the left edge.
                spring(stiffness = Spring.StiffnessVeryLow)
            }
        },
        label = "Indicator right"
    ) { page ->
        tabPositions[page.ordinal].right
    }
    val color by transition.animateColor(
        label = "Border color"
    ) { page ->
        if (page == Type.trueFalseQuestion) PaleDogwood else Green
    }
    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .padding(4.dp)
            .fillMaxSize()
            .border(
                BorderStroke(2.dp, color),
                RoundedCornerShape(4.dp)
            )
    )
}

@Composable
private fun QuestionTab(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title)
    }
}

@Composable
fun ExamDetailsBody(
    questions: List<Question>,
    topicList: List<TopicDto>,
    pointList: List<PointDto>,
    tabPage: Type,
    modifier: Modifier = Modifier,
    examViewModel: ExamDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
){
    val coroutineScope = rememberCoroutineScope()
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    var lazyQuestions by remember { mutableStateOf(questions) }
    lazyQuestions = questions.toMutableList()
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        lazyQuestions = lazyQuestions.toMutableList().apply {
            add(to.index, removeAt(from.index))
            coroutineScope.launch {
                examViewModel.saveQuestionOrdering(lazyQuestions)
            }
        }
    })
    LazyColumn(
        state = state.listState,
        modifier = modifier
            .reorderable(state = state)
            .detectReorderAfterLongPress(state)
    ) {
        items(lazyQuestions, {if(it is TrueFalseQuestionDto) "${it.type}-${it.id}" else if(it is MultipleChoiceQuestionDto) "${it.type}-${it.id}" else 0}) { question ->
            ReorderableItem(state, key = question) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                Column(
                    modifier = Modifier
                        .shadow(elevation.value)
                ) {
                    ExpandableQuestionItem(
                        topicList = topicList,
                        pointList = pointList,
                        question = question,
                        examViewModel = examViewModel
                    )
                }
            }
        }
    }
    OutlinedButton(
        onClick = { deleteConfirmationRequired = true },
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.delete))
    }
    if (deleteConfirmationRequired) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                coroutineScope.launch { examViewModel.deleteExam() }
            },
            onDeleteCancel = { deleteConfirmationRequired = false },
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableQuestionItem(
    topicList: List<TopicDto>,
    pointList: List<PointDto>,
    question: Question,
    examViewModel: ExamDetailsViewModel,
){
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f, label = ""
    )
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = shape,
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(6f)
            ) {
                when (question.typeOrdinal) {
                    Type.trueFalseQuestion.ordinal -> {
                        val trueFalseQuestion = question as TrueFalseQuestionDto
                        if (expandedState) {
                            TrueFalseQuestionDetails(
                                trueFalseQuestion = trueFalseQuestion.toTrueFalseQuestionDetails(
                                    topicName = if (trueFalseQuestion.topic != -1)
                                        topicList.first { it.id == trueFalseQuestion.topic }.topic
                                    else "",
                                    pointName = if (trueFalseQuestion.point != -1)
                                        pointList.first { it.id == trueFalseQuestion.point }.type
                                    else ""
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                            RemoveButton(coroutineScope, examViewModel, question)
                        } else {
                            CollapsedQuestion(trueFalseQuestion.question)
                        }
                    }

                    Type.multipleChoiceQuestion.ordinal -> {
                        val multipleChoiceQuestion = question as MultipleChoiceQuestionDto
                        if (expandedState) {
                            MultipleChoiceQuestionDetails(
                                multipleChoiceQuestion = multipleChoiceQuestion.toMultipleChoiceQuestionDetails(
                                    topicName = if (multipleChoiceQuestion.topic != -1)
                                        topicList.first { it.id == multipleChoiceQuestion.topic }.topic
                                    else "",
                                    pointName = if (multipleChoiceQuestion.point != -1)
                                        pointList.first { it.id == multipleChoiceQuestion.point }.type
                                    else ""
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                            RemoveButton(coroutineScope, examViewModel, question)
                        } else {
                            CollapsedQuestion(multipleChoiceQuestion.question)
                        }
                    }


                }

            }
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .alpha(0.2f)
                    .rotate(rotationState),
                onClick = {
                    expandedState = !expandedState
                }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Drop-Down Arrow"
                )
            }
        }
    }
}

@Composable
private fun RemoveButton(
    coroutineScope: CoroutineScope,
    examViewModel: ExamDetailsViewModel,
    question: Question
) {
    OutlinedButton(
        onClick = {
            coroutineScope.launch {
                examViewModel.removeQuestion(question)
            }
        },
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.remove_question))
    }
}

@Composable
private fun CollapsedQuestion(question: String){
    Card(
        modifier = Modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Row(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen
                .padding_medium))
            ) {
                Text(text = stringResource(R.string.question))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = question, fontWeight = FontWeight.Bold)
            }
        }
    }
}

