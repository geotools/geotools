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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.maven.enforcer.rule.api.EnforcerLogger;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** Unit tests for EnforceManagedDependencies enforcer rule. */
public class EnforceManagedDependenciesTest {

    @Mock
    private EnforcerLogger logger;

    @Mock
    private MavenSession session;

    @Mock
    private MavenProject project;

    /** @see #testManagedInParentProject() */
    @Mock
    private MavenProject parentProject;

    private EnforceManagedDependencies rule;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(project.getOriginalModel()).thenReturn(null); // Fallback to getDependencies()
        when(session.getCurrentProject()).thenReturn(project);
        when(session.getProjects()).thenReturn(List.of(project));
        rule = new EnforceManagedDependencies(session);
        rule.setLog(logger);
    }

    @Test
    public void testValidManagedDependency() throws Exception {
        // Given: A dependency without version that is managed
        Dependency dependency = createDependency("org.example", "test-artifact", null);
        Dependency managedDep = createDependency("org.example", "test-artifact", "1.0.0");

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(createDependencyManagement(managedDep));
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        // When: Rule is executed
        rule.execute();

        // Then: No exception should be thrown
        verify(logger, never()).warn(anyString());
    }

    @Test(expected = EnforcerRuleException.class)
    public void testUnmanagedDependency() throws Exception {
        // Given: A dependency without version that is not managed
        Dependency dependency = createDependency("org.example", "unmanaged-artifact", null);

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(null);
        when(project.getParent()).thenReturn(null);
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        // When: Rule is executed
        rule.execute();

        // Then: Exception should be thrown
    }

    @Test(expected = EnforcerRuleException.class)
    public void testInlineVersionWithManagedVersion() throws Exception {
        // Given: A dependency with inline version that is also managed
        Dependency dependency = createDependency("org.example", "test-artifact", "1.0.0");
        Dependency managedDep = createDependency("org.example", "test-artifact", "1.0.0");

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(createDependencyManagement(managedDep));
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        // When: Rule is executed
        rule.execute();

        // Then: Exception should be thrown
    }

    @Test(expected = EnforcerRuleException.class)
    public void testInlineVersionWithoutManagedVersion() throws Exception {
        // Given: A dependency with inline version that is not managed
        Dependency dependency = createDependency("org.example", "test-artifact", "1.0.0");

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(null);
        when(project.getParent()).thenReturn(null);
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        // When: Rule is executed
        rule.execute();

        // Then: Exception should be thrown (inline versions not allowed in BOM-managed projects)
    }

    @Test
    public void testManagedInParentProject() throws Exception {
        // Given: A dependency managed in parent project
        Dependency dependency = createDependency("org.example", "test-artifact", null);
        Dependency managedDep = createDependency("org.example", "test-artifact", "1.0.0");

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(null);
        when(project.getParent()).thenReturn(parentProject);
        when(parentProject.getDependencyManagement()).thenReturn(createDependencyManagement(managedDep));
        when(parentProject.getParent()).thenReturn(null);
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        // When: Rule is executed
        rule.execute();

        // Then: No exception should be thrown
        verify(logger, never()).warn(anyString());
    }

    @Test
    public void testExcludedDependency() throws Exception {
        // Given: A dependency that is excluded from the rule
        Dependency dependency = createDependency("org.example", "excluded-artifact", "1.0.0");
        Dependency managedDep = createDependency("org.example", "excluded-artifact", "1.0.0");

        rule.setExcludes(Arrays.asList("org.example:excluded-artifact"));

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(createDependencyManagement(managedDep));
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        // When: Rule is executed
        rule.execute();

        // Then: No exception should be thrown
        verify(logger, never()).warn(anyString());
    }

    @Test
    public void testSnapshotDependencyWithAllowSnapshots() throws Exception {
        // Given: A snapshot dependency with allowSnapshots enabled
        Dependency dependency = createDependency("org.example", "snapshot-artifact", "1.0.0-SNAPSHOT");
        Dependency managedDep = createDependency("org.example", "snapshot-artifact", "1.0.0-SNAPSHOT");

        rule.setAllowSnapshots(true);

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(createDependencyManagement(managedDep));
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        // When: Rule is executed
        rule.execute();

        // Then: No exception should be thrown
        verify(logger, never()).warn(anyString());
    }

    @Test
    public void testTestScopeDependencyWithAllowTestScope() throws Exception {
        // Given: A test-scoped dependency with inline version and allowTestScope enabled
        Dependency dependency = createDependency("org.example", "test-artifact", "1.0.0");
        dependency.setScope("test");

        rule.setAllowTestScope(true);

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(null);
        when(project.getParent()).thenReturn(null);
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        // When: Rule is executed
        rule.execute();

        // Then: No exception should be thrown
        verify(logger, never()).warn(anyString());
    }

    @Test
    public void testWarningMode() throws Exception {
        // Given: Rule configured to warn instead of fail
        Dependency dependency = createDependency("org.example", "unmanaged-artifact", null);

        rule.setFailOnViolation(false);

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(null);
        when(project.getParent()).thenReturn(null);
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        // When: Rule is executed
        rule.execute();

        // Then: Warning should be logged instead of throwing exception
        verify(logger).warn(contains("EnforceManagedDependencies rule violated"));
    }

    @Test
    public void testCustomMessage() throws Exception {
        // Given: Rule with custom message
        String customMessage = "Custom violation message";
        Dependency dependency = createDependency("org.example", "unmanaged-artifact", null);

        rule.setMessage(customMessage);
        rule.setFailOnViolation(false);

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(null);
        when(project.getParent()).thenReturn(null);
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        // When: Rule is executed
        rule.execute();

        // Then: Custom message should be included in warning
        verify(logger).warn(contains(customMessage));
    }

    @Test
    public void testDependencyWithClassifier() throws Exception {
        // Given: A dependency with classifier that is managed
        Dependency dependency = createDependencyWithClassifier("org.example", "test-artifact", null, "tests");
        Dependency managedDep = createDependencyWithClassifier("org.example", "test-artifact", "1.0.0", "tests");

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(createDependencyManagement(managedDep));
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        // When: Rule is executed
        rule.execute();

        // Then: No exception should be thrown
        verify(logger, never()).warn(anyString());
    }

    @Test
    public void testDependencyWithType() throws Exception {
        // Given: A dependency with type that is managed
        Dependency dependency = createDependencyWithType("org.example", "test-artifact", null, "pom");
        Dependency managedDep = createDependencyWithType("org.example", "test-artifact", "1.0.0", "pom");

        when(project.getDependencies()).thenReturn(Arrays.asList(dependency));
        when(project.getDependencyManagement()).thenReturn(createDependencyManagement(managedDep));
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        // When: Rule is executed
        rule.execute();

        // Then: No exception should be thrown
        verify(logger, never()).warn(anyString());
    }

    @Test
    public void testNoDependencies() throws Exception {
        // Given: Project with no dependencies
        when(project.getDependencies()).thenReturn(null);
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        // When: Rule is executed
        rule.execute();

        // Then: No exception should be thrown
        verify(logger, never()).warn(anyString());
    }

    @Test
    public void testEmptyDependencies() throws Exception {
        // Given: Project with empty dependencies list
        when(project.getDependencies()).thenReturn(Collections.emptyList());
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        // When: Rule is executed
        rule.execute();

        // Then: No exception should be thrown
        verify(logger, never()).warn(anyString());
    }

    @Test
    public void testCacheId() {
        // Cache ID should include project coordinates for per-project caching
        when(project.getGroupId()).thenReturn("org.geotools");
        when(project.getArtifactId()).thenReturn("test-module");

        String expectedCacheId = EnforceManagedDependencies.class.getName() + ":org.geotools:test-module";
        assertEquals(expectedCacheId, rule.getCacheId());
    }

    // Helper methods

    private Dependency createDependency(String groupId, String artifactId, String version) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        return dependency;
    }

    private Dependency createDependencyWithClassifier(
            String groupId, String artifactId, String version, String classifier) {
        Dependency dependency = createDependency(groupId, artifactId, version);
        dependency.setClassifier(classifier);
        return dependency;
    }

    private Dependency createDependencyWithType(String groupId, String artifactId, String version, String type) {
        Dependency dependency = createDependency(groupId, artifactId, version);
        dependency.setType(type);
        return dependency;
    }

    private DependencyManagement createDependencyManagement(Dependency... dependencies) {
        DependencyManagement depMgmt = new DependencyManagement();
        depMgmt.setDependencies(Arrays.asList(dependencies));
        return depMgmt;
    }
}
