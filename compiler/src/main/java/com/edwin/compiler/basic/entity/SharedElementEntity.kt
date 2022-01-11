package com.edwin.compiler.basic.entity

import com.edwin.annotations.SharedElement
import com.edwin.annotations.SharedElementByNames
import com.edwin.annotations.SharedElementWithName

class SharedElementEntity(
    val targetName: String,
    val sourceId: Int = 0,
    val sourceName: String? = null
) {

    constructor(sharedElement: SharedElement) : this(
        sharedElement.targetName,
        sourceId = sharedElement.sourceId
    )

    constructor(sharedElement: SharedElementWithName) : this(
        sharedElement.value,
        sourceName = sharedElement.value
    )

    constructor(sharedElement: SharedElementByNames) : this(
        sharedElement.target,
        sourceName = sharedElement.source
    )

}
