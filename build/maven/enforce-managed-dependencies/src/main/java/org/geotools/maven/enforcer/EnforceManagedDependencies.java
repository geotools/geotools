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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.enforcer.rule.api.AbstractEnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

/**
 * Maven Enforcer Rule that ensures all dependency versions are managed by {@literal <dependencyManagement>} sections,
 * either inherited or in the same pom.xml file.
 *
 * <p>This rule validates that:
 *
 * <ul>
 *   <li>All direct dependencies declare versions through {@literal dependencyManagement}, not inline
 *   <li>Dependencies without versions are managed through inherited or local {@literal dependencyManagement}
 *   <li>No dependencies have hardcoded versions when a managed version exists
 * </ul>
 *
 * <p>Usage in pom.xml:
 *
 * <pre>{@code
 * <plugin>
 *   <groupId>org.apache.maven.plugins</groupId>
 *   <artifactId>maven-enforcer-plugin</artifactId>
 *   <executions>
 *     <execution>
 *       <id>enforce-managed-dependencies</id>
 *       <goals>
 *         <goal>enforce</goal>
 *       </goals>
 *       <configuration>
 *         <rules>
 *           <enforceManagedDependencies>
 *             <message>custom error/warning message</message>
 *             <failOnViolation>true</failOnViolation>
 *             <allowSnapshots>false</allowSnapshots>
 *             <allowTestScope>true</allowTestScope>
 *             <excludes>
 *               <exclude>org.example:test-artifact</exclude>
 *             </excludes>
 *           </enforceManagedDependencies>
 *         </rules>
 *       </configuration>
 *     </execution>
 *   </executions>
 * </plugin>
 * }</pre>
 */
@Named("enforceManagedDependencies")
public class EnforceManagedDependencies extends AbstractEnforcerRule {

    /** The Maven project being enforced */
    private MavenProject project;

    /** The Maven session containing reactor information, may be null on single-project builds */
    private MavenSession session;

    // configuration options
    /**
     * List of dependencies to exclude from this rule. Format: groupId:artifactId or groupId:artifactId:type or
     * groupId:artifactId:type:classifier
     */
    private List<String> excludes;

    /** Whether to fail the build on violations or just warn. */
    private boolean failOnViolation = true;

    /** Whether to allow snapshot dependencies to have unmanaged versions. */
    private boolean allowSnapshots = false;

    /** Whether to allow test-scoped dependencies to have inline versions. */
    private boolean allowTestScope = false;

    /** Custom message to display when the rule fails. */
    private String message;

    @Inject
    public EnforceManagedDependencies(MavenSession session) {
        this.session = Objects.requireNonNull(session, "MavenSession can't be null");
        this.project = Objects.requireNonNull(session.getCurrentProject(), "current project is null");
    }

    @Override
    public String getCacheId() {
        // Include project coordinates in cache ID so each project is evaluated separately
        return getClass().getName() + ":" + project.getGroupId() + ":" + project.getArtifactId();
    }

    @Override
    public void execute() throws EnforcerRuleException {
        List<String> violations = new ArrayList<>();

        // Check direct dependencies from the original model (before dependency management is applied)
        List<Dependency> originalDependencies = getOriginalDependencies(project);
        if (originalDependencies != null) {
            for (Dependency dependency : originalDependencies) {
                checkDependency(dependency, project, violations);
            }
        }

        if (!violations.isEmpty()) {
            String message = buildViolationMessage(violations, project);
            if (failOnViolation) {
                throw new EnforcerRuleException(message);
            } else {
                getLog().warn(message);
            }
        }
    }

    private List<Dependency> getOriginalDependencies(MavenProject project) {
        // Try to get original dependencies from the original model if available
        if (project.getOriginalModel() != null && project.getOriginalModel().getDependencies() != null) {
            return project.getOriginalModel().getDependencies();
        }
        // Fallback to regular dependencies if original model is not available (e.g., in tests)
        return project.getDependencies();
    }

