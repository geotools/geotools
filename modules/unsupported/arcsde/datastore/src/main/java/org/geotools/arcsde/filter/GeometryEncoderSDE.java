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
 *
 */
package org.geotools.arcsde.filter;

import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeExtent;
import com.esri.sde.sdk.client.SeFilter;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeShape;
import com.esri.sde.sdk.client.SeShapeFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.geotools.arcsde.data.ArcSDEGeometryBuilder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

/**
 * Encodes the geometry related parts of a filter into a set of <code>SeFilter</code> objects and
 * provides a method to get the resulting filters suitable to set up an SeQuery's spatial
 * constraints.
 *
 * <p>Although not all filters support is coded yet, the strategy to filtering queries for ArcSDE
 * datasources is separated in two parts, the SQL where clause construction, provided by <code>
 * FilterToSQLSDE</code> and the spatial filters (or spatial constraints, in SDE vocabulary)
 * provided here; mirroring the java SDE api approach
 *
 * @author Gabriel Rold?n
 */
@SuppressWarnings("deprecation")
public class GeometryEncoderSDE extends DefaultFilterVisitor implements FilterVisitor {
    /** Standard java logger */
    private static Logger log =
            org.geotools.util.logging.Logging.getLogger(GeometryEncoderSDE.class);

    private static FilterCapabilities capabilities = new FilterCapabilities();

    static {
        capabilities.addType(And.class);
        capabilities.addType(Not.class);
        // capabilities.addType(Or.class);

        capabilities.addType(Id.class);

        capabilities.addType(BBOX.class);
        capabilities.addType(Contains.class);
        capabilities.addType(Crosses.class);
        capabilities.addType(Disjoint.class);
        capabilities.addType(Equals.class);
        capabilities.addType(Intersects.class);
        capabilities.addType(Overlaps.class);
        capabilities.addType(Within.class);
        capabilities.addType(DWithin.class);
        capabilities.addType(Beyond.class);
        capabilities.addType(Touches.class);
    }

    private List<SeShapeFilter> sdeSpatialFilters;

    private SeLayer sdeLayer;

    private SimpleFeatureType featureType;

    /** */
    public GeometryEncoderSDE() {
        // intentionally blank
    }

    /** */
    public GeometryEncoderSDE(SeLayer layer, SimpleFeatureType featureType) {
        this.sdeLayer = layer;
        this.featureType = featureType;
    }

    public static FilterCapabilities getCapabilities() {
        return capabilities;
    }

    public SeFilter[] getSpatialFilters() {
        SeFilter[] filters = new SeFilter[this.sdeSpatialFilters.size()];

        return (SeFilter[]) this.sdeSpatialFilters.toArray(filters);
    }

    private String getLayerName() throws SeException {
        if (this.sdeLayer == null) {
            throw new IllegalStateException("SDE layer has not been set");
        }
        return this.sdeLayer.getQualifiedName();
    }

    /** overrides just to avoid the "WHERE" keyword */
    public void encode(Filter filter) throws GeometryEncoderException {
        this.sdeSpatialFilters = new ArrayList<SeShapeFilter>();
        if (Filter.INCLUDE.equals(filter)) {
            return;
        }
        if (capabilities.fullySupports(filter)) {
            filter.accept(this, null);
        } else {
            throw new GeometryEncoderException(
                    "Filter type " + filter.getClass() + " not supported");
        }
    }

