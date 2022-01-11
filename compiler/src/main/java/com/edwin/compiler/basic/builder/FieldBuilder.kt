package com.edwin.compiler.basic.builder

import com.edwin.compiler.basic.BasicClass
import com.edwin.compiler.basic.entity.OptionalField
import com.edwin.compiler.basic.types.INTENT
import com.squareup.javapoet.*
import javax.lang.model.element.Modifier.*

class FieldBuilder(private val basicClass: BasicClass) {

    fun build(typeBuilder: TypeSpec.Builder) {
        val builderClassTypeName =
            ClassName.get(basicClass.packageName, basicClass.builderClassName)

        val groupedFields = basicClass.fields.groupBy { it is OptionalField }
        val requiredFields = groupedFields[false] ?: emptyList()
        val optionalFields = groupedFields[true] ?: emptyList()

        typeBuilder.addMethod(MethodSpec.constructorBuilder().addModifiers(PRIVATE).build())

        val createBuilderMethodBuilder = MethodSpec.methodBuilder("builder")
            .addModifiers(PUBLIC, STATIC)
            .returns(builderClassTypeName)
            .addStatement("\$T builder = new \$T()", builderClassTypeName, builderClassTypeName)

        val fillIntentMethodBuilder = MethodSpec.methodBuilder("fillIntent")
            .addModifiers(PRIVATE)
            .addParameter(INTENT.java, "intent")

        requiredFields.forEach { field ->
            //field
            typeBuilder.addField(FieldSpec.builder(field.asTypeName(), field.name, PRIVATE).build())
            //fillIntent
            fillIntentMethodBuilder.addStatement(
                "intent.putExtra(\$S, \$L)",
                field.name,
                field.name
            )
            //constructor
            createBuilderMethodBuilder.addParameter(
                ParameterSpec.builder(
                    field.asTypeName(),
                    field.name
                ).build()
            )
                .addStatement("builder.\$L = \$L", field.name, field.name)
        }

        optionalFields.forEach { field ->
            //field
            typeBuilder.addField(FieldSpec.builder(field.asTypeName(), field.name, PRIVATE).build())
            //setter
            typeBuilder.addMethod(
                MethodSpec.methodBuilder(field.name)
                    .addModifiers(PUBLIC)
                    .addParameter(field.asTypeName(), field.name)
                    .addStatement("this.${field.name} = ${field.name}")
                    .addStatement("return this")
                    .returns(builderClassTypeName)
                    .build()
            )
            //fillIntent
            if (field.isPrimitive) {
                fillIntentMethodBuilder.addStatement(
                    "intent.putExtra(\$S, \$L)",
                    field.name,
                    field.name
                )
            } else {
                fillIntentMethodBuilder
                    .beginControlFlow("if(\$L != null)", field.name)
                    .addStatement("intent.putExtra(\$S, \$L)", field.name, field.name)
                    .endControlFlow()
            }
        }

        typeBuilder.addMethod(createBuilderMethodBuilder.addStatement("return builder").build())
        typeBuilder.addMethod(fillIntentMethodBuilder.build())
    }

}