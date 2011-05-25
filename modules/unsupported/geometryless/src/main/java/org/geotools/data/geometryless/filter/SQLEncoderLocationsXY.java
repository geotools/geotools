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
package org.geotools.data.geometryless.filter;

import java.io.IOException;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.UnaliasSQLEncoder;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Encodes a filter into a SQL WHERE statement for generic SQL. This class adds
 * the ability to turn geometry filters into sql statements if they are based on
 * x,y (longitude/latitude) column pairs..
 * 
 * @author Rob Atkinson , SCO
 * @author Debasish Sahu, debasish.sahu@rmsi.com
 * 
 *
 * @source $URL$
 */
public class SQLEncoderLocationsXY extends UnaliasSQLEncoder {
    /** Standard java logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.filter");

    /**
     * The srid of the schema, so the bbox conforms. Could be better to have it
     * in the bbox filter itself, but this works for now.
     */
    private int srid;

    // names of sql addressable columns containing numerical coordinates
    private String xcolumn = null;

    private String ycolumn = null;

    /** The geometry attribute to use if none is specified. */
    private String defaultGeom;

    /**
     * Empty constructor TODO: rethink empty constructor, as BBOXes _need_ an
     * SRID, must make client set it somehow. Maybe detect when encode is
     * called?
     */
    public SQLEncoderLocationsXY(String xcolumn, String ycolumn) {
        capabilities = createFilterCapabilities();
        this.xcolumn = xcolumn;
        this.ycolumn = ycolumn;

        setSqlNameEscape("");
    }

    public SQLEncoderLocationsXY(int srid) {
        this.srid = srid;
    }

    /**
     * @see org.geotools.filter.SQLEncoder#createFilterCapabilities()
     */
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities capabilities = new FilterCapabilities();

        capabilities.addAll(FilterCapabilities.LOGICAL_OPENGIS);
        capabilities.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
        capabilities.addType(PropertyIsNull.class);
        capabilities.addType(PropertyIsBetween.class);
        capabilities.addType(Id.class);
        capabilities.addType(IncludeFilter.class);
        capabilities.addType(ExcludeFilter.class);
        capabilities.addType(PropertyIsLike.class);

        capabilities.addType(BBOX.class);

        return capabilities;
    }

    /**
     * Sets a spatial reference system ESPG number, so that the geometry can be
     * properly encoded for postgis. If geotools starts actually creating
     * geometries with valid srids then this method will no longer be needed.
     * 
     * @param srid
     *            the integer code for the EPSG spatial reference system.
     */
    public void setSRID(int srid) {
        this.srid = srid;
    }

    /**
     * Sets the default geometry, so that filters with null for one of their
     * expressions can assume that the default geometry is intended.
     * 
     * @param name
     *            the name of the default geometry Attribute.
     * 
     * @task REVISIT: pass in a featureType so that geometries can figure out
     *       their own default geometry?
     */
    public void setDefaultGeometry(String name) {
        // Do we really want clients to be using malformed filters?
        // I mean, this is a useful method for unit tests, but shouldn't
        // fully formed filters usually be used? Though I guess adding
        // the option wouldn't hurt. -ch
        this.defaultGeom = name;
    }

    /**
     * Turns a geometry filter into the postgis sql bbox statement.
     * 
     * @param filter
     *            the geometry filter to be encoded.
     * 
     * @throws RuntimeException
     *             for IO exception (need a better error)
     */
    public Object visitBinarySpatialOperator(BinarySpatialOperator filter, Object extraData)
            throws RuntimeException {

        if (filter instanceof BBOX) {
            Expression left = (Expression) filter.getExpression1();
            Expression right = (Expression) filter.getExpression2();

            PropertyName propertyExpr;
            Literal geomLiteralExpr;

            // left and right have to be valid expressions
            try {
                if (left instanceof PropertyName && right instanceof Literal) {
                    propertyExpr = (PropertyName) left;
                    geomLiteralExpr = (Literal) right;
                } else if (right instanceof PropertyName && left instanceof Literal) {
                    propertyExpr = (PropertyName) right;
                    geomLiteralExpr = (Literal) left;
                } else {
                    String err = "LocationsXY currently supports one geometry and one "
                            + "attribute expr.  You gave: " + left + ", " + right;
                    throw new DataSourceException(err);
                }

                visitLiteralGeometry(geomLiteralExpr);

            } catch (java.io.IOException ioe) {
                LOGGER.warning("Unable to export filter" + ioe);
            }
        } else {
            LOGGER.warning("exporting unknown filter type, only bbox supported");
            throw new RuntimeException("Only BBox is currently supported");
        }

        return extraData;
    }

    /**
     * Checks to see if the literal is a geometry, and encodes it if it is, if
     * not just sends to the parent class.
     * 
     * @param expression
     *            the expression to visit and encode.
     * 
     * @throws IOException
     *             for IO exception (need a better error)
     */
    public void visitLiteralGeometry(Literal expression) throws IOException {
        Geometry bbox = (Geometry) expression.getValue();
        Envelope e = bbox.getEnvelopeInternal();
        double x1 = e.getMinX();
        double x2 = e.getMaxX();
        double y1 = e.getMinY();
        double y2 = e.getMaxY();
        out.write("( " + xcolumn + " < " + x2 + " and " + xcolumn + " > " + x1 + " and " + ycolumn
                + " < " + y2 + " and " + ycolumn + " > " + y1 + " )");
    }
}
