package com.ring.ring.test.fake

import com.ring.ring.log.Logger

class FakeLogger : Logger {
    var vWasCalled: Parameter? = null
    override fun v(tag: String, msg: String, tr: Throwable?) {
        vWasCalled = Parameter(tag, msg, tr)
    }

    var iWasCalled: Parameter? = null
    override fun i(tag: String, msg: String, tr: Throwable?) {
        iWasCalled = Parameter(tag, msg, tr)
    }

    var wWasCalled: Parameter? = null
    override fun w(tag: String, msg: String, tr: Throwable?) {
        wWasCalled = Parameter(tag, msg, tr)
    }

    var dWasCalled: Parameter? = null
    override fun d(tag: String, msg: String, tr: Throwable?) {
        dWasCalled = Parameter(tag, msg, tr)
    }

    var eWasCalled: Parameter? = null
    override fun e(tag: String, msg: String, tr: Throwable?) {
        eWasCalled = Parameter(tag, msg, tr)
    }

    data class Parameter(
        val tag: String,
        val msg: String,
        val tr: Throwable?
    )
}