    private void checkDependency(Dependency dependency, MavenProject project, List<String> violations) {
        if (isExcluded(dependency)) {
            return;
        }

        if (isSnapshot(dependency) && allowSnapshots) {
            return;
        }

        if (isTestScope(dependency) && allowTestScope) {
            return;
        }

        String dependencyKey = getDependencyKey(dependency);
        String managedVersion = getManagedVersion(dependency, project);

        // In a BOM-managed project like GeoTools, NO dependency should have inline versions
        if (StringUtils.isNotEmpty(dependency.getVersion())) {
            String errorMessage;
            if (managedVersion != null) {
                errorMessage = String.format(
                        "Dependency %s has inline version %s but is managed with version %s",
                        dependencyKey, dependency.getVersion(), managedVersion);
            } else {
                errorMessage =
                        String.format("Dependency %s has inline version %s", dependencyKey, dependency.getVersion());
            }
            violations.add(errorMessage);
        } else {
            // Check if dependency without version is actually managed
            if (managedVersion == null) {
                violations.add(String.format(
                        "Dependency %s is not managed by any dependencyManagement section", dependencyKey));
            }
        }
    }

    private String getManagedVersion(Dependency dependency, MavenProject project) {
        // Check if we're in a reactor build with BOMs available
        if (isReactorBuild() && areBOMsInReactor()) {
            // In reactor builds with BOMs, use effective dependency management
            try {
                Artifact artifact = createArtifact(dependency);
                Artifact managedArtifact = project.getManagedVersionMap().get(artifact.getDependencyConflictId());
                if (managedArtifact != null) {
                    return managedArtifact.getVersion();
                }
            } catch (Exception e) {
                getLog().debug("Failed to create artifact for dependency " + dependency
                        + ", falling back to manual checking: " + e.getMessage());
            }
        }

        // Fallback to manual checking for non-reactor builds or when BOMs aren't in reactor
        return getManagedVersionFromProject(dependency, project);
    }

    private boolean isReactorBuild() {
        return session != null
                && session.getProjects() != null
                && session.getProjects().size() > 1;
    }

    private boolean areBOMsInReactor() {
        if (session == null || session.getProjects() == null) {
            return false;
        }

        // Check if the BOM modules are in the current reactor
        for (MavenProject reactorProject : session.getProjects()) {
            String artifactId = reactorProject.getArtifactId();
            if ("gt-platform-dependencies".equals(artifactId) || "gt-bom".equals(artifactId)) {
                return true;
            }
        }
        return false;
    }

    private Artifact createArtifact(Dependency dependency) {
        String type = StringUtils.defaultString(dependency.getType(), "jar");
        String classifier = StringUtils.defaultString(dependency.getClassifier(), "");

        return new DefaultArtifact(
                dependency.getGroupId(),
                dependency.getArtifactId(),
                dependency.getVersion(),
                "compile", // scope doesn't matter for version management lookup
                type,
                classifier,
                new DefaultArtifactHandler(type));
    }

    private String getManagedVersionFromProject(Dependency dependency, MavenProject project) {
        // Check current project's dependency management
        if (project.getDependencyManagement() != null
                && project.getDependencyManagement().getDependencies() != null) {

            for (Dependency managed : project.getDependencyManagement().getDependencies()) {
                if (dependencyMatches(dependency, managed)) {
                    return managed.getVersion();
                }
            }
        }

        // Check parent projects recursively
        if (project.getParent() != null) {
            return getManagedVersionFromProject(dependency, project.getParent());
        }

        return null;
    }

    private boolean dependencyMatches(Dependency dependency, Dependency managed) {
        String dependencyType = StringUtils.defaultString(dependency.getType(), "jar");
        String managedType = StringUtils.defaultString(managed.getType(), "jar");

        String dependencyClassifier = StringUtils.defaultString(dependency.getClassifier(), "");
        String manageClassifier = StringUtils.defaultString(managed.getClassifier(), "");
        return Objects.equals(dependency.getGroupId(), managed.getGroupId())
                && Objects.equals(dependency.getArtifactId(), managed.getArtifactId())
                && Objects.equals(dependencyType, managedType)
                && Objects.equals(dependencyClassifier, manageClassifier);
    }

