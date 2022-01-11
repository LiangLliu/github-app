package com.edwin.compiler.basic.entity

import com.edwin.annotations.Optional
import com.edwin.aptutils.types.isSameTypeWith
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeKind

class OptionalField(element: VariableElement, optional: Optional) : Field(element) {

    var defaultValue: Any? = null
        private set

    override val prefix = "OPTIONAL_"

    init {
        val fieldType = element.asType()
        defaultValue = when (fieldType.kind) {
            TypeKind.BOOLEAN -> optional.booleanValue
            TypeKind.CHAR -> "'${optional.charValue}'"
            TypeKind.BYTE -> "(byte) ${optional.byteValue}"
            TypeKind.SHORT -> "(short) ${optional.shortValue}"
            TypeKind.INT -> optional.intValue
            TypeKind.LONG -> "${optional.longValue}L"
            TypeKind.FLOAT -> "${optional.floatValue}f"
            TypeKind.DOUBLE -> optional.doubleValue
            else -> if (fieldType.isSameTypeWith(String::class)) {
                //注意字面量的引号
                """"${optional.stringValue}""""
            } else {
                null
            }
        }
    }

    override fun asKotlinTypeName() = super.asKotlinTypeName()

    override fun compareTo(other: Field): Int {
        return if (other is OptionalField) {
            super.compareTo(other)
        } else {
            //如果与 RequiredField 比较，Optional 永远排在后面
            1
        }
    }
}
