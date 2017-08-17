/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.StyleVariationType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Style Variation Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.StyleVariationTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.StyleVariationTypeImpl#getFeaturePropertyRange <em>Feature Property Range</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.StyleVariationTypeImpl#getStyleProperty <em>Style Property</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StyleVariationTypeImpl extends MinimalEObjectImpl.Container implements StyleVariationType {
    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final String VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected String value = VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getFeaturePropertyRange() <em>Feature Property Range</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeaturePropertyRange()
     * @generated
     * @ordered
     */
    protected static final String FEATURE_PROPERTY_RANGE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFeaturePropertyRange() <em>Feature Property Range</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeaturePropertyRange()
     * @generated
     * @ordered
     */
    protected String featurePropertyRange = FEATURE_PROPERTY_RANGE_EDEFAULT;

    /**
     * The default value of the '{@link #getStyleProperty() <em>Style Property</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStyleProperty()
     * @generated
     * @ordered
     */
    protected static final String STYLE_PROPERTY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getStyleProperty() <em>Style Property</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStyleProperty()
     * @generated
     * @ordered
     */
    protected String styleProperty = STYLE_PROPERTY_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected StyleVariationTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getStyleVariationType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.STYLE_VARIATION_TYPE__VALUE, oldValue, value));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFeaturePropertyRange() {
        return featurePropertyRange;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFeaturePropertyRange(String newFeaturePropertyRange) {
        String oldFeaturePropertyRange = featurePropertyRange;
        featurePropertyRange = newFeaturePropertyRange;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.STYLE_VARIATION_TYPE__FEATURE_PROPERTY_RANGE, oldFeaturePropertyRange, featurePropertyRange));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getStyleProperty() {
        return styleProperty;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStyleProperty(String newStyleProperty) {
        String oldStyleProperty = styleProperty;
        styleProperty = newStyleProperty;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.STYLE_VARIATION_TYPE__STYLE_PROPERTY, oldStyleProperty, styleProperty));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.STYLE_VARIATION_TYPE__VALUE:
                return getValue();
            case Gml311Package.STYLE_VARIATION_TYPE__FEATURE_PROPERTY_RANGE:
                return getFeaturePropertyRange();
            case Gml311Package.STYLE_VARIATION_TYPE__STYLE_PROPERTY:
                return getStyleProperty();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Gml311Package.STYLE_VARIATION_TYPE__VALUE:
                setValue((String)newValue);
                return;
            case Gml311Package.STYLE_VARIATION_TYPE__FEATURE_PROPERTY_RANGE:
                setFeaturePropertyRange((String)newValue);
                return;
            case Gml311Package.STYLE_VARIATION_TYPE__STYLE_PROPERTY:
                setStyleProperty((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Gml311Package.STYLE_VARIATION_TYPE__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case Gml311Package.STYLE_VARIATION_TYPE__FEATURE_PROPERTY_RANGE:
                setFeaturePropertyRange(FEATURE_PROPERTY_RANGE_EDEFAULT);
                return;
            case Gml311Package.STYLE_VARIATION_TYPE__STYLE_PROPERTY:
                setStyleProperty(STYLE_PROPERTY_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Gml311Package.STYLE_VARIATION_TYPE__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case Gml311Package.STYLE_VARIATION_TYPE__FEATURE_PROPERTY_RANGE:
                return FEATURE_PROPERTY_RANGE_EDEFAULT == null ? featurePropertyRange != null : !FEATURE_PROPERTY_RANGE_EDEFAULT.equals(featurePropertyRange);
            case Gml311Package.STYLE_VARIATION_TYPE__STYLE_PROPERTY:
                return STYLE_PROPERTY_EDEFAULT == null ? styleProperty != null : !STYLE_PROPERTY_EDEFAULT.equals(styleProperty);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (value: ");
        result.append(value);
        result.append(", featurePropertyRange: ");
        result.append(featurePropertyRange);
        result.append(", styleProperty: ");
        result.append(styleProperty);
        result.append(')');
        return result.toString();
    }

} //StyleVariationTypeImpl
