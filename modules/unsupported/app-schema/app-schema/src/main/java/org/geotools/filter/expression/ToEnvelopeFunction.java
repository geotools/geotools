/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.Converters;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem; 
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * ToEnvelope function can take in the following set of parameters and return as either Envelope or
 * ReferencedEnvelope type:
 * <ol>
 * <li>Option 1 (1D Envelope) : <OCQL>ToEnvelope(minx,maxx)</OCQL>
 * <li>Option 2 (1D Envelope with crsname): <OCQL>ToEnvelope(minx,maxx,crsname)</OCQL>
 * <li>Option 3 (2D Envelope) : <OCQL>ToEnvelope(minx,maxx,miny,maxy)</OCQL>
 * <li>Option 4 (2D Envelope with crsname): <OCQL>ToEnvelope(minx,maxx,miny,maxy,crsname)</OCQL>
 * </ol>
 * 
 * @author Florence Tan, Curtin University of Technology
 * 
 *
 *
 * @source $URL$
 */
public class ToEnvelopeFunction implements Function {
    private final List<Expression> parameters;

    private final Literal fallback;

    /**
     * Make the instance of FunctionName available in a consistent spot.
     */
    public static final FunctionName NAME = new Name();

    /**
     * Describe how this function works. (should be available via FactoryFinder lookup...)
     */
    public static class Name implements FunctionName {

        public int getArgumentCount() {
            return 2; // minimum 2 required
        }

        public List<String> getArgumentNames() {
            return Arrays.asList(new String[] { "minxvalue", "maxxvalue", "minyvalue", "maxyvalue",
                    "crsvalue" });
        }

        public String getName() {
            return "ToEnvelope";
        }
    };

    public ToEnvelopeFunction() {
        this(new ArrayList<Expression>(), null);
    }

    public ToEnvelopeFunction(List<Expression> parameters, Literal fallback) {
        this.parameters = parameters;
        this.fallback = fallback;
    }

    public String getName() {
        return "ToEnvelope";
    }

    public List<Expression> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    public Object evaluate(Object object) {
        return evaluate(object, Object.class);
    }

    public <T> T evaluate(Object object, Class<T> context) {
        boolean crsexists = false;
        Envelope envelope = null;
        ReferencedEnvelope refenvelope = null;
        CoordinateReferenceSystem sourceCRS = null;
        if (parameters.size() <= 3) {
            // create 1D envelope
            Expression x = parameters.get(0);
            Expression y = parameters.get(1);
            double xvalue = x.evaluate(object, Double.class);
            double yvalue = y.evaluate(object, Double.class);
            if (parameters.size() == 3) {
                // coordinate reference system name provided
                crsexists = true;
                Expression crs = parameters.get(2);
                String crsvalue = crs.evaluate(object, String.class);
                try {
                    sourceCRS = CRS.decode(crsvalue);
                } catch (NoSuchAuthorityCodeException e) {
                    throw new IllegalArgumentException(
                            "Invalid or unsupported SRS name detected for toEnvelope function: "
                                    + crsvalue + ". Cause: " + e.getMessage());
                } catch (FactoryException e) {
                    throw new RuntimeException("Unable to decode SRS name. Cause: "
                            + e.getMessage());
                }
            }
            Coordinate coordinate = new Coordinate(xvalue, yvalue);
            envelope = new Envelope(coordinate);
            if (crsexists) {
                refenvelope = new ReferencedEnvelope(envelope, sourceCRS);
            }
        } else {
            // create 2D envelope
            Expression minx = parameters.get(0);
            Expression maxx = parameters.get(1);
            Expression miny = parameters.get(2);
            Expression maxy = parameters.get(3);
            double minxvalue = minx.evaluate(object, Double.class);
            double minyvalue = miny.evaluate(object, Double.class);
            double maxxvalue = maxx.evaluate(object, Double.class);
            double maxyvalue = maxy.evaluate(object, Double.class);
            if (parameters.size() == 5) {
                // coordinate reference system name provided
                crsexists = true;
                Expression crs = parameters.get(4);
                String crsvalue = crs.evaluate(object, String.class);
                try {
                    sourceCRS = CRS.decode(crsvalue);
                } catch (NoSuchAuthorityCodeException e) {
                    throw new IllegalArgumentException(
                            "Invalid or unsupported SRS name detected for toEnvelope function: "
                                    + crsvalue + ". Cause: " + e.getMessage());
                } catch (FactoryException e) {
                    throw new RuntimeException("Unable to decode SRS name. Cause: "
                            + e.getMessage());
                }
            }
            envelope = new Envelope(minxvalue, maxxvalue, minyvalue, maxyvalue);
            if (crsexists) {
                refenvelope = new ReferencedEnvelope(envelope, sourceCRS);
            }
        }

        if (crsexists) {
            // return new ReferencedEnvelope
            return Converters.convert(refenvelope, context);
        } else {
            // return new Envelope
            return Converters.convert(envelope, context);
        }

    }

    public Literal getFallbackValue() {
        return fallback;
    }

}
