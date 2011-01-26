/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.opengis.gml.PolygonType;
import net.opengis.wcs10.SpatialDomainType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.geotools.geometry.GeneralEnvelope;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spatial Domain Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.SpatialDomainTypeImpl#getEnvelope <em>Envelope</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.SpatialDomainTypeImpl#getGridGroup <em>Grid Group</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.SpatialDomainTypeImpl#getGrid <em>Grid</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.SpatialDomainTypeImpl#getPolygon <em>Polygon</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SpatialDomainTypeImpl extends EObjectImpl implements SpatialDomainType {
    /**
	 * The cached value of the '{@link #getEnvelope() <em>Envelope</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnvelope()
	 * @generated
	 * @ordered
	 */
	protected EList envelope;

				/**
	 * The cached value of the '{@link #getGridGroup() <em>Grid Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getGridGroup()
	 * @generated
	 * @ordered
	 */
    protected FeatureMap gridGroup;

    /**
	 * The cached value of the '{@link #getPolygon() <em>Polygon</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getPolygon()
	 * @generated
	 * @ordered
	 */
    protected EList polygon;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected SpatialDomainTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.SPATIAL_DOMAIN_TYPE;
	}

/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList getEnvelope() {
		if (envelope == null) {
			envelope = new EDataTypeUniqueEList(GeneralEnvelope.class, this, Wcs10Package.SPATIAL_DOMAIN_TYPE__ENVELOPE);
		}
		return envelope;
	}

				//    /**
//     * <!-- begin-user-doc -->
//     * <!-- end-user-doc -->
//     */
//    public List getEnvelopeGroup() {
//        if (envelopeGroup == null) {
////            envelopeGroup = new BasicFeatureMap(this, Wcs10Package.SPATIAL_DOMAIN_TYPE__ENVELOPE_GROUP);
//            envelopeGroup = new ArrayList();
//        }
//        return envelopeGroup;
//    }
//
//    /**
//     * <!-- begin-user-doc -->
//     * <!-- end-user-doc -->
//     */
//    public List getEnvelope() {
////        return getEnvelopeGroup().list(Wcs10Package.Literals.SPATIAL_DOMAIN_TYPE__ENVELOPE);
//        return getEnvelopeGroup();
//    }

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureMap getGridGroup() {
		if (gridGroup == null) {
			gridGroup = new BasicFeatureMap(this, Wcs10Package.SPATIAL_DOMAIN_TYPE__GRID_GROUP);
		}
		return gridGroup;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getGrid() {
		return getGridGroup().list(Wcs10Package.Literals.SPATIAL_DOMAIN_TYPE__GRID);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getPolygon() {
		if (polygon == null) {
			polygon = new EObjectContainmentEList(PolygonType.class, this, Wcs10Package.SPATIAL_DOMAIN_TYPE__POLYGON);
		}
		return polygon;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__GRID_GROUP:
				return ((InternalEList)getGridGroup()).basicRemove(otherEnd, msgs);
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__GRID:
				return ((InternalEList)getGrid()).basicRemove(otherEnd, msgs);
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__POLYGON:
				return ((InternalEList)getPolygon()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__ENVELOPE:
				return getEnvelope();
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__GRID_GROUP:
				if (coreType) return getGridGroup();
				return ((FeatureMap.Internal)getGridGroup()).getWrapper();
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__GRID:
				return getGrid();
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__POLYGON:
				return getPolygon();
		}
		return super.eGet(featureID, resolve, coreType);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__ENVELOPE:
				getEnvelope().clear();
				getEnvelope().addAll((Collection)newValue);
				return;
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__GRID_GROUP:
				((FeatureMap.Internal)getGridGroup()).set(newValue);
				return;
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__GRID:
				getGrid().clear();
				getGrid().addAll((Collection)newValue);
				return;
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__POLYGON:
				getPolygon().clear();
				getPolygon().addAll((Collection)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(int featureID) {
		switch (featureID) {
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__ENVELOPE:
				getEnvelope().clear();
				return;
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__GRID_GROUP:
				getGridGroup().clear();
				return;
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__GRID:
				getGrid().clear();
				return;
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__POLYGON:
				getPolygon().clear();
				return;
		}
		super.eUnset(featureID);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__ENVELOPE:
				return envelope != null && !envelope.isEmpty();
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__GRID_GROUP:
				return gridGroup != null && !gridGroup.isEmpty();
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__GRID:
				return !getGrid().isEmpty();
			case Wcs10Package.SPATIAL_DOMAIN_TYPE__POLYGON:
				return polygon != null && !polygon.isEmpty();
		}
		return super.eIsSet(featureID);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (gridGroup: ");
		result.append(gridGroup);
		result.append(')');
		return result.toString();
	}

} //SpatialDomainTypeImpl
