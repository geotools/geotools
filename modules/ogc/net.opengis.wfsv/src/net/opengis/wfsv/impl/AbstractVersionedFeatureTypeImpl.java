/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv.impl;

import javax.xml.datatype.XMLGregorianCalendar;

import net.opengis.wfsv.AbstractVersionedFeatureType;
import net.opengis.wfsv.WfsvPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Versioned Feature Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfsv.impl.AbstractVersionedFeatureTypeImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.AbstractVersionedFeatureTypeImpl#getAuthor <em>Author</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.AbstractVersionedFeatureTypeImpl#getDate <em>Date</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.AbstractVersionedFeatureTypeImpl#getMessage <em>Message</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AbstractVersionedFeatureTypeImpl extends EObjectImpl implements AbstractVersionedFeatureType {
    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final String VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected String version = VERSION_EDEFAULT;

    /**
     * The default value of the '{@link #getAuthor() <em>Author</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAuthor()
     * @generated
     * @ordered
     */
    protected static final String AUTHOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAuthor() <em>Author</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAuthor()
     * @generated
     * @ordered
     */
    protected String author = AUTHOR_EDEFAULT;

    /**
     * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDate()
     * @generated
     * @ordered
     */
    protected static final XMLGregorianCalendar DATE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDate()
     * @generated
     * @ordered
     */
    protected XMLGregorianCalendar date = DATE_EDEFAULT;

    /**
     * The default value of the '{@link #getMessage() <em>Message</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMessage()
     * @generated
     * @ordered
     */
    protected static final String MESSAGE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMessage() <em>Message</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMessage()
     * @generated
     * @ordered
     */
    protected String message = MESSAGE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractVersionedFeatureTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return WfsvPackage.Literals.ABSTRACT_VERSIONED_FEATURE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getVersion() {
        return version;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVersion(String newVersion) {
        String oldVersion = version;
        version = newVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__VERSION, oldVersion, version));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAuthor() {
        return author;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAuthor(String newAuthor) {
        String oldAuthor = author;
        author = newAuthor;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__AUTHOR, oldAuthor, author));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDate(XMLGregorianCalendar newDate) {
        XMLGregorianCalendar oldDate = date;
        date = newDate;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__DATE, oldDate, date));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getMessage() {
        return message;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMessage(String newMessage) {
        String oldMessage = message;
        message = newMessage;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__MESSAGE, oldMessage, message));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__VERSION:
                return getVersion();
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__AUTHOR:
                return getAuthor();
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__DATE:
                return getDate();
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__MESSAGE:
                return getMessage();
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
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__VERSION:
                setVersion((String)newValue);
                return;
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__AUTHOR:
                setAuthor((String)newValue);
                return;
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__DATE:
                setDate((XMLGregorianCalendar)newValue);
                return;
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__MESSAGE:
                setMessage((String)newValue);
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
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__VERSION:
                setVersion(VERSION_EDEFAULT);
                return;
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__AUTHOR:
                setAuthor(AUTHOR_EDEFAULT);
                return;
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__DATE:
                setDate(DATE_EDEFAULT);
                return;
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__MESSAGE:
                setMessage(MESSAGE_EDEFAULT);
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
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__VERSION:
                return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__AUTHOR:
                return AUTHOR_EDEFAULT == null ? author != null : !AUTHOR_EDEFAULT.equals(author);
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__DATE:
                return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE__MESSAGE:
                return MESSAGE_EDEFAULT == null ? message != null : !MESSAGE_EDEFAULT.equals(message);
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
        result.append(" (version: ");
        result.append(version);
        result.append(", author: ");
        result.append(author);
        result.append(", date: ");
        result.append(date);
        result.append(", message: ");
        result.append(message);
        result.append(')');
        return result.toString();
    }

} //AbstractVersionedFeatureTypeImpl
