/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml.impl;

import net.opengis.gml.GmlPackage;

import net.opengis.gml.TimeIndeterminateValueType;
import net.opengis.gml.TimePositionType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Time Position Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.gml.impl.TimePositionTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml.impl.TimePositionTypeImpl#getCalendarEraName <em>Calendar Era Name</em>}</li>
 *   <li>{@link net.opengis.gml.impl.TimePositionTypeImpl#getFrame <em>Frame</em>}</li>
 *   <li>{@link net.opengis.gml.impl.TimePositionTypeImpl#getIndeterminatePosition <em>Indeterminate Position</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TimePositionTypeImpl extends EObjectImpl implements TimePositionType {
    /**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
    protected static final Object VALUE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
    protected Object value = VALUE_EDEFAULT;

    /**
	 * The default value of the '{@link #getCalendarEraName() <em>Calendar Era Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getCalendarEraName()
	 * @generated
	 * @ordered
	 */
    protected static final String CALENDAR_ERA_NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getCalendarEraName() <em>Calendar Era Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getCalendarEraName()
	 * @generated
	 * @ordered
	 */
    protected String calendarEraName = CALENDAR_ERA_NAME_EDEFAULT;

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
	 * The default value of the '{@link #getIndeterminatePosition() <em>Indeterminate Position</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getIndeterminatePosition()
	 * @generated
	 * @ordered
	 */
    protected static final TimeIndeterminateValueType INDETERMINATE_POSITION_EDEFAULT = TimeIndeterminateValueType.AFTER_LITERAL;

    /**
	 * The cached value of the '{@link #getIndeterminatePosition() <em>Indeterminate Position</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getIndeterminatePosition()
	 * @generated
	 * @ordered
	 */
    protected TimeIndeterminateValueType indeterminatePosition = INDETERMINATE_POSITION_EDEFAULT;

    /**
	 * This is true if the Indeterminate Position attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean indeterminatePositionESet;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected TimePositionTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return GmlPackage.Literals.TIME_POSITION_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object getValue() {
		return value;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setValue(Object newValue) {
		Object oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GmlPackage.TIME_POSITION_TYPE__VALUE, oldValue, value));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getCalendarEraName() {
		return calendarEraName;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCalendarEraName(String newCalendarEraName) {
		String oldCalendarEraName = calendarEraName;
		calendarEraName = newCalendarEraName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GmlPackage.TIME_POSITION_TYPE__CALENDAR_ERA_NAME, oldCalendarEraName, calendarEraName));
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
			eNotify(new ENotificationImpl(this, Notification.SET, GmlPackage.TIME_POSITION_TYPE__FRAME, oldFrame, frame, !oldFrameESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, GmlPackage.TIME_POSITION_TYPE__FRAME, oldFrame, FRAME_EDEFAULT, oldFrameESet));
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
    public TimeIndeterminateValueType getIndeterminatePosition() {
		return indeterminatePosition;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setIndeterminatePosition(TimeIndeterminateValueType newIndeterminatePosition) {
		TimeIndeterminateValueType oldIndeterminatePosition = indeterminatePosition;
		indeterminatePosition = newIndeterminatePosition == null ? INDETERMINATE_POSITION_EDEFAULT : newIndeterminatePosition;
		boolean oldIndeterminatePositionESet = indeterminatePositionESet;
		indeterminatePositionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GmlPackage.TIME_POSITION_TYPE__INDETERMINATE_POSITION, oldIndeterminatePosition, indeterminatePosition, !oldIndeterminatePositionESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetIndeterminatePosition() {
		TimeIndeterminateValueType oldIndeterminatePosition = indeterminatePosition;
		boolean oldIndeterminatePositionESet = indeterminatePositionESet;
		indeterminatePosition = INDETERMINATE_POSITION_EDEFAULT;
		indeterminatePositionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, GmlPackage.TIME_POSITION_TYPE__INDETERMINATE_POSITION, oldIndeterminatePosition, INDETERMINATE_POSITION_EDEFAULT, oldIndeterminatePositionESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetIndeterminatePosition() {
		return indeterminatePositionESet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GmlPackage.TIME_POSITION_TYPE__VALUE:
				return getValue();
			case GmlPackage.TIME_POSITION_TYPE__CALENDAR_ERA_NAME:
				return getCalendarEraName();
			case GmlPackage.TIME_POSITION_TYPE__FRAME:
				return getFrame();
			case GmlPackage.TIME_POSITION_TYPE__INDETERMINATE_POSITION:
				return getIndeterminatePosition();
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
			case GmlPackage.TIME_POSITION_TYPE__VALUE:
				setValue(newValue);
				return;
			case GmlPackage.TIME_POSITION_TYPE__CALENDAR_ERA_NAME:
				setCalendarEraName((String)newValue);
				return;
			case GmlPackage.TIME_POSITION_TYPE__FRAME:
				setFrame((String)newValue);
				return;
			case GmlPackage.TIME_POSITION_TYPE__INDETERMINATE_POSITION:
				setIndeterminatePosition((TimeIndeterminateValueType)newValue);
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
			case GmlPackage.TIME_POSITION_TYPE__VALUE:
				setValue(VALUE_EDEFAULT);
				return;
			case GmlPackage.TIME_POSITION_TYPE__CALENDAR_ERA_NAME:
				setCalendarEraName(CALENDAR_ERA_NAME_EDEFAULT);
				return;
			case GmlPackage.TIME_POSITION_TYPE__FRAME:
				unsetFrame();
				return;
			case GmlPackage.TIME_POSITION_TYPE__INDETERMINATE_POSITION:
				unsetIndeterminatePosition();
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
			case GmlPackage.TIME_POSITION_TYPE__VALUE:
				return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
			case GmlPackage.TIME_POSITION_TYPE__CALENDAR_ERA_NAME:
				return CALENDAR_ERA_NAME_EDEFAULT == null ? calendarEraName != null : !CALENDAR_ERA_NAME_EDEFAULT.equals(calendarEraName);
			case GmlPackage.TIME_POSITION_TYPE__FRAME:
				return isSetFrame();
			case GmlPackage.TIME_POSITION_TYPE__INDETERMINATE_POSITION:
				return isSetIndeterminatePosition();
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
		result.append(" (value: ");
		result.append(value);
		result.append(", calendarEraName: ");
		result.append(calendarEraName);
		result.append(", frame: ");
		if (frameESet) result.append(frame); else result.append("<unset>");
		result.append(", indeterminatePosition: ");
		if (indeterminatePositionESet) result.append(indeterminatePosition); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //TimePositionTypeImpl
