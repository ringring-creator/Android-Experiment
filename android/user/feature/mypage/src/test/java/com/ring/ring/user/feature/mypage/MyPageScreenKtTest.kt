package com.ring.ring.user.feature.mypage

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.ring.ring.user.infra.test.TestActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class MyPageScreenKtTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `set user fetched from local`() {
        setupMyPageScreen()
    }

    private fun setupMyPageScreen() {
        composeTestRule.setContent {
            MyPageScreen()
        }
    }
}