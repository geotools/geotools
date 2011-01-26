/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.dir;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.data.AbstractFileDataStore;
import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.view.DefaultView;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.NameImpl;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;


/**
 * This datastore represents methods of reading an enture directory. It
 * propagates actual reading / writing of the data to the dataStore  which
 * reads / writes the requested format.
 * </p>
 *
 * @author David Zwiers, Refractions Research, Inc.
 * @source $URL$
 * @deprecated Use {@link org.geotools.data.directory.DirectoryDataStore} instead
 */
public class DirectoryDataStore implements DataStore, LockingManager {

    // the directory for this ds
    private File dir;

    // map of featureTypes to dataStore instances
    private Map dataStores;

    // suffix order to attempt to store new featureTypes
    private String[] createOrder;

    /** List<TypeEntry> subclass control provided by createContents.
     * <p>
     * Access via entries(), creation by createContents.
     */
    private List contents = null;   
    
    // should not be used
    private DirectoryDataStore() {
    }

    // This is the *better* implementation of getview from AbstractDataStore
    public FeatureSource<SimpleFeatureType, SimpleFeature> getView(final Query query)
        throws IOException, SchemaException {
        return new DefaultView( this.getFeatureSource( query.getTypeName() ), query );
    }
    
    public DirectoryDataStore(File f) throws MalformedURLException, IOException {
        dir = f;
        createOrder = FileDataStoreFinder.getAvailableFileExtentions().toArray( new String[0]);
        dataStores = new HashMap();
        load(f);
    }
    /**
     * Creates a new DirectoryDataStore object.
     *
     * @param f File the directory
     * @param co list of file suffixes in order of preference for creating new
     *        FTs
     *
     * @throws MalformedURLException
     * @throws IOException
     */
    public DirectoryDataStore(File f, String[] co)
        throws MalformedURLException, IOException {
        dir = f;
        createOrder = co;
        dataStores = new HashMap();
        load(f);
    }

    private void load( File f ) throws IOException, MalformedURLException {
        // load list of dataStores by typeName
        File[] children = f.listFiles();

        for (int i = 0; i < children.length; i++) {
            if (children[i].isFile()) {
                AbstractFileDataStore afds = (AbstractFileDataStore) FileDataStoreFinder
                    .getDataStore(children[i].toURI().toURL());

                if (afds != null) {
                    dataStores.put(afds.getTypeNames()[0], afds);
                }
            }
        }
    }

    public ServiceInfo getInfo() {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setDescription("Features from Directory "+dir );
        info.setSchema( FeatureTypes.DEFAULT_NAMESPACE );
        info.setSource( dir.toURI() );
        try {
            info.setPublisher( new URI(System.getProperty("user.name")) );
        } catch (URISyntaxException e) {
        }
        return info;
    }
    
    /**
     * @see org.geotools.data.DataStore#getTypeNames()
     */
    public String[] getTypeNames() throws IOException {
        Set l = new HashSet();
        Iterator i = dataStores.values().iterator();

        while (i.hasNext()) {
            AbstractFileDataStore afds = (AbstractFileDataStore) i.next();
            String[] strs = afds.getTypeNames();

            if (strs != null) {
                for (int j = 0; j < strs.length; j++)
                    l.add(strs[j]);
            }
        }

        return (String[]) l.toArray(new String[l.size()]);
    }

    /**
     * @see org.geotools.data.DataStore#getSchema(java.lang.String)
     */
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        AbstractFileDataStore afds = (AbstractFileDataStore) dataStores.get(typeName);

        if (afds != null) {
            return afds.getSchema();
        }

