package org.example;

import org.example.exceptions.RuntimeError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnvironmentTest {

    @Test
    void testDefineAndGetVariable() {
        Environment env = new Environment(null);
        env.define("x", 42);

        assertEquals(42, env.get("x"));
    }

    @Test
    void testDefineDuplicateVariableThrowsError() {
        Environment env = new Environment(null);
        env.define("x", 42);

        RuntimeError exception = assertThrows(RuntimeError.class, () -> env.define("x", 100));
        assertTrue(exception.getMessage().contains("Cette variable est déjà définie"));
        assertTrue(exception.getMessage().contains("'x'"));
    }

    @Test
    void testGetVariableFromEnclosingEnvironment() {
        Environment parent = new Environment(null);
        parent.define("y", "hello");

        Environment child = new Environment(parent);

        assertEquals("hello", child.get("y"));
    }

    @Test
    void testGetUndefinedVariableThrowsError() {
        Environment env = new Environment(null);

        RuntimeError exception = assertThrows(RuntimeError.class, () -> env.get("z"));
        assertTrue(exception.getMessage().contains("Cette variable n'existe pas"));
        assertTrue(exception.getMessage().contains("'z'"));
    }

    @Test
    void testAssignExistingVariable() {
        Environment env = new Environment(null);
        env.define("x", 42);

        env.assign("x", 100);

        assertEquals(100, env.get("x"));
    }

    @Test
    void testAssignVariableInEnclosingEnvironment() {
        Environment parent = new Environment(null);
        parent.define("x", 42);

        Environment child = new Environment(parent);
        child.assign("x", 99);

        assertEquals(99, parent.get("x"));
    }

    @Test
    void testAssignUndefinedVariableThrowsError() {
        Environment env = new Environment(null);

        RuntimeError exception = assertThrows(RuntimeError.class, () -> env.assign("x", 10));
        assertTrue(exception.getMessage().contains("Cette variable n'est pas définie"));
        assertTrue(exception.getMessage().contains("'x'"));
    }
}
