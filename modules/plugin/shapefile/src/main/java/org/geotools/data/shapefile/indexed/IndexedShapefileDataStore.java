/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.indexed;

import static org.geotools.data.shapefile.ShpFileType.DBF;
import static org.geotools.data.shapefile.ShpFileType.FIX;
import static org.geotools.data.shapefile.ShpFileType.QIX;
import static org.geotools.data.shapefile.ShpFileType.SHP;
import static org.geotools.data.shapefile.ShpFileType.SHX;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.EmptyFeatureWriter;
import org.geotools.data.FIDReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.InProcessLockingManager;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.TransactionStateDiff;
import org.geotools.data.shapefile.FileWriter;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.ShpFileType;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.IndexedDbaseFileReader;
import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileReader.Record;
import org.geotools.factory.Hints;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.IdCollectorFilterVisitor;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.index.CachedQuadTree;
import org.geotools.index.CloseableIterator;
import org.geotools.index.Data;
import org.geotools.index.DataDefinition;
import org.geotools.index.LockTimeoutException;
import org.geotools.index.TreeException;
import org.geotools.index.quadtree.QuadTree;
import org.geotools.index.quadtree.StoreException;
import org.geotools.index.quadtree.fs.FileSystemIndexStore;
import org.geotools.renderer.ScreenMap;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.identity.Identifier;

import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * A DataStore implementation which allows reading and writing from Shapefiles.
 * 
 * @author Ian Schneider
 * @author Tommaso Nolli
 * @author jesse eichar
 * 
 *
 * @source $URL$
 */
