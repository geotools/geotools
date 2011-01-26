/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import java.util.Collection;

import net.opengis.ows11.AcceptVersionsType;
import net.opengis.ows11.Ows11Package;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Accept Versions Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.AcceptVersionsTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AcceptVersionsTypeImpl extends EObjectImpl implements AcceptVersionsType {
    /**
     * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected EList version;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AcceptVersionsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.ACCEPT_VERSIONS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getVersion() {
        if (version == null) {
            version = new EDataTypeEList(String.class, this, Ows11Package.ACCEPT_VERSIONS_TYPE__VERSION);
        }
        return version;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows11Package.ACCEPT_VERSIONS_TYPE__VERSION:
                return getVersion();
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
            case Ows11Package.ACCEPT_VERSIONS_TYPE__VERSION:
                getVersion().clear();
                getVersion().addAll((Collection)newValue);
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
            case Ows11Package.ACCEPT_VERSIONS_TYPE__VERSION:
                getVersion().clear();
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
            case Ows11Package.ACCEPT_VERSIONS_TYPE__VERSION:
                return version != null && !version.isEmpty();
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
        result.append(')');
        return result.toString();
    }

} //AcceptVersionsTypeImpl
