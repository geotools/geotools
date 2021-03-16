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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.xsd.XSDSchema;
import org.opengis.feature.type.Schema;

/**
 * Generates an instance of {@link org.opengis.feature.type.Schema } from an xml schema.
 *
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
@Mojo(name = "generateSchema")
public class SchemaGeneratorMojo extends AbstractGeneratorMojo {

	/**
	 * Flag controlling whether complex types from the schema should be included.
	 */
	@Parameter(defaultValue = "true")
	boolean includeComplexTypes;
	/**
	 * Flag controlling whether simple types from the schema should be included.
	 */
	@Parameter(defaultValue = "true")
	boolean includeSimpleTypes;
	/**
	 * Flag controlling whether complex types should be composed of geotools 
	 * attribute descriptors which mirror the xml schema particles.
	 */
	@Parameter(defaultValue = "true")
	boolean followComplexTypes;
	/**
	 * List of schema classes to use as imports
	 */
	@Parameter
	String[] imports;
	/**
	 * Flag controlling whether paths are printed out as the generator recurses
	 * through the schema.
	 */
	@Parameter(defaultValue = "false")
	boolean printRecursionPaths;
	/**
	 * Controls how far the generator will recurse into the schema.
	 */
	@Parameter
	int maxRecursionDepth;
	/**
	 * List of explicit bindings from XSD type to fully-qualified class name.
	 * Namespace defaults to the target schema namespace.
	 */
	@Parameter
	private TypeBinding[] typeBindings;
	
	/**
	 * Support types that are cyclically defined, such as gmd:CI_CitationType from GML 3.2. Types in
	 * generated Schema file will be defined using AbstractLazyAttributeType and
	 * AbstractLazyComplexType.
	 */
	@Parameter(defaultValue = "false")
	boolean cyclicTypeSupport;

	/**
	 * The entry point to Maven Artifact Resolver, i.e. the component doing all the work.
	 */
	@Component
	private RepositorySystem repoSystem;

	/**
	 * The current repository/network configuration of Maven.
	 */
	@Parameter(defaultValue = "${repositorySystemSession}", readonly = true)
	private RepositorySystemSession repoSession;

	@Override
    public void execute() throws MojoExecutionException, MojoFailureException {
    	XSDSchema schema = schema();
    	if ( schema == null ) 
    		return;
    	
    	SchemaGenerator generator;
    	if (cyclicTypeSupport) {
    	    generator = new CycleSchemaGenerator(schema);
    	} else {
    	    generator = new SchemaGenerator(schema);
    	}
        
    	generator.setComplexTypes( includeComplexTypes );
        generator.setSimpleTypes( includeSimpleTypes );
    	generator.setOverwriting( overwriteExistingFiles );
		//generator.setLocation( outputDirectory.getAbsolutePath() );
		generator.setSourceLocation(sourceOutputDirectory.getAbsolutePath());
        generator.setTestLocation(testOutputDirectory.getAbsolutePath());
        generator.setResourceLocation(((Resource)project.getBuild().getResources().get( 0 )).getDirectory());
		generator.setFollowComplexTypes(followComplexTypes);
		generator.setIncludes( includes );
        generator.setMaxRecursionDepth(maxRecursionDepth);
        generator.setPrintRecursionPaths(printRecursionPaths);
        generator.setTypeBindings(typeBindings);
		
		if (imports != null) {
		    //build a url classload from dependencies
		    List<URL> urls = new ArrayList<>();
            for (Dependency dep : project.getDependencies()) {

									Artifact artifact = new DefaultArtifact(
											dep.getGroupId(), dep.getArtifactId(), null, dep.getVersion(), dep.getType());
									ArtifactRequest request = new ArtifactRequest();
									request.setArtifact(artifact);
									request.setRepositories(remoteRepositories);
                try {
										ArtifactResult result = repoSystem.resolveArtifact( repoSession, request );
										urls.add(result.getArtifact().getFile().toURI().toURL());
                } catch (Exception e) {
                    getLog().error("Unable to resolve " + artifact.getArtifactId());
                }
            }
	        
	        //add compiled classes to classloader
	        try {
	            urls.add( new File(project.getBuild().getOutputDirectory()).toURI().toURL() );    
	        }
	        catch( MalformedURLException e ) {
	            getLog().error("Bad url: " + project.getBuild().getOutputDirectory() );
	            return;
	        }
	        
	        
	        ClassLoader ext = 
	            new URLClassLoader(urls.toArray( new URL[ urls.size() ] ), getClass().getClassLoader() );

            for (String schemaClassName : imports) {
                Class<?> schemaClass = null;
                try {
                    schemaClass = ext.loadClass(schemaClassName);
                } catch (ClassNotFoundException e) {
                    getLog().error("Could not load class: " + schemaClassName);
                    return;
                }

                getLog().info("Loading import schema: " + schemaClassName);
                Schema gtSchema = null;
                try {
                    gtSchema = (Schema) schemaClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    getLog().error("Could not insantiate class: " + schemaClass.getName());
                    return;
                }

                if (gtSchema != null) {
                    generator.addImport(gtSchema);
                }
            }
		    
		}
		
        try {
			generator.generate( );
		} 
        catch (Exception e) {
        	getLog().error( e );
		}
    }
    
}
