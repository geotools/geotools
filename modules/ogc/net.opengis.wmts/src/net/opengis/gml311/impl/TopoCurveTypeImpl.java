/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.DirectedEdgePropertyType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.TopoCurveType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Topo Curve Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.TopoCurveTypeImpl#getDirectedEdge <em>Directed Edge</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TopoCurveTypeImpl extends AbstractTopologyTypeImpl implements TopoCurveType {
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TopoCurveTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getTopoCurveType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DirectedEdgePropertyType> getDirectedEdge() {
        if (directedEdge == null) {
            directedEdge = new EObjectContainmentEList<DirectedEdgePropertyType>(DirectedEdgePropertyType.class, this, Gml311Package.TOPO_CURVE_TYPE__DIRECTED_EDGE);
        }
        return directedEdge;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.TOPO_CURVE_TYPE__DIRECTED_EDGE:
                return ((InternalEList<?>)getDirectedEdge()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.TOPO_CURVE_TYPE__DIRECTED_EDGE:
                return getDirectedEdge();
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
            case Gml311Package.TOPO_CURVE_TYPE__DIRECTED_EDGE:
                getDirectedEdge().clear();
                getDirectedEdge().addAll((Collection<? extends DirectedEdgePropertyType>)newValue);
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
            case Gml311Package.TOPO_CURVE_TYPE__DIRECTED_EDGE:
                getDirectedEdge().clear();
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
            case Gml311Package.TOPO_CURVE_TYPE__DIRECTED_EDGE:
                return directedEdge != null && !directedEdge.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //TopoCurveTypeImpl
