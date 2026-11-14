package com.example.chinesechattutor

/** Replace this provider in Phase 2; Android's text-processing contract stays separate. */
data class TutorFeedback(
    val chinese: String,
    val pinyin: String,
    val english: String,
    val explanation: String,
)

object SampleTutor {
    // Always returns the same demonstration. It is NOT a translation of arbitrary input.
    fun feedback() = TutorFeedback(
        chinese = "嗨，你好呀！",
        pinyin = "Hāi, nǐ hǎo ya!",
        english = "Hi there!",
        explanation = "A friendly, casual greeting. The sample original uses 美女, " +
            "which can sound flirtatious or overly familiar depending on the relationship.",
    )
}
