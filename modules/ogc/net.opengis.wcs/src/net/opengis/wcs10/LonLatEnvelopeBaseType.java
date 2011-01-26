/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import net.opengis.gml.EnvelopeType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Lon Lat Envelope Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * For WCS use, LonLatEnvelopeBaseType restricts gml:Envelope to the WGS84 geographic CRS with Longitude preceding Latitude and both using decimal degrees only. If included, height values are third and use metre units.
 * Envelope defines an extent using a pair of positions defining opposite corners in arbitrary dimensions.
 * <!-- end-model-doc -->
 *
 *
 * @see net.opengis.wcs10.Wcs10Package#getLonLatEnvelopeBaseType()
 * @model extendedMetaData="name='LonLatEnvelopeBaseType' kind='elementOnly'"
 * @generated
 */
public interface LonLatEnvelopeBaseType extends EnvelopeType {
} // LonLatEnvelopeBaseType
