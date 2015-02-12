/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.Converters;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

public abstract class JDBCXMLTest extends JDBCTestSupport {
    
    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    
    @Override
    protected abstract JDBCXMLTestSetup createTestSetup();

    public void testMappings() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema( tname("xml") );
        
        assertEquals( DocumentFragment.class, ft.getDescriptor( aname("xml") ).getType().getBinding() );
    }
    
    public void testRead() throws Exception {
        JDBCXMLTestSetup setup = (JDBCXMLTestSetup)super.setup;
        
        FeatureCollection<SimpleFeatureType, SimpleFeature> fc = 
            dataStore.getFeatureSource(tname("xml")).getFeatures();
        
        FeatureIterator<SimpleFeature> fi = fc.features();
        try {
            assertTrue(fi.hasNext());
            SimpleFeature f = fi.next();
            assertEquals(f.getID(), "xml.1");
            assertTrue(((Node)f.getAttribute(aname("xml"))).isEqualNode(
                    Converters.convert(setup.readXML(JDBCXMLTestSetup.FRAGMENT), DocumentFragment.class)));
            
            assertTrue(fi.hasNext());
            f = fi.next();
            assertEquals(f.getID(), "xml.2");
            assertTrue(((Node)f.getAttribute(aname("xml"))).isEqualNode(
                    Converters.convert(setup.readXML(JDBCXMLTestSetup.DOCUMENT), DocumentFragment.class)));
            
            assertTrue(fi.hasNext());
            f = fi.next();
            assertEquals(f.getID(), "xml.3");
            assertTrue(((Node)f.getAttribute(aname("xml"))).isEqualNode(
                    Converters.convert(setup.readXML(JDBCXMLTestSetup.LONGDOC), DocumentFragment.class)));
            
        } finally {
            fi.close();
        }
    }
    
    public void testWrite() throws Exception {
        JDBCXMLTestSetup setup = (JDBCXMLTestSetup)super.setup;
        
        FeatureStore<SimpleFeatureType, SimpleFeature> fs =  (FeatureStore<SimpleFeatureType, SimpleFeature>) 
            dataStore.getFeatureSource(tname("xml"));
        
        SimpleFeatureType ft = dataStore.getSchema( tname("xml") );
  
        
        List<FeatureId> fids = new ArrayList<FeatureId>();
        fids.addAll(fs.addFeatures(DataUtilities.collection(
                SimpleFeatureBuilder.build(ft, 
                        new Object[] {setup.readXML(JDBCXMLTestSetup.FRAGMENT)}, null))));
        fids.addAll(fs.addFeatures(DataUtilities.collection(
                SimpleFeatureBuilder.build(ft, 
                        new Object[] {setup.readXML(JDBCXMLTestSetup.DOCUMENT)}, null))));
        fids.addAll(fs.addFeatures(DataUtilities.collection(
                SimpleFeatureBuilder.build(ft, 
                        new Object[] {setup.readXML(JDBCXMLTestSetup.LONGDOC)}, null))));
        
        Filter filter = ff.id(new HashSet<Identifier>(fids));
        FeatureIterator<SimpleFeature> fi = fs.getFeatures(filter).features();
        try {
            assertTrue(fi.hasNext());
            SimpleFeature f = fi.next();
            assertEquals(f.getID(), fids.get(0).getID());
            assertTrue(((Node)f.getAttribute(aname("xml"))).isEqualNode(
                    Converters.convert(setup.readXML(JDBCXMLTestSetup.FRAGMENT), DocumentFragment.class)));
            
            assertTrue(fi.hasNext());
            f = fi.next();
            assertEquals(f.getID(), fids.get(1).getID());
            assertTrue(((Node)f.getAttribute(aname("xml"))).isEqualNode(
                    Converters.convert(setup.readXML(JDBCXMLTestSetup.DOCUMENT), DocumentFragment.class)));
            
            assertTrue(fi.hasNext());
            f = fi.next();
            assertEquals(f.getID(), fids.get(2).getID());
            assertTrue(((Node)f.getAttribute(aname("xml"))).isEqualNode(
                    Converters.convert(setup.readXML(JDBCXMLTestSetup.LONGDOC), DocumentFragment.class)));
        } finally {
            fi.close();
        }
    }
}
