package com.example.chinesechattutor

import org.junit.Assert.*
import org.junit.Test

class SampleTutorTest {
    @Test fun sampleHasAllRequiredLearningFields() {
        val sample = SampleTutor.feedback()
        assertEquals("嗨，你好呀！", sample.chinese)
        assertTrue(sample.pinyin.isNotBlank())
        assertTrue(sample.english.isNotBlank())
        assertTrue(sample.explanation.contains("美女"))
    }
}
