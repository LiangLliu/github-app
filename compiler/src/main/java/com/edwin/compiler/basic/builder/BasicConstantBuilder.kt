package com.edwin.compiler.basic.builder

import com.edwin.compiler.basic.BasicClass
import com.edwin.compiler.utils.camelToUnderline
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

abstract class BasicConstantBuilder(private val basicClass: BasicClass) {
    open fun build(typeBuilder: TypeSpec.Builder) {
        basicClass.fields.forEach { field ->
            typeBuilder.addField(
                FieldSpec.builder(
                    String::class.java,
                    field.prefix + field.name.camelToUnderline(),
                    Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL
                )
                    .initializer("\$S", field.name)
                    .build()
            )
        }
    }
}