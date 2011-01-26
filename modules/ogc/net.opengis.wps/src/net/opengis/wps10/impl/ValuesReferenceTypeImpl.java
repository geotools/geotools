/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.wps10.ValuesReferenceType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Values Reference Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.ValuesReferenceTypeImpl#getReference <em>Reference</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ValuesReferenceTypeImpl#getValuesForm <em>Values Form</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ValuesReferenceTypeImpl extends EObjectImpl implements ValuesReferenceType {
    /**
     * The default value of the '{@link #getReference() <em>Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReference()
     * @generated
     * @ordered
     */
    protected static final String REFERENCE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getReference() <em>Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReference()
     * @generated
     * @ordered
     */
    protected String reference = REFERENCE_EDEFAULT;

    /**
     * The default value of the '{@link #getValuesForm() <em>Values Form</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValuesForm()
     * @generated
     * @ordered
     */
    protected static final String VALUES_FORM_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValuesForm() <em>Values Form</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValuesForm()
     * @generated
     * @ordered
     */
    protected String valuesForm = VALUES_FORM_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ValuesReferenceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.VALUES_REFERENCE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getReference() {
        return reference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReference(String newReference) {
        String oldReference = reference;
        reference = newReference;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.VALUES_REFERENCE_TYPE__REFERENCE, oldReference, reference));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getValuesForm() {
        return valuesForm;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValuesForm(String newValuesForm) {
        String oldValuesForm = valuesForm;
        valuesForm = newValuesForm;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.VALUES_REFERENCE_TYPE__VALUES_FORM, oldValuesForm, valuesForm));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wps10Package.VALUES_REFERENCE_TYPE__REFERENCE:
                return getReference();
            case Wps10Package.VALUES_REFERENCE_TYPE__VALUES_FORM:
                return getValuesForm();
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
            case Wps10Package.VALUES_REFERENCE_TYPE__REFERENCE:
                setReference((String)newValue);
                return;
            case Wps10Package.VALUES_REFERENCE_TYPE__VALUES_FORM:
                setValuesForm((String)newValue);
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
            case Wps10Package.VALUES_REFERENCE_TYPE__REFERENCE:
                setReference(REFERENCE_EDEFAULT);
                return;
            case Wps10Package.VALUES_REFERENCE_TYPE__VALUES_FORM:
                setValuesForm(VALUES_FORM_EDEFAULT);
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
            case Wps10Package.VALUES_REFERENCE_TYPE__REFERENCE:
                return REFERENCE_EDEFAULT == null ? reference != null : !REFERENCE_EDEFAULT.equals(reference);
            case Wps10Package.VALUES_REFERENCE_TYPE__VALUES_FORM:
                return VALUES_FORM_EDEFAULT == null ? valuesForm != null : !VALUES_FORM_EDEFAULT.equals(valuesForm);
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
        result.append(" (reference: ");
        result.append(reference);
        result.append(", valuesForm: ");
        result.append(valuesForm);
        result.append(')');
        return result.toString();
    }

} //ValuesReferenceTypeImpl
