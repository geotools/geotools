/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.LabelStylePropertyType;
import net.opengis.gml311.SymbolType;
import net.opengis.gml311.TopologyStyleType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Topology Style Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.TopologyStyleTypeImpl#getSymbol <em>Symbol</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TopologyStyleTypeImpl#getStyle <em>Style</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TopologyStyleTypeImpl#getLabelStyle <em>Label Style</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TopologyStyleTypeImpl#getTopologyProperty <em>Topology Property</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TopologyStyleTypeImpl#getTopologyType <em>Topology Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TopologyStyleTypeImpl extends BaseStyleDescriptorTypeImpl implements TopologyStyleType {
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
     * The default value of the '{@link #getTopologyProperty() <em>Topology Property</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTopologyProperty()
     * @generated
     * @ordered
     */
    protected static final String TOPOLOGY_PROPERTY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTopologyProperty() <em>Topology Property</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTopologyProperty()
     * @generated
     * @ordered
     */
    protected String topologyProperty = TOPOLOGY_PROPERTY_EDEFAULT;

    /**
     * The default value of the '{@link #getTopologyType() <em>Topology Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTopologyType()
     * @generated
     * @ordered
     */
    protected static final String TOPOLOGY_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTopologyType() <em>Topology Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTopologyType()
     * @generated
     * @ordered
     */
    protected String topologyType = TOPOLOGY_TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TopologyStyleTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getTopologyStyleType();
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.TOPOLOGY_STYLE_TYPE__SYMBOL, oldSymbol, newSymbol);
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
                msgs = ((InternalEObject)symbol).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TOPOLOGY_STYLE_TYPE__SYMBOL, null, msgs);
            if (newSymbol != null)
                msgs = ((InternalEObject)newSymbol).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TOPOLOGY_STYLE_TYPE__SYMBOL, null, msgs);
            msgs = basicSetSymbol(newSymbol, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TOPOLOGY_STYLE_TYPE__SYMBOL, newSymbol, newSymbol));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TOPOLOGY_STYLE_TYPE__STYLE, oldStyle, style));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.TOPOLOGY_STYLE_TYPE__LABEL_STYLE, oldLabelStyle, newLabelStyle);
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
                msgs = ((InternalEObject)labelStyle).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TOPOLOGY_STYLE_TYPE__LABEL_STYLE, null, msgs);
            if (newLabelStyle != null)
                msgs = ((InternalEObject)newLabelStyle).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TOPOLOGY_STYLE_TYPE__LABEL_STYLE, null, msgs);
            msgs = basicSetLabelStyle(newLabelStyle, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TOPOLOGY_STYLE_TYPE__LABEL_STYLE, newLabelStyle, newLabelStyle));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTopologyProperty() {
        return topologyProperty;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTopologyProperty(String newTopologyProperty) {
        String oldTopologyProperty = topologyProperty;
        topologyProperty = newTopologyProperty;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TOPOLOGY_STYLE_TYPE__TOPOLOGY_PROPERTY, oldTopologyProperty, topologyProperty));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTopologyType() {
        return topologyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTopologyType(String newTopologyType) {
        String oldTopologyType = topologyType;
        topologyType = newTopologyType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TOPOLOGY_STYLE_TYPE__TOPOLOGY_TYPE, oldTopologyType, topologyType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.TOPOLOGY_STYLE_TYPE__SYMBOL:
                return basicSetSymbol(null, msgs);
            case Gml311Package.TOPOLOGY_STYLE_TYPE__LABEL_STYLE:
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
            case Gml311Package.TOPOLOGY_STYLE_TYPE__SYMBOL:
                return getSymbol();
            case Gml311Package.TOPOLOGY_STYLE_TYPE__STYLE:
                return getStyle();
            case Gml311Package.TOPOLOGY_STYLE_TYPE__LABEL_STYLE:
                return getLabelStyle();
            case Gml311Package.TOPOLOGY_STYLE_TYPE__TOPOLOGY_PROPERTY:
                return getTopologyProperty();
            case Gml311Package.TOPOLOGY_STYLE_TYPE__TOPOLOGY_TYPE:
                return getTopologyType();
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
            case Gml311Package.TOPOLOGY_STYLE_TYPE__SYMBOL:
                setSymbol((SymbolType)newValue);
                return;
            case Gml311Package.TOPOLOGY_STYLE_TYPE__STYLE:
                setStyle((String)newValue);
                return;
            case Gml311Package.TOPOLOGY_STYLE_TYPE__LABEL_STYLE:
                setLabelStyle((LabelStylePropertyType)newValue);
                return;
            case Gml311Package.TOPOLOGY_STYLE_TYPE__TOPOLOGY_PROPERTY:
                setTopologyProperty((String)newValue);
                return;
            case Gml311Package.TOPOLOGY_STYLE_TYPE__TOPOLOGY_TYPE:
                setTopologyType((String)newValue);
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
            case Gml311Package.TOPOLOGY_STYLE_TYPE__SYMBOL:
                setSymbol((SymbolType)null);
                return;
            case Gml311Package.TOPOLOGY_STYLE_TYPE__STYLE:
                setStyle(STYLE_EDEFAULT);
                return;
            case Gml311Package.TOPOLOGY_STYLE_TYPE__LABEL_STYLE:
                setLabelStyle((LabelStylePropertyType)null);
                return;
            case Gml311Package.TOPOLOGY_STYLE_TYPE__TOPOLOGY_PROPERTY:
                setTopologyProperty(TOPOLOGY_PROPERTY_EDEFAULT);
                return;
            case Gml311Package.TOPOLOGY_STYLE_TYPE__TOPOLOGY_TYPE:
                setTopologyType(TOPOLOGY_TYPE_EDEFAULT);
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
            case Gml311Package.TOPOLOGY_STYLE_TYPE__SYMBOL:
                return symbol != null;
            case Gml311Package.TOPOLOGY_STYLE_TYPE__STYLE:
                return STYLE_EDEFAULT == null ? style != null : !STYLE_EDEFAULT.equals(style);
            case Gml311Package.TOPOLOGY_STYLE_TYPE__LABEL_STYLE:
                return labelStyle != null;
            case Gml311Package.TOPOLOGY_STYLE_TYPE__TOPOLOGY_PROPERTY:
                return TOPOLOGY_PROPERTY_EDEFAULT == null ? topologyProperty != null : !TOPOLOGY_PROPERTY_EDEFAULT.equals(topologyProperty);
            case Gml311Package.TOPOLOGY_STYLE_TYPE__TOPOLOGY_TYPE:
                return TOPOLOGY_TYPE_EDEFAULT == null ? topologyType != null : !TOPOLOGY_TYPE_EDEFAULT.equals(topologyType);
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
        result.append(", topologyProperty: ");
        result.append(topologyProperty);
        result.append(", topologyType: ");
        result.append(topologyType);
        result.append(')');
        return result.toString();
    }

} //TopologyStyleTypeImpl
