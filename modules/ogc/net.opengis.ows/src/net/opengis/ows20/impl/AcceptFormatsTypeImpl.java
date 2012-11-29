/**
 */
package net.opengis.ows20.impl;

import java.util.Collection;

import net.opengis.ows20.AcceptFormatsType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Accept Formats Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows20.impl.AcceptFormatsTypeImpl#getOutputFormat <em>Output Format</em>}</li>
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
    protected EList<String> outputFormat;

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
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.ACCEPT_FORMATS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getOutputFormat() {
        if (outputFormat == null) {
            outputFormat = new EDataTypeEList<String>(String.class, this, Ows20Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT);
        }
        return outputFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows20Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Ows20Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
                getOutputFormat().clear();
                getOutputFormat().addAll((Collection<? extends String>)newValue);
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
            case Ows20Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
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
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Ows20Package.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
                return outputFormat != null && !outputFormat.isEmpty();
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
        result.append(" (outputFormat: ");
        result.append(outputFormat);
        result.append(')');
        return result.toString();
    }

} //AcceptFormatsTypeImpl
