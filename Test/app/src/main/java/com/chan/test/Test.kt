package com.chan.test

import kotlinx.coroutines.*

fun main() = runBlocking {
    launch {
        delay(200L)
        println("launch 1")
    }

    repeat()

    coroutineScope{
        launch {
            delay(500L)
            println("launch 2")
        }

        delay(100L)
        println("coroutineScope end")
    }
    println("Coroutine scope is over")
}

/*
fun main() = runBlocking {
    repeat(100_000) {
        launch {
            delay(1000L)
            println(".")
        }
    }
}
*/

/*
fun main() {
    GlobalScope.launch { // 在后台启动一个新的协程并继续
        delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
        println("World!") // 在延迟后打印输出
    }
    println("Hello,") // 协程已在等待时主线程还在继续
    Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
}*/
