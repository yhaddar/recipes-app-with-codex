package com.recipemanager;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class JavaRuntimeVersionTest {

    @Test
    void runsOnJava25() {
        assertThat(Runtime.version().feature()).isEqualTo(25);
    }
}
