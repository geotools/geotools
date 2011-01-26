/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import java.util.List;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

import com.vividsolutions.jts.geom.Envelope;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spatial Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Defines the spatial domain of a coverage offering. A server shall describe the spatial domain by its edges, using one or more gml:Envelope elements. The gml:EnvelopeWithTimePeriod element may be used in place of gml:Envelope, to add the time bounds of the coverage offering. Each of these elements describes a bounding box defined by two points in space (or two positions in space and two in time). This bounding box could simply duplicate the information in the lonLatEnvelope of CoverageOfferingBrief; but the intent is to describe the locations in more detail (e.g., in several different CRSs, or several rectangular areas instead of one overall bounding box).
 * 
 * In addition, a server can describe the internal grid structure of a coverage offering, using a gml:Grid (or gml:RectifiedGrid) in addition to a gml:Envelope. This element can help clients assess the fitness of the gridded data for their use (e.g. its native resolution, inferred from the offsetVector of a gml:RectifiedGrid), and to formulate grid coverage requests expressed in the internal grid coordinate reference system.
 * 
 * Finally, a server can describe the spatial domain by means of a (repeatable) gml:Polygon, representing the polygon(s) covered by the coverage spatial domain. This is particularly useful for areas that are poorly approximated by a gml:Envelope (such as satellite image swaths, island groups, other non-convex areas).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.SpatialDomainType#getEnvelope <em>Envelope</em>}</li>
 *   <li>{@link net.opengis.wcs10.SpatialDomainType#getGridGroup <em>Grid Group</em>}</li>
 *   <li>{@link net.opengis.wcs10.SpatialDomainType#getGrid <em>Grid</em>}</li>
 *   <li>{@link net.opengis.wcs10.SpatialDomainType#getPolygon <em>Polygon</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getSpatialDomainType()
 * @model extendedMetaData="name='SpatialDomainType' kind='elementOnly'"
 * @generated
 */
public interface SpatialDomainType extends EObject {
    /**
     * Returns the value of the '<em><b>Envelope Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Envelope Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Envelope Group</em>' attribute list.
     * @see net.opengis.wcs10.Wcs10Package#getSpatialDomainType_EnvelopeGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="true"
     *        extendedMetaData="kind='group' name='Envelope:group' namespace='http://www.opengis.net/gml'"
     */
    //FeatureMap getEnvelopeGroup();

    /**
     * Returns the value of the '<em><b>Envelope</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml.EnvelopeType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Envelope</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Envelope</em>' containment reference list.
     * @see net.opengis.wcs10.Wcs10Package#getSpatialDomainType_Envelope()
     * @model type="org.geotools.geometry.GeneralEnvelope"
     */
    EList getEnvelope();

    /**
	 * Returns the value of the '<em><b>Grid Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Grid Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Grid Group</em>' attribute list.
	 * @see net.opengis.wcs10.Wcs10Package#getSpatialDomainType_GridGroup()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='Grid:group' namespace='http://www.opengis.net/gml'"
	 * @generated
	 */
    FeatureMap getGridGroup();

    /**
	 * Returns the value of the '<em><b>Grid</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.gml.GridType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Grid</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Grid</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getSpatialDomainType_Grid()
	 * @model type="net.opengis.gml.GridType" containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Grid' namespace='http://www.opengis.net/gml' group='http://www.opengis.net/gml#Grid:group'"
	 * @generated
	 */
    EList getGrid();

    /**
	 * Returns the value of the '<em><b>Polygon</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.gml.PolygonType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Polygon</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Polygon</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getSpatialDomainType_Polygon()
	 * @model type="net.opengis.gml.PolygonType" containment="true"
	 *        extendedMetaData="kind='element' name='Polygon' namespace='http://www.opengis.net/gml'"
	 * @generated
	 */
    EList getPolygon();

} // SpatialDomainType
