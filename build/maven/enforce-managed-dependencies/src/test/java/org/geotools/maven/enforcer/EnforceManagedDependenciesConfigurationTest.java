/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.maven.enforcer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** Unit tests for {@link EnforceManagedDependencies} configuration properties. */
public class EnforceManagedDependenciesConfigurationTest {

    @Mock
    private MavenSession session;

    @Mock
    private MavenProject project;

    private EnforceManagedDependencies rule;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(session.getCurrentProject()).thenReturn(project);
        when(session.getProjects()).thenReturn(List.of(project));
        rule = new EnforceManagedDependencies(session);
    }

    @Test
    public void testDefaultConfiguration() {
        // Test default values
        assertTrue("Default failOnViolation should be true", rule.isFailOnViolation());
        assertFalse("Default allowSnapshots should be false", rule.isAllowSnapshots());
        assertNull("Default message should be null", rule.getMessage());
        assertTrue("Default excludes should be empty", rule.getExcludes().isEmpty());
    }

    @Test
    public void testFailOnViolationConfiguration() {
        // Test failOnViolation property
        rule.setFailOnViolation(false);
        assertFalse("FailOnViolation should be false after setting", rule.isFailOnViolation());

        rule.setFailOnViolation(true);
        assertTrue("FailOnViolation should be true after setting", rule.isFailOnViolation());
    }

    @Test
    public void testAllowSnapshotsConfiguration() {
        // Test allowSnapshots property
        rule.setAllowSnapshots(true);
        assertTrue("AllowSnapshots should be true after setting", rule.isAllowSnapshots());

        rule.setAllowSnapshots(false);
        assertFalse("AllowSnapshots should be false after setting", rule.isAllowSnapshots());
    }

    @Test
    public void testMessageConfiguration() {
        // Test message property
        String testMessage = "Test custom message";
        rule.setMessage(testMessage);
        assertEquals("Message should match what was set", testMessage, rule.getMessage());

        rule.setMessage(null);
        assertNull("Message should be null after setting to null", rule.getMessage());

        rule.setMessage("");
        assertEquals("Message should be empty string", "", rule.getMessage());
    }

    @Test
    public void testExcludesConfiguration() {
        // Test excludes property with various scenarios
        assertTrue("Initial excludes should be empty", rule.getExcludes().isEmpty());

        // Test setting excludes
        rule.setExcludes(Arrays.asList("org.example:test"));
        assertEquals("Excludes should contain one item", 1, rule.getExcludes().size());
        assertEquals(
                "Exclude should match", "org.example:test", rule.getExcludes().get(0));

        // Test multiple excludes
        rule.setExcludes(Arrays.asList("org.example:test1", "org.example:test2"));
        assertEquals("Excludes should contain two items", 2, rule.getExcludes().size());
        assertTrue("Should contain first exclude", rule.getExcludes().contains("org.example:test1"));
        assertTrue("Should contain second exclude", rule.getExcludes().contains("org.example:test2"));

        // Test setting to null
        rule.setExcludes(null);
        assertTrue(
                "Excludes should be empty after setting to null",
                rule.getExcludes().isEmpty());

        // Test setting to empty list
        rule.setExcludes(Collections.emptyList());
        assertTrue(
                "Excludes should be empty after setting to empty list",
                rule.getExcludes().isEmpty());
    }

    @Test
    public void testExcludesImmutability() {
        // Test that returned excludes list is immutable
        rule.setExcludes(Arrays.asList("org.example:test"));

        try {
            rule.getExcludes().add("org.example:new");
            fail("Should not be able to modify returned excludes list");
        } catch (UnsupportedOperationException e) {
            // Expected behavior
        }
    }

    @Test
    public void testExcludesDefensiveCopy() {
        // Test that setting excludes creates a defensive copy
        java.util.List<String> originalList = new java.util.ArrayList<>();
        originalList.add("org.example:test");

        rule.setExcludes(originalList);

        // Modify original list
        originalList.add("org.example:new");

        // Rule's excludes should not be affected
        assertEquals(
                "Rule excludes should not be affected by changes to original list",
                1,
                rule.getExcludes().size());
        assertEquals(
                "Rule should still contain original exclude",
                "org.example:test",
                rule.getExcludes().get(0));
    }

    @Test
    public void testConfigurationChaining() {
        // Test that configuration methods can be chained (if they return void, this just tests they work)
        rule.setFailOnViolation(false);
        rule.setAllowSnapshots(true);
        rule.setMessage("Test message");
        rule.setExcludes(Arrays.asList("org.example:test"));

        // Verify all configurations are set
        assertFalse("FailOnViolation should be false", rule.isFailOnViolation());
        assertTrue("AllowSnapshots should be true", rule.isAllowSnapshots());
        assertEquals("Message should be set", "Test message", rule.getMessage());
        assertEquals("Excludes should be set", 1, rule.getExcludes().size());
    }

    @Test
    public void testExcludePatterns() {
        // Test various exclude pattern formats that the rule should support
        rule.setExcludes(Arrays.asList(
                "org.example:artifact", // groupId:artifactId
                "org.example:artifact:jar", // groupId:artifactId:type
                "org.example:artifact:jar:tests", // groupId:artifactId:type:classifier
                "org.example:*" // wildcard pattern
                ));

        assertEquals("Should have 4 exclude patterns", 4, rule.getExcludes().size());
        assertTrue("Should contain basic pattern", rule.getExcludes().contains("org.example:artifact"));
        assertTrue("Should contain type pattern", rule.getExcludes().contains("org.example:artifact:jar"));
        assertTrue("Should contain classifier pattern", rule.getExcludes().contains("org.example:artifact:jar:tests"));
        assertTrue("Should contain wildcard pattern", rule.getExcludes().contains("org.example:*"));
    }
}
