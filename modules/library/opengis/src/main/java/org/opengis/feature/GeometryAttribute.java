/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature;

import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.geometry.BoundingBox;

/**
 * An attribute which has a geometric value.
 * <p>
 * The type of the value of the attribute is an arbitrary object and is
 * implementation dependent. Implementations of this interface may wish to type
 * narrow {@link Property#getValue()} to be specific about the type geometry.
 * For instance to return explicitly a JTS geometry.
 * </p>
 * <p>
 * Past a regular attribute, GeometryAttribute provides a method for obtaining
 * the bounds of the underlying geometry, {@link #getBounds()}. The
 * {@link #setBounds(BoundingBox)} method is used to explicitly set the bounds
 * which can be useful in situations where the data source stores the bounds
 * explicitly along with the geometry.
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/feature/GeometryAttribute.java $
 */
public interface GeometryAttribute extends Attribute {

    /**
     * Override and type narrow to GeometryType.
     */
    GeometryType getType();

    /**
     * Override and type narrow to GeometryDescriptor.
     * @return The geometry descriptor, may be null if this is a top-level value
     */
    GeometryDescriptor getDescriptor();

    /**
     * The bounds of the attribute.
     * <p>
     * This value should be derived unless explicitly set via
     * {@link #setBounds(BoundingBox)}.
     * </p>
     * <p>
     * In the case that the underlying geometry is <code>null</code>, this
     * method should return an empty bounds as opposed to returning
     * <code>null</code>.
     * </p>
     *
     * @return The bounds of the underlying geometry, possibly empty.
     */
    BoundingBox getBounds();

    /**
     * Sets the bounds of the geometry.
     * <p>
     * This method should be used when the bounds is pre-computed and there is
     * no need to derive it from scratch. This is mostly only relevant to data
     * sources which store the bounds along with the geometry.
     * </p>
     * <p>
     * Setting the bounds to <code>null</code> is allowed and will force the
     * bounds to be derived manually on the next call to {@link #getBounds()}.
     * </p>
     *
     * @param bounds
     *            The bounds of the attribute.
     */
    void setBounds(BoundingBox bounds);

}
