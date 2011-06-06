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
import org.geotools.data.efeature.EFeatureAttribute;
import org.geotools.data.efeature.EFeatureAttributeInfo;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureStatus;
import org.opengis.feature.Attribute;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.Identifier;

/**
 * An abstract implementation of the model data type {@link EFeatureAttribute}.
 * 
 * @author kengu, 22. apr. 2011
 */
public class EFeatureAttributeDelegate<V> extends
        EFeaturePropertyDelegate<V, Attribute, EAttribute> implements EFeatureAttribute<V> {

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------


    /**
     * Default constructor.
     * <p>
     * 
     * @param eContainer - {@link EObject} instance owning the {@link #getStructuralFeature()
     *        feature attribute}.
     * @param eAttribute - {@link EAttribute} containing the feature {@link #getData() property
     *        data}.
     * @param valueType - {@link #getValue() property value} type.
     * @param eStructure - {@link EFeatureAttributeInfo} defining the {@link #getStructure()
     *        structure} of this feature attribute.
     */
    public EFeatureAttributeDelegate(EObject eContainer, EAttribute eAttribute, Class<V> valueType,
            EFeatureAttributeInfo eStructure) {
        super(eContainer, eAttribute, Attribute.class, valueType, eStructure);
    }

    // ----------------------------------------------------- 
    //  EFeaturePropertyDelegate implementation
    // -----------------------------------------------------

    @Override
    public EFeatureAttributeInfo getStructure() {
        return (EFeatureAttributeInfo) eStructure;
    }

    @Override
    protected Attribute create() {
        return new AttributeDelegate();
    }

    @Override
    protected V validate(Attribute data) {
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
    //  Attribute delegate implementation
    // -----------------------------------------------------

    private class AttributeDelegate implements Attribute {

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

        public AttributeType getType() {
            return getDescriptor().getType();
        }

        public AttributeDescriptor getDescriptor() {
            return getStructure().getDescriptor();
        }

        public Object getValue() {
            return EFeatureAttributeDelegate.this.getValue();
        }

        public void setValue(Object newValue) {
            // Cast to value type
            //
            V value = valueType.cast(newValue);
            //
            // Update delegate
            //
            EFeatureAttributeDelegate.this.setValue(value);
        }

        public Map<Object, Object> getUserData() {
            if (userData == null) {
                userData = new HashMap<Object, Object>();
            }
            return userData;
        }

        public void validate() throws IllegalArgumentException {
            EFeatureAttributeDelegate.this.validate(getValue());
        }

    }

    // ----------------------------------------------------- 
    //  Static helper methods
    // -----------------------------------------------------

    /**
     * Create new {@link EFeatureAttribute} instance
     * 
     * @param <V> - generic value type
     * @param eContainer - value container
     * @param eName - EAttribute name
     * @param type - value class
     * @param eInfo - {@link EFeatureInfo} instance
     * @return a {@link EFeatureAttribute} instance
     */
    public static <V> EFeatureAttribute<V> create(EObject eContainer, String eName, Class<V> type,
            EFeatureInfo eInfo) {
        // Get EFeatureAttribute structure
        //
        EFeatureAttributeInfo eAttributeInfo = eInfo.eGetAttributeInfo(eName, false);
        //
        // Found attribute?
        //
        if (eAttributeInfo != null) {
            // Get EAttribute delegate
            //
            EAttribute eAttribute = eAttributeInfo.eAttribute();
            Class<?> actualType = eAttribute.getEAttributeType().getInstanceClass();
            if (type.isAssignableFrom(actualType)) {
                // Create delegate
                //
                return new EFeatureAttributeDelegate<V>(eContainer, eAttribute, type,
                        eAttributeInfo);
            }
        }

        // Not found, return null;
        //
        return null;
    }

} // EFeatureAttributeDelegate
