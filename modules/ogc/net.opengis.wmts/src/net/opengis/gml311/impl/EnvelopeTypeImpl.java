/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import java.util.Collection;
import java.util.List;

import net.opengis.gml311.CoordType;
import net.opengis.gml311.CoordinatesType;
import net.opengis.gml311.DirectPositionType;
import net.opengis.gml311.EnvelopeType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Envelope Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.EnvelopeTypeImpl#getLowerCorner <em>Lower Corner</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.EnvelopeTypeImpl#getUpperCorner <em>Upper Corner</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.EnvelopeTypeImpl#getCoord <em>Coord</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.EnvelopeTypeImpl#getPos <em>Pos</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.EnvelopeTypeImpl#getCoordinates <em>Coordinates</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.EnvelopeTypeImpl#getAxisLabels <em>Axis Labels</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.EnvelopeTypeImpl#getSrsDimension <em>Srs Dimension</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.EnvelopeTypeImpl#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.EnvelopeTypeImpl#getUomLabels <em>Uom Labels</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnvelopeTypeImpl extends MinimalEObjectImpl.Container implements EnvelopeType {
    /**
     * The cached value of the '{@link #getLowerCorner() <em>Lower Corner</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLowerCorner()
     * @generated
     * @ordered
     */
    protected DirectPositionType lowerCorner;

    /**
     * The cached value of the '{@link #getUpperCorner() <em>Upper Corner</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUpperCorner()
     * @generated
     * @ordered
     */
    protected DirectPositionType upperCorner;

    /**
     * The cached value of the '{@link #getCoord() <em>Coord</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoord()
     * @generated
     * @ordered
     */
    protected EList<CoordType> coord;

    /**
     * The cached value of the '{@link #getPos() <em>Pos</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPos()
     * @generated
     * @ordered
     */
    protected EList<DirectPositionType> pos;

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
     * The default value of the '{@link #getAxisLabels() <em>Axis Labels</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAxisLabels()
     * @generated
     * @ordered
     */
    protected static final List<String> AXIS_LABELS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAxisLabels() <em>Axis Labels</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAxisLabels()
     * @generated
     * @ordered
     */
    protected List<String> axisLabels = AXIS_LABELS_EDEFAULT;

    /**
     * The default value of the '{@link #getSrsDimension() <em>Srs Dimension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsDimension()
     * @generated
     * @ordered
     */
    protected static final BigInteger SRS_DIMENSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSrsDimension() <em>Srs Dimension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsDimension()
     * @generated
     * @ordered
     */
    protected BigInteger srsDimension = SRS_DIMENSION_EDEFAULT;

    /**
     * The default value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected static final String SRS_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected String srsName = SRS_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getUomLabels() <em>Uom Labels</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUomLabels()
     * @generated
     * @ordered
     */
    protected static final List<String> UOM_LABELS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUomLabels() <em>Uom Labels</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUomLabels()
     * @generated
     * @ordered
     */
    protected List<String> uomLabels = UOM_LABELS_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EnvelopeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getEnvelopeType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectPositionType getLowerCorner() {
        return lowerCorner;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLowerCorner(DirectPositionType newLowerCorner, NotificationChain msgs) {
        DirectPositionType oldLowerCorner = lowerCorner;
        lowerCorner = newLowerCorner;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ENVELOPE_TYPE__LOWER_CORNER, oldLowerCorner, newLowerCorner);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLowerCorner(DirectPositionType newLowerCorner) {
        if (newLowerCorner != lowerCorner) {
            NotificationChain msgs = null;
            if (lowerCorner != null)
                msgs = ((InternalEObject)lowerCorner).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ENVELOPE_TYPE__LOWER_CORNER, null, msgs);
            if (newLowerCorner != null)
                msgs = ((InternalEObject)newLowerCorner).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ENVELOPE_TYPE__LOWER_CORNER, null, msgs);
            msgs = basicSetLowerCorner(newLowerCorner, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ENVELOPE_TYPE__LOWER_CORNER, newLowerCorner, newLowerCorner));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectPositionType getUpperCorner() {
        return upperCorner;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUpperCorner(DirectPositionType newUpperCorner, NotificationChain msgs) {
        DirectPositionType oldUpperCorner = upperCorner;
        upperCorner = newUpperCorner;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ENVELOPE_TYPE__UPPER_CORNER, oldUpperCorner, newUpperCorner);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUpperCorner(DirectPositionType newUpperCorner) {
        if (newUpperCorner != upperCorner) {
            NotificationChain msgs = null;
            if (upperCorner != null)
                msgs = ((InternalEObject)upperCorner).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ENVELOPE_TYPE__UPPER_CORNER, null, msgs);
            if (newUpperCorner != null)
                msgs = ((InternalEObject)newUpperCorner).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ENVELOPE_TYPE__UPPER_CORNER, null, msgs);
            msgs = basicSetUpperCorner(newUpperCorner, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ENVELOPE_TYPE__UPPER_CORNER, newUpperCorner, newUpperCorner));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<CoordType> getCoord() {
        if (coord == null) {
            coord = new EObjectContainmentEList<CoordType>(CoordType.class, this, Gml311Package.ENVELOPE_TYPE__COORD);
        }
        return coord;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DirectPositionType> getPos() {
        if (pos == null) {
            pos = new EObjectContainmentEList<DirectPositionType>(DirectPositionType.class, this, Gml311Package.ENVELOPE_TYPE__POS);
        }
        return pos;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ENVELOPE_TYPE__COORDINATES, oldCoordinates, newCoordinates);
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
                msgs = ((InternalEObject)coordinates).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ENVELOPE_TYPE__COORDINATES, null, msgs);
            if (newCoordinates != null)
                msgs = ((InternalEObject)newCoordinates).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ENVELOPE_TYPE__COORDINATES, null, msgs);
            msgs = basicSetCoordinates(newCoordinates, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ENVELOPE_TYPE__COORDINATES, newCoordinates, newCoordinates));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<String> getAxisLabels() {
        return axisLabels;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAxisLabels(List<String> newAxisLabels) {
        List<String> oldAxisLabels = axisLabels;
        axisLabels = newAxisLabels;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ENVELOPE_TYPE__AXIS_LABELS, oldAxisLabels, axisLabels));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getSrsDimension() {
        return srsDimension;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSrsDimension(BigInteger newSrsDimension) {
        BigInteger oldSrsDimension = srsDimension;
        srsDimension = newSrsDimension;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ENVELOPE_TYPE__SRS_DIMENSION, oldSrsDimension, srsDimension));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSrsName() {
        return srsName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSrsName(String newSrsName) {
        String oldSrsName = srsName;
        srsName = newSrsName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ENVELOPE_TYPE__SRS_NAME, oldSrsName, srsName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<String> getUomLabels() {
        return uomLabels;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUomLabels(List<String> newUomLabels) {
        List<String> oldUomLabels = uomLabels;
        uomLabels = newUomLabels;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ENVELOPE_TYPE__UOM_LABELS, oldUomLabels, uomLabels));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ENVELOPE_TYPE__LOWER_CORNER:
                return basicSetLowerCorner(null, msgs);
            case Gml311Package.ENVELOPE_TYPE__UPPER_CORNER:
                return basicSetUpperCorner(null, msgs);
            case Gml311Package.ENVELOPE_TYPE__COORD:
                return ((InternalEList<?>)getCoord()).basicRemove(otherEnd, msgs);
            case Gml311Package.ENVELOPE_TYPE__POS:
                return ((InternalEList<?>)getPos()).basicRemove(otherEnd, msgs);
            case Gml311Package.ENVELOPE_TYPE__COORDINATES:
                return basicSetCoordinates(null, msgs);
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
            case Gml311Package.ENVELOPE_TYPE__LOWER_CORNER:
                return getLowerCorner();
            case Gml311Package.ENVELOPE_TYPE__UPPER_CORNER:
                return getUpperCorner();
            case Gml311Package.ENVELOPE_TYPE__COORD:
                return getCoord();
            case Gml311Package.ENVELOPE_TYPE__POS:
                return getPos();
            case Gml311Package.ENVELOPE_TYPE__COORDINATES:
                return getCoordinates();
            case Gml311Package.ENVELOPE_TYPE__AXIS_LABELS:
                return getAxisLabels();
            case Gml311Package.ENVELOPE_TYPE__SRS_DIMENSION:
                return getSrsDimension();
            case Gml311Package.ENVELOPE_TYPE__SRS_NAME:
                return getSrsName();
            case Gml311Package.ENVELOPE_TYPE__UOM_LABELS:
                return getUomLabels();
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
            case Gml311Package.ENVELOPE_TYPE__LOWER_CORNER:
                setLowerCorner((DirectPositionType)newValue);
                return;
            case Gml311Package.ENVELOPE_TYPE__UPPER_CORNER:
                setUpperCorner((DirectPositionType)newValue);
                return;
            case Gml311Package.ENVELOPE_TYPE__COORD:
                getCoord().clear();
                getCoord().addAll((Collection<? extends CoordType>)newValue);
                return;
            case Gml311Package.ENVELOPE_TYPE__POS:
                getPos().clear();
                getPos().addAll((Collection<? extends DirectPositionType>)newValue);
                return;
            case Gml311Package.ENVELOPE_TYPE__COORDINATES:
                setCoordinates((CoordinatesType)newValue);
                return;
            case Gml311Package.ENVELOPE_TYPE__AXIS_LABELS:
                setAxisLabels((List<String>)newValue);
                return;
            case Gml311Package.ENVELOPE_TYPE__SRS_DIMENSION:
                setSrsDimension((BigInteger)newValue);
                return;
            case Gml311Package.ENVELOPE_TYPE__SRS_NAME:
                setSrsName((String)newValue);
                return;
            case Gml311Package.ENVELOPE_TYPE__UOM_LABELS:
                setUomLabels((List<String>)newValue);
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
            case Gml311Package.ENVELOPE_TYPE__LOWER_CORNER:
                setLowerCorner((DirectPositionType)null);
                return;
            case Gml311Package.ENVELOPE_TYPE__UPPER_CORNER:
                setUpperCorner((DirectPositionType)null);
                return;
            case Gml311Package.ENVELOPE_TYPE__COORD:
                getCoord().clear();
                return;
            case Gml311Package.ENVELOPE_TYPE__POS:
                getPos().clear();
                return;
            case Gml311Package.ENVELOPE_TYPE__COORDINATES:
                setCoordinates((CoordinatesType)null);
                return;
            case Gml311Package.ENVELOPE_TYPE__AXIS_LABELS:
                setAxisLabels(AXIS_LABELS_EDEFAULT);
                return;
            case Gml311Package.ENVELOPE_TYPE__SRS_DIMENSION:
                setSrsDimension(SRS_DIMENSION_EDEFAULT);
                return;
            case Gml311Package.ENVELOPE_TYPE__SRS_NAME:
                setSrsName(SRS_NAME_EDEFAULT);
                return;
            case Gml311Package.ENVELOPE_TYPE__UOM_LABELS:
                setUomLabels(UOM_LABELS_EDEFAULT);
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
            case Gml311Package.ENVELOPE_TYPE__LOWER_CORNER:
                return lowerCorner != null;
            case Gml311Package.ENVELOPE_TYPE__UPPER_CORNER:
                return upperCorner != null;
            case Gml311Package.ENVELOPE_TYPE__COORD:
                return coord != null && !coord.isEmpty();
            case Gml311Package.ENVELOPE_TYPE__POS:
                return pos != null && !pos.isEmpty();
            case Gml311Package.ENVELOPE_TYPE__COORDINATES:
                return coordinates != null;
            case Gml311Package.ENVELOPE_TYPE__AXIS_LABELS:
                return AXIS_LABELS_EDEFAULT == null ? axisLabels != null : !AXIS_LABELS_EDEFAULT.equals(axisLabels);
            case Gml311Package.ENVELOPE_TYPE__SRS_DIMENSION:
                return SRS_DIMENSION_EDEFAULT == null ? srsDimension != null : !SRS_DIMENSION_EDEFAULT.equals(srsDimension);
            case Gml311Package.ENVELOPE_TYPE__SRS_NAME:
                return SRS_NAME_EDEFAULT == null ? srsName != null : !SRS_NAME_EDEFAULT.equals(srsName);
            case Gml311Package.ENVELOPE_TYPE__UOM_LABELS:
                return UOM_LABELS_EDEFAULT == null ? uomLabels != null : !UOM_LABELS_EDEFAULT.equals(uomLabels);
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
        result.append(" (axisLabels: ");
        result.append(axisLabels);
        result.append(", srsDimension: ");
        result.append(srsDimension);
        result.append(", srsName: ");
        result.append(srsName);
        result.append(", uomLabels: ");
        result.append(uomLabels);
        result.append(')');
        return result.toString();
    }

} //EnvelopeTypeImpl
