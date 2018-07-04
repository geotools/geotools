/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.RelatedTimeType;
import net.opengis.gml311.RelativePositionType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Related Time Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.RelatedTimeTypeImpl#getRelativePosition <em>Relative Position</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RelatedTimeTypeImpl extends TimePrimitivePropertyTypeImpl implements RelatedTimeType {
    /**
     * The default value of the '{@link #getRelativePosition() <em>Relative Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRelativePosition()
     * @generated
     * @ordered
     */
    protected static final RelativePositionType RELATIVE_POSITION_EDEFAULT = RelativePositionType.BEFORE;

    /**
     * The cached value of the '{@link #getRelativePosition() <em>Relative Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRelativePosition()
     * @generated
     * @ordered
     */
    protected RelativePositionType relativePosition = RELATIVE_POSITION_EDEFAULT;

    /**
     * This is true if the Relative Position attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean relativePositionESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RelatedTimeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getRelatedTimeType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RelativePositionType getRelativePosition() {
        return relativePosition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRelativePosition(RelativePositionType newRelativePosition) {
        RelativePositionType oldRelativePosition = relativePosition;
        relativePosition = newRelativePosition == null ? RELATIVE_POSITION_EDEFAULT : newRelativePosition;
        boolean oldRelativePositionESet = relativePositionESet;
        relativePositionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RELATED_TIME_TYPE__RELATIVE_POSITION, oldRelativePosition, relativePosition, !oldRelativePositionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetRelativePosition() {
        RelativePositionType oldRelativePosition = relativePosition;
        boolean oldRelativePositionESet = relativePositionESet;
        relativePosition = RELATIVE_POSITION_EDEFAULT;
        relativePositionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.RELATED_TIME_TYPE__RELATIVE_POSITION, oldRelativePosition, RELATIVE_POSITION_EDEFAULT, oldRelativePositionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetRelativePosition() {
        return relativePositionESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.RELATED_TIME_TYPE__RELATIVE_POSITION:
                return getRelativePosition();
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
            case Gml311Package.RELATED_TIME_TYPE__RELATIVE_POSITION:
                setRelativePosition((RelativePositionType)newValue);
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
            case Gml311Package.RELATED_TIME_TYPE__RELATIVE_POSITION:
                unsetRelativePosition();
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
            case Gml311Package.RELATED_TIME_TYPE__RELATIVE_POSITION:
                return isSetRelativePosition();
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
        result.append(" (relativePosition: ");
        if (relativePositionESet) result.append(relativePosition); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //RelatedTimeTypeImpl
