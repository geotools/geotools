/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import net.opengis.gml311.AbstractCoverageType;
import net.opengis.gml311.DomainSetType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.RangeSetType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoverageTypeImpl#getDomainSetGroup <em>Domain Set Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoverageTypeImpl#getDomainSet <em>Domain Set</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoverageTypeImpl#getRangeSet <em>Range Set</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoverageTypeImpl#getDimension <em>Dimension</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractCoverageTypeImpl extends AbstractFeatureTypeImpl implements AbstractCoverageType {
    /**
     * The cached value of the '{@link #getDomainSetGroup() <em>Domain Set Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDomainSetGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap domainSetGroup;

    /**
     * The cached value of the '{@link #getRangeSet() <em>Range Set</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeSet()
     * @generated
     * @ordered
     */
    protected RangeSetType rangeSet;

    /**
     * The default value of the '{@link #getDimension() <em>Dimension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDimension()
     * @generated
     * @ordered
     */
    protected static final BigInteger DIMENSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDimension() <em>Dimension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDimension()
     * @generated
     * @ordered
     */
    protected BigInteger dimension = DIMENSION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractCoverageTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractCoverageType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getDomainSetGroup() {
        if (domainSetGroup == null) {
            domainSetGroup = new BasicFeatureMap(this, Gml311Package.ABSTRACT_COVERAGE_TYPE__DOMAIN_SET_GROUP);
        }
        return domainSetGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainSetType getDomainSet() {
        return (DomainSetType)getDomainSetGroup().get(Gml311Package.eINSTANCE.getAbstractCoverageType_DomainSet(), true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDomainSet(DomainSetType newDomainSet, NotificationChain msgs) {
        return ((FeatureMap.Internal)getDomainSetGroup()).basicAdd(Gml311Package.eINSTANCE.getAbstractCoverageType_DomainSet(), newDomainSet, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDomainSet(DomainSetType newDomainSet) {
        ((FeatureMap.Internal)getDomainSetGroup()).set(Gml311Package.eINSTANCE.getAbstractCoverageType_DomainSet(), newDomainSet);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeSetType getRangeSet() {
        return rangeSet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRangeSet(RangeSetType newRangeSet, NotificationChain msgs) {
        RangeSetType oldRangeSet = rangeSet;
        rangeSet = newRangeSet;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COVERAGE_TYPE__RANGE_SET, oldRangeSet, newRangeSet);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRangeSet(RangeSetType newRangeSet) {
        if (newRangeSet != rangeSet) {
            NotificationChain msgs = null;
            if (rangeSet != null)
                msgs = ((InternalEObject)rangeSet).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_COVERAGE_TYPE__RANGE_SET, null, msgs);
            if (newRangeSet != null)
                msgs = ((InternalEObject)newRangeSet).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_COVERAGE_TYPE__RANGE_SET, null, msgs);
            msgs = basicSetRangeSet(newRangeSet, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COVERAGE_TYPE__RANGE_SET, newRangeSet, newRangeSet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getDimension() {
        return dimension;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDimension(BigInteger newDimension) {
        BigInteger oldDimension = dimension;
        dimension = newDimension;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COVERAGE_TYPE__DIMENSION, oldDimension, dimension));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__DOMAIN_SET_GROUP:
                return ((InternalEList<?>)getDomainSetGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__DOMAIN_SET:
                return basicSetDomainSet(null, msgs);
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__RANGE_SET:
                return basicSetRangeSet(null, msgs);
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
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__DOMAIN_SET_GROUP:
                if (coreType) return getDomainSetGroup();
                return ((FeatureMap.Internal)getDomainSetGroup()).getWrapper();
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__DOMAIN_SET:
                return getDomainSet();
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__RANGE_SET:
                return getRangeSet();
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__DIMENSION:
                return getDimension();
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
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__DOMAIN_SET_GROUP:
                ((FeatureMap.Internal)getDomainSetGroup()).set(newValue);
                return;
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__DOMAIN_SET:
                setDomainSet((DomainSetType)newValue);
                return;
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__RANGE_SET:
                setRangeSet((RangeSetType)newValue);
                return;
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__DIMENSION:
                setDimension((BigInteger)newValue);
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
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__DOMAIN_SET_GROUP:
                getDomainSetGroup().clear();
                return;
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__DOMAIN_SET:
                setDomainSet((DomainSetType)null);
                return;
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__RANGE_SET:
                setRangeSet((RangeSetType)null);
                return;
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__DIMENSION:
                setDimension(DIMENSION_EDEFAULT);
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
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__DOMAIN_SET_GROUP:
                return domainSetGroup != null && !domainSetGroup.isEmpty();
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__DOMAIN_SET:
                return getDomainSet() != null;
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__RANGE_SET:
                return rangeSet != null;
            case Gml311Package.ABSTRACT_COVERAGE_TYPE__DIMENSION:
                return DIMENSION_EDEFAULT == null ? dimension != null : !DIMENSION_EDEFAULT.equals(dimension);
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
        result.append(" (domainSetGroup: ");
        result.append(domainSetGroup);
        result.append(", dimension: ");
        result.append(dimension);
        result.append(')');
        return result.toString();
    }

} //AbstractCoverageTypeImpl
