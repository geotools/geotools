/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Affine Placement Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A placement takes a standard geometric
 *    construction and places it in geographic space. It defines a
 *    transformation from a constructive parameter space to the 
 *    co-ordinate space of the co-ordinate reference system being used.  
 *    Parameter spaces in formulae in this International Standard are 
 *    given as (u, v) in 2D and(u, v, w) in 3D. Co-ordinate reference 
 *    systems positions are given in formulae, in this International 
 *    Standard, by either (x, y) in 2D, or (x, y, z) in 3D.
 * 
 *    Affine placements are defined by linear transformations from 
 *    parameter space to the target co-ordiante space. 2-dimensional 
 *    Cartesian parameter space,(u,v) transforms into 3-dimensional co-
 *    ordinate reference systems,(x,y,z) by using an affine 
 *    transformation,(u,v)->(x,y,z) which is defined :
 * 
 * 	x	ux vx  	x0
 * 			 u	  
 * 	y =	uy vy   + y0
 * 			 v		
 * 	x	uz vz	z0
 * 	
 *    Then, given this equation, the location element of the 
 *    AffinePlacement is the direct position (x0, y0, z0), which is the
 *    target position of the origin in (u, v). The two reference
 *    directions (ux, uy, uz) and (vx, vy, vz) are the target     
 *    directions of the unit vectors at the origin in (u, v).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AffinePlacementType#getLocation <em>Location</em>}</li>
 *   <li>{@link net.opengis.gml311.AffinePlacementType#getRefDirection <em>Ref Direction</em>}</li>
 *   <li>{@link net.opengis.gml311.AffinePlacementType#getInDimension <em>In Dimension</em>}</li>
 *   <li>{@link net.opengis.gml311.AffinePlacementType#getOutDimension <em>Out Dimension</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAffinePlacementType()
 * @model extendedMetaData="name='AffinePlacementType' kind='elementOnly'"
 * @generated
 */
public interface AffinePlacementType extends EObject {
    /**
     * Returns the value of the '<em><b>Location</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The location property gives 
     *      the target of the parameter space origin. This is the vector  
     *     (x0, y0, z0) in the formulae above.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Location</em>' containment reference.
     * @see #setLocation(DirectPositionType)
     * @see net.opengis.gml311.Gml311Package#getAffinePlacementType_Location()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='location' namespace='##targetNamespace'"
     * @generated
     */
    DirectPositionType getLocation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AffinePlacementType#getLocation <em>Location</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Location</em>' containment reference.
     * @see #getLocation()
     * @generated
     */
    void setLocation(DirectPositionType value);

    /**
     * Returns the value of the '<em><b>Ref Direction</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.VectorType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The attribute refDirection gives the    
     * target directions for the co-ordinate basis vectors of the  
     * parameter space. These are the columns of the matrix in the 
     * formulae given above. The number of directions given shall be 
     * inDimension. The dimension of the directions shall be 
     * outDimension.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Ref Direction</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getAffinePlacementType_RefDirection()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='refDirection' namespace='##targetNamespace'"
     * @generated
     */
    EList<VectorType> getRefDirection();

    /**
     * Returns the value of the '<em><b>In Dimension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Dimension of the constructive parameter 
     *      space.
     * <!-- end-model-doc -->
     * @return the value of the '<em>In Dimension</em>' attribute.
     * @see #setInDimension(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getAffinePlacementType_InDimension()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='inDimension' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getInDimension();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AffinePlacementType#getInDimension <em>In Dimension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>In Dimension</em>' attribute.
     * @see #getInDimension()
     * @generated
     */
    void setInDimension(BigInteger value);

    /**
     * Returns the value of the '<em><b>Out Dimension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Dimension of the co-ordinate space.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Out Dimension</em>' attribute.
     * @see #setOutDimension(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getAffinePlacementType_OutDimension()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='outDimension' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getOutDimension();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AffinePlacementType#getOutDimension <em>Out Dimension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Out Dimension</em>' attribute.
     * @see #getOutDimension()
     * @generated
     */
    void setOutDimension(BigInteger value);

} // AffinePlacementType
