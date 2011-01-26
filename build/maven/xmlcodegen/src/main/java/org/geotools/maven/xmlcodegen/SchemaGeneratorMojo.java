package org.geotools.maven.xmlcodegen;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.xsd.XSDSchema;
import org.opengis.feature.type.Schema;


/**
 * Generates an instance of {@link org.opengis.feature.type.Schema } from an xml schema.
 * 
 * @goal generateSchema
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public class SchemaGeneratorMojo extends AbstractGeneratorMojo {

	/**
	 * Flag controlling wether complex types from the schema should be included.
	 * @parameter expression="true"
	 */
	boolean includeComplexTypes;
	/**
	 * Flag controlling wether simple types from the schema should be included.
	 * @parameter expression="true"
	 */
	boolean includeSimpleTypes;
	/**
	 * Flag controlling wether complex types should be composed of geotools 
	 * attribute descriptors which mirror the xml schema particles.
	 * @parameter expression="true"
	 */
	boolean followComplexTypes;
	/**
	 * List of schema classes to use as imports
	 * @parameter
	 */
	String[] imports;
	/**
         * Flag controlling wether paths are printed out as the generator recurses 
         * through the schema.
         * @parameter expression="false"
         */
	boolean printRecursionPaths;
	/**
	 * Controls how far the generator will recurse into the schema.
	 * @parameter
	 */
	int maxRecursionDepth;
	/**
	 * List of explicit bindings from XSD type to fully-qualified class name.
	 * Namespace defaults to the target schema namespace.
	 * @parameter
	 */
	private TypeBinding[] typeBindings;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
    	XSDSchema schema = schema();
    	if ( schema == null ) 
    		return;
    	
    	SchemaGenerator generator = new SchemaGenerator(schema);
        
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
		    List urls = new ArrayList();
	        for ( Iterator d = project.getDependencies().iterator(); d.hasNext(); ) {
	            Dependency dep = (Dependency) d.next();
	            
	            Artifact artifact = artifactFactory.createArtifact( 
                    dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), null, dep.getType()
                );
	            try {
	                artifactResolver.resolve( artifact, remoteRepositories, localRepository );
	                urls.add( artifact.getFile().toURI().toURL() );
	            } 
	            catch( Exception e ) {
	                getLog().error( "Unable to resolve " + artifact.getId() );
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
	            new URLClassLoader( (URL[]) urls.toArray( new URL[ urls.size() ] ), getClass().getClassLoader() );

		    for ( int i = 0; i < imports.length; i++ ) {
		        String schemaClassName = imports[i];
		        Class schemaClass = null;
		        try {
                    schemaClass = ext.loadClass(schemaClassName);
                } 
		        catch (ClassNotFoundException e) {
		            getLog().error("Could note load class: " + schemaClassName);
                    return;
		        }
		        
		        getLog().info("Loading import schema: " + schemaClassName);
		        Schema gtSchema = null;
		        try {
                    gtSchema = (Schema) schemaClass.newInstance();
                } 
		        catch( Exception e ) {
		            getLog().error("Could not insantiate class: " + schemaClass.getName());
		            return;
		        }
		        
		        if ( gtSchema != null ) {
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
