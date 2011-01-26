/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.wcs10.ClosureType;
import net.opengis.wcs10.TypedLiteralType;
import net.opengis.wcs10.ValueRangeType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Value Range Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.ValueRangeTypeImpl#getMin <em>Min</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ValueRangeTypeImpl#getMax <em>Max</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ValueRangeTypeImpl#isAtomic <em>Atomic</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ValueRangeTypeImpl#getClosure <em>Closure</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ValueRangeTypeImpl#getSemantic <em>Semantic</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ValueRangeTypeImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ValueRangeTypeImpl extends EObjectImpl implements ValueRangeType {
    /**
	 * The cached value of the '{@link #getMin() <em>Min</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getMin()
	 * @generated
	 * @ordered
	 */
    protected TypedLiteralType min;

    /**
	 * The cached value of the '{@link #getMax() <em>Max</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getMax()
	 * @generated
	 * @ordered
	 */
    protected TypedLiteralType max;

    /**
	 * The default value of the '{@link #isAtomic() <em>Atomic</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isAtomic()
	 * @generated
	 * @ordered
	 */
    protected static final boolean ATOMIC_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isAtomic() <em>Atomic</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isAtomic()
	 * @generated
	 * @ordered
	 */
    protected boolean atomic = ATOMIC_EDEFAULT;

    /**
	 * This is true if the Atomic attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean atomicESet;

    /**
	 * The default value of the '{@link #getClosure() <em>Closure</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getClosure()
	 * @generated
	 * @ordered
	 */
    protected static final ClosureType CLOSURE_EDEFAULT = ClosureType.CLOSED_LITERAL;

    /**
	 * The cached value of the '{@link #getClosure() <em>Closure</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getClosure()
	 * @generated
	 * @ordered
	 */
    protected ClosureType closure = CLOSURE_EDEFAULT;

    /**
	 * This is true if the Closure attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean closureESet;

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
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected static final String TYPE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected String type = TYPE_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected ValueRangeTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.VALUE_RANGE_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TypedLiteralType getMin() {
		return min;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetMin(TypedLiteralType newMin, NotificationChain msgs) {
		TypedLiteralType oldMin = min;
		min = newMin;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.VALUE_RANGE_TYPE__MIN, oldMin, newMin);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMin(TypedLiteralType newMin) {
		if (newMin != min) {
			NotificationChain msgs = null;
			if (min != null)
				msgs = ((InternalEObject)min).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.VALUE_RANGE_TYPE__MIN, null, msgs);
			if (newMin != null)
				msgs = ((InternalEObject)newMin).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.VALUE_RANGE_TYPE__MIN, null, msgs);
			msgs = basicSetMin(newMin, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.VALUE_RANGE_TYPE__MIN, newMin, newMin));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TypedLiteralType getMax() {
		return max;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetMax(TypedLiteralType newMax, NotificationChain msgs) {
		TypedLiteralType oldMax = max;
		max = newMax;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.VALUE_RANGE_TYPE__MAX, oldMax, newMax);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMax(TypedLiteralType newMax) {
		if (newMax != max) {
			NotificationChain msgs = null;
			if (max != null)
				msgs = ((InternalEObject)max).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.VALUE_RANGE_TYPE__MAX, null, msgs);
			if (newMax != null)
				msgs = ((InternalEObject)newMax).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.VALUE_RANGE_TYPE__MAX, null, msgs);
			msgs = basicSetMax(newMax, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.VALUE_RANGE_TYPE__MAX, newMax, newMax));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isAtomic() {
		return atomic;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAtomic(boolean newAtomic) {
		boolean oldAtomic = atomic;
		atomic = newAtomic;
		boolean oldAtomicESet = atomicESet;
		atomicESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.VALUE_RANGE_TYPE__ATOMIC, oldAtomic, atomic, !oldAtomicESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetAtomic() {
		boolean oldAtomic = atomic;
		boolean oldAtomicESet = atomicESet;
		atomic = ATOMIC_EDEFAULT;
		atomicESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.VALUE_RANGE_TYPE__ATOMIC, oldAtomic, ATOMIC_EDEFAULT, oldAtomicESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetAtomic() {
		return atomicESet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ClosureType getClosure() {
		return closure;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setClosure(ClosureType newClosure) {
		ClosureType oldClosure = closure;
		closure = newClosure == null ? CLOSURE_EDEFAULT : newClosure;
		boolean oldClosureESet = closureESet;
		closureESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.VALUE_RANGE_TYPE__CLOSURE, oldClosure, closure, !oldClosureESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetClosure() {
		ClosureType oldClosure = closure;
		boolean oldClosureESet = closureESet;
		closure = CLOSURE_EDEFAULT;
		closureESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.VALUE_RANGE_TYPE__CLOSURE, oldClosure, CLOSURE_EDEFAULT, oldClosureESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetClosure() {
		return closureESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.VALUE_RANGE_TYPE__SEMANTIC, oldSemantic, semantic));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getType() {
		return type;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setType(String newType) {
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.VALUE_RANGE_TYPE__TYPE, oldType, type));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.VALUE_RANGE_TYPE__MIN:
				return basicSetMin(null, msgs);
			case Wcs10Package.VALUE_RANGE_TYPE__MAX:
				return basicSetMax(null, msgs);
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
			case Wcs10Package.VALUE_RANGE_TYPE__MIN:
				return getMin();
			case Wcs10Package.VALUE_RANGE_TYPE__MAX:
				return getMax();
			case Wcs10Package.VALUE_RANGE_TYPE__ATOMIC:
				return isAtomic() ? Boolean.TRUE : Boolean.FALSE;
			case Wcs10Package.VALUE_RANGE_TYPE__CLOSURE:
				return getClosure();
			case Wcs10Package.VALUE_RANGE_TYPE__SEMANTIC:
				return getSemantic();
			case Wcs10Package.VALUE_RANGE_TYPE__TYPE:
				return getType();
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
			case Wcs10Package.VALUE_RANGE_TYPE__MIN:
				setMin((TypedLiteralType)newValue);
				return;
			case Wcs10Package.VALUE_RANGE_TYPE__MAX:
				setMax((TypedLiteralType)newValue);
				return;
			case Wcs10Package.VALUE_RANGE_TYPE__ATOMIC:
				setAtomic(((Boolean)newValue).booleanValue());
				return;
			case Wcs10Package.VALUE_RANGE_TYPE__CLOSURE:
				setClosure((ClosureType)newValue);
				return;
			case Wcs10Package.VALUE_RANGE_TYPE__SEMANTIC:
				setSemantic((String)newValue);
				return;
			case Wcs10Package.VALUE_RANGE_TYPE__TYPE:
				setType((String)newValue);
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
			case Wcs10Package.VALUE_RANGE_TYPE__MIN:
				setMin((TypedLiteralType)null);
				return;
			case Wcs10Package.VALUE_RANGE_TYPE__MAX:
				setMax((TypedLiteralType)null);
				return;
			case Wcs10Package.VALUE_RANGE_TYPE__ATOMIC:
				unsetAtomic();
				return;
			case Wcs10Package.VALUE_RANGE_TYPE__CLOSURE:
				unsetClosure();
				return;
			case Wcs10Package.VALUE_RANGE_TYPE__SEMANTIC:
				setSemantic(SEMANTIC_EDEFAULT);
				return;
			case Wcs10Package.VALUE_RANGE_TYPE__TYPE:
				setType(TYPE_EDEFAULT);
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
			case Wcs10Package.VALUE_RANGE_TYPE__MIN:
				return min != null;
			case Wcs10Package.VALUE_RANGE_TYPE__MAX:
				return max != null;
			case Wcs10Package.VALUE_RANGE_TYPE__ATOMIC:
				return isSetAtomic();
			case Wcs10Package.VALUE_RANGE_TYPE__CLOSURE:
				return isSetClosure();
			case Wcs10Package.VALUE_RANGE_TYPE__SEMANTIC:
				return SEMANTIC_EDEFAULT == null ? semantic != null : !SEMANTIC_EDEFAULT.equals(semantic);
			case Wcs10Package.VALUE_RANGE_TYPE__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
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
		result.append(" (atomic: ");
		if (atomicESet) result.append(atomic); else result.append("<unset>");
		result.append(", closure: ");
		if (closureESet) result.append(closure); else result.append("<unset>");
		result.append(", semantic: ");
		result.append(semantic);
		result.append(", type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

} //ValueRangeTypeImpl
