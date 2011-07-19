Code Generation
^^^^^^^^^^^^^^^

The xmlcodegen maven plugin provides a code generator which can be used to generate bindings and other classes used in the geotools xml framework. The code generator feeds off a single schema file and generates a number of java classes.

Plugin Configuration
''''''''''''''''''''

To use the plugin in a project, the following configuration must first be added to the pom of the project::
  
  <build>
     <plugins>
          ...
          <plugin>
             <groupId>org.geotools.maven</groupId>
             <artifactId>xmlcodegen</artifactId>
             <version>8-SNAPSHOT</version>
             <configuration>
                 <schemaLocation>po.xsd</schemaLocation>
                 <schemaSourceDirectory>${basedir}/src/main/resources</schemaSourceDirectory>
                 <destinationPackage>org.geotools.po.bindings</destinationPackage>
                 <generateConfiguration>true</generateConfiguration>
                 <generateBindingInterface>true</generateBindingInterface>
                 <generateSchemaLocationResolver>true</generateSchemaLocationResolver>
                 <bindingConstructorArguments>
                     <bindingConstructorArgument>
                         <name>factory</name>
                         <type>org.geotools.po.ObjectFactory</type>
                     </bindingConstructorArgument>
                 </bindingConstructorArguments>
             </configuration>
         </plugin>
         ...
     </plugins>
  </build>

In the above, po.xsd should be replaced with an actual schema file. Once configured the plugin can be executed with the following command::
  
  mvn org.geotools.maven:xmlcodegen:generate 

The output of the command will look something like the following::
  
  [INFO] Generating bindings...
  8/Jan/2007 9:56:00 org.geotools.maven.xmlcodegen.BindingGenerator generate
  INFO: Generating binding for Items
  8/Jan/2007 9:56:00 org.geotools.maven.xmlcodegen.BindingGenerator generate
  INFO: Generating binding for PurchaseOrderType
  8/Jan/2007 9:56:00 org.geotools.maven.xmlcodegen.BindingGenerator generate
  INFO: Generating binding for SKU
  8/Jan/2007 9:56:00 org.geotools.maven.xmlcodegen.BindingGenerator generate
  INFO: Generating binding for USAddress
  8/Jan/2007 9:56:00 org.geotools.maven.xmlcodegen.BindingGenerator generate
  INFO: Generating binding for Items_item
  [INFO] Generating parser configuration...
  [INFO] ------------------------------------------------------------------------
  [INFO] BUILD SUCCESSFUL
  [INFO] ------------------------------------------------------------------------
  [INFO] Total time: 4 seconds
  [INFO] Finished at: Mon Jan 08 09:56:00 PST 2007
  [INFO] Final Memory: 7M/12M
  [INFO] ------------------------------------------------------------------------

By default, the code generator will generate the following classes:

* A binding for each type in the schema
* A subclass of org.geotools.xml.Configuration for the schema
* A subclass of org.geotools.xml.BindingConfiguration
* An interface containing the qualified names of all elements, attributes, and types defined in the schema

The plugin contains may configuration options which can be used to modify this default behaviour.

**Example: gt-xsd-gml3 GMLSchema**

This example demonstrates how to generate (just) GMLSchema.java, a file containing a type constant for every GML 3.1.1 XSD type, for gt-xsd-gml3, the GeoTools GML 3.1.1 support module.

1. Build xmlcodegen
   
   In build/maven/xmlcodegen::
     
     mvn clean install
   
2. Generate the schema
   
   In modules/extension/xsd/xsd-gml3::
     
     mvn -o xmlcodegen:generateSchema

3. Move and clean up the schema
   
   There are still some manual changes to perform:
   
   * The default location of the schema source file is wrong. Move it, replacing the current GMLSchema.java::
       
       mv src/main/java/GMLSchema.java src/main/java/org/geotools/gml3/GMLSchema.java
   
   * The Java package is wrong. Add to the top of GMLSchema.java::
       
       package org.geotools.gml3;
   
   * Java imports are wrong, Fix GMLSchema.java imports in Eclipse with Ctrl-Shift-O.

