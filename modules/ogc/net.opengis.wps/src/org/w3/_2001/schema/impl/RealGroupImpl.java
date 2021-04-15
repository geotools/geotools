/**
 */
package org.w3._2001.schema.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.w3._2001.schema.All;
import org.w3._2001.schema.ExplicitGroup;
import org.w3._2001.schema.RealGroup;
import org.w3._2001.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Real Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.impl.RealGroupImpl#getAll1 <em>All1</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RealGroupImpl#getChoice1 <em>Choice1</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RealGroupImpl#getSequence1 <em>Sequence1</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RealGroupImpl extends GroupImpl implements RealGroup {
	/**
	 * The cached value of the '{@link #getAll1() <em>All1</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAll1()
	 * @generated
	 * @ordered
	 */
	protected All all1;

	/**
	 * The cached value of the '{@link #getChoice1() <em>Choice1</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChoice1()
	 * @generated
	 * @ordered
	 */
	protected ExplicitGroup choice1;

	/**
	 * The cached value of the '{@link #getSequence1() <em>Sequence1</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequence1()
	 * @generated
	 * @ordered
	 */
	protected ExplicitGroup sequence1;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RealGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.REAL_GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public All getAll1() {
		return all1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAll1(All newAll1, NotificationChain msgs) {
		All oldAll1 = all1;
		all1 = newAll1;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.REAL_GROUP__ALL1, oldAll1, newAll1);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAll1(All newAll1) {
		if (newAll1 != all1) {
			NotificationChain msgs = null;
			if (all1 != null)
				msgs = ((InternalEObject)all1).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.REAL_GROUP__ALL1, null, msgs);
			if (newAll1 != null)
				msgs = ((InternalEObject)newAll1).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.REAL_GROUP__ALL1, null, msgs);
			msgs = basicSetAll1(newAll1, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.REAL_GROUP__ALL1, newAll1, newAll1));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExplicitGroup getChoice1() {
		return choice1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetChoice1(ExplicitGroup newChoice1, NotificationChain msgs) {
		ExplicitGroup oldChoice1 = choice1;
		choice1 = newChoice1;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.REAL_GROUP__CHOICE1, oldChoice1, newChoice1);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChoice1(ExplicitGroup newChoice1) {
		if (newChoice1 != choice1) {
			NotificationChain msgs = null;
			if (choice1 != null)
				msgs = ((InternalEObject)choice1).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.REAL_GROUP__CHOICE1, null, msgs);
			if (newChoice1 != null)
				msgs = ((InternalEObject)newChoice1).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.REAL_GROUP__CHOICE1, null, msgs);
			msgs = basicSetChoice1(newChoice1, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.REAL_GROUP__CHOICE1, newChoice1, newChoice1));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExplicitGroup getSequence1() {
		return sequence1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSequence1(ExplicitGroup newSequence1, NotificationChain msgs) {
		ExplicitGroup oldSequence1 = sequence1;
		sequence1 = newSequence1;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.REAL_GROUP__SEQUENCE1, oldSequence1, newSequence1);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSequence1(ExplicitGroup newSequence1) {
		if (newSequence1 != sequence1) {
			NotificationChain msgs = null;
			if (sequence1 != null)
				msgs = ((InternalEObject)sequence1).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.REAL_GROUP__SEQUENCE1, null, msgs);
			if (newSequence1 != null)
				msgs = ((InternalEObject)newSequence1).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.REAL_GROUP__SEQUENCE1, null, msgs);
			msgs = basicSetSequence1(newSequence1, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.REAL_GROUP__SEQUENCE1, newSequence1, newSequence1));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.REAL_GROUP__ALL1:
				return basicSetAll1(null, msgs);
			case SchemaPackage.REAL_GROUP__CHOICE1:
				return basicSetChoice1(null, msgs);
			case SchemaPackage.REAL_GROUP__SEQUENCE1:
				return basicSetSequence1(null, msgs);
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
			case SchemaPackage.REAL_GROUP__ALL1:
				return getAll1();
			case SchemaPackage.REAL_GROUP__CHOICE1:
				return getChoice1();
			case SchemaPackage.REAL_GROUP__SEQUENCE1:
				return getSequence1();
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
			case SchemaPackage.REAL_GROUP__ALL1:
				setAll1((All)newValue);
				return;
			case SchemaPackage.REAL_GROUP__CHOICE1:
				setChoice1((ExplicitGroup)newValue);
				return;
			case SchemaPackage.REAL_GROUP__SEQUENCE1:
				setSequence1((ExplicitGroup)newValue);
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
			case SchemaPackage.REAL_GROUP__ALL1:
				setAll1((All)null);
				return;
			case SchemaPackage.REAL_GROUP__CHOICE1:
				setChoice1((ExplicitGroup)null);
				return;
			case SchemaPackage.REAL_GROUP__SEQUENCE1:
				setSequence1((ExplicitGroup)null);
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
			case SchemaPackage.REAL_GROUP__ALL1:
				return all1 != null;
			case SchemaPackage.REAL_GROUP__CHOICE1:
				return choice1 != null;
			case SchemaPackage.REAL_GROUP__SEQUENCE1:
				return sequence1 != null;
		}
		return super.eIsSet(featureID);
	}

} //RealGroupImpl
