# GeoTools BOM Usage Guide

GeoTools 34 introduces the Bill of Materials (BOM) pattern to improve dependency management. This guide explains how to use the BOMs in your projects.

## What is a BOM?

A BOM (Bill of Materials) is a special POM file that manages dependency versions across a project. Rather than specifying versions for each dependency individually, you can import the BOM which provides consistent versioning.

GeoTools provides two BOMs:

1. `gt-platform-dependencies` - Third-party dependency versions
2. `gt-bom` - GeoTools module versions (also imports platform-dependencies)

## Using the BOMs

### For GeoTools Module Development

When developing GeoTools modules, you don't need to specify versions for internal dependencies:

```xml
<dependencies>
  <!-- No version needed for GeoTools modules -->
  <dependency>
    <groupId>org.geotools</groupId>
    <artifactId>gt-main</artifactId>
  </dependency>
  
  <!-- No version needed for third-party dependencies declared in platform-dependencies -->
  <dependency>
    <groupId>org.locationtech.jts</groupId>
    <artifactId>jts-core</artifactId>
  </dependency>
</dependencies>
```

### For Applications Using GeoTools

Applications using GeoTools should import the `gt-bom` in their dependency management:

```xml
<dependencyManagement>
  <dependencies>
    <!-- Import GeoTools BOM -->
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-bom</artifactId>
      <version>34.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependencies>
  <!-- No version needed for GeoTools modules -->
  <dependency>
    <groupId>org.geotools</groupId>
    <artifactId>gt-shapefile</artifactId>
  </dependency>
  <dependency>
    <groupId>org.geotools</groupId>
    <artifactId>gt-epsg-hsql</artifactId>
  </dependency>
  
  <!-- Other dependencies still need versions -->
  <dependency>
    <groupId>com.example</groupId>
    <artifactId>my-library</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

### For Spring Framework Applications

When using Spring Framework, you can combine GeoTools BOM with Spring Framework's BOM:

```xml
<dependencyManagement>
  <dependencies>
    <!-- Spring Framework BOM -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-framework-bom</artifactId>
      <version>5.3.39</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
    
    <!-- GeoTools BOM -->
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-bom</artifactId>
      <version>34.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

## Test Artifacts

The BOM also manages test artifacts, which use the `tests` classifier:

```xml
<dependency>
  <groupId>org.geotools</groupId>
  <artifactId>gt-main</artifactId>
  <classifier>tests</classifier>
  <scope>test</scope>
</dependency>
```

## Platform Dependencies Only

In some cases, you might want to use only the platform dependencies BOM:

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-platform-dependencies</artifactId>
      <version>34.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

## Version Overrides

If you need to override a specific dependency version:

```xml
<dependencyManagement>
  <dependencies>
    <!-- Import GeoTools BOM -->
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-bom</artifactId>
      <version>34.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
    
    <!-- Override a specific dependency -->
    <dependency>
      <groupId>org.locationtech.jts</groupId>
      <artifactId>jts-core</artifactId>
      <version>1.19.0</version>
    </dependency>
  </dependencies>
</dependencyManagement>
```

## Benefits

Using the GeoTools BOM offers several advantages:

1. **Simplified dependency management**: No need to specify versions for each GeoTools module
2. **Version consistency**: All GeoTools modules use compatible versions
3. **Easier upgrades**: Change one version number instead of dozens
4. **Reduced transitive dependency conflicts**: The BOM ensures compatible dependencies
5. **Industry standard approach**: Following Maven best practices

## Common Issues

### Missing Dependency Version

If you see an error like:

```
'dependencies.dependency.version' for org.geotools:gt-main:jar is missing
```

It means you haven't properly imported the BOM in your `dependencyManagement` section.

### Version Conflicts

If you see version conflicts, check if you're:

1. Importing the BOM correctly with `<scope>import</scope>` and `<type>pom</type>`
2. Not overriding versions unintentionally
3. Using compatible versions of BOMs if importing multiple
