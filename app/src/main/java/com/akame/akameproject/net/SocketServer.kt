package com.akame.akameproject.net

import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

object SocketServer {
    @JvmStatic
    fun main(args: Array<String>) {
        val serverSocket = ServerSocket(9999)
        println("server starting...")
        val threadPool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2
        )
        serverSocket.use { server ->
            while (true) {
                threadPool.execute {
                    ServerRunnable(server.accept())
                }
            }
        }
    }

    private class ServerRunnable(val socket: Socket) : Runnable {
        override fun run() {
            socket.use { socket ->
                val input = ObjectInputStream(socket.getInputStream())
                val clientData = input.readUTF()
                print("connect :${clientData}")
                val output = ObjectOutputStream(socket.getOutputStream())
                output.writeUTF("Hello :${clientData}")
                output.flush()

                input.close()
                output.close()
            }
        }
    }

}