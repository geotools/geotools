/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005-2006, David Zwiers
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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.opengis.ows10.KeywordsType;

import org.geotools.data.AbstractFeatureSource;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultFeatureResults;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.ResourceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * {@link FeatureSource} extension interface to provide WFS specific extra information.
 * 
 * @author dzwiers
 *
 *
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/wfs/src/main/java/org/geotools
 *         /wfs/v_1_0_0/data/WFSFeatureSource.java $
 */
public class WFSFeatureSource extends AbstractFeatureSource implements SimpleFeatureSource {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.wfs");

    protected WFS_1_0_0_DataStore ds;

    protected String fname;

    private FeatureSetDescription featureSetDescription;

    protected WFSFeatureSource( WFS_1_0_0_DataStore ds, String fname ) {
        this.ds = ds;
        this.fname = fname;
        this.featureSetDescription = WFSCapabilities.getFeatureSetDescription(ds.getCapabilities(),
                fname);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Set getSupportedHints() {
        return super.getSupportedHints();
    }
    /**
     * @since 2.5
     * @see FeatureSource#getName()
     */
    public Name getName() {
        if (featureSetDescription != null && featureSetDescription.getNamespace() != null) {
            return new NameImpl(featureSetDescription.getNamespace().toString(), fname);
        }
        return new NameImpl(fname);
    }

    /**
     * Resource information from the wfs capabilities document.
     * 
     * @return ResoruceInfo from the capabilities document
     * @see FeatureSource#getInfo()
     */
    public ResourceInfo getInfo() {
        return new ResourceInfo(){
            public ReferencedEnvelope getBounds() {
                try {
                    return ds.getBounds(new Query(fname));
                } catch (IOException e) {
                    return new ReferencedEnvelope();
                }
            }

            public CoordinateReferenceSystem getCRS() {
                try {
                    return CRS.decode(featureSetDescription.getSRS());
                } catch (NoSuchAuthorityCodeException e) {
                    LOGGER.log(Level.INFO, e.getMessage(), e);
                } catch (FactoryException e) {
                    LOGGER.log(Level.INFO, e.getMessage(), e);
                }
                return null;
            }

            public String getDescription() {
                return featureSetDescription.getAbstract();
            }

            public Set<String> getKeywords() {
                return extractKeywords(featureSetDescription.getKeywords());
            }
            
            @SuppressWarnings("unchecked")
            private Set<String> extractKeywords(List<?> keywordsList) {
                Set<String> keywords = new HashSet<String>();
                if (keywordsList != null) {
                    for (Object keys : keywordsList) {
                        if (keys instanceof KeywordsType) {
                            List<String> kws = ((KeywordsType)keys).getKeyword();
                            for (String kw : kws) {
                                if (kw != null && kw.trim().length() > 0) {
                                    keywords.add(kw);
                                }
                            }
                        } else if (keys instanceof String) {
                            keywords.add((String)keys);
                        }
                    }
                }
                return keywords;
            }

            public String getName() {
                return fname;
            }

            @SuppressWarnings("deprecation")
            public URI getSchema() {
                try {
                    return ds.protocolHandler.getDescribeFeatureTypeURLGet(fname).toURI();
                } catch (MalformedURLException e) {
                    return null;
                } catch (URISyntaxException e) {
                    return null;
                }
            }

            public String getTitle() {
                String title = featureSetDescription.getTitle();
                if (null == title || title.trim().length() == 0) {
                    title = getName();
                }
                return title;
            }
        };
    }

    /**
     * @see org.geotools.data.FeatureSource#getDataStore()
     */
    public DataStore getDataStore() {
        return ds;
    }

    /**
     * @see org.geotools.data.FeatureSource#addFeatureListener(org.geotools.data.FeatureListener)
     */
    public void addFeatureListener( FeatureListener listener ) {
        ds.listenerManager.addFeatureListener(this, listener);
    }

    /**
     * @see org.geotools.data.FeatureSource#removeFeatureListener(org.geotools.data.FeatureListener)
     */
    public void removeFeatureListener( FeatureListener listener ) {
        ds.listenerManager.removeFeatureListener(this, listener);
    }

    /**
     * @see org.geotools.data.FeatureSource#getSchema()
     */
    public SimpleFeatureType getSchema() {
        try {
            return ds.getSchema(fname);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * @see org.geotools.data.FeatureSource#getBounds()
     */
    public ReferencedEnvelope getBounds() throws IOException {
        return getBounds((fname == null) ? Query.ALL : new Query(fname));
    }

    /**
     * @see org.geotools.data.FeatureSource#getBounds(org.geotools.data.Query)
     */
    public ReferencedEnvelope getBounds( Query query ) throws IOException {
        return ds.getBounds(namedQuery(query));
    }

    /**
     * @see org.geotools.data.FeatureSource#getFeatures()
     */
    public SimpleFeatureCollection getFeatures() throws IOException {
        return getFeatures(new Query(getSchema().getTypeName(), Filter.INCLUDE));
    }

    /**
     * @see org.geotools.data.FeatureSource#getFeatures(Filter)
     */
    public SimpleFeatureCollection getFeatures( Filter filter )
            throws IOException {
        return getFeatures(new Query(getSchema().getTypeName(), filter));
    }

    /**
     * @see org.geotools.data.FeatureSource#getFeatures(org.geotools.data.Query)
     */
    public SimpleFeatureCollection getFeatures( Query query )
            throws IOException {
        SimpleFeatureType schema = getSchema();
        String typeName = schema.getTypeName();

        if (query.getTypeName() == null) { // typeName unspecified we will
            // "any" use a default
            Query Query = new Query(query);
            Query.setTypeName(typeName);
            query = Query;
        }

        if (!typeName.equals(query.getTypeName())) {
            return new EmptyFeatureCollection(schema);
        } else {
            return new DefaultFeatureResults(this, query);
        }
    }

    /**
     * @see org.geotools.data.AbstractFeatureSource#getTransaction()
     */
    public Transaction getTransaction() {
        return Transaction.AUTO_COMMIT;
    }

    /**
     * @author dzwiers
     */
    public static class WFSFeatureResults extends DefaultFeatureResults {
        private WFSFeatureSource fs;

        private Query query;

        /**
         * @param fs
         * @param query
         */
        public WFSFeatureResults( WFSFeatureSource fs, Query query ) throws IOException {
            super(fs, query);
            this.query = query;
            this.fs = fs;
        }

        /**
         * @see org.geotools.data.FeatureResults#getSchema()
         */
        public SimpleFeatureType getSchema() {
            return fs.getSchema();
        }

        /**
         * @see org.geotools.data.FeatureResults#reader()
         */
        public FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
            return fs.ds.getFeatureReader(query, fs.getTransaction());
        }

        /**
         * @see org.geotools.data.FeatureResults#getBounds()
         */
        public ReferencedEnvelope getBounds() {
            try {
                return fs.getBounds(query);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * @see org.geotools.data.FeatureResults#getCount()
         */
        public int getCount() throws IOException {
            return fs.getCount(query);
        }
    }
}
