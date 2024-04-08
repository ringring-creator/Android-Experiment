package com.ring.ring.signup

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test

class SignUpViewModelTest {
    private lateinit var subject: SignUpViewModel

    @Before
    fun setUp() {
        subject = SignUpViewModel()
    }

    @Test
    fun setEmail() {
        val expect = "fake-email"
        subject.setEmail(expect)


        MatcherAssert.assertThat(subject.email.value, CoreMatchers.equalTo(expect))
    }

    @Test
    fun setPassword() {
        val expect = "fake-password"
        subject.setPassword(expect)


        MatcherAssert.assertThat(subject.password.value, CoreMatchers.equalTo(expect))
    }

    @Test
    fun signUp() {

    }
}