/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.geotools.data.efeature.EFeatureAttributeInfo;
import org.geotools.data.efeature.EFeatureGeometry;
import org.geotools.data.efeature.EFeatureGeometryInfo;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureStatus;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.Identifier;
import org.opengis.geometry.BoundingBox;

import com.vividsolutions.jts.geom.Geometry;

/**
 * An abstract implementation of the model data type {@link EFeatureGeometry}.
 * 
 * @author kengu, 22. apr. 2011
 */
public class EFeatureGeometryDelegate<V extends Geometry> extends
        EFeaturePropertyDelegate<V, GeometryAttribute, EAttribute> implements EFeatureGeometry<V> {

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------


    /**
     * Default constructor.
     * <p>
     * 
     * @param eObject - {@link EObject} instance owning the {@link #getValue() data}.
     * @param eStructuralFeature - {@link EStructuralFeature} containing the {@link #getData() data}
     *        .
     * @param valueType - {@link #getValue() value} type.
     * @param eStructure - {@link EFeatureAttributeInfo} defining the {@link #getStructure()
     *        structure} of this feature attribute.
     */
    public EFeatureGeometryDelegate(EObject eObject, EAttribute eStructuralFeature,
            Class<V> valueType, EFeatureGeometryInfo eStructure) {
        super(eObject, eStructuralFeature, GeometryAttribute.class, valueType, eStructure);
    }

    // ----------------------------------------------------- 
    //  EFeatureGeometry implementation
    // -----------------------------------------------------


    public boolean isEmpty() {
        Geometry geom = getValue();
        return (geom == null || geom.isEmpty());
    }

    public boolean isDefault() {
        return getStructure().isDefaultGeometry();
    }

    // ----------------------------------------------------- 
    //  EFeaturePropertyDelegate implementation
    // -----------------------------------------------------


    @Override
    public EFeatureGeometryInfo getStructure() {
        return (EFeatureGeometryInfo) eStructure;
    }

    @Override
    protected GeometryAttribute create() {
        return new GeometryAttributeDelegate();
    }

    @Override
    protected V validate(GeometryAttribute data) {
        EFeatureStatus s;
        if (!(s = getStructure().validate(data)).isSuccess()) {
            throw new IllegalArgumentException(s.getMessage());
        }
        return valueType.cast(data.getValue());
    }

    @Override
    protected V validate(Object value) {
        EFeatureStatus s;
        if (!(s = getStructure().validate(value)).isSuccess()) {
            throw new IllegalArgumentException(s.getMessage(), s.getCause());
        }

        // Is valid, cast to value type
        //
        return valueType.cast(data.getValue());
    }

    // ----------------------------------------------------- 
    //  GeometryAttribute delegate implementation
    // -----------------------------------------------------


    private class GeometryAttributeDelegate implements GeometryAttribute {

        /**
         * bounds, derived
         */
        private BoundingBox bounds;

        /**
         * Cached User data
         */
        private Map<Object, Object> userData;

        public Identifier getIdentifier() {
            return null;
        }

        public Name getName() {
            return getType().getName();
        }

        public boolean isNillable() {
            return getDescriptor().isNillable();
        }

        public GeometryType getType() {
            return getDescriptor().getType();
        }

        public GeometryDescriptor getDescriptor() {
            return getStructure().getDescriptor();
        }

        public Object getValue() {
            return EFeatureGeometryDelegate.this.getValue();
        }

        public void setValue(Object newValue) {
            // Cast to value type
            //
            V value = valueType.cast(newValue);
            //
            // Update delegate
            //
            EFeatureGeometryDelegate.this.setValue(value);
            // Geometry has changed, hence bounds must be recalculated
            //
            bounds = null;
        }

        public BoundingBox getBounds() {
            if (bounds == null) {
                ReferencedEnvelope bbox = new ReferencedEnvelope(getType()
                        .getCoordinateReferenceSystem());
                V geom = EFeatureGeometryDelegate.this.getValue();
                if (geom != null) {
                    bbox.expandToInclude(geom.getEnvelopeInternal());
                } else {
                    bbox.setToNull();
                }
                bounds = bbox;
            }
            return bounds;
        }

        public void setBounds(BoundingBox bounds) {
            this.bounds = bounds;
        }

        public Map<Object, Object> getUserData() {
            if (userData == null) {
                userData = new HashMap<Object, Object>();
            }
            return userData;
        }

        public void validate() throws IllegalArgumentException {
            EFeatureGeometryDelegate.this.validate(getValue());
        }

    }

    // ----------------------------------------------------- 
    //  Static helper methods
    // -----------------------------------------------------


    /**
     * Create new {@link EFeatureGeometry} instance
     * 
     * @param <V> - generic geometry value type
     * @param eContainer - geometry container
     * @param eName - EAttribute name
     * @param type - geometry class
     * @param eInfo - {@link EFeatureInfo} instance
     * @return a {@link EFeatureGeometry} instance
     */
    public static <V extends Geometry> EFeatureGeometry<V> create(EObject eContainer, String eName,
            Class<V> type, EFeatureInfo eInfo) {
        // Get EFeatureGeometry structure
        //
        EFeatureGeometryInfo eGeometryInfo = eInfo.eGetGeometryInfo(eName);
        //
        // Found geometry?
        //
        if (eGeometryInfo != null) {
            // Get EAttribute delegate
            //
            EAttribute eAttribute = eGeometryInfo.eAttribute();

            // Create delegate
            //
            return new EFeatureGeometryDelegate<V>(eContainer, eAttribute, type, eGeometryInfo);
        }

        // Not found, return null;
        //
        return null;

    }

} // EFeatureGeometryDelegate
