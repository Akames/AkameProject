package com.akame.compile

import com.akame.ak_annotation.ARouter
import com.google.auto.service.AutoService
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import java.lang.Exception
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

//注册
@AutoService(Processor::class)
// 指定JDK编译版本
@SupportedSourceVersion(SourceVersion.RELEASE_8)
// 允许/支持的注解类型，让注解处理器处理
@SupportedAnnotationTypes(ProcessorConfig.AROUTER_PACKAGE)
// 注解处理器接收的参数
@SupportedOptions(ProcessorConfig.OPTIONS, ProcessorConfig.APT_PACKAGE)
class ARouterProcessor : AbstractProcessor() {
    // 操作Element的工具类（类，函数，属性，其实都是Element）
    private var elementTool: Elements? = null

    // type(类信息)的工具类，包含用于操作TypeMirror的工具方法
    private val typeTool: Types? = null

    // Message用来打印 日志相关信息
    private var messager: Messager? = null

    // 文件生成器， 类 资源 等，就是最终要生成的文件 是需要Filer来完成的
    private var filer: Filer? = null

    private val options: String? = null

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        elementTool = processingEnv.elementUtils
        messager = processingEnv.messager
        filer = processingEnv.filer
        messager?.printMessage(
            Diagnostic.Kind.NOTE,
            "${ProcessorConfig.OPTIONS} : ${processingEnv.options[ProcessorConfig.OPTIONS]}"
        )
        messager?.printMessage(
            Diagnostic.Kind.NOTE,
            "${ProcessorConfig.APT_PACKAGE} : ${processingEnv.options[ProcessorConfig.APT_PACKAGE]}"
        )
    }


    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        if (annotations?.isEmpty() != false) {
            return false
        }
        //找到所有引用了ARouter类的集合
        val elementSet = roundEnv?.getElementsAnnotatedWith(ARouter::class.java)
        elementSet?.forEach {
            messager?.printMessage(Diagnostic.Kind.NOTE, "className -> :${it.simpleName}")
        }

        val callType = elementTool?.getTypeElement("android.app.Activity")
        callType?.asType()?.apply { //获取类自描述信息
            messager?.printMessage(Diagnostic.Kind.NOTE, this.kind.name)
        }

        /**
         * package com.example.helloworld;

        public final class HelloWorld {
        public static void main(String[] args) {
        System.out.println("Hello, JavaPoet!");
        }
        }
         */
        val mainMethod = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(TypeName.VOID)
            .addParameter(Array<String>::class.java, "args")
            .addStatement("${'$'}T.out.println(${'$'}S)", System::class.java, "Hello javaPoet")
            .build()

        val helloWorldCls = TypeSpec.classBuilder("HelloWorld")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(mainMethod)
            .build()

        val javaFile = JavaFile.builder("com.akame.javapoet", helloWorldCls)
            .build()
        try {
            javaFile.writeTo(filer)
        } catch (e: Exception) {
            messager?.printMessage(Diagnostic.Kind.NOTE, "create java poet error : ${e.message}")
        }
        //false：表示执行完成不再执行 true表示执行完毕
        return false
    }
}