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
package org.geotools.data.property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Uses PropertyAttributeWriter to generate a property file on disk.
 *
 *
 * @source $URL$
 */
public class PropertyFeatureWriter implements
        FeatureWriter<SimpleFeatureType, SimpleFeature> {

    PropertyDataStore store;
    File read;
    PropertyAttributeReader reader;
    File write;
    PropertyAttributeWriter writer;
    SimpleFeature origional = null;
    SimpleFeature live = null;

    public PropertyFeatureWriter(PropertyDataStore dataStore, String typeName)
            throws IOException {
        store = dataStore;
        File dir = store.directory;
        read = new File(dir, typeName + ".properties");
        write = File.createTempFile(typeName + System.currentTimeMillis(),
                null, dir);

        reader = new PropertyAttributeReader(read);
        writer = new PropertyAttributeWriter(write, reader.type);
    }

    // constructor end

    // getFeatureType start
    public SimpleFeatureType getFeatureType() {
        return reader.type;
    }

    // getFeatureType end
    // hasNext start
    public boolean hasNext() throws IOException {
        if (writer == null) {
            throw new IOException("Writer has been closed");
        }
        if (live != null && origional != null) {
            // we have returned something to the user,
            // and it has not been writen out or removed
            //
            writeImplementation(origional);
            origional = null;
            live = null;
        }
        return reader.hasNext();
    }

    // hasNext end
    // writeImplementation start
    private void writeImplementation(SimpleFeature f) throws IOException {
        writer.next();
        String fid = f.getID();
        if( Boolean.TRUE.equals( f.getUserData().get(Hints.USE_PROVIDED_FID) ) ){
            if( f.getUserData().containsKey(Hints.PROVIDED_FID)){
                fid = (String) f.getUserData().get(Hints.PROVIDED_FID);
            }
        }
        writer.writeFeatureID(fid);
        for (int i = 0; i < f.getAttributeCount(); i++) {
            Object value = f.getAttribute(i);
            writer.write(i, value );            
        }
    }

    // writeImplementation end
    // next start
    long nextFid = System.currentTimeMillis(); // seed with a big number
    public SimpleFeature next() throws IOException {
        if (writer == null) {
            throw new IOException("Writer has been closed");
        }
        String fid = null;
        SimpleFeatureType type = reader.type;
        try {
            if (hasNext()) {
                reader.next(); // grab next line

                fid = reader.getFeatureID();
                Object values[] = new Object[reader.getAttributeCount()];
                for (int i = 0; i < reader.getAttributeCount(); i++) {
                    values[i] = reader.read(i);
                }

                origional = SimpleFeatureBuilder.build(type, values, fid);
                live = SimpleFeatureBuilder.copy(origional);
                return live;
            } else {
                fid = type.getTypeName() + "." + (nextFid++);
                Object values[] = DataUtilities.defaultValues(type);

                origional = null;
                live = SimpleFeatureBuilder.build(type, values, fid);
                return live;
            }
        } catch (IllegalAttributeException e) {
            String message = "Problem creating feature "
                    + (fid != null ? fid : "");
            throw new DataSourceException(message, e);
        }
    }

    // next end
    // write start
    public void write() throws IOException {
        if (live == null) {
            throw new IOException("No current feature to write");
        }
        if (live.equals(origional)) {
            writeImplementation(origional);
        } else {
            writeImplementation(live);
            String typeName = live.getFeatureType().getTypeName();
            Transaction autoCommit = Transaction.AUTO_COMMIT;
            if (origional != null) {
                ReferencedEnvelope bounds = new ReferencedEnvelope();
                bounds.include(live.getBounds());
                bounds.include(origional.getBounds());
                store.listenerManager.fireFeaturesChanged(typeName, autoCommit,
                        bounds, false);
            } else {
                store.listenerManager.fireFeaturesAdded(typeName, autoCommit,
                        ReferencedEnvelope.reference(live.getBounds()), false);
            }
        }
        origional = null;
        live = null;
    }

    // write end
    // remove start
    public void remove() throws IOException {
        if (live == null) {
            throw new IOException("No current feature to remove");
        }
        if (origional != null) {
            String typeName = live.getFeatureType().getTypeName();
            Transaction autoCommit = Transaction.AUTO_COMMIT;
            store.listenerManager.fireFeaturesRemoved(typeName, autoCommit,
                    ReferencedEnvelope.reference(origional.getBounds()), false);
        }
        origional = null;
        live = null; // prevent live and remove from being written out
    }
    // remove end

    // close start
    public void close() throws IOException {
        if (writer == null) {
            throw new IOException("writer already closed");
        }
        // write out remaining contents from reader
        // if applicable
        while (reader.hasNext()) {
            reader.next(); // advance
            writer.next();
            writer.echoLine(reader.line); // echo unchanged
        }
        writer.close();
        reader.close();
        writer = null;
        reader = null;
        read.delete();

        if (write.exists() && !write.renameTo(read)) {
            FileChannel out = new FileOutputStream(read).getChannel();
            FileChannel in = new FileInputStream(write).getChannel();
            try {
                long len = in.size();
                long copied = out.transferFrom(in, 0, in.size());

                if (len != copied) {
                    throw new IOException("unable to complete write");
                }
            } finally {
                in.close();
                out.close();
            }
        }
        read = null;
        write = null;
        store = null;
    }
    // close end
}
