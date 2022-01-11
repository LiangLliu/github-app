package com.edwin.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface ActivityBuilder {
    GenerateMode mode() default GenerateMode.Auto;

    ResultEntity[] resultTypes() default {};

    PendingTransition pendingTransition() default @PendingTransition;

    SharedElement[] sharedElements() default {};

    SharedElementByNames[] sharedElementsByNames() default {};

    SharedElementWithName[] sharedElementsWithName() default {};

    String[] categories() default {};

    int[] flags() default {};
}
