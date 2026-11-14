package com.example.chinesechattutor

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.doAfterTextChanged

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TutorTheme {
                Surface(Modifier.fillMaxSize()) {
                    TestScreen()
                }
            }
        }
    }
}

@Composable
private fun TestScreen() {
    var draft by rememberSaveable { mutableStateOf("哈喽，美女") }
    val samples = listOf("哈喽，美女", "我今天想喝咖啡。", "你周末有空吗？")
    Column(Modifier.fillMaxSize().safeDrawingPadding()
        .imePadding().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Chinese Chat Tutor", style = MaterialTheme.typography.headlineMedium)
        Text("Phase 1 · offline test bench", style = MaterialTheme.typography.titleMedium)
        Text("Long-press the Chinese below, or select it with your S Pen. " +
            "Open the selection menu's ⋮ / More menu and tap Improve Chinese.")
        // A framework EditText exercises Android's actual PROCESS_TEXT integration.
        // Compose text fields can have different selection toolbar behavior.
        AndroidView(factory = { context ->
            EditText(context).apply {
                hint = "Write Chinese here"
                textSize = 22f
                minLines = 3
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                setText(draft)
                doAfterTextChanged { draft = it?.toString().orEmpty() }
            }
        }, update = { view ->
            if (view.text.toString() != draft) {
                view.setText(draft)
                view.setSelection(view.text.length)
            }
        }, modifier = Modifier.fillMaxWidth())
        Text("Load a sample", style = MaterialTheme.typography.titleSmall)
        samples.forEach { phrase ->
            OutlinedButton(onClick = { draft = phrase }, modifier = Modifier.fillMaxWidth()) {
                Text(phrase)
            }
        }
        Text("Verify replacement", style = MaterialTheme.typography.titleMedium)
        Text("Select the whole greeting, choose Improve Chinese, then Use Suggestion. " +
            "The field should become 嗨，你好呀！. Select only part of a sentence to check " +
            "that surrounding text stays intact. Cancel should leave the field unchanged.")
        Text("Read-only test", style = MaterialTheme.typography.titleMedium)
        AndroidView(factory = { context ->
            TextView(context).apply {
                text = "哈喽，美女"
                textSize = 22f
                setTextIsSelectable(true)
            }
        }, modifier = Modifier.fillMaxWidth())
        Text("Select the read-only greeting above. Use Suggestion should be disabled; " +
            "Copy should work. Return to the editable field and paste to verify.")
        Text("This prototype never sends messages or contacts an AI service. " +
            "Feedback is a fixed example, even for other phrases.")
    }
}
