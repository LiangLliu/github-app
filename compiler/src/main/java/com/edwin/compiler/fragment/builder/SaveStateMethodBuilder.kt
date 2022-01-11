package com.edwin.compiler.fragment.builder

import com.edwin.compiler.basic.builder.BasicSaveStateMethodBuilder
import com.edwin.compiler.basic.types.FRAGMENT
import com.edwin.compiler.fragment.FragmentClass

/**
 * Created by benny on 1/31/18.
 */
class SaveStateMethodBuilder(fragmentClass: FragmentClass) :
    BasicSaveStateMethodBuilder(fragmentClass) {

    override val instanceType = FRAGMENT.java

}
