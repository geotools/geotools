/**
 */
package net.opengis.wps20.impl;

import java.math.BigInteger;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.w3._2001.schema.SchemaFactory;
import org.w3._2001.schema.SchemaPackage;

import net.opengis.wps20.DataDescriptionType;
import net.opengis.wps20.InputDescriptionType;
import net.opengis.wps20.Wps20Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Input Description Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link wps20.impl.InputDescriptionTypeImpl#getDataDescriptionGroup <em>Data Description Group</em>}</li>
 *   <li>{@link wps20.impl.InputDescriptionTypeImpl#getDataDescription <em>Data Description</em>}</li>
 *   <li>{@link wps20.impl.InputDescriptionTypeImpl#getInput <em>Input</em>}</li>
 *   <li>{@link wps20.impl.InputDescriptionTypeImpl#getMaxOccurs <em>Max Occurs</em>}</li>
 *   <li>{@link wps20.impl.InputDescriptionTypeImpl#getMinOccurs <em>Min Occurs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InputDescriptionTypeImpl extends DescriptionTypeImpl implements InputDescriptionType {
	/**
	 * The cached value of the '{@link #getDataDescriptionGroup() <em>Data Description Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataDescriptionGroup()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap dataDescriptionGroup;

	/**
	 * The cached value of the '{@link #getInput() <em>Input</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInput()
	 * @generated
	 * @ordered
	 */
	protected EList<InputDescriptionType> input;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InputDescriptionTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.INPUT_DESCRIPTION_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FeatureMap getDataDescriptionGroup() {
		if (dataDescriptionGroup == null) {
			dataDescriptionGroup = new BasicFeatureMap(this, Wps20Package.INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP);
		}
		return dataDescriptionGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DataDescriptionType getDataDescription() {
		return (DataDescriptionType)getDataDescriptionGroup().get(Wps20Package.Literals.INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDataDescription(DataDescriptionType newDataDescription, NotificationChain msgs) {
		return ((FeatureMap.Internal)getDataDescriptionGroup()).basicAdd(Wps20Package.Literals.INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION, newDataDescription, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<InputDescriptionType> getInput() {
		if (input == null) {
			input = new EObjectContainmentEList<InputDescriptionType>(InputDescriptionType.class, this, Wps20Package.INPUT_DESCRIPTION_TYPE__INPUT);
		}
		return input;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getMaxOccurs() {
		return maxOccurs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxOccurs(Object newMaxOccurs) {
		Object oldMaxOccurs = maxOccurs;
		maxOccurs = newMaxOccurs;
		boolean oldMaxOccursESet = maxOccursESet;
		maxOccursESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.INPUT_DESCRIPTION_TYPE__MAX_OCCURS, oldMaxOccurs, maxOccurs, !oldMaxOccursESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMaxOccurs() {
		Object oldMaxOccurs = maxOccurs;
		boolean oldMaxOccursESet = maxOccursESet;
		maxOccurs = MAX_OCCURS_EDEFAULT;
		maxOccursESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wps20Package.INPUT_DESCRIPTION_TYPE__MAX_OCCURS, oldMaxOccurs, MAX_OCCURS_EDEFAULT, oldMaxOccursESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMaxOccurs() {
		return maxOccursESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BigInteger getMinOccurs() {
		return minOccurs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinOccurs(BigInteger newMinOccurs) {
		BigInteger oldMinOccurs = minOccurs;
		minOccurs = newMinOccurs;
		boolean oldMinOccursESet = minOccursESet;
		minOccursESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.INPUT_DESCRIPTION_TYPE__MIN_OCCURS, oldMinOccurs, minOccurs, !oldMinOccursESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMinOccurs() {
		BigInteger oldMinOccurs = minOccurs;
		boolean oldMinOccursESet = minOccursESet;
		minOccurs = MIN_OCCURS_EDEFAULT;
		minOccursESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wps20Package.INPUT_DESCRIPTION_TYPE__MIN_OCCURS, oldMinOccurs, MIN_OCCURS_EDEFAULT, oldMinOccursESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMinOccurs() {
		return minOccursESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wps20Package.INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP:
				return ((InternalEList<?>)getDataDescriptionGroup()).basicRemove(otherEnd, msgs);
			case Wps20Package.INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION:
				return basicSetDataDescription(null, msgs);
			case Wps20Package.INPUT_DESCRIPTION_TYPE__INPUT:
				return ((InternalEList<?>)getInput()).basicRemove(otherEnd, msgs);
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
			case Wps20Package.INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP:
				if (coreType) return getDataDescriptionGroup();
				return ((FeatureMap.Internal)getDataDescriptionGroup()).getWrapper();
			case Wps20Package.INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION:
				return getDataDescription();
			case Wps20Package.INPUT_DESCRIPTION_TYPE__INPUT:
				return getInput();
			case Wps20Package.INPUT_DESCRIPTION_TYPE__MAX_OCCURS:
				return getMaxOccurs();
			case Wps20Package.INPUT_DESCRIPTION_TYPE__MIN_OCCURS:
				return getMinOccurs();
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
			case Wps20Package.INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP:
				((FeatureMap.Internal)getDataDescriptionGroup()).set(newValue);
				return;
			case Wps20Package.INPUT_DESCRIPTION_TYPE__INPUT:
				getInput().clear();
				getInput().addAll((Collection<? extends InputDescriptionType>)newValue);
				return;
			case Wps20Package.INPUT_DESCRIPTION_TYPE__MAX_OCCURS:
				setMaxOccurs(newValue);
				return;
			case Wps20Package.INPUT_DESCRIPTION_TYPE__MIN_OCCURS:
				setMinOccurs((BigInteger)newValue);
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
			case Wps20Package.INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP:
				getDataDescriptionGroup().clear();
				return;
			case Wps20Package.INPUT_DESCRIPTION_TYPE__INPUT:
				getInput().clear();
				return;
			case Wps20Package.INPUT_DESCRIPTION_TYPE__MAX_OCCURS:
				unsetMaxOccurs();
				return;
			case Wps20Package.INPUT_DESCRIPTION_TYPE__MIN_OCCURS:
				unsetMinOccurs();
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
			case Wps20Package.INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP:
				return dataDescriptionGroup != null && !dataDescriptionGroup.isEmpty();
			case Wps20Package.INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION:
				return getDataDescription() != null;
			case Wps20Package.INPUT_DESCRIPTION_TYPE__INPUT:
				return input != null && !input.isEmpty();
			case Wps20Package.INPUT_DESCRIPTION_TYPE__MAX_OCCURS:
				return isSetMaxOccurs();
			case Wps20Package.INPUT_DESCRIPTION_TYPE__MIN_OCCURS:
				return isSetMinOccurs();
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
		result.append(" (dataDescriptionGroup: ");
		result.append(dataDescriptionGroup);
		result.append(", maxOccurs: ");
		if (maxOccursESet) result.append(maxOccurs); else result.append("<unset>");
		result.append(", minOccurs: ");
		if (minOccursESet) result.append(minOccurs); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //InputDescriptionTypeImpl