        return null;
    }

    public void createSchema( final SimpleFeatureType featureType ) throws IOException
    {
        boolean notDone = true;
        int i = 0;

        while (notDone && (i < createOrder.length)) {
            File f = new File(dir, featureType.getTypeName() + createOrder[i]);

            if (!f.exists()) {
                AbstractFileDataStore afds = (AbstractFileDataStore) FileDataStoreFinder
                    .getDataStore(f.toURI().toURL());

                if (afds != null) {
                    afds.createSchema(featureType);
                    dataStores.put(featureType.getTypeName(), afds);
                    notDone = false;
                }
            }

            i++;
        }
    }

    public void updateSchema( final String typeName, final SimpleFeatureType featureType ) throws IOException
    {
        AbstractFileDataStore afds = (AbstractFileDataStore) dataStores.get(typeName);

        if (afds != null) {
            afds.updateSchema(featureType);
        }
    }

    /**
     * @see org.geotools.data.DataStore#getFeatureSource(java.lang.String)
     */
    public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource(String typeName)
        throws IOException {
        AbstractFileDataStore afds = (AbstractFileDataStore) dataStores.get(typeName);

        if (afds != null) {
            return afds.getFeatureSource();
        }

        return null;
    }

    /**
     * @see org.geotools.data.DataStore#getFeatureReader(org.geotools.data.Query,
     *      org.geotools.data.Transaction)
     */
    public  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query, Transaction transaction)
        throws IOException {
        AbstractFileDataStore afds = (AbstractFileDataStore) dataStores.get(query
                .getTypeName());

        if (afds != null) {
            return afds.getFeatureReader(query, transaction);
        }

        return null;
    }

    /**
     * @see org.geotools.data.DataStore#getFeatureWriter(java.lang.String,
            Filter, org.geotools.data.Transaction)
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName, Filter filter,
        Transaction transaction) throws IOException {
        AbstractFileDataStore afds = (AbstractFileDataStore) dataStores.get(typeName);

        if (afds != null) {
            return afds.getFeatureWriter(filter, transaction);
        }

        return null;
    }

    /**
     * @see org.geotools.data.DataStore#getFeatureWriter(java.lang.String,
     *      org.geotools.data.Transaction)
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
        Transaction transaction) throws IOException {
        AbstractFileDataStore afds = (AbstractFileDataStore) dataStores.get(typeName);

        if (afds != null) {
            return afds.getFeatureWriter(transaction);
        }

        return null;
    }

    /**
     * @see org.geotools.data.DataStore#getFeatureWriterAppend(java.lang.String,
     *      org.geotools.data.Transaction)
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName,
        Transaction transaction) throws IOException {
        AbstractFileDataStore afds = (AbstractFileDataStore) dataStores.get(typeName);

        if (afds != null) {
            return afds.getFeatureWriterAppend(transaction);
        }

        return null;
    }

    /**
     * @see org.geotools.data.DataStore#getLockingManager()
     */
    public LockingManager getLockingManager() {
        return this;
    }

    /**
     * @see org.geotools.data.LockingManager#exists(java.lang.String)
     */
    public boolean exists(String authID) {
        Iterator i = dataStores.values().iterator();

        while (i.hasNext()) {
            AbstractFileDataStore afds = (AbstractFileDataStore) i.next();

            if ((afds.getLockingManager() != null)
                    && afds.getLockingManager().exists(authID)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @see org.geotools.data.LockingManager#release(java.lang.String,
     *      org.geotools.data.Transaction)
     */
    public boolean release(String authID, Transaction transaction)
        throws IOException {
        Iterator i = dataStores.values().iterator();

        while (i.hasNext()) {
            AbstractFileDataStore afds = (AbstractFileDataStore) i.next();

            if ((afds.getLockingManager() != null)
                    && afds.getLockingManager().exists(authID)) {
                return afds.getLockingManager().release(authID, transaction);
            }
        }

        return false;
    }

    /**
     * @see org.geotools.data.LockingManager#refresh(java.lang.String,
     *      org.geotools.data.Transaction)
     */
    public boolean refresh(String authID, Transaction transaction)
        throws IOException {
        Iterator i = dataStores.values().iterator();

        while (i.hasNext()) {
            AbstractFileDataStore afds = (AbstractFileDataStore) i.next();

            if ((afds.getLockingManager() != null)
                    && afds.getLockingManager().exists(authID)) {
                return afds.getLockingManager().refresh(authID, transaction);
            }
        }

        return false;
    }

    /**
     * @see org.geotools.data.LockingManager#unLockFeatureID(java.lang.String,
     *      java.lang.String, org.geotools.data.Transaction,
     *      org.geotools.data.FeatureLock)
     */
    public void unLockFeatureID(String typeName, String authID,
        Transaction transaction, FeatureLock featureLock)
        throws IOException {
        AbstractFileDataStore afds = (AbstractFileDataStore) dataStores.get(typeName);

        if ((afds != null) && (afds.getLockingManager() != null)) {
            afds.getLockingManager().unLockFeatureID(typeName, authID,
                transaction, featureLock);
        }
    }

    /**
     * @see org.geotools.data.LockingManager#lockFeatureID(java.lang.String,
     *      java.lang.String, org.geotools.data.Transaction,
     *      org.geotools.data.FeatureLock)
     */
    public void lockFeatureID(String typeName, String authID,
        Transaction transaction, FeatureLock featureLock)
        throws IOException {
        AbstractFileDataStore afds = (AbstractFileDataStore) dataStores.get(typeName);

        if ((afds != null) && (afds.getLockingManager() != null)) {
            afds.getLockingManager().lockFeatureID(typeName, authID,
                transaction, featureLock);
        }
    }
    
    /**
     * Will dispose all child datastores. After this call the {@link DirectoryDataStore} will
     * be unusable.
     */
    public void dispose() {
        if(dataStores != null) {
            for (Iterator it = dataStores.values().iterator(); it.hasNext();) {
                DataStore ds = (DataStore) it.next();
                ds.dispose();
            }
            dataStores = null;
        }
    }

    /**
     * Delegates to {@link #getFeatureSource(String)} with
     * {@code name.getLocalPart()}
     * 
     * @since 2.5
     * @see DataAccess#getFeatureSource(Name)
     */
    public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource(Name typeName)
            throws IOException {
        return getFeatureSource(typeName.getLocalPart());
    }

    /**
     * Returns the same list of names than {@link #getTypeNames()} meaning the
     * returned Names have no namespace set.
     * 
     * @since 2.5
     * @see DataAccess#getNames()
     */
    public List<Name> getNames() throws IOException {
        String[] typeNames = getTypeNames();
        List<Name> names = new ArrayList<Name>(typeNames.length);
        for (String typeName : typeNames) {
            names.add(new NameImpl(typeName));
        }
        return names;
    }

    /**
     * Delegates to {@link #getSchema(String)} with {@code name.getLocalPart()}
     * 
     * @since 2.5
     * @see DataAccess#getSchema(Name)
     */
    public SimpleFeatureType getSchema(Name name) throws IOException {
        return getSchema(name.getLocalPart());
    }

    /**
     * Delegates to {@link #updateSchema(String, SimpleFeatureType)} with
     * {@code name.getLocalPart()}
     * 
     * @since 2.5
     * @see DataAccess#getFeatureSource(Name)
     */
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        updateSchema(typeName.getLocalPart(), featureType);
    }
}
