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
package org.geotools.data.efeature;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;

/**
 * {@link EFeature} hints class.
 * <p>
 * 
 * @author kengu - 4. juni 2011
 *
 */
public class EFeatureHints extends HashMap<Object, Object> {
    
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 
     * Set of ID {@link EAttribute}s
     */
    public static final int EFEATURE_ID_ATTRIBUTE_HINTS = 1;

    public static final int EFEATURE_SRID_ATTRIBUTE_HINTS = 2;
    
    public static final int EFEATURE_DEFAULT_ATTRIBUTE_HINTS = 3;
    
    public static final int EFEATURE_DEFAULT_SRID_HINT = 4;
    
    public static final int EFEATURE_DEFAULT_GEOMETRY_NAME_HINT = 5;
    

    /**
     * Default {@link EFeature} Hints
     */
    public EFeatureHints() {
        super();
        put(EFEATURE_ID_ATTRIBUTE_HINTS, new HashSet<EAttribute>(
                Arrays.asList(new EAttribute[]{EFeaturePackage.eINSTANCE.getEFeature_ID()})));
        put(EFEATURE_SRID_ATTRIBUTE_HINTS, new HashSet<EAttribute>(
                Arrays.asList(new EAttribute[]{EFeaturePackage.eINSTANCE.getEFeature_SRID()})));
        put(EFEATURE_DEFAULT_ATTRIBUTE_HINTS, new HashSet<EAttribute>(
                Arrays.asList(new EAttribute[]{EFeaturePackage.eINSTANCE.getEFeature_Default()})));
        put(EFEATURE_DEFAULT_SRID_HINT,EFeatureConstants.DEFAULT_SRID);
        put(EFEATURE_DEFAULT_GEOMETRY_NAME_HINT,EFeatureConstants.DEFAULT_GEOMETRY_NAME);
    }
    
    public EAttribute eGetAttribute(EClass eClass, int eHint) {
        Object eHints = get(eHint);
        if(eHints instanceof Set) {
            Set<?> eSet = (Set<?>)eHints;
            EList<EAttribute> eAll = eClass.getEAllAttributes();
            for(EAttribute it : eAll) {
                if(eSet.contains(it)) {
                    return it;
                }
            }
        } else if(eHints instanceof EAttribute) {
            EList<EAttribute> eAll = eClass.getEAllAttributes();
            if(eAll.contains(eHints)) {
                return (EAttribute)eHints;
            }
        }
        return null;
    }
    
    

}
