/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.AbstractTimeGeometricPrimitiveType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Time Geometric Primitive Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractTimeGeometricPrimitiveTypeImpl#getFrame <em>Frame</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractTimeGeometricPrimitiveTypeImpl extends AbstractTimePrimitiveTypeImpl implements AbstractTimeGeometricPrimitiveType {
    /**
     * The default value of the '{@link #getFrame() <em>Frame</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFrame()
     * @generated
     * @ordered
     */
    protected static final String FRAME_EDEFAULT = "#ISO-8601";

    /**
     * The cached value of the '{@link #getFrame() <em>Frame</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFrame()
     * @generated
     * @ordered
     */
    protected String frame = FRAME_EDEFAULT;

    /**
     * This is true if the Frame attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean frameESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractTimeGeometricPrimitiveTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractTimeGeometricPrimitiveType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFrame() {
        return frame;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFrame(String newFrame) {
        String oldFrame = frame;
        frame = newFrame;
        boolean oldFrameESet = frameESet;
        frameESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_TIME_GEOMETRIC_PRIMITIVE_TYPE__FRAME, oldFrame, frame, !oldFrameESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetFrame() {
        String oldFrame = frame;
        boolean oldFrameESet = frameESet;
        frame = FRAME_EDEFAULT;
        frameESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.ABSTRACT_TIME_GEOMETRIC_PRIMITIVE_TYPE__FRAME, oldFrame, FRAME_EDEFAULT, oldFrameESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetFrame() {
        return frameESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_TIME_GEOMETRIC_PRIMITIVE_TYPE__FRAME:
                return getFrame();
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
            case Gml311Package.ABSTRACT_TIME_GEOMETRIC_PRIMITIVE_TYPE__FRAME:
                setFrame((String)newValue);
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
            case Gml311Package.ABSTRACT_TIME_GEOMETRIC_PRIMITIVE_TYPE__FRAME:
                unsetFrame();
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
            case Gml311Package.ABSTRACT_TIME_GEOMETRIC_PRIMITIVE_TYPE__FRAME:
                return isSetFrame();
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
        result.append(" (frame: ");
        if (frameESet) result.append(frame); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //AbstractTimeGeometricPrimitiveTypeImpl
