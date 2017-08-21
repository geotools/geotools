/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.LineStringPropertyType;
import net.opengis.gml311.MultiLineStringType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multi Line String Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.MultiLineStringTypeImpl#getLineStringMember <em>Line String Member</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MultiLineStringTypeImpl extends AbstractGeometricAggregateTypeImpl implements MultiLineStringType {
    /**
     * The cached value of the '{@link #getLineStringMember() <em>Line String Member</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLineStringMember()
     * @generated
     * @ordered
     */
    protected EList<LineStringPropertyType> lineStringMember;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MultiLineStringTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getMultiLineStringType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<LineStringPropertyType> getLineStringMember() {
        if (lineStringMember == null) {
            lineStringMember = new EObjectContainmentEList<LineStringPropertyType>(LineStringPropertyType.class, this, Gml311Package.MULTI_LINE_STRING_TYPE__LINE_STRING_MEMBER);
        }
        return lineStringMember;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.MULTI_LINE_STRING_TYPE__LINE_STRING_MEMBER:
                return ((InternalEList<?>)getLineStringMember()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.MULTI_LINE_STRING_TYPE__LINE_STRING_MEMBER:
                return getLineStringMember();
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
            case Gml311Package.MULTI_LINE_STRING_TYPE__LINE_STRING_MEMBER:
                getLineStringMember().clear();
                getLineStringMember().addAll((Collection<? extends LineStringPropertyType>)newValue);
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
            case Gml311Package.MULTI_LINE_STRING_TYPE__LINE_STRING_MEMBER:
                getLineStringMember().clear();
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
            case Gml311Package.MULTI_LINE_STRING_TYPE__LINE_STRING_MEMBER:
                return lineStringMember != null && !lineStringMember.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //MultiLineStringTypeImpl
