# GeoTools Enforce Managed Dependencies Rule

A [Maven Enforcer Plugin](https://maven.apache.org/enforcer/maven-enforcer-plugin/) custom rule that ensures all dependency versions
are properly managed through `dependencyManagement` sections rather than being hardcoded inline in POMs.

## Overview

GeoTools is a large multi-module library, and we want to provide BOMs to manage dependencies in downstream projects,
while at the same time them for GeoTools development itself, in order to force us to keep the BOMs up to date.

The `maven-enforcer-plugin` does not have a rule to enforce managed dependencies though.

This enforcer rule is designed for BOM-managed projects like GeoTools, where dependency versions should be centrally managed through
Bill of Materials (BOM) files or local `dependencyManagement` sections. It prevents the common anti-pattern of hard-coding dependency versions
directly in `<dependency>` declarations, hence providing:

- **Version Consistency**: Ensure all modules use the same versions of third-party dependencies
- **Maintenance**: Centralize version management, making upgrades easier
- **Conflict Prevention**: Reduce dependency version conflicts across modules
- **CI/CD Reliability**: Prevent builds from passing inconsistently between single-module and reactor builds

## Features

- All dependencies must have versions managed through `dependencyManagement`
- No inline/hardcoded versions in `<dependency>` declarations (except in allowed cases)
- Consistent dependency management across all modules
- Test-scoped dependencies can have inline versions when `allowTestScope=true`
- Specific dependencies can be excluded from the rule
- Modules can manage their own dependencies via `dependencyManagement`. This should be constrained to "unsupported" modules.

### Internals:

- **Per-project caching**: Each project is evaluated independently
- **Original model analysis**: Checks dependencies before Maven applies dependency management
- **Reactor-aware**: Works consistently in both single-module and multi-module builds

## Error Messages

When violations are detected, the rule provides clear error messages:

```
mvn install -Dqa -DskipTests
...
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.974 s
[INFO] Finished at: 2025-06-08T12:55:36-03:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-enforcer-plugin:3.4.1:enforce (enforce-managed-versions) on project gt-main:
[ERROR] Rule 0: org.geotools.maven.enforcer.EnforceManagedDependencies failed with message:
[ERROR] All dependency versions must be managed in gt-platform-dependencies or gt-bom. Unsupported modules may define their own dependencyManagement sections
[ERROR]
[ERROR] EnforceManagedDependencies rule violated in project org.geotools:gt-main:
[ERROR]   - Dependency org.geotools:gt-referencing has inline version ${project.version} but is managed with version 34-SNAPSHOT
[ERROR]   - Dependency it.geosolutions.jaiext.utilities:jt-utilities has inline version 1.1.30 but is managed with version 1.1.31
[ERROR]
[ERROR] Ensure all dependencies are managed through dependencyManagement sections in this project or inherited from parent projects.
```

## Configuration

### Basic Usage

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-enforcer-plugin</artifactId>
  <dependencies>
    <dependency>
      <groupId>org.geotools.maven</groupId>
      <artifactId>gt-enforce-managed-dependencies</artifactId>
      <version>${project.version}</version>
    </dependency>
  </dependencies>
  <executions>
    <execution>
      <id>enforce-managed-dependencies</id>
      <goals>
        <goal>enforce</goal>
      </goals>
      <configuration>
        <rules>
          <enforceManagedDependencies>
            <!-- Configuration options here -->
          </enforceManagedDependencies>
        </rules>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### Configuration Options

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `failOnViolation` | boolean | `true` | Whether to fail the build on violations or just warn |
| `allowSnapshots` | boolean | `false` | Allow SNAPSHOT dependencies to have inline versions |
| `allowTestScope` | boolean | `false` | Allow test-scoped dependencies to have inline versions |
| `message` | String | _(default message)_ | Custom message to display when rule fails |
| `excludes` | List<String> | `[]` | Dependencies to exclude from this rule |

### GeoTools Configuration Example

```xml
<enforceManagedDependencies>
  <message>All dependency versions must be managed by dependencyManagement sections in platform-dependencies or bom modules</message>
  <failOnViolation>true</failOnViolation>
  <allowSnapshots>false</allowSnapshots>
  <allowTestScope>true</allowTestScope>
  <excludes>
    <exclude>org.geotools:gt-platform-dependencies</exclude>
    <exclude>org.geotools:gt-bom</exclude>
  </excludes>
</enforceManagedDependencies>
```

## Use Cases

### Valid Scenarios

#### 1. BOM-Managed Dependencies

```xml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-core</artifactId>
  <!-- Version managed by gt-platform-dependencies BOM -->
</dependency>
```

#### 2. Local Dependency Management

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.example</groupId>
      <artifactId>custom-library</artifactId>
      <version>2.1.0</version>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependencies>
  <dependency>
    <groupId>com.example</groupId>
    <artifactId>custom-library</artifactId>
    <!-- Version managed locally -->
  </dependency>
</dependencies>
```

#### 3. Test Dependencies (with allowTestScope=true)

```xml
<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-core</artifactId>
  <version>4.6.1</version>
  <scope>test</scope>
</dependency>
```

### Invalid Scenarios

#### 1. Inline Versions for Managed Dependencies

```xml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-core</artifactId>
  <version>2.19.0</version> <!-- INVALID: Should be managed by BOM -->
</dependency>
```

#### 2. Unmanaged Dependencies
```xml
<dependency>
  <groupId>com.example</groupId>
  <artifactId>unknown-library</artifactId>
  <!-- INVALID: No version specified and not managed anywhere -->
</dependency>
```

## Exclude Patterns

The rule supports flexible exclude patterns:

| Pattern | Matches |
|---------|---------|
| `org.example:artifact` | Exact groupId:artifactId |
| `org.example:artifact:jar` | With specific type |
| `org.example:artifact:jar:classifier` | With type and classifier |
| `org.example` | All artifacts from groupId |


## Architecture

### Core Components

1. **EnforceManagedDependencies**: Main rule class that implements `AbstractEnforcerRule`
2. **Dependency Analysis**: Checks original POM model before dependency management is applied
3. **Caching Strategy**: Per-project cache IDs prevent cross-contamination in reactor builds
4. **Flexible Configuration**: Supports various exclusion and allowance patterns

### Technical Details

- **Maven API**: Uses modern Maven Enforcer API with dependency injection
- **Original Model**: Analyzes `project.getOriginalModel().getDependencies()` to see pre-resolution state
- **Reactor Awareness**: Handles both single-module and multi-module builds consistently
- **Performance**: Efficient caching with per-project cache keys

## Testing

The rule includes comprehensive tests covering:

- Valid managed dependencies
- Invalid inline versions
- Test scope allowances
- Exclude patterns
- Configuration validation
- Edge cases and error handling

## Integration with GeoTools

In the GeoTools project, this rule is activated via the `qa` profile in `modules/pom.xml`:

```bash
# Check modules for violations
mvn validate -Dqa -f modules/

# Check specific module
mvn validate -Dqa -f modules/library/main/
```

The rule integrates with GeoTools' BOM architecture:

- **gt-platform-dependencies**: Manages third-party dependency versions
- **gt-bom**: Manages GeoTools artifact versions
- **Module POMs**: Should not contain inline versions (except for test dependencies)

## Troubleshooting

### Common Issues

#### 1. "Dependency has inline version" Error

**Cause**: A dependency has a hardcoded version that should be managed.

**Solution**: Remove the `<version>` tag and ensure the dependency is managed in:
- `gt-platform-dependencies/pom.xml` (for third-party deps)
- `gt-bom/pom.xml` (for GeoTools deps)
- Local `<dependencyManagement>` section

#### 3. Rule Not Executing

**Cause**: Profile not activated or dependency not found.

**Solution**: 
- Ensure the `qa` profile is activated: `mvn validate -Dqa`
- Verify the rule artifact is built: `mvn install -f build/maven/enforce-managed-dependencies/`

### Debug Options

```bash
# Enable debug logging
mvn validate -Dqa -X

# Skip enforcer for testing
mvn validate -Denforcer.skip=true

# Run specific enforcer rules only
mvn validate -Dqa -Denforcer.rules=enforceManagedDependencies
```
