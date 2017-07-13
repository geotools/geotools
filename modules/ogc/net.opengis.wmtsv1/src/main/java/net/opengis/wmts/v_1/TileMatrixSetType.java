/**
 */
package net.opengis.wmts.v_1;

import net.opengis.ows11.BoundingBoxType;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.DescriptionType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tile Matrix Set Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixSetType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixSetType#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixSetType#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixSetType#getSupportedCRS <em>Supported CRS</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixSetType#getWellKnownScaleSet <em>Well Known Scale Set</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixSetType#getTileMatrix <em>Tile Matrix</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixSetType()
 * @model extendedMetaData="name='TileMatrixSet_._type' kind='elementOnly'"
 * @generated
 */
public interface TileMatrixSetType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Tile matrix set identifier
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference.
     * @see #setIdentifier(CodeType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixSetType_Identifier()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    CodeType getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixSetType#getIdentifier <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' containment reference.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(CodeType value);

    /**
     * Returns the value of the '<em><b>Bounding Box Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 									Minimum bounding rectangle surrounding 
     * 									the visible layer presented by this tile matrix 
     * 									set, in the supported CRS 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box Group</em>' attribute list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixSetType_BoundingBoxGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='BoundingBox:group' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    FeatureMap getBoundingBoxGroup();

    /**
     * Returns the value of the '<em><b>Bounding Box</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 									Minimum bounding rectangle surrounding 
     * 									the visible layer presented by this tile matrix 
     * 									set, in the supported CRS 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box</em>' containment reference.
     * @see #setBoundingBox(BoundingBoxType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixSetType_BoundingBox()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BoundingBox' namespace='http://www.opengis.net/ows/1.1' group='http://www.opengis.net/ows/1.1#BoundingBox:group'"
     * @generated
     */
    BoundingBoxType getBoundingBox();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixSetType#getBoundingBox <em>Bounding Box</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bounding Box</em>' containment reference.
     * @see #getBoundingBox()
     * @generated
     */
    void setBoundingBox(BoundingBoxType value);

    /**
     * Returns the value of the '<em><b>Supported CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to one coordinate reference 
     * 								system (CRS).
     * <!-- end-model-doc -->
     * @return the value of the '<em>Supported CRS</em>' attribute.
     * @see #setSupportedCRS(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixSetType_SupportedCRS()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='element' name='SupportedCRS' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    String getSupportedCRS();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixSetType#getSupportedCRS <em>Supported CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Supported CRS</em>' attribute.
     * @see #getSupportedCRS()
     * @generated
     */
    void setSupportedCRS(String value);

    /**
     * Returns the value of the '<em><b>Well Known Scale Set</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a well known scale set.
     * 									urn:ogc:def:wkss:OGC:1.0:GlobalCRS84Scale, 
     * 									urn:ogc:def:wkss:OGC:1.0:GlobalCRS84Pixel, 
     * 									urn:ogc:def:wkss:OGC:1.0:GoogleCRS84Quad and 
     * 									urn:ogc:def:wkss:OGC:1.0:GoogleMapsCompatible are 
     * 								possible values that are defined in Annex E. It has to be consistent with the 
     * 								SupportedCRS and with the ScaleDenominators of the TileMatrix elements.
     * 								
     * <!-- end-model-doc -->
     * @return the value of the '<em>Well Known Scale Set</em>' attribute.
     * @see #setWellKnownScaleSet(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixSetType_WellKnownScaleSet()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='WellKnownScaleSet' namespace='##targetNamespace'"
     * @generated
     */
    String getWellKnownScaleSet();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixSetType#getWellKnownScaleSet <em>Well Known Scale Set</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Well Known Scale Set</em>' attribute.
     * @see #getWellKnownScaleSet()
     * @generated
     */
    void setWellKnownScaleSet(String value);

    /**
     * Returns the value of the '<em><b>Tile Matrix</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wmts.v_1.TileMatrixType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Describes a scale level and its tile matrix.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Matrix</em>' containment reference list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixSetType_TileMatrix()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='TileMatrix' namespace='##targetNamespace'"
     * @generated
     */
    EList<TileMatrixType> getTileMatrix();

} // TileMatrixSetType