    private boolean isExcluded(Dependency dependency) {
        if (excludes == null || excludes.isEmpty()) {
            return false;
        }

        String dependencyKey = getDependencyKey(dependency);
        String shortKey = dependency.getGroupId() + ":" + dependency.getArtifactId();

        for (String exclude : excludes) {
            // Normalize exclude pattern by removing :jar: if present
            String normalizedExclude = normalizeExcludePattern(exclude);

            if (normalizedExclude.equals(dependencyKey) || normalizedExclude.equals(shortKey)) {
                return true;
            }
            // Check if exclude is a prefix (like "org.example" matching "org.example:artifact")
            if (dependencyKey.startsWith(normalizedExclude + ":") || shortKey.startsWith(normalizedExclude + ":")) {
                return true;
            }
        }

        return false;
    }

    private String normalizeExcludePattern(String exclude) {
        // Remove :jar: from exclude patterns to match the way getDependencyKey works
        // Convert "group:artifact:jar:classifier" to "group:artifact:classifier"
        if (exclude.contains(":jar:")) {
            return exclude.replace(":jar:", ":");
        }
        // Convert "group:artifact:jar" to "group:artifact"
        if (exclude.endsWith(":jar")) {
            return exclude.substring(0, exclude.length() - 4);
        }
        return exclude;
    }

    private boolean isSnapshot(Dependency dependency) {
        return StringUtils.isNotEmpty(dependency.getVersion())
                && dependency.getVersion().endsWith("-SNAPSHOT");
    }

    private boolean isTestScope(Dependency dependency) {
        return "test".equals(dependency.getScope());
    }

    private String getDependencyKey(Dependency dependency) {
        StringBuilder key = new StringBuilder();
        key.append(dependency.getGroupId()).append(":").append(dependency.getArtifactId());

        if (StringUtils.isNotEmpty(dependency.getType()) && !"jar".equals(dependency.getType())) {
            key.append(":").append(dependency.getType());
        }

        if (StringUtils.isNotEmpty(dependency.getClassifier())) {
            key.append(":").append(dependency.getClassifier());
        }

        return key.toString();
    }

    private String buildViolationMessage(List<String> violations, MavenProject project) {
        StringBuilder msgBuilder = new StringBuilder();

        // Use custom message if provided, otherwise use default
        if (StringUtils.isNotEmpty(message)) {
            msgBuilder.append(message).append("\n\n");
        }

        msgBuilder
                .append("EnforceManagedDependencies rule violated in project ")
                .append(project.getGroupId())
                .append(":")
                .append(project.getArtifactId())
                .append(":\n");

        for (String violation : violations) {
            msgBuilder.append("  - ").append(violation).append("\n");
        }

        msgBuilder.append(
                "\nEnsure all dependencies are managed through dependencyManagement sections in this project or inherited from parent projects.");

        return msgBuilder.toString();
    }

    // Getters and setters for configuration

    public List<String> getExcludes() {
        return excludes != null ? Collections.unmodifiableList(new ArrayList<>(excludes)) : Collections.emptyList();
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes != null ? new ArrayList<>(excludes) : null;
    }

    public boolean isFailOnViolation() {
        return failOnViolation;
    }

    public void setFailOnViolation(boolean failOnViolation) {
        this.failOnViolation = failOnViolation;
    }

    public boolean isAllowSnapshots() {
        return allowSnapshots;
    }

    public void setAllowSnapshots(boolean allowSnapshots) {
        this.allowSnapshots = allowSnapshots;
    }

    public boolean isAllowTestScope() {
        return allowTestScope;
    }

    public void setAllowTestScope(boolean allowTestScope) {
        this.allowTestScope = allowTestScope;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
