package com.akame.akameproject.io

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object IODemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val filePath = "app/src/test/FirstFile.txt"
        createFile(filePath)
        copyText(filePath)
    }

    private fun createFile(filePath:String){
        val file = File(filePath)
        //如果文件存在 则删除
        if (file.exists()){
            return
        }
        //判断父文件夹是否存在
        if (file.parentFile?.exists()!=true){
            //如果不存在。则创建
            file.parentFile?.mkdirs()
        }
        //创建文件
        file.createNewFile()
    }

    private fun copyText(filePath: String){
        //创建文件输入流
        val inputStream = FileInputStream(File("app/src/test/DNF.txt"))
        //定义拷贝大小
        val byte = ByteArray(1024)
        //拷贝长度
        var length:Int
        //定义文件输出流
        val outputStream = FileOutputStream(filePath)
        //遍历文件边读边写
        while ((inputStream.read(byte).also { length = it})!=-1){
            //输入流写入 第一个是写入数据 第二个参数是否需要数据偏移。第三个是写入长度
            outputStream.write(byte,0,length)
        }
        //输入流关闭
        inputStream.close()
        //输出流关闭
        outputStream.close()
    }
}