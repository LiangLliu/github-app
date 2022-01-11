package com.edwin.compiler.fragment

import com.edwin.annotations.Builder
import com.edwin.compiler.basic.BasicClass
import java.util.*
import javax.lang.model.element.TypeElement

/**
 * Created by benny on 1/29/18.
 */
class FragmentClass
private constructor(type: TypeElement, builder: Builder) : BasicClass(type, builder) {

    companion object {
        private val fragmentClassCache = WeakHashMap<TypeElement, FragmentClass>()

        fun getOrNull(typeElement: TypeElement): FragmentClass? {
            var fragmentClass = fragmentClassCache[typeElement]
            if (fragmentClass == null) {
                val builder = typeElement.getAnnotation(Builder::class.java) ?: return null
                fragmentClass = FragmentClass(typeElement, builder)
                fragmentClassCache[typeElement] = fragmentClass
            }
            return fragmentClass
        }

        fun create(typeElement: TypeElement): FragmentClass {
            return fragmentClassCache.getOrPut(typeElement) {
                FragmentClass(typeElement, typeElement.getAnnotation(Builder::class.java))
            }
        }
    }

    val builder = FragmentClassBuilder(this)

    override fun createSuperClass(superClassElement: TypeElement) = getOrNull(superClassElement)
}
