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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.apache.maven.enforcer.rule.api.EnforcerLogger;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** Unit tests for utility methods and edge cases in EnforceManagedDependencies. */
public class EnforceManagedDependenciesUtilityTest {

    @Mock
    private EnforcerLogger logger;

    @Mock
    private MavenSession session;

    @Mock
    private MavenProject project;

    private EnforceManagedDependencies rule;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");
        when(project.getOriginalModel()).thenReturn(null); // Fallback to getDependencies()

        when(session.getCurrentProject()).thenReturn(project);
        when(session.getProjects()).thenReturn(List.of(project));
        rule = new EnforceManagedDependencies(session);
        rule.setLog(logger);
    }

    @Test
    public void testDependencyKeyGeneration() throws Exception {
        // Test various dependency key formats

        // Basic dependency (groupId:artifactId)
        testDependencyKey("org.example", "artifact", null, null, "org.example:artifact");

        // Dependency with jar type (should not add :jar to key)
        testDependencyKey("org.example", "artifact", "jar", null, "org.example:artifact");

        // Dependency with non-jar type
        testDependencyKey("org.example", "artifact", "pom", null, "org.example:artifact:pom");

        // Dependency with classifier
        testDependencyKey("org.example", "artifact", null, "tests", "org.example:artifact:tests");

        // Dependency with type and classifier
        testDependencyKey("org.example", "artifact", "war", "sources", "org.example:artifact:war:sources");
    }

    private void testDependencyKey(
            String groupId, String artifactId, String type, String classifier, String expectedKey) throws Exception {
        // Create a dependency that should be excluded if it matches the expected key
        Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setType(type);
        dependency.setClassifier(classifier);
        dependency.setVersion("1.0.0");

        // Create a managed version for this dependency
        Dependency managedDep = new Dependency();
        managedDep.setGroupId(groupId);
        managedDep.setArtifactId(artifactId);
        managedDep.setType(type);
        managedDep.setClassifier(classifier);
        managedDep.setVersion("1.0.0");

        // Set up exclusion based on expected key
        rule.setExcludes(Arrays.asList(expectedKey));

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(createDependencyManagement(managedDep));

        // Execute rule - should not throw exception because dependency is excluded
        rule.execute();

        // Verify no warnings (dependency should be excluded)
        verify(logger, never()).warn(anyString());
    }

    @Test
    public void testDependencyMatching() throws Exception {
        // Test that dependencies match their managed versions correctly

        // Exact match
        testDependencyMatching("org.example", "artifact", "jar", null, "org.example", "artifact", "jar", null, true);

        // Type defaults to jar
        testDependencyMatching("org.example", "artifact", null, null, "org.example", "artifact", "jar", null, true);

        // Classifier defaults to empty
        testDependencyMatching("org.example", "artifact", "jar", null, "org.example", "artifact", "jar", "", true);

        // Different groupId
        testDependencyMatching("org.example", "artifact", "jar", null, "org.other", "artifact", "jar", null, false);

        // Different artifactId
        testDependencyMatching("org.example", "artifact", "jar", null, "org.example", "other", "jar", null, false);

        // Different type
        testDependencyMatching("org.example", "artifact", "war", null, "org.example", "artifact", "jar", null, false);

        // Different classifier
        testDependencyMatching(
                "org.example", "artifact", "jar", "tests", "org.example", "artifact", "jar", "sources", false);
    }

    private void testDependencyMatching(
            String depGroupId,
            String depArtifactId,
            String depType,
            String depClassifier,
            String mgmtGroupId,
            String mgmtArtifactId,
            String mgmtType,
            String mgmtClassifier,
            boolean shouldMatch)
            throws Exception {
        // Create dependency without version
        Dependency dependency = new Dependency();
        dependency.setGroupId(depGroupId);
        dependency.setArtifactId(depArtifactId);
        dependency.setType(depType);
        dependency.setClassifier(depClassifier);
        // No version - should be managed

        // Create managed dependency
        Dependency managedDep = new Dependency();
        managedDep.setGroupId(mgmtGroupId);
        managedDep.setArtifactId(mgmtArtifactId);
        managedDep.setType(mgmtType);
        managedDep.setClassifier(mgmtClassifier);
        managedDep.setVersion("1.0.0");

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(createDependencyManagement(managedDep));
        when(project.getParent()).thenReturn(null);

        if (shouldMatch) {
            // Should not throw exception - dependency is managed
            rule.execute();
            verify(logger, never()).warn(anyString());
        } else {
            // Should throw exception - dependency is not managed
            try {
                rule.execute();
                fail("Expected EnforcerRuleException for unmanaged dependency");
            } catch (org.apache.maven.enforcer.rule.api.EnforcerRuleException e) {
                assertTrue(
                        "Exception message should mention unmanaged dependency",
                        e.getMessage().contains("is not managed"));
            }
        }

        // Reset for next test
        reset(logger, project);
        rule.setExcludes(null);
        rule.setAllowSnapshots(false);
    }

    @Test
    public void testExcludePatternMatching() throws Exception {
        // Test different exclude patterns

        // Exact match: groupId:artifactId
        testExcludePattern("org.example:artifact", "org.example", "artifact", null, null, true);

        // Full pattern: groupId:artifactId:type:classifier
        testExcludePattern("org.example:artifact:jar:tests", "org.example", "artifact", "jar", "tests", true);

        // Partial match should not exclude
        testExcludePattern("org.example:other", "org.example", "artifact", null, null, false);

        // Prefix match using startsWith logic
        testExcludePattern("org.example", "org.example", "artifact", null, null, true);
    }

    private void testExcludePattern(
            String excludePattern,
            String groupId,
            String artifactId,
            String type,
            String classifier,
            boolean shouldExclude)
            throws Exception {
        Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setType(type);
        dependency.setClassifier(classifier);
        dependency.setVersion("1.0.0");

        // Create managed version to trigger violation
        Dependency managedDep = new Dependency();
        managedDep.setGroupId(groupId);
        managedDep.setArtifactId(artifactId);
        managedDep.setType(type);
        managedDep.setClassifier(classifier);
        managedDep.setVersion("1.0.0");

        rule.setExcludes(Arrays.asList(excludePattern));

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(createDependencyManagement(managedDep));

        if (shouldExclude) {
            // Should not throw exception - dependency is excluded
            rule.execute();
            verify(logger, never()).warn(anyString());
        } else {
            // Should throw exception - dependency is not excluded and has violation
            try {
                rule.execute();
                fail("Expected EnforcerRuleException for non-excluded dependency with violation");
            } catch (org.apache.maven.enforcer.rule.api.EnforcerRuleException e) {
                assertTrue(
                        "Exception message should mention inline version",
                        e.getMessage().contains("has inline version"));
            }
        }

        // Reset for next test
        reset(logger);
    }

    @Test
    public void testSnapshotDetection() throws Exception {
        // Test snapshot version detection with allowSnapshots=true
        rule.setAllowSnapshots(true);

        // Test SNAPSHOT versions (should be allowed and not throw exception)
        testSnapshotIsAllowed("1.0.0-SNAPSHOT");
        testSnapshotIsAllowed("2.0-SNAPSHOT");

        // Test non-SNAPSHOT versions (should still apply regular rules)
        testNonSnapshotWithViolation("1.0.0");

        // Test null/empty versions (handled separately)
        testNullVersionWithManagedDependency(null);
        testNullVersionWithManagedDependency("");
    }

    private void testSnapshotIsAllowed(String version) throws Exception {
        // Reset mocks
        reset(project);
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        Dependency dependency = new Dependency();
        dependency.setGroupId("org.example");
        dependency.setArtifactId("artifact");
        dependency.setVersion(version);

        Dependency managedDep = new Dependency();
        managedDep.setGroupId("org.example");
        managedDep.setArtifactId("artifact");
        managedDep.setVersion(version);

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(createDependencyManagement(managedDep));

        // Should not throw exception - snapshots are allowed
        rule.execute();
    }

    private void testNonSnapshotWithViolation(String version) throws Exception {
        // Reset mocks
        reset(project);
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        Dependency dependency = new Dependency();
        dependency.setGroupId("org.example");
        dependency.setArtifactId("artifact");
        dependency.setVersion(version);

        Dependency managedDep = new Dependency();
        managedDep.setGroupId("org.example");
        managedDep.setArtifactId("artifact");
        managedDep.setVersion(version);

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(createDependencyManagement(managedDep));

        // Should throw exception for inline version
        try {
            rule.execute();
            fail("Expected EnforcerRuleException for non-snapshot dependency with violation");
        } catch (org.apache.maven.enforcer.rule.api.EnforcerRuleException e) {
            assertTrue(
                    "Exception message should mention inline version",
                    e.getMessage().contains("has inline version"));
        }
    }

    private void testNullVersionWithManagedDependency(String version) throws Exception {
        // Reset mocks
        reset(project);
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        Dependency dependency = new Dependency();
        dependency.setGroupId("org.example");
        dependency.setArtifactId("artifact");
        dependency.setVersion(version);

        Dependency managedDep = new Dependency();
        managedDep.setGroupId("org.example");
        managedDep.setArtifactId("artifact");
        managedDep.setVersion("1.0.0");

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(createDependencyManagement(managedDep));

        // Should not throw exception - null/empty version with managed dependency is OK
        rule.execute();
    }

    private DependencyManagement createDependencyManagement(Dependency... dependencies) {
        DependencyManagement depMgmt = new DependencyManagement();
        depMgmt.setDependencies(Arrays.asList(dependencies));
        return depMgmt;
    }
}
