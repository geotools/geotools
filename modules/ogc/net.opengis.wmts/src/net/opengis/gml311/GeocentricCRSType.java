/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Geocentric CRS Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A 3D coordinate reference system with the origin at the approximate centre of mass of the earth. A geocentric CRS deals with the earth's curvature by taking a 3D spatial view, which obviates the need to model the earth's curvature. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.GeocentricCRSType#getUsesCartesianCS <em>Uses Cartesian CS</em>}</li>
 *   <li>{@link net.opengis.gml311.GeocentricCRSType#getUsesSphericalCS <em>Uses Spherical CS</em>}</li>
 *   <li>{@link net.opengis.gml311.GeocentricCRSType#getUsesGeodeticDatum <em>Uses Geodetic Datum</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getGeocentricCRSType()
 * @model extendedMetaData="name='GeocentricCRSType' kind='elementOnly'"
 * @generated
 */
public interface GeocentricCRSType extends AbstractReferenceSystemType {
    /**
     * Returns the value of the '<em><b>Uses Cartesian CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the Cartesian coordinate system used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Cartesian CS</em>' containment reference.
     * @see #setUsesCartesianCS(CartesianCSRefType)
     * @see net.opengis.gml311.Gml311Package#getGeocentricCRSType_UsesCartesianCS()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='usesCartesianCS' namespace='##targetNamespace'"
     * @generated
     */
    CartesianCSRefType getUsesCartesianCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.GeocentricCRSType#getUsesCartesianCS <em>Uses Cartesian CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Cartesian CS</em>' containment reference.
     * @see #getUsesCartesianCS()
     * @generated
     */
    void setUsesCartesianCS(CartesianCSRefType value);

    /**
     * Returns the value of the '<em><b>Uses Spherical CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the spherical coordinate system used by this CRS.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Spherical CS</em>' containment reference.
     * @see #setUsesSphericalCS(SphericalCSRefType)
     * @see net.opengis.gml311.Gml311Package#getGeocentricCRSType_UsesSphericalCS()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='usesSphericalCS' namespace='##targetNamespace'"
     * @generated
     */
    SphericalCSRefType getUsesSphericalCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.GeocentricCRSType#getUsesSphericalCS <em>Uses Spherical CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Spherical CS</em>' containment reference.
     * @see #getUsesSphericalCS()
     * @generated
     */
    void setUsesSphericalCS(SphericalCSRefType value);

    /**
     * Returns the value of the '<em><b>Uses Geodetic Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the geodetic datum used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Geodetic Datum</em>' containment reference.
     * @see #setUsesGeodeticDatum(GeodeticDatumRefType)
     * @see net.opengis.gml311.Gml311Package#getGeocentricCRSType_UsesGeodeticDatum()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='usesGeodeticDatum' namespace='##targetNamespace'"
     * @generated
     */
    GeodeticDatumRefType getUsesGeodeticDatum();

    /**
     * Sets the value of the '{@link net.opengis.gml311.GeocentricCRSType#getUsesGeodeticDatum <em>Uses Geodetic Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Geodetic Datum</em>' containment reference.
     * @see #getUsesGeodeticDatum()
     * @generated
     */
    void setUsesGeodeticDatum(GeodeticDatumRefType value);

} // GeocentricCRSType
