/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.maven.xmlcodegen;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.factory.DefaultArtifactFactory;
import org.apache.maven.repository.legacy.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.impl.ArtifactResolver;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geotools.xsd.Schemas;
import org.geotools.xsd.XSD;

/**
 * Generates the bindings and utility classes used to parse xml documents 
 * for a particular schema. 
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public abstract class AbstractGeneratorMojo extends AbstractMojo {

	/**
	 * The .xsd file defining the schema to generate bindings for.
	 */
	@Parameter(required = true)
	protected File schemaLocation;
	
	/**
	 * Directory containing xml schemas, default is ${basedir}/src/main/xsd.
	 */
	@Parameter(defaultValue = "${basedir}/src/main/xsd")
	protected File schemaSourceDirectory;
	
	/**
	 * Additional directories used to locate included and imported schemas.
	 */
	@Parameter
	protected File[] schemaLookupDirectories;
	
	/**
	 * The destination package of the generated source files in the standard dot-seperated 
	 * naming format.
	 */
	@Parameter
	protected String destinationPackage;
	
	/**
	 * Directory to output generated files to. Default is {@code ${project.build.sourceDirectory}}
	 *
	 * @eprecated use one of {@link #sourceOutputDirectory} or {@link #testOutputDirectory}
	 */
	@Deprecated
	@Parameter(defaultValue = "${project.build.sourceDirectory}")
	protected File outputDirectory;
	
	/**
	 * Directory to output generated source files to. Default is
	 * {@code ${project.build.sourceDirectory}}
	 */
	@Parameter(defaultValue = "${project.build.sourceDirectory}")
	protected File sourceOutputDirectory;
	/**
     * Directory to output generated test files to. Default is 
     * {@code ${project.build.testDirectory}}
     *
     */
	@Parameter(defaultValue = "${project.build.testSourceDirectory}")
	protected File testOutputDirectory;
	/**
	 * Flag controlling whether files should override files that already
	 * exist with the same name. False by default.
	 */
	@Parameter(defaultValue = "false")
	protected boolean overwriteExistingFiles;
	
	/**
     * List of names of attributes, elements, and types to include, if unset all will
     * be generated.
     */
	@Parameter
    protected String[] includes;
    /**
     * The prefix to use for the targetNamespace.
     *
     */
		@Parameter
    protected String targetPrefix;
	
	/**
     * The currently executing project.
     */
	@Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;
    
    /**
     * The local maven repository.
     */
		@Parameter(defaultValue = "${localRepository}", required = true, readonly = true)
    ArtifactRepository localRepository;
    
    /**
     * Remote maven repositories.
     */
		@Parameter(defaultValue = "${project.remoteArtifactRepositories}", required = true, readonly = true)
    List remoteRepositories;
    

		@Component(role=ArtifactFactory.class)
		DefaultArtifactFactory artifactFactory;
    

    @Component
		ArtifactResolver artifactResolver;

	@Component
    ArtifactMetadataSource artifactMetadataSource;
    
    /**
     * The classpath elements of the project.
     */
    @Parameter(required = true, readonly = true, defaultValue = "${project.runtimeClasspathElements}")
    List classpathElements;
    
    /**
     * Flag to control whether to include GML libraries on classpath when running.
     */
		@Parameter(defaultValue = "true")
    boolean includeGML;
    
    /**
     * Treat all relative schema references (include and import) as relative to the schema (XSD)
     * resource in which they are found, rather than looking for them in compiled classes or
     * schemaLookupDirectories. This requires all included/imported schemas to be present in the
     * expected relative filesystem location. The main advantage of this approach is that it
     * supports schema files that have cyclic dependencies (e.g. GML 3.2).
     *
     */
		@Parameter(defaultValue = "false")
    boolean relativeSchemaReference;
    
    protected XSDSchema schema() {
    
    	getLog().info( artifactFactory.toString() );
    	
    	//check schema source
		if ( !schemaSourceDirectory.exists() ) {
			getLog().error( schemaSourceDirectory.getAbsolutePath() + " does not exist" );
			return null;
		}
		
		//check schema
		if ( !schemaLocation.exists() ) {
			//check relative to schemaSourceDirectory
			schemaLocation = new File( schemaSourceDirectory, schemaLocation.getName() );
			if ( !schemaLocation.exists() ) {
				getLog().error( "Could not locate schema: " + schemaLocation.getName() );
				return null;
			}
		}
		
		//build an "extended" classloader for "well-known
		List<Artifact> artifacts = new ArrayList<>();
		if (includeGML) {
        		artifacts.add( 
        			artifactFactory.createArtifact( 
        				"org.geotools", "gt2-xml-gml2", "2.7-SNAPSHOT", "compile", "jar"
        
                    )
        		);
        		artifacts.add( 
        			artifactFactory.createArtifact( 
        				"org.geotools", "gt2-xml-gml3", "2.7-SNAPSHOT", "compile", "jar"
        			) 
        		);
        		artifacts.add( 
        			artifactFactory.createArtifact( 
        				"org.geotools", "gt2-xml-filter", "2.7-SNAPSHOT", "compile", "jar"
        			) 
        		);
        		artifacts.add( 
        			artifactFactory.createArtifact( 
        				"org.geotools", "gt2-xml-sld","2.7-SNAPSHOT", "compile", "jar"
        			) 
        		);
		}
	
		Set<URL> urls = new HashSet<>();
        for (Artifact artifact : artifacts) {
            getLog().debug("Attempting to dynamically resolve: " + artifact);
            try {
                Set<Artifact> resolvedArtifacts = project.createArtifacts(artifactFactory, null, null);
                //artifactResolver.resolve( artifact, remoteRepositories, localRepository );
                ArtifactResolutionResult result =
                        artifactResolver.resolveTransitively(resolvedArtifacts, artifact,
                                remoteRepositories, localRepository, artifactMetadataSource);

                resolvedArtifacts = result.getArtifacts();

                for (Object o : resolvedArtifacts) {
                    Artifact resolvedArtifact = (Artifact) o;
                    urls.add(resolvedArtifact.getFile().toURI().toURL());
                }

            } catch (Exception e) {
                getLog().warn("Unable to resolve " + artifact.getId(), e);
            }
        }
		
		ClassLoader ext = 
			new URLClassLoader(urls.toArray( new URL[ urls.size() ] ), getClass().getClassLoader() );
		StringBuffer sb = new StringBuffer();
		sb.append( "Using following classpath for XSD lookup: ");
        for (URL url : urls) {
            sb.append(url.toString());
        }
		getLog().debug(sb.toString());
		
		//use extended classloader to load up configuration classes to load schema files
		// with
		final List<String> xsdNames = new ArrayList<>();
		final List<XSD> xsds = new ArrayList<>();
                xsdNames.add( "org.geotools.xsd.XML" );
                xsdNames.add( "org.geotools.xlink.XLINK" );
                
                if (includeGML) {
                    xsdNames.add( "org.geotools.gml2.GML" );
                    xsdNames.add( "org.geotools.gml3.GML" );
                    xsdNames.add( "org.geotools.filter.v1_0.OGC" );
                    xsdNames.add( "org.geotools.filter.v1_1.OGC" );
                }
		
		for ( int i = 0; i < xsdNames.size(); i++ ) {
			String className = xsdNames.get( i );
			try {
				Class<?> clazz = ext.loadClass( className );
				Method m = clazz.getMethod("getInstance", null);
				XSD xsd = (XSD) m.invoke(null, null);
				xsds.add(xsd);
			} 
			catch (Exception e) {
				getLog().warn( "Unable to load " + className);
				getLog().debug(e);
				xsdNames.set( i , null );
			}
		}
		
		//add a schema locator which uses the xsd objects to get at the schemas
		XSDSchemaLocator locator = new XSDSchemaLocator() {

            @Override
            public XSDSchema locateSchema(XSDSchema schema, String namespaceURI,
                String rawSchemaLocationURI, String resolvedSchemaLocationURI) {

                for (XSD xsd : xsds) {
                    if (xsd == null) {
                        continue;
                    }
                    if (xsd.getNamespaceURI().equals(namespaceURI)) {
                        try {
                            return xsd.getSchema();
                        } catch (IOException e) {
                            getLog().warn("XSDSchemaLocator: Error occurred locating schema: " + namespaceURI, e);
                        }
                    }
                }
             
                getLog().warn( "XSDSchemaLocator: Could not locate schema for: " + namespaceURI );
                return null;
            }
		    
		};
		
		//add a location resolver which checks the schema source directory
		XSDSchemaLocationResolver locationResolver = new XSDSchemaLocationResolver() {

			@Override
            public String resolveSchemaLocation(
				XSDSchema schema, String namespaceURI, String schemaLocation 
			) {
			
                                if ( schemaLocation == null ) {
                                   getLog().warn("Null location for " + namespaceURI );
                                   return null;
                                }
                                
				//check location directly
				File file = new File( schemaLocation );  
				if ( file.exists() ) {
					getLog().debug( "Resolving " + schemaLocation + " to " + schemaLocation );
					return schemaLocation;
				}
				
				String fileName = new File( schemaLocation ).getName();
				
				//check under the schema source directory
				file = new File( schemaSourceDirectory, fileName ); 
				if ( file.exists() ) {
					getLog().debug( "Resolving " + schemaLocation + " to " + file.getAbsolutePath() );
					return file.getAbsolutePath();
				}
				
				//check the lookup directories
				if ( schemaLookupDirectories != null ) {
                    for (File schemaLookupDirectory : schemaLookupDirectories) {
                        file = new File(schemaLookupDirectory, fileName);
                        if (file.exists()) {
                            getLog().debug("Resolving " + schemaLocation + " to " + file.getAbsolutePath());
                            return file.getAbsolutePath();
                        }

                    }
				}
				
				getLog().warn( "Could not resolve location for: " + fileName );
				return null;
			}
			
		};
		
		//parse the schema
		XSDSchema xsdSchema = null;
		try {
			getLog().info("Parsing schema: " + schemaLocation);
			if (relativeSchemaReference) {
				xsdSchema = Schemas.parse(schemaLocation.getAbsolutePath(), Collections.emptyList(),
						Collections.singletonList(new XSDSchemaLocationResolver() {
							@Override
                            public String resolveSchemaLocation(XSDSchema xsdSchema,
									String namespaceURI, String schemaLocationURI) {
								try {
									URI contextUri = new URI(xsdSchema.getSchemaLocation());
									if (contextUri.isOpaque()) {
										// probably a jar:file: URL, which is opaque and thus not
										// supported by URI.resolve()
										URL contextUrl = new URL(xsdSchema.getSchemaLocation());
										return (new URL(contextUrl, schemaLocationURI)).toString();
									} else {
										return contextUri.resolve(schemaLocationURI).toString();
									}
								} catch (URISyntaxException | MalformedURLException e) {
									throw new RuntimeException(e);
								}
                            }
						}));
			} else {
				xsdSchema = Schemas.parse(schemaLocation.getAbsolutePath(),
						Collections.singletonList(locator),
						Collections.singletonList(locationResolver));
			}
			
			if ( xsdSchema == null ) {
				throw new NullPointerException();
			}
		} 
		catch (Exception e) {
			getLog().error( "Failed to parse schema");
			getLog().error( e );
			return null;
		}	
		
		//set the target prefix if set
        if (targetPrefix != null) {
            xsdSchema.getQNamePrefixToNamespaceMap().put(targetPrefix,xsdSchema.getTargetNamespace());
        }
        
        //do some sanity checks on the schema
        if ( Schemas.getTargetPrefix(xsdSchema) == null ) {
            String msg = "Unable to determine a prefix for the target namespace " +
                "of the schema Either  include a mapping in the schema or manually " +
                "specify one with the 'targetPrefix' parameter.";
            throw new RuntimeException(msg);
        }
		
		return xsdSchema;
    }
    
}
