/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package net.opengis.wfs20.impl;


import static org.junit.Assert.assertFalse;

import org.junit.Test;

import net.opengis.wfs20.TransactionType;
import net.opengis.wfs20.Wfs20Factory;

/**
 * Contains tests for {@link BaseRequestTypeImpl} 
 * @author awaterme
 */
public class BaseRequestTypeImplTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testOwnExtendedProperties() {
        TransactionType lTransaction1 = Wfs20Factory.eINSTANCE.createTransactionType();
        lTransaction1.getExtendedProperties().put("foo", "bar");
        TransactionType lTransaction2 = Wfs20Factory.eINSTANCE.createTransactionType();
        assertFalse(lTransaction2.getExtendedProperties().containsKey("foo"));
    }

}
