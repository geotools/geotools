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

import org.w3.xlink.LocatorType;
import org.w3.xlink.TypeType;
import org.w3.xlink.XlinkPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Locator Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.w3.xlink.impl.LocatorTypeImpl#getTitleGroup <em>Title Group</em>}</li>
 *   <li>{@link org.w3.xlink.impl.LocatorTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link org.w3.xlink.impl.LocatorTypeImpl#getHref <em>Href</em>}</li>
 *   <li>{@link org.w3.xlink.impl.LocatorTypeImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.w3.xlink.impl.LocatorTypeImpl#getRole <em>Role</em>}</li>
 *   <li>{@link org.w3.xlink.impl.LocatorTypeImpl#getTitle1 <em>Title1</em>}</li>
 *   <li>{@link org.w3.xlink.impl.LocatorTypeImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LocatorTypeImpl extends EObjectImpl implements LocatorType {
    /**
     * The cached value of the '{@link #getTitleGroup() <em>Title Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitleGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap titleGroup;

    /**
     * The default value of the '{@link #getHref() <em>Href</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHref()
     * @generated
     * @ordered
     */
    protected static final String HREF_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHref() <em>Href</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHref()
     * @generated
     * @ordered
     */
    protected String href = HREF_EDEFAULT;

    /**
     * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected static final String LABEL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected String label = LABEL_EDEFAULT;

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
     * The default value of the '{@link #getTitle1() <em>Title1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitle1()
     * @generated
     * @ordered
     */
    protected static final String TITLE1_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTitle1() <em>Title1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitle1()
     * @generated
     * @ordered
     */
    protected String title1 = TITLE1_EDEFAULT;

    /**
     * The default value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final TypeType TYPE_EDEFAULT = TypeType.LOCATOR_LITERAL;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LocatorTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return XlinkPackage.Literals.LOCATOR_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getTitleGroup() {
        if (titleGroup == null) {
            titleGroup = new BasicFeatureMap(this, XlinkPackage.LOCATOR_TYPE__TITLE_GROUP);
        }
        return titleGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getTitle() {
        return getTitleGroup().list(XlinkPackage.Literals.LOCATOR_TYPE__TITLE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getHref() {
        return href;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHref(String newHref) {
        String oldHref = href;
        href = newHref;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.LOCATOR_TYPE__HREF, oldHref, href));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLabel() {
        return label;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLabel(String newLabel) {
        String oldLabel = label;
        label = newLabel;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.LOCATOR_TYPE__LABEL, oldLabel, label));
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
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.LOCATOR_TYPE__ROLE, oldRole, role));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTitle1() {
        return title1;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTitle1(String newTitle1) {
        String oldTitle1 = title1;
        title1 = newTitle1;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.LOCATOR_TYPE__TITLE1, oldTitle1, title1));
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
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.LOCATOR_TYPE__TYPE, oldType, type, !oldTypeESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, XlinkPackage.LOCATOR_TYPE__TYPE, oldType, TYPE_EDEFAULT, oldTypeESet));
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
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case XlinkPackage.LOCATOR_TYPE__TITLE_GROUP:
                return ((InternalEList)getTitleGroup()).basicRemove(otherEnd, msgs);
            case XlinkPackage.LOCATOR_TYPE__TITLE:
                return ((InternalEList)getTitle()).basicRemove(otherEnd, msgs);
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
            case XlinkPackage.LOCATOR_TYPE__TITLE_GROUP:
                if (coreType) return getTitleGroup();
                return ((FeatureMap.Internal)getTitleGroup()).getWrapper();
            case XlinkPackage.LOCATOR_TYPE__TITLE:
                return getTitle();
            case XlinkPackage.LOCATOR_TYPE__HREF:
                return getHref();
            case XlinkPackage.LOCATOR_TYPE__LABEL:
                return getLabel();
            case XlinkPackage.LOCATOR_TYPE__ROLE:
                return getRole();
            case XlinkPackage.LOCATOR_TYPE__TITLE1:
                return getTitle1();
            case XlinkPackage.LOCATOR_TYPE__TYPE:
                return getType();
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
            case XlinkPackage.LOCATOR_TYPE__TITLE_GROUP:
                ((FeatureMap.Internal)getTitleGroup()).set(newValue);
                return;
            case XlinkPackage.LOCATOR_TYPE__HREF:
                setHref((String)newValue);
                return;
            case XlinkPackage.LOCATOR_TYPE__LABEL:
                setLabel((String)newValue);
                return;
            case XlinkPackage.LOCATOR_TYPE__ROLE:
                setRole((String)newValue);
                return;
            case XlinkPackage.LOCATOR_TYPE__TITLE1:
                setTitle1((String)newValue);
                return;
            case XlinkPackage.LOCATOR_TYPE__TYPE:
                setType((TypeType)newValue);
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
            case XlinkPackage.LOCATOR_TYPE__TITLE_GROUP:
                getTitleGroup().clear();
                return;
            case XlinkPackage.LOCATOR_TYPE__HREF:
                setHref(HREF_EDEFAULT);
                return;
            case XlinkPackage.LOCATOR_TYPE__LABEL:
                setLabel(LABEL_EDEFAULT);
                return;
            case XlinkPackage.LOCATOR_TYPE__ROLE:
                setRole(ROLE_EDEFAULT);
                return;
            case XlinkPackage.LOCATOR_TYPE__TITLE1:
                setTitle1(TITLE1_EDEFAULT);
                return;
            case XlinkPackage.LOCATOR_TYPE__TYPE:
                unsetType();
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
            case XlinkPackage.LOCATOR_TYPE__TITLE_GROUP:
                return titleGroup != null && !titleGroup.isEmpty();
            case XlinkPackage.LOCATOR_TYPE__TITLE:
                return !getTitle().isEmpty();
            case XlinkPackage.LOCATOR_TYPE__HREF:
                return HREF_EDEFAULT == null ? href != null : !HREF_EDEFAULT.equals(href);
            case XlinkPackage.LOCATOR_TYPE__LABEL:
                return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
            case XlinkPackage.LOCATOR_TYPE__ROLE:
                return ROLE_EDEFAULT == null ? role != null : !ROLE_EDEFAULT.equals(role);
            case XlinkPackage.LOCATOR_TYPE__TITLE1:
                return TITLE1_EDEFAULT == null ? title1 != null : !TITLE1_EDEFAULT.equals(title1);
            case XlinkPackage.LOCATOR_TYPE__TYPE:
                return isSetType();
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
        result.append(" (titleGroup: ");
        result.append(titleGroup);
        result.append(", href: ");
        result.append(href);
        result.append(", label: ");
        result.append(label);
        result.append(", role: ");
        result.append(role);
        result.append(", title1: ");
        result.append(title1);
        result.append(", type: ");
        if (typeESet) result.append(type); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //LocatorTypeImpl
