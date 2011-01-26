/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import java.util.Collection;

import net.opengis.ows11.AcceptFormatsType;
import net.opengis.ows11.Ows11Package;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Accept Formats Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.AcceptFormatsTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AcceptFormatsTypeImpl extends EObjectImpl implements AcceptFormatsType {
    /**
     * The cached value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected EList outputFormat;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AcceptFormatsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.ACCEPT_FORMATS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getOutputFormat() {
        if (outputFormat == null) {
            outputFormat = new EDataTypeEList(String.class, this, Ows11Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT);
        }
        return outputFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows11Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
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
            case Ows11Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
                getOutputFormat().clear();
                getOutputFormat().addAll((Collection)newValue);
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
            case Ows11Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
                getOutputFormat().clear();
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
            case Ows11Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
                return outputFormat != null && !outputFormat.isEmpty();
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
        result.append(" (outputFormat: ");
        result.append(outputFormat);
        result.append(')');
        return result.toString();
    }

} //AcceptFormatsTypeImpl
