/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import net.opengis.gml311.AbstractGeneralOperationParameterType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract General Operation Parameter Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractGeneralOperationParameterTypeImpl#getMinimumOccurs <em>Minimum Occurs</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractGeneralOperationParameterTypeImpl extends DefinitionTypeImpl implements AbstractGeneralOperationParameterType {
    /**
     * The default value of the '{@link #getMinimumOccurs() <em>Minimum Occurs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinimumOccurs()
     * @generated
     * @ordered
     */
    protected static final BigInteger MINIMUM_OCCURS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMinimumOccurs() <em>Minimum Occurs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinimumOccurs()
     * @generated
     * @ordered
     */
    protected BigInteger minimumOccurs = MINIMUM_OCCURS_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractGeneralOperationParameterTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractGeneralOperationParameterType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getMinimumOccurs() {
        return minimumOccurs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMinimumOccurs(BigInteger newMinimumOccurs) {
        BigInteger oldMinimumOccurs = minimumOccurs;
        minimumOccurs = newMinimumOccurs;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_GENERAL_OPERATION_PARAMETER_TYPE__MINIMUM_OCCURS, oldMinimumOccurs, minimumOccurs));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_GENERAL_OPERATION_PARAMETER_TYPE__MINIMUM_OCCURS:
                return getMinimumOccurs();
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
            case Gml311Package.ABSTRACT_GENERAL_OPERATION_PARAMETER_TYPE__MINIMUM_OCCURS:
                setMinimumOccurs((BigInteger)newValue);
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
            case Gml311Package.ABSTRACT_GENERAL_OPERATION_PARAMETER_TYPE__MINIMUM_OCCURS:
                setMinimumOccurs(MINIMUM_OCCURS_EDEFAULT);
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
            case Gml311Package.ABSTRACT_GENERAL_OPERATION_PARAMETER_TYPE__MINIMUM_OCCURS:
                return MINIMUM_OCCURS_EDEFAULT == null ? minimumOccurs != null : !MINIMUM_OCCURS_EDEFAULT.equals(minimumOccurs);
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
        result.append(" (minimumOccurs: ");
        result.append(minimumOccurs);
        result.append(')');
        return result.toString();
    }

} //AbstractGeneralOperationParameterTypeImpl
