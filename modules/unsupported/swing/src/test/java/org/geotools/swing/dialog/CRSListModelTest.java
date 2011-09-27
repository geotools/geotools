/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.dialog;

import java.util.Set;

import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for CRSListModel.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class CRSListModelTest {
    private static final String AUTHORITY = "EPSG";
    private static CRSAuthorityFactory FACTORY;
    private static Set<String> CODES;

    private CRSListModel model;
    
    @BeforeClass
    public static void setupOnce() throws Exception {
        FACTORY = ReferencingFactoryFinder.getCRSAuthorityFactory(AUTHORITY, null);
        CODES = FACTORY.getAuthorityCodes(CoordinateReferenceSystem.class);
    }
    
    @Before
    public void setup() {
        model = new CRSListModel("EPSG");
    }
    
    @Test
    public void modelHasAllCodesExceptSpecialAdditions() {
        assertEquals(CODES.size(), model.getSize());
    }
    
    @Test
    public void filterOnDescriptionSubString() throws Exception {
        final String filter = "albers";
        
        model.setFilter(filter);
        int observedCount = model.getSize();
        int expectedCount = 0;
        for (String code : CODES) {
            String desc = FACTORY.getDescriptionText(AUTHORITY + ":" + code).toString();
            if (desc.toLowerCase().contains(filter)) {
                expectedCount++ ;
            }
        }
        
        assertEquals(expectedCount, observedCount);
    }
}
