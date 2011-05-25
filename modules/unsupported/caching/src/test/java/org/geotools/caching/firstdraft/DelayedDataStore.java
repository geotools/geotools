/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching.firstdraft;

import java.io.IOException;
import java.util.List;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;


/**
 * @author  crousson
 *
 *
 * @source $URL$
 */
public class DelayedDataStore implements DataStore {
    private final DataStore sourceDataStore;
    private long msResponseDelay;

    public DelayedDataStore(DataStore sourceDataStore) {
        this.sourceDataStore = sourceDataStore;
        this.msResponseDelay = 0;
    }

    public DelayedDataStore(DataStore sourceDataStore, long msResponseDelay) {
        this.sourceDataStore = sourceDataStore;
        this.msResponseDelay = msResponseDelay;
    }

    /**
     * @param msResponseDelay  the msResponseDelay to set
     * @uml.property  name="msResponseDelay"
     */
    public void setMsResponseDelay(long ms) {
        this.msResponseDelay = ms;
    }

    public long getMsReponseDelay() {
        return this.msResponseDelay;
    }

    private synchronized void idle() {
        try {
            this.wait(this.msResponseDelay);
        } catch (InterruptedException e) {
            // do nothing
            e.printStackTrace();
        }
    }

    public void createSchema(SimpleFeatureType arg0) throws IOException {
        this.sourceDataStore.createSchema(arg0);
    }

    public FeatureReader getFeatureReader(Query arg0, Transaction arg1)
        throws IOException {
        idle();

        return this.sourceDataStore.getFeatureReader(arg0, arg1);
    }

    public SimpleFeatureSource getFeatureSource(String arg0)
        throws IOException {
        idle();

        return this.sourceDataStore.getFeatureSource(arg0);
    }

    public FeatureWriter getFeatureWriter(String arg0, Transaction arg1)
        throws IOException {
        idle();

        return this.sourceDataStore.getFeatureWriter(arg0, arg1);
    }

    public FeatureWriter getFeatureWriter(String arg0, Filter arg1, Transaction arg2)
        throws IOException {
        idle();

        return this.sourceDataStore.getFeatureWriter(arg0, arg1, arg2);
    }

    public FeatureWriter getFeatureWriterAppend(String arg0, Transaction arg1)
        throws IOException {
        idle();

        return this.sourceDataStore.getFeatureWriterAppend(arg0, arg1);
    }

    public LockingManager getLockingManager() {
        return this.sourceDataStore.getLockingManager();
    }

    public SimpleFeatureType getSchema(String arg0) throws IOException {
        return this.sourceDataStore.getSchema(arg0);
    }

    public String[] getTypeNames() throws IOException {
        return this.sourceDataStore.getTypeNames();
    }

    public void updateSchema(String arg0, SimpleFeatureType arg1)
        throws IOException {
        this.sourceDataStore.updateSchema(arg0, arg1);
    }

	public void dispose() {
		sourceDataStore.dispose();
		
	}

	public SimpleFeatureSource getFeatureSource(
			Name typeName) throws IOException {
		 idle();
		return sourceDataStore.getFeatureSource(typeName);
	}

	public ServiceInfo getInfo() {
		return sourceDataStore.getInfo();
	}

	public List<Name> getNames() throws IOException {
		return sourceDataStore.getNames();
	}

	public SimpleFeatureType getSchema(Name name) throws IOException {
		 idle();
		return sourceDataStore.getSchema(name);
	}

	public void updateSchema(Name typeName, SimpleFeatureType featureType)
			throws IOException {
		 idle();
		sourceDataStore.updateSchema(typeName, featureType);		
	}
}
