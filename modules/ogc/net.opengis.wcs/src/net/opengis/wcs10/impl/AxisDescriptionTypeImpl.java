/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.wcs10.AxisDescriptionType;
import net.opengis.wcs10.ValuesType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Axis Description Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.AxisDescriptionTypeImpl#getValues <em>Values</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.AxisDescriptionTypeImpl#getRefSys <em>Ref Sys</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.AxisDescriptionTypeImpl#getRefSysLabel <em>Ref Sys Label</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.AxisDescriptionTypeImpl#getSemantic <em>Semantic</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AxisDescriptionTypeImpl extends AbstractDescriptionTypeImpl implements AxisDescriptionType {
    /**
	 * The cached value of the '{@link #getValues() <em>Values</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getValues()
	 * @generated
	 * @ordered
	 */
    protected ValuesType values;

    /**
	 * The default value of the '{@link #getRefSys() <em>Ref Sys</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRefSys()
	 * @generated
	 * @ordered
	 */
    protected static final String REF_SYS_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getRefSys() <em>Ref Sys</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRefSys()
	 * @generated
	 * @ordered
	 */
    protected String refSys = REF_SYS_EDEFAULT;

    /**
	 * The default value of the '{@link #getRefSysLabel() <em>Ref Sys Label</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRefSysLabel()
	 * @generated
	 * @ordered
	 */
    protected static final String REF_SYS_LABEL_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getRefSysLabel() <em>Ref Sys Label</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRefSysLabel()
	 * @generated
	 * @ordered
	 */
    protected String refSysLabel = REF_SYS_LABEL_EDEFAULT;

    /**
	 * The default value of the '{@link #getSemantic() <em>Semantic</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getSemantic()
	 * @generated
	 * @ordered
	 */
    protected static final String SEMANTIC_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getSemantic() <em>Semantic</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getSemantic()
	 * @generated
	 * @ordered
	 */
    protected String semantic = SEMANTIC_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected AxisDescriptionTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.AXIS_DESCRIPTION_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ValuesType getValues() {
		return values;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetValues(ValuesType newValues, NotificationChain msgs) {
		ValuesType oldValues = values;
		values = newValues;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.AXIS_DESCRIPTION_TYPE__VALUES, oldValues, newValues);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setValues(ValuesType newValues) {
		if (newValues != values) {
			NotificationChain msgs = null;
			if (values != null)
				msgs = ((InternalEObject)values).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.AXIS_DESCRIPTION_TYPE__VALUES, null, msgs);
			if (newValues != null)
				msgs = ((InternalEObject)newValues).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.AXIS_DESCRIPTION_TYPE__VALUES, null, msgs);
			msgs = basicSetValues(newValues, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.AXIS_DESCRIPTION_TYPE__VALUES, newValues, newValues));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getRefSys() {
		return refSys;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRefSys(String newRefSys) {
		String oldRefSys = refSys;
		refSys = newRefSys;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.AXIS_DESCRIPTION_TYPE__REF_SYS, oldRefSys, refSys));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getRefSysLabel() {
		return refSysLabel;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRefSysLabel(String newRefSysLabel) {
		String oldRefSysLabel = refSysLabel;
		refSysLabel = newRefSysLabel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.AXIS_DESCRIPTION_TYPE__REF_SYS_LABEL, oldRefSysLabel, refSysLabel));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getSemantic() {
		return semantic;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSemantic(String newSemantic) {
		String oldSemantic = semantic;
		semantic = newSemantic;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.AXIS_DESCRIPTION_TYPE__SEMANTIC, oldSemantic, semantic));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__VALUES:
				return basicSetValues(null, msgs);
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
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__VALUES:
				return getValues();
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__REF_SYS:
				return getRefSys();
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__REF_SYS_LABEL:
				return getRefSysLabel();
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__SEMANTIC:
				return getSemantic();
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
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__VALUES:
				setValues((ValuesType)newValue);
				return;
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__REF_SYS:
				setRefSys((String)newValue);
				return;
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__REF_SYS_LABEL:
				setRefSysLabel((String)newValue);
				return;
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__SEMANTIC:
				setSemantic((String)newValue);
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
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__VALUES:
				setValues((ValuesType)null);
				return;
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__REF_SYS:
				setRefSys(REF_SYS_EDEFAULT);
				return;
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__REF_SYS_LABEL:
				setRefSysLabel(REF_SYS_LABEL_EDEFAULT);
				return;
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__SEMANTIC:
				setSemantic(SEMANTIC_EDEFAULT);
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
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__VALUES:
				return values != null;
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__REF_SYS:
				return REF_SYS_EDEFAULT == null ? refSys != null : !REF_SYS_EDEFAULT.equals(refSys);
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__REF_SYS_LABEL:
				return REF_SYS_LABEL_EDEFAULT == null ? refSysLabel != null : !REF_SYS_LABEL_EDEFAULT.equals(refSysLabel);
			case Wcs10Package.AXIS_DESCRIPTION_TYPE__SEMANTIC:
				return SEMANTIC_EDEFAULT == null ? semantic != null : !SEMANTIC_EDEFAULT.equals(semantic);
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
		result.append(" (refSys: ");
		result.append(refSys);
		result.append(", refSysLabel: ");
		result.append(refSysLabel);
		result.append(", semantic: ");
		result.append(semantic);
		result.append(')');
		return result.toString();
	}

} //AxisDescriptionTypeImpl