    /**
     * @param truth de default truth value for <code>sdeMethod</code>
     * @param extraData if an instanceof java.lang.Boolean, <code>truth</code> is and'ed with its
     *     boolean value. May have been set by {@link #visit(Not, Object)} to revert the logical
     *     evaluation criteria.
     */
    private void addSpatialFilter(
            final BinarySpatialOperator filter,
            final int sdeMethod,
            final boolean truth,
            final Object extraData) {
        boolean appliedTruth = truth;

        // At the time of writing, extraData can only be null or false.
        // appliedTruth is calculated from following matrix.
        //
        // appliedTruth truth extraData
        // true ........false....false
        // false........true.....false
        // false........false....null
        // true.........true.....null
        if (extraData != null && extraData instanceof Boolean) {
            boolean andValue = ((Boolean) extraData).booleanValue();
            if (andValue) {
                /** TRUE ... should not occur, so fallback to old behaviour. */
                appliedTruth = truth && andValue;
            } else {
                /**
                 * FALSE ... toggle truth Parameter, so for example NOT DISJOINT works properly see
                 * http://jira.codehaus.org/browse/GEOS-3735 for more information.
                 */
                appliedTruth = !truth;
            }
        }
        org.opengis.filter.expression.Expression left, right;
        PropertyName propertyExpr;
        Literal geomLiteralExpr;

        left = filter.getExpression1();
        right = filter.getExpression2();
        if (left instanceof PropertyName && right instanceof Literal) {
            propertyExpr = (PropertyName) left;
            geomLiteralExpr = (Literal) right;
        } else if (right instanceof PropertyName && left instanceof Literal) {
            propertyExpr = (PropertyName) right;
            geomLiteralExpr = (Literal) left;
        } else {
            String err =
                    "SDE currently supports one geometry and one "
                            + "attribute expr.  You gave: "
                            + left
                            + ", "
                            + right;
            throw new IllegalArgumentException(err);
        }

        // Should probably assert that attExpr's property name is equal to
        // spatialCol...

        // HACK: we want to support <namespace>:SHAPE, but current FM doesn't
        // support it. I guess we should try stripping the prefix and seeing if
        // that
        // matches...
        final String spatialCol = featureType.getGeometryDescriptor().getLocalName();
        final String rawPropName = propertyExpr.getPropertyName();
        String localPropName = rawPropName;
        if (rawPropName.indexOf(":") != -1) {
            localPropName = rawPropName.substring(rawPropName.indexOf(":") + 1);
        }
        if ("".equals(localPropName)) {
            log.fine("Empty property name found on filter, using default geometry property");
            localPropName = spatialCol;
        }
        if (!rawPropName.equalsIgnoreCase(spatialCol)
                && !localPropName.equalsIgnoreCase(spatialCol)) {
            throw new IllegalArgumentException(
                    "When querying against a spatial "
                            + "column, your property name must match the spatial"
                            + " column name.You used '"
                            + propertyExpr.getPropertyName()
                            + "', but the DB's spatial column name is '"
                            + spatialCol
                            + "'");
        }
        Geometry geom = (Geometry) geomLiteralExpr.getValue();

        // To prevent errors in ArcSDE, we first trim the user's Filter
        // geometry to the extents of our layer.
        ArcSDEGeometryBuilder gb = ArcSDEGeometryBuilder.builderFor(Polygon.class);
        SeExtent seExtent = this.sdeLayer.getExtent();

        // If a layer just has one point in it (or one very horizontal or
        // vertical line) then we may have
        // a layer extent that's a point or line. We need to correct this.
        if (seExtent.getMaxX() == seExtent.getMinX()) {
            seExtent =
                    new SeExtent(
                            seExtent.getMinX() - 100,
                            seExtent.getMinY(),
                            seExtent.getMaxX() + 100,
                            seExtent.getMaxY());
        }
        if (seExtent.getMaxY() == seExtent.getMinY()) {
            seExtent =
                    new SeExtent(
                            seExtent.getMinX(),
                            seExtent.getMinY() - 100,
                            seExtent.getMaxX(),
                            seExtent.getMaxY() + 100);
        }

        try {
            // Now make an SeShape
            SeShape filterShape;

            if (seExtent.isEmpty() == true) {
                // The extent of the sdeLayer is uninitialised so create an extent.
                // If seExtent.isEmpty() == true, when passed to SeShape.generateRectangle()
                // an exception occurs.
                filterShape = new SeShape(this.sdeLayer.getCoordRef());
            } else {
                SeShape extent = new SeShape(this.sdeLayer.getCoordRef());
                extent.generateRectangle(seExtent);

                // this is a bit hacky, but I don't yet know this code well enough
                // to do it right. Basically if the geometry collection is
                // completely
                // outside of the area of the layer then an intersection will return
                // a geometryCollection (two seperate geometries not intersecting
                // will
                // be a collection of two). Passing this into GeometryBuilder causes
                // an exception. So what I did was just look to see if it is a gc
                // and if so then just make a null seshape, as it shouldn't match
                // any features in arcsde. -ch
                if (geom.getClass() == GeometryCollection.class) {
                    filterShape = new SeShape(this.sdeLayer.getCoordRef());
                } else {
                    gb = ArcSDEGeometryBuilder.builderFor(geom.getClass());
                    filterShape = gb.constructShape(geom, this.sdeLayer.getCoordRef());
                }
            }
            // Add the filter to our list
            SeShapeFilter shapeFilter =
                    new SeShapeFilter(
                            getLayerName(),
                            this.sdeLayer.getSpatialColumn(),
                            filterShape,
                            sdeMethod,
                            appliedTruth);
            this.sdeSpatialFilters.add(shapeFilter);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } catch (SeException se) {
            throw new RuntimeException(se);
        }
    }

