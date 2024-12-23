/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal;

import static org.geotools.data.wfs.internal.WFSOperationType.TRANSACTION;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.api.feature.GeometryAttribute;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.NameImpl;
import org.geotools.http.HTTPResponse;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;

public class TransactionRequest extends WFSRequest {

    private Set<QName> typeNames;

    private List<TransactionElement> txElements;

    public TransactionRequest(WFSConfig config, WFSStrategy strategy) {
        super(TRANSACTION, config, strategy);
        txElements = new ArrayList<>();
        typeNames = new HashSet<>();
    }

    public void add(TransactionElement txElem) {
        synchronized (txElements) {
            txElements.add(txElem);
            typeNames.add(txElem.getTypeName());
        }
    }

    public Set<QName> getTypeNames() {
        return Collections.unmodifiableSet(new HashSet<>(typeNames));
    }

    public List<TransactionElement> getTransactionElements() {
        synchronized (txElements) {
            return new ArrayList<>(txElements);
        }
    }

    public Insert createInsert(QName typeName) {
        return new Insert(typeName);
    }

    public Update createUpdate(QName typeName, List<QName> propertyNames, List<Object> newValues, Filter updateFilter) {
        return new Update(typeName, propertyNames, newValues, updateFilter);
    }

    public Delete createDelete(QName typeName, Filter deleteFilter) {
        return new Delete(typeName, deleteFilter);
    }

    @Override
    public WFSResponse createResponse(HTTPResponse response) throws IOException {
        return super.createResponse(response);
    }

    /** @author groldan */
    public abstract static class TransactionElement {

        private QName typeName;

        TransactionElement(QName typeName) {
            this.typeName = typeName;
        }

        public QName getTypeName() {
            return typeName;
        }
    }

    public class Insert extends TransactionElement {
        private boolean isUseExisting = false;

        private List<SimpleFeature> added;

        Insert(QName typeName) {
            super(typeName);
            added = new LinkedList<>();
        }

        public void add(final SimpleFeature feature) {
            Name name = feature.getFeatureType().getName();
            QName typeName = getTypeName();

            if (!new NameImpl(typeName).equals(name)) {
                throw new IllegalArgumentException(
                        "Type name does not match. Expected " + new NameImpl(typeName) + ", but got " + name);
            }

            WFSStrategy strategy = getStrategy();
            FeatureTypeInfo typeInfo = strategy.getFeatureTypeInfo(typeName);
            CoordinateReferenceSystem crs = typeInfo.getCRS();
            for (Property property : feature.getProperties()) {
                if (!(property instanceof GeometryAttribute)) {
                    continue;
                }
                CoordinateReferenceSystem attCrs = ((GeometryType) property.getType()).getCoordinateReferenceSystem();
                if (!CRS.equalsIgnoreMetadata(crs, attCrs)) {
                    throw new IllegalArgumentException("Added Features shall match the native CRS: "
                            + typeInfo.getDefaultSRS()
                            + ". Got "
                            + attCrs);
                }
            }

            isUseExisting = Boolean.TRUE.equals(feature.getUserData().get(Hints.USE_PROVIDED_FID));

            added.add(feature);
        }

        public List<SimpleFeature> getFeatures() {
            return Collections.unmodifiableList(new ArrayList<>(added));
        }

        public boolean isUseExisting() {
            return isUseExisting;
        }
    }

    public static class Update extends TransactionElement {

        private final List<QName> propertyNames;

        private final List<Object> newValues;

        private final Filter filter;

        Update(QName typeName, List<QName> propertyNames, List<Object> newValues, Filter updateFilter) {
            super(typeName);
            this.propertyNames = Collections.unmodifiableList(new ArrayList<>(propertyNames));
            this.newValues = Collections.unmodifiableList(new ArrayList<>(newValues));
            this.filter = updateFilter;
        }

        public List<QName> getPropertyNames() {
            return propertyNames;
        }

        public List<Object> getNewValues() {
            return newValues;
        }

        public Filter getFilter() {
            return filter;
        }
    }

    public static class Delete extends TransactionElement {

        private final Filter filter;

        Delete(QName typeName, Filter deleteFilter) {
            super(typeName);
            this.filter = deleteFilter;
        }

        public Filter getFilter() {
            return filter;
        }
    }
}
