/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.solr;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.geometry.jts.JTS;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.TContains;
import org.opengis.temporal.Period;

import com.vividsolutions.jts.densify.Densifier;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Encodes a OGC expression into a SOLR query syntax
 * 
 * @see {@link FilterToSolr}
 */

public class ExpressionToSolr implements ExpressionVisitor {
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ExpressionToSolr.class);
    private static final Envelope WORLD = new Envelope(-180,180,-90,90);
    private static final double SOLR_DISTANCE_TOLERANCE = 180;

    /**
     * Default format used to SOLR to compare date type fields, timezone will set to UTC
     */
    protected SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /*
     * Filter that contains expression to encode
     */
    private Filter filter;

    /*
     * strategy for specific solr type
     */
    private SolrSpatialStrategy spatialStrategy;

    public ExpressionToSolr() {
        dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Construct an expression encoder with parent Filter
     */
    public ExpressionToSolr(Filter filter) {
        this();
        this.filter = filter;
    }

    public void setSpatialStrategy(SolrSpatialStrategy spatialStrategy) {
        this.spatialStrategy = spatialStrategy;
    }

    @Override
    public Object visit(NilExpression expression, Object extraData) {
        throw new UnsupportedOperationException("Nil expression not supported");
    }

    @Override
    public Object visit(Add expression, Object extraData) {
        throw new UnsupportedOperationException("Add expression not supported");
    }

    @Override
    public Object visit(Divide expression, Object extraData) {
        throw new UnsupportedOperationException("Divide expression not supported");
    }

    @Override
    public Object visit(Function expression, Object extraData) {
        throw new UnsupportedOperationException("Function expression not supported");
    }

    @Override
    public Object visit(Multiply expression, Object extraData) {
        throw new UnsupportedOperationException("Multiply expression not supported");
    }

    @Override
    public Object visit(Subtract expression, Object extraData) {
        throw new UnsupportedOperationException("Subtract expression not supported");
    }

    @Override
    public Object visit(Literal expression, Object extraData) {
        StringBuffer temp = new StringBuffer("");
        StringWriter output = FilterToSolr.asStringWriter(extraData);

        Object literal = expression.getValue();
        if (literal instanceof Geometry) {
            if (spatialStrategy == null) {
                throw new IllegalStateException("Attempt to encode geometry literal but spatialStrategy is null");
            }

            Geometry geometry = (Geometry) literal;
            if (!WORLD.contains(geometry.getEnvelopeInternal()) && !WORLD.equals(geometry.getEnvelopeInternal())) {
                if(LOGGER.isLoggable(Level.FINE)){
                    LOGGER.fine("SOLR cannot deal with filters using geometries that span beyond the whole world, clip feature geometry to world");
                }
                geometry = geometry.intersection(JTS.toGeometry(WORLD));
            }
            //Splits segments exceeds the 180 degrees longitude limit to conforms to SOLR WKT manager specification
            //Using JTS Densify, all segments exceeds the 180 degrees length will be densified, not only the one exceeds it in longitude!
            Envelope env = geometry.getEnvelopeInternal();
            if(env.getWidth() > SOLR_DISTANCE_TOLERANCE){
                if(LOGGER.isLoggable(Level.FINE)){
                    LOGGER.fine("Split segment exceeds the 180 degree longitude limit to conform to SOLR WKT manager specification");
                }
                Densifier densifier = new Densifier(geometry);
                densifier.setDistanceTolerance(SOLR_DISTANCE_TOLERANCE);
                if(LOGGER.isLoggable(Level.FINE)){
                    LOGGER.fine("Original geometry: " + geometry.toText());
                }
                geometry = densifier.getResultGeometry();
                if(LOGGER.isLoggable(Level.FINE)){
                    LOGGER.fine("Densified geometry: " + geometry.toText());
                }               
            }

            temp.append(spatialStrategy.encode(geometry));
        } else if (literal instanceof Number) {
            // don't convert to string
            temp.append(literal.toString());
        } else if (literal instanceof Date) {
            temp.append("\"" + dateFormatUTC.format(literal) + "\"");
        } else if (literal instanceof Period) {
            if (filter instanceof After) {
                Period period = (Period) literal;
                temp.append(dateFormatUTC.format(period.getEnding().getPosition().getDate()));
            }
            if (filter instanceof Before || filter instanceof Begins || filter instanceof BegunBy) {
                Period period = (Period) literal;
                temp.append("\""
                        + dateFormatUTC.format(period.getBeginning().getPosition().getDate())
                        + "\"");
            }
            if (filter instanceof Ends || filter instanceof EndedBy) {
                Period period = (Period) literal;
                temp.append("\"" + dateFormatUTC.format(period.getEnding().getPosition().getDate())
                        + "\"");
            }
            if (filter instanceof During || filter instanceof TContains) {
                Period period = (Period) literal;
                temp.append("\""
                        + dateFormatUTC.format(period.getBeginning().getPosition().getDate())
                        + "\"");
                temp.append(" TO ");
                temp.append("\"" + dateFormatUTC.format(period.getEnding().getPosition().getDate())
                        + "\"");
            }
        } else {
            String escaped = FilterToSolr.escapeSpecialCharacters(literal.toString());
            escaped = "\"" + escaped + "\"";
            temp.append(escaped);
        }
        output.append(temp);
        return temp;
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        StringBuffer temp = new StringBuffer("");
        StringWriter output = FilterToSolr.asStringWriter(extraData);
        temp.append(expression.getPropertyName());
        output.append(temp);
        return temp;
    }

}
