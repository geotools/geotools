/**
 */
package org.w3._2001.smil20.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.w3._2001.smil20.AttributeTypeType;
import org.w3._2001.smil20.SetPrototype;
import org.w3._2001.smil20.Smil20Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Set Prototype</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.smil20.impl.SetPrototypeImpl#getAttributeName <em>Attribute Name</em>}</li>
 *   <li>{@link org.w3._2001.smil20.impl.SetPrototypeImpl#getAttributeType <em>Attribute Type</em>}</li>
 *   <li>{@link org.w3._2001.smil20.impl.SetPrototypeImpl#getTo <em>To</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SetPrototypeImpl extends MinimalEObjectImpl.Container implements SetPrototype {
    /**
     * The default value of the '{@link #getAttributeName() <em>Attribute Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAttributeName()
     * @generated
     * @ordered
     */
    protected static final String ATTRIBUTE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAttributeName() <em>Attribute Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAttributeName()
     * @generated
     * @ordered
     */
    protected String attributeName = ATTRIBUTE_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getAttributeType() <em>Attribute Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAttributeType()
     * @generated
     * @ordered
     */
    protected static final AttributeTypeType ATTRIBUTE_TYPE_EDEFAULT = AttributeTypeType.AUTO;

    /**
     * The cached value of the '{@link #getAttributeType() <em>Attribute Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAttributeType()
     * @generated
     * @ordered
     */
    protected AttributeTypeType attributeType = ATTRIBUTE_TYPE_EDEFAULT;

    /**
     * This is true if the Attribute Type attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean attributeTypeESet;

    /**
     * The default value of the '{@link #getTo() <em>To</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTo()
     * @generated
     * @ordered
     */
    protected static final String TO_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTo() <em>To</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTo()
     * @generated
     * @ordered
     */
    protected String to = TO_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SetPrototypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Smil20Package.Literals.SET_PROTOTYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAttributeName(String newAttributeName) {
        String oldAttributeName = attributeName;
        attributeName = newAttributeName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Smil20Package.SET_PROTOTYPE__ATTRIBUTE_NAME, oldAttributeName, attributeName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AttributeTypeType getAttributeType() {
        return attributeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAttributeType(AttributeTypeType newAttributeType) {
        AttributeTypeType oldAttributeType = attributeType;
        attributeType = newAttributeType == null ? ATTRIBUTE_TYPE_EDEFAULT : newAttributeType;
        boolean oldAttributeTypeESet = attributeTypeESet;
        attributeTypeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Smil20Package.SET_PROTOTYPE__ATTRIBUTE_TYPE, oldAttributeType, attributeType, !oldAttributeTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetAttributeType() {
        AttributeTypeType oldAttributeType = attributeType;
        boolean oldAttributeTypeESet = attributeTypeESet;
        attributeType = ATTRIBUTE_TYPE_EDEFAULT;
        attributeTypeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Smil20Package.SET_PROTOTYPE__ATTRIBUTE_TYPE, oldAttributeType, ATTRIBUTE_TYPE_EDEFAULT, oldAttributeTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetAttributeType() {
        return attributeTypeESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTo() {
        return to;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTo(String newTo) {
        String oldTo = to;
        to = newTo;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Smil20Package.SET_PROTOTYPE__TO, oldTo, to));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Smil20Package.SET_PROTOTYPE__ATTRIBUTE_NAME:
                return getAttributeName();
            case Smil20Package.SET_PROTOTYPE__ATTRIBUTE_TYPE:
                return getAttributeType();
            case Smil20Package.SET_PROTOTYPE__TO:
                return getTo();
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
            case Smil20Package.SET_PROTOTYPE__ATTRIBUTE_NAME:
                setAttributeName((String)newValue);
                return;
            case Smil20Package.SET_PROTOTYPE__ATTRIBUTE_TYPE:
                setAttributeType((AttributeTypeType)newValue);
                return;
            case Smil20Package.SET_PROTOTYPE__TO:
                setTo((String)newValue);
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
            case Smil20Package.SET_PROTOTYPE__ATTRIBUTE_NAME:
                setAttributeName(ATTRIBUTE_NAME_EDEFAULT);
                return;
            case Smil20Package.SET_PROTOTYPE__ATTRIBUTE_TYPE:
                unsetAttributeType();
                return;
            case Smil20Package.SET_PROTOTYPE__TO:
                setTo(TO_EDEFAULT);
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
            case Smil20Package.SET_PROTOTYPE__ATTRIBUTE_NAME:
                return ATTRIBUTE_NAME_EDEFAULT == null ? attributeName != null : !ATTRIBUTE_NAME_EDEFAULT.equals(attributeName);
            case Smil20Package.SET_PROTOTYPE__ATTRIBUTE_TYPE:
                return isSetAttributeType();
            case Smil20Package.SET_PROTOTYPE__TO:
                return TO_EDEFAULT == null ? to != null : !TO_EDEFAULT.equals(to);
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
        result.append(" (attributeName: ");
        result.append(attributeName);
        result.append(", attributeType: ");
        if (attributeTypeESet) result.append(attributeType); else result.append("<unset>");
        result.append(", to: ");
        result.append(to);
        result.append(')');
        return result.toString();
    }

} //SetPrototypeImpl
