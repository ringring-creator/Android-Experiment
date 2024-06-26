package com.ring.ring.util.log


interface Logger {
    fun v(tag: String, msg: String, tr: Throwable?)
    fun i(tag: String, msg: String, tr: Throwable?)
    fun w(tag: String, msg: String, tr: Throwable?)
    fun d(tag: String, msg: String, tr: Throwable?)
    fun e(tag: String, msg: String, tr: Throwable?)
}