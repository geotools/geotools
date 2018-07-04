/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.DirectedEdgePropertyType;
import net.opengis.gml311.DirectedTopoSolidPropertyType;
import net.opengis.gml311.FaceType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.SurfacePropertyType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Face Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.FaceTypeImpl#getDirectedEdge <em>Directed Edge</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FaceTypeImpl#getDirectedTopoSolid <em>Directed Topo Solid</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FaceTypeImpl#getSurfaceProperty <em>Surface Property</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FaceTypeImpl extends AbstractTopoPrimitiveTypeImpl implements FaceType {
    /**
     * The cached value of the '{@link #getDirectedEdge() <em>Directed Edge</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDirectedEdge()
     * @generated
     * @ordered
     */
    protected EList<DirectedEdgePropertyType> directedEdge;

    /**
     * The cached value of the '{@link #getDirectedTopoSolid() <em>Directed Topo Solid</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDirectedTopoSolid()
     * @generated
     * @ordered
     */
    protected EList<DirectedTopoSolidPropertyType> directedTopoSolid;

    /**
     * The cached value of the '{@link #getSurfaceProperty() <em>Surface Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSurfaceProperty()
     * @generated
     * @ordered
     */
    protected SurfacePropertyType surfaceProperty;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FaceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getFaceType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DirectedEdgePropertyType> getDirectedEdge() {
        if (directedEdge == null) {
            directedEdge = new EObjectContainmentEList<DirectedEdgePropertyType>(DirectedEdgePropertyType.class, this, Gml311Package.FACE_TYPE__DIRECTED_EDGE);
        }
        return directedEdge;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DirectedTopoSolidPropertyType> getDirectedTopoSolid() {
        if (directedTopoSolid == null) {
            directedTopoSolid = new EObjectContainmentEList<DirectedTopoSolidPropertyType>(DirectedTopoSolidPropertyType.class, this, Gml311Package.FACE_TYPE__DIRECTED_TOPO_SOLID);
        }
        return directedTopoSolid;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SurfacePropertyType getSurfaceProperty() {
        return surfaceProperty;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSurfaceProperty(SurfacePropertyType newSurfaceProperty, NotificationChain msgs) {
        SurfacePropertyType oldSurfaceProperty = surfaceProperty;
        surfaceProperty = newSurfaceProperty;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.FACE_TYPE__SURFACE_PROPERTY, oldSurfaceProperty, newSurfaceProperty);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSurfaceProperty(SurfacePropertyType newSurfaceProperty) {
        if (newSurfaceProperty != surfaceProperty) {
            NotificationChain msgs = null;
            if (surfaceProperty != null)
                msgs = ((InternalEObject)surfaceProperty).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.FACE_TYPE__SURFACE_PROPERTY, null, msgs);
            if (newSurfaceProperty != null)
                msgs = ((InternalEObject)newSurfaceProperty).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.FACE_TYPE__SURFACE_PROPERTY, null, msgs);
            msgs = basicSetSurfaceProperty(newSurfaceProperty, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FACE_TYPE__SURFACE_PROPERTY, newSurfaceProperty, newSurfaceProperty));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.FACE_TYPE__DIRECTED_EDGE:
                return ((InternalEList<?>)getDirectedEdge()).basicRemove(otherEnd, msgs);
            case Gml311Package.FACE_TYPE__DIRECTED_TOPO_SOLID:
                return ((InternalEList<?>)getDirectedTopoSolid()).basicRemove(otherEnd, msgs);
            case Gml311Package.FACE_TYPE__SURFACE_PROPERTY:
                return basicSetSurfaceProperty(null, msgs);
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
            case Gml311Package.FACE_TYPE__DIRECTED_EDGE:
                return getDirectedEdge();
            case Gml311Package.FACE_TYPE__DIRECTED_TOPO_SOLID:
                return getDirectedTopoSolid();
            case Gml311Package.FACE_TYPE__SURFACE_PROPERTY:
                return getSurfaceProperty();
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
            case Gml311Package.FACE_TYPE__DIRECTED_EDGE:
                getDirectedEdge().clear();
                getDirectedEdge().addAll((Collection<? extends DirectedEdgePropertyType>)newValue);
                return;
            case Gml311Package.FACE_TYPE__DIRECTED_TOPO_SOLID:
                getDirectedTopoSolid().clear();
                getDirectedTopoSolid().addAll((Collection<? extends DirectedTopoSolidPropertyType>)newValue);
                return;
            case Gml311Package.FACE_TYPE__SURFACE_PROPERTY:
                setSurfaceProperty((SurfacePropertyType)newValue);
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
            case Gml311Package.FACE_TYPE__DIRECTED_EDGE:
                getDirectedEdge().clear();
                return;
            case Gml311Package.FACE_TYPE__DIRECTED_TOPO_SOLID:
                getDirectedTopoSolid().clear();
                return;
            case Gml311Package.FACE_TYPE__SURFACE_PROPERTY:
                setSurfaceProperty((SurfacePropertyType)null);
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
            case Gml311Package.FACE_TYPE__DIRECTED_EDGE:
                return directedEdge != null && !directedEdge.isEmpty();
            case Gml311Package.FACE_TYPE__DIRECTED_TOPO_SOLID:
                return directedTopoSolid != null && !directedTopoSolid.isEmpty();
            case Gml311Package.FACE_TYPE__SURFACE_PROPERTY:
                return surfaceProperty != null;
        }
        return super.eIsSet(featureID);
    }

} //FaceTypeImpl
