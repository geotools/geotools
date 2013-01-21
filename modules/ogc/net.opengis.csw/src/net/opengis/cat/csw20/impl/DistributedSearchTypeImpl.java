/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.math.BigInteger;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.DistributedSearchType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Distributed Search Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.DistributedSearchTypeImpl#getHopCount <em>Hop Count</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DistributedSearchTypeImpl extends EObjectImpl implements DistributedSearchType {
    /**
     * The default value of the '{@link #getHopCount() <em>Hop Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHopCount()
     * @generated
     * @ordered
     */
    protected static final Integer HOP_COUNT_EDEFAULT = new Integer(2);

    /**
     * The cached value of the '{@link #getHopCount() <em>Hop Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHopCount()
     * @generated
     * @ordered
     */
    protected Integer hopCount = HOP_COUNT_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DistributedSearchTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.DISTRIBUTED_SEARCH_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Integer getHopCount() {
        return hopCount;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHopCount(Integer newHopCount) {
        Integer oldHopCount = hopCount;
        hopCount = newHopCount;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.DISTRIBUTED_SEARCH_TYPE__HOP_COUNT, oldHopCount, hopCount));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Csw20Package.DISTRIBUTED_SEARCH_TYPE__HOP_COUNT:
                return getHopCount();
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
            case Csw20Package.DISTRIBUTED_SEARCH_TYPE__HOP_COUNT:
                setHopCount((Integer)newValue);
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
            case Csw20Package.DISTRIBUTED_SEARCH_TYPE__HOP_COUNT:
                setHopCount(HOP_COUNT_EDEFAULT);
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
            case Csw20Package.DISTRIBUTED_SEARCH_TYPE__HOP_COUNT:
                return HOP_COUNT_EDEFAULT == null ? hopCount != null : !HOP_COUNT_EDEFAULT.equals(hopCount);
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
        result.append(" (hopCount: ");
        result.append(hopCount);
        result.append(')');
        return result.toString();
    }

} //DistributedSearchTypeImpl
