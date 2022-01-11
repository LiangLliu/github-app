package com.edwin.compiler.basic.builder

import com.edwin.compiler.basic.BasicClass
import com.edwin.compiler.basic.types.BUNDLE
import com.edwin.compiler.basic.types.INTENT
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import java.util.*
import javax.lang.model.element.Modifier

abstract class BasicSaveStateMethodBuilder(val basicClass: BasicClass) {

    abstract val instanceType: TypeName

    fun build(typeBuilder: TypeSpec.Builder) {
        val methodBuilder = MethodSpec.methodBuilder("saveState")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(TypeName.VOID)
            .addParameter(instanceType, "instance")
            .addParameter(BUNDLE.java, "outState")
            .beginControlFlow("if(instance instanceof \$T)", basicClass.typeElement)
            .addStatement(
                "\$T typedInstance = (\$T) instance",
                basicClass.typeElement,
                basicClass.typeElement
            )

        methodBuilder.addStatement("\$T intent = new \$T()", INTENT.java, INTENT.java)

        for (requiredField in basicClass.fields) {
            val name = requiredField.name
            if (requiredField.isPrivate) {
                methodBuilder.addStatement(
                    "intent.putExtra(\$S, typedInstance.get\$L())",
                    name,
                    name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                        else it.toString()
                    }
                )
            } else {
                methodBuilder.addStatement("intent.putExtra(\$S, typedInstance.\$L)", name, name)
            }
        }

        methodBuilder.addStatement("outState.putAll(intent.getExtras())").endControlFlow()

        typeBuilder.addMethod(methodBuilder.build())
    }

}