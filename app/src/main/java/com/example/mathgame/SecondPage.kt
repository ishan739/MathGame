package com.example.mathgame

import android.content.IntentSender
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondPage(navController: NavController, category: String) {

    val life = remember { mutableStateOf(3) }
    val remainingTimeText = remember { mutableStateOf("30") }
    val score = remember { mutableStateOf(0) }
    val myQuestion = remember { mutableStateOf("") }
    val myAnswer = remember { mutableStateOf("") }
    val isEnabled = remember { mutableStateOf(true) }
    val correctAnswer = remember { mutableStateOf(0) }
    val myContext = LocalContext.current
    val totalTimeInMillis = remember { mutableStateOf(10000L) }

    val timer = remember {
        mutableStateOf(
            object : CountDownTimer(totalTimeInMillis.value, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.d("RemainingTimeText", "Value: ${remainingTimeText.value}")
                    remainingTimeText.value = String.format(Locale.getDefault(), "%02d", millisUntilFinished / 1000)
                }

                override fun onFinish() {
                    cancel()
                    myQuestion.value = "Sorry, Time is Up!"
                    life.value -= 1;
                    isEnabled.value = false
                }

            }.start()
        )
    }

    val systemUIController = rememberSystemUiController()
    systemUIController.setStatusBarColor(color = colorResource(id = R.color.blue))

    LaunchedEffect(key1 = "math", block = {
        val resultList = generateQuestion(category)
        myQuestion.value = resultList[0].toString()
        correctAnswer.value = resultList[1] as Int
        Log.d("question", myQuestion.value)

    })


    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() })
                    {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Text(
                        text = when (category) {
                            "add" -> "Addition"
                            "sub" -> "Subtraction"
                            "mul" -> "Multiplication"
                            else -> "Division"
                        },
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.blue),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .paint(
                        painter = painterResource(id = R.drawable.bg4),
                        contentScale = ContentScale.FillBounds
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = "Life: ", fontSize = 16.sp, color = Color.White)
                    Text(text = life.value.toString(), fontSize = 16.sp, color = Color.White)
                    Text(text = "Score: ", fontSize = 16.sp, color = Color.White)
                    Text(text = score.value.toString(), fontSize = 16.sp, color = Color.White)
                    Text(text = "Remaining Time: ", fontSize = 16.sp, color = Color.White)
                    Text(
                        text = remainingTimeText.value,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(4.dp)
                    )

                }

                Spacer(modifier = Modifier.height(30.dp))
                TextForQuestion(text = myQuestion.value)

                Spacer(modifier = Modifier.height(15.dp))

                TextFieldForAnswer(text = myAnswer)

                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                )
                {
                    ButtonOkNext(
                        buttonText = "CHECK",
                        myOnclick = {
                            if (myAnswer.value.isEmpty()) {
                                Toast.makeText(
                                    myContext,
                                    "Write an Answer or Click the NEXT button",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                timer.value.cancel()
                                isEnabled.value = false
                                if (myAnswer.value.toInt() == correctAnswer.value) {
                                    score.value += 10
                                    myQuestion.value = "Congratulations..."
                                    myAnswer.value = ""
                                } else {
                                    life.value -= 1
                                    myQuestion.value = "Sorry , you answer is wrong"
                                    myAnswer.value = ""
                                }

                            }

                        },
                        isEnabled = isEnabled.value
                    )

                    ButtonOkNext(
                        buttonText = "NEXT",
                        myOnclick = {
                            timer.value.cancel()
                            timer.value.start()

                            if (life.value == 0) {
                                Toast.makeText(myContext, "GAME OVER", Toast.LENGTH_SHORT).show()

                                navController.navigate("ResultPage/${score.value}")
                                {
                                    popUpTo("FirstPage") {
                                        inclusive = false
                                    }
                                }
                            } else {
                                val newResultList = generateQuestion(category)
                                myQuestion.value = newResultList[0].toString()
                                correctAnswer.value = newResultList[1] as Int
                                myAnswer.value = ""
                                isEnabled.value = true
                            }
                        },
                        isEnabled = true
                    )
                }
            }
        }
    )

}

