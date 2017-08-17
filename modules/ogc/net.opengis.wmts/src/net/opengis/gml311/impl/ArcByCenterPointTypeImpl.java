/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import net.opengis.gml311.AngleType;
import net.opengis.gml311.ArcByCenterPointType;
import net.opengis.gml311.CoordinatesType;
import net.opengis.gml311.CurveInterpolationType;
import net.opengis.gml311.DirectPositionListType;
import net.opengis.gml311.DirectPositionType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.LengthType;
import net.opengis.gml311.PointPropertyType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Arc By Center Point Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.ArcByCenterPointTypeImpl#getPos <em>Pos</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ArcByCenterPointTypeImpl#getPointProperty <em>Point Property</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ArcByCenterPointTypeImpl#getPointRep <em>Point Rep</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ArcByCenterPointTypeImpl#getPosList <em>Pos List</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ArcByCenterPointTypeImpl#getCoordinates <em>Coordinates</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ArcByCenterPointTypeImpl#getRadius <em>Radius</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ArcByCenterPointTypeImpl#getStartAngle <em>Start Angle</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ArcByCenterPointTypeImpl#getEndAngle <em>End Angle</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ArcByCenterPointTypeImpl#getInterpolation <em>Interpolation</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ArcByCenterPointTypeImpl#getNumArc <em>Num Arc</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ArcByCenterPointTypeImpl extends AbstractCurveSegmentTypeImpl implements ArcByCenterPointType {
    /**
     * The cached value of the '{@link #getPos() <em>Pos</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPos()
     * @generated
     * @ordered
     */
    protected DirectPositionType pos;

    /**
     * The cached value of the '{@link #getPointProperty() <em>Point Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPointProperty()
     * @generated
     * @ordered
     */
    protected PointPropertyType pointProperty;

    /**
     * The cached value of the '{@link #getPointRep() <em>Point Rep</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPointRep()
     * @generated
     * @ordered
     */
    protected PointPropertyType pointRep;

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
     * The cached value of the '{@link #getRadius() <em>Radius</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRadius()
     * @generated
     * @ordered
     */
    protected LengthType radius;

    /**
     * The cached value of the '{@link #getStartAngle() <em>Start Angle</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartAngle()
     * @generated
     * @ordered
     */
    protected AngleType startAngle;

    /**
     * The cached value of the '{@link #getEndAngle() <em>End Angle</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndAngle()
     * @generated
     * @ordered
     */
    protected AngleType endAngle;

    /**
     * The default value of the '{@link #getInterpolation() <em>Interpolation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolation()
     * @generated
     * @ordered
     */
    protected static final CurveInterpolationType INTERPOLATION_EDEFAULT = CurveInterpolationType.CIRCULAR_ARC_CENTER_POINT_WITH_RADIUS;

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
     * The default value of the '{@link #getNumArc() <em>Num Arc</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumArc()
     * @generated
     * @ordered
     */
    protected static final BigInteger NUM_ARC_EDEFAULT = new BigInteger("1");

    /**
     * The cached value of the '{@link #getNumArc() <em>Num Arc</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumArc()
     * @generated
     * @ordered
     */
    protected BigInteger numArc = NUM_ARC_EDEFAULT;

    /**
     * This is true if the Num Arc attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean numArcESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ArcByCenterPointTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getArcByCenterPointType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectPositionType getPos() {
        return pos;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPos(DirectPositionType newPos, NotificationChain msgs) {
        DirectPositionType oldPos = pos;
        pos = newPos;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS, oldPos, newPos);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPos(DirectPositionType newPos) {
        if (newPos != pos) {
            NotificationChain msgs = null;
            if (pos != null)
                msgs = ((InternalEObject)pos).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS, null, msgs);
            if (newPos != null)
                msgs = ((InternalEObject)newPos).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS, null, msgs);
            msgs = basicSetPos(newPos, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS, newPos, newPos));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PointPropertyType getPointProperty() {
        return pointProperty;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPointProperty(PointPropertyType newPointProperty, NotificationChain msgs) {
        PointPropertyType oldPointProperty = pointProperty;
        pointProperty = newPointProperty;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_PROPERTY, oldPointProperty, newPointProperty);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPointProperty(PointPropertyType newPointProperty) {
        if (newPointProperty != pointProperty) {
            NotificationChain msgs = null;
            if (pointProperty != null)
                msgs = ((InternalEObject)pointProperty).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_PROPERTY, null, msgs);
            if (newPointProperty != null)
                msgs = ((InternalEObject)newPointProperty).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_PROPERTY, null, msgs);
            msgs = basicSetPointProperty(newPointProperty, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_PROPERTY, newPointProperty, newPointProperty));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PointPropertyType getPointRep() {
        return pointRep;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPointRep(PointPropertyType newPointRep, NotificationChain msgs) {
        PointPropertyType oldPointRep = pointRep;
        pointRep = newPointRep;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_REP, oldPointRep, newPointRep);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPointRep(PointPropertyType newPointRep) {
        if (newPointRep != pointRep) {
            NotificationChain msgs = null;
            if (pointRep != null)
                msgs = ((InternalEObject)pointRep).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_REP, null, msgs);
            if (newPointRep != null)
                msgs = ((InternalEObject)newPointRep).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_REP, null, msgs);
            msgs = basicSetPointRep(newPointRep, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_REP, newPointRep, newPointRep));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS_LIST, oldPosList, newPosList);
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
                msgs = ((InternalEObject)posList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS_LIST, null, msgs);
            if (newPosList != null)
                msgs = ((InternalEObject)newPosList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS_LIST, null, msgs);
            msgs = basicSetPosList(newPosList, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS_LIST, newPosList, newPosList));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__COORDINATES, oldCoordinates, newCoordinates);
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
                msgs = ((InternalEObject)coordinates).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__COORDINATES, null, msgs);
            if (newCoordinates != null)
                msgs = ((InternalEObject)newCoordinates).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__COORDINATES, null, msgs);
            msgs = basicSetCoordinates(newCoordinates, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__COORDINATES, newCoordinates, newCoordinates));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LengthType getRadius() {
        return radius;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRadius(LengthType newRadius, NotificationChain msgs) {
        LengthType oldRadius = radius;
        radius = newRadius;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__RADIUS, oldRadius, newRadius);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRadius(LengthType newRadius) {
        if (newRadius != radius) {
            NotificationChain msgs = null;
            if (radius != null)
                msgs = ((InternalEObject)radius).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__RADIUS, null, msgs);
            if (newRadius != null)
                msgs = ((InternalEObject)newRadius).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__RADIUS, null, msgs);
            msgs = basicSetRadius(newRadius, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__RADIUS, newRadius, newRadius));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AngleType getStartAngle() {
        return startAngle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetStartAngle(AngleType newStartAngle, NotificationChain msgs) {
        AngleType oldStartAngle = startAngle;
        startAngle = newStartAngle;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__START_ANGLE, oldStartAngle, newStartAngle);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStartAngle(AngleType newStartAngle) {
        if (newStartAngle != startAngle) {
            NotificationChain msgs = null;
            if (startAngle != null)
                msgs = ((InternalEObject)startAngle).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__START_ANGLE, null, msgs);
            if (newStartAngle != null)
                msgs = ((InternalEObject)newStartAngle).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__START_ANGLE, null, msgs);
            msgs = basicSetStartAngle(newStartAngle, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__START_ANGLE, newStartAngle, newStartAngle));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AngleType getEndAngle() {
        return endAngle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetEndAngle(AngleType newEndAngle, NotificationChain msgs) {
        AngleType oldEndAngle = endAngle;
        endAngle = newEndAngle;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__END_ANGLE, oldEndAngle, newEndAngle);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEndAngle(AngleType newEndAngle) {
        if (newEndAngle != endAngle) {
            NotificationChain msgs = null;
            if (endAngle != null)
                msgs = ((InternalEObject)endAngle).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__END_ANGLE, null, msgs);
            if (newEndAngle != null)
                msgs = ((InternalEObject)newEndAngle).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ARC_BY_CENTER_POINT_TYPE__END_ANGLE, null, msgs);
            msgs = basicSetEndAngle(newEndAngle, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__END_ANGLE, newEndAngle, newEndAngle));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__INTERPOLATION, oldInterpolation, interpolation, !oldInterpolationESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__INTERPOLATION, oldInterpolation, INTERPOLATION_EDEFAULT, oldInterpolationESet));
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
    public BigInteger getNumArc() {
        return numArc;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNumArc(BigInteger newNumArc) {
        BigInteger oldNumArc = numArc;
        numArc = newNumArc;
        boolean oldNumArcESet = numArcESet;
        numArcESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__NUM_ARC, oldNumArc, numArc, !oldNumArcESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetNumArc() {
        BigInteger oldNumArc = numArc;
        boolean oldNumArcESet = numArcESet;
        numArc = NUM_ARC_EDEFAULT;
        numArcESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.ARC_BY_CENTER_POINT_TYPE__NUM_ARC, oldNumArc, NUM_ARC_EDEFAULT, oldNumArcESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetNumArc() {
        return numArcESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS:
                return basicSetPos(null, msgs);
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_PROPERTY:
                return basicSetPointProperty(null, msgs);
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_REP:
                return basicSetPointRep(null, msgs);
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS_LIST:
                return basicSetPosList(null, msgs);
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__COORDINATES:
                return basicSetCoordinates(null, msgs);
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__RADIUS:
                return basicSetRadius(null, msgs);
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__START_ANGLE:
                return basicSetStartAngle(null, msgs);
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__END_ANGLE:
                return basicSetEndAngle(null, msgs);
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
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS:
                return getPos();
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_PROPERTY:
                return getPointProperty();
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_REP:
                return getPointRep();
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS_LIST:
                return getPosList();
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__COORDINATES:
                return getCoordinates();
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__RADIUS:
                return getRadius();
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__START_ANGLE:
                return getStartAngle();
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__END_ANGLE:
                return getEndAngle();
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__INTERPOLATION:
                return getInterpolation();
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__NUM_ARC:
                return getNumArc();
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
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS:
                setPos((DirectPositionType)newValue);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_PROPERTY:
                setPointProperty((PointPropertyType)newValue);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_REP:
                setPointRep((PointPropertyType)newValue);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS_LIST:
                setPosList((DirectPositionListType)newValue);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__COORDINATES:
                setCoordinates((CoordinatesType)newValue);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__RADIUS:
                setRadius((LengthType)newValue);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__START_ANGLE:
                setStartAngle((AngleType)newValue);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__END_ANGLE:
                setEndAngle((AngleType)newValue);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__INTERPOLATION:
                setInterpolation((CurveInterpolationType)newValue);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__NUM_ARC:
                setNumArc((BigInteger)newValue);
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
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS:
                setPos((DirectPositionType)null);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_PROPERTY:
                setPointProperty((PointPropertyType)null);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_REP:
                setPointRep((PointPropertyType)null);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS_LIST:
                setPosList((DirectPositionListType)null);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__COORDINATES:
                setCoordinates((CoordinatesType)null);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__RADIUS:
                setRadius((LengthType)null);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__START_ANGLE:
                setStartAngle((AngleType)null);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__END_ANGLE:
                setEndAngle((AngleType)null);
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__INTERPOLATION:
                unsetInterpolation();
                return;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__NUM_ARC:
                unsetNumArc();
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
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS:
                return pos != null;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_PROPERTY:
                return pointProperty != null;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POINT_REP:
                return pointRep != null;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__POS_LIST:
                return posList != null;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__COORDINATES:
                return coordinates != null;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__RADIUS:
                return radius != null;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__START_ANGLE:
                return startAngle != null;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__END_ANGLE:
                return endAngle != null;
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__INTERPOLATION:
                return isSetInterpolation();
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE__NUM_ARC:
                return isSetNumArc();
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
        result.append(" (interpolation: ");
        if (interpolationESet) result.append(interpolation); else result.append("<unset>");
        result.append(", numArc: ");
        if (numArcESet) result.append(numArc); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //ArcByCenterPointTypeImpl
