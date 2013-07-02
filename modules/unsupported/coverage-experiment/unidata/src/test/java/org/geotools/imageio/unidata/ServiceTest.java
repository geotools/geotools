/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.unidata;

import java.io.File;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import junit.framework.Assert;

import org.geotools.imageio.unidata.reader.DummyUnidataImageReader;
import org.geotools.test.TestData;
import org.geotools.util.logging.Logging;
import org.junit.Test;

/**
 * Class for testing availability of undiata format factory
 * 
 * @author Simone Giannecchini, GeoSolutions
 *
 * @source $URL$
 */
public class ServiceTest extends Assert {


    private final static Logger LOGGER = Logging.getLogger(ServiceTest.class.toString());

    @Test
    public void isAvailable() throws Exception {
        final File file = TestData.file(this, "O3-NO2.nc");
        if(!file.exists()){
            
            LOGGER.severe("Unable to locate test data O3-NO2.nc. Test aborted!");
            return;
        }
        Iterator<ImageReader> readers = ImageIO.getImageReaders(file);
        assertTrue("No valid unidata readers found",readers.hasNext());
        
        boolean found=false;
        while(readers.hasNext()){
            ImageReader reader=readers.next();
            if(reader instanceof DummyUnidataImageReader){
                found=true;
            }
            
        }
        assertTrue("DummyUnidataReader not registered", found);
    }
}
