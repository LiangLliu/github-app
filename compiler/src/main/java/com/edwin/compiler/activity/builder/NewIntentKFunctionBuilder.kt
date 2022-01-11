package com.edwin.compiler.activity.builder

import com.edwin.compiler.activity.ActivityClass
import com.edwin.compiler.basic.types.INTENT
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.asClassName

class NewIntentKFunctionBuilder(private val activityClass: ActivityClass) {

    fun build(builder: FileSpec.Builder) {
        val newIntentFunBuilder = FunSpec.builder("processNewIntent")
            .receiver(activityClass.typeElement.asClassName())
            .addParameter("intent", INTENT.kotlin)
            .addParameter(
                ParameterSpec.builder("updateIntent", Boolean::class.java).defaultValue("true")
                    .build()
            )

        newIntentFunBuilder.addStatement(
            "%L.processNewIntent(this, intent, updateIntent)",
            activityClass.builderClassName
        )
        builder.addFunction(newIntentFunBuilder.build())
    }
}