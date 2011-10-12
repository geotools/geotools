/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import net.opengis.fes20.AvailableFunctionsType;
import net.opengis.fes20.ConformanceType;
import net.opengis.fes20.ExtendedCapabilitiesType;
import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.FilterCapabilitiesType;
import net.opengis.fes20.IdCapabilitiesType;
import net.opengis.fes20.ScalarCapabilitiesType;
import net.opengis.fes20.SpatialCapabilitiesType;
import net.opengis.fes20.TemporalCapabilitiesType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Filter Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.FilterCapabilitiesTypeImpl#getConformance <em>Conformance</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.FilterCapabilitiesTypeImpl#getIdCapabilities <em>Id Capabilities</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.FilterCapabilitiesTypeImpl#getScalarCapabilities <em>Scalar Capabilities</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.FilterCapabilitiesTypeImpl#getSpatialCapabilities <em>Spatial Capabilities</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.FilterCapabilitiesTypeImpl#getTemporalCapabilities <em>Temporal Capabilities</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.FilterCapabilitiesTypeImpl#getFunctions <em>Functions</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.FilterCapabilitiesTypeImpl#getExtendedCapabilities <em>Extended Capabilities</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FilterCapabilitiesTypeImpl extends EObjectImpl implements FilterCapabilitiesType {
    /**
     * The cached value of the '{@link #getConformance() <em>Conformance</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConformance()
     * @generated
     * @ordered
     */
    protected ConformanceType conformance;

    /**
     * The cached value of the '{@link #getIdCapabilities() <em>Id Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdCapabilities()
     * @generated
     * @ordered
     */
    protected IdCapabilitiesType idCapabilities;

    /**
     * The cached value of the '{@link #getScalarCapabilities() <em>Scalar Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScalarCapabilities()
     * @generated
     * @ordered
     */
    protected ScalarCapabilitiesType scalarCapabilities;

    /**
     * The cached value of the '{@link #getSpatialCapabilities() <em>Spatial Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSpatialCapabilities()
     * @generated
     * @ordered
     */
    protected SpatialCapabilitiesType spatialCapabilities;

    /**
     * The cached value of the '{@link #getTemporalCapabilities() <em>Temporal Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTemporalCapabilities()
     * @generated
     * @ordered
     */
    protected TemporalCapabilitiesType temporalCapabilities;

    /**
     * The cached value of the '{@link #getFunctions() <em>Functions</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFunctions()
     * @generated
     * @ordered
     */
    protected AvailableFunctionsType functions;

    /**
     * The cached value of the '{@link #getExtendedCapabilities() <em>Extended Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getExtendedCapabilities()
     * @generated
     * @ordered
     */
    protected ExtendedCapabilitiesType extendedCapabilities;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FilterCapabilitiesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.FILTER_CAPABILITIES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConformanceType getConformance() {
        return conformance;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetConformance(ConformanceType newConformance, NotificationChain msgs) {
        ConformanceType oldConformance = conformance;
        conformance = newConformance;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.FILTER_CAPABILITIES_TYPE__CONFORMANCE, oldConformance, newConformance);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setConformance(ConformanceType newConformance) {
        if (newConformance != conformance) {
            NotificationChain msgs = null;
            if (conformance != null)
                msgs = ((InternalEObject)conformance).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.FILTER_CAPABILITIES_TYPE__CONFORMANCE, null, msgs);
            if (newConformance != null)
                msgs = ((InternalEObject)newConformance).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.FILTER_CAPABILITIES_TYPE__CONFORMANCE, null, msgs);
            msgs = basicSetConformance(newConformance, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.FILTER_CAPABILITIES_TYPE__CONFORMANCE, newConformance, newConformance));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IdCapabilitiesType getIdCapabilities() {
        return idCapabilities;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetIdCapabilities(IdCapabilitiesType newIdCapabilities, NotificationChain msgs) {
        IdCapabilitiesType oldIdCapabilities = idCapabilities;
        idCapabilities = newIdCapabilities;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.FILTER_CAPABILITIES_TYPE__ID_CAPABILITIES, oldIdCapabilities, newIdCapabilities);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdCapabilities(IdCapabilitiesType newIdCapabilities) {
        if (newIdCapabilities != idCapabilities) {
            NotificationChain msgs = null;
            if (idCapabilities != null)
                msgs = ((InternalEObject)idCapabilities).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.FILTER_CAPABILITIES_TYPE__ID_CAPABILITIES, null, msgs);
            if (newIdCapabilities != null)
                msgs = ((InternalEObject)newIdCapabilities).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.FILTER_CAPABILITIES_TYPE__ID_CAPABILITIES, null, msgs);
            msgs = basicSetIdCapabilities(newIdCapabilities, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.FILTER_CAPABILITIES_TYPE__ID_CAPABILITIES, newIdCapabilities, newIdCapabilities));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ScalarCapabilitiesType getScalarCapabilities() {
        return scalarCapabilities;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetScalarCapabilities(ScalarCapabilitiesType newScalarCapabilities, NotificationChain msgs) {
        ScalarCapabilitiesType oldScalarCapabilities = scalarCapabilities;
        scalarCapabilities = newScalarCapabilities;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.FILTER_CAPABILITIES_TYPE__SCALAR_CAPABILITIES, oldScalarCapabilities, newScalarCapabilities);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setScalarCapabilities(ScalarCapabilitiesType newScalarCapabilities) {
        if (newScalarCapabilities != scalarCapabilities) {
            NotificationChain msgs = null;
            if (scalarCapabilities != null)
                msgs = ((InternalEObject)scalarCapabilities).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.FILTER_CAPABILITIES_TYPE__SCALAR_CAPABILITIES, null, msgs);
            if (newScalarCapabilities != null)
                msgs = ((InternalEObject)newScalarCapabilities).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.FILTER_CAPABILITIES_TYPE__SCALAR_CAPABILITIES, null, msgs);
            msgs = basicSetScalarCapabilities(newScalarCapabilities, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.FILTER_CAPABILITIES_TYPE__SCALAR_CAPABILITIES, newScalarCapabilities, newScalarCapabilities));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SpatialCapabilitiesType getSpatialCapabilities() {
        return spatialCapabilities;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSpatialCapabilities(SpatialCapabilitiesType newSpatialCapabilities, NotificationChain msgs) {
        SpatialCapabilitiesType oldSpatialCapabilities = spatialCapabilities;
        spatialCapabilities = newSpatialCapabilities;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.FILTER_CAPABILITIES_TYPE__SPATIAL_CAPABILITIES, oldSpatialCapabilities, newSpatialCapabilities);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSpatialCapabilities(SpatialCapabilitiesType newSpatialCapabilities) {
        if (newSpatialCapabilities != spatialCapabilities) {
            NotificationChain msgs = null;
            if (spatialCapabilities != null)
                msgs = ((InternalEObject)spatialCapabilities).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.FILTER_CAPABILITIES_TYPE__SPATIAL_CAPABILITIES, null, msgs);
            if (newSpatialCapabilities != null)
                msgs = ((InternalEObject)newSpatialCapabilities).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.FILTER_CAPABILITIES_TYPE__SPATIAL_CAPABILITIES, null, msgs);
            msgs = basicSetSpatialCapabilities(newSpatialCapabilities, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.FILTER_CAPABILITIES_TYPE__SPATIAL_CAPABILITIES, newSpatialCapabilities, newSpatialCapabilities));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TemporalCapabilitiesType getTemporalCapabilities() {
        return temporalCapabilities;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTemporalCapabilities(TemporalCapabilitiesType newTemporalCapabilities, NotificationChain msgs) {
        TemporalCapabilitiesType oldTemporalCapabilities = temporalCapabilities;
        temporalCapabilities = newTemporalCapabilities;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.FILTER_CAPABILITIES_TYPE__TEMPORAL_CAPABILITIES, oldTemporalCapabilities, newTemporalCapabilities);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTemporalCapabilities(TemporalCapabilitiesType newTemporalCapabilities) {
        if (newTemporalCapabilities != temporalCapabilities) {
            NotificationChain msgs = null;
            if (temporalCapabilities != null)
                msgs = ((InternalEObject)temporalCapabilities).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.FILTER_CAPABILITIES_TYPE__TEMPORAL_CAPABILITIES, null, msgs);
            if (newTemporalCapabilities != null)
                msgs = ((InternalEObject)newTemporalCapabilities).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.FILTER_CAPABILITIES_TYPE__TEMPORAL_CAPABILITIES, null, msgs);
            msgs = basicSetTemporalCapabilities(newTemporalCapabilities, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.FILTER_CAPABILITIES_TYPE__TEMPORAL_CAPABILITIES, newTemporalCapabilities, newTemporalCapabilities));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AvailableFunctionsType getFunctions() {
        return functions;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFunctions(AvailableFunctionsType newFunctions, NotificationChain msgs) {
        AvailableFunctionsType oldFunctions = functions;
        functions = newFunctions;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.FILTER_CAPABILITIES_TYPE__FUNCTIONS, oldFunctions, newFunctions);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFunctions(AvailableFunctionsType newFunctions) {
        if (newFunctions != functions) {
            NotificationChain msgs = null;
            if (functions != null)
                msgs = ((InternalEObject)functions).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.FILTER_CAPABILITIES_TYPE__FUNCTIONS, null, msgs);
            if (newFunctions != null)
                msgs = ((InternalEObject)newFunctions).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.FILTER_CAPABILITIES_TYPE__FUNCTIONS, null, msgs);
            msgs = basicSetFunctions(newFunctions, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.FILTER_CAPABILITIES_TYPE__FUNCTIONS, newFunctions, newFunctions));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExtendedCapabilitiesType getExtendedCapabilities() {
        return extendedCapabilities;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExtendedCapabilities(ExtendedCapabilitiesType newExtendedCapabilities, NotificationChain msgs) {
        ExtendedCapabilitiesType oldExtendedCapabilities = extendedCapabilities;
        extendedCapabilities = newExtendedCapabilities;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.FILTER_CAPABILITIES_TYPE__EXTENDED_CAPABILITIES, oldExtendedCapabilities, newExtendedCapabilities);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExtendedCapabilities(ExtendedCapabilitiesType newExtendedCapabilities) {
        if (newExtendedCapabilities != extendedCapabilities) {
            NotificationChain msgs = null;
            if (extendedCapabilities != null)
                msgs = ((InternalEObject)extendedCapabilities).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.FILTER_CAPABILITIES_TYPE__EXTENDED_CAPABILITIES, null, msgs);
            if (newExtendedCapabilities != null)
                msgs = ((InternalEObject)newExtendedCapabilities).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.FILTER_CAPABILITIES_TYPE__EXTENDED_CAPABILITIES, null, msgs);
            msgs = basicSetExtendedCapabilities(newExtendedCapabilities, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.FILTER_CAPABILITIES_TYPE__EXTENDED_CAPABILITIES, newExtendedCapabilities, newExtendedCapabilities));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.FILTER_CAPABILITIES_TYPE__CONFORMANCE:
                return basicSetConformance(null, msgs);
            case Fes20Package.FILTER_CAPABILITIES_TYPE__ID_CAPABILITIES:
                return basicSetIdCapabilities(null, msgs);
            case Fes20Package.FILTER_CAPABILITIES_TYPE__SCALAR_CAPABILITIES:
                return basicSetScalarCapabilities(null, msgs);
            case Fes20Package.FILTER_CAPABILITIES_TYPE__SPATIAL_CAPABILITIES:
                return basicSetSpatialCapabilities(null, msgs);
            case Fes20Package.FILTER_CAPABILITIES_TYPE__TEMPORAL_CAPABILITIES:
                return basicSetTemporalCapabilities(null, msgs);
            case Fes20Package.FILTER_CAPABILITIES_TYPE__FUNCTIONS:
                return basicSetFunctions(null, msgs);
            case Fes20Package.FILTER_CAPABILITIES_TYPE__EXTENDED_CAPABILITIES:
                return basicSetExtendedCapabilities(null, msgs);
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
            case Fes20Package.FILTER_CAPABILITIES_TYPE__CONFORMANCE:
                return getConformance();
            case Fes20Package.FILTER_CAPABILITIES_TYPE__ID_CAPABILITIES:
                return getIdCapabilities();
            case Fes20Package.FILTER_CAPABILITIES_TYPE__SCALAR_CAPABILITIES:
                return getScalarCapabilities();
            case Fes20Package.FILTER_CAPABILITIES_TYPE__SPATIAL_CAPABILITIES:
                return getSpatialCapabilities();
            case Fes20Package.FILTER_CAPABILITIES_TYPE__TEMPORAL_CAPABILITIES:
                return getTemporalCapabilities();
            case Fes20Package.FILTER_CAPABILITIES_TYPE__FUNCTIONS:
                return getFunctions();
            case Fes20Package.FILTER_CAPABILITIES_TYPE__EXTENDED_CAPABILITIES:
                return getExtendedCapabilities();
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
            case Fes20Package.FILTER_CAPABILITIES_TYPE__CONFORMANCE:
                setConformance((ConformanceType)newValue);
                return;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__ID_CAPABILITIES:
                setIdCapabilities((IdCapabilitiesType)newValue);
                return;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__SCALAR_CAPABILITIES:
                setScalarCapabilities((ScalarCapabilitiesType)newValue);
                return;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__SPATIAL_CAPABILITIES:
                setSpatialCapabilities((SpatialCapabilitiesType)newValue);
                return;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__TEMPORAL_CAPABILITIES:
                setTemporalCapabilities((TemporalCapabilitiesType)newValue);
                return;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__FUNCTIONS:
                setFunctions((AvailableFunctionsType)newValue);
                return;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__EXTENDED_CAPABILITIES:
                setExtendedCapabilities((ExtendedCapabilitiesType)newValue);
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
            case Fes20Package.FILTER_CAPABILITIES_TYPE__CONFORMANCE:
                setConformance((ConformanceType)null);
                return;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__ID_CAPABILITIES:
                setIdCapabilities((IdCapabilitiesType)null);
                return;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__SCALAR_CAPABILITIES:
                setScalarCapabilities((ScalarCapabilitiesType)null);
                return;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__SPATIAL_CAPABILITIES:
                setSpatialCapabilities((SpatialCapabilitiesType)null);
                return;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__TEMPORAL_CAPABILITIES:
                setTemporalCapabilities((TemporalCapabilitiesType)null);
                return;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__FUNCTIONS:
                setFunctions((AvailableFunctionsType)null);
                return;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__EXTENDED_CAPABILITIES:
                setExtendedCapabilities((ExtendedCapabilitiesType)null);
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
            case Fes20Package.FILTER_CAPABILITIES_TYPE__CONFORMANCE:
                return conformance != null;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__ID_CAPABILITIES:
                return idCapabilities != null;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__SCALAR_CAPABILITIES:
                return scalarCapabilities != null;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__SPATIAL_CAPABILITIES:
                return spatialCapabilities != null;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__TEMPORAL_CAPABILITIES:
                return temporalCapabilities != null;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__FUNCTIONS:
                return functions != null;
            case Fes20Package.FILTER_CAPABILITIES_TYPE__EXTENDED_CAPABILITIES:
                return extendedCapabilities != null;
        }
        return super.eIsSet(featureID);
    }

} //FilterCapabilitiesTypeImpl
