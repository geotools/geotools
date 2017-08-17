/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import java.util.Collection;

import net.opengis.gml311.BSplineType;
import net.opengis.gml311.CoordinatesType;
import net.opengis.gml311.CurveInterpolationType;
import net.opengis.gml311.DirectPositionListType;
import net.opengis.gml311.DirectPositionType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.KnotPropertyType;
import net.opengis.gml311.KnotTypesType;
import net.opengis.gml311.PointPropertyType;

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

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>BSpline Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.BSplineTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BSplineTypeImpl#getPos <em>Pos</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BSplineTypeImpl#getPointProperty <em>Point Property</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BSplineTypeImpl#getPointRep <em>Point Rep</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BSplineTypeImpl#getPosList <em>Pos List</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BSplineTypeImpl#getCoordinates <em>Coordinates</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BSplineTypeImpl#getDegree <em>Degree</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BSplineTypeImpl#getKnot <em>Knot</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BSplineTypeImpl#getInterpolation <em>Interpolation</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BSplineTypeImpl#isIsPolynomial <em>Is Polynomial</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BSplineTypeImpl#getKnotType <em>Knot Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BSplineTypeImpl extends AbstractCurveSegmentTypeImpl implements BSplineType {
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
     * The cached value of the '{@link #getPosList() <em>Pos List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPosList()
     * @generated
     * @ordered
     */
    protected DirectPositionListType posList;

    /**
     * The cached value of the '{@link #getCoordinates() <em>Coordinates</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoordinates()
     * @generated
     * @ordered
     */
    protected CoordinatesType coordinates;

    /**
     * The default value of the '{@link #getDegree() <em>Degree</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDegree()
     * @generated
     * @ordered
     */
    protected static final BigInteger DEGREE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDegree() <em>Degree</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDegree()
     * @generated
     * @ordered
     */
    protected BigInteger degree = DEGREE_EDEFAULT;

    /**
     * The cached value of the '{@link #getKnot() <em>Knot</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKnot()
     * @generated
     * @ordered
     */
    protected EList<KnotPropertyType> knot;

    /**
     * The default value of the '{@link #getInterpolation() <em>Interpolation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolation()
     * @generated
     * @ordered
     */
    protected static final CurveInterpolationType INTERPOLATION_EDEFAULT = CurveInterpolationType.POLYNOMIAL_SPLINE;

    /**
     * The cached value of the '{@link #getInterpolation() <em>Interpolation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolation()
     * @generated
     * @ordered
     */
    protected CurveInterpolationType interpolation = INTERPOLATION_EDEFAULT;

    /**
     * This is true if the Interpolation attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean interpolationESet;

    /**
     * The default value of the '{@link #isIsPolynomial() <em>Is Polynomial</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsPolynomial()
     * @generated
     * @ordered
     */
    protected static final boolean IS_POLYNOMIAL_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsPolynomial() <em>Is Polynomial</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsPolynomial()
     * @generated
     * @ordered
     */
    protected boolean isPolynomial = IS_POLYNOMIAL_EDEFAULT;

    /**
     * This is true if the Is Polynomial attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean isPolynomialESet;

    /**
     * The default value of the '{@link #getKnotType() <em>Knot Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKnotType()
     * @generated
     * @ordered
     */
    protected static final KnotTypesType KNOT_TYPE_EDEFAULT = KnotTypesType.UNIFORM;

    /**
     * The cached value of the '{@link #getKnotType() <em>Knot Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKnotType()
     * @generated
     * @ordered
     */
    protected KnotTypesType knotType = KNOT_TYPE_EDEFAULT;

    /**
     * This is true if the Knot Type attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean knotTypeESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected BSplineTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getBSplineType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getGroup() {
        if (group == null) {
            group = new BasicFeatureMap(this, Gml311Package.BSPLINE_TYPE__GROUP);
        }
        return group;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DirectPositionType> getPos() {
        return getGroup().list(Gml311Package.eINSTANCE.getBSplineType_Pos());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<PointPropertyType> getPointProperty() {
        return getGroup().list(Gml311Package.eINSTANCE.getBSplineType_PointProperty());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<PointPropertyType> getPointRep() {
        return getGroup().list(Gml311Package.eINSTANCE.getBSplineType_PointRep());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectPositionListType getPosList() {
        return posList;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPosList(DirectPositionListType newPosList, NotificationChain msgs) {
        DirectPositionListType oldPosList = posList;
        posList = newPosList;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.BSPLINE_TYPE__POS_LIST, oldPosList, newPosList);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPosList(DirectPositionListType newPosList) {
        if (newPosList != posList) {
            NotificationChain msgs = null;
            if (posList != null)
                msgs = ((InternalEObject)posList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.BSPLINE_TYPE__POS_LIST, null, msgs);
            if (newPosList != null)
                msgs = ((InternalEObject)newPosList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.BSPLINE_TYPE__POS_LIST, null, msgs);
            msgs = basicSetPosList(newPosList, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.BSPLINE_TYPE__POS_LIST, newPosList, newPosList));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoordinatesType getCoordinates() {
        return coordinates;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoordinates(CoordinatesType newCoordinates, NotificationChain msgs) {
        CoordinatesType oldCoordinates = coordinates;
        coordinates = newCoordinates;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.BSPLINE_TYPE__COORDINATES, oldCoordinates, newCoordinates);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoordinates(CoordinatesType newCoordinates) {
        if (newCoordinates != coordinates) {
            NotificationChain msgs = null;
            if (coordinates != null)
                msgs = ((InternalEObject)coordinates).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.BSPLINE_TYPE__COORDINATES, null, msgs);
            if (newCoordinates != null)
                msgs = ((InternalEObject)newCoordinates).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.BSPLINE_TYPE__COORDINATES, null, msgs);
            msgs = basicSetCoordinates(newCoordinates, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.BSPLINE_TYPE__COORDINATES, newCoordinates, newCoordinates));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getDegree() {
        return degree;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDegree(BigInteger newDegree) {
        BigInteger oldDegree = degree;
        degree = newDegree;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.BSPLINE_TYPE__DEGREE, oldDegree, degree));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<KnotPropertyType> getKnot() {
        if (knot == null) {
            knot = new EObjectContainmentEList<KnotPropertyType>(KnotPropertyType.class, this, Gml311Package.BSPLINE_TYPE__KNOT);
        }
        return knot;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CurveInterpolationType getInterpolation() {
        return interpolation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInterpolation(CurveInterpolationType newInterpolation) {
        CurveInterpolationType oldInterpolation = interpolation;
        interpolation = newInterpolation == null ? INTERPOLATION_EDEFAULT : newInterpolation;
        boolean oldInterpolationESet = interpolationESet;
        interpolationESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.BSPLINE_TYPE__INTERPOLATION, oldInterpolation, interpolation, !oldInterpolationESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetInterpolation() {
        CurveInterpolationType oldInterpolation = interpolation;
        boolean oldInterpolationESet = interpolationESet;
        interpolation = INTERPOLATION_EDEFAULT;
        interpolationESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.BSPLINE_TYPE__INTERPOLATION, oldInterpolation, INTERPOLATION_EDEFAULT, oldInterpolationESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetInterpolation() {
        return interpolationESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsPolynomial() {
        return isPolynomial;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsPolynomial(boolean newIsPolynomial) {
        boolean oldIsPolynomial = isPolynomial;
        isPolynomial = newIsPolynomial;
        boolean oldIsPolynomialESet = isPolynomialESet;
        isPolynomialESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.BSPLINE_TYPE__IS_POLYNOMIAL, oldIsPolynomial, isPolynomial, !oldIsPolynomialESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetIsPolynomial() {
        boolean oldIsPolynomial = isPolynomial;
        boolean oldIsPolynomialESet = isPolynomialESet;
        isPolynomial = IS_POLYNOMIAL_EDEFAULT;
        isPolynomialESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.BSPLINE_TYPE__IS_POLYNOMIAL, oldIsPolynomial, IS_POLYNOMIAL_EDEFAULT, oldIsPolynomialESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetIsPolynomial() {
        return isPolynomialESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public KnotTypesType getKnotType() {
        return knotType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setKnotType(KnotTypesType newKnotType) {
        KnotTypesType oldKnotType = knotType;
        knotType = newKnotType == null ? KNOT_TYPE_EDEFAULT : newKnotType;
        boolean oldKnotTypeESet = knotTypeESet;
        knotTypeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.BSPLINE_TYPE__KNOT_TYPE, oldKnotType, knotType, !oldKnotTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetKnotType() {
        KnotTypesType oldKnotType = knotType;
        boolean oldKnotTypeESet = knotTypeESet;
        knotType = KNOT_TYPE_EDEFAULT;
        knotTypeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.BSPLINE_TYPE__KNOT_TYPE, oldKnotType, KNOT_TYPE_EDEFAULT, oldKnotTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetKnotType() {
        return knotTypeESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.BSPLINE_TYPE__GROUP:
                return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.BSPLINE_TYPE__POS:
                return ((InternalEList<?>)getPos()).basicRemove(otherEnd, msgs);
            case Gml311Package.BSPLINE_TYPE__POINT_PROPERTY:
                return ((InternalEList<?>)getPointProperty()).basicRemove(otherEnd, msgs);
            case Gml311Package.BSPLINE_TYPE__POINT_REP:
                return ((InternalEList<?>)getPointRep()).basicRemove(otherEnd, msgs);
            case Gml311Package.BSPLINE_TYPE__POS_LIST:
                return basicSetPosList(null, msgs);
            case Gml311Package.BSPLINE_TYPE__COORDINATES:
                return basicSetCoordinates(null, msgs);
            case Gml311Package.BSPLINE_TYPE__KNOT:
                return ((InternalEList<?>)getKnot()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.BSPLINE_TYPE__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal)getGroup()).getWrapper();
            case Gml311Package.BSPLINE_TYPE__POS:
                return getPos();
            case Gml311Package.BSPLINE_TYPE__POINT_PROPERTY:
                return getPointProperty();
            case Gml311Package.BSPLINE_TYPE__POINT_REP:
                return getPointRep();
            case Gml311Package.BSPLINE_TYPE__POS_LIST:
                return getPosList();
            case Gml311Package.BSPLINE_TYPE__COORDINATES:
                return getCoordinates();
            case Gml311Package.BSPLINE_TYPE__DEGREE:
                return getDegree();
            case Gml311Package.BSPLINE_TYPE__KNOT:
                return getKnot();
            case Gml311Package.BSPLINE_TYPE__INTERPOLATION:
                return getInterpolation();
            case Gml311Package.BSPLINE_TYPE__IS_POLYNOMIAL:
                return isIsPolynomial();
            case Gml311Package.BSPLINE_TYPE__KNOT_TYPE:
                return getKnotType();
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
            case Gml311Package.BSPLINE_TYPE__GROUP:
                ((FeatureMap.Internal)getGroup()).set(newValue);
                return;
            case Gml311Package.BSPLINE_TYPE__POS:
                getPos().clear();
                getPos().addAll((Collection<? extends DirectPositionType>)newValue);
                return;
            case Gml311Package.BSPLINE_TYPE__POINT_PROPERTY:
                getPointProperty().clear();
                getPointProperty().addAll((Collection<? extends PointPropertyType>)newValue);
                return;
            case Gml311Package.BSPLINE_TYPE__POINT_REP:
                getPointRep().clear();
                getPointRep().addAll((Collection<? extends PointPropertyType>)newValue);
                return;
            case Gml311Package.BSPLINE_TYPE__POS_LIST:
                setPosList((DirectPositionListType)newValue);
                return;
            case Gml311Package.BSPLINE_TYPE__COORDINATES:
                setCoordinates((CoordinatesType)newValue);
                return;
            case Gml311Package.BSPLINE_TYPE__DEGREE:
                setDegree((BigInteger)newValue);
                return;
            case Gml311Package.BSPLINE_TYPE__KNOT:
                getKnot().clear();
                getKnot().addAll((Collection<? extends KnotPropertyType>)newValue);
                return;
            case Gml311Package.BSPLINE_TYPE__INTERPOLATION:
                setInterpolation((CurveInterpolationType)newValue);
                return;
            case Gml311Package.BSPLINE_TYPE__IS_POLYNOMIAL:
                setIsPolynomial((Boolean)newValue);
                return;
            case Gml311Package.BSPLINE_TYPE__KNOT_TYPE:
                setKnotType((KnotTypesType)newValue);
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
            case Gml311Package.BSPLINE_TYPE__GROUP:
                getGroup().clear();
                return;
            case Gml311Package.BSPLINE_TYPE__POS:
                getPos().clear();
                return;
            case Gml311Package.BSPLINE_TYPE__POINT_PROPERTY:
                getPointProperty().clear();
                return;
            case Gml311Package.BSPLINE_TYPE__POINT_REP:
                getPointRep().clear();
                return;
            case Gml311Package.BSPLINE_TYPE__POS_LIST:
                setPosList((DirectPositionListType)null);
                return;
            case Gml311Package.BSPLINE_TYPE__COORDINATES:
                setCoordinates((CoordinatesType)null);
                return;
            case Gml311Package.BSPLINE_TYPE__DEGREE:
                setDegree(DEGREE_EDEFAULT);
                return;
            case Gml311Package.BSPLINE_TYPE__KNOT:
                getKnot().clear();
                return;
            case Gml311Package.BSPLINE_TYPE__INTERPOLATION:
                unsetInterpolation();
                return;
            case Gml311Package.BSPLINE_TYPE__IS_POLYNOMIAL:
                unsetIsPolynomial();
                return;
            case Gml311Package.BSPLINE_TYPE__KNOT_TYPE:
                unsetKnotType();
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
            case Gml311Package.BSPLINE_TYPE__GROUP:
                return group != null && !group.isEmpty();
            case Gml311Package.BSPLINE_TYPE__POS:
                return !getPos().isEmpty();
            case Gml311Package.BSPLINE_TYPE__POINT_PROPERTY:
                return !getPointProperty().isEmpty();
            case Gml311Package.BSPLINE_TYPE__POINT_REP:
                return !getPointRep().isEmpty();
            case Gml311Package.BSPLINE_TYPE__POS_LIST:
                return posList != null;
            case Gml311Package.BSPLINE_TYPE__COORDINATES:
                return coordinates != null;
            case Gml311Package.BSPLINE_TYPE__DEGREE:
                return DEGREE_EDEFAULT == null ? degree != null : !DEGREE_EDEFAULT.equals(degree);
            case Gml311Package.BSPLINE_TYPE__KNOT:
                return knot != null && !knot.isEmpty();
            case Gml311Package.BSPLINE_TYPE__INTERPOLATION:
                return isSetInterpolation();
            case Gml311Package.BSPLINE_TYPE__IS_POLYNOMIAL:
                return isSetIsPolynomial();
            case Gml311Package.BSPLINE_TYPE__KNOT_TYPE:
                return isSetKnotType();
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
        result.append(" (group: ");
        result.append(group);
        result.append(", degree: ");
        result.append(degree);
        result.append(", interpolation: ");
        if (interpolationESet) result.append(interpolation); else result.append("<unset>");
        result.append(", isPolynomial: ");
        if (isPolynomialESet) result.append(isPolynomial); else result.append("<unset>");
        result.append(", knotType: ");
        if (knotTypeESet) result.append(knotType); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //BSplineTypeImpl
