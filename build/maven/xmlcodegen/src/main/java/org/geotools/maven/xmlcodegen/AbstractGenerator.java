/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.maven.xmlcodegen;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchema;
import org.geotools.xsd.Schemas;


/**
 * Abstract base class for code generators.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public abstract class AbstractGenerator {
    static Logger logger = org.geotools.util.logging.Logging.getLogger(AbstractGenerator.class);

    /**
     * Package base
     */
    String packageBase;

    /**
     * location to write out files
     */
    //String location;
    String sourceLocation;
    String testLocation;
    String resourceLocation;

    /**
     * Flag determining if generator will overwrite existing files.
     */
    boolean overwriting = false;

    /**
     * Schema source directory
     */
    File schemaSourceDirectory;
    /**
     * lookup directories for schemas
     */
    File[] schemaLookupDirectories;

    Set included = null;
    
    /**
     * Sets the base package for generated classes.
     *
     * @param packageBase Dot seperate package name, or <code>null</code> for
     * no package.
     */
    public void setPackageBase(String packageBase) {
        this.packageBase = packageBase;
    }

    public String getPackageBase() {
        return packageBase;
    }

//    /**
//     * Sets the location to write out generated java classes.
//     *
//     * @param location A file path.
//     */
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public String getLocation() {
//        return location;
//    }
    
    /**
     * Sets the location to write out generated source files.
     *
     * @param sourceLocation A file path.
     */
    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }
    /**
     * Sets the location to write out generated test files.
     *
     * @param testLocation A file path.
     */
    public void setTestLocation(String testLocation) {
        this.testLocation = testLocation;
    }
    /**
     * Sets the location to write out generated resource files.
     *
     * @param resourceLocation A file path.
     */
    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    /**
     * Flag controlling the behaviour of the generator when a generated file
     * already exists.
     * <p>
     * If set to <code>true</code>, the generator will overwrite existing files.
     * if set to <code>false</code>, the generator will not overwrite the file
     * and issue a warning.
     * </p>
     *
     * @param overwriting overwrite flag.
     */
    public void setOverwriting(boolean overwriting) {
        this.overwriting = overwriting;
    }

    /**
     * Sets the single directory to lookup schemas.
     * 
     * @param schemaSourceDirectory A directory.
     */
    public void setSchemaSourceDirectory(File schemaSourceDirectory) {
        this.schemaSourceDirectory = schemaSourceDirectory;
    }
    
    /**
     * Sets the directories to use when attempting to locate a schema via a 
     * relative reference.
     * 
     * @param schemaLookupDirectories An array of directories.
     */
    public void setSchemaLookupDirectories(File[] schemaLookupDirectories) {
		this.schemaLookupDirectories = schemaLookupDirectories;
	}
   
    /**
     * Writes out a string to a file.
     * <p>
     * THe file written out is located under {@link #location}, with the path
     * generated from {@link #packageBase} appended.
     * </p>
     *
     * @param result Result to write to the files.
     * @param className The name of the file to write out.
     */
    protected void write(String result, String className, String baseLocation)
        throws IOException {
        //convert package to a path
        File location = outputLocation(baseLocation);

        location.mkdirs();
        location = new File(location, className + ".java");

        //check for existing file
        if (location.exists() && !overwriting) {
            logger.warning("Generated file: " + location + " already exists.");

            return;
        }

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(
                    location));

        if (packageBase != null) {
            out.write(("package " + packageBase + ";\n\n").getBytes(StandardCharsets.UTF_8));
        }

        out.write(result.getBytes(StandardCharsets.UTF_8));

        out.flush();
        out.close();
    }

    /**
     * Copies a file to the output location.
     * <p>
     * THe file written out is located under {@link #location}, with the path
     * generated from {@link #packageBase} appended.
     * </p>
     *
     * @param file The file to copy.
     */
    protected void copy(File file, String baseLocation) throws IOException {
        File dest = new File(outputLocation(baseLocation), file.getName());

        logger.info( "Copying " + file + " to " + dest );
        
        //check for existing file
        if (dest.exists() && !overwriting) {
            logger.warning("Generated file: " + dest + " already exists.");

            return;
        }

        InputStream in = new BufferedInputStream(new FileInputStream(file));
        OutputStream out = new BufferedOutputStream(new FileOutputStream(dest));

        int b = -1;

        while ((b = in.read()) != -1)
            out.write(b);

        out.flush();
        out.close();
        in.close();
    }
    
    /**
     * Attempts to locate a schema file by name by iterating through 
     * {@link #schemaLookupDirectories}.
     * 
     * @param path The path of the file.
     * 
     */
    protected File findSchemaFile( String path ) throws IOException {
        File file = null;
        try {
            file = new File(new URL(path).toURI());
        } catch( MalformedURLException | URISyntaxException e ) {
            file = new File(path);
        }

        if ( file.isAbsolute() ) {
    		return file;
    	}
    	
    	if ( schemaSourceDirectory != null ) {
    	    file = new File( schemaSourceDirectory, path );
            if ( file.exists() ) {
                return file;
            }
    	}
    	
    	if ( schemaLookupDirectories != null ) {
            for (File dir : schemaLookupDirectories) {
                file = new File(dir, path);
                if (file.exists()) {
                    return file;
                }
            }
    	}
    	
    	return null;
    }

    /**
     * Convenience method for generating the output location of generated files based on
     * {@link #getLocation()}
     */
    protected File outputLocation( String baseLocation ) {
        File location = null;

        if ( baseLocation == null ) {
            baseLocation = sourceLocation;
        }
        
        if (baseLocation != null) {
            location = new File(baseLocation);
        } else {
            location = new File(System.getProperty("user.dir"));
        }

        if (packageBase != null) {
            String path = packageBase.replace('.', File.separatorChar);
            location = new File(location, path);
        }

        return location;
    }

    /**
     * Executes a code generation template.
     * <p>
     * The class of the template is formed by prepending
     * <code>org.geotools.xml.codegen.</code> to <code>name</code>.
     * <p>
     *
     * @param templateName The non-qualified class name of the template.
     * @param input        The input to the template.
     *
     * @return The result of the code generator
     *
     * @throws ClassNotFoundException If the template class could not be
     * found.
     *
     * @throws RuntimeException If any exceptions ( ex, relection) occur.
     * while attempting to execute the template.
     *
     */
    protected String execute(String templateName, Object input)
        throws ClassNotFoundException, RuntimeException {
        Class<?> c = Class.forName("org.geotools.maven.xmlcodegen.templates."
                + templateName);

        try {
            Object template = c.getDeclaredConstructor().newInstance();

            Method generate = c.getMethod("generate",
                    new Class[] { Object.class });

            return (String) generate.invoke(template, new Object[] { input });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    String prefix(XSDSchema schema) {
       return Schemas.getTargetPrefix( schema );
    }

    public void setIncluded(Set included) {
    	this.included = included;
    }

    protected boolean included(XSDNamedComponent c) {
    	return included != null ? included.contains( c.getName() ) : true;
    }
}
