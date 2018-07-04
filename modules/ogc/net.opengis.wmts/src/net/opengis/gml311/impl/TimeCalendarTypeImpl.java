/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.TimeCalendarEraPropertyType;
import net.opengis.gml311.TimeCalendarType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Time Calendar Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.TimeCalendarTypeImpl#getReferenceFrame <em>Reference Frame</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TimeCalendarTypeImpl extends AbstractTimeReferenceSystemTypeImpl implements TimeCalendarType {
    /**
     * The cached value of the '{@link #getReferenceFrame() <em>Reference Frame</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReferenceFrame()
     * @generated
     * @ordered
     */
    protected EList<TimeCalendarEraPropertyType> referenceFrame;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TimeCalendarTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getTimeCalendarType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TimeCalendarEraPropertyType> getReferenceFrame() {
        if (referenceFrame == null) {
            referenceFrame = new EObjectContainmentEList<TimeCalendarEraPropertyType>(TimeCalendarEraPropertyType.class, this, Gml311Package.TIME_CALENDAR_TYPE__REFERENCE_FRAME);
        }
        return referenceFrame;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.TIME_CALENDAR_TYPE__REFERENCE_FRAME:
                return ((InternalEList<?>)getReferenceFrame()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.TIME_CALENDAR_TYPE__REFERENCE_FRAME:
                return getReferenceFrame();
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
            case Gml311Package.TIME_CALENDAR_TYPE__REFERENCE_FRAME:
                getReferenceFrame().clear();
                getReferenceFrame().addAll((Collection<? extends TimeCalendarEraPropertyType>)newValue);
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
            case Gml311Package.TIME_CALENDAR_TYPE__REFERENCE_FRAME:
                getReferenceFrame().clear();
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
            case Gml311Package.TIME_CALENDAR_TYPE__REFERENCE_FRAME:
                return referenceFrame != null && !referenceFrame.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //TimeCalendarTypeImpl
