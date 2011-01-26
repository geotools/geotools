/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Metadata Type Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.wcs10.Wcs10Package#getMetadataTypeType()
 * @model extendedMetaData="name='metadataType_._type'"
 * @generated
 */
public final class MetadataTypeType extends AbstractEnumerator {
    /**
	 * The '<em><b>TC211</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This metadata uses a profile of ISO TC211Õs Geospatial Metadata Standard 19115.
	 * <!-- end-model-doc -->
	 * @see #TC211_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
    public static final int TC211 = 0;

    /**
	 * The '<em><b>FGDC</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This metadata uses a profile of the US FGDC Content Standard for Digital Geospatial Metadata.
	 * <!-- end-model-doc -->
	 * @see #FGDC_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
    public static final int FGDC = 1;

    /**
	 * The '<em><b>Other</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This metadata uses some other metadata standard(s) and/or no standard.
	 * <!-- end-model-doc -->
	 * @see #OTHER_LITERAL
	 * @model name="other"
	 * @generated
	 * @ordered
	 */
    public static final int OTHER = 2;

    /**
	 * The '<em><b>TC211</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #TC211
	 * @generated
	 * @ordered
	 */
    public static final MetadataTypeType TC211_LITERAL = new MetadataTypeType(TC211, "TC211", "TC211");

    /**
	 * The '<em><b>FGDC</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #FGDC
	 * @generated
	 * @ordered
	 */
    public static final MetadataTypeType FGDC_LITERAL = new MetadataTypeType(FGDC, "FGDC", "FGDC");

    /**
	 * The '<em><b>Other</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #OTHER
	 * @generated
	 * @ordered
	 */
    public static final MetadataTypeType OTHER_LITERAL = new MetadataTypeType(OTHER, "other", "other");

    /**
	 * An array of all the '<em><b>Metadata Type Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private static final MetadataTypeType[] VALUES_ARRAY =
        new MetadataTypeType[] {
			TC211_LITERAL,
			FGDC_LITERAL,
			OTHER_LITERAL,
		};

    /**
	 * A public read-only list of all the '<em><b>Metadata Type Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
	 * Returns the '<em><b>Metadata Type Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static MetadataTypeType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			MetadataTypeType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

    /**
	 * Returns the '<em><b>Metadata Type Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static MetadataTypeType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			MetadataTypeType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

    /**
	 * Returns the '<em><b>Metadata Type Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static MetadataTypeType get(int value) {
		switch (value) {
			case TC211: return TC211_LITERAL;
			case FGDC: return FGDC_LITERAL;
			case OTHER: return OTHER_LITERAL;
		}
		return null;
	}

    /**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private MetadataTypeType(int value, String name, String literal) {
		super(value, name, literal);
	}

} //MetadataTypeType
