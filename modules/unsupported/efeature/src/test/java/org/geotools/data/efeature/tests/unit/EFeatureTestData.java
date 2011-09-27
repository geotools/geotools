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
package org.geotools.data.efeature.tests.unit;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.query.conditions.eobjects.EObjectCondition;
import org.geotools.data.efeature.DataBuilder;
import org.geotools.data.efeature.DataTypes;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.query.EAttributeValueIsEqual;
import org.geotools.data.efeature.query.EFeatureEncoderException;
import org.geotools.data.efeature.query.EGeometryValueEquals;
import org.geotools.data.efeature.tests.EFeatureCompatibleData;
import org.geotools.data.efeature.tests.EFeatureData;
import org.geotools.data.efeature.tests.EFeatureTestsFactory;
import org.geotools.data.efeature.tests.NonGeoEObject;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;

/**
 * {@link EFeature} test data class.
 * <p>
 * This class adds test data to given EMF 
 * {@link Resource resource} instance.
 *  
 * @author kengu - 20. mai 2011 
 *
 *
 * @source $URL$
 */
public class EFeatureTestData {
    
    private static final String WKT_COORDS = "%1$s %2$s";
    private static final String WKT_GROUP = "(%1$s)";
    private static final String WTK_POINT = "POINT "+WKT_GROUP;
    private static final String WTK_LINE_STRING = "LINESTRING "+WKT_GROUP;
    private static final String WTK_POLYGON = "POLYGON "+WKT_GROUP;
    
    private boolean isInit = false;
    
    private Resource eResource;
    
    private int nonGeoEObjectCount = 0;
    private int eFeatureDataCount = 0;
    private int eFeatureCompatibleDataCount = 0;
    
    public static void main(String[] args) {
        String[] s = generatePointWKTs(10, 0, 20);
        for(String it : s) {
            System.out.println(it);            
        }
        s = generateLineStringWKTs(10, 0, 20, 1, 5);
        for(String it : s) {
            System.out.println(it);            
        }
        s = generatePolygonWKTs(10, 0, 20, 1, 5, 1, 10);
        for(String it : s) {
            System.out.println(it);            
        }
    }        
    
    public EFeatureTestData(Resource eResource)
    {
        this.eResource = eResource;
    }

    public int getNonGeoEObjectCount() {
        return nonGeoEObjectCount;
    }
    
    public int getEFeatureDataCount() {
        return eFeatureDataCount;
    }
    
    public int getEFeatureCompatibleDataCount() {
        return eFeatureCompatibleDataCount;
    }
    
    
    /**
     * Initialize resource by adding given amount of data into it.
     * <p>
     * If already initialized, this does nothing.
     * <p>
     * @param ncount - number of {@link NonGeoEObject} instances
     * @param fcount - number of {@link EFeatureData} instances
     * @param ccount - number of {@link EFeatureCompatibleData} instances
     * 
     * @throws Exception If initialization fails for some reason
     */
    public void random(int ncount, int fcount, int ccount) throws Exception {
        //
        // Only initialize once 
        //
        if(!isInit) {            
            //
            // Add given amount of NonGeoEObject instances
            //
            addNonGeoEObjects(ncount);
            //
            // Calculate number object with geometry data
            //
            int gcount = fcount + ccount;
            //
            // Add objects with geometry data?
            //
            if(gcount>0) {
                //
                // Get numeric attribute data types
                //
                List<Class<Number>> aTypes = DataTypes.getSubTypes(Number.class);
                //
                // Get number of numeric attribute data types
                //
                int acount = aTypes.size();
                //
                // Calculate number of attribute values per numeric type 
                //
                int tcount = (int)Math.ceil((double)gcount/(double)acount);
                //
                // Prepare attribute generating data
                //
                int i = 0;
                int n = Math.min(tcount, gcount);
                int count = n;
                // 
                // Generate random numeric data
                //             
                Number[] attributes = new Number[0];
                while(i<acount && count<=gcount) {
                    Class<Number> type = aTypes.get(i);
                    Number nmin = DataTypes.getMinValue(type);
                    Number nmax = DataTypes.getMaxValue(type);
                    attributes = attributes(Number.class, type, attributes, n, nmin, nmax);
                    count += (n = Math.min(tcount, gcount-count));
                    i++;
                }
                //
                // Calculate geometry values per unique type 
                //
                tcount = (int)Math.ceil(gcount/3.0);    
                //
                // Prepare geometry data for EFeatureData instances
                //
                i = 0;
                count = (n = Math.min(tcount, gcount));
                // 
                // Generate random geometry data
                //             
                Geometry[] geometries = geometries(null, Geometry.class, generatePointWKTs(n, 0, 20));
                count += (n = Math.min(tcount, gcount-count));
                if(count<=gcount) {
                    geometries = geometries(geometries, Geometry.class, generateLineStringWKTs(n, 0, 20, 1, 5));
                    count += (n = Math.min(tcount, gcount-count));
                } if(count<=gcount) {
                    geometries = geometries(geometries, Geometry.class, generatePolygonWKTs(n, 0, 20, 1, 5, 1, 10));
                }
                //
                // Add objects 
                //           
                addFeatureData(0,fcount,attributes, geometries);
                addFeatureCompatibleData(fcount,gcount,attributes, geometries);
            }
            // 
            // Save counts
            //
            nonGeoEObjectCount = ncount;
            eFeatureDataCount = fcount;
            eFeatureCompatibleDataCount = ccount;
            //
            // Prevent future initializations
            //
            isInit = true;
        }
    }
        
