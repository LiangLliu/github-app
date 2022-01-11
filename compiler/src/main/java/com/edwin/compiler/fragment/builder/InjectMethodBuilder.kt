package com.edwin.compiler.fragment.builder

import com.edwin.compiler.basic.builder.BasicInjectMethodBuilder
import com.edwin.compiler.basic.types.FRAGMENT
import com.edwin.compiler.fragment.FragmentClass

class InjectMethodBuilder(private val fragmentClass: FragmentClass) :
    BasicInjectMethodBuilder(fragmentClass) {

    override val instanceType = FRAGMENT.java

    override val snippetToRetrieveState = "typedInstance.getArguments() : savedInstanceState"
}
