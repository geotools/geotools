/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.GetDomainType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Domain Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.GetDomainTypeImpl#getPropertyName <em>Property Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetDomainTypeImpl#getParameterName <em>Parameter Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetDomainTypeImpl extends RequestBaseTypeImpl implements GetDomainType {
    /**
     * The default value of the '{@link #getPropertyName() <em>Property Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPropertyName()
     * @generated
     * @ordered
     */
    protected static final String PROPERTY_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPropertyName() <em>Property Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPropertyName()
     * @generated
     * @ordered
     */
    protected String propertyName = PROPERTY_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getParameterName() <em>Parameter Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameterName()
     * @generated
     * @ordered
     */
    protected static final String PARAMETER_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getParameterName() <em>Parameter Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameterName()
     * @generated
     * @ordered
     */
    protected String parameterName = PARAMETER_NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetDomainTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.GET_DOMAIN_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPropertyName(String newPropertyName) {
        String oldPropertyName = propertyName;
        propertyName = newPropertyName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_DOMAIN_TYPE__PROPERTY_NAME, oldPropertyName, propertyName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setParameterName(String newParameterName) {
        String oldParameterName = parameterName;
        parameterName = newParameterName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_DOMAIN_TYPE__PARAMETER_NAME, oldParameterName, parameterName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Csw20Package.GET_DOMAIN_TYPE__PROPERTY_NAME:
                return getPropertyName();
            case Csw20Package.GET_DOMAIN_TYPE__PARAMETER_NAME:
                return getParameterName();
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
            case Csw20Package.GET_DOMAIN_TYPE__PROPERTY_NAME:
                setPropertyName((String)newValue);
                return;
            case Csw20Package.GET_DOMAIN_TYPE__PARAMETER_NAME:
                setParameterName((String)newValue);
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
            case Csw20Package.GET_DOMAIN_TYPE__PROPERTY_NAME:
                setPropertyName(PROPERTY_NAME_EDEFAULT);
                return;
            case Csw20Package.GET_DOMAIN_TYPE__PARAMETER_NAME:
                setParameterName(PARAMETER_NAME_EDEFAULT);
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
            case Csw20Package.GET_DOMAIN_TYPE__PROPERTY_NAME:
                return PROPERTY_NAME_EDEFAULT == null ? propertyName != null : !PROPERTY_NAME_EDEFAULT.equals(propertyName);
            case Csw20Package.GET_DOMAIN_TYPE__PARAMETER_NAME:
                return PARAMETER_NAME_EDEFAULT == null ? parameterName != null : !PARAMETER_NAME_EDEFAULT.equals(parameterName);
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
        result.append(" (propertyName: ");
        result.append(propertyName);
        result.append(", parameterName: ");
        result.append(parameterName);
        result.append(')');
        return result.toString();
    }

} //GetDomainTypeImpl
