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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.artifact.MavenMetadataSource;
import org.eclipse.xsd.XSDSchema;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.XSD;

/**
 * Generates the bindings and utility classes used to parse xml documents 
 * for a particular schema. 
 * 
 *
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
@Mojo(name = "generate")
public class BindingGeneratorMojo extends AbstractGeneratorMojo {

	/**
     * Flag controlling whether a parser configuration ( {@link Configuration} )
     * the default is {@code true}.
     */
	@Parameter(defaultValue = "true")
    boolean generateConfiguration;
    
    /**
     * Flag controlling whether an xsd ({@link XSD} subclass should be generated.
     */
    @Parameter(defaultValue = "true")
    boolean generateXsd;
    
    /**
     * Flag controlling whether bindings for attributes should be generated, default is
     * {@code false}.
     */
    @Parameter(defaultValue = "false")
    boolean generateAttributeBindings;
    
    /**
     * Flag controlling whether bindings for elements should be generated, default is
     * {@code false}.
     */
    @Parameter(defaultValue = "false")
    boolean generateElementBindings;
    
    /**
     * Flag controlling whether bindings for types should be generated, default is
     * {@code true}.
     */
    @Parameter(defaultValue = "true")
    boolean generateTypeBindings;
	
    /**
     * Flag controlling whether test for bindings should be generated, default is
     * false.
     */
    @Parameter(defaultValue = "false")
    boolean generateTests;
    
    /**
     * List of constructor arguments that should be supplied to generated bindings.
     * Each argument is a 'name','type','mode' triplet. 'name' and 'type' declare 
     * the name and class of the argument respectively. 'mode' can be set to
     * "member", or "parent". If set to "member" the argument will be set to a 
     * member of the binding. If set to "parent" the argument will passed through
     * to the call to the super constructor. The default is "member"
     *
     */
    @Parameter
    BindingConstructorArgument[] bindingConstructorArguments;
    
    /**
     * The base class for complex bindings. If unspecified {@link AbstractComplexBinding}
     * is used.
     */
    @Parameter(defaultValue = "org.geotools.xsd.AbstractComplexBinding")
    String complexBindingBaseClass;
    
    /**
     * The base class for simple bindings. If unspecified {@link AbstractSimpleBinding}
     * is used.
     */
    @Parameter(defaultValue = "org.geotools.xsd.AbstractSimpleBinding")
    String simpleBindingBaseClass;
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
		
    	XSDSchema xsdSchema = schema();
    	if ( xsdSchema == null ) {
    		return;
    	}
		
		BindingGenerator generator = new BindingGenerator();
		generator.setGenerateAttributes( generateAttributeBindings );
		generator.setGenerateElements( generateElementBindings );
		generator.setGenerateTypes( generateTypeBindings );
		generator.setGenerateConfiguration( generateConfiguration );
                generator.setGenerateXsd(generateXsd);
		generator.setGenerateTests(generateTests);
		generator.setOverwriting( overwriteExistingFiles );
		//generator.setLocation( outputDirectory.getAbsolutePath() );
		generator.setSourceLocation(sourceOutputDirectory.getAbsolutePath());
		generator.setTestLocation(testOutputDirectory.getAbsolutePath());
		generator.setResourceLocation(((Resource)project.getBuild().getResources().get( 0 )).getDirectory());
		generator.setSchemaSourceDirectory(schemaSourceDirectory);
		
		try {
		    Class c = Class.forName(complexBindingBaseClass);
		    generator.setComplexBindingBaseClass(c);
		}
		catch( ClassNotFoundException e ) {
		    getLog().error("Could not load class: " + complexBindingBaseClass);
		    return;
		}
		try {
            Class c = Class.forName(simpleBindingBaseClass);
            generator.setSimpleBindingBaseClass(c);
        }
        catch( ClassNotFoundException e ) {
            getLog().error("Could not load class: " + simpleBindingBaseClass);
            return;
        }
		
		if ( schemaLookupDirectories != null ) {
		    generator.setSchemaLookupDirectories(schemaLookupDirectories);
		}
	
		if ( destinationPackage != null ) {
			generator.setPackageBase( destinationPackage );
		}
		
		//list of urls to use as class loading locations
		Set<URL> urls = new HashSet<>();
		
		try {
		    //get the ones from the project
			List l = project.getCompileClasspathElements();
            for (Object item : l) {
                String element = (String) item;
                File d = new File(element);

                if (d.exists() && d.isDirectory()) {
                    urls.add(d.toURI().toURL());
                }
            }
			
			//get the ones from project dependencies
			List d = project.getDependencies();

            for (Object value : d) {
                Dependency dep = (Dependency) value;
                if ("jar".equals(dep.getType())) {
                    Artifact artifact = artifactFactory.createArtifact(
                            dep.getGroupId(), dep.getArtifactId(), dep.getVersion(),
                            dep.getScope(), dep.getType()
                    );
                    Set artifacts = project.createArtifacts(artifactFactory, null, null);
                    ArtifactResolutionResult result =
                            artifactResolver.resolveTransitively(artifacts, artifact,
                                    remoteRepositories, localRepository, artifactMetadataSource);
                    artifacts = result.getArtifacts();
                    for (Object o : artifacts) {
                        Artifact dartifact = (Artifact) o;
                        urls.add(dartifact.getFile().toURI().toURL());
                    }

                }
            }
			
		} catch (Exception e) {
			getLog().error( e );
			return;
		}
		
		ClassLoader cl = new URLClassLoader(urls.toArray( new URL[ urls.size() ] ));
		if ( bindingConstructorArguments != null ) {
			HashMap map = new HashMap();

            for (BindingConstructorArgument bindingConstructorArgument :
                    bindingConstructorArguments) {
                String name = bindingConstructorArgument.getName();
                String type = bindingConstructorArgument.getType();

                try {
                    bindingConstructorArgument.clazz = cl.loadClass(type);
                } catch (ClassNotFoundException e) {
                    getLog().error("Could not locate class:" + type);
                    return;
                }
            }
			
			generator.setBindingConstructorArguments( bindingConstructorArguments );
		}
		
		if ( includes != null && includes.length > 0 ) {
			Set<String> included = new HashSet<>( Arrays.asList( includes ) );
			getLog().info( "Including: " + included ); 
			generator.setIncluded( included );
		}
		
		getLog().info( "Generating bindings...");
		generator.generate( xsdSchema );
	}

}
