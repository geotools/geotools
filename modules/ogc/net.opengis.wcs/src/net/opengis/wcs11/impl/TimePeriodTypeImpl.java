/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import net.opengis.wcs11.TimePeriodType;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Time Period Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.TimePeriodTypeImpl#getBeginPosition <em>Begin Position</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.TimePeriodTypeImpl#getEndPosition <em>End Position</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.TimePeriodTypeImpl#getTimeResolution <em>Time Resolution</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.TimePeriodTypeImpl#getFrame <em>Frame</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TimePeriodTypeImpl extends EObjectImpl implements TimePeriodType {
    /**
     * The default value of the '{@link #getBeginPosition() <em>Begin Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBeginPosition()
     * @generated
     * @ordered
     */
    protected static final Object BEGIN_POSITION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBeginPosition() <em>Begin Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBeginPosition()
     * @generated
     * @ordered
     */
    protected Object beginPosition = BEGIN_POSITION_EDEFAULT;

    /**
     * The default value of the '{@link #getEndPosition() <em>End Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndPosition()
     * @generated
     * @ordered
     */
    protected static final Object END_POSITION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEndPosition() <em>End Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndPosition()
     * @generated
     * @ordered
     */
    protected Object endPosition = END_POSITION_EDEFAULT;

    /**
     * The default value of the '{@link #getTimeResolution() <em>Time Resolution</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTimeResolution()
     * @generated
     * @ordered
     */
    protected static final Object TIME_RESOLUTION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTimeResolution() <em>Time Resolution</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTimeResolution()
     * @generated
     * @ordered
     */
    protected Object timeResolution = TIME_RESOLUTION_EDEFAULT;

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
    protected TimePeriodTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.TIME_PERIOD_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getBeginPosition() {
        return beginPosition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBeginPosition(Object newBeginPosition) {
        Object oldBeginPosition = beginPosition;
        beginPosition = newBeginPosition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.TIME_PERIOD_TYPE__BEGIN_POSITION, oldBeginPosition, beginPosition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getEndPosition() {
        return endPosition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEndPosition(Object newEndPosition) {
        Object oldEndPosition = endPosition;
        endPosition = newEndPosition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.TIME_PERIOD_TYPE__END_POSITION, oldEndPosition, endPosition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getTimeResolution() {
        return timeResolution;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTimeResolution(Object newTimeResolution) {
        Object oldTimeResolution = timeResolution;
        timeResolution = newTimeResolution;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.TIME_PERIOD_TYPE__TIME_RESOLUTION, oldTimeResolution, timeResolution));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.TIME_PERIOD_TYPE__FRAME, oldFrame, frame, !oldFrameESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs111Package.TIME_PERIOD_TYPE__FRAME, oldFrame, FRAME_EDEFAULT, oldFrameESet));
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
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs111Package.TIME_PERIOD_TYPE__BEGIN_POSITION:
                return getBeginPosition();
            case Wcs111Package.TIME_PERIOD_TYPE__END_POSITION:
                return getEndPosition();
            case Wcs111Package.TIME_PERIOD_TYPE__TIME_RESOLUTION:
                return getTimeResolution();
            case Wcs111Package.TIME_PERIOD_TYPE__FRAME:
                return getFrame();
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
            case Wcs111Package.TIME_PERIOD_TYPE__BEGIN_POSITION:
                setBeginPosition(newValue);
                return;
            case Wcs111Package.TIME_PERIOD_TYPE__END_POSITION:
                setEndPosition(newValue);
                return;
            case Wcs111Package.TIME_PERIOD_TYPE__TIME_RESOLUTION:
                setTimeResolution(newValue);
                return;
            case Wcs111Package.TIME_PERIOD_TYPE__FRAME:
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
    public void eUnset(int featureID) {
        switch (featureID) {
            case Wcs111Package.TIME_PERIOD_TYPE__BEGIN_POSITION:
                setBeginPosition(BEGIN_POSITION_EDEFAULT);
                return;
            case Wcs111Package.TIME_PERIOD_TYPE__END_POSITION:
                setEndPosition(END_POSITION_EDEFAULT);
                return;
            case Wcs111Package.TIME_PERIOD_TYPE__TIME_RESOLUTION:
                setTimeResolution(TIME_RESOLUTION_EDEFAULT);
                return;
            case Wcs111Package.TIME_PERIOD_TYPE__FRAME:
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
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Wcs111Package.TIME_PERIOD_TYPE__BEGIN_POSITION:
                return BEGIN_POSITION_EDEFAULT == null ? beginPosition != null : !BEGIN_POSITION_EDEFAULT.equals(beginPosition);
            case Wcs111Package.TIME_PERIOD_TYPE__END_POSITION:
                return END_POSITION_EDEFAULT == null ? endPosition != null : !END_POSITION_EDEFAULT.equals(endPosition);
            case Wcs111Package.TIME_PERIOD_TYPE__TIME_RESOLUTION:
                return TIME_RESOLUTION_EDEFAULT == null ? timeResolution != null : !TIME_RESOLUTION_EDEFAULT.equals(timeResolution);
            case Wcs111Package.TIME_PERIOD_TYPE__FRAME:
                return isSetFrame();
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
        result.append(" (beginPosition: ");
        result.append(beginPosition);
        result.append(", endPosition: ");
        result.append(endPosition);
        result.append(", timeResolution: ");
        result.append(timeResolution);
        result.append(", frame: ");
        if (frameESet) result.append(frame); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //TimePeriodTypeImpl
