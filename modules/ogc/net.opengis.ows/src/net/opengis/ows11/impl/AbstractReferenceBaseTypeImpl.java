/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import net.opengis.ows11.AbstractReferenceBaseType;
import net.opengis.ows11.Ows11Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Reference Base Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.AbstractReferenceBaseTypeImpl#getActuate <em>Actuate</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.AbstractReferenceBaseTypeImpl#getArcrole <em>Arcrole</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.AbstractReferenceBaseTypeImpl#getHref <em>Href</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.AbstractReferenceBaseTypeImpl#getRole <em>Role</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.AbstractReferenceBaseTypeImpl#getShow <em>Show</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.AbstractReferenceBaseTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.AbstractReferenceBaseTypeImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AbstractReferenceBaseTypeImpl extends EObjectImpl implements AbstractReferenceBaseType {
    /**
     * The default value of the '{@link #getActuate() <em>Actuate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getActuate()
     * @generated
     * @ordered
     */
    protected static final Object ACTUATE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getActuate() <em>Actuate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getActuate()
     * @generated
     * @ordered
     */
    protected Object actuate = ACTUATE_EDEFAULT;

    /**
     * The default value of the '{@link #getArcrole() <em>Arcrole</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getArcrole()
     * @generated
     * @ordered
     */
    protected static final Object ARCROLE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getArcrole() <em>Arcrole</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getArcrole()
     * @generated
     * @ordered
     */
    protected Object arcrole = ARCROLE_EDEFAULT;

    /**
     * The default value of the '{@link #getHref() <em>Href</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHref()
     * @generated
     * @ordered
     */
    protected static final Object HREF_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHref() <em>Href</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHref()
     * @generated
     * @ordered
     */
    protected Object href = HREF_EDEFAULT;

    /**
     * The default value of the '{@link #getRole() <em>Role</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRole()
     * @generated
     * @ordered
     */
    protected static final Object ROLE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRole() <em>Role</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRole()
     * @generated
     * @ordered
     */
    protected Object role = ROLE_EDEFAULT;

    /**
     * The default value of the '{@link #getShow() <em>Show</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getShow()
     * @generated
     * @ordered
     */
    protected static final Object SHOW_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getShow() <em>Show</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getShow()
     * @generated
     * @ordered
     */
    protected Object show = SHOW_EDEFAULT;

    /**
     * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitle()
     * @generated
     * @ordered
     */
    protected static final Object TITLE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitle()
     * @generated
     * @ordered
     */
    protected Object title = TITLE_EDEFAULT;

    /**
     * The default value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final String TYPE_EDEFAULT = "simple";

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected String type = TYPE_EDEFAULT;

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
    protected AbstractReferenceBaseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.ABSTRACT_REFERENCE_BASE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getActuate() {
        return actuate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setActuate(Object newActuate) {
        Object oldActuate = actuate;
        actuate = newActuate;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ACTUATE, oldActuate, actuate));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getArcrole() {
        return arcrole;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setArcrole(Object newArcrole) {
        Object oldArcrole = arcrole;
        arcrole = newArcrole;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ARCROLE, oldArcrole, arcrole));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getHref() {
        return href;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHref(Object newHref) {
        Object oldHref = href;
        href = newHref;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__HREF, oldHref, href));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getRole() {
        return role;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRole(Object newRole) {
        Object oldRole = role;
        role = newRole;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ROLE, oldRole, role));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getShow() {
        return show;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setShow(Object newShow) {
        Object oldShow = show;
        show = newShow;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__SHOW, oldShow, show));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getTitle() {
        return title;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTitle(Object newTitle) {
        Object oldTitle = title;
        title = newTitle;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__TITLE, oldTitle, title));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(String newType) {
        String oldType = type;
        type = newType;
        boolean oldTypeESet = typeESet;
        typeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__TYPE, oldType, type, !oldTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetType() {
        String oldType = type;
        boolean oldTypeESet = typeESet;
        type = TYPE_EDEFAULT;
        typeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__TYPE, oldType, TYPE_EDEFAULT, oldTypeESet));
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
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ACTUATE:
                return getActuate();
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ARCROLE:
                return getArcrole();
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__HREF:
                return getHref();
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ROLE:
                return getRole();
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__SHOW:
                return getShow();
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__TITLE:
                return getTitle();
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__TYPE:
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
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ACTUATE:
                setActuate(newValue);
                return;
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ARCROLE:
                setArcrole(newValue);
                return;
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__HREF:
                setHref(newValue);
                return;
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ROLE:
                setRole(newValue);
                return;
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__SHOW:
                setShow(newValue);
                return;
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__TITLE:
                setTitle(newValue);
                return;
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__TYPE:
                setType((String)newValue);
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
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ACTUATE:
                setActuate(ACTUATE_EDEFAULT);
                return;
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ARCROLE:
                setArcrole(ARCROLE_EDEFAULT);
                return;
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__HREF:
                setHref(HREF_EDEFAULT);
                return;
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ROLE:
                setRole(ROLE_EDEFAULT);
                return;
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__SHOW:
                setShow(SHOW_EDEFAULT);
                return;
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__TITLE:
                setTitle(TITLE_EDEFAULT);
                return;
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__TYPE:
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
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ACTUATE:
                return ACTUATE_EDEFAULT == null ? actuate != null : !ACTUATE_EDEFAULT.equals(actuate);
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ARCROLE:
                return ARCROLE_EDEFAULT == null ? arcrole != null : !ARCROLE_EDEFAULT.equals(arcrole);
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__HREF:
                return HREF_EDEFAULT == null ? href != null : !HREF_EDEFAULT.equals(href);
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__ROLE:
                return ROLE_EDEFAULT == null ? role != null : !ROLE_EDEFAULT.equals(role);
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__SHOW:
                return SHOW_EDEFAULT == null ? show != null : !SHOW_EDEFAULT.equals(show);
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__TITLE:
                return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE__TYPE:
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
        result.append(" (actuate: ");
        result.append(actuate);
        result.append(", arcrole: ");
        result.append(arcrole);
        result.append(", href: ");
        result.append(href);
        result.append(", role: ");
        result.append(role);
        result.append(", show: ");
        result.append(show);
        result.append(", title: ");
        result.append(title);
        result.append(", type: ");
        if (typeESet) result.append(type); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //AbstractReferenceBaseTypeImpl
