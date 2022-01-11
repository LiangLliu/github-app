package com.edwin.compiler.activity.entity

import com.edwin.compiler.activity.ActivityClass
import com.edwin.compiler.basic.types.BUNDLE
import com.edwin.compiler.basic.types.ON_ACTIVITY_RESULT_LISTENER
import com.edwin.compiler.basic.types.RUNTIME_UTILS
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import java.util.*
import javax.lang.model.element.Modifier

class JavaOnResultListener(private val activityClass: ActivityClass) {

    /**
     * @return literal name like "onSampleActivityResultListener"
     */
    val name = "on" + activityClass.simpleName + "ResultListener"

    val typeName = ClassName.get(
        activityClass.packageName, activityClass.builderClassName,
        "On" + activityClass.simpleName + "ResultListener"
    )

    fun buildInterface(): TypeSpec {
        val interfaceOnResultMethodBuilder = MethodSpec.methodBuilder("onResult")
            .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
            .returns(TypeName.VOID)

        activityClass.resultParameters.forEach { resultParameter ->
            interfaceOnResultMethodBuilder.addParameter(
                resultParameter.javaTypeName,
                resultParameter.name
            )
        }

        return TypeSpec.interfaceBuilder(typeName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addMethod(interfaceOnResultMethodBuilder.build())
            .build()
    }

    fun buildObject(): TypeSpec {
        val onResultMethodBuilder = MethodSpec.methodBuilder("onResult")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(BUNDLE.java, "bundle")
            .returns(TypeName.VOID)

        onResultMethodBuilder.beginControlFlow("if(\$L != null)", name)
        val statementBuilder = StringBuilder()

        val args = ArrayList<Any>()
        args.add(name)
        activityClass.resultParameters.forEach { resultEntity ->
            statementBuilder.append("\$T.<\$T>get(bundle, \$S),")
            args.add(RUNTIME_UTILS.java)
            args.add(resultEntity.javaTypeName.box())
            args.add(resultEntity.name)
        }
        if (statementBuilder.isNotEmpty()) {
            statementBuilder.deleteCharAt(statementBuilder.length - 1)
        }
        onResultMethodBuilder.addStatement(
            "\$L.onResult($statementBuilder)",
            *args.toTypedArray()
        )
        onResultMethodBuilder.endControlFlow()

        return TypeSpec.anonymousClassBuilder(name)
            .addSuperinterface(ON_ACTIVITY_RESULT_LISTENER.java)
            .addMethod(onResultMethodBuilder.build())
            .build()
    }
}