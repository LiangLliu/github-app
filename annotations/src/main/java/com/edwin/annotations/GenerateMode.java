package com.edwin.annotations;

public enum GenerateMode {
    /**
     * Generate Java utility method for Java developers only.
     */
    JavaOnly,

    /**
     * Generate Kotlin utility functions for Kotlin developers only.
     */
    KotlinOnly,

    Both,

    /**
     * Generate methods according to the source file.
     *
     * When source is Java file, generate java method, while source is Kotlin file, generate both.
     */
    Auto
}
