package com.edwin.compiler.fragment.builder

import com.edwin.aptutils.types.asKotlinTypeName
import com.edwin.compiler.basic.entity.OptionalField
import com.edwin.compiler.basic.types.*

import com.edwin.compiler.fragment.FragmentClass
import com.edwin.compiler.fragment.builder.Op.ADD
import com.edwin.compiler.fragment.builder.Op.REPLACE
import com.squareup.kotlinpoet.*

abstract class StartFragmentKFunctionBuilder(private val fragmentClass: FragmentClass) {

    abstract val name: String
    abstract val op: Op

    open fun build(fileBuilder: FileSpec.Builder) {
        val isReplace = op == REPLACE
        val returnType = fragmentClass.typeElement.asType().asKotlinTypeName()
        val funBuilderOfContext = FunSpec.builder(name)
            .receiver(FRAGMENT_ACTIVITY.kotlin)
            .addModifiers(KModifier.PUBLIC)
            .returns(returnType)
            .addParameter("containerId", INT)
            .addStatement("%T.INSTANCE.init(this)", ACTIVITY_BUILDER.kotlin)
            .addStatement("val intent = %T()", INTENT.kotlin)

        for (field in fragmentClass.fields) {
            val name = field.name
            val className = field.asKotlinTypeName()
            if (field is OptionalField) {
                funBuilderOfContext.addParameter(
                    ParameterSpec.builder(name, className).defaultValue("null").build()
                )
            } else {
                funBuilderOfContext.addParameter(name, className)
            }
            funBuilderOfContext.addStatement("intent.putExtra(%S, %L)", name, name)
        }

        funBuilderOfContext.addParameter(
            ParameterSpec.builder("tag", com.edwin.compiler.basic.types.STRING.kotlin)
                .defaultValue("null").build()
        )

        val sharedElements = fragmentClass.sharedElements
        if (sharedElements.isEmpty()) {
            funBuilderOfContext.addStatement(
                "return %T.showFragment(this, %L, containerId, tag, intent.getExtras(), %T::class.java, null)",
                FRAGMENT_BUILDER.kotlin,
                isReplace,
                fragmentClass.typeElement
            )
        } else {
            funBuilderOfContext.addStatement(
                "val sharedElements = %T()",
                ARRAY_LIST[PAIR[com.edwin.compiler.basic.types.STRING, com.edwin.compiler.basic.types.STRING]].kotlin
            )
                .addStatement("val container: %T = findViewById(containerId)", VIEW.kotlin)
            for (sharedElement in sharedElements) {
                if (sharedElement.sourceName != null) {
                    funBuilderOfContext.addStatement(
                        "sharedElements.add(Pair(%S, %S))",
                        sharedElement.sourceName,
                        sharedElement.targetName
                    )
                } else {
                    funBuilderOfContext.addStatement(
                        "sharedElements.add(Pair(%T.getTransitionName(container.findViewById(%L)), %S))",
                        VIEW_COMPAT.kotlin,
                        sharedElement.sourceId,
                        sharedElement.targetName
                    )
                }
            }
            funBuilderOfContext.addStatement(
                "return %T.showFragment(this, %L, containerId, tag, intent.getExtras(), %T::class.java, sharedElements)",
                FRAGMENT_BUILDER.kotlin,
                isReplace,
                fragmentClass.typeElement
            )
        }


        val parameterSpecs = funBuilderOfContext.parameters.let { it.subList(1, it.size) }
        val parameterLiteral = parameterSpecs.joinToString { it.name }

        fileBuilder.addFunction(funBuilderOfContext.build())

        fileBuilder.addFunction(
            FunSpec.builder(name)
                .receiver(VIEW_GROUP.kotlin)
                .addModifiers(KModifier.PUBLIC)
                .returns(returnType)
                .addParameters(parameterSpecs)
                .addStatement(
                    "return (context as? %T)?.%L(id %L)",
                    FRAGMENT_ACTIVITY.kotlin,
                    name,
                    if (parameterLiteral.isBlank()) parameterLiteral else ", $parameterLiteral"
                ).build()
        )

        fileBuilder.addFunction(
            FunSpec.builder(name)
                .receiver(FRAGMENT.kotlin)
                .addModifiers(KModifier.PUBLIC)
                .returns(returnType)
                .addParameters(parameterSpecs)
                .addStatement(
                    "return (view?.parent as? %T)?.%L(%L)",
                    VIEW_GROUP.kotlin,
                    name,
                    parameterLiteral
                ).build()
        )

        when (op) {
            Op.ADD -> {
                fileBuilder.addFunction(
                    FunSpec.builder(name)
                        .receiver(FRAGMENT_ACTIVITY.kotlin)
                        .addModifiers(KModifier.PUBLIC)
                        .returns(returnType)
                        .addParameter("tag", com.edwin.compiler.basic.types.STRING.kotlin)
                        .addParameters(parameterSpecs.subList(0, parameterSpecs.size - 1))
                        .addParameter(
                            ParameterSpec.builder("containerId", INT).defaultValue("0").build()
                        )
                        .addStatement("return %L(containerId, %L)", name, parameterLiteral).build()
                )
            }
            Op.REPLACE -> {

            }
        }
    }
}

class ReplaceKFunctionBuilder(fragmentClass: FragmentClass) :
    StartFragmentKFunctionBuilder(fragmentClass) {
    override val name: String = "replace" + fragmentClass.simpleName
    override val op: Op = REPLACE
}

class AddKFunctionBuilder(fragmentClass: FragmentClass) :
    StartFragmentKFunctionBuilder(fragmentClass) {
    override val name: String = "add" + fragmentClass.simpleName
    override val op: Op = ADD
}
