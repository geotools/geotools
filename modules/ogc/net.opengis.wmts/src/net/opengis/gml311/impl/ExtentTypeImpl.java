/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.EnvelopeType;
import net.opengis.gml311.ExtentType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.PolygonType;
import net.opengis.gml311.StringOrRefType;
import net.opengis.gml311.TimePeriodType;

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
 * An implementation of the model object '<em><b>Extent Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.ExtentTypeImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ExtentTypeImpl#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ExtentTypeImpl#getBoundingPolygon <em>Bounding Polygon</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ExtentTypeImpl#getVerticalExtent <em>Vertical Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ExtentTypeImpl#getTemporalExtent <em>Temporal Extent</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExtentTypeImpl extends MinimalEObjectImpl.Container implements ExtentType {
    /**
     * The cached value of the '{@link #getDescription() <em>Description</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDescription()
     * @generated
     * @ordered
     */
    protected StringOrRefType description;

    /**
     * The cached value of the '{@link #getBoundingBox() <em>Bounding Box</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBoundingBox()
     * @generated
     * @ordered
     */
    protected EList<EnvelopeType> boundingBox;

    /**
     * The cached value of the '{@link #getBoundingPolygon() <em>Bounding Polygon</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBoundingPolygon()
     * @generated
     * @ordered
     */
    protected EList<PolygonType> boundingPolygon;

    /**
     * The cached value of the '{@link #getVerticalExtent() <em>Vertical Extent</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVerticalExtent()
     * @generated
     * @ordered
     */
    protected EList<EnvelopeType> verticalExtent;

    /**
     * The cached value of the '{@link #getTemporalExtent() <em>Temporal Extent</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTemporalExtent()
     * @generated
     * @ordered
     */
    protected EList<TimePeriodType> temporalExtent;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ExtentTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getExtentType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StringOrRefType getDescription() {
        return description;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDescription(StringOrRefType newDescription, NotificationChain msgs) {
        StringOrRefType oldDescription = description;
        description = newDescription;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.EXTENT_TYPE__DESCRIPTION, oldDescription, newDescription);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDescription(StringOrRefType newDescription) {
        if (newDescription != description) {
            NotificationChain msgs = null;
            if (description != null)
                msgs = ((InternalEObject)description).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.EXTENT_TYPE__DESCRIPTION, null, msgs);
            if (newDescription != null)
                msgs = ((InternalEObject)newDescription).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.EXTENT_TYPE__DESCRIPTION, null, msgs);
            msgs = basicSetDescription(newDescription, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.EXTENT_TYPE__DESCRIPTION, newDescription, newDescription));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<EnvelopeType> getBoundingBox() {
        if (boundingBox == null) {
            boundingBox = new EObjectContainmentEList<EnvelopeType>(EnvelopeType.class, this, Gml311Package.EXTENT_TYPE__BOUNDING_BOX);
        }
        return boundingBox;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<PolygonType> getBoundingPolygon() {
        if (boundingPolygon == null) {
            boundingPolygon = new EObjectContainmentEList<PolygonType>(PolygonType.class, this, Gml311Package.EXTENT_TYPE__BOUNDING_POLYGON);
        }
        return boundingPolygon;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<EnvelopeType> getVerticalExtent() {
        if (verticalExtent == null) {
            verticalExtent = new EObjectContainmentEList<EnvelopeType>(EnvelopeType.class, this, Gml311Package.EXTENT_TYPE__VERTICAL_EXTENT);
        }
        return verticalExtent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TimePeriodType> getTemporalExtent() {
        if (temporalExtent == null) {
            temporalExtent = new EObjectContainmentEList<TimePeriodType>(TimePeriodType.class, this, Gml311Package.EXTENT_TYPE__TEMPORAL_EXTENT);
        }
        return temporalExtent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.EXTENT_TYPE__DESCRIPTION:
                return basicSetDescription(null, msgs);
            case Gml311Package.EXTENT_TYPE__BOUNDING_BOX:
                return ((InternalEList<?>)getBoundingBox()).basicRemove(otherEnd, msgs);
            case Gml311Package.EXTENT_TYPE__BOUNDING_POLYGON:
                return ((InternalEList<?>)getBoundingPolygon()).basicRemove(otherEnd, msgs);
            case Gml311Package.EXTENT_TYPE__VERTICAL_EXTENT:
                return ((InternalEList<?>)getVerticalExtent()).basicRemove(otherEnd, msgs);
            case Gml311Package.EXTENT_TYPE__TEMPORAL_EXTENT:
                return ((InternalEList<?>)getTemporalExtent()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.EXTENT_TYPE__DESCRIPTION:
                return getDescription();
            case Gml311Package.EXTENT_TYPE__BOUNDING_BOX:
                return getBoundingBox();
            case Gml311Package.EXTENT_TYPE__BOUNDING_POLYGON:
                return getBoundingPolygon();
            case Gml311Package.EXTENT_TYPE__VERTICAL_EXTENT:
                return getVerticalExtent();
            case Gml311Package.EXTENT_TYPE__TEMPORAL_EXTENT:
                return getTemporalExtent();
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
            case Gml311Package.EXTENT_TYPE__DESCRIPTION:
                setDescription((StringOrRefType)newValue);
                return;
            case Gml311Package.EXTENT_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                getBoundingBox().addAll((Collection<? extends EnvelopeType>)newValue);
                return;
            case Gml311Package.EXTENT_TYPE__BOUNDING_POLYGON:
                getBoundingPolygon().clear();
                getBoundingPolygon().addAll((Collection<? extends PolygonType>)newValue);
                return;
            case Gml311Package.EXTENT_TYPE__VERTICAL_EXTENT:
                getVerticalExtent().clear();
                getVerticalExtent().addAll((Collection<? extends EnvelopeType>)newValue);
                return;
            case Gml311Package.EXTENT_TYPE__TEMPORAL_EXTENT:
                getTemporalExtent().clear();
                getTemporalExtent().addAll((Collection<? extends TimePeriodType>)newValue);
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
            case Gml311Package.EXTENT_TYPE__DESCRIPTION:
                setDescription((StringOrRefType)null);
                return;
            case Gml311Package.EXTENT_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                return;
            case Gml311Package.EXTENT_TYPE__BOUNDING_POLYGON:
                getBoundingPolygon().clear();
                return;
            case Gml311Package.EXTENT_TYPE__VERTICAL_EXTENT:
                getVerticalExtent().clear();
                return;
            case Gml311Package.EXTENT_TYPE__TEMPORAL_EXTENT:
                getTemporalExtent().clear();
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
            case Gml311Package.EXTENT_TYPE__DESCRIPTION:
                return description != null;
            case Gml311Package.EXTENT_TYPE__BOUNDING_BOX:
                return boundingBox != null && !boundingBox.isEmpty();
            case Gml311Package.EXTENT_TYPE__BOUNDING_POLYGON:
                return boundingPolygon != null && !boundingPolygon.isEmpty();
            case Gml311Package.EXTENT_TYPE__VERTICAL_EXTENT:
                return verticalExtent != null && !verticalExtent.isEmpty();
            case Gml311Package.EXTENT_TYPE__TEMPORAL_EXTENT:
                return temporalExtent != null && !temporalExtent.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ExtentTypeImpl
