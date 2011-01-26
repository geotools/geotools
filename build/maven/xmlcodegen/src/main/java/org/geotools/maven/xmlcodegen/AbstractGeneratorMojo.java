package org.geotools.maven.xmlcodegen;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geotools.xml.Schemas;
import org.geotools.xml.XSD;

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
	 * 
	 * @parameter 
	 * @required
	 */
	protected File schemaLocation;
	
	/**
	 * Directory containing xml schemas, default is ${basedir}/src/main/xsd.
	 *
	 * @parameter expression="${basedir}/src/main/xsd"
	 */
	protected File schemaSourceDirectory;
	
	/**
	 * Additional directories used to locate included and imported schemas.
	 * 
	 * @parameter
	 */
	protected File[] schemaLookupDirectories;
	
	/**
	 * The destination package of the generated source files in the standard dot-seperated 
	 * naming format.
	 * 
	 * @parameter
	 */
	protected String destinationPackage;
	
	/**
	 * Directory to output generated files to. Default is ${project.build.sourceDirectory}
	 * <p>
	 * {@link Deprecated}, use one of {@link #sourceOutputDirectory} or {@link #testOutputDirectory}
	 * </p>
	 * @parameter expression="${project.build.sourceDirectory}"
	 */
	protected File outputDirectory;
	
	/**
     * Directory to output generated source files to. Default is 
     * ${project.build.sourceDirectory}
     * 
     * @parameter expression="${project.build.sourceDirectory}"
     */
	protected File sourceOutputDirectory;
	/**
     * Directory to output generated test files to. Default is 
     * ${project.build.testDirectory}
     * 
     * @parameter expression="${project.build.testSourceDirectory}"
     */
	protected File testOutputDirectory;
	/**
	 * Flag controlling wether files should overide files that already 
	 * exist with the same name. False by default.
	 * 
	 * @param expression="false"
	 */
	protected boolean overwriteExistingFiles;
	
	/**
     * List of names of attributes, elements, and types to include, if unset all will
     * be generated.
     * 
     * @parameter
     */
    protected String[] includes;
    /**
     * The prefix to use for the targetNamespace.
     * 
     * @parameter
     */
    protected String targetPrefix;
	
	/**
     * The currently executing project
     * 
     * @parameter expression="${project}"
     */
    MavenProject project;
    
    /**
     * The local maven repository
     * 
     * @parameter expression="${localRepository}" 
     */
    ArtifactRepository localRepository;
    
    /**
     * Remote maven repositories
     *  
     * @parameter expression="${project.remoteArtifactRepositories}" 
     */
    List remoteRepositories;
    
    /** 
     * @component 
     */
    ArtifactFactory artifactFactory;
    
    /**
     * @component
     */
    ArtifactResolver artifactResolver;
    
    /**
     * @component
     */
    ArtifactMetadataSource artifactMetadataSource;
    
    /**
     * The classpath elements of the project.
     *
     * @parameter expression="${project.runtimeClasspathElements}"
     * @required
     * @readonly
     */
    List classpathElements;
    
    /**
     * Flag to control whether to include GML libraries on classpath when running.
     * @parameter expression="true"
     */
    boolean includeGML;
    
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
		List artifacts = new ArrayList();
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
	
		Set urls = new HashSet();
		for ( Iterator a = artifacts.iterator(); a.hasNext(); ) {
			Artifact artifact = (Artifact) a.next();
			getLog().debug("Attempting to dynamically resolve: " + artifact);
			try {
			    Set resolvedArtifacts = project.createArtifacts( artifactFactory, null, null);
			    //artifactResolver.resolve( artifact, remoteRepositories, localRepository );
				ArtifactResolutionResult result = artifactResolver.resolveTransitively(resolvedArtifacts, artifact, remoteRepositories, localRepository, artifactMetadataSource);
				resolvedArtifacts = result.getArtifacts();
				
				for ( Iterator ra = resolvedArtifacts.iterator(); ra.hasNext(); ) {
				    Artifact resolvedArtifact = (Artifact) ra.next();
				    urls.add( resolvedArtifact.getFile().toURI().toURL() );    
				}
				
			} 
			catch( Exception e ) {
				getLog().warn( "Unable to resolve " + artifact.getId(), e );
			}
		}
		
		ClassLoader ext = 
			new URLClassLoader( (URL[]) urls.toArray( new URL[ urls.size() ] ), getClass().getClassLoader() );
		StringBuffer sb = new StringBuffer();
		sb.append( "Using following classpath for XSD lookup: ");
		for ( Iterator u = urls.iterator(); u.hasNext(); ) {
		    sb.append( u.next().toString() );
		}
		getLog().debug(sb.toString());
		
		//use extended classloader to load up configuration classes to load schema files
		// with
		final List xsds = new ArrayList();
                xsds.add( "org.geotools.xml.XML" );
                xsds.add( "org.geotools.xlink.XLINK" );
                
                if (includeGML) {
                    xsds.add( "org.geotools.gml2.GML" );
                    xsds.add( "org.geotools.gml3.GML" );
                    xsds.add( "org.geotools.filter.v1_0.OGC" );
                    xsds.add( "org.geotools.filter.v1_1.OGC" );
                }
		
		for ( int i = 0; i < xsds.size(); i++ ) {
			String className = (String) xsds.get( i );
			try {
				Class clazz = ext.loadClass( className );
				Method m = clazz.getMethod("getInstance", null);
				Object xsd = m.invoke(null, null);
				xsds.set( i, xsd );
			} 
			catch (Exception e) {
				getLog().warn( "Unable to load " + className);
				getLog().debug(e);
				xsds.set( i , null );
			}
		}
		
		//add a schema locator which uses the xsd objects to get at the schemas
		XSDSchemaLocator locator = new XSDSchemaLocator() {

            public XSDSchema locateSchema(XSDSchema schema, String namespaceURI,
                String rawSchemaLocationURI, String resolvedSchemaLocationURI) {
                
                for ( Iterator x = xsds.iterator(); x.hasNext(); ) {
                    XSD xsd = (XSD) x.next();
                    if ( xsd == null ) {
                        continue;
                    }
                    if ( xsd.getNamespaceURI().equals( namespaceURI ) ) {
                        try {
                            return xsd.getSchema();
                        } 
                        catch (IOException e) {
                            getLog().warn("Error occured locating schema: " + namespaceURI, e);
                        }
                    }
                }
             
                getLog().warn( "Could not locate schema for: " + namespaceURI );
                return null;
            }
		    
		};
		
		//add a location resolver which checks the schema source directory
		XSDSchemaLocationResolver locationResolver = new XSDSchemaLocationResolver() {

			public String resolveSchemaLocation(
				XSDSchema schema, String namespaceURI, String schemaLocation 
			) {
			
                                if ( schemaLocation == null ) {
                                   getLog().warn("Null location for " + namespaceURI );
                                   return null;
                                }
                                
				//check location directlry
				File file = new File( schemaLocation );  
				if ( file.exists() ) {
					getLog().debug( "Resolving " + schemaLocation + " to " + schemaLocation );
					return schemaLocation;
				}
				
				String fileName = new File( schemaLocation ).getName();
				
				//check under teh schema source directory
				file = new File( schemaSourceDirectory, fileName ); 
				if ( file.exists() ) {
					getLog().debug( "Resolving " + schemaLocation + " to " + file.getAbsolutePath() );
					return file.getAbsolutePath();
				}
				
				//check hte lookup directories
				if ( schemaLookupDirectories != null ) {
					for ( int i = 0; i < schemaLookupDirectories.length; i++ ) {
						File schemaLookupDirectory = schemaLookupDirectories[ i ];
						file = new File( schemaLookupDirectory, fileName );
						if ( file.exists() ) {
							getLog().debug( "Resolving " + schemaLocation + " to " + file.getAbsolutePath() );
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
			getLog().info( "Parsing schema: " + schemaLocation );
			xsdSchema = 
				Schemas.parse( 
					schemaLocation.getAbsolutePath(),
					new XSDSchemaLocator[]{ locator } , new XSDSchemaLocationResolver[]{ locationResolver }
				);
			
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
