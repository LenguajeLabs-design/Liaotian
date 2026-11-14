package com.example.chinesechattutor

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class ProcessTextActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Back, outside tap and Cancel all leave the caller's selection untouched.
        setResult(Activity.RESULT_CANCELED)
        val selected = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()
        if (intent.action != Intent.ACTION_PROCESS_TEXT || selected.isNullOrBlank()) {
            Toast.makeText(this, "Select some text first.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        // Missing flags are treated conservatively because this activity is exported.
        val readOnly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, true)
        val feedback = SampleTutor.feedback()
        setContent {
            TutorTheme {
                Surface {
                    Column(Modifier.fillMaxWidth().heightIn(max = 620.dp)
                        .verticalScroll(rememberScrollState()).padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Improve Chinese", style = MaterialTheme.typography.headlineSmall)
                        Text("Offline prototype • fixed sample feedback", style = MaterialTheme.typography.labelLarge)
                        Text("For any selection, this demo suggests the same greeting. " +
                            "Only use it if that is what you intend to say.")
                        FeedbackSection("Original Chinese", selected)
                        FeedbackSection("Suggested Chinese", feedback.chinese)
                        FeedbackSection("Pinyin", feedback.pinyin)
                        FeedbackSection("English", feedback.english)
                        FeedbackSection("Explanation (sample greeting)", feedback.explanation)
                        Text(if (readOnly) "Read-only selection: use Copy, then paste into an editable field."
                            else "Use Suggestion asks the original app to replace the selected text. " +
                                "If it ignores the result, reopen this popup and use Copy.")
                        Button(onClick = {
                            setResult(Activity.RESULT_OK, Intent().putExtra(
                                Intent.EXTRA_PROCESS_TEXT, feedback.chinese))
                            finish()
                        }, enabled = !readOnly, modifier = Modifier.fillMaxWidth()) {
                            Text("Use Suggestion")
                        }
                        OutlinedButton(onClick = {
                            getSystemService(ClipboardManager::class.java).setPrimaryClip(
                                ClipData.newPlainText("Chinese suggestion", feedback.chinese))
                            Toast.makeText(this@ProcessTextActivity, "Suggestion copied", Toast.LENGTH_SHORT).show()
                        }, modifier = Modifier.fillMaxWidth()) { Text("Copy") }
                        TextButton(onClick = { finish() }, modifier = Modifier.fillMaxWidth()) { Text("Cancel") }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeedbackSection(title: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
