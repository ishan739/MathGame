package com.example.mathgame

import kotlin.random.Random


fun generateQuestion(selectedCategory: String) : ArrayList<Any>{

    var number1 = Random.nextInt(0,100)
    var number2 = Random.nextInt(0,100)

    val textQuestion : String
    val correctAnswer : Int

    when(selectedCategory){
        "add" -> {
            textQuestion = "$number1 + $number2"
            correctAnswer = number1 + number2
        }
        "sub" -> {
            if(number1 < number2){
                val temp = number1
                number1 = number2
                number2 = temp
            }
            textQuestion = "$number1 - $number2"
            correctAnswer = number1 - number2
        }
        "mul" -> {
            number1 = Random.nextInt(1 , 20)
            number2 = Random.nextInt(1 , 20)
            textQuestion = "$number1 * $number2"
            correctAnswer = number1 * number2
        }
        else  -> {
            if(number1== 0 ||  number2 == 0){
                textQuestion = " 0 / 1"
                correctAnswer = 0

            } else if (number1 >= number2){
                val newBignumber =  number1 - (number1 % number2)
                textQuestion = "$newBignumber / $number2"
                correctAnswer = newBignumber / number2
            } else {
                val newBignumber =  number2 - (number2 % number1)
                textQuestion = "$newBignumber / $number1"
                correctAnswer = newBignumber / number1
            }
        }
    }
    val gameResultList = ArrayList<Any>()
    gameResultList.add(textQuestion)
    gameResultList.add(correctAnswer)

    return gameResultList

}