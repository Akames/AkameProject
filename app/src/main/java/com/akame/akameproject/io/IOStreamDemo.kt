package com.akame.akameproject.io

import java.io.*
import java.nio.charset.Charset

object IOStreamDemo {

    @JvmStatic
    fun main(args: Array<String>) {
        val filePath = "outFile.txt"
//        testOutput(filePath)
//        testInput(filePath)
//        byteArraySteamOutput()
//        byteArraySteamInput()
//        charOutput()
        charInput()
    }

    /**
     * 测试输出
     */
    private fun testOutput(outFilePath: String) {
        val dataOutput = DataOutputStream(
            BufferedOutputStream(
                FileOutputStream(File(outFilePath))
            )
        )
        dataOutput.writeUTF("我和我的祖国")
        dataOutput.flush()
        dataOutput.close()
    }

    /**
     * 输入
     */
    private fun testInput(inputFilePath: String) {
        val dataInputStream = DataInputStream(
            BufferedInputStream(
                FileInputStream(File(inputFilePath))
            )
        )
        val data = dataInputStream.readUTF()
        println("读出数据")
        println(data)
        dataInputStream.close()
    }


    private fun byteArraySteamOutput() {
        val byteArray = byteArrayOf(1, 2, 3, 5, 6, 6, 6)
        val outputStream = ByteArrayOutputStream()
        outputStream.write(byteArray)
        outputStream.writeTo(FileOutputStream(File("byteOutFile.txt")))
        outputStream.flush()
        outputStream.close()
    }

    private fun byteArraySteamInput() {
        val fis = FileInputStream(File("byteOutFile.txt"))
        val byteArray = fis.readBytes()
        byteArray.forEach {
            println(it)
        }
        fis.close()
    }

    /**
     * 字符写出
     */
    private fun charOutput() {
        //原始用法
//        val writer = OutputStreamWriter(
//            FileOutputStream(File("charFile.txt")), Charset.defaultCharset()
//        )
//        writer.write("祖国你好！！")
//        writer.write("希望你更好！！")
//        writer.flush()
//        writer.close()

        //jdk 进行分装 通过FileWriter可以更加方便使用字节流读取和写入
        val f = FileWriter("charFile.txt")
        f.write("在你胸口比划一个郭富城")
        f.write("\n")
        f.write("左边右边摇摇头")
        f.flush()
        f.close()
    }

    /**
     * 字符读入
     */
    private fun charInput() {
//        val input = InputStreamReader(
//            FileInputStream("charFile.txt"), Charset.defaultCharset()
//        )
//        val readLines = input.readLines()
//        readLines.forEach {
//            println(it)
//        }
//        input.close()

        //进阶使用
        val r  = FileReader("charFile.txt")
        r.readLines().forEach {
            println(it)
        }
        r.close()
    }


}