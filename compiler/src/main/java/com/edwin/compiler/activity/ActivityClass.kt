package com.edwin.compiler.activity

import com.edwin.annotations.Builder
import com.edwin.annotations.PendingTransition
import com.edwin.annotations.ResultEntity
import com.edwin.compiler.basic.BasicClass
import com.edwin.compiler.basic.entity.ResultParameter
import com.edwin.compiler.basic.entity.asResultParameter
import com.edwin.compiler.utils.isDefault
import java.util.*
import javax.lang.model.element.TypeElement

/**
 * Created by benny on 1/29/18.
 */
class ActivityClass
private constructor(type: TypeElement, builder: Builder) : BasicClass(type, builder) {

    companion object {
        private val activityClassCache = WeakHashMap<TypeElement, ActivityClass>()

        fun getOrNull(typeElement: TypeElement): ActivityClass? {
            var activityClass = activityClassCache[typeElement]
            if (activityClass == null) {
                val builder = typeElement.getAnnotation(Builder::class.java) ?: return null
                activityClass = ActivityClass(typeElement, builder)
                activityClassCache[typeElement] = activityClass
            }
            return activityClass
        }

        fun create(typeElement: TypeElement): ActivityClass {
            return activityClassCache.getOrPut(typeElement) {
                ActivityClass(typeElement, typeElement.getAnnotation(Builder::class.java))
            }
        }
    }

    private val declaredPendingTransition: PendingTransition
    private val declaredPendingTransitionOnFinish: PendingTransition
    private val declaredCategories = ArrayList<String>()
    private val declaredFlags = ArrayList<Int>()
    private val declaredResultParameters = TreeSet<ResultParameter>()

    val builder = ActivityClassBuilder(this)

    init {
        declaredFlags.addAll(builder.flags.asList())
        declaredCategories.addAll(builder.categories)

        declaredPendingTransition = builder.pendingTransition
        declaredPendingTransitionOnFinish = builder.pendingTransitionOnFinish

        if (builder.resultTypes.isNotEmpty()) {
            builder.resultTypes.mapTo(declaredResultParameters, ResultEntity::asResultParameter)
        }
    }

    var pendingTransition = declaredPendingTransition
        private set

    var pendingTransitionOnFinish = declaredPendingTransitionOnFinish
        private set

    val categories = ArrayList(declaredCategories)

    val flags = ArrayList(declaredFlags)

    val resultParameters = TreeSet(declaredResultParameters)

    val hasResult: Boolean
        get() = resultParameters.isNotEmpty()

    init {
        val superActivityClass = superClass as? ActivityClass
        if (superActivityClass != null) {
            categories += superActivityClass.categories
            flags += superActivityClass.flags
            resultParameters.addAll(superActivityClass.resultParameters)

            if (pendingTransition.isDefault()) pendingTransition =
                superActivityClass.pendingTransition
            if (pendingTransitionOnFinish.isDefault()) pendingTransitionOnFinish =
                superActivityClass.pendingTransitionOnFinish
        }
    }

    override fun createSuperClass(superClassElement: TypeElement) = getOrNull(superClassElement)
}