public class IndexedShapefileDataStore extends ShapefileDataStore implements
        FileWriter {
    private final static class IdentifierComparator implements Comparator<Identifier>
    {
        public int compare(Identifier o1, Identifier o2)
        {
            return o1.toString().compareTo(o2.toString());
        }
    }

    IndexType treeType;

    final boolean useIndex;
    
    final boolean createIndex;
    
    CachedQuadTree cachedTree;

	int maxQixCacheSize = DEFAULT_MAX_QIX_CACHE_SIZE;
	
	static final int DEFAULT_MAX_QIX_CACHE_SIZE;
	
	static {
		int max = -1;
		try {
			String smax = System.getProperty("org.geotools.shapefile.maxQixCacheSize");
			if(smax != null) {
				max = Integer.parseInt(smax);
			}
		} catch(Throwable t) {
			LOGGER.log(Level.SEVERE, "Could not set the max qix cache size", t);
		}
		DEFAULT_MAX_QIX_CACHE_SIZE = max;
	}

    /**
     * Creates a new instance of ShapefileDataStore.
     * 
     * @param url
     *                The URL of the shp file to use for this DataSource.
     */
    public IndexedShapefileDataStore(URL url)
            throws java.net.MalformedURLException {
        this(url, null, false, true, IndexType.QIX);
    }

    /**
     * Creates a new instance of ShapefileDataStore.
     * 
     * @param url
     *                The URL of the shp file to use for this DataSource.
     * @param namespace
     *                DOCUMENT ME!
     */
    public IndexedShapefileDataStore(URL url, URI namespace)
            throws java.net.MalformedURLException {
        this(url, namespace, false, true, IndexType.QIX);
    }

    /**
     * Creates a new instance of ShapefileDataStore.
     * 
     * @param url
     *                The URL of the shp file to use for this DataSource.
     * @param useMemoryMappedBuffer
     *                enable/disable memory mapping of files
     * @param createIndex
     *                enable/disable automatic index creation if needed
     */
    public IndexedShapefileDataStore(URL url, boolean useMemoryMappedBuffer,
            boolean createIndex) throws java.net.MalformedURLException {
        this(url, null, useMemoryMappedBuffer, createIndex, IndexType.QIX);
    }

    /**
     * Creates a new instance of ShapefileDataStore.
     * 
     * @param url
     *                The URL of the shp file to use for this DataSource.
     * @param namespace
     *                DOCUMENT ME!
     * @param useMemoryMappedBuffer
     *                enable/disable memory mapping of files
     * @param createIndex
     *                enable/disable automatic index creation if needed
     * @param treeType
     *                The type of index to use
     * 
     */
    public IndexedShapefileDataStore(URL url, URI namespace,
            boolean useMemoryMappedBuffer, boolean createIndex,
            IndexType treeType) throws MalformedURLException {
        this(url, namespace, useMemoryMappedBuffer, createIndex, treeType,
                DEFAULT_STRING_CHARSET);
    }

    /**
     * Creates a new instance of ShapefileDataStore.
     * 
     * @param url
     *                The URL of the shp file to use for this DataSource.
     * @param namespace
     *                DOCUMENT ME!
     * @param useMemoryMappedBuffer
     *                enable/disable memory mapping of files
     * @param createIndex
     *                enable/disable automatic index creation if needed
     * @param treeType
     *                The type of index used
     * @param dbfCharset
     *                {@link Charset} used to decode strings from the DBF
     * 
     * @throws NullPointerException
     *                 DOCUMENT ME!
     * @throws .
     */
    public IndexedShapefileDataStore(URL url, URI namespace,
            boolean useMemoryMappedBuffer, boolean createIndex,
            IndexType treeType, Charset dbfCharset)
            throws java.net.MalformedURLException {
    	super(url, namespace, useMemoryMappedBuffer, dbfCharset);

        this.treeType = treeType;
        this.useIndex = treeType != IndexType.NONE;
        this.createIndex = createIndex;
    }
    
    public IndexedShapefileDataStore(URL url, URI namespace,
            boolean useMemoryMappedBuffer, boolean cacheMemoryMaps, boolean createIndex,
            IndexType treeType, Charset dbfCharset)
            throws java.net.MalformedURLException {
    	super(url, namespace, useMemoryMappedBuffer, cacheMemoryMaps, dbfCharset);

        this.treeType = treeType;
        this.useIndex = treeType != IndexType.NONE;
        this.createIndex = createIndex;
    }
    
    /**
     * Creates the spatial index is appropriate. 
     * @param force Forces the index re-creation even if the spatial index seems to be up to date
     * @return true if the spatial index has been created/updated
     */
    public boolean createSpatialIndex(boolean force) {
        // create index as needed
        try {
            if (shpFiles.isLocal() && createIndex
                    && (needsGeneration(treeType.shpFileType) || force)) {
                createSpatialIndex();
                return true;
            }
        } catch (IOException e) {
            this.treeType = IndexType.NONE;
            ShapefileDataStoreFactory.LOGGER.log(Level.SEVERE, e
                    .getLocalizedMessage());
        }
        return false;
    }

    /**
     * Forces the spatial index to be created
     */
    public void createSpatialIndex() throws IOException {
        buildQuadTree();
    }

    protected Filter getUnsupportedFilter(String typeName, Filter filter) {

        if (filter instanceof Id && isLocal() && shpFiles.exists(FIX))
            return Filter.INCLUDE;

        return filter;
    }

    /**
     * Creates a new instance of ShapefileDataStore.
     * 
     * @param url
     *                The URL of the shp file to use for this DataSource.
     * @param namespace
     *                DOCUMENT ME!
     * @param useMemoryMappedBuffer
     *                enable/disable memory mapping of files
     * @param cacheMemoryMaps
     *                caches and reuses the read only memory mapped buffers                
     * @param createIndex
     *                enable/disable automatic index creation if needed
     * @param treeType
     *                The type of index used
     * @param dbfCharset
     *                {@link Charset} used to decode strings from the DBF
     * 
     * @throws NullPointerException
     *                 DOCUMENT ME!
     * @throws .
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName,
            Transaction transaction) throws IOException {
        if (transaction == null) {
            throw new NullPointerException(
                    "getFeatureWriter requires Transaction: "
                            + "did you mean to use Transaction.AUTO_COMMIT?");
        }

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        if (transaction == Transaction.AUTO_COMMIT) {
            return super.getFeatureWriterAppend(typeName, transaction);
        } else {
            writer = state(transaction).writer(typeName, Filter.EXCLUDE);
        }

        if (getLockingManager() != null) {
            // subclass has not provided locking so we will
            // fake it with InProcess locks
            writer = ((InProcessLockingManager) getLockingManager())
                    .checkedWriter(writer, transaction);
        }

        while (writer.hasNext())
            writer.next();
        return writer;
    }

    /**
     * This method is identical to the super class WHY?
     */
    protected TransactionStateDiff state(Transaction transaction) {
        synchronized (transaction) {
            TransactionStateDiff state = (TransactionStateDiff) transaction
                    .getState(this);

            if (state == null) {
                state = new TransactionStateDiff(this);
                transaction.putState(this, state);
            }

            return state;
        }
    }

    /**
     * Use the spatial index if available and adds a small optimization: if no
     * attributes are going to be read, don't uselessly open and read the dbf
     * file.
     * 
     * @see org.geotools.data.AbstractDataStore#getFeatureReader(java.lang.String,
     *      org.geotools.data.Query)
     */
    protected  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String typeName, Query query)
            throws IOException {
        if (query.getFilter() == Filter.EXCLUDE)
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(getSchema());

        String[] propertyNames = query.getPropertyNames() == null ? new String[0]
                : query.getPropertyNames();
        String defaultGeomName = schema.getGeometryDescriptor().getLocalName();


        // add the attributes we need to read to keep the filtering going
        if(propertyNames.length  > 0) {
            FilterAttributeExtractor fae = new FilterAttributeExtractor(schema);
            query.getFilter().accept(fae, null);
    
            Set<String> attributes = new LinkedHashSet<String>(Arrays.asList(propertyNames));
            attributes.addAll(fae.getAttributeNameSet());
    
            propertyNames = (String[]) attributes.toArray(new String[attributes
                    .size()]);
        }

        // check what we actually have to read
        SimpleFeatureType newSchema = schema;
        boolean readDbf = true;
        boolean readGeometry = true;
        try {
            if (((query.getPropertyNames() != Query.NO_NAMES)
                    && (propertyNames.length == 1) && propertyNames[0]
                    .equals(defaultGeomName))) {
                readDbf = false;
                newSchema = createSubType( propertyNames);
            } else if ((query.getPropertyNames() == Query.NO_NAMES)
                    && (propertyNames.length == 0)) {
                readDbf = false;
                readGeometry = false;
                newSchema = createSubType( propertyNames);
            } else if( propertyNames.length > 0 && !propertyNames[0].equals(defaultGeomName) ){
                readGeometry = false;
                newSchema = createSubType(propertyNames);
            } else if(propertyNames.length > 0) {
                newSchema = createSubType(propertyNames);
            }

            return createFeatureReader(typeName, getAttributesReader(readDbf,
                    readGeometry, query, newSchema), newSchema);
        } catch (SchemaException se) {
            throw new DataSourceException("Error creating schema", se);
        }
    }
    
    /**
     * Much like {@link DataUtilities#createSubType(SimpleFeatureType, String[])}, but makes
     * sure to preserve the original attribute order
     * @param properties
     * @return
     * @throws SchemaException
     */
    public SimpleFeatureType createSubType(String[] properties) throws SchemaException {
        if (properties == null || properties.length == 0) {
            return schema;
        }

        boolean same = schema.getAttributeCount() == properties.length;

        for (int i = 0; (i < schema.getAttributeCount()) && same; i++) {
            same = schema.getDescriptor(i).getLocalName().equals(properties[i]);
        }

        if (same) {
            return schema;
        }

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName( schema.getName() );
        
        Set<String> propIndex = new HashSet<String>(Arrays.asList(properties));
        for(AttributeDescriptor ad : schema.getAttributeDescriptors()) {
            if(propIndex.contains(ad.getLocalName()))
                tb.add(ad);
        }
        return tb.buildFeatureType();
    }

    protected  FeatureReader<SimpleFeatureType, SimpleFeature> createFeatureReader(String typeName,
            IndexedShapefileAttributeReader r, SimpleFeatureType readerSchema)
            throws SchemaException, IOException {
        
        if(r == null) {
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(readerSchema);
        }

        FIDReader fidReader;
        if (!indexUseable(FIX)) {
            fidReader = new ShapeFIDReader(getCurrentTypeName(), r);
        } else {
            fidReader = new IndexedFidReader(shpFiles, r);
        }
        return new org.geotools.data.FIDFeatureReader(r, fidReader,
                readerSchema);
    }

    /**
     * Forces the FID index to be regenerated
     * 
     * @throws IOException
     */
    public void generateFidIndex() throws IOException {
        FidIndexer.generate(shpFiles);
    }
    
    /**
     * If the fid index can be used and it is missing this method will try to create it
     * @return
     */
    boolean existsOrCreateFidIndex() {
        if(indexUseable(ShpFileType.FIX)) {
            return true;
        } else {
            if(isLocal()) {
                try {
                    FidIndexer.generate(shpFiles);
                    return true;
                } catch(IOException e) {
                    LOGGER.log(Level.WARNING, "Failed to create fid index");
                    return false;
                }
            } else {
                return false;
            }
            
        }
    }

    /**
     * Returns the attribute reader, allowing for a pure shape reader, or a
     * combined dbf/shp reader. Will return null if reading the indexes confirms there is nothing
     * to read
     * 
     * @param readDbf -
     *                if true, the dbf fill will be opened and read
     * @param readGeometry
     *                DOCUMENT ME!
     * @param filter -
     *                a Filter to use
     * 
     * 
     * @throws IOException
     */
    protected IndexedShapefileAttributeReader getAttributesReader(
            boolean readDbf, boolean readGeometry, Query query, SimpleFeatureType targetSchema)
            throws IOException {
        Envelope bbox = new ReferencedEnvelope(); // will be bbox.isNull() to
        // start

        Filter filter = query != null ? query.getFilter() : null;
        CloseableIterator<Data> goodRecs = null;
        if (filter instanceof Id && shpFiles.isLocal() && existsOrCreateFidIndex()) {
            Id fidFilter = (Id) filter;

            TreeSet idsSet = new TreeSet(new IdentifierComparator());
            idsSet.addAll(fidFilter.getIdentifiers());
            List<Data> records = queryFidIndex(idsSet);
            if(records != null) {
            	goodRecs = new CloseableIteratorWrapper<Data>(records.iterator());
            }
        } else {
            if (filter != null) {
                // Add additional bounds from the filter
                // will be null for Filter.EXCLUDES
                bbox = (Envelope) filter.accept(
                        ExtractBoundsFilterVisitor.BOUNDS_VISITOR, bbox);
                if (bbox == null) {
                    bbox = new ReferencedEnvelope();
                    // we hit Filter.EXCLUDES consider returning an empty
                    // reader?
                    // (however should simplify the filter to detect ff.not(
                    // fitler.EXCLUDE )
                }
            }

            if (!bbox.isNull() && this.useIndex) {
                try {
                    goodRecs = this.queryQuadTree(bbox);
                } catch (TreeException e) {
                    throw new IOException("Error querying index: "
                            + e.getMessage());
                }
            }
        }
        List<AttributeDescriptor> atts = targetSchema.getAttributeDescriptors();

        IndexedDbaseFileReader dbfR = null;
        
        // do we have anything to read at all? If not don't bother opening all the files
        if(goodRecs != null && !goodRecs.hasNext()) {
            // System.out.println("Empty results for " + targetSchema.getName().getLocalPart() + ", skipping read");
            goodRecs.close();
            return null;
        }

        if (!readDbf) {
            LOGGER.fine("The DBF file won't be opened since no attributes "
                    + "will be read from it");
            atts = new ArrayList<AttributeDescriptor>(1);
            atts.add(schema.getGeometryDescriptor());

            if (!readGeometry) {
                atts = new ArrayList<AttributeDescriptor>(1);
            }
        } else {
            dbfR = (IndexedDbaseFileReader) openDbfReader();
        }

        Hints hints = query != null ? query.getHints() : null;
        final ShapefileReader shapeReader = openShapeReader(getGeometryFactory(hints), goodRecs != null);
        IndexedShapefileAttributeReader reader =  new IndexedShapefileAttributeReader(atts, 
                shapeReader, dbfR, goodRecs);
        reader.setTargetBBox(bbox);
        if(hints != null) {
            Number simplificationDistance = (Number) hints.get(Hints.GEOMETRY_DISTANCE);    
            if(simplificationDistance != null) {
                reader.setSimplificationDistance(simplificationDistance.doubleValue());
            }
            reader.setScreenMap((ScreenMap) hints.get(Hints.SCREENMAP));
            
            if(Boolean.TRUE.equals(hints.get(Hints.FEATURE_2D))) {
                shapeReader.setFlatGeometry(true);
            }
        }
        
        return reader;
    }
    
    /**
     * Convenience method for opening a ShapefileReader.
     * 
     * @return A new ShapefileReader.
     * 
     * @throws IOException
     *                 If an error occurs during creation.
     */
    protected ShapefileReader openShapeReader(GeometryFactory gf, boolean onlyRandomAccess) throws IOException {
        try {
            return new ShapefileReader(shpFiles, true, useMemoryMappedBuffer, gf, onlyRandomAccess);
        } catch (ShapefileException se) {
            throw new DataSourceException("Error creating ShapefileReader", se);
        }
    }

    /**
     * Uses the Fid index to quickly lookup the shp offset and the record number
     * for the list of fids
     * 
     * @param fids
     *                the fids of the features to find.  If the set is sorted by alphabet the performance is likely to be better.
     * @return a list of Data objects
     * @throws IOException
     * @throws TreeException
     */
    private List<Data> queryFidIndex(Set<Identifier> idsSet) throws IOException {

        if (!indexUseable(FIX)) {
            return null;
        }

        IndexedFidReader reader = new IndexedFidReader(shpFiles);

        List<Data> records = new ArrayList(idsSet.size());
        try {
            IndexFile shx = openIndexFile();
            try {

                DataDefinition def = new DataDefinition("US-ASCII");
                def.addField(Integer.class);
                def.addField(Long.class);
                for (Identifier identifier : idsSet) {
                    String fid = identifier.toString();
                    long recno = reader.findFid(fid);
                    if (recno == -1){
                        if(LOGGER.isLoggable(Level.FINEST)){
                            LOGGER.finest("fid " + fid+ " not found in index, continuing with next queried fid...");
                        }
                        continue;
                    }
                    try {
                        Data data = new Data(def);
                        data.addValue(new Integer((int) recno + 1));
                        data.addValue(new Long(shx
                                .getOffsetInBytes((int) recno)));
                        if(LOGGER.isLoggable(Level.FINEST)){
                            LOGGER.finest("fid " + fid+ " found for record #"
                                    + data.getValue(0) + " at index file offset "
                                    + data.getValue(1));
                        }
                        records.add(data);
                    } catch (Exception e) {
                        IOException exception = new IOException();
                        exception.initCause(e);
                        throw exception;
                    }
                }
            } finally {
                shx.close();
            }
        } finally {
            reader.close();
        }

        return records;
    }

    /**
     * Returns true if the index for the given type exists and is useable.
     * 
     * @param indexType
     *                the type of index to check
     * 
     * @return true if the index for the given type exists and is useable.
     */
    public boolean indexUseable(ShpFileType indexType) {
        if (isLocal()) {
            if (needsGeneration(indexType) || !shpFiles.exists(indexType)) {
                return false;
            }
        } else {

            ReadableByteChannel read = null;
            try {
                read = shpFiles.getReadChannel(indexType, this);
            } catch (IOException e) {
                return false;
            } finally {
                if (read != null) {
                    try {
                        read.close();
                    } catch (IOException e) {
                        ShapefileDataStoreFactory.LOGGER.log(Level.WARNING,
                                "could not close stream", e);
                    }
                }
            }
        }

        return true;
    }

    boolean needsGeneration(ShpFileType indexType) {
        // happens if the IndexType.NONE.shpFileType is used)
        if(indexType == null)
            return false;
        
        if (!isLocal())
            throw new IllegalStateException(
                    "This method only applies if the files are local and the file can be created");

        URL indexURL = shpFiles.acquireRead(indexType, this);
        URL shpURL = shpFiles.acquireRead(SHP, this);
        try {

            if (indexURL == null) {
                return true;
            }
            // indexes require both the SHP and SHX so if either or missing then
            // you don't need to
            // index
            if (!shpFiles.exists(SHX) || !shpFiles.exists(SHP)) {
                return false;
            }

            File indexFile = DataUtilities.urlToFile(indexURL);
            File shpFile = DataUtilities.urlToFile(shpURL);
            long indexLastModified = indexFile.lastModified();
            long shpLastModified = shpFile.lastModified();
            boolean shpChangedMoreRecently = indexLastModified < shpLastModified;
            return !indexFile.exists() || shpChangedMoreRecently;
        } finally {
            if (shpURL != null) {
                shpFiles.unlockRead(shpURL, this);
            }
            if (indexURL != null) {
                shpFiles.unlockRead(indexURL, this);
            }
        }
    }

    /**
     * Returns true if the indices already exist and do not need to be
     * regenerated or cannot be generated (IE isn't local).
     * 
     * @return true if the indices already exist and do not need to be
     *         regenerated.
     */
    public boolean isIndexed() {
        if (shpFiles.isLocal()) {
            return true;
        }
        return !needsGeneration(FIX) && !needsGeneration(treeType.shpFileType);
    }

    // /**
    // * RTree query
    // *
    // * @param bbox
    // *
    // *
    // * @throws DataSourceException
    // * @throws IOException
    // */
    // private List queryRTree(Envelope bbox) throws DataSourceException,
    // IOException {
    // List goodRecs = null;
    // RTree rtree = this.openRTree();
    //
    // try {
    // if ((rtree != null) && (rtree.getBounds() != null)
    // && !bbox.contains(rtree.getBounds())) {
    // goodRecs = rtree.search(bbox);
    // }
    // } catch (LockTimeoutException le) {
    // throw new DataSourceException("Error querying RTree", le);
    // } catch (TreeException re) {
    // throw new DataSourceException("Error querying RTree", re);
    // }
    //
    // return goodRecs;
    // }

    /**
     * QuadTree Query
     * 
     * @param bbox
     * 
     * 
     * @throws DataSourceException
     * @throws IOException
     * @throws TreeException
     *                 DOCUMENT ME!
     */
    protected CloseableIterator<Data> queryQuadTree(Envelope bbox)
            throws DataSourceException, IOException, TreeException {
        CloseableIterator<Data> tmp = null;
        
        // check if the spatial index needs recreating
        createSpatialIndex(false);
        
        if(cachedTree == null) {
            boolean canCache = false;
            URL treeURL = shpFiles.acquireRead(QIX, this);
            try {
                File treeFile = DataUtilities.urlToFile(treeURL);

                if (treeFile.exists() && treeFile.length() < 1024 * maxQixCacheSize) {
                    canCache = true;
                }
            } finally {
                shpFiles.unlockRead(treeURL, this);
            }

            if(canCache) {
                QuadTree quadTree = openQuadTree();
                if(quadTree != null) {
                    LOGGER.warning("Experimental: loading in memory the quadtree for " + shpFiles.get(SHP));
                    cachedTree = new CachedQuadTree(quadTree);
                    quadTree.close();
                }
            }
        }
        if(cachedTree != null) {
            if(!bbox.contains(cachedTree.getBounds())) {
                return cachedTree.search(bbox);
            } else {
                return null;
            }
        } else {
            try {
                QuadTree quadTree = openQuadTree();
                if ((quadTree != null)
                        && !bbox.contains(quadTree.getRoot().getBounds())) {
                    tmp = quadTree.search(bbox);
                }
                if (tmp == null && quadTree != null) {
                    quadTree.close();
                }
            } catch (Exception e) {
                throw new DataSourceException("Error querying QuadTree", e);
            }
        }

        return tmp;
    }

    /**
     * Convenience method for opening a DbaseFileReader.
     * 
     * @return A new DbaseFileReader
     * 
     * @throws IOException
     *                 If an error occurs during creation.
     */
    protected DbaseFileReader openDbfReader() throws IOException {
        if (shpFiles.get(DBF) == null) {
            return null;
        }

        if (isLocal() && !shpFiles.exists(DBF)) {
            return null;
        }

        return new IndexedDbaseFileReader(shpFiles, useMemoryMappedBuffer, dbfCharset);
    }

    //
    // /**
    // * Convenience method for opening an RTree index.
    // *
    // * @return A new RTree.
    // *
    // * @throws IOException
    // * If an error occurs during creation.
    // * @throws DataSourceException
    // * DOCUMENT ME!
    // */
    // protected RTree openRTree() throws IOException {
    // if (!isLocal()) {
    // return null;
    // }
    // URL treeURL = shpFiles.acquireRead(GRX, this);
    // try {
    // File treeFile = DataUtilities.urlToFile(treeURL);
    //
    // if (!treeFile.exists() || (treeFile.length() == 0)) {
    // treeType = IndexType.NONE;
    // return null;
    // }
    //
    // try {
    // FileSystemPageStore fps = new FileSystemPageStore(treeFile);
    // rtree = new RTree(fps);
    // } catch (TreeException re) {
    // throw new DataSourceException("Error opening RTree", re);
    // }
    //
    // return rtree;
    // } finally {
    // shpFiles.unlockRead(treeURL, this);
    // }
    // }

    /**
     * Convenience method for opening a QuadTree index.
     * 
     * @return A new QuadTree
     * 
     * @throws StoreException
     */
    protected QuadTree openQuadTree() throws StoreException {
        if (!isLocal()) {
            return null;
        }
        URL treeURL = shpFiles.acquireRead(QIX, this);
        try {
            File treeFile = DataUtilities.urlToFile(treeURL);

            if (!treeFile.exists() || (treeFile.length() == 0)) {
                treeType = IndexType.NONE;
                return null;
            }

            try {
                FileSystemIndexStore store = new FileSystemIndexStore(treeFile);
                return store.load(openIndexFile(), useMemoryMappedBuffer);
            } catch (IOException e) {
                throw new StoreException(e);
            }
        } finally {
            shpFiles.unlockRead(treeURL, this);
        }

    }

    /**
     * Create a FeatureWriter for the given type name.
     * 
     * @param typeName
     *                The typeName of the FeatureType to write
     * @param transaction
     *                DOCUMENT ME!
     * 
     * @return A new FeatureWriter.
     * 
     * @throws IOException
     *                 If the typeName is not available or some other error
     *                 occurs.
     */
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> createFeatureWriter(String typeName,
            Transaction transaction) throws IOException {
        typeCheck(typeName);
        
        // when writing we may remove features, in order to prevent fid changes we'll need
        // a .fix file
        existsOrCreateFidIndex();

        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader;
        IndexedShapefileAttributeReader attReader;
        try {
            SimpleFeatureType schema = getSchema();
            if (schema == null) {
                throw new IOException(
                        "To create a shapefile, you must first call createSchema()");
            }
            attReader = getAttributesReader(true, true, null, schema);
            featureReader = createFeatureReader(typeName, attReader, schema);

        } catch (Exception e) {
            attReader = getAttributesReader(true, true, null, schema);
            featureReader = new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(schema);
        }
        
        if(featureReader == null) {
            return new EmptyFeatureWriter(schema);
        }

        return new IndexedShapefileFeatureWriter(typeName, shpFiles, attReader,
                featureReader, this, dbfCharset);
    }

    /**
     * @see org.geotools.data.AbstractDataStore#getBounds(org.geotools.data.Query)
     */
    protected ReferencedEnvelope getBounds(Query query) throws IOException {
        ReferencedEnvelope ret = null;

        Set records = new HashSet();
        Filter filter = query.getFilter();
        if (filter == Filter.INCLUDE || query == Query.ALL) {
            return getBounds();
        }
        // else if (this.useIndex) {
        // if (treeType == IndexType.GRX) {
        // return getBoundsRTree(query);
        // }
        // }

        Comparator<Identifier> identifierComparator = new IdentifierComparator();
        Set<Identifier> fids = (Set<Identifier>) filter.accept(
                IdCollectorFilterVisitor.IDENTIFIER_COLLECTOR, new TreeSet<Identifier>(identifierComparator));

        if (!fids.isEmpty()) {
            List<Data> recordsFound = queryFidIndex(fids);
            if (recordsFound != null) {
                records.addAll(recordsFound);
            }
        }

        if (records.isEmpty())
            return null;
        
        // grab a geometry factory... check for a special hint
        Hints hints = query.getHints(); 
        GeometryFactory geometryFactory = (GeometryFactory) hints.get(Hints.JTS_GEOMETRY_FACTORY);
        if (geometryFactory == null) {
            // look for a coordinate sequence factory
            CoordinateSequenceFactory csFactory = 
                (CoordinateSequenceFactory) hints.get(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);

            if (csFactory != null) {
                geometryFactory = new GeometryFactory(csFactory);
            }
        }

        if (geometryFactory == null) {
            // fall back on the default one
            geometryFactory = new GeometryFactory();
        }

        ShapefileReader reader = new ShapefileReader(shpFiles, false, false, geometryFactory);
        try {
            ret = new ReferencedEnvelope(getSchema().getCoordinateReferenceSystem());
            for (Iterator iter = records.iterator(); iter.hasNext();) {
                Data data = (Data) iter.next();
                reader.goTo(((Long) data.getValue(1)).intValue());
                Record record = reader.nextRecord();
                ret.expandToInclude(new Envelope(record.minX, record.maxX,
                        record.minY, record.maxY));
            }
            return ret;
        } finally {
            reader.close();
        }
    }

    // private ReferencedEnvelope getBoundsRTree(Query query) throws IOException
    // {
    // ReferencedEnvelope ret = null;
    //
    // RTree rtree = this.openRTree();
    //
    // if (rtree != null) {
    // try {
    // Envelope envelopeFromIndex = rtree.getBounds(query.getFilter());
    // ret = new ReferencedEnvelope(envelopeFromIndex, schema.getCRS());
    // } catch (TreeException e) {
    // LOGGER.log(Level.SEVERE, e.getMessage(), e);
    // } catch (UnsupportedFilterException e) {
    // // Ignoring...
    // } finally {
    // try {
    // rtree.close();
    // } catch (Exception ee) {
    // }
    // }
    // }
    // return ret;
    // }

    /**
     * @see org.geotools.data.DataStore#getFeatureSource(java.lang.String)
     *
    public SimpleFeatureSource getFeatureSource(final String typeName)
            throws IOException {
        final SimpleFeatureType featureType = getSchema(typeName);

        if (isWriteable) {
            if (getLockingManager() != null) {
                return new AbstractFeatureLocking() {
                    public DataStore getDataStore() {
                        return IndexedShapefileDataStore.this;
                    }

                    public void addFeatureListener(FeatureListener listener) {
                        listenerManager.addFeatureListener(this, listener);
                    }

                    public void removeFeatureListener(FeatureListener listener) {
                        listenerManager.removeFeatureListener(this, listener);
                    }

                    public SimpleFeatureType getSchema() {
                        return featureType;
                    }

                    public ReferencedEnvelope getBounds(Query query)
                            throws IOException {
                        return IndexedShapefileDataStore.this.getBounds(query);
                    }
                };
            } else {
                return new AbstractFeatureStore() {
                    public DataStore getDataStore() {
                        return IndexedShapefileDataStore.this;
                    }

                    public void addFeatureListener(FeatureListener listener) {
                        listenerManager.addFeatureListener(this, listener);
                    }

                    public void removeFeatureListener(FeatureListener listener) {
                        listenerManager.removeFeatureListener(this, listener);
                    }

                    public SimpleFeatureType getSchema() {
                        return featureType;
                    }

                    public ReferencedEnvelope getBounds(Query query)
                            throws IOException {
                        return IndexedShapefileDataStore.this.getBounds(query);
                    }
                };
            }
        } else {
            return new AbstractFeatureSource() {
                public DataStore getDataStore() {
                    return IndexedShapefileDataStore.this;
                }

                public void addFeatureListener(FeatureListener listener) {
                    listenerManager.addFeatureListener(this, listener);
                }

                public void removeFeatureListener(FeatureListener listener) {
                    listenerManager.removeFeatureListener(this, listener);
                }

                public SimpleFeatureType getSchema() {
                    return featureType;
                }

                public ReferencedEnvelope getBounds(Query query)
                        throws IOException {
                    return IndexedShapefileDataStore.this.getBounds(query);
                }
            };
        }
    }
    */

    //
    // /**
    // * Builds the RTree index
    // *
    // * @throws TreeException
    // * DOCUMENT ME!
    // */
    // void buildRTree() throws TreeException {
    // if (isLocal()) {
    // LOGGER.fine("Creating spatial index for " + shpFiles.get(SHP));
    //
    // synchronized (this) {
    // if (rtree != null) {
    // rtree.close();
    // }
    //
    // rtree = null;
    // }
    //
    // ShapeFileIndexer indexer = new ShapeFileIndexer();
    // indexer.setIdxType(IndexType.GRX);
    // indexer.setShapeFileName(shpFiles);
    //
    // try {
    // indexer.index(false, new NullProgressListener());
    // } catch (MalformedURLException e) {
    // throw new TreeException(e);
    // } catch (LockTimeoutException e) {
    // throw new TreeException(e);
    // } catch (Exception e) {
    // if (e instanceof TreeException) {
    // throw (TreeException) e;
    // } else {
    // throw new TreeException(e);
    // }
    // }
    // }
    // }

    /**
     * Builds the QuadTree index. Usually not necessary since reading features
     * will index when required
     * @throws TreeException
     */
    public void buildQuadTree() throws TreeException {
        if (isLocal()) {
            LOGGER.fine("Creating spatial index for " + shpFiles.get(SHP));

            ShapeFileIndexer indexer = new ShapeFileIndexer();
            indexer.setShapeFileName(shpFiles);
            
            try {
                indexer.index(false, new NullProgressListener());
            } catch (MalformedURLException e) {
                throw new TreeException(e);
            } catch (LockTimeoutException e) {
                throw new TreeException(e);
            } catch (Exception e) {
                if (e instanceof TreeException) {
                    throw (TreeException) e;
                } else {
                    throw new TreeException(e);
                }
            }
        }
    }

    public boolean isMemoryMapped() {
        return useMemoryMappedBuffer;
    }

    public String id() {
        return getClass().getName() + ": " + getCurrentTypeName();
    }
 
    @Override
    protected Set getSupportedHints() {
        Set<Hints.Key> hints = new HashSet<Hints.Key>();
        hints.add( Hints.FEATURE_DETACHED );
        hints.add( Hints.JTS_GEOMETRY_FACTORY );
        hints.add( Hints.JTS_COORDINATE_SEQUENCE_FACTORY );
        hints.add( Hints.GEOMETRY_DISTANCE);
        hints.add( Hints.SCREENMAP);
        return hints;
    }
}
