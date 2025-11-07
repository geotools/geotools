
Building FAQ
------------

What version of JDK?
^^^^^^^^^^^^^^^^^^^^

Our policy is waiting for the majority of our users before migrating to a new version of the Java
language. In general we are held up by the slow migration of Java Enterprise Edition environments
such as websphere.

GeoTools 34.x uses Java 17.

How do I build from source code?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Complete build instructions are provided in the user guide:

* :doc:`/build/index`

GeoTools makes use of the Maven build system (in part to help us reused code from
a number of other Java projects).

To build all the modules::
 
  mvn install -Dall

To load the modules into the eclipse IDE.

1. Use :menuselection:`Windows --> Preferences` to open the Preference Dialog. 
   Using the tree on the left navigate to the Java > Build path > Classpath Variables preference
   Page.
   
2. Add an **M2_REPO** classpath variable pointing to your local repository
   where maven downloads jars.

    ==================  ========================================================
       PLATFORM           LOCAL REPOSITORY
    ==================  ========================================================
       Windows XP:      :file:`C:\\Documents and Settings\\Jody\\.m2\\repository`
       Windows:         :file:`C:\\Users\\Jody\.m2\\repository`
       Linux and Mac:   :file:`~/.m2/repository`
    ==================  ========================================================

2. Generate the ``.project`` and ``.classpath`` files needed for eclipse::
      
      mvn eclipse:eclipse -Dall

4. You can now use the eclipse import wizard to load existing projects.

Why is Maven 3 Slower?
^^^^^^^^^^^^^^^^^^^^^^

Maven 3 is not faster out of the box with the default settings.

However what is new is that you can ask it to use more than one core::
  
  mvn install -Dall -T 2C
  
The above asks the build to go in "threaded" mode; using two threads for each core.

What the fastest build?
^^^^^^^^^^^^^^^^^^^^^^^

This is the fastest build on my machine::

  mvn install -DskipTests -o -T 2C

The above options:

  + install (without clean) only re-compiles modified code 
  + no profiles or flags are used to build optional code; only the core library
    is built 
  + ``skipTests`` - the tests are still built; they are just not run 
  + ``o`` - allows the build to work "offline" (thus no external servers are
    checked during the build) 
  + T 2C - builds with two threads per core

I use this configuration to quickly push all local changes into my local maven repository so I can
test in a downstream application such as uDig or GeoServer.

How do I create an executable jar for my GeoTools app?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If you're familiar with Maven you might have used the `assembly plugin
<http://maven.apache.org/plugins/maven-assembly-plugin/>`_ to create self-contained, executable jars.
Note that this does not work for GeoTools without additional configuration. The problem is that GeoTools
plugins are connected to the library using one or more files in the ``META-INF/services`` directory
with the name of the plugin interface being registered. The assembly plugin just
copies files with the same name over the top of each other rather than merging
their contents.

The good news is that the `Maven shade plugin
<http://maven.apache.org/plugins/maven-shade-plugin/index.html>`_ can be used
instead and it will correctly merge the ``META-INF/services`` files from each of
the GeoTools modules used by your application.

The pom below will create an executable jar for the GeoTools :doc:`/tutorial/quickstart/index` module which includes all of the required
GeoTools modules and their dependencies.

.. sourcecode:: xml

  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>org.geotools.demo</groupId>
    <artifactId>quickstart</artifactId>
    <packaging>jar</packaging>
    <version>1.0</version>
    <name>GeoTools Quickstart example</name>
    <url>http://geotools.org</url>

    <properties>
        <gt.version>34.0</gt.version>
    </properties>

    <build>
        <plugins>
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <encoding>UTF-8</encoding>
                    <release>17</release>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.6.1</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <filters>
                        <!-- filter signed jars in the dependencies -->
                                <filter>
                                    <artifact>*:*</artifact>
                                    <excludes>
                                        <exclude>META-INF/*.SF</exclude>
                                        <exclude>META-INF/*.DSA</exclude>
                                        <exclude>META-INF/*.RSA</exclude>
                                    </excludes>
                                </filter>
                            </filters>
                            <shadedArtifactAttached>true</shadedArtifactAttached>
                            <shadedClassifierName>shaded</shadedClassifierName>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>org.geotools.demo.Quickstart</mainClass>
                                </transformer>

                                <!-- merges the various GeoTools META-INF/services files  (e.g. referencing plugins) -->
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                                <!-- merges META-INF/services/ entries instead of overwriting -->
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>

                                <!-- merges META-INF/org.eclipse.imagen.registryFile.imagen entries instead of overwriting -->
                                <transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
                                  <resource>META-INF/org.eclipse.imagen.registryFile.imagen</resource>
                                </transformer>
                                <!-- merges META-INF/registryFile.imagen entries instead of overwriting -->
                                <transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
                                  <resource>META-INF/registryFile.imagen</resource>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

  <dependencyManagement>
    <dependencies>
      <!-- Import the GeoTools BOM -->
      <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-bom</artifactId>
        <version>${gt.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-shapefile</artifactId>
        </dependency>
        <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-epsg-hsql</artifactId>
        </dependency>
        <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-swing</artifactId>
        </dependency>
    </dependencies>
  </project>
