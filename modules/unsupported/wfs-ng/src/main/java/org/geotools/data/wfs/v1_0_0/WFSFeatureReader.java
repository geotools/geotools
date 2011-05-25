/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureReader;
import org.geotools.data.wfs.v1_0_0.Action.InsertAction;
import org.geotools.data.wfs.v1_0_0.Action.UpdateAction;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.XMLHandlerHints;
import org.geotools.xml.gml.FCBuffer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.xml.sax.SAXException;


/**
 * <p>
 * DOCUMENT ME!
 * </p>
 *
 * @author dzwiers
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/main/java/org/geotools/data/wfs/v1_0_0/WFSFeatureReader.java $
 */
public class WFSFeatureReader extends FCBuffer {
    private InputStream is = null;
    private WFSTransactionState ts = null;
    private SimpleFeature next = null;
    private int insertSearchIndex = -1;

    private WFSFeatureReader(InputStream is, int capacity, int timeout,
        WFSTransactionState trans, SimpleFeatureType ft) {
        //document may be null
        super(null, capacity, timeout,ft);
        this.is = is;
        ts = trans;
    }

    /**
     * 
     * @param document
     * @param capacity
     * @param timeout
     * @param transaction
     * @param ft
     * @return WFSFeatureReader
     * @throws SAXException
     */
    public static  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(URI document, int capacity,
        int timeout, WFSTransactionState transaction, SimpleFeatureType ft) throws SAXException {
        HttpURLConnection hc;

        try {
            hc = (HttpURLConnection) document.toURL().openConnection();

            return getFeatureReader(hc.getInputStream(), capacity, timeout,
                transaction, ft);
        } catch (MalformedURLException e) {
            logger.warning(e.toString());
            throw new SAXException(e);
        } catch (IOException e) {
            logger.warning(e.toString());
            throw new SAXException(e);
        }
    }

    /**
     * 
     * @param is
     * @param capacity
     * @param timeout
     * @param transaction
     * @param ft
     * @return WFSFeatureReader
     * @throws SAXException
     */
    public static WFSFeatureReader getFeatureReader(InputStream is,
        int capacity, int timeout, WFSTransactionState transaction, SimpleFeatureType ft)
        throws SAXException {
        WFSFeatureReader fc = new WFSFeatureReader(is, capacity, timeout,
                transaction, ft);
        fc.start(); // calls run

        if (fc.exception != null) {
            throw fc.exception;
        }

        return fc;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        XMLHandlerHints hints = new XMLHandlerHints();
        initHints(hints);

        try {
            try {
                DocumentFactory.getInstance(is, hints, logger.getLevel());
                is.close();

                // start parsing until buffer part full, then yield();
            } catch (StopException e) {
                exception = e;
                state = STOP;
                is.close();
                yield();
            } catch (SAXException e) {
                exception = e;
                state = STOP;
                is.close();
                yield();
            }  catch (RuntimeException e) {
            	exception = new SAXException(e.getMessage());
            	exception.initCause(e);
                state = STOP;
                is.close();
                yield();
            }
        } catch (IOException e) {
            logger.warning(e.toString());
        }
    }

    protected void initHints( XMLHandlerHints hints ) {
        super.initHints(hints);

//        Map<String,URI> schemas=new HashMap<String,URI>(1);
//        SimpleFeatureType wfsFT= ft;
//        
//        String namespace = ft.getName().getNamespaceURI();
//        URI uri;
//        try {
//            uri = new URI( namespace );
//            schemas.put(namespace,uri);
//            hints.put(XMLHandlerHints.NAMESPACE_MAPPING, schemas);
//            
//        } catch (URISyntaxException e) {            
//        }
    }
    
    /**
     * 
     * @see org.geotools.data.FeatureReader#hasNext()
     */
    public boolean hasNext() throws IOException {
        if (next != null) {
            return true;
        }

        try {
            loadElement();
        } catch (NoSuchElementException e) {
            return false;
        }

        return next != null;
    }

    private boolean loadElement()
        throws NoSuchElementException, IOException {
        if (ts == null) {
            while ((next == null) && super.hasNext()){
                next = super.next();
            }
        } else {
            List l = ts.getActions(ft.getTypeName());

            if ((insertSearchIndex < l.size()) && (next == null) ) {
                // look for an insert then
                // advance one spot

                while ((insertSearchIndex+1 < l.size()) && (next == null)) {
                    insertSearchIndex++;
                    Action a = (Action) l.get(insertSearchIndex);
                    if (a.getType() == Action.INSERT) {
                        InsertAction ia = (InsertAction) a;
                        next = ia.getFeature();

                        //run thorough the rest to look for deletes / mods
                        int i = insertSearchIndex + 1;

                        while ((i < l.size()) && (next != null)) {
                            a = (Action) l.get(i);
                            next=updateFeature(a, next);
                            i++;
                        }
                    }
                }
            }
            
            while ((next == null) && super.hasNext()) {
                next = super.next();

                if ((ts != null) && (next != null)) {
                    // 	check to make sure it wasn't deleted
                    // 	check for updates
                    Iterator i = l.iterator();

                    while (i.hasNext() && (next != null)) {
                        Action a = (Action) i.next();

                        next=updateFeature(a, next);
                    }
                }
            }

        }

        return next != null;
    }

    private SimpleFeature updateFeature( Action a, SimpleFeature feature ) {
        if ((a.getType() == Action.DELETE)
                && a.getFilter().evaluate(feature)) {
            return null;
        } else {
            if ((a.getType() == Action.UPDATE)
                    && a.getFilter().evaluate(feature)) {
                // update the feature
                UpdateAction ua = (UpdateAction) a;
                ua.update(feature);
            }
        }
        return feature;
    }

    /**
     * @see org.geotools.data.FeatureReader#next()
     */
    public SimpleFeature next()
        throws IOException, NoSuchElementException {
        if (next == null) {
            loadElement(); // load it

            if (next == null) {
                throw new NoSuchElementException();
            }
        }
        SimpleFeature r = next;
        next = null;

        return r;
    }

}
