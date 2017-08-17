/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.DirectedNodePropertyType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.TopoPointType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Topo Point Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.TopoPointTypeImpl#getDirectedNode <em>Directed Node</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TopoPointTypeImpl extends AbstractTopologyTypeImpl implements TopoPointType {
    /**
     * The cached value of the '{@link #getDirectedNode() <em>Directed Node</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDirectedNode()
     * @generated
     * @ordered
     */
    protected DirectedNodePropertyType directedNode;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TopoPointTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getTopoPointType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectedNodePropertyType getDirectedNode() {
        return directedNode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDirectedNode(DirectedNodePropertyType newDirectedNode, NotificationChain msgs) {
        DirectedNodePropertyType oldDirectedNode = directedNode;
        directedNode = newDirectedNode;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.TOPO_POINT_TYPE__DIRECTED_NODE, oldDirectedNode, newDirectedNode);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDirectedNode(DirectedNodePropertyType newDirectedNode) {
        if (newDirectedNode != directedNode) {
            NotificationChain msgs = null;
            if (directedNode != null)
                msgs = ((InternalEObject)directedNode).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TOPO_POINT_TYPE__DIRECTED_NODE, null, msgs);
            if (newDirectedNode != null)
                msgs = ((InternalEObject)newDirectedNode).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TOPO_POINT_TYPE__DIRECTED_NODE, null, msgs);
            msgs = basicSetDirectedNode(newDirectedNode, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TOPO_POINT_TYPE__DIRECTED_NODE, newDirectedNode, newDirectedNode));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.TOPO_POINT_TYPE__DIRECTED_NODE:
                return basicSetDirectedNode(null, msgs);
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
            case Gml311Package.TOPO_POINT_TYPE__DIRECTED_NODE:
                return getDirectedNode();
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
            case Gml311Package.TOPO_POINT_TYPE__DIRECTED_NODE:
                setDirectedNode((DirectedNodePropertyType)newValue);
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
            case Gml311Package.TOPO_POINT_TYPE__DIRECTED_NODE:
                setDirectedNode((DirectedNodePropertyType)null);
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
            case Gml311Package.TOPO_POINT_TYPE__DIRECTED_NODE:
                return directedNode != null;
        }
        return super.eIsSet(featureID);
    }

} //TopoPointTypeImpl