    /**
     * Save changes to file.
     * @throws IOException
     */
    public void save() throws IOException {
        if(eResource.isLoaded()) {
            eResource.save(Collections.emptyMap());
        }
    }
    
    public <A, G extends Geometry> List<EFeatureData<A, G>> addFeatureData(
            Class<G> type, A[] attributes, String... wkts) throws Exception {
        G[] g = geometries(null, type, wkts);
        return addFeatureData(0, g.length, attributes, g);
    }
    
    public <A, G extends Geometry> List<EFeatureCompatibleData<A, G>> addFeatureCompatibleData(
            Class<G> type, A[] attributes, String... wkts) throws Exception {
        G[] g = geometries(null, type, wkts);
        return addFeatureCompatibleData(0, g.length, attributes, g);
    }
    
    
    /**
     * Print data statistics
     */
    public void print() {
        int nCount = 0;
        int gCount = 0;
        
        TreeIterator<EObject> it = eResource.getAllContents();
        while(it.hasNext()) {
            if(it.next() instanceof NonGeoEObject) {
                nCount++;
            } else {
                gCount++;
            }
        }
        System.out.println("NonGeoEObject count: " + nCount);
        System.out.println("EFeatureData count: " + gCount);
    }
    
    /**
     * Dispose this test.
     */
    public void dispose() {
        eResource = null;
    }
        
    /**
     * Add given number of {@link NonGeoEObject} instances.
     * @param count - number of instances to add
     */
    public List<NonGeoEObject> addNonGeoEObjects(int count)
    {
        List<NonGeoEObject> items = new ArrayList<NonGeoEObject>(count);
        if(count>0) {
            for(int i=0;i<count;i++) {
                NonGeoEObject it = EFeatureTestsFactory.eINSTANCE.createNonGeoEObject();
                items.add(it);            
            }
            eResource.getContents().addAll(items);
        }
        return items;
    }    
    
    /**
     * Add given number of valid {@link EFeatureData} instances.
     * @param offset TODO
     * @param attrClass - attribute data class
     * @param geoClass - geometry data class
     * @param eData - array of data
     * @param <A> - Attribute data type 
     * @param <G> - Geometry data type
     * @return
     */
    public <A, G extends Geometry> List<EFeatureData<A, G>> addFeatureData(int offset, int count, A[] a, G[] g)
    {
        count = Math.min(count, Math.min(a.length,g.length));        
        List<EFeatureData<A, G>> items = new ArrayList<EFeatureData<A,G>>(count);
        for(int i=offset;i<count;i++) {
            EFeatureData<A, G> it = EFeatureTestsFactory.eINSTANCE.<A,G>createEFeatureData();
            it.setAttribute(a[i]);
            it.setGeometry(g[i]);
            eResource.getContents().add(it);
        }
        return items;
    }
    
