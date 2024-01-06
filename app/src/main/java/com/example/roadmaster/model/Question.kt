package com.example.roadmaster.model

data class Question(
    var id: Int = -1,
    var category: String = String(),
    var text: String = String(),
    var answers: MutableList<Pair<String, Boolean>> = mutableListOf(),
    var chosenAnswers: MutableList<Boolean> = mutableListOf()
)
