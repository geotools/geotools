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
package org.geotools.data.efeature.query;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.Condition;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;

import org.geotools.data.efeature.EFeatureInfo;

/**
 * @author kengu - 11. juni 2011
 *
 */
public class EFeatureValueCondition extends EObjectAttributeValueCondition {
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    /**
     * Default constructor
     * @param attribute
     * @param condition
     */
    public EFeatureValueCondition(EAttribute eAttribute, Condition condition) {
        //
        // Forward to super class
        //
        super(eAttribute, condition);
    }

    /**
     * {@link EAttribute} mapping constructor.
     * @param attribute
     * @param condition
     */
    public EFeatureValueCondition(EAttribute eAttribute, Condition condition, EFeatureInfo eInfo) {
        //
        // Forward to super class
        //
        super(eInfo.eMappedTo(eAttribute), condition);
    }
    
}
