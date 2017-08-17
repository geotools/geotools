/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.GeometryStyleType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.LabelStylePropertyType;
import net.opengis.gml311.SymbolType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Geometry Style Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.GeometryStyleTypeImpl#getSymbol <em>Symbol</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GeometryStyleTypeImpl#getStyle <em>Style</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GeometryStyleTypeImpl#getLabelStyle <em>Label Style</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GeometryStyleTypeImpl#getGeometryProperty <em>Geometry Property</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GeometryStyleTypeImpl#getGeometryType <em>Geometry Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GeometryStyleTypeImpl extends BaseStyleDescriptorTypeImpl implements GeometryStyleType {
    /**
     * The cached value of the '{@link #getSymbol() <em>Symbol</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSymbol()
     * @generated
     * @ordered
     */
    protected SymbolType symbol;

    /**
     * The default value of the '{@link #getStyle() <em>Style</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStyle()
     * @generated
     * @ordered
     */
    protected static final String STYLE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getStyle() <em>Style</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStyle()
     * @generated
     * @ordered
     */
    protected String style = STYLE_EDEFAULT;

    /**
     * The cached value of the '{@link #getLabelStyle() <em>Label Style</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLabelStyle()
     * @generated
     * @ordered
     */
    protected LabelStylePropertyType labelStyle;

    /**
     * The default value of the '{@link #getGeometryProperty() <em>Geometry Property</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGeometryProperty()
     * @generated
     * @ordered
     */
    protected static final String GEOMETRY_PROPERTY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getGeometryProperty() <em>Geometry Property</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGeometryProperty()
     * @generated
     * @ordered
     */
    protected String geometryProperty = GEOMETRY_PROPERTY_EDEFAULT;

    /**
     * The default value of the '{@link #getGeometryType() <em>Geometry Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGeometryType()
     * @generated
     * @ordered
     */
    protected static final String GEOMETRY_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getGeometryType() <em>Geometry Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGeometryType()
     * @generated
     * @ordered
     */
    protected String geometryType = GEOMETRY_TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GeometryStyleTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getGeometryStyleType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SymbolType getSymbol() {
        return symbol;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSymbol(SymbolType newSymbol, NotificationChain msgs) {
        SymbolType oldSymbol = symbol;
        symbol = newSymbol;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.GEOMETRY_STYLE_TYPE__SYMBOL, oldSymbol, newSymbol);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSymbol(SymbolType newSymbol) {
        if (newSymbol != symbol) {
            NotificationChain msgs = null;
            if (symbol != null)
                msgs = ((InternalEObject)symbol).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEOMETRY_STYLE_TYPE__SYMBOL, null, msgs);
            if (newSymbol != null)
                msgs = ((InternalEObject)newSymbol).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEOMETRY_STYLE_TYPE__SYMBOL, null, msgs);
            msgs = basicSetSymbol(newSymbol, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GEOMETRY_STYLE_TYPE__SYMBOL, newSymbol, newSymbol));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getStyle() {
        return style;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStyle(String newStyle) {
        String oldStyle = style;
        style = newStyle;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GEOMETRY_STYLE_TYPE__STYLE, oldStyle, style));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LabelStylePropertyType getLabelStyle() {
        return labelStyle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLabelStyle(LabelStylePropertyType newLabelStyle, NotificationChain msgs) {
        LabelStylePropertyType oldLabelStyle = labelStyle;
        labelStyle = newLabelStyle;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.GEOMETRY_STYLE_TYPE__LABEL_STYLE, oldLabelStyle, newLabelStyle);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLabelStyle(LabelStylePropertyType newLabelStyle) {
        if (newLabelStyle != labelStyle) {
            NotificationChain msgs = null;
            if (labelStyle != null)
                msgs = ((InternalEObject)labelStyle).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEOMETRY_STYLE_TYPE__LABEL_STYLE, null, msgs);
            if (newLabelStyle != null)
                msgs = ((InternalEObject)newLabelStyle).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEOMETRY_STYLE_TYPE__LABEL_STYLE, null, msgs);
            msgs = basicSetLabelStyle(newLabelStyle, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GEOMETRY_STYLE_TYPE__LABEL_STYLE, newLabelStyle, newLabelStyle));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getGeometryProperty() {
        return geometryProperty;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGeometryProperty(String newGeometryProperty) {
        String oldGeometryProperty = geometryProperty;
        geometryProperty = newGeometryProperty;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GEOMETRY_STYLE_TYPE__GEOMETRY_PROPERTY, oldGeometryProperty, geometryProperty));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getGeometryType() {
        return geometryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGeometryType(String newGeometryType) {
        String oldGeometryType = geometryType;
        geometryType = newGeometryType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GEOMETRY_STYLE_TYPE__GEOMETRY_TYPE, oldGeometryType, geometryType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.GEOMETRY_STYLE_TYPE__SYMBOL:
                return basicSetSymbol(null, msgs);
            case Gml311Package.GEOMETRY_STYLE_TYPE__LABEL_STYLE:
                return basicSetLabelStyle(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.GEOMETRY_STYLE_TYPE__SYMBOL:
                return getSymbol();
            case Gml311Package.GEOMETRY_STYLE_TYPE__STYLE:
                return getStyle();
            case Gml311Package.GEOMETRY_STYLE_TYPE__LABEL_STYLE:
                return getLabelStyle();
            case Gml311Package.GEOMETRY_STYLE_TYPE__GEOMETRY_PROPERTY:
                return getGeometryProperty();
            case Gml311Package.GEOMETRY_STYLE_TYPE__GEOMETRY_TYPE:
                return getGeometryType();
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
            case Gml311Package.GEOMETRY_STYLE_TYPE__SYMBOL:
                setSymbol((SymbolType)newValue);
                return;
            case Gml311Package.GEOMETRY_STYLE_TYPE__STYLE:
                setStyle((String)newValue);
                return;
            case Gml311Package.GEOMETRY_STYLE_TYPE__LABEL_STYLE:
                setLabelStyle((LabelStylePropertyType)newValue);
                return;
            case Gml311Package.GEOMETRY_STYLE_TYPE__GEOMETRY_PROPERTY:
                setGeometryProperty((String)newValue);
                return;
            case Gml311Package.GEOMETRY_STYLE_TYPE__GEOMETRY_TYPE:
                setGeometryType((String)newValue);
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
            case Gml311Package.GEOMETRY_STYLE_TYPE__SYMBOL:
                setSymbol((SymbolType)null);
                return;
            case Gml311Package.GEOMETRY_STYLE_TYPE__STYLE:
                setStyle(STYLE_EDEFAULT);
                return;
            case Gml311Package.GEOMETRY_STYLE_TYPE__LABEL_STYLE:
                setLabelStyle((LabelStylePropertyType)null);
                return;
            case Gml311Package.GEOMETRY_STYLE_TYPE__GEOMETRY_PROPERTY:
                setGeometryProperty(GEOMETRY_PROPERTY_EDEFAULT);
                return;
            case Gml311Package.GEOMETRY_STYLE_TYPE__GEOMETRY_TYPE:
                setGeometryType(GEOMETRY_TYPE_EDEFAULT);
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
            case Gml311Package.GEOMETRY_STYLE_TYPE__SYMBOL:
                return symbol != null;
            case Gml311Package.GEOMETRY_STYLE_TYPE__STYLE:
                return STYLE_EDEFAULT == null ? style != null : !STYLE_EDEFAULT.equals(style);
            case Gml311Package.GEOMETRY_STYLE_TYPE__LABEL_STYLE:
                return labelStyle != null;
            case Gml311Package.GEOMETRY_STYLE_TYPE__GEOMETRY_PROPERTY:
                return GEOMETRY_PROPERTY_EDEFAULT == null ? geometryProperty != null : !GEOMETRY_PROPERTY_EDEFAULT.equals(geometryProperty);
            case Gml311Package.GEOMETRY_STYLE_TYPE__GEOMETRY_TYPE:
                return GEOMETRY_TYPE_EDEFAULT == null ? geometryType != null : !GEOMETRY_TYPE_EDEFAULT.equals(geometryType);
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
        result.append(" (style: ");
        result.append(style);
        result.append(", geometryProperty: ");
        result.append(geometryProperty);
        result.append(", geometryType: ");
        result.append(geometryType);
        result.append(')');
        return result.toString();
    }

} //GeometryStyleTypeImpl
