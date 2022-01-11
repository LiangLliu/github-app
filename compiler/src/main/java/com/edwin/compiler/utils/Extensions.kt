package com.edwin.compiler.utils

import com.edwin.annotations.PendingTransition

fun PendingTransition.isDefault(): Boolean {
    return enterAnim == PendingTransition.DEFAULT && exitAnim == PendingTransition.DEFAULT
}
