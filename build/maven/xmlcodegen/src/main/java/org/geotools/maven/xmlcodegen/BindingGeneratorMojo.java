package org.geotools.maven.xmlcodegen;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.xsd.XSDSchema;
import org.geotools.xml.XSD;

/**
 * Generates the bindings and utility classes used to parse xml documents 
 * for a particular schema. 
 * 
 * @goal generate 
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public class BindingGeneratorMojo extends AbstractGeneratorMojo {

	/**
     * Flag controlling wether a parser configuration ( {@link org.geotools.xml.Configuration} ) 
     * the default is true.
     * 
     * @parameter expression="true"
     */
    boolean generateConfiguration;
    
    /**
     * Flag controlling wether an xsd ({@link XSD} subclass should be generated.
     * 
     * @parameter expression="true"
     */
    boolean generateXsd;
    
    /**
     * Flag controlling wether bindings for attributes should be generated, default is
     * false.
     * 
     * @parameter expression="false"
     */
    boolean generateAttributeBindings;
    
    /**
     * Flag controlling wether bindings for eleements should be generated, default is
     * false.
     * 
     * @parameter expression="false"
     */
    boolean generateElementBindings;
    
    /**
     * Flag controlling wether bindings for types should be generated, default is
     * true.
     * 
     * @parameter expression="true"
     */
    boolean generateTypeBindings;
	
    /**
     * Flag controlling wether test for bindings should be generated, default is
     * false.
     * 
     * @parameter expression="false"
     */
    boolean generateTests;
    
    /**
     * List of constructor arguments that should be supplied to generated bindings.
     * Each argument is a 'name','type','mode' triplet. 'name' and 'type' declare 
     * the name and class of the argument respectivley. 'mode' can be set to 
     * "member", or "parent". If set to "member" the argument will be set to a 
     * member of the binding. If set to "parent" the argument will passed through
     * to the call to the super constructor. The default is "member"
     * 
     * @parameter
     */
    BindingConstructorArgument[] bindingConstructorArguments;
    
    /**
     * The base class for complex bindings. If unspecified {@link org.geotools.xml.AbstractComplexBinding}
     * is used.
     * 
     * @parameter expression="org.geotools.xml.AbstractComplexBinding"
     * 
     */
    String complexBindingBaseClass;
    
    /**
     * The base class for simple bindings. If unspecified {@link org.geotools.xml.AbstractSimpleBinding}
     * is used.
     * 
     * @parameter expression="org.geotools.xml.AbstractSimpleBinding"
     */
    String simpleBindingBaseClass;
    
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
		Set urls = new HashSet();
		
		try {
		    //get the ones from the project
			List l = project.getCompileClasspathElements();
			for ( Iterator i = l.iterator(); i.hasNext(); ) {
				String element = (String) i.next();
				File d = new File( element );
			
				if ( d.exists() && d.isDirectory() ) {
					urls.add( d.toURI().toURL() );
				}
			}
			
			//get the ones from project dependencies
			List d = project.getDependencies();
			
			for ( Iterator i = d.iterator(); i.hasNext(); ) {
			    Dependency dep = (Dependency) i.next();
			    if ( "jar".equals( dep.getType() ) ) {
			        Artifact artifact = artifactFactory.createArtifact( 
	                    dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), 
	                    dep.getScope(), dep.getType()
	                );
			        Set artifacts = project.createArtifacts( artifactFactory, null, null);
			        ArtifactResolutionResult result = 
			            artifactResolver.resolveTransitively(artifacts, artifact, remoteRepositories, localRepository, artifactMetadataSource);
			        artifacts = result.getArtifacts();
			        for ( Iterator a = artifacts.iterator(); a.hasNext(); ) {
			            Artifact dartifact = (Artifact) a.next();
			            urls.add(dartifact.getFile().toURI().toURL());
			        }
			        
			    }
			}
			
		} catch (Exception e) {
			getLog().error( e );
			return;
		}
		
		ClassLoader cl = new URLClassLoader( (URL[]) urls.toArray( new URL[ urls.size() ] ) );
		if ( bindingConstructorArguments != null ) {
			HashMap map = new HashMap();
			
			for ( int i = 0; i < bindingConstructorArguments.length; i++) {
				String name = bindingConstructorArguments[i].getName();
				String type = bindingConstructorArguments[i].getType();
				
				try {
				    bindingConstructorArguments[i].clazz = cl.loadClass( type );
				} catch (ClassNotFoundException e) {
					getLog().error( "Could not locate class:" + type );
					return;
				}
			}
			
			generator.setBindingConstructorArguments( bindingConstructorArguments );
		}
		
		if ( includes != null && includes.length > 0 ) {
			HashSet included = new HashSet( Arrays.asList( includes ) );
			getLog().info( "Including: " + included ); 
			generator.setIncluded( included );
		}
		
		getLog().info( "Generating bindings...");
		generator.generate( xsdSchema );
	}

}
