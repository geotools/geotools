/**
 */
package org.w3.xlink.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.w3.xlink.Extended;
import org.w3.xlink.TypeType;
import org.w3.xlink.XlinkPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Extended</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.w3.xlink.impl.ExtendedImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link org.w3.xlink.impl.ExtendedImpl#getResource <em>Resource</em>}</li>
 *   <li>{@link org.w3.xlink.impl.ExtendedImpl#getLocator <em>Locator</em>}</li>
 *   <li>{@link org.w3.xlink.impl.ExtendedImpl#getArc <em>Arc</em>}</li>
 *   <li>{@link org.w3.xlink.impl.ExtendedImpl#getRole <em>Role</em>}</li>
 *   <li>{@link org.w3.xlink.impl.ExtendedImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.w3.xlink.impl.ExtendedImpl#getTitleAttribute <em>Title Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExtendedImpl extends EObjectImpl implements Extended {
    /**
     * The default value of the '{@link #getRole() <em>Role</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRole()
     * @generated
     * @ordered
     */
    protected static final String ROLE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRole() <em>Role</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRole()
     * @generated
     * @ordered
     */
    protected String role = ROLE_EDEFAULT;

    /**
     * The default value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final TypeType TYPE_EDEFAULT = TypeType.EXTENDED_LITERAL;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected TypeType type = TYPE_EDEFAULT;

    /**
     * This is true if the Type attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean typeESet;

    /**
     * The default value of the '{@link #getTitleAttribute() <em>Title Attribute</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitleAttribute()
     * @generated
     * @ordered
     */
    protected static final String TITLE_ATTRIBUTE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTitleAttribute() <em>Title Attribute</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitleAttribute()
     * @generated
     * @ordered
     */
    protected String titleAttribute = TITLE_ATTRIBUTE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ExtendedImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return XlinkPackage.Literals.EXTENDED;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getTitle() {
        // TODO: implement this method to return the 'Title' containment reference list
        // Ensure that you remove @generated or mark it @generated NOT
        // The list is expected to implement org.eclipse.emf.ecore.util.InternalEList and org.eclipse.emf.ecore.EStructuralFeature.Setting
        // so it's likely that an appropriate subclass of org.eclipse.emf.ecore.util.EcoreEList should be used.
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getResource() {
        // TODO: implement this method to return the 'Resource' containment reference list
        // Ensure that you remove @generated or mark it @generated NOT
        // The list is expected to implement org.eclipse.emf.ecore.util.InternalEList and org.eclipse.emf.ecore.EStructuralFeature.Setting
        // so it's likely that an appropriate subclass of org.eclipse.emf.ecore.util.EcoreEList should be used.
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getLocator() {
        // TODO: implement this method to return the 'Locator' containment reference list
        // Ensure that you remove @generated or mark it @generated NOT
        // The list is expected to implement org.eclipse.emf.ecore.util.InternalEList and org.eclipse.emf.ecore.EStructuralFeature.Setting
        // so it's likely that an appropriate subclass of org.eclipse.emf.ecore.util.EcoreEList should be used.
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getArc() {
        // TODO: implement this method to return the 'Arc' containment reference list
        // Ensure that you remove @generated or mark it @generated NOT
        // The list is expected to implement org.eclipse.emf.ecore.util.InternalEList and org.eclipse.emf.ecore.EStructuralFeature.Setting
        // so it's likely that an appropriate subclass of org.eclipse.emf.ecore.util.EcoreEList should be used.
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRole() {
        return role;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRole(String newRole) {
        String oldRole = role;
        role = newRole;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.EXTENDED__ROLE, oldRole, role));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TypeType getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(TypeType newType) {
        TypeType oldType = type;
        type = newType == null ? TYPE_EDEFAULT : newType;
        boolean oldTypeESet = typeESet;
        typeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.EXTENDED__TYPE, oldType, type, !oldTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetType() {
        TypeType oldType = type;
        boolean oldTypeESet = typeESet;
        type = TYPE_EDEFAULT;
        typeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, XlinkPackage.EXTENDED__TYPE, oldType, TYPE_EDEFAULT, oldTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetType() {
        return typeESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTitleAttribute() {
        return titleAttribute;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTitleAttribute(String newTitleAttribute) {
        String oldTitleAttribute = titleAttribute;
        titleAttribute = newTitleAttribute;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.EXTENDED__TITLE_ATTRIBUTE, oldTitleAttribute, titleAttribute));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case XlinkPackage.EXTENDED__TITLE:
                return ((InternalEList)getTitle()).basicRemove(otherEnd, msgs);
            case XlinkPackage.EXTENDED__RESOURCE:
                return ((InternalEList)getResource()).basicRemove(otherEnd, msgs);
            case XlinkPackage.EXTENDED__LOCATOR:
                return ((InternalEList)getLocator()).basicRemove(otherEnd, msgs);
            case XlinkPackage.EXTENDED__ARC:
                return ((InternalEList)getArc()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case XlinkPackage.EXTENDED__TITLE:
                return getTitle();
            case XlinkPackage.EXTENDED__RESOURCE:
                return getResource();
            case XlinkPackage.EXTENDED__LOCATOR:
                return getLocator();
            case XlinkPackage.EXTENDED__ARC:
                return getArc();
            case XlinkPackage.EXTENDED__ROLE:
                return getRole();
            case XlinkPackage.EXTENDED__TYPE:
                return getType();
            case XlinkPackage.EXTENDED__TITLE_ATTRIBUTE:
                return getTitleAttribute();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case XlinkPackage.EXTENDED__ROLE:
                setRole((String)newValue);
                return;
            case XlinkPackage.EXTENDED__TYPE:
                setType((TypeType)newValue);
                return;
            case XlinkPackage.EXTENDED__TITLE_ATTRIBUTE:
                setTitleAttribute((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case XlinkPackage.EXTENDED__ROLE:
                setRole(ROLE_EDEFAULT);
                return;
            case XlinkPackage.EXTENDED__TYPE:
                unsetType();
                return;
            case XlinkPackage.EXTENDED__TITLE_ATTRIBUTE:
                setTitleAttribute(TITLE_ATTRIBUTE_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case XlinkPackage.EXTENDED__TITLE:
                return !getTitle().isEmpty();
            case XlinkPackage.EXTENDED__RESOURCE:
                return !getResource().isEmpty();
            case XlinkPackage.EXTENDED__LOCATOR:
                return !getLocator().isEmpty();
            case XlinkPackage.EXTENDED__ARC:
                return !getArc().isEmpty();
            case XlinkPackage.EXTENDED__ROLE:
                return ROLE_EDEFAULT == null ? role != null : !ROLE_EDEFAULT.equals(role);
            case XlinkPackage.EXTENDED__TYPE:
                return isSetType();
            case XlinkPackage.EXTENDED__TITLE_ATTRIBUTE:
                return TITLE_ATTRIBUTE_EDEFAULT == null ? titleAttribute != null : !TITLE_ATTRIBUTE_EDEFAULT.equals(titleAttribute);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (role: ");
        result.append(role);
        result.append(", type: ");
        if (typeESet) result.append(type); else result.append("<unset>");
        result.append(", titleAttribute: ");
        result.append(titleAttribute);
        result.append(')');
        return result.toString();
    }

} //ExtendedImpl