Configuration Parameter Reference
'''''''''''''''''''''''''''''''''

The code generator plugins offers the following goals:

* xmlcodegen:generate - Generates the parser/encoding configuration and bindings
* xmlcodegen:generateSchema - Generates the class that represents the xml schema as a geotools feature model schema

**Common Configuration**

Many configuration parameters are shared between the above goals. The following is a list of all the common configuration parameters:

* schemaLocation (no default)
  
  Location of the \*.xsd file defining the schema. This can be a relative or aboslute path. For relative paths, the schemaSourceDirectory and schemaLookupDirectories parameters are used.::
    
    
    <schemaLocation>po.xsd</schemaLocation>
    <schemaLocation>/home/bob/filter.xsd</schemaLocation>

* schemaSourceDirectory (default src/main/xsd)
  
  Directory containing the schema specified by schemaLocation, and optionally any other schemas referenced by it.::
     
     <schemaSourceDirectory>src/main/resources</schemaSourceDirectory>
     <schemaSourceDirectory>/home/bob/schemas</schemaSourceDirectory>

* schemaLookupDirectories (no default)
  
  List of additional directories to be used to locate referenced schemas.::
    
    <schemaLookupDirectories>
       <schemaLookupDirectory>/home/bob/schemas</schemaLookupDirectory>
       <schemaLookupDirectory>/home/bob/otherSchemas</schemaLookupDirectory>
    </schemaLookupDirectories>

* targetPrefix (no default)
  
  The prefix to be mapped to the 'targetNamespace' of the schema.::
     
     <targetPrefix>po</targetPrefix>

* destinationPackage (no default)
  
  Name of java package in which to place generated files into. If unspecified the root of the default ( empty ) package is used.::
     
     <destinationPackage>org.geotools.po</destinationPackage>

* outputDirectory (default src/main/java)
  
  Base location of where generated files should be written. If the destinationPackage is also specified it is appended to this location.::
    
    <outputDirectory>/home/bob/xml</outputDirectory>
    <outputDirectory>src/other/java</outputDirectory>

* overwriteExistingFiles (default false)
  
  Flag controlling wether existing files should be overwritten by newly generated files.::
     
     <overwriteExistingFiles>true</overwriteExistingFiles>

* relativeSchemaReference (default false)

  Treat all relative schema references (include and import) as relative to the schema (XSD) resource in which they are found, rather than looking for them in compiled classes or ``schemaLookupDirectories``. This requires all included/imported schemas to be present in the expected relative filesystem location. The main advantage of this approach is that it supports schema files that have cyclic dependencies (e.g. GML 3.2)::

      <relativeSchemaReference>true</relativeSchemaReference>

**Generate Configuration**

The following configuration parameters apply only to the **generate** goal:

* generateXsd (default true)
  
  Flag controlling wether the interface containing all the qualified element, attribute, and type names for the schema is generated.::
    
    <generateXsd>false</generateXsd>

* generateConfiguration (default true)
  
  Flag controlling wether the Configuration for the schema should be generated.::
    
    <generateConfiguration>false</generateConfiguration>

* generateAttributeBindings (default false)
  
  Flag controlling wether bindings for attributes declared in the schema should be generated.::
     
     <generateAttributeBindings>true</generateAttributeBindings>

* generateElementBindings (default false)
  
  Flag controlling wether bindings for elements declared in the schema should be generated.::
    
    <generateElementBindings>true</generateElementBindings>

* generateTypeBindings (default true)
  
  Flag controlling wether bindings for types declared in the schema should be generated.::
    
    <generateTypeBindings>false</generateTypeBindings>

* includes (default none)
  
  Inclusion filter for attribute, element, and type bindings. An inclusion is specified by the name of the attribute, element, or type.::
    
    
    <includes>
      <include>PurchaseOrderType</include>
      <include>AbstractFeatureType</include>
      <include>PropertyIsNotEqualTo</include>
    </includes>

* bindingConstructorArguments (defaults none)
  
  List of name, class pairs which define the constructor arguments for each generated binding.::
    
    <bindingConstructorArguments>
      <bindingConstructorArgument>
        <name>factory</name>
        <type>org.geotools.po.ObjectFactory</type>
      </bindingConstructorArgument>
    <bindingConstructorArguments>
    <bindingConstructorArguments>
      <bindingConstructorArgument>
        <name>filterFactory</name>
        <value>org.opengis.filter.FilterFactory</value>
      </bindingConstructorArgument>
      <bindingConstructorArgument>
        <name>geometryFactory</name>
        <value>com.vividsolutions.jts.geom.GeometryFactory</value>
      </bindingConstructorArgument>
    <bindingConstructorArguments>

**GenerateSchema Configuration**

The following configuration parameters apply only to the generateSchema goal:

* includeComplexTypes (default true)
  
  Controls whether complex types from the xml schema are included in the generated geotools schema

* includeSimpleTypes (default true)
  
  Controls whether simple types from the xml schema are included in the generated geotools schema.

* followComplexTypes (default false)
  
  Controls whether the contents of complex xml types are processed during schema generation. Setting this parameter to true will cause the generator to create geotools types which model exactly their corresponding xml schema types in term of base types and composition::

      <followComplexTypes>true</followComplexTypes>

* cyclicTypeSupport (default false)

  Support complex xml types that are cyclically defined, such as ``gmd:CI_CitationType`` from GML 3.2. Types in the generated Schema file will be defined using ``AbstractLazyAttributeType`` and ``AbstractLazyComplexType`` from ``gt-main``. A ``main()`` method is also included in the generated Schema class to aid testing. This option is best used with ``followComplexTypes`` set to ``true``, although if you are working with a multi-package schema such as GML 3.2, you will likely need to bootstrap with ``followComplexTypes`` set to ``false`` to generate skeleton Schema files that you can import before recreating them all with ``followComplexTypes`` set to ``true``::

      <cyclicTypeSupport>true</cyclicTypeSupport>

* imports (default none)
  
  List of geotools schema classes to include as an import when generating the schema.::
    
    <imports>
      <import>org.geotools.xml.XLINKSchema</import>
    </imports>
 
* printRecursionPaths (default false)
  
  Causes the generator to print out information about how it recurses through the xml schema

* maxRecrusionDepth (default 15)
  
  Causes the generator to stop recursion and throw back an error when it reaches a particular recursion depth. This will happen if the schema has circular dependencies among its contents.

* typebindings (default none)
  
  Override the default mapping of XSD complex types to Java types in generated *Schema.java*. By default, XSD complex types are mapped to Java ComplexTypeImpl bound to Collection, but for complex types represented by atomic Java types (such a geometries), it may be preferable to map these to AttributeTypeImpl with a binding to the atomic Java type.::
    
    <typeBindings>
       <typeBinding>
            <name>PointPropertyType</name>
            <binding>com.vividsolutions.jts.geom.Point</binding>
        </typeBinding>
        <typeBinding>
            <name>PointType</name>
            <binding>com.vividsolutions.jts.geom.Point</binding>
         </typeBinding>
    </typeBindings>
