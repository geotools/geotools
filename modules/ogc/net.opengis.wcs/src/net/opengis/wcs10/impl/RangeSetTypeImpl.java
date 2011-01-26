/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.Collection;

import net.opengis.wcs10.AxisDescriptionType1;
import net.opengis.wcs10.RangeSetType;
import net.opengis.wcs10.ValueEnumType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Range Set Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.RangeSetTypeImpl#getAxisDescription <em>Axis Description</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.RangeSetTypeImpl#getNullValues <em>Null Values</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.RangeSetTypeImpl#getRefSys <em>Ref Sys</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.RangeSetTypeImpl#getRefSysLabel <em>Ref Sys Label</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.RangeSetTypeImpl#getSemantic <em>Semantic</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RangeSetTypeImpl extends AbstractDescriptionTypeImpl implements RangeSetType {
    /**
	 * The cached value of the '{@link #getAxisDescription() <em>Axis Description</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getAxisDescription()
	 * @generated
	 * @ordered
	 */
    protected EList axisDescription;

    /**
	 * The cached value of the '{@link #getNullValues() <em>Null Values</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getNullValues()
	 * @generated
	 * @ordered
	 */
    protected ValueEnumType nullValues;

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
    protected RangeSetTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.RANGE_SET_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getAxisDescription() {
		if (axisDescription == null) {
			axisDescription = new EObjectContainmentEList(AxisDescriptionType1.class, this, Wcs10Package.RANGE_SET_TYPE__AXIS_DESCRIPTION);
		}
		return axisDescription;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ValueEnumType getNullValues() {
		return nullValues;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetNullValues(ValueEnumType newNullValues, NotificationChain msgs) {
		ValueEnumType oldNullValues = nullValues;
		nullValues = newNullValues;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.RANGE_SET_TYPE__NULL_VALUES, oldNullValues, newNullValues);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNullValues(ValueEnumType newNullValues) {
		if (newNullValues != nullValues) {
			NotificationChain msgs = null;
			if (nullValues != null)
				msgs = ((InternalEObject)nullValues).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.RANGE_SET_TYPE__NULL_VALUES, null, msgs);
			if (newNullValues != null)
				msgs = ((InternalEObject)newNullValues).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.RANGE_SET_TYPE__NULL_VALUES, null, msgs);
			msgs = basicSetNullValues(newNullValues, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.RANGE_SET_TYPE__NULL_VALUES, newNullValues, newNullValues));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.RANGE_SET_TYPE__REF_SYS, oldRefSys, refSys));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.RANGE_SET_TYPE__REF_SYS_LABEL, oldRefSysLabel, refSysLabel));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.RANGE_SET_TYPE__SEMANTIC, oldSemantic, semantic));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.RANGE_SET_TYPE__AXIS_DESCRIPTION:
				return ((InternalEList)getAxisDescription()).basicRemove(otherEnd, msgs);
			case Wcs10Package.RANGE_SET_TYPE__NULL_VALUES:
				return basicSetNullValues(null, msgs);
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
			case Wcs10Package.RANGE_SET_TYPE__AXIS_DESCRIPTION:
				return getAxisDescription();
			case Wcs10Package.RANGE_SET_TYPE__NULL_VALUES:
				return getNullValues();
			case Wcs10Package.RANGE_SET_TYPE__REF_SYS:
				return getRefSys();
			case Wcs10Package.RANGE_SET_TYPE__REF_SYS_LABEL:
				return getRefSysLabel();
			case Wcs10Package.RANGE_SET_TYPE__SEMANTIC:
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
			case Wcs10Package.RANGE_SET_TYPE__AXIS_DESCRIPTION:
				getAxisDescription().clear();
				getAxisDescription().addAll((Collection)newValue);
				return;
			case Wcs10Package.RANGE_SET_TYPE__NULL_VALUES:
				setNullValues((ValueEnumType)newValue);
				return;
			case Wcs10Package.RANGE_SET_TYPE__REF_SYS:
				setRefSys((String)newValue);
				return;
			case Wcs10Package.RANGE_SET_TYPE__REF_SYS_LABEL:
				setRefSysLabel((String)newValue);
				return;
			case Wcs10Package.RANGE_SET_TYPE__SEMANTIC:
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
			case Wcs10Package.RANGE_SET_TYPE__AXIS_DESCRIPTION:
				getAxisDescription().clear();
				return;
			case Wcs10Package.RANGE_SET_TYPE__NULL_VALUES:
				setNullValues((ValueEnumType)null);
				return;
			case Wcs10Package.RANGE_SET_TYPE__REF_SYS:
				setRefSys(REF_SYS_EDEFAULT);
				return;
			case Wcs10Package.RANGE_SET_TYPE__REF_SYS_LABEL:
				setRefSysLabel(REF_SYS_LABEL_EDEFAULT);
				return;
			case Wcs10Package.RANGE_SET_TYPE__SEMANTIC:
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
			case Wcs10Package.RANGE_SET_TYPE__AXIS_DESCRIPTION:
				return axisDescription != null && !axisDescription.isEmpty();
			case Wcs10Package.RANGE_SET_TYPE__NULL_VALUES:
				return nullValues != null;
			case Wcs10Package.RANGE_SET_TYPE__REF_SYS:
				return REF_SYS_EDEFAULT == null ? refSys != null : !REF_SYS_EDEFAULT.equals(refSys);
			case Wcs10Package.RANGE_SET_TYPE__REF_SYS_LABEL:
				return REF_SYS_LABEL_EDEFAULT == null ? refSysLabel != null : !REF_SYS_LABEL_EDEFAULT.equals(refSysLabel);
			case Wcs10Package.RANGE_SET_TYPE__SEMANTIC:
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

} //RangeSetTypeImpl
