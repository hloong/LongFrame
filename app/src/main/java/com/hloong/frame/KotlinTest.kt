package com.hloong.frame

import kotlin.math.abs

fun main() {
    val num1 = 12.3f
    val num2 = 12
    val num3 = "123"
    val num4 = -1.78

    println(abs(num4))
    println(num1.toInt())
    print(num1)
    print(num2)
    print(num3)
}

fun print(param:Any) {
    println("$param is ${param::class.simpleName}")
}

