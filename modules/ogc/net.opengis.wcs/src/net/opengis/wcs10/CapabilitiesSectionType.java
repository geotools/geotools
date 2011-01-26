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
 * A representation of the literals of the enumeration '<em><b>Capabilities Section Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Identification of desired part of full Capabilities XML document to be returned.
 * <!-- end-model-doc -->
 * @see net.opengis.wcs10.Wcs10Package#getCapabilitiesSectionType()
 * @model extendedMetaData="name='CapabilitiesSectionType'"
 * @generated
 */
public final class CapabilitiesSectionType extends AbstractEnumerator {
    /**
	 * The '<em><b></b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * TBD.
	 * <!-- end-model-doc -->
	 * @see #__LITERAL
	 * @model literal="/"
	 * @generated
	 * @ordered
	 */
    public static final int _ = 0;

    /**
	 * The '<em><b>WCS Capabilities Service</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * TBD.
	 * <!-- end-model-doc -->
	 * @see #WCS_CAPABILITIES_SERVICE_LITERAL
	 * @model name="WCSCapabilitiesService" literal="/WCS_Capabilities/Service"
	 * @generated
	 * @ordered
	 */
    public static final int WCS_CAPABILITIES_SERVICE = 1;

    /**
	 * The '<em><b>WCS Capabilities Capability</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * TBD.
	 * <!-- end-model-doc -->
	 * @see #WCS_CAPABILITIES_CAPABILITY_LITERAL
	 * @model name="WCSCapabilitiesCapability" literal="/WCS_Capabilities/Capability"
	 * @generated
	 * @ordered
	 */
    public static final int WCS_CAPABILITIES_CAPABILITY = 2;

    /**
	 * The '<em><b>WCS Capabilities Content Metadata</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * TBD.
	 * <!-- end-model-doc -->
	 * @see #WCS_CAPABILITIES_CONTENT_METADATA_LITERAL
	 * @model name="WCSCapabilitiesContentMetadata" literal="/WCS_Capabilities/ContentMetadata"
	 * @generated
	 * @ordered
	 */
    public static final int WCS_CAPABILITIES_CONTENT_METADATA = 3;

    /**
	 * The '<em><b></b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #_
	 * @generated
	 * @ordered
	 */
    public static final CapabilitiesSectionType __LITERAL = new CapabilitiesSectionType(_, "_", "/");

    /**
	 * The '<em><b>WCS Capabilities Service</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #WCS_CAPABILITIES_SERVICE
	 * @generated
	 * @ordered
	 */
    public static final CapabilitiesSectionType WCS_CAPABILITIES_SERVICE_LITERAL = new CapabilitiesSectionType(WCS_CAPABILITIES_SERVICE, "WCSCapabilitiesService", "/WCS_Capabilities/Service");

    /**
	 * The '<em><b>WCS Capabilities Capability</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #WCS_CAPABILITIES_CAPABILITY
	 * @generated
	 * @ordered
	 */
    public static final CapabilitiesSectionType WCS_CAPABILITIES_CAPABILITY_LITERAL = new CapabilitiesSectionType(WCS_CAPABILITIES_CAPABILITY, "WCSCapabilitiesCapability", "/WCS_Capabilities/Capability");

    /**
	 * The '<em><b>WCS Capabilities Content Metadata</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #WCS_CAPABILITIES_CONTENT_METADATA
	 * @generated
	 * @ordered
	 */
    public static final CapabilitiesSectionType WCS_CAPABILITIES_CONTENT_METADATA_LITERAL = new CapabilitiesSectionType(WCS_CAPABILITIES_CONTENT_METADATA, "WCSCapabilitiesContentMetadata", "/WCS_Capabilities/ContentMetadata");

    /**
	 * An array of all the '<em><b>Capabilities Section Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private static final CapabilitiesSectionType[] VALUES_ARRAY =
        new CapabilitiesSectionType[] {
			__LITERAL,
			WCS_CAPABILITIES_SERVICE_LITERAL,
			WCS_CAPABILITIES_CAPABILITY_LITERAL,
			WCS_CAPABILITIES_CONTENT_METADATA_LITERAL,
		};

    /**
	 * A public read-only list of all the '<em><b>Capabilities Section Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
	 * Returns the '<em><b>Capabilities Section Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static CapabilitiesSectionType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			CapabilitiesSectionType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

    /**
	 * Returns the '<em><b>Capabilities Section Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static CapabilitiesSectionType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			CapabilitiesSectionType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

    /**
	 * Returns the '<em><b>Capabilities Section Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static CapabilitiesSectionType get(int value) {
		switch (value) {
			case _: return __LITERAL;
			case WCS_CAPABILITIES_SERVICE: return WCS_CAPABILITIES_SERVICE_LITERAL;
			case WCS_CAPABILITIES_CAPABILITY: return WCS_CAPABILITIES_CAPABILITY_LITERAL;
			case WCS_CAPABILITIES_CONTENT_METADATA: return WCS_CAPABILITIES_CONTENT_METADATA_LITERAL;
		}
		return null;
	}

    /**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private CapabilitiesSectionType(int value, String name, String literal) {
		super(value, name, literal);
	}

} //CapabilitiesSectionType
