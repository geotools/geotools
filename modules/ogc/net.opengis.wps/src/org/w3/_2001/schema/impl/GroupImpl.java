/**
 */
package org.w3._2001.schema.impl;

import java.math.BigInteger;

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
import org.w3._2001.schema.AnyType;
import org.w3._2001.schema.ExplicitGroup;
import org.w3._2001.schema.Group;
import org.w3._2001.schema.GroupRef;
import org.w3._2001.schema.LocalElement;
import org.w3._2001.schema.SchemaFactory;
import org.w3._2001.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.impl.GroupImpl#getParticle <em>Particle</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.GroupImpl#getElement <em>Element</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.GroupImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.GroupImpl#getAll <em>All</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.GroupImpl#getChoice <em>Choice</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.GroupImpl#getSequence <em>Sequence</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.GroupImpl#getAny <em>Any</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.GroupImpl#getMaxOccurs <em>Max Occurs</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.GroupImpl#getMinOccurs <em>Min Occurs</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.GroupImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.GroupImpl#getRef <em>Ref</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class GroupImpl extends AnnotatedImpl implements Group {
	/**
	 * The cached value of the '{@link #getParticle() <em>Particle</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParticle()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap particle;

	/**
	 * The default value of the '{@link #getMaxOccurs() <em>Max Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxOccurs()
	 * @generated
	 * @ordered
	 */
	protected static final Object MAX_OCCURS_EDEFAULT = SchemaFactory.eINSTANCE.createFromString(SchemaPackage.eINSTANCE.getAllNNI(), "1");

	/**
	 * The cached value of the '{@link #getMaxOccurs() <em>Max Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxOccurs()
	 * @generated
	 * @ordered
	 */
	protected Object maxOccurs = MAX_OCCURS_EDEFAULT;

	/**
	 * This is true if the Max Occurs attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxOccursESet;

	/**
	 * The default value of the '{@link #getMinOccurs() <em>Min Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinOccurs()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger MIN_OCCURS_EDEFAULT = new BigInteger("1");

	/**
	 * The cached value of the '{@link #getMinOccurs() <em>Min Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinOccurs()
	 * @generated
	 * @ordered
	 */
	protected BigInteger minOccurs = MIN_OCCURS_EDEFAULT;

	/**
	 * This is true if the Min Occurs attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minOccursESet;

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
	protected GroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getParticle() {
		if (particle == null) {
			particle = new BasicFeatureMap(this, SchemaPackage.GROUP__PARTICLE);
		}
		return particle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<LocalElement> getElement() {
		return getParticle().list(SchemaPackage.Literals.GROUP__ELEMENT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<GroupRef> getGroup() {
		return getParticle().list(SchemaPackage.Literals.GROUP__GROUP);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<All> getAll() {
		return getParticle().list(SchemaPackage.Literals.GROUP__ALL);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ExplicitGroup> getChoice() {
		return getParticle().list(SchemaPackage.Literals.GROUP__CHOICE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ExplicitGroup> getSequence() {
		return getParticle().list(SchemaPackage.Literals.GROUP__SEQUENCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AnyType> getAny() {
		return getParticle().list(SchemaPackage.Literals.GROUP__ANY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getMaxOccurs() {
		return maxOccurs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxOccurs(Object newMaxOccurs) {
		Object oldMaxOccurs = maxOccurs;
		maxOccurs = newMaxOccurs;
		boolean oldMaxOccursESet = maxOccursESet;
		maxOccursESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.GROUP__MAX_OCCURS, oldMaxOccurs, maxOccurs, !oldMaxOccursESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMaxOccurs() {
		Object oldMaxOccurs = maxOccurs;
		boolean oldMaxOccursESet = maxOccursESet;
		maxOccurs = MAX_OCCURS_EDEFAULT;
		maxOccursESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.GROUP__MAX_OCCURS, oldMaxOccurs, MAX_OCCURS_EDEFAULT, oldMaxOccursESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMaxOccurs() {
		return maxOccursESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getMinOccurs() {
		return minOccurs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinOccurs(BigInteger newMinOccurs) {
		BigInteger oldMinOccurs = minOccurs;
		minOccurs = newMinOccurs;
		boolean oldMinOccursESet = minOccursESet;
		minOccursESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.GROUP__MIN_OCCURS, oldMinOccurs, minOccurs, !oldMinOccursESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMinOccurs() {
		BigInteger oldMinOccurs = minOccurs;
		boolean oldMinOccursESet = minOccursESet;
		minOccurs = MIN_OCCURS_EDEFAULT;
		minOccursESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.GROUP__MIN_OCCURS, oldMinOccurs, MIN_OCCURS_EDEFAULT, oldMinOccursESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMinOccurs() {
		return minOccursESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.GROUP__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.GROUP__REF, oldRef, ref));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.GROUP__PARTICLE:
				return ((InternalEList<?>)getParticle()).basicRemove(otherEnd, msgs);
			case SchemaPackage.GROUP__ELEMENT:
				return ((InternalEList<?>)getElement()).basicRemove(otherEnd, msgs);
			case SchemaPackage.GROUP__GROUP:
				return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
			case SchemaPackage.GROUP__ALL:
				return ((InternalEList<?>)getAll()).basicRemove(otherEnd, msgs);
			case SchemaPackage.GROUP__CHOICE:
				return ((InternalEList<?>)getChoice()).basicRemove(otherEnd, msgs);
			case SchemaPackage.GROUP__SEQUENCE:
				return ((InternalEList<?>)getSequence()).basicRemove(otherEnd, msgs);
			case SchemaPackage.GROUP__ANY:
				return ((InternalEList<?>)getAny()).basicRemove(otherEnd, msgs);
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
			case SchemaPackage.GROUP__PARTICLE:
				if (coreType) return getParticle();
				return ((FeatureMap.Internal)getParticle()).getWrapper();
			case SchemaPackage.GROUP__ELEMENT:
				return getElement();
			case SchemaPackage.GROUP__GROUP:
				return getGroup();
			case SchemaPackage.GROUP__ALL:
				return getAll();
			case SchemaPackage.GROUP__CHOICE:
				return getChoice();
			case SchemaPackage.GROUP__SEQUENCE:
				return getSequence();
			case SchemaPackage.GROUP__ANY:
				return getAny();
			case SchemaPackage.GROUP__MAX_OCCURS:
				return getMaxOccurs();
			case SchemaPackage.GROUP__MIN_OCCURS:
				return getMinOccurs();
			case SchemaPackage.GROUP__NAME:
				return getName();
			case SchemaPackage.GROUP__REF:
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
			case SchemaPackage.GROUP__PARTICLE:
				((FeatureMap.Internal)getParticle()).set(newValue);
				return;
			case SchemaPackage.GROUP__ELEMENT:
				getElement().clear();
				getElement().addAll((Collection<? extends LocalElement>)newValue);
				return;
			case SchemaPackage.GROUP__GROUP:
				getGroup().clear();
				getGroup().addAll((Collection<? extends GroupRef>)newValue);
				return;
			case SchemaPackage.GROUP__ALL:
				getAll().clear();
				getAll().addAll((Collection<? extends All>)newValue);
				return;
			case SchemaPackage.GROUP__CHOICE:
				getChoice().clear();
				getChoice().addAll((Collection<? extends ExplicitGroup>)newValue);
				return;
			case SchemaPackage.GROUP__SEQUENCE:
				getSequence().clear();
				getSequence().addAll((Collection<? extends ExplicitGroup>)newValue);
				return;
			case SchemaPackage.GROUP__ANY:
				getAny().clear();
				getAny().addAll((Collection<? extends AnyType>)newValue);
				return;
			case SchemaPackage.GROUP__MAX_OCCURS:
				setMaxOccurs(newValue);
				return;
			case SchemaPackage.GROUP__MIN_OCCURS:
				setMinOccurs((BigInteger)newValue);
				return;
			case SchemaPackage.GROUP__NAME:
				setName((String)newValue);
				return;
			case SchemaPackage.GROUP__REF:
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
			case SchemaPackage.GROUP__PARTICLE:
				getParticle().clear();
				return;
			case SchemaPackage.GROUP__ELEMENT:
				getElement().clear();
				return;
			case SchemaPackage.GROUP__GROUP:
				getGroup().clear();
				return;
			case SchemaPackage.GROUP__ALL:
				getAll().clear();
				return;
			case SchemaPackage.GROUP__CHOICE:
				getChoice().clear();
				return;
			case SchemaPackage.GROUP__SEQUENCE:
				getSequence().clear();
				return;
			case SchemaPackage.GROUP__ANY:
				getAny().clear();
				return;
			case SchemaPackage.GROUP__MAX_OCCURS:
				unsetMaxOccurs();
				return;
			case SchemaPackage.GROUP__MIN_OCCURS:
				unsetMinOccurs();
				return;
			case SchemaPackage.GROUP__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SchemaPackage.GROUP__REF:
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
			case SchemaPackage.GROUP__PARTICLE:
				return particle != null && !particle.isEmpty();
			case SchemaPackage.GROUP__ELEMENT:
				return !getElement().isEmpty();
			case SchemaPackage.GROUP__GROUP:
				return !getGroup().isEmpty();
			case SchemaPackage.GROUP__ALL:
				return !getAll().isEmpty();
			case SchemaPackage.GROUP__CHOICE:
				return !getChoice().isEmpty();
			case SchemaPackage.GROUP__SEQUENCE:
				return !getSequence().isEmpty();
			case SchemaPackage.GROUP__ANY:
				return !getAny().isEmpty();
			case SchemaPackage.GROUP__MAX_OCCURS:
				return isSetMaxOccurs();
			case SchemaPackage.GROUP__MIN_OCCURS:
				return isSetMinOccurs();
			case SchemaPackage.GROUP__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SchemaPackage.GROUP__REF:
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
		result.append(" (particle: ");
		result.append(particle);
		result.append(", maxOccurs: ");
		if (maxOccursESet) result.append(maxOccurs); else result.append("<unset>");
		result.append(", minOccurs: ");
		if (minOccursESet) result.append(minOccurs); else result.append("<unset>");
		result.append(", name: ");
		result.append(name);
		result.append(", ref: ");
		result.append(ref);
		result.append(')');
		return result.toString();
	}

} //GroupImpl
