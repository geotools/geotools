/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Identifier Generation Option Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.wfs.WfsPackage#getIdentifierGenerationOptionType()
 * @model
 * @generated
 */
public final class IdentifierGenerationOptionType extends AbstractEnumerator {
	/**
     * The '<em><b>Use Existing</b></em>' literal value.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   The UseExsiting value indicates that WFS should not
     *                   generate a new feature identifier for the feature
     *                   being inserted into the repositry.  Instead, the WFS
     *                   should use the identifier encoded if the feature.
     *                   If a duplicate exists then the WFS should raise an
     *                   exception.
     * <!-- end-model-doc -->
     * @see #USE_EXISTING_LITERAL
     * @model name="UseExisting"
     * @generated
     * @ordered
     */
	public static final int USE_EXISTING = 0;

	/**
     * The '<em><b>Replace Duplicate</b></em>' literal value.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   The ReplaceDuplicate value indicates that WFS should
     *                   not generate a new feature identifier for the feature
     *                   being inserted into the repositry.  Instead, the WFS
     *                   should use the identifier encoded if the feature.
     *                   If a duplicate exists then the WFS should replace the
     *                   existing feature instance with the one encoded in the
     *                   Insert action.
     * <!-- end-model-doc -->
     * @see #REPLACE_DUPLICATE_LITERAL
     * @model name="ReplaceDuplicate"
     * @generated
     * @ordered
     */
	public static final int REPLACE_DUPLICATE = 1;

	/**
     * The '<em><b>Generate New</b></em>' literal value.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   The GenerateNew value indicates that WFS should
     *                   generate a new unique feature identifier for the
     *                   feature being inserted into the repositry.
     * <!-- end-model-doc -->
     * @see #GENERATE_NEW_LITERAL
     * @model name="GenerateNew"
     * @generated
     * @ordered
     */
	public static final int GENERATE_NEW = 2;

	/**
     * The '<em><b>Use Existing</b></em>' literal object.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #USE_EXISTING
     * @generated
     * @ordered
     */
	public static final IdentifierGenerationOptionType USE_EXISTING_LITERAL = new IdentifierGenerationOptionType(USE_EXISTING, "UseExisting", "UseExisting");

	/**
     * The '<em><b>Replace Duplicate</b></em>' literal object.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #REPLACE_DUPLICATE
     * @generated
     * @ordered
     */
	public static final IdentifierGenerationOptionType REPLACE_DUPLICATE_LITERAL = new IdentifierGenerationOptionType(REPLACE_DUPLICATE, "ReplaceDuplicate", "ReplaceDuplicate");

	/**
     * The '<em><b>Generate New</b></em>' literal object.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #GENERATE_NEW
     * @generated
     * @ordered
     */
	public static final IdentifierGenerationOptionType GENERATE_NEW_LITERAL = new IdentifierGenerationOptionType(GENERATE_NEW, "GenerateNew", "GenerateNew");

	/**
     * An array of all the '<em><b>Identifier Generation Option Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private static final IdentifierGenerationOptionType[] VALUES_ARRAY =
		new IdentifierGenerationOptionType[] {
            USE_EXISTING_LITERAL,
            REPLACE_DUPLICATE_LITERAL,
            GENERATE_NEW_LITERAL,
        };

	/**
     * A public read-only list of all the '<em><b>Identifier Generation Option Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
     * Returns the '<em><b>Identifier Generation Option Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public static IdentifierGenerationOptionType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            IdentifierGenerationOptionType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

	/**
     * Returns the '<em><b>Identifier Generation Option Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public static IdentifierGenerationOptionType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            IdentifierGenerationOptionType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

	/**
     * Returns the '<em><b>Identifier Generation Option Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public static IdentifierGenerationOptionType get(int value) {
        switch (value) {
            case USE_EXISTING: return USE_EXISTING_LITERAL;
            case REPLACE_DUPLICATE: return REPLACE_DUPLICATE_LITERAL;
            case GENERATE_NEW: return GENERATE_NEW_LITERAL;
        }
        return null;
    }

	/**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private IdentifierGenerationOptionType(int value, String name, String literal) {
        super(value, name, literal);
    }

} //IdentifierGenerationOptionType
