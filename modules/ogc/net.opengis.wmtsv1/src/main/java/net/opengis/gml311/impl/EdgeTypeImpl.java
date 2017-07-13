/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.CurvePropertyType;
import net.opengis.gml311.DirectedFacePropertyType;
import net.opengis.gml311.DirectedNodePropertyType;
import net.opengis.gml311.EdgeType;
import net.opengis.gml311.Gml311Package;

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
 * An implementation of the model object '<em><b>Edge Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.EdgeTypeImpl#getDirectedNode <em>Directed Node</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.EdgeTypeImpl#getDirectedFace <em>Directed Face</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.EdgeTypeImpl#getCurveProperty <em>Curve Property</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EdgeTypeImpl extends AbstractTopoPrimitiveTypeImpl implements EdgeType {
    /**
     * The cached value of the '{@link #getDirectedNode() <em>Directed Node</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDirectedNode()
     * @generated
     * @ordered
     */
    protected EList<DirectedNodePropertyType> directedNode;

    /**
     * The cached value of the '{@link #getDirectedFace() <em>Directed Face</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDirectedFace()
     * @generated
     * @ordered
     */
    protected EList<DirectedFacePropertyType> directedFace;

    /**
     * The cached value of the '{@link #getCurveProperty() <em>Curve Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCurveProperty()
     * @generated
     * @ordered
     */
    protected CurvePropertyType curveProperty;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EdgeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getEdgeType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DirectedNodePropertyType> getDirectedNode() {
        if (directedNode == null) {
            directedNode = new EObjectContainmentEList<DirectedNodePropertyType>(DirectedNodePropertyType.class, this, Gml311Package.EDGE_TYPE__DIRECTED_NODE);
        }
        return directedNode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DirectedFacePropertyType> getDirectedFace() {
        if (directedFace == null) {
            directedFace = new EObjectContainmentEList<DirectedFacePropertyType>(DirectedFacePropertyType.class, this, Gml311Package.EDGE_TYPE__DIRECTED_FACE);
        }
        return directedFace;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CurvePropertyType getCurveProperty() {
        return curveProperty;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCurveProperty(CurvePropertyType newCurveProperty, NotificationChain msgs) {
        CurvePropertyType oldCurveProperty = curveProperty;
        curveProperty = newCurveProperty;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.EDGE_TYPE__CURVE_PROPERTY, oldCurveProperty, newCurveProperty);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCurveProperty(CurvePropertyType newCurveProperty) {
        if (newCurveProperty != curveProperty) {
            NotificationChain msgs = null;
            if (curveProperty != null)
                msgs = ((InternalEObject)curveProperty).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.EDGE_TYPE__CURVE_PROPERTY, null, msgs);
            if (newCurveProperty != null)
                msgs = ((InternalEObject)newCurveProperty).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.EDGE_TYPE__CURVE_PROPERTY, null, msgs);
            msgs = basicSetCurveProperty(newCurveProperty, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.EDGE_TYPE__CURVE_PROPERTY, newCurveProperty, newCurveProperty));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.EDGE_TYPE__DIRECTED_NODE:
                return ((InternalEList<?>)getDirectedNode()).basicRemove(otherEnd, msgs);
            case Gml311Package.EDGE_TYPE__DIRECTED_FACE:
                return ((InternalEList<?>)getDirectedFace()).basicRemove(otherEnd, msgs);
            case Gml311Package.EDGE_TYPE__CURVE_PROPERTY:
                return basicSetCurveProperty(null, msgs);
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
            case Gml311Package.EDGE_TYPE__DIRECTED_NODE:
                return getDirectedNode();
            case Gml311Package.EDGE_TYPE__DIRECTED_FACE:
                return getDirectedFace();
            case Gml311Package.EDGE_TYPE__CURVE_PROPERTY:
                return getCurveProperty();
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
            case Gml311Package.EDGE_TYPE__DIRECTED_NODE:
                getDirectedNode().clear();
                getDirectedNode().addAll((Collection<? extends DirectedNodePropertyType>)newValue);
                return;
            case Gml311Package.EDGE_TYPE__DIRECTED_FACE:
                getDirectedFace().clear();
                getDirectedFace().addAll((Collection<? extends DirectedFacePropertyType>)newValue);
                return;
            case Gml311Package.EDGE_TYPE__CURVE_PROPERTY:
                setCurveProperty((CurvePropertyType)newValue);
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
            case Gml311Package.EDGE_TYPE__DIRECTED_NODE:
                getDirectedNode().clear();
                return;
            case Gml311Package.EDGE_TYPE__DIRECTED_FACE:
                getDirectedFace().clear();
                return;
            case Gml311Package.EDGE_TYPE__CURVE_PROPERTY:
                setCurveProperty((CurvePropertyType)null);
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
            case Gml311Package.EDGE_TYPE__DIRECTED_NODE:
                return directedNode != null && !directedNode.isEmpty();
            case Gml311Package.EDGE_TYPE__DIRECTED_FACE:
                return directedFace != null && !directedFace.isEmpty();
            case Gml311Package.EDGE_TYPE__CURVE_PROPERTY:
                return curveProperty != null;
        }
        return super.eIsSet(featureID);
    }

} //EdgeTypeImpl
