/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mongodb;

import org.apache.commons.lang3.StringUtils;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.data.mongodb.complex.JsonSelectAllFunction;
import org.geotools.data.mongodb.complex.JsonSelectFunction;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.ClientTransactionAccessor;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

public class MongoFilterSplitter extends PostPreProcessFilterSplittingVisitor {

    private MongoCollectionMeta mongoCollectionMeta;
    /**
     * Create a new instance.
     *
     * @param fcs The FilterCapabilties that describes what Filters/Expressions the server can process.
     * @param parent The FeatureType that this filter involves. Why is this needed?
     * @param transactionAccessor If the transaction is handled on the client and not the server then different filters
     *     must be sent to the server. This class provides a generic way of
     */
    public MongoFilterSplitter(
            FilterCapabilities fcs,
            SimpleFeatureType parent,
            ClientTransactionAccessor transactionAccessor,
            MongoCollectionMeta mongoCollectionMeta) {
        super(fcs, parent, transactionAccessor);
        this.mongoCollectionMeta = mongoCollectionMeta;
    }

    @Override
    protected void visitBinaryComparisonOperator(BinaryComparisonOperator filter) {
        Expression expression1 = filter.getExpression1();
        Expression expression2 = filter.getExpression2();
        if (expression1 instanceof JsonSelectFunction || expression1 instanceof JsonSelectAllFunction) {
            if (expression2 instanceof Literal) {
                preStack.push(filter);
            } else {
                postStack.push(filter);
            }
        } else if (expression2 instanceof JsonSelectFunction || expression2 instanceof JsonSelectAllFunction) {
            if (expression1 instanceof Literal) {
                preStack.push(filter);
            } else {
                postStack.push(filter);
            }
        } else {
            super.visitBinaryComparisonOperator(filter);
        }
    }

    @Override
    protected void visitBinarySpatialOperator(BinarySpatialOperator filter) {
        Expression expression2 = filter.getExpression2();
        if (filter instanceof DWithin) {
            if (expression2 != null) {
                Geometry geometry = expression2.evaluate(null, Geometry.class);
                if (geometry instanceof Point) {
                    processPoint(filter);
                } else {
                    preStack.push(filter);
                }
                return;
            }
        }
        super.visitBinarySpatialOperator(filter);
    }

    /**
     * Checking for presence of spatial index, because DWithin with point will be delegated to $near operator, which
     * requires geospatial index. Otherwise, will delegate to memory.
     *
     * @param filter
     */
    private void processPoint(BinarySpatialOperator filter) {
        if (mongoCollectionMeta != null
                && StringUtils.equalsAny(
                        mongoCollectionMeta
                                .getIndexes()
                                .get(filter.getExpression1().toString()),
                        "2dsphere",
                        "2d")) {
            super.visitBinarySpatialOperator(filter);
        } else {
            postStack.push(filter);
        }
    }

    @Override
    public Object visit(PropertyIsLike filter, Object notUsed) {
        if (original == null) original = filter;

        if (!(filter.getExpression() instanceof PropertyName)) {
            // MongoDB can only encode like expressions using propertyName
            postStack.push(filter);
            return null;
        }

        int i = postStack.size();
        filter.getExpression().accept(this, null);

        if (i < postStack.size()) {
            postStack.pop();
            postStack.push(filter);

            return null;
        }

        preStack.pop(); // value
        preStack.push(filter);
        return null;
    }

    @Override
    public Object visit(PropertyIsNull filter, Object notUsed) {
        preStack.push(filter);
        return null;
    }
}
