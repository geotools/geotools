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
package org.geotools.data.shapefile.dbf.index;

import static org.geotools.data.shapefile.files.ShpFileType.DBF;
import static org.geotools.data.shapefile.files.ShpFileType.SHX;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.index.Data;
import org.geotools.data.shapefile.index.DataDefinition;
import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.resources.LazySet;

import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BinarySpatialOperator;

/**
 * Manages the index files on behalf of a Dbase file.
 * 
 * @author Alvaro Huarte
 */
public class DbaseIndexManager {
    
    // Factory Registry of supported Dbase index file managers.
    private static FactoryRegistry registry;
    
    // Static constructor of DbaseIndexManager class.
    static {
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] { DbaseFileIndexFactory.class }));
        }
    }
    
    /**
     * Returns the DbaseFileIndexFactory collection registered.
     */
    private static LazySet<DbaseFileIndexFactory> getDbaseFileIndexFactories() {
        Hints hints = GeoTools.getDefaultHints();
        return new LazySet<DbaseFileIndexFactory>(
                registry.getServiceProviders(DbaseFileIndexFactory.class, null, hints));
    }
    
    protected ShapefileDataStore store;
    protected ShpFiles shpFiles;
    
    /**
     * Creates a new DbaseIndexManager object.
     */
    public DbaseIndexManager(ShpFiles shpFiles, ShapefileDataStore store) {
        this.shpFiles = shpFiles;
        this.store = store;
    }
    
    /**
     * Creates a new ExpressionFilterVisitor to resolve the specified Filter.
     */
    protected ExpressionFilterVisitor createExpressionFilterVisitor(Filter filter) {
        return new ExpressionFilterVisitor();
    }
    
    /**
     * Returns whether the specified filter can be processed using Dbase indexing.
     * @throws IOException 
     */
    private boolean supportsFilter(Filter filter, Map<String,Node> indexNodeMap) throws IOException {
        
        if (indexNodeMap == null) 
            throw new IOException("Dbase index file is wrong!");
        
        FilterAttributeExtractor attributeExtractor = new FilterAttributeExtractor();
        Object result = filter.accept(attributeExtractor, null);
        
        // Something wrong or there are no properties, not use the Dbase indexing.
        if (result == null || attributeExtractor.getPropertyNameSet().size() == 0)
            return false;
        
        SimpleFeatureType featureSchema = store.getSchema();
        String geometryName = featureSchema.getGeometryDescriptor().getLocalName();
        
        // Check if all properties in filter are indexed.
        for (PropertyName property : attributeExtractor.getPropertyNameSet()) {
            if (!indexNodeMap.containsKey(property.getPropertyName()) && !geometryName.equalsIgnoreCase(property.getPropertyName())) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Converts the specified collection of Dbase record numbers to a list of Data Objects.
     * @throws IOException
     */
    private List<Data> convertRecordNumbersToDataObjects(Collection<Integer> recnoCollection, boolean clearCollection) throws IOException {
        
        if (recnoCollection != null) {
            IndexFile shx = shpFiles.exists(SHX) && shpFiles.get(SHX) != null ? new IndexFile(shpFiles, store.isMemoryMapped()) : null;
            
            DataDefinition def = new DataDefinition("US-ASCII");
            def.addField(Integer.class);
            def.addField(Long.class);
            
            try {
                List<Data> records = new ArrayList<Data>(recnoCollection.size());
                
                for (int recno : recnoCollection) {
                    
                    Data data = new Data(def);
                    data.addValue(new Integer(recno + 1));
                    data.addValue(new Long(shx.getOffsetInBytes(recno)));
                    
                    records.add(data);
                }
                if (clearCollection) {
                    recnoCollection.clear();
                }
                
                // Sort offsets, it is faster to read from disk.
                if (records.size() > 1) {
                    Collections.sort(records, new Comparator<Data>() {
                        public int compare( Data data1, Data data2 ) {
                            return ((Long)data1.getValue(1)).compareTo((Long)data2.getValue(1));
                        }
                    });
                }
                return records;
                
            } finally {
                shx.close();
            }
        }
        return null;
    }
    
    /**
     * Queries a supported Dbase index for features available in the specified alphanumeric filter.
     * 
     * @param filter Alphanumeric Filter to use
     * @param maxFeatures Maximum number of features to fetch
     * @return a list of Data objects 
     * @throws IOException
     */
    protected List<Data> queryDbaseIndex(Filter filter, int maxFeatures) throws IOException {
        
        if (filter!=null && !Filter.INCLUDE.equals(filter) && !Filter.EXCLUDE.equals(filter)) {
            
            DbaseFileIndex dbaseIndex = null;
            
            // Exist a supported index manager for the current Dbase file?
            if (!(filter instanceof BinarySpatialOperator)) {
                URL url = new URL(shpFiles.get(DBF));
                
                for (DbaseFileIndexFactory factory : getDbaseFileIndexFactories()) {
                    if ((dbaseIndex = factory.getIndexManager(url, store.getCharset(), store.getTimeZone()))!=null) {
                        break;
                    }
                }
            }
            if (dbaseIndex == null) {
                return null;
            }
            
            ExpressionFilterVisitor filterVisitor = createExpressionFilterVisitor(filter);
            List<Integer> recnoList = null;
            
            // Gets the recno list that matches the specified filter.
            try {
                Map<String,Node> indexNodeMap = dbaseIndex.getIndexNodeMap();
                if (!supportsFilter(filter, indexNodeMap)) return null;
                filterVisitor.setIndexNodeMap(indexNodeMap);
                
                if (filterVisitor.fullySupports(filter)) {
                    recnoList = DbaseFileIndex.queryDbaseIndex(filter, filterVisitor, maxFeatures);
                }
            } finally {
                dbaseIndex.close();
            }
            return convertRecordNumbersToDataObjects(recnoList, true);
        }
        return null;
    }
}
