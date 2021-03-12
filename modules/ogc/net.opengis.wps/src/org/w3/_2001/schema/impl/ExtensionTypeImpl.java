/**
 */
package org.w3._2001.schema.impl;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.w3._2001.schema.All;
import org.w3._2001.schema.Attribute;
import org.w3._2001.schema.AttributeGroupRef;
import org.w3._2001.schema.ExplicitGroup;
import org.w3._2001.schema.ExtensionType;
import org.w3._2001.schema.GroupRef;
import org.w3._2001.schema.SchemaPackage;
import org.w3._2001.schema.Wildcard;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Extension Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.impl.ExtensionTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ExtensionTypeImpl#getAll <em>All</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ExtensionTypeImpl#getChoice <em>Choice</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ExtensionTypeImpl#getSequence <em>Sequence</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ExtensionTypeImpl#getGroup1 <em>Group1</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ExtensionTypeImpl#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ExtensionTypeImpl#getAttributeGroup <em>Attribute Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ExtensionTypeImpl#getAnyAttribute1 <em>Any Attribute1</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ExtensionTypeImpl#getBase <em>Base</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExtensionTypeImpl extends AnnotatedImpl implements ExtensionType {
	/**
	 * The cached value of the '{@link #getGroup() <em>Group</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup()
	 * @generated
	 * @ordered
	 */
	protected GroupRef group;

	/**
	 * The cached value of the '{@link #getAll() <em>All</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAll()
	 * @generated
	 * @ordered
	 */
	protected All all;

	/**
	 * The cached value of the '{@link #getChoice() <em>Choice</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChoice()
	 * @generated
	 * @ordered
	 */
	protected ExplicitGroup choice;

	/**
	 * The cached value of the '{@link #getSequence() <em>Sequence</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequence()
	 * @generated
	 * @ordered
	 */
	protected ExplicitGroup sequence;

	/**
	 * The cached value of the '{@link #getGroup1() <em>Group1</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup1()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap group1;

	/**
	 * The cached value of the '{@link #getAnyAttribute1() <em>Any Attribute1</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnyAttribute1()
	 * @generated
	 * @ordered
	 */
	protected Wildcard anyAttribute1;

	/**
	 * The default value of the '{@link #getBase() <em>Base</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBase()
	 * @generated
	 * @ordered
	 */
	protected static final QName BASE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBase() <em>Base</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBase()
	 * @generated
	 * @ordered
	 */
	protected QName base = BASE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExtensionTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.EXTENSION_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupRef getGroup() {
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGroup(GroupRef newGroup, NotificationChain msgs) {
		GroupRef oldGroup = group;
		group = newGroup;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.EXTENSION_TYPE__GROUP, oldGroup, newGroup);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGroup(GroupRef newGroup) {
		if (newGroup != group) {
			NotificationChain msgs = null;
			if (group != null)
				msgs = ((InternalEObject)group).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.EXTENSION_TYPE__GROUP, null, msgs);
			if (newGroup != null)
				msgs = ((InternalEObject)newGroup).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.EXTENSION_TYPE__GROUP, null, msgs);
			msgs = basicSetGroup(newGroup, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.EXTENSION_TYPE__GROUP, newGroup, newGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public All getAll() {
		return all;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAll(All newAll, NotificationChain msgs) {
		All oldAll = all;
		all = newAll;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.EXTENSION_TYPE__ALL, oldAll, newAll);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAll(All newAll) {
		if (newAll != all) {
			NotificationChain msgs = null;
			if (all != null)
				msgs = ((InternalEObject)all).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.EXTENSION_TYPE__ALL, null, msgs);
			if (newAll != null)
				msgs = ((InternalEObject)newAll).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.EXTENSION_TYPE__ALL, null, msgs);
			msgs = basicSetAll(newAll, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.EXTENSION_TYPE__ALL, newAll, newAll));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExplicitGroup getChoice() {
		return choice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetChoice(ExplicitGroup newChoice, NotificationChain msgs) {
		ExplicitGroup oldChoice = choice;
		choice = newChoice;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.EXTENSION_TYPE__CHOICE, oldChoice, newChoice);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChoice(ExplicitGroup newChoice) {
		if (newChoice != choice) {
			NotificationChain msgs = null;
			if (choice != null)
				msgs = ((InternalEObject)choice).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.EXTENSION_TYPE__CHOICE, null, msgs);
			if (newChoice != null)
				msgs = ((InternalEObject)newChoice).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.EXTENSION_TYPE__CHOICE, null, msgs);
			msgs = basicSetChoice(newChoice, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.EXTENSION_TYPE__CHOICE, newChoice, newChoice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExplicitGroup getSequence() {
		return sequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSequence(ExplicitGroup newSequence, NotificationChain msgs) {
		ExplicitGroup oldSequence = sequence;
		sequence = newSequence;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.EXTENSION_TYPE__SEQUENCE, oldSequence, newSequence);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSequence(ExplicitGroup newSequence) {
		if (newSequence != sequence) {
			NotificationChain msgs = null;
			if (sequence != null)
				msgs = ((InternalEObject)sequence).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.EXTENSION_TYPE__SEQUENCE, null, msgs);
			if (newSequence != null)
				msgs = ((InternalEObject)newSequence).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.EXTENSION_TYPE__SEQUENCE, null, msgs);
			msgs = basicSetSequence(newSequence, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.EXTENSION_TYPE__SEQUENCE, newSequence, newSequence));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getGroup1() {
		if (group1 == null) {
			group1 = new BasicFeatureMap(this, SchemaPackage.EXTENSION_TYPE__GROUP1);
		}
		return group1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Attribute> getAttribute() {
		return getGroup1().list(SchemaPackage.Literals.EXTENSION_TYPE__ATTRIBUTE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AttributeGroupRef> getAttributeGroup() {
		return getGroup1().list(SchemaPackage.Literals.EXTENSION_TYPE__ATTRIBUTE_GROUP);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Wildcard getAnyAttribute1() {
		return anyAttribute1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAnyAttribute1(Wildcard newAnyAttribute1, NotificationChain msgs) {
		Wildcard oldAnyAttribute1 = anyAttribute1;
		anyAttribute1 = newAnyAttribute1;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.EXTENSION_TYPE__ANY_ATTRIBUTE1, oldAnyAttribute1, newAnyAttribute1);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAnyAttribute1(Wildcard newAnyAttribute1) {
		if (newAnyAttribute1 != anyAttribute1) {
			NotificationChain msgs = null;
			if (anyAttribute1 != null)
				msgs = ((InternalEObject)anyAttribute1).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.EXTENSION_TYPE__ANY_ATTRIBUTE1, null, msgs);
			if (newAnyAttribute1 != null)
				msgs = ((InternalEObject)newAnyAttribute1).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.EXTENSION_TYPE__ANY_ATTRIBUTE1, null, msgs);
			msgs = basicSetAnyAttribute1(newAnyAttribute1, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.EXTENSION_TYPE__ANY_ATTRIBUTE1, newAnyAttribute1, newAnyAttribute1));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QName getBase() {
		return base;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBase(QName newBase) {
		QName oldBase = base;
		base = newBase;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.EXTENSION_TYPE__BASE, oldBase, base));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.EXTENSION_TYPE__GROUP:
				return basicSetGroup(null, msgs);
			case SchemaPackage.EXTENSION_TYPE__ALL:
				return basicSetAll(null, msgs);
			case SchemaPackage.EXTENSION_TYPE__CHOICE:
				return basicSetChoice(null, msgs);
			case SchemaPackage.EXTENSION_TYPE__SEQUENCE:
				return basicSetSequence(null, msgs);
			case SchemaPackage.EXTENSION_TYPE__GROUP1:
				return ((InternalEList<?>)getGroup1()).basicRemove(otherEnd, msgs);
			case SchemaPackage.EXTENSION_TYPE__ATTRIBUTE:
				return ((InternalEList<?>)getAttribute()).basicRemove(otherEnd, msgs);
			case SchemaPackage.EXTENSION_TYPE__ATTRIBUTE_GROUP:
				return ((InternalEList<?>)getAttributeGroup()).basicRemove(otherEnd, msgs);
			case SchemaPackage.EXTENSION_TYPE__ANY_ATTRIBUTE1:
				return basicSetAnyAttribute1(null, msgs);
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
			case SchemaPackage.EXTENSION_TYPE__GROUP:
				return getGroup();
			case SchemaPackage.EXTENSION_TYPE__ALL:
				return getAll();
			case SchemaPackage.EXTENSION_TYPE__CHOICE:
				return getChoice();
			case SchemaPackage.EXTENSION_TYPE__SEQUENCE:
				return getSequence();
			case SchemaPackage.EXTENSION_TYPE__GROUP1:
				if (coreType) return getGroup1();
				return ((FeatureMap.Internal)getGroup1()).getWrapper();
			case SchemaPackage.EXTENSION_TYPE__ATTRIBUTE:
				return getAttribute();
			case SchemaPackage.EXTENSION_TYPE__ATTRIBUTE_GROUP:
				return getAttributeGroup();
			case SchemaPackage.EXTENSION_TYPE__ANY_ATTRIBUTE1:
				return getAnyAttribute1();
			case SchemaPackage.EXTENSION_TYPE__BASE:
				return getBase();
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
			case SchemaPackage.EXTENSION_TYPE__GROUP:
				setGroup((GroupRef)newValue);
				return;
			case SchemaPackage.EXTENSION_TYPE__ALL:
				setAll((All)newValue);
				return;
			case SchemaPackage.EXTENSION_TYPE__CHOICE:
				setChoice((ExplicitGroup)newValue);
				return;
			case SchemaPackage.EXTENSION_TYPE__SEQUENCE:
				setSequence((ExplicitGroup)newValue);
				return;
			case SchemaPackage.EXTENSION_TYPE__GROUP1:
				((FeatureMap.Internal)getGroup1()).set(newValue);
				return;
			case SchemaPackage.EXTENSION_TYPE__ATTRIBUTE:
				getAttribute().clear();
				getAttribute().addAll((Collection<? extends Attribute>)newValue);
				return;
			case SchemaPackage.EXTENSION_TYPE__ATTRIBUTE_GROUP:
				getAttributeGroup().clear();
				getAttributeGroup().addAll((Collection<? extends AttributeGroupRef>)newValue);
				return;
			case SchemaPackage.EXTENSION_TYPE__ANY_ATTRIBUTE1:
				setAnyAttribute1((Wildcard)newValue);
				return;
			case SchemaPackage.EXTENSION_TYPE__BASE:
				setBase((QName)newValue);
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
			case SchemaPackage.EXTENSION_TYPE__GROUP:
				setGroup((GroupRef)null);
				return;
			case SchemaPackage.EXTENSION_TYPE__ALL:
				setAll((All)null);
				return;
			case SchemaPackage.EXTENSION_TYPE__CHOICE:
				setChoice((ExplicitGroup)null);
				return;
			case SchemaPackage.EXTENSION_TYPE__SEQUENCE:
				setSequence((ExplicitGroup)null);
				return;
			case SchemaPackage.EXTENSION_TYPE__GROUP1:
				getGroup1().clear();
				return;
			case SchemaPackage.EXTENSION_TYPE__ATTRIBUTE:
				getAttribute().clear();
				return;
			case SchemaPackage.EXTENSION_TYPE__ATTRIBUTE_GROUP:
				getAttributeGroup().clear();
				return;
			case SchemaPackage.EXTENSION_TYPE__ANY_ATTRIBUTE1:
				setAnyAttribute1((Wildcard)null);
				return;
			case SchemaPackage.EXTENSION_TYPE__BASE:
				setBase(BASE_EDEFAULT);
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
			case SchemaPackage.EXTENSION_TYPE__GROUP:
				return group != null;
			case SchemaPackage.EXTENSION_TYPE__ALL:
				return all != null;
			case SchemaPackage.EXTENSION_TYPE__CHOICE:
				return choice != null;
			case SchemaPackage.EXTENSION_TYPE__SEQUENCE:
				return sequence != null;
			case SchemaPackage.EXTENSION_TYPE__GROUP1:
				return group1 != null && !group1.isEmpty();
			case SchemaPackage.EXTENSION_TYPE__ATTRIBUTE:
				return !getAttribute().isEmpty();
			case SchemaPackage.EXTENSION_TYPE__ATTRIBUTE_GROUP:
				return !getAttributeGroup().isEmpty();
			case SchemaPackage.EXTENSION_TYPE__ANY_ATTRIBUTE1:
				return anyAttribute1 != null;
			case SchemaPackage.EXTENSION_TYPE__BASE:
				return BASE_EDEFAULT == null ? base != null : !BASE_EDEFAULT.equals(base);
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (group1: ");
		result.append(group1);
		result.append(", base: ");
		result.append(base);
		result.append(')');
		return result.toString();
	}

} //ExtensionTypeImpl
