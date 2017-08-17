/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.LineStringSegmentArrayPropertyType;
import net.opengis.gml311.LineStringSegmentType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Line String Segment Array Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.LineStringSegmentArrayPropertyTypeImpl#getLineStringSegment <em>Line String Segment</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LineStringSegmentArrayPropertyTypeImpl extends MinimalEObjectImpl.Container implements LineStringSegmentArrayPropertyType {
    /**
     * The cached value of the '{@link #getLineStringSegment() <em>Line String Segment</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLineStringSegment()
     * @generated
     * @ordered
     */
    protected EList<LineStringSegmentType> lineStringSegment;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LineStringSegmentArrayPropertyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getLineStringSegmentArrayPropertyType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<LineStringSegmentType> getLineStringSegment() {
        if (lineStringSegment == null) {
            lineStringSegment = new EObjectContainmentEList<LineStringSegmentType>(LineStringSegmentType.class, this, Gml311Package.LINE_STRING_SEGMENT_ARRAY_PROPERTY_TYPE__LINE_STRING_SEGMENT);
        }
        return lineStringSegment;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.LINE_STRING_SEGMENT_ARRAY_PROPERTY_TYPE__LINE_STRING_SEGMENT:
                return ((InternalEList<?>)getLineStringSegment()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.LINE_STRING_SEGMENT_ARRAY_PROPERTY_TYPE__LINE_STRING_SEGMENT:
                return getLineStringSegment();
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
            case Gml311Package.LINE_STRING_SEGMENT_ARRAY_PROPERTY_TYPE__LINE_STRING_SEGMENT:
                getLineStringSegment().clear();
                getLineStringSegment().addAll((Collection<? extends LineStringSegmentType>)newValue);
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
            case Gml311Package.LINE_STRING_SEGMENT_ARRAY_PROPERTY_TYPE__LINE_STRING_SEGMENT:
                getLineStringSegment().clear();
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
            case Gml311Package.LINE_STRING_SEGMENT_ARRAY_PROPERTY_TYPE__LINE_STRING_SEGMENT:
                return lineStringSegment != null && !lineStringSegment.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //LineStringSegmentArrayPropertyTypeImpl
