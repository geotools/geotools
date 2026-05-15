/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.dggs.datastore;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.LockingManager;
import org.geotools.api.data.Query;
import org.geotools.api.data.ServiceInfo;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.dggs.DGGSInstance;
import org.geotools.dggs.gstore.DGGSFeatureSource;
import org.geotools.dggs.gstore.DGGSResolutionCalculator;
import org.geotools.dggs.gstore.DGGSStore;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Polygon;

/**
 * Wraps another store containing feature types with the following characteristics:
 *
 * <ul>
 *   <li>A zoneId attribute
 *   <li>A resolution attribute (for ease of filtering)
 *   <li>Does not have a "geometry" attribute, the adds it
 * </ul>
 *
 * The zoneId is interpreted as a DGGS zone identifier, and common spatial queries and DGGS query functions translated
 * into tests against the zone identifiers.
 */
public class DGGSDataStore<I> implements DGGSStore<I> {

    private static final Class<?>[] ZONE_ID_BINDINGS = {
        String.class, Long.class, Integer.class, Short.class, java.math.BigInteger.class
    };

    private static final Class<?>[] RESOLUTION_BINDINGS = {Byte.class, Short.class, Integer.class};

    static final Logger LOGGER = Logging.getLogger(DGGSDataStore.class);

    /**
     * Was using {@link org.geotools.referencing.crs.DefaultGeographicCRS#WGS84} before, but then GeoServer would
     * configure the layer as EPSG:4326, the two CRSs are not exactly the same, and a
     * {@link org.geotools.data.crs.ForceCoordinateSystemFeatureResults} would be used in the middle, which would
     * prevent visitor optimizations from kicking in.
     */
    static final CoordinateReferenceSystem DEFAULT_CRS;

    static {
        try {
            DEFAULT_CRS = CRS.decode("EPSG:4326", true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode default CRS", e);
        }
    }

    /** The geometry property, in the returned features */
    public static final String GEOMETRY = "geometry";
    /** The default geometry property, often used in filters */
    public static final String DEFAULT_GEOMETRY = "";

    private final DGGSInstance<I> dggs;
    private final DataStore delegate;
    private final String zoneIdAttribute;
    private final DGGSResolutionCalculator resolutions;

    public DGGSDataStore(DGGSInstance<I> dggs, DataStore delegate, String zoneIdAttribute, Integer fixedResolution) {
        this.delegate = delegate;
        this.dggs = dggs;
        this.zoneIdAttribute = zoneIdAttribute;
        this.resolutions = new DGGSResolutionCalculator(dggs, fixedResolution);
    }

    /** return the attribute name used to store the DGGS zone identifier */
    public String getZoneIdAttribute() {
        return zoneIdAttribute;
    }

    @Override
    public ServiceInfo getInfo() {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setDescription("Features from " + getClass().getSimpleName());
        info.setSchema(FeatureTypes.DEFAULT_NAMESPACE);
        return info;
    }

    @Override
    public List<Name> getNames() throws IOException {
        return delegate.getNames().stream()
                .filter(n -> {
                    try {
                        return isDGGSSchema(delegate.getSchema(n));
                    } catch (IOException e) {
                        LOGGER.log(Level.WARNING, "Failed to grab schema for " + n, e);
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public String[] getTypeNames() throws IOException {
        return Arrays.stream(delegate.getTypeNames())
                .filter(t -> {
                    try {
                        return isDGGSSchema(delegate.getSchema(t));
                    } catch (IOException e) {
                        LOGGER.log(Level.WARNING, "Failed to grab schema for " + t, e);
                        return false;
                    }
                })
                .toArray(n -> new String[n]);
    }

    private boolean isDGGSSchema(SimpleFeatureType schema) {
        return checkAttribute(schema, zoneIdAttribute, ZONE_ID_BINDINGS)
                && (resolutions.hasFixedResolution() || checkAttribute(schema, RESOLUTION, RESOLUTION_BINDINGS))
                && schema.getDescriptor(GEOMETRY) == null;
    }

    private boolean checkAttribute(SimpleFeatureType schema, String name, Class<?>... expectedBindings) {
        return Optional.ofNullable(schema.getDescriptor(name))
                .filter(ad -> {
                    for (Class<?> binding : expectedBindings) {
                        if (binding.isAssignableFrom(ad.getType().getBinding())) {
                            return true;
                        }
                    }
                    return false;
                })
                .isPresent();
    }

    @Override
    public SimpleFeatureType getSchema(Name name) throws IOException {
        SimpleFeatureType ft = delegate.getSchema(name);
        return wrapType(ft);
    }

    @Override
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        SimpleFeatureType ft = delegate.getSchema(typeName);
        return wrapType(ft);
    }

    private SimpleFeatureType wrapType(SimpleFeatureType ft) {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(ft.getName());
        for (AttributeDescriptor ad : ft.getAttributeDescriptors()) {
            tb.minOccurs(0);
            tb.add(ad.getLocalName(), ad.getType().getBinding());
        }
        tb.add("geometry", Polygon.class, DEFAULT_CRS);
        return tb.buildFeatureType();
    }

    @Override
    public DGGSFeatureSource<I> getFeatureSource(String typeName) throws IOException {
        return new DGGSFeatureSourceImpl<>(this, delegate.getFeatureSource(typeName), getSchema(typeName));
    }

    @Override
    public DGGSFeatureSource<I> getFeatureSource(Name typeName) throws IOException {
        return new DGGSFeatureSourceImpl<>(this, delegate.getFeatureSource(typeName), getSchema(typeName));
    }

    @Override
    public DGGSFeatureSource<I> getDGGSFeatureSource(String typeName) throws IOException {
        return getFeatureSource(typeName);
    }

    @Override
    public void dispose() {
        // Since we went through the repository to get the delegate, we should not close it
        // ourselves
        dggs.close();
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query, Transaction transaction)
            throws IOException {
        // just delegating to the FeatureSource machinery
        DGGSFeatureSource<I> source = getFeatureSource(query.getTypeName());
        @SuppressWarnings("PMD.CloseResource") // wrapped and returned
        SimpleFeatureIterator features = source.getFeatures(query).features();
        return new FeatureReader<>() {
            @Override
            public SimpleFeatureType getFeatureType() {
                return source.getSchema();
            }

            @Override
            public SimpleFeature next() throws IllegalArgumentException, NoSuchElementException {
                return features.next();
            }

            @Override
            public boolean hasNext() {
                return features.hasNext();
            }

            @Override
            public void close() {
                features.next();
            }
        };
    }

    @Override
    public LockingManager getLockingManager() {
        // cannot write so not relevant, but still need to respect interface
        return delegate.getLockingManager();
    }

    // UNSUPPORTED WRITE STUFF -----------------------------------------------------

    @Override
    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeSchema(String typeName) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Filter filter, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName, Transaction transaction)
            throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            String typeName, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeSchema(Name typeName) throws IOException {
        throw new UnsupportedOperationException();
    }

    public DataStore getDelegate() {
        return delegate;
    }

    public DGGSInstance<I> getDggs() {
        return dggs;
    }

    public DGGSResolutionCalculator getResolutions() {
        return resolutions;
    }
}
