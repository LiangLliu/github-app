package com.edwin.compiler.basic.entity

import com.edwin.annotations.ResultEntity
import com.edwin.aptutils.types.asJavaTypeName
import com.edwin.aptutils.types.asKotlinTypeName
import com.edwin.aptutils.types.asTypeMirror
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror
import com.squareup.javapoet.TypeName as JavaTypeName
import com.squareup.kotlinpoet.TypeName as KotlinTypeName

class ResultParameter(val name: String, val type: TypeMirror) : Comparable<ResultParameter> {

    val javaTypeName: JavaTypeName by lazy { type.asJavaTypeName() }

    val kotlinTypeName: KotlinTypeName by lazy { type.asKotlinTypeName() }

    override fun compareTo(other: ResultParameter) = name.compareTo(other.name)
}

fun ResultEntity.asResultParameter() = ResultParameter(name, resultType)

val ResultEntity.resultType: TypeMirror
    get() = try {
        type.asTypeMirror()
    } catch (e: MirroredTypeException) {
        e.typeMirror
    }