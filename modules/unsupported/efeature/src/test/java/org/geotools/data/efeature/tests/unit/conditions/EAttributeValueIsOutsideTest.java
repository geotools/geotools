/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.efeature.tests.unit.conditions;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.query.conditions.Condition;
import org.geotools.data.efeature.query.EAttributeValueIsOutside;
import org.geotools.data.efeature.query.EFeatureEncoderException;

/**
 * @author kengu - 14. juni 2011
 *
 *
 * @source $URL$
 */
public class EAttributeValueIsOutsideTest extends AbstractEAttributeValueTest {
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    /**
     * @param name
     */
    public EAttributeValueIsOutsideTest(String name) {
        super(name,OUTSIDE);
    }

    // ----------------------------------------------------- 
    //  AbstractEAttributeValueTest implementation
    // -----------------------------------------------------

    @Override
    protected Condition createCondition(int index, Object filter) throws EFeatureEncoderException {
        //
        // Get iterator
        //
        Iterator<?> it = ((Collection<?>)filter).iterator();
        //
        // Get lower and upper values
        //
        Object lower = it.next();
        Object upper = it.next();        
        //
        // Create condition
        //
        return new EAttributeValueIsOutside(eAttribute, lower, upper);
    }

}
