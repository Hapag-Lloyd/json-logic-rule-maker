package com.hlag.rulemaker;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericUnitTest {
    @Test
    void shouldCreateJava11CompatibleClasses_whenCompile() throws Exception {
        // Replace with the class you want to test
        String className = getClass().getName();

        try (InputStream in = ClassLoader.getSystemResourceAsStream(className.replace('.', '/') + ".class")) {
            ClassReader classReader = new ClassReader(in);
            // Java 11 corresponds to major version 55
            assertEquals(55, classReader.readUnsignedShort(6));
        }
    }
}
