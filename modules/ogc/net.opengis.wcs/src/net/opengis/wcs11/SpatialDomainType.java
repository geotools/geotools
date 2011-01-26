/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spatial Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of the spatial domain of a coverage. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.SpatialDomainType#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.wcs11.SpatialDomainType#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wcs11.SpatialDomainType#getGridCRS <em>Grid CRS</em>}</li>
 *   <li>{@link net.opengis.wcs11.SpatialDomainType#getTransformation <em>Transformation</em>}</li>
 *   <li>{@link net.opengis.wcs11.SpatialDomainType#getImageCRS <em>Image CRS</em>}</li>
 *   <li>{@link net.opengis.wcs11.SpatialDomainType#getPolygon <em>Polygon</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getSpatialDomainType()
 * @model extendedMetaData="name='SpatialDomainType' kind='elementOnly'"
 * @generated
 */
public interface SpatialDomainType extends EObject {
    /**
     * Returns the value of the '<em><b>Bounding Box Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The first bounding box shall exactly specify the spatial domain of the offered coverage in the CRS of that offered coverage, thus specifying the available grid row and column indices. For a georectified coverage (that has a GridCRS), this bounding box shall specify the spatial domain in that GridCRS. For an image that is not georectified, this bounding box shall specify the spatial domain in the ImageCRS of that image, whether or not that image is georeferenced. 
     * 					Additional bounding boxes, if any, shall specify the spatial domain in other CRSs. One bounding box could simply duplicate the information in the ows:WGS84BoundingBox; but the intent is to describe the spatial domain in more detail (e.g., in several different CRSs, or several rectangular areas instead of one overall bounding box). Multiple bounding boxes with the same CRS shall be interpreted as an unordered list of bounding boxes whose union covers spatial domain of this coverage. Notice that WCS use of this BoundingBox is further specified in specification Subclause 7.5. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box Group</em>' attribute list.
     * @see net.opengis.wcs11.Wcs111Package#getSpatialDomainType_BoundingBoxGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="true"
     *        extendedMetaData="kind='group' name='BoundingBox:group' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    FeatureMap getBoundingBoxGroup();

    /**
     * Returns the value of the '<em><b>Bounding Box</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.BoundingBoxType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The first bounding box shall exactly specify the spatial domain of the offered coverage in the CRS of that offered coverage, thus specifying the available grid row and column indices. For a georectified coverage (that has a GridCRS), this bounding box shall specify the spatial domain in that GridCRS. For an image that is not georectified, this bounding box shall specify the spatial domain in the ImageCRS of that image, whether or not that image is georeferenced. 
     * 					Additional bounding boxes, if any, shall specify the spatial domain in other CRSs. One bounding box could simply duplicate the information in the ows:WGS84BoundingBox; but the intent is to describe the spatial domain in more detail (e.g., in several different CRSs, or several rectangular areas instead of one overall bounding box). Multiple bounding boxes with the same CRS shall be interpreted as an unordered list of bounding boxes whose union covers spatial domain of this coverage. Notice that WCS use of this BoundingBox is further specified in specification Subclause 7.5. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box</em>' containment reference list.
     * @see net.opengis.wcs11.Wcs111Package#getSpatialDomainType_BoundingBox()
     * @model type="net.opengis.ows11.BoundingBoxType" containment="true" required="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BoundingBox' namespace='http://www.opengis.net/ows/1.1' group='http://www.opengis.net/ows/1.1#BoundingBox:group'"
     * @generated
     */
    EList getBoundingBox();

    /**
     * Returns the value of the '<em><b>Grid CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Definition of GridCRS of the offered coverage. This GridCRS shall be included when this coverage is georectified and is thus stored in a GridCRS. This GridCRS applies to this offered coverage, and specifies its spatial resolution. The definition is included to inform clients of this GridCRS, for possible use in a GetCoverage operation request. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Grid CRS</em>' containment reference.
     * @see #setGridCRS(GridCrsType)
     * @see net.opengis.wcs11.Wcs111Package#getSpatialDomainType_GridCRS()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='GridCRS' namespace='##targetNamespace'"
     * @generated
     */
    GridCrsType getGridCRS();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.SpatialDomainType#getGridCRS <em>Grid CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid CRS</em>' containment reference.
     * @see #getGridCRS()
     * @generated
     */
    void setGridCRS(GridCrsType value);

    /**
     * Returns the value of the '<em><b>Transformation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Georeferencing coordinate transformation for unrectified coverage, which should be included when available for a coverage that is georeferenced but not georectified. When included, this Transformation will specify the variable spatial resolution of this non-georectified image. To support use cases 4, 5, 9, and/or 10 specified in Annex H, a WCS server needs to use a georeferencing coordinate transformation for a georeferenced but not georectified coverage. That georeferencing transformation can be specified as a Transformation, or a ConcatenatedOperation that includes at least one Transformation. However, a WCS server may not support those use cases, not use a georeferencing transformation specified in that manner, or not make that transformation available to clients. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Transformation</em>' attribute.
     * @see #setTransformation(Object)
     * @see net.opengis.wcs11.Wcs111Package#getSpatialDomainType_Transformation()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='element' name='Transformation' namespace='##targetNamespace'"
     * @generated
     */
    Object getTransformation();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.SpatialDomainType#getTransformation <em>Transformation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Transformation</em>' attribute.
     * @see #getTransformation()
     * @generated
     */
    void setTransformation(Object value);

    /**
     * Returns the value of the '<em><b>Image CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to ImageCRS of the offered coverage. This ImageCRS shall be included when this coverage is an image. This imageCRS applies to this offered coverage, but does not (normally) specify its spatial resolution. The ImageCRS for an image coverage is referenced to inform clients of the ImageCRS, for possible use in a GetCoverage operation request. The definition of this ImageCRS shall be included unless the association reference URI completely specifies that ImageCRS (such as by using the URL of the definition or using a suitable URN). 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Image CRS</em>' containment reference.
     * @see #setImageCRS(ImageCRSRefType)
     * @see net.opengis.wcs11.Wcs111Package#getSpatialDomainType_ImageCRS()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ImageCRS' namespace='##targetNamespace'"
     * @generated
     */
    ImageCRSRefType getImageCRS();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.SpatialDomainType#getImageCRS <em>Image CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Image CRS</em>' containment reference.
     * @see #getImageCRS()
     * @generated
     */
    void setImageCRS(ImageCRSRefType value);

    /**
     * Returns the value of the '<em><b>Polygon</b></em>' attribute list.
     * The list contents are of type {@link java.lang.Object}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of polygons which more accurately describe the coverage spatial domain in various CRSs. Polygons are particularly useful for areas that are poorly approximated by a BoundingBox (such as satellite image swaths, island groups, other non-convex areas). 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Polygon</em>' attribute list.
     * @see net.opengis.wcs11.Wcs111Package#getSpatialDomainType_Polygon()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='element' name='Polygon' namespace='http://www.opengis.net/gml'"
     * @generated
     */
    EList getPolygon();

} // SpatialDomainType
