/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import net.opengis.cat.csw20.ConceptualSchemeType;
import net.opengis.cat.csw20.Csw20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Conceptual Scheme Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.ConceptualSchemeTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.ConceptualSchemeTypeImpl#getDocument <em>Document</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.ConceptualSchemeTypeImpl#getAuthority <em>Authority</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConceptualSchemeTypeImpl extends EObjectImpl implements ConceptualSchemeType {
    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getDocument() <em>Document</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDocument()
     * @generated
     * @ordered
     */
    protected static final String DOCUMENT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDocument() <em>Document</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDocument()
     * @generated
     * @ordered
     */
    protected String document = DOCUMENT_EDEFAULT;

    /**
     * The default value of the '{@link #getAuthority() <em>Authority</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAuthority()
     * @generated
     * @ordered
     */
    protected static final String AUTHORITY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAuthority() <em>Authority</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAuthority()
     * @generated
     * @ordered
     */
    protected String authority = AUTHORITY_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConceptualSchemeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.CONCEPTUAL_SCHEME_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.CONCEPTUAL_SCHEME_TYPE__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDocument() {
        return document;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDocument(String newDocument) {
        String oldDocument = document;
        document = newDocument;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.CONCEPTUAL_SCHEME_TYPE__DOCUMENT, oldDocument, document));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAuthority(String newAuthority) {
        String oldAuthority = authority;
        authority = newAuthority;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.CONCEPTUAL_SCHEME_TYPE__AUTHORITY, oldAuthority, authority));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Csw20Package.CONCEPTUAL_SCHEME_TYPE__NAME:
                return getName();
            case Csw20Package.CONCEPTUAL_SCHEME_TYPE__DOCUMENT:
                return getDocument();
            case Csw20Package.CONCEPTUAL_SCHEME_TYPE__AUTHORITY:
                return getAuthority();
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
            case Csw20Package.CONCEPTUAL_SCHEME_TYPE__NAME:
                setName((String)newValue);
                return;
            case Csw20Package.CONCEPTUAL_SCHEME_TYPE__DOCUMENT:
                setDocument((String)newValue);
                return;
            case Csw20Package.CONCEPTUAL_SCHEME_TYPE__AUTHORITY:
                setAuthority((String)newValue);
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
            case Csw20Package.CONCEPTUAL_SCHEME_TYPE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case Csw20Package.CONCEPTUAL_SCHEME_TYPE__DOCUMENT:
                setDocument(DOCUMENT_EDEFAULT);
                return;
            case Csw20Package.CONCEPTUAL_SCHEME_TYPE__AUTHORITY:
                setAuthority(AUTHORITY_EDEFAULT);
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
            case Csw20Package.CONCEPTUAL_SCHEME_TYPE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case Csw20Package.CONCEPTUAL_SCHEME_TYPE__DOCUMENT:
                return DOCUMENT_EDEFAULT == null ? document != null : !DOCUMENT_EDEFAULT.equals(document);
            case Csw20Package.CONCEPTUAL_SCHEME_TYPE__AUTHORITY:
                return AUTHORITY_EDEFAULT == null ? authority != null : !AUTHORITY_EDEFAULT.equals(authority);
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
        result.append(" (name: ");
        result.append(name);
        result.append(", document: ");
        result.append(document);
        result.append(", authority: ");
        result.append(authority);
        result.append(')');
        return result.toString();
    }

} //ConceptualSchemeTypeImpl
