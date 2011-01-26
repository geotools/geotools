/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2005-2006, GeoTools Project Managment Committee (PMC)
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
package org.geotools.data.edigeo;

import java.io.File;
import java.io.IOException;

import org.geotools.test.TestData;

public class EdigeoTestUtils {
	
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	private static File getDataPath() {
        try {
        	return TestData.file(EdigeoTestUtils.class, null);
        } catch (IOException e) {
            return null;
        }
    }
	
	/**
	 * @param fileName
	 * @return
	 */
	protected static String fileName(String fileName) {
        if (fileName.equals("")) {
            return getDataPath().getAbsolutePath();
        }

        File f = new File(getDataPath(), fileName);

        return f.getAbsolutePath();
    }
	
	
}
