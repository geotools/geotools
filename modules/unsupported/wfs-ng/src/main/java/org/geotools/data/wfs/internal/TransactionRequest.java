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

import org.geotools.data.ows.HTTPResponse;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.CRS;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class TransactionRequest extends WFSRequest {

    private Set<QName> typeNames;

    private List<TransactionElement> txElements;

    public TransactionRequest(WFSConfig config, WFSStrategy strategy) {
        super(TRANSACTION, config, strategy);
        txElements = new ArrayList<TransactionElement>();
        typeNames = new HashSet<QName>();
    }

    public void add(TransactionElement txElem) {
        synchronized (txElements) {
            txElements.add(txElem);
            typeNames.add(txElem.getTypeName());
        }
    }

    public Set<QName> getTypeNames() {
        return Collections.unmodifiableSet(new HashSet<QName>(typeNames));
    }

    public List<TransactionElement> getTransactionElements() {
        synchronized (txElements) {
            return new ArrayList<TransactionElement>(txElements);
        }
    }

    public Insert createInsert(QName typeName) {
        return new Insert(typeName);
    }

    public Update createUpdate(QName typeName, List<QName> propertyNames, List<Object> newValues,
            Filter updateFilter) {
        return new Update(typeName, propertyNames, newValues, updateFilter);
    }

    public Delete createDelete(QName typeName, Filter deleteFilter) {
        return new Delete(typeName, deleteFilter);
    }

    @Override
    public WFSResponse createResponse(HTTPResponse response) throws IOException {
        return super.createResponse(response);
    }

    /**
     * @author groldan
     * 
     */
    public static abstract class TransactionElement {

        private QName typeName;

        TransactionElement(QName typeName) {
            this.typeName = typeName;
        }

        public QName getTypeName() {
            return typeName;
        }
    }

    public class Insert extends TransactionElement {

        private List<SimpleFeature> added;

        Insert(QName typeName) {
            super(typeName);
            added = new LinkedList<SimpleFeature>();
        }

        public void add(final SimpleFeature feature) {
            Name name = feature.getFeatureType().getName();
            QName typeName = getTypeName();

            if (!new NameImpl(typeName).equals(name)) {
                throw new IllegalArgumentException("Type name does not match. Expected "
                        + new NameImpl(typeName) + ", but got " + name);
            }

            WFSStrategy strategy = getStrategy();
            FeatureTypeInfo typeInfo = strategy.getFeatureTypeInfo(typeName);
            CoordinateReferenceSystem crs = typeInfo.getCRS();
            for (Property property : feature.getProperties()) {
                if (!(property instanceof GeometryAttribute)) {
                    continue;
                }
                CoordinateReferenceSystem attCrs = ((GeometryType) property.getType())
                        .getCoordinateReferenceSystem();
                if (!CRS.equalsIgnoreMetadata(crs, attCrs)) {
                    throw new IllegalArgumentException(
                            "Added Features shall match the native CRS: "
                                    + typeInfo.getDefaultSRS() + ". Got " + attCrs);
                }
            }

            added.add(feature);
        }

        public List<SimpleFeature> getFeatures() {
            return Collections.unmodifiableList(new ArrayList<SimpleFeature>(added));
        }
    }

    public static class Update extends TransactionElement {

        private final List<QName> propertyNames;

        private final List<Object> newValues;

        private final Filter filter;

        Update(QName typeName, List<QName> propertyNames, List<Object> newValues,
                Filter updateFilter) {
            super(typeName);
            this.propertyNames = Collections.unmodifiableList(new ArrayList<QName>(propertyNames));
            this.newValues = Collections.unmodifiableList(new ArrayList<Object>(newValues));
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
