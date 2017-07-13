/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.AbstractTimePrimitiveType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.RelatedTimeType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Time Primitive Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractTimePrimitiveTypeImpl#getRelatedTime <em>Related Time</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractTimePrimitiveTypeImpl extends AbstractTimeObjectTypeImpl implements AbstractTimePrimitiveType {
    /**
     * The cached value of the '{@link #getRelatedTime() <em>Related Time</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRelatedTime()
     * @generated
     * @ordered
     */
    protected EList<RelatedTimeType> relatedTime;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractTimePrimitiveTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractTimePrimitiveType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<RelatedTimeType> getRelatedTime() {
        if (relatedTime == null) {
            relatedTime = new EObjectContainmentEList<RelatedTimeType>(RelatedTimeType.class, this, Gml311Package.ABSTRACT_TIME_PRIMITIVE_TYPE__RELATED_TIME);
        }
        return relatedTime;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_TIME_PRIMITIVE_TYPE__RELATED_TIME:
                return ((InternalEList<?>)getRelatedTime()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_TIME_PRIMITIVE_TYPE__RELATED_TIME:
                return getRelatedTime();
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
            case Gml311Package.ABSTRACT_TIME_PRIMITIVE_TYPE__RELATED_TIME:
                getRelatedTime().clear();
                getRelatedTime().addAll((Collection<? extends RelatedTimeType>)newValue);
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
            case Gml311Package.ABSTRACT_TIME_PRIMITIVE_TYPE__RELATED_TIME:
                getRelatedTime().clear();
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
            case Gml311Package.ABSTRACT_TIME_PRIMITIVE_TYPE__RELATED_TIME:
                return relatedTime != null && !relatedTime.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //AbstractTimePrimitiveTypeImpl