    /**
     * Add given number of valid {@link EFeatureCompatibleData} instances.
     * @param offset TODO
     * @param attrClass - attribute data class
     * @param geoClass - geometry data class
     * @param eData - array of data
     * @param <A> - Attribute data type 
     * @param <G> - Geometry data type
     * @return
     */
    public <A, G extends Geometry> List<EFeatureCompatibleData<A, G>> addFeatureCompatibleData(int offset, int count, A[] a, G[] g)
    {
        count = Math.min(count, Math.min(a.length,g.length));        
        List<EFeatureCompatibleData<A, G>> items = new ArrayList<EFeatureCompatibleData<A,G>>(count);
        for(int i=offset;i<count;i++) {
            EFeatureCompatibleData<A, G> it = EFeatureTestsFactory.eINSTANCE.<A,G>createEFeatureCompatibleData();
            it.setAttribute(a[i]);
            it.setGeometry(g[i]);
            eResource.getContents().add(it);
        }
        return items;
    }
    
    
    public static String[] generateCoordWKTs(int count, int min, int max)
    {
        String[] coords = new String[count];
        for(int i=0; i<count; i++) {
            int x = (int)(Math.random()*max + min);
            int y = (int)(Math.random()*max + min);
            coords[i] = String.format(WKT_COORDS,x,y);
        }
        return coords;        
    }
    
    public static String toCSV(String[] items) {
        StringBuffer b = new StringBuffer(items[0]); 
        int count = items.length;
        for(int i=1; i<count; i++) {
            b.append(", "+items[i]);
        }
        return b.toString();        
    }
    
    public static String[] generatePointWKTs(int count, int min, int max) {
        String[] points = new String[count];
        String[] coords = generateCoordWKTs(count, min, max);
        for(int i=0; i<count; i++) {
            points[i] = String.format(WTK_POINT, coords[i]);
        }
        return points;
    }
    
    public static String[] generateLineStringWKTs(int count, int cmin, int cmax, int lmin, int lmax) {
        lmin = Math.max(2, lmin);
        lmax = Math.max(2, lmax);
        String[] lines = new String[count];
        for(int i=0; i<count; i++) {
            int l = (int)(Math.random()*lmax + lmin);
            String[] coords = generateCoordWKTs(l, cmin, cmax);            
            lines[i] = String.format(WTK_LINE_STRING, toCSV(coords));
        }
        return lines;
    }
    
    public static String[] generatePolygonWKTs(int count, int cmin, int cmax, int lmin, int lmax, int pmin, int pmax) {
        lmin = Math.max(4, lmin);
        lmax = Math.max(4, lmax);
        pmin = Math.max(2, pmin);
        pmax = Math.max(2, pmax);
        String[] polygons = new String[count];
        for(int i=0; i<count; i++) {
            int p = (int)(Math.random()*pmax + pmin);
            String[] lines = new String[p];
            for(int j=0; j<p; j++) {
                int l = (int)(Math.random()*lmax + lmin);
                String[] coords = generateCoordWKTs(l, cmin, cmax);
                coords[coords.length-1] = coords[0];
                lines[j] = String.format(WKT_GROUP, toCSV(coords));
            }
            polygons[i] = String.format(WTK_POLYGON, toCSV(lines));
        }
        return polygons;
    }
    
    @SuppressWarnings("unchecked")
    public static <A ,N extends Number> A[] attributes(Class<A> attClass, Class<N> numClass, 
            A[] attributes, int count, N min, N max) throws ParseException {
        //
        // Initialize list
        //
        List<Object> items = null;
        if(attributes==null) {
            items = new ArrayList<Object>(count);
        } else {
            items = new ArrayList<Object>(Arrays.asList(attributes));
        }
        //
        // Create random numbers
        //
        for(int i=0; i<count; i++) {
            N v = scale(numClass,Math.random(),min,max);
            items.add(v);
        }
        return items.toArray((A[])Array.newInstance(attClass, items.size()));
    }
    
