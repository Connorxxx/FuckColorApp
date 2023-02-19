package com.connor.fuckcolorapp.test

class Foo() {
    val justVal = System.nanoTime().toString()
    val getterVal get() = System.nanoTime().toString()
    val lazyVal by lazy { System.nanoTime().toString() }
}

fun main() {
    with(Foo()) {
        repeat(2) {
            println("Just: $justVal")
            println("Getter: $getterVal")
            println("Lazy:   $lazyVal")
        }
    }
}