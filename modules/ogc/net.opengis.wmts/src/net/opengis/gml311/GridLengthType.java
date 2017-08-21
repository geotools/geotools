/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Grid Length Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Value of a length (or distance) quantity in a grid, where the grid spacing does not have any associated physical units, or does not have a constant physical spacing. This grid length will often be used in a digital image grid, where the base units are likely to be pixel spacings. Uses the MeasureType with the restriction that the unit of measure referenced by uom must be suitable for length along the axes of a grid, such as pixel spacings or grid spacings.
 * <!-- end-model-doc -->
 *
 *
 * @see net.opengis.gml311.Gml311Package#getGridLengthType()
 * @model extendedMetaData="name='GridLengthType' kind='simple'"
 * @generated
 */
public interface GridLengthType extends MeasureType {
} // GridLengthType
