/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.transform;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.MathTransformProvider;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;

/**
 * The provider for the "<cite>Similarity transformation</cite>" (EPSG 9621).
 * <p>
 * Note that similarity transform is a special case of an Affine transform 2D.
 *
 * @source $URL$
 * @version $Id$
 * @author Oscar Fonts
 */
public class SimilarityTransformProvider extends MathTransformProvider {

    private static final long serialVersionUID = -7413519919588731455L;

    // TODO: TRANSLATION_1 and TRANSLATION_2 should be expressed in "target CRS units", not necessarily SI.METER.
    
    /**
     * "Ordinate 1 of evaluation point in target CRS" EPSG::8621
     */
    public static final ParameterDescriptor<Double> TRANSLATION_1 = createDescriptor(
        new NamedIdentifier[] {
            new NamedIdentifier(Citations.EPSG, "Ordinate 1 of evaluation point in target CRS"),
            new NamedIdentifier(Citations.EPSG, "8621")
        },
        0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, SI.METER); 

    /**
     * "Ordinate 2 of evaluation point in target CRS" EPSG::8622
     */
    public static final ParameterDescriptor<Double> TRANSLATION_2 = createDescriptor(
        new NamedIdentifier[] {
            new NamedIdentifier(Citations.EPSG, "Ordinate 2 of evaluation point in target CRS"),
            new NamedIdentifier(Citations.EPSG, "8622")
        },
        0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, SI.METER);
    
    /**
     * "Scale difference" EPSG::8611
     */
    public static final ParameterDescriptor<Double> SCALE = createDescriptor(
        new NamedIdentifier[] {
            new NamedIdentifier(Citations.EPSG, "Scale difference"),
            new NamedIdentifier(Citations.EPSG, "8611")
        },
        1, /* Double.MIN_NORMAL, but not available in jdk 1.5 */ 0x1.0p-1022 , Double.POSITIVE_INFINITY, Dimensionless.UNIT);
    
    /**
     * "Rotation angle of source coordinate reference system axes" EPSG::8614
     */
    public static final ParameterDescriptor<Double> ROTATION = createDescriptor(
        new NamedIdentifier[] {
            new NamedIdentifier(Citations.EPSG, "Rotation angle of source coordinate reference system axes"),
            new NamedIdentifier(Citations.EPSG, "8614")
        },
        0, 0, 360 * 60 * 60, NonSI.SECOND_ANGLE);

    /**
     * The parameter group for "Similarity transformation" EPSG::9621.
     * 
     * Includes {@link #TRANSLATION_1}, {@link #TRANSLATION_2}, {@link #SCALE}, {@link #ROTATION}.
     */
    static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
        new NamedIdentifier[] {
            new NamedIdentifier(Citations.EPSG, "Similarity transformation"),
            new NamedIdentifier(Citations.EPSG, "9621")
        },
        new ParameterDescriptor[] {
            TRANSLATION_1,
            TRANSLATION_2,
            SCALE,
            ROTATION
        }
    );
    
    /**
     * Creates a two-dimensional similarity transform.
     * 
     * EPSG defines explicitly this transform as 2D.
     */
    public SimilarityTransformProvider() {
        super(2, 2, PARAMETERS);
    }

    /**
     * Constructs an {@link AffineTransform2D} math transform from the specified group of parameter values.
     * 
     * The similarity transform is a particular case of Affine Transform 2D where:
     * 
     * <blockquote><pre>
     * m00 = SCALE * cos(ROTATION)
     * m01 = SCALE * sin(ROTATION)
     * m02 = TRANSLATION_1
     * m10 = -m01
     * m11 = m00
     * m12 = TRANSLATION_2
     * </pre></blockquote>
     *
     * @param  values The group of parameter values {@link #PARAMETERS}.
     * @return an {@link AffineTransform2D}.
     * @throws ParameterNotFoundException if a required parameter was not found.
     */
    protected MathTransform createMathTransform(ParameterValueGroup values)
            throws ParameterNotFoundException {
        
        // The four parameters
        double t1 = doubleValue(TRANSLATION_1, values);
        double t2 = doubleValue(TRANSLATION_2, values);
        double scale = doubleValue(SCALE, values);
        double rotation = doubleValue(ROTATION, values);
        
        // Calculate affine transform coefficients
        double theta = Math.PI * rotation / 648000; // arcsec to rad
        double p1 = scale * Math.cos(theta);
        double p2 = scale * Math.sin(theta);
        
        return new AffineTransform2D(p1, -p2, p2, p1, t1, t2);
    }

}