    private static <N extends Number> N scale(Class<N> type, double value, N min, N max) {
        if(type.isAssignableFrom(Integer.class))
            return type.cast(Integer.valueOf((int)(value*max.intValue() + min.intValue())));        
        if(type.isAssignableFrom(Double.class))
            return type.cast((value*max.doubleValue() + min.doubleValue()));        
        if(type.isAssignableFrom(Long.class))
            return type.cast(Long.valueOf((long)(value*max.longValue() + min.longValue())));        
        if(type.isAssignableFrom(Short.class))
            return type.cast(Short.valueOf((short)(value*max.shortValue() + min.shortValue())));        
        if(type.isAssignableFrom(Byte.class))
            return type.cast(Byte.valueOf((byte)(value*max.byteValue() + min.byteValue())));        
        if(type.isAssignableFrom(Float.class))
            return type.cast(Float.valueOf((float)(value*max.floatValue() + min.floatValue())));        
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public static <G extends Geometry> G[] geometries(G[] geometries, 
            Class<G> geoClass, String... wkts) throws ParseException {
        //
        // Initialize list
        //
        List<G> items = null;
        if(geometries==null) {
            items = new ArrayList<G>(wkts.length);
        } else {
            items = new ArrayList<G>(Arrays.asList(geometries));
        }
        for(String wkt : wkts) {
            Geometry geom = DataBuilder.toGeometry(wkt);
            if(geoClass.isInstance(geom)) {
                items.add(geoClass.cast(geom));
            }
        }
        return items.toArray((G[]) Array.newInstance(geoClass, items.size()));
    }       
    
    public <A, G extends Geometry> Object[] generateData(Object[] data, A[] attributes, G[] geometries) {
        //
        // Prepare to merge
        //
        int cmin = 0;
        int cmax = 0;
        Object[] amin = null;
        Object[] amax = null;
        if(attributes.length<geometries.length) {
            cmin = attributes.length;
            cmax = geometries.length;
            amin = attributes;
            amax = geometries;
        } else {
            cmin = geometries.length;
            cmax = attributes.length;
            amin = geometries;
            amax = attributes;            
        }
        //
        // Initialize list
        //
        List<Object> items = null;
        if(data==null) {
            items = new ArrayList<Object>(cmax);
        } else {
            items = new ArrayList<Object>(Arrays.asList(data));
        }
        //
        // Order on array length
        //
        //
        // Merge into one continuous array of objects
        //
        for(int i=0; i<cmin; i++) {
            items.add(amin[i]);
            items.add(amax[i]);
        }
        //
        // Add residue
        // 
        for(int i=cmin;i<cmax;i++) {
            items.add(amax[i]);
        }
        //
        // Finished
        //
        return items.toArray(new Object[items.size()]);
    }
    
    // ----------------------------------------------------- 
    //  Static utility methods
    // -----------------------------------------------------

    public static EObjectCondition newIsEqual(EAttribute eAttribute, String value, 
            EFeatureInfo... eFeatureInfo) throws EFeatureEncoderException
    {
        if(eFeatureInfo.length>0) {
            eAttribute = eFeatureInfo[0].eMappedTo(eAttribute);
        }
        return new EAttributeValueIsEqual(eAttribute,value);
    }
    
    public static EObjectCondition newIsEqual(EAttribute eAttribute, Integer value, 
            EFeatureInfo...eFeatureInfo) throws EFeatureEncoderException
    {
        if(eFeatureInfo.length>0) {
            eAttribute = eFeatureInfo[0].eMappedTo(eAttribute);
        }
        return new EAttributeValueIsEqual(eAttribute,value);
    }
    
    public static EObjectCondition newIsEqual(EAttribute eAttribute, Geometry value, 
            EFeatureInfo...eFeatureInfo) throws EFeatureEncoderException
    {
        if(eFeatureInfo.length>0) {
            eAttribute = eFeatureInfo[0].eMappedTo(eAttribute);
        }
        return new EGeometryValueEquals(eAttribute,value,false);
    }
    
    
}
