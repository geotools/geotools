/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.DirectedEdgePropertyType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.NodeType;
import net.opengis.gml311.PointPropertyType;

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
 * An implementation of the model object '<em><b>Node Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.NodeTypeImpl#getDirectedEdge <em>Directed Edge</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.NodeTypeImpl#getPointProperty <em>Point Property</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NodeTypeImpl extends AbstractTopoPrimitiveTypeImpl implements NodeType {
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
     * The cached value of the '{@link #getPointProperty() <em>Point Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPointProperty()
     * @generated
     * @ordered
     */
    protected PointPropertyType pointProperty;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected NodeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getNodeType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DirectedEdgePropertyType> getDirectedEdge() {
        if (directedEdge == null) {
            directedEdge = new EObjectContainmentEList<DirectedEdgePropertyType>(DirectedEdgePropertyType.class, this, Gml311Package.NODE_TYPE__DIRECTED_EDGE);
        }
        return directedEdge;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.NODE_TYPE__POINT_PROPERTY, oldPointProperty, newPointProperty);
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
                msgs = ((InternalEObject)pointProperty).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.NODE_TYPE__POINT_PROPERTY, null, msgs);
            if (newPointProperty != null)
                msgs = ((InternalEObject)newPointProperty).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.NODE_TYPE__POINT_PROPERTY, null, msgs);
            msgs = basicSetPointProperty(newPointProperty, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.NODE_TYPE__POINT_PROPERTY, newPointProperty, newPointProperty));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.NODE_TYPE__DIRECTED_EDGE:
                return ((InternalEList<?>)getDirectedEdge()).basicRemove(otherEnd, msgs);
            case Gml311Package.NODE_TYPE__POINT_PROPERTY:
                return basicSetPointProperty(null, msgs);
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
            case Gml311Package.NODE_TYPE__DIRECTED_EDGE:
                return getDirectedEdge();
            case Gml311Package.NODE_TYPE__POINT_PROPERTY:
                return getPointProperty();
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
            case Gml311Package.NODE_TYPE__DIRECTED_EDGE:
                getDirectedEdge().clear();
                getDirectedEdge().addAll((Collection<? extends DirectedEdgePropertyType>)newValue);
                return;
            case Gml311Package.NODE_TYPE__POINT_PROPERTY:
                setPointProperty((PointPropertyType)newValue);
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
            case Gml311Package.NODE_TYPE__DIRECTED_EDGE:
                getDirectedEdge().clear();
                return;
            case Gml311Package.NODE_TYPE__POINT_PROPERTY:
                setPointProperty((PointPropertyType)null);
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
            case Gml311Package.NODE_TYPE__DIRECTED_EDGE:
                return directedEdge != null && !directedEdge.isEmpty();
            case Gml311Package.NODE_TYPE__POINT_PROPERTY:
                return pointProperty != null;
        }
        return super.eIsSet(featureID);
    }

} //NodeTypeImpl
