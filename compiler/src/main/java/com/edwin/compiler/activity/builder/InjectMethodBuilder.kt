package com.edwin.compiler.activity.builder

import com.edwin.compiler.activity.ActivityClass
import com.edwin.compiler.basic.builder.BasicInjectMethodBuilder
import com.edwin.compiler.basic.types.ACTIVITY

/**
 * Created by benny on 1/31/18.
 */

class InjectMethodBuilder(activityClass: ActivityClass) : BasicInjectMethodBuilder(activityClass) {

    override val instanceType = ACTIVITY.java

    override val snippetToRetrieveState =
        "typedInstance.getIntent().getExtras() : savedInstanceState"

}
