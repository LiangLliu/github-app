package com.edwin.compiler

import com.edwin.aptutils.AptContext
import com.edwin.annotations.Builder
import com.edwin.annotations.Optional
import com.edwin.annotations.Required
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

/**
 * Created by benny on 10/2/16.
 */
class BuilderProcessor : AbstractProcessor() {

    private val supportedAnnotations =
        setOf(Builder::class.java, Required::class.java, Optional::class.java)

    @Synchronized
    override fun init(env: ProcessingEnvironment) {
        super.init(env)
        AptContext.init(env)
    }

    override fun getSupportedAnnotationTypes() =
        supportedAnnotations.map { it.canonicalName }.toSet()

    override fun getSupportedSourceVersion() = SourceVersion.RELEASE_8

    override fun process(annotations: Set<TypeElement>, env: RoundEnvironment): Boolean {
        if (annotations.isNotEmpty()) {
            ClassProcessor(AptContext.filer).process(env)
        }
        return true
    }
}
