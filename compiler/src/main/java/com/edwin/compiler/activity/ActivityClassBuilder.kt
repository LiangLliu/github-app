package com.edwin.compiler.activity

import com.edwin.compiler.activity.builder.*
import com.edwin.compiler.basic.BasicClassBuilder
import com.edwin.compiler.basic.builder.FieldBuilder
import com.squareup.javapoet.TypeSpec.Builder
import com.squareup.kotlinpoet.FileSpec

class ActivityClassBuilder(private val activityClass: ActivityClass) :
    BasicClassBuilder(activityClass) {

    override fun buildCommon(typeBuilder: Builder) {
        ConstantBuilder(activityClass).build(typeBuilder)
        FieldBuilder(activityClass).build(typeBuilder)
        InjectMethodBuilder(activityClass).build(typeBuilder)
        SaveStateMethodBuilder(activityClass).build(typeBuilder)
        NewIntentMethodBuilder(activityClass).build(typeBuilder)
    }

    override fun buildKotlinBuilders(fileBuilder: FileSpec.Builder) {
        StartKFunctionBuilder(activityClass).build(fileBuilder)
        FinishKFunctionBuilder(activityClass).build(fileBuilder)
        NewIntentKFunctionBuilder(activityClass).build(fileBuilder)
    }

    override fun buildJavaBuilders(typeBuilder: Builder) {
        StartMethodBuilder(activityClass, METHOD_NAME).build(typeBuilder)
        FinishMethodBuilder(activityClass).build(typeBuilder)
    }

    companion object {
        const val METHOD_NAME = "start"
        const val CONSTS_RESULT_PREFIX = "RESULT_"
    }
}