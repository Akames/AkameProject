package com.akame.akameproject.gson

import com.google.gson.*
import dalvik.system.PathClassLoader
import java.lang.Exception
import java.lang.reflect.Type

object GsonAnalyze {

    @JvmStatic
    fun main(args: Array<String>) {
//        createJson()
        analyzeJson()

    }

    /**
     * {
    "name": "张三",
    "age": 18
    }
     */
    private fun createJson() {
        val prettyTest = PrettyTest("张三", 18)
        //通过gsonBuilder 格式化输出json
        val gson = GsonBuilder().setPrettyPrinting().create()
        val json = gson.toJson(prettyTest)
        println(json)
    }


    private fun analyzeJson() {
        val json = """
            {
              "name": "张三",
              "age": "呵呵"
            }
        """.trimIndent()

        val gson = GsonBuilder().registerTypeAdapter(Int::class.java, CustomIntAdapter())
            .create()

        val result = gson.fromJson(json, PrettyTest::class.java)
        println(result.age)
    }

    /**
     * 创建一个解析器来处理字段异常情况
     */
    class CustomIntAdapter : JsonDeserializer<Int> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Int {
            //当解析字段出现为Int时候 如果为null 或者 为字符串的时候 返回0
            return try {
                json?.asInt ?: 0
            } catch (e: Exception) {
                0
            }
        }
    }

    data class PrettyTest(val name: String, val age: Int)
}