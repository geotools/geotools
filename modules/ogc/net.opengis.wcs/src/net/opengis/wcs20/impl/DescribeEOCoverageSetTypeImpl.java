/**
 */
package net.opengis.wcs20.impl;

import java.util.Collection;

import net.opengis.wcs20.DescribeEOCoverageSetType;
import net.opengis.wcs20.DimensionTrimType;
import net.opengis.wcs20.Sections;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Describe EO Coverage Set Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.DescribeEOCoverageSetTypeImpl#getEoId <em>Eo Id</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DescribeEOCoverageSetTypeImpl#getContainmentType <em>Containment Type</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DescribeEOCoverageSetTypeImpl#getSections <em>Sections</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DescribeEOCoverageSetTypeImpl#getDimensionTrim <em>Dimension Trim</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DescribeEOCoverageSetTypeImpl#getCount <em>Count</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DescribeEOCoverageSetTypeImpl extends RequestBaseTypeImpl implements DescribeEOCoverageSetType {
    /**
	 * The cached value of the '{@link #getEoId() <em>Eo Id</em>}' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getEoId()
	 * @generated
	 * @ordered
	 */
    protected EList<String> eoId;

    /**
	 * The default value of the '{@link #getContainmentType() <em>Containment Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getContainmentType()
	 * @generated
	 * @ordered
	 */
    protected static final String CONTAINMENT_TYPE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getContainmentType() <em>Containment Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getContainmentType()
	 * @generated
	 * @ordered
	 */
    protected String containmentType = CONTAINMENT_TYPE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getSections() <em>Sections</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getSections()
	 * @generated
	 * @ordered
	 */
    protected Sections sections;

    /**
	 * The cached value of the '{@link #getDimensionTrim() <em>Dimension Trim</em>}' reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getDimensionTrim()
	 * @generated
	 * @ordered
	 */
    protected EList<DimensionTrimType> dimensionTrim;

    /**
	 * The default value of the '{@link #getCount() <em>Count</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getCount()
	 * @generated
	 * @ordered
	 */
    protected static final int COUNT_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getCount() <em>Count</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getCount()
	 * @generated
	 * @ordered
	 */
    protected int count = COUNT_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected DescribeEOCoverageSetTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
		return Wcs20Package.Literals.DESCRIBE_EO_COVERAGE_SET_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<String> getEoId() {
		if (eoId == null) {
			eoId = new EDataTypeEList<String>(String.class, this, Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__EO_ID);
		}
		return eoId;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getContainmentType() {
		return containmentType;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setContainmentType(String newContainmentType) {
		String oldContainmentType = containmentType;
		containmentType = newContainmentType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__CONTAINMENT_TYPE, oldContainmentType, containmentType));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Sections getSections() {
		return sections;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSections(Sections newSections, NotificationChain msgs) {
		Sections oldSections = sections;
		sections = newSections;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__SECTIONS, oldSections, newSections);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSections(Sections newSections) {
		if (newSections != sections) {
			NotificationChain msgs = null;
			if (sections != null)
				msgs = ((InternalEObject)sections).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__SECTIONS, null, msgs);
			if (newSections != null)
				msgs = ((InternalEObject)newSections).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__SECTIONS, null, msgs);
			msgs = basicSetSections(newSections, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__SECTIONS, newSections, newSections));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<DimensionTrimType> getDimensionTrim() {
		if (dimensionTrim == null) {
			dimensionTrim = new EObjectResolvingEList<DimensionTrimType>(DimensionTrimType.class, this, Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__DIMENSION_TRIM);
		}
		return dimensionTrim;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public int getCount() {
		return count;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCount(int newCount) {
		int oldCount = count;
		count = newCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__COUNT, oldCount, count));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__SECTIONS:
				return basicSetSections(null, msgs);
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
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__EO_ID:
				return getEoId();
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__CONTAINMENT_TYPE:
				return getContainmentType();
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__SECTIONS:
				return getSections();
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__DIMENSION_TRIM:
				return getDimensionTrim();
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__COUNT:
				return getCount();
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
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__EO_ID:
				getEoId().clear();
				getEoId().addAll((Collection<? extends String>)newValue);
				return;
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__CONTAINMENT_TYPE:
				setContainmentType((String)newValue);
				return;
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__SECTIONS:
				setSections((Sections)newValue);
				return;
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__DIMENSION_TRIM:
				getDimensionTrim().clear();
				getDimensionTrim().addAll((Collection<? extends DimensionTrimType>)newValue);
				return;
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__COUNT:
				setCount((Integer)newValue);
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
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__EO_ID:
				getEoId().clear();
				return;
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__CONTAINMENT_TYPE:
				setContainmentType(CONTAINMENT_TYPE_EDEFAULT);
				return;
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__SECTIONS:
				setSections((Sections)null);
				return;
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__DIMENSION_TRIM:
				getDimensionTrim().clear();
				return;
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__COUNT:
				setCount(COUNT_EDEFAULT);
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
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__EO_ID:
				return eoId != null && !eoId.isEmpty();
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__CONTAINMENT_TYPE:
				return CONTAINMENT_TYPE_EDEFAULT == null ? containmentType != null : !CONTAINMENT_TYPE_EDEFAULT.equals(containmentType);
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__SECTIONS:
				return sections != null;
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__DIMENSION_TRIM:
				return dimensionTrim != null && !dimensionTrim.isEmpty();
			case Wcs20Package.DESCRIBE_EO_COVERAGE_SET_TYPE__COUNT:
				return count != COUNT_EDEFAULT;
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (eoId: ");
		result.append(eoId);
		result.append(", containmentType: ");
		result.append(containmentType);
		result.append(", count: ");
		result.append(count);
		result.append(')');
		return result.toString();
	}

} //DescribeEOCoverageSetTypeImpl
