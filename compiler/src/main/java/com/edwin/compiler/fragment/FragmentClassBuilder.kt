package com.edwin.compiler.fragment

import com.edwin.compiler.basic.BasicClassBuilder
import com.edwin.compiler.basic.builder.FieldBuilder
import com.edwin.compiler.fragment.builder.*
import com.squareup.javapoet.TypeSpec.Builder
import com.squareup.kotlinpoet.FileSpec

class FragmentClassBuilder(private val fragmentClass: FragmentClass)
    : BasicClassBuilder(fragmentClass) {

    override fun buildCommon(typeBuilder: Builder) {
        ConstantBuilder(fragmentClass).build(typeBuilder)
        FieldBuilder(fragmentClass).build(typeBuilder)
        InjectMethodBuilder(fragmentClass).build(typeBuilder)
        SaveStateMethodBuilder(fragmentClass).build(typeBuilder)
    }

    override fun buildKotlinBuilders(fileBuilder: FileSpec.Builder) {
        ReplaceKFunctionBuilder(fragmentClass).build(fileBuilder)
        AddKFunctionBuilder(fragmentClass).build(fileBuilder)
    }

    override fun buildJavaBuilders(typeBuilder: Builder) {
        ReplaceMethodBuilder(fragmentClass).build(typeBuilder)
        AddMethodBuilder(fragmentClass).build(typeBuilder)
    }
}