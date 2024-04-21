package com.ring.ring.user.infra.test

import com.ring.ring.user.infra.model.User

val userTestData = User.generate(10L, "email@example.com", "Abcdefg1", "fakeToken")