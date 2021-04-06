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

import org.w3._2001.schema.Attribute;
import org.w3._2001.schema.AttributeGroup;
import org.w3._2001.schema.AttributeGroupRef;
import org.w3._2001.schema.SchemaPackage;
import org.w3._2001.schema.Wildcard;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Attribute Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.impl.AttributeGroupImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.AttributeGroupImpl#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.AttributeGroupImpl#getAttributeGroup <em>Attribute Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.AttributeGroupImpl#getAnyAttribute1 <em>Any Attribute1</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.AttributeGroupImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.AttributeGroupImpl#getRef <em>Ref</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AttributeGroupImpl extends AnnotatedImpl implements AttributeGroup {
	/**
	 * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap group;

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
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getRef() <em>Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRef()
	 * @generated
	 * @ordered
	 */
	protected static final QName REF_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRef() <em>Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRef()
	 * @generated
	 * @ordered
	 */
	protected QName ref = REF_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AttributeGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.ATTRIBUTE_GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getGroup() {
		if (group == null) {
			group = new BasicFeatureMap(this, SchemaPackage.ATTRIBUTE_GROUP__GROUP);
		}
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Attribute> getAttribute() {
		return getGroup().list(SchemaPackage.Literals.ATTRIBUTE_GROUP__ATTRIBUTE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AttributeGroupRef> getAttributeGroup() {
		return getGroup().list(SchemaPackage.Literals.ATTRIBUTE_GROUP__ATTRIBUTE_GROUP);
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.ATTRIBUTE_GROUP__ANY_ATTRIBUTE1, oldAnyAttribute1, newAnyAttribute1);
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
				msgs = ((InternalEObject)anyAttribute1).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.ATTRIBUTE_GROUP__ANY_ATTRIBUTE1, null, msgs);
			if (newAnyAttribute1 != null)
				msgs = ((InternalEObject)newAnyAttribute1).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.ATTRIBUTE_GROUP__ANY_ATTRIBUTE1, null, msgs);
			msgs = basicSetAnyAttribute1(newAnyAttribute1, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ATTRIBUTE_GROUP__ANY_ATTRIBUTE1, newAnyAttribute1, newAnyAttribute1));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ATTRIBUTE_GROUP__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QName getRef() {
		return ref;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRef(QName newRef) {
		QName oldRef = ref;
		ref = newRef;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ATTRIBUTE_GROUP__REF, oldRef, ref));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.ATTRIBUTE_GROUP__GROUP:
				return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
			case SchemaPackage.ATTRIBUTE_GROUP__ATTRIBUTE:
				return ((InternalEList<?>)getAttribute()).basicRemove(otherEnd, msgs);
			case SchemaPackage.ATTRIBUTE_GROUP__ATTRIBUTE_GROUP:
				return ((InternalEList<?>)getAttributeGroup()).basicRemove(otherEnd, msgs);
			case SchemaPackage.ATTRIBUTE_GROUP__ANY_ATTRIBUTE1:
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
			case SchemaPackage.ATTRIBUTE_GROUP__GROUP:
				if (coreType) return getGroup();
				return ((FeatureMap.Internal)getGroup()).getWrapper();
			case SchemaPackage.ATTRIBUTE_GROUP__ATTRIBUTE:
				return getAttribute();
			case SchemaPackage.ATTRIBUTE_GROUP__ATTRIBUTE_GROUP:
				return getAttributeGroup();
			case SchemaPackage.ATTRIBUTE_GROUP__ANY_ATTRIBUTE1:
				return getAnyAttribute1();
			case SchemaPackage.ATTRIBUTE_GROUP__NAME:
				return getName();
			case SchemaPackage.ATTRIBUTE_GROUP__REF:
				return getRef();
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
			case SchemaPackage.ATTRIBUTE_GROUP__GROUP:
				((FeatureMap.Internal)getGroup()).set(newValue);
				return;
			case SchemaPackage.ATTRIBUTE_GROUP__ATTRIBUTE:
				getAttribute().clear();
				getAttribute().addAll((Collection<? extends Attribute>)newValue);
				return;
			case SchemaPackage.ATTRIBUTE_GROUP__ATTRIBUTE_GROUP:
				getAttributeGroup().clear();
				getAttributeGroup().addAll((Collection<? extends AttributeGroupRef>)newValue);
				return;
			case SchemaPackage.ATTRIBUTE_GROUP__ANY_ATTRIBUTE1:
				setAnyAttribute1((Wildcard)newValue);
				return;
			case SchemaPackage.ATTRIBUTE_GROUP__NAME:
				setName((String)newValue);
				return;
			case SchemaPackage.ATTRIBUTE_GROUP__REF:
				setRef((QName)newValue);
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
			case SchemaPackage.ATTRIBUTE_GROUP__GROUP:
				getGroup().clear();
				return;
			case SchemaPackage.ATTRIBUTE_GROUP__ATTRIBUTE:
				getAttribute().clear();
				return;
			case SchemaPackage.ATTRIBUTE_GROUP__ATTRIBUTE_GROUP:
				getAttributeGroup().clear();
				return;
			case SchemaPackage.ATTRIBUTE_GROUP__ANY_ATTRIBUTE1:
				setAnyAttribute1((Wildcard)null);
				return;
			case SchemaPackage.ATTRIBUTE_GROUP__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SchemaPackage.ATTRIBUTE_GROUP__REF:
				setRef(REF_EDEFAULT);
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
			case SchemaPackage.ATTRIBUTE_GROUP__GROUP:
				return group != null && !group.isEmpty();
			case SchemaPackage.ATTRIBUTE_GROUP__ATTRIBUTE:
				return !getAttribute().isEmpty();
			case SchemaPackage.ATTRIBUTE_GROUP__ATTRIBUTE_GROUP:
				return !getAttributeGroup().isEmpty();
			case SchemaPackage.ATTRIBUTE_GROUP__ANY_ATTRIBUTE1:
				return anyAttribute1 != null;
			case SchemaPackage.ATTRIBUTE_GROUP__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SchemaPackage.ATTRIBUTE_GROUP__REF:
				return REF_EDEFAULT == null ? ref != null : !REF_EDEFAULT.equals(ref);
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
		result.append(" (group: ");
		result.append(group);
		result.append(", name: ");
		result.append(name);
		result.append(", ref: ");
		result.append(ref);
		result.append(')');
		return result.toString();
	}

} //AttributeGroupImpl
