package com.edwin.compiler.activity.builder

import com.edwin.compiler.activity.ActivityClass
import com.edwin.compiler.basic.builder.BasicSaveStateMethodBuilder
import com.edwin.compiler.basic.types.ACTIVITY

/**
 * Created by benny on 1/31/18.
 */
class SaveStateMethodBuilder(activityClass: ActivityClass) :
    BasicSaveStateMethodBuilder(activityClass) {

    override val instanceType = ACTIVITY.java

}
