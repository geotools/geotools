/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature;

import com.vividsolutions.jts.geom.Geometry;

import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;

/**
 * Generic interface for accessing {@link GeometryAttribute feature geometry} data.
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link EFeatureGeometry#isEmpty <em>Empty</em>}</li>
 * <li>{@link EFeatureGeometry#isDefaultGeometry <em>Default</em>}</li>
 * <li>{@link EFeatureGeometry#getStructure <em>Structure</em>}</li>
 * </ul>
 * </p>
 * 
 * @param <V> - Actual {@link Property#getValue() property value} class.
 * 
 * @see EFeatureAttribute
 * @see EFeaturePackage#getEFeatureAttribute()
 * @see EFeaturePackage#getEFeatureGeometry()
 * 
 * @author kengu
 */
public interface EFeatureGeometry<V extends Geometry> extends
        EFeatureProperty<V, GeometryAttribute> {

    /**
     * Check if the {@link Property#getValue() feature geometry value} is {@link Geometry#isEmpty()
     * empty}
     * 
     * @return the value of the '<em>Default</em>' attribute.
     */
    boolean isEmpty();

    /**
     * Check if this is the {@link Feature#getDefaultGeometryProperty() default feature geometry}
     * 
     * @return the value of the '<em>Default</em>' attribute.
     */
    boolean isDefault();

    /**
     * Get the attribute {@link EFeatureGeometryInfo structure} instance.
     * 
     * @return the value of the '<em>Structure</em>' attribute.
     */
    @Override
    public EFeatureGeometryInfo getStructure();

} // EGeometry