    // The Spatial Operator methods (these call to the above visit() method
    @Override
    public Object visit(BBOX filter, Object extraData) {
        addSpatialFilter(filter, SeFilter.METHOD_ENVP, true, extraData);
        return extraData;
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        // SDE can assert only one way, we need to invert from contains to within in case the
        // assertion is the other way around
        if (filter.getExpression1() instanceof PropertyName
                && filter.getExpression2() instanceof Literal) {
            addSpatialFilter(filter, SeFilter.METHOD_PC, true, extraData);
        } else {
            addSpatialFilter(filter, SeFilter.METHOD_SC, true, extraData);
        }
        return extraData;
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        addSpatialFilter(filter, SeFilter.METHOD_LCROSS_OR_CP, true, extraData);
        return extraData;
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        addSpatialFilter(filter, SeFilter.METHOD_II_OR_ET, false, extraData);
        return extraData;
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        // SFS definition of Touches says that two geometries 'a' and 'b' touch each other if the
        // intersection of the interiors of 'a' and 'b' is empty and if the intersection of 'a' and
        // 'b' is nonempty.
        // Hence we use a negated METHOD_II_NO_ET (Interior intersect and no edge touch search
        // method.)

        addSpatialFilter(filter, SeFilter.METHOD_II_NO_ET, false, extraData);
        return extraData;
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        return visitDistanceBufferOperator(filter, true, extraData);
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        return visitDistanceBufferOperator(filter, false, extraData);
    }

    /** Converts a distance buffer op to an intersects againt the buffered input geometry */
    private Object visitDistanceBufferOperator(
            DistanceBufferOperator filter, boolean truth, Object extraData) {
        // SDE can assert only one way, we need to invert from contains to within in case the
        // assertion is the other way around
        PropertyName property;
        Literal literal;
        {
            Expression expression1 = filter.getExpression1();
            Expression expression2 = filter.getExpression2();

            if (expression1 instanceof PropertyName && expression2 instanceof Literal) {
                property = (PropertyName) expression1;
                literal = (Literal) expression2;
            } else if (expression2 instanceof PropertyName && expression1 instanceof Literal) {
                property = (PropertyName) expression2;
                literal = (Literal) expression1;
            } else {
                // not supported
                throw new IllegalArgumentException(
                        "expected propertyname/literal, got " + expression1 + "/" + expression2);
            }
        }

        final Geometry geom = literal.evaluate(null, Geometry.class);
        final double distance = filter.getDistance();
        final Geometry buffer = geom.buffer(distance);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        BinarySpatialOperator intersects = ff.intersects(property, ff.literal(buffer));

        addSpatialFilter(intersects, SeFilter.METHOD_II_OR_ET, truth, extraData);

        return extraData;
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        addSpatialFilter(filter, SeFilter.METHOD_IDENTICAL, true, extraData);
        return extraData;
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        addSpatialFilter(filter, SeFilter.METHOD_II_OR_ET, true, extraData);
        return extraData;
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        addSpatialFilter(filter, SeFilter.METHOD_II, true, extraData);
        // AA: nope, Overlaps definition is The geometries have some but not all points in common,
        // they have the same dimension, and the intersection of the interiors of the two geometries
        // has the same dimension as the geometries themselves.
        // --> that is, one can be contained in the other and they still overlap
        // addSpatialFilter(filter, SeFilter.METHOD_PC, false, extraData);
        // addSpatialFilter(filter, SeFilter.METHOD_SC, false, extraData);
        return extraData;
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        // SDE can assert only one way, we need to invert from contains to within in case the
        // assertion is the other way around
        if (filter.getExpression1() instanceof PropertyName
                && filter.getExpression2() instanceof Literal) {
            addSpatialFilter(filter, SeFilter.METHOD_SC, true, extraData);
        } else {
            addSpatialFilter(filter, SeFilter.METHOD_PC, true, extraData);
        }
        return extraData;
    }

    @Override
    public Object visit(And filter, Object extraData) {
        List<Filter> children = filter.getChildren();
        for (Filter child : children) {
            child.accept(this, extraData);
        }
        return extraData;
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        List<Filter> children = filter.getChildren();
        for (Filter child : children) {
            child.accept(this, extraData);
        }
        return extraData;
    }

    /**
     * Sets <code>extraData</code> to Boolean.FALSE to revert the truth value of the spatial filter
     * contained, if any.
     */
    @Override
    public Object visit(Not filter, Object extraData) {
        Boolean truth = Boolean.FALSE;
        Filter negated = filter.getFilter();
        return negated.accept(this, truth);
    }
}
