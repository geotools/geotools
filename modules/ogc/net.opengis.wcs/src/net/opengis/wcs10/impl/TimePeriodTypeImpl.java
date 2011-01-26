/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.gml.TimePositionType;

import net.opengis.wcs10.TimePeriodType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Time Period Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.TimePeriodTypeImpl#getBeginPosition <em>Begin Position</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.TimePeriodTypeImpl#getEndPosition <em>End Position</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.TimePeriodTypeImpl#getTimeResolution <em>Time Resolution</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.TimePeriodTypeImpl#getFrame <em>Frame</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TimePeriodTypeImpl extends EObjectImpl implements TimePeriodType {
    /**
	 * The cached value of the '{@link #getBeginPosition() <em>Begin Position</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getBeginPosition()
	 * @generated
	 * @ordered
	 */
    protected TimePositionType beginPosition;

    /**
	 * The cached value of the '{@link #getEndPosition() <em>End Position</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getEndPosition()
	 * @generated
	 * @ordered
	 */
    protected TimePositionType endPosition;

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
		return Wcs10Package.Literals.TIME_PERIOD_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TimePositionType getBeginPosition() {
		return beginPosition;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetBeginPosition(TimePositionType newBeginPosition, NotificationChain msgs) {
		TimePositionType oldBeginPosition = beginPosition;
		beginPosition = newBeginPosition;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.TIME_PERIOD_TYPE__BEGIN_POSITION, oldBeginPosition, newBeginPosition);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBeginPosition(TimePositionType newBeginPosition) {
		if (newBeginPosition != beginPosition) {
			NotificationChain msgs = null;
			if (beginPosition != null)
				msgs = ((InternalEObject)beginPosition).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.TIME_PERIOD_TYPE__BEGIN_POSITION, null, msgs);
			if (newBeginPosition != null)
				msgs = ((InternalEObject)newBeginPosition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.TIME_PERIOD_TYPE__BEGIN_POSITION, null, msgs);
			msgs = basicSetBeginPosition(newBeginPosition, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.TIME_PERIOD_TYPE__BEGIN_POSITION, newBeginPosition, newBeginPosition));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TimePositionType getEndPosition() {
		return endPosition;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetEndPosition(TimePositionType newEndPosition, NotificationChain msgs) {
		TimePositionType oldEndPosition = endPosition;
		endPosition = newEndPosition;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.TIME_PERIOD_TYPE__END_POSITION, oldEndPosition, newEndPosition);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEndPosition(TimePositionType newEndPosition) {
		if (newEndPosition != endPosition) {
			NotificationChain msgs = null;
			if (endPosition != null)
				msgs = ((InternalEObject)endPosition).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.TIME_PERIOD_TYPE__END_POSITION, null, msgs);
			if (newEndPosition != null)
				msgs = ((InternalEObject)newEndPosition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.TIME_PERIOD_TYPE__END_POSITION, null, msgs);
			msgs = basicSetEndPosition(newEndPosition, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.TIME_PERIOD_TYPE__END_POSITION, newEndPosition, newEndPosition));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.TIME_PERIOD_TYPE__TIME_RESOLUTION, oldTimeResolution, timeResolution));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.TIME_PERIOD_TYPE__FRAME, oldFrame, frame, !oldFrameESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.TIME_PERIOD_TYPE__FRAME, oldFrame, FRAME_EDEFAULT, oldFrameESet));
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
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.TIME_PERIOD_TYPE__BEGIN_POSITION:
				return basicSetBeginPosition(null, msgs);
			case Wcs10Package.TIME_PERIOD_TYPE__END_POSITION:
				return basicSetEndPosition(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Wcs10Package.TIME_PERIOD_TYPE__BEGIN_POSITION:
				return getBeginPosition();
			case Wcs10Package.TIME_PERIOD_TYPE__END_POSITION:
				return getEndPosition();
			case Wcs10Package.TIME_PERIOD_TYPE__TIME_RESOLUTION:
				return getTimeResolution();
			case Wcs10Package.TIME_PERIOD_TYPE__FRAME:
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
			case Wcs10Package.TIME_PERIOD_TYPE__BEGIN_POSITION:
				setBeginPosition((TimePositionType)newValue);
				return;
			case Wcs10Package.TIME_PERIOD_TYPE__END_POSITION:
				setEndPosition((TimePositionType)newValue);
				return;
			case Wcs10Package.TIME_PERIOD_TYPE__TIME_RESOLUTION:
				setTimeResolution(newValue);
				return;
			case Wcs10Package.TIME_PERIOD_TYPE__FRAME:
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
			case Wcs10Package.TIME_PERIOD_TYPE__BEGIN_POSITION:
				setBeginPosition((TimePositionType)null);
				return;
			case Wcs10Package.TIME_PERIOD_TYPE__END_POSITION:
				setEndPosition((TimePositionType)null);
				return;
			case Wcs10Package.TIME_PERIOD_TYPE__TIME_RESOLUTION:
				setTimeResolution(TIME_RESOLUTION_EDEFAULT);
				return;
			case Wcs10Package.TIME_PERIOD_TYPE__FRAME:
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
			case Wcs10Package.TIME_PERIOD_TYPE__BEGIN_POSITION:
				return beginPosition != null;
			case Wcs10Package.TIME_PERIOD_TYPE__END_POSITION:
				return endPosition != null;
			case Wcs10Package.TIME_PERIOD_TYPE__TIME_RESOLUTION:
				return TIME_RESOLUTION_EDEFAULT == null ? timeResolution != null : !TIME_RESOLUTION_EDEFAULT.equals(timeResolution);
			case Wcs10Package.TIME_PERIOD_TYPE__FRAME:
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
		result.append(" (timeResolution: ");
		result.append(timeResolution);
		result.append(", frame: ");
		if (frameESet) result.append(frame); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //TimePeriodTypeImpl
