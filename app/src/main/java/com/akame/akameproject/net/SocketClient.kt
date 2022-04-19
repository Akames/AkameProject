package com.akame.akameproject.net

import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.net.Socket

object SocketClient {

    @JvmStatic
    fun main(args: Array<String>) {
        val socket = Socket()
        val socketAddress = InetSocketAddress("127.0.0.1", 9999)
        socket.connect(socketAddress)

        val oos = ObjectOutputStream(socket.getOutputStream())
        oos.writeUTF("Akame")
        oos.flush()

//        val ois = ObjectInputStream(socket.getInputStream())
//        val result = ois.readUTF()
//        println(result)

        oos.close()
//        ois.close()
        socket.close()
    }
}