/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.i18n;


import java.io.File;

import org.geotools.resources.IndexedResourceCompiler;


/**
 * Resource compiler.
 * 
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini
 */
public final class Compiler {
    /**
     * The base directory for {@code "java"} {@code "resources"} sub-directories.
     * The directory structure must be consistent with Maven conventions.
     */
    private static final File SOURCE_DIRECTORY = new File("./src/main");

    /**
     * The resources to process.
     */
    private static final Class[] RESOURCES_TO_PROCESS = {
        Errors.class, Vocabulary.class
    };

    /**
     * Do not allows instantiation of this class.
     */
    private Compiler() {
    }

    /**
     * Run the resource compiler.
     */
    public static void main(final String[] args) {
        IndexedResourceCompiler.main(args, SOURCE_DIRECTORY, RESOURCES_TO_PROCESS);
        System.out.println(SOURCE_DIRECTORY.getAbsolutePath());
    }
}
