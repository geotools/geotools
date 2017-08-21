/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.LabelStyleType;
import net.opengis.gml311.LabelType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Label Style Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.LabelStyleTypeImpl#getStyle <em>Style</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.LabelStyleTypeImpl#getLabel <em>Label</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LabelStyleTypeImpl extends BaseStyleDescriptorTypeImpl implements LabelStyleType {
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
     * The cached value of the '{@link #getLabel() <em>Label</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected LabelType label;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LabelStyleTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getLabelStyleType();
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
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.LABEL_STYLE_TYPE__STYLE, oldStyle, style));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LabelType getLabel() {
        return label;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLabel(LabelType newLabel, NotificationChain msgs) {
        LabelType oldLabel = label;
        label = newLabel;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.LABEL_STYLE_TYPE__LABEL, oldLabel, newLabel);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLabel(LabelType newLabel) {
        if (newLabel != label) {
            NotificationChain msgs = null;
            if (label != null)
                msgs = ((InternalEObject)label).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.LABEL_STYLE_TYPE__LABEL, null, msgs);
            if (newLabel != null)
                msgs = ((InternalEObject)newLabel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.LABEL_STYLE_TYPE__LABEL, null, msgs);
            msgs = basicSetLabel(newLabel, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.LABEL_STYLE_TYPE__LABEL, newLabel, newLabel));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.LABEL_STYLE_TYPE__LABEL:
                return basicSetLabel(null, msgs);
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
            case Gml311Package.LABEL_STYLE_TYPE__STYLE:
                return getStyle();
            case Gml311Package.LABEL_STYLE_TYPE__LABEL:
                return getLabel();
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
            case Gml311Package.LABEL_STYLE_TYPE__STYLE:
                setStyle((String)newValue);
                return;
            case Gml311Package.LABEL_STYLE_TYPE__LABEL:
                setLabel((LabelType)newValue);
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
            case Gml311Package.LABEL_STYLE_TYPE__STYLE:
                setStyle(STYLE_EDEFAULT);
                return;
            case Gml311Package.LABEL_STYLE_TYPE__LABEL:
                setLabel((LabelType)null);
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
            case Gml311Package.LABEL_STYLE_TYPE__STYLE:
                return STYLE_EDEFAULT == null ? style != null : !STYLE_EDEFAULT.equals(style);
            case Gml311Package.LABEL_STYLE_TYPE__LABEL:
                return label != null;
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
        result.append(')');
        return result.toString();
    }

} //LabelStyleTypeImpl
