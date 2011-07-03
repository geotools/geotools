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
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.geotools.factory.Hints;
import org.opengis.feature.simple.SimpleFeature;

/**
 * {@link EFeature} hints class.
 * <p>
 * 
 * @author kengu - 4. juni 2011
 *
 */
public class EFeatureHints extends Hints {
    
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 
     * Key to set of {@link EAttribute}s which maps to {@link EFeature#getID()}
     */
    public static final Key EFEATURE_ID_ATTRIBUTES = new Key(Set.class);

    /** 
     * Key to set of {@link EAttribute}s which maps to {@link EFeature#getSRID()}  
     */
    public static final Key EFEATURE_SRID_ATTRIBUTES = new Key(Set.class);
    
    /** 
     * Key to set of {@link EAttribute}s which maps to {@link EFeature#getDefault()}  
     */
    public static final Key EFEATURE_DEFAULT_ATTRIBUTES = new Key(Set.class);
    
    /** 
     * Key to set of default  {@link EFeature#getDefault()} value
     */
    public static final Key EFEATURE_DEFAULT_GEOMETRY_NAMES = new Key(Set.class);

    /** 
     * Key to default {@link EFeature#getSRID()} value 
     */
    public static final Key EFEATURE_DEFAULT_SRID_HINT = new Key(String.class);
    
    /**
     * Indicates that {@link ESimpleFeature}s returned by {@link EFeature#getData()} and
     * {@link EFeatureReader#next()} should be considered detached from the 
     * {@link EFeatureDataStore#eResource() backing store}. If <code>true</code> 
     * the features can be updated without altering the backing store.
     * <p>
     * Default value is <code>false</code>.
     * </p> 
     * @see {@link Hints#FEATURE_DETACHED} (same key)
     */
    public static final Key EFEATURE_VALUES_DETACHED = Hints.FEATURE_DETACHED;

    /**
     * Indicates that {@link ESimpleFeature}s returned by {@link EFeature#getData()} and
     * {@link EFeatureReader#next()} should be singletons. If <code>true</code> 
     * repeated calls to {@link EFeature#getData()} and {@link EFeatureReader#next()} will
     * return the same feature instance.
     * <p>
     * Use this hint to optimize operations where only 
     * {@link SimpleFeature#getAttributes() values} are read or written. 
     * <p>
     * Default value is <code>true</code>.
     * </p> 
     */    
    public static final Key EFEATURE_SINGLETON_FEATURES = new Key(Boolean.class);

    /**
     * Default {@link EFeature} Hints
     */
    public EFeatureHints() {
        super();
        //
        // Initialize EFeature hints
        //
        put(EFEATURE_ID_ATTRIBUTES, new HashSet<EAttribute>(
                Arrays.asList(new EAttribute[]{EFeaturePackage.eINSTANCE.getEFeature_ID()})));
        put(EFEATURE_SRID_ATTRIBUTES, new HashSet<EAttribute>(
                Arrays.asList(new EAttribute[]{EFeaturePackage.eINSTANCE.getEFeature_SRID()})));
        put(EFEATURE_DEFAULT_ATTRIBUTES, new HashSet<EAttribute>(
                Arrays.asList(new EAttribute[]{EFeaturePackage.eINSTANCE.getEFeature_Default()})));
        put(EFEATURE_DEFAULT_SRID_HINT,EFeatureConstants.DEFAULT_SRID);
        put(EFEATURE_DEFAULT_GEOMETRY_NAMES,new HashSet<String>(
                Arrays.asList(EFeatureConstants.DEFAULT_GEOMETRY_NAME)));
        put(EFEATURE_VALUES_DETACHED,new Boolean(false));
        put(EFEATURE_SINGLETON_FEATURES,new Boolean(true));
    }
    
    public EAttribute eGetAttribute(EClass eClass, Key eHint) {
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
    
    public boolean eValuesDetached() {
        Object v = get(EFEATURE_VALUES_DETACHED);
        return v instanceof Boolean && (Boolean)v;
    }
    
    public boolean eSingletonFeatures() {
        Object v = get(EFEATURE_SINGLETON_FEATURES);        
        return v instanceof Boolean && (Boolean)v;
    }
    
    public Object replace(Hints hints, Key key) {
        Object value = hints.get(key); 
        if(value!=null) {
            return put(key,value);
        }
        return null;
    }
    
    public void restore(Key key, Object value) {
        if(value!=null) {
            put(key,value);
        }
    }
    
    
    
}
