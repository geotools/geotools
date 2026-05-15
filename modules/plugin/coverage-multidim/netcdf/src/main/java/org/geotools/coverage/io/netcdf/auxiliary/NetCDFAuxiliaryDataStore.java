/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf.auxiliary;

import it.geosolutions.imageio.stream.input.FileImageInputStreamExtImpl;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import javax.imageio.stream.ImageInputStream;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.coverage.io.catalog.CoverageSlice;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.imageio.netcdf.NetCDFImageReader;
import org.geotools.imageio.netcdf.VariableAdapter;
import org.geotools.imageio.netcdf.slice.NetCDFSliceProvider;
import ucar.nc2.Variable;
import ucar.nc2.dataset.VariableDS;

/**
 * A {@link ContentDataStore} that exposes the NetCDF slices directly from the reader, without relying on an external DB
 * index.
 */
public class NetCDFAuxiliaryDataStore extends ContentDataStore {

    private final NetCDFImageReader reader;
    private final List<AuxiliaryType> types;

    public NetCDFAuxiliaryDataStore(File file, String namespaceURI) throws IOException {
        Objects.requireNonNull(file, "file");
        if (namespaceURI != null) {
            setNamespaceURI(namespaceURI);
        }

        this.reader = createReader(file);
        this.types = Collections.unmodifiableList(loadTypes(reader, namespaceURI));
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        List<Name> names = new ArrayList<>(types.size());
        for (AuxiliaryType type : types) {
            names.add(type.name);
        }
        return names;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        AuxiliaryType type = getType(entry.getName());
        return new NetCDFAuxiliaryFeatureSource(entry, type);
    }

    AuxiliaryType getType(Name name) throws IOException {
        for (AuxiliaryType type : types) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new IOException("Unknown type: " + name);
    }

    @Override
    public void dispose() {
        try {
            reader.dispose();
        } finally {
            super.dispose();
        }
    }

    @SuppressWarnings("PMD.CloseResource")
    private static NetCDFImageReader createReader(File file) throws IOException {
        ImageInputStream iis = null;
        NetCDFImageReader reader = null;
        try {
            // The imageInputStream will be closed by the reader when the reader is disposed,
            // so we don't need to worry about it here.
            iis = new FileImageInputStreamExtImpl(file);
            reader = new NetCDFImageReader(null);
            reader.setInput(iis, false, false);
            return reader;
        } catch (RuntimeException e) {
            if (iis != null) {
                iis.close();
            }
            if (e.getCause() instanceof IOException ioe) {
                throw ioe;
            }
            throw new IOException("Failed to open NetCDF reader for " + file, e);
        }
    }

    private static List<AuxiliaryType> loadTypes(NetCDFImageReader reader, String namespaceURI) throws IOException {
        List<AuxiliaryType> result = new ArrayList<>();
        int variableImageStartIndex = 0;

        for (Name coverageName : reader.getCoveragesNames()) {
            try {
                VariableAdapter adapter = reader.getCoverageDescriptor(coverageName);
                if (adapter == null) {
                    continue;
                }

                Variable variable = reader.getVariableByName(adapter.getName());
                if (variable == null) {
                    continue;
                }

                SimpleFeatureType schema = reader.getIndexSchema(coverageName, (VariableDS) variable);
                if (schema == null) {
                    continue;
                }

                ReferencedEnvelope bounds = adapter.getSpatialDomain().getReferencedEnvelope();

                NetCDFSliceProvider selector = new NetCDFSliceProvider(
                        adapter,
                        schema,
                        variableImageStartIndex,
                        bounds,
                        new NetCDFSliceProvider.DimensionIndexesContext(adapter));

                Name typeName = namespaceURI != null
                        ? new NameImpl(namespaceURI, schema.getTypeName())
                        : new NameImpl(schema.getTypeName());

                result.add(new AuxiliaryType(typeName, schema, bounds, selector));

                variableImageStartIndex += adapter.getNumberOfSlices();
            } catch (Exception e) {
                throw new IOException("Failed to build auxiliary type for coverage " + coverageName, e);
            }
        }

        return result;
    }

    static final class AuxiliaryType {
        final Name name;
        final SimpleFeatureType schema;
        final ReferencedEnvelope bounds;
        final NetCDFSliceProvider sliceProvider;

        AuxiliaryType(
                Name name, SimpleFeatureType schema, ReferencedEnvelope bounds, NetCDFSliceProvider sliceProvider) {
            this.name = Objects.requireNonNull(name, "name");
            this.schema = Objects.requireNonNull(schema, "schema");
            this.bounds = bounds;
            this.sliceProvider = Objects.requireNonNull(sliceProvider, "sliceProvider");
        }
    }

    static class NetCDFAuxiliaryFeatureSource extends ContentFeatureSource {

        private final NetCDFAuxiliaryDataStore.AuxiliaryType type;

        NetCDFAuxiliaryFeatureSource(ContentEntry entry, NetCDFAuxiliaryDataStore.AuxiliaryType type) {
            super(entry, Query.ALL);
            this.type = type;
        }

        @Override
        protected SimpleFeatureType buildFeatureType() throws IOException {
            return type.schema;
        }

        @Override
        protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
            return type.sliceProvider.bounds(query);
        }

        @Override
        protected int getCountInternal(Query query) throws IOException {
            return type.sliceProvider.count(query);
        }

        @Override
        protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
            return new NetCDFAuxiliaryFeatureReader(type.schema, type.sliceProvider.iterate(query));
        }
    }

    static class NetCDFAuxiliaryFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

        private final SimpleFeatureType featureType;
        private final Iterator<CoverageSlice> delegate;
        private SimpleFeature next;

        NetCDFAuxiliaryFeatureReader(SimpleFeatureType featureType, Iterator<CoverageSlice> delegate) {
            this.featureType = featureType;
            this.delegate = delegate;
        }

        @Override
        public SimpleFeatureType getFeatureType() {
            return featureType;
        }

        @Override
        public boolean hasNext() throws IOException {
            if (next != null) {
                return true;
            }

            while (delegate.hasNext()) {
                CoverageSlice slice = delegate.next();
                if (slice != null && slice.getOriginator() != null) {
                    next = slice.getOriginator();
                    return true;
                }
            }

            return false;
        }

        @Override
        public void close() throws IOException {
            // Do nothing
        }

        @Override
        public SimpleFeature next() throws IOException {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            SimpleFeature result = next;
            next = null;
            return result;
        }
    }
}
