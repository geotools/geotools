/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.xs;

import org.picocontainer.MutablePicoContainer;
import org.geotools.xml.Configuration;
import org.geotools.xs.bindings.XSAllBinding;
import org.geotools.xs.bindings.XSAllNNIBinding;
import org.geotools.xs.bindings.XSAnnotatedBinding;
import org.geotools.xs.bindings.XSAnySimpleTypeBinding;
import org.geotools.xs.bindings.XSAnyTypeBinding;
import org.geotools.xs.bindings.XSAnyURIBinding;
import org.geotools.xs.bindings.XSAttributeBinding;
import org.geotools.xs.bindings.XSAttributeGroupBinding;
import org.geotools.xs.bindings.XSAttributeGroupRefBinding;
import org.geotools.xs.bindings.XSBase64BinaryBinding;
import org.geotools.xs.bindings.XSBlockSetBinding;
import org.geotools.xs.bindings.XSBooleanBinding;
import org.geotools.xs.bindings.XSByteBinding;
import org.geotools.xs.bindings.XSComplexRestrictionTypeBinding;
import org.geotools.xs.bindings.XSComplexTypeBinding;
import org.geotools.xs.bindings.XSDateBinding;
import org.geotools.xs.bindings.XSDateTimeBinding;
import org.geotools.xs.bindings.XSDecimalBinding;
import org.geotools.xs.bindings.XSDerivationControlBinding;
import org.geotools.xs.bindings.XSDerivationSetBinding;
import org.geotools.xs.bindings.XSDoubleBinding;
import org.geotools.xs.bindings.XSDurationBinding;
import org.geotools.xs.bindings.XSENTITIESBinding;
import org.geotools.xs.bindings.XSENTITYBinding;
import org.geotools.xs.bindings.XSElementBinding;
import org.geotools.xs.bindings.XSExplicitGroupBinding;
import org.geotools.xs.bindings.XSExtensionTypeBinding;
import org.geotools.xs.bindings.XSFacetBinding;
import org.geotools.xs.bindings.XSFloatBinding;
import org.geotools.xs.bindings.XSFormChoiceBinding;
import org.geotools.xs.bindings.XSFullDerivationSetBinding;
import org.geotools.xs.bindings.XSGDayBinding;
import org.geotools.xs.bindings.XSGMonthBinding;
import org.geotools.xs.bindings.XSGMonthDayBinding;
import org.geotools.xs.bindings.XSGYearBinding;
import org.geotools.xs.bindings.XSGYearMonthBinding;
import org.geotools.xs.bindings.XSGroupBinding;
import org.geotools.xs.bindings.XSGroupRefBinding;
import org.geotools.xs.bindings.XSHexBinaryBinding;
import org.geotools.xs.bindings.XSIDBinding;
import org.geotools.xs.bindings.XSIDREFBinding;
import org.geotools.xs.bindings.XSIDREFSBinding;
import org.geotools.xs.bindings.XSIntBinding;
import org.geotools.xs.bindings.XSIntegerBinding;
import org.geotools.xs.bindings.XSKeybaseBinding;
import org.geotools.xs.bindings.XSLanguageBinding;
import org.geotools.xs.bindings.XSLocalComplexTypeBinding;
import org.geotools.xs.bindings.XSLocalElementBinding;
import org.geotools.xs.bindings.XSLocalSimpleTypeBinding;
import org.geotools.xs.bindings.XSLongBinding;
import org.geotools.xs.bindings.XSNCNameBinding;
import org.geotools.xs.bindings.XSNMTOKENBinding;
import org.geotools.xs.bindings.XSNMTOKENSBinding;
import org.geotools.xs.bindings.XSNOTATIONBinding;
import org.geotools.xs.bindings.XSNameBinding;
import org.geotools.xs.bindings.XSNamedAttributeGroupBinding;
import org.geotools.xs.bindings.XSNamedGroupBinding;
import org.geotools.xs.bindings.XSNamespaceListBinding;
import org.geotools.xs.bindings.XSNarrowMaxMinBinding;
import org.geotools.xs.bindings.XSNegativeIntegerBinding;
import org.geotools.xs.bindings.XSNoFixedFacetBinding;
import org.geotools.xs.bindings.XSNonNegativeIntegerBinding;
import org.geotools.xs.bindings.XSNonPositiveIntegerBinding;
import org.geotools.xs.bindings.XSNormalizedStringBinding;
import org.geotools.xs.bindings.XSNumFacetBinding;
import org.geotools.xs.bindings.XSOpenAttrsBinding;
import org.geotools.xs.bindings.XSPositiveIntegerBinding;
import org.geotools.xs.bindings.XSPublicBinding;
import org.geotools.xs.bindings.XSQNameBinding;
import org.geotools.xs.bindings.XSRealGroupBinding;
import org.geotools.xs.bindings.XSReducedDerivationControlBinding;
import org.geotools.xs.bindings.XSRestrictionTypeBinding;
import org.geotools.xs.bindings.XSShortBinding;
import org.geotools.xs.bindings.XSSimpleDerivationSetBinding;
import org.geotools.xs.bindings.XSSimpleExplicitGroupBinding;
import org.geotools.xs.bindings.XSSimpleExtensionTypeBinding;
import org.geotools.xs.bindings.XSSimpleRestrictionTypeBinding;
import org.geotools.xs.bindings.XSSimpleTypeBinding;
import org.geotools.xs.bindings.XSStringBinding;
import org.geotools.xs.bindings.XSTimeBinding;
import org.geotools.xs.bindings.XSTokenBinding;
import org.geotools.xs.bindings.XSTopLevelAttributeBinding;
import org.geotools.xs.bindings.XSTopLevelComplexTypeBinding;
import org.geotools.xs.bindings.XSTopLevelElementBinding;
import org.geotools.xs.bindings.XSTopLevelSimpleTypeBinding;
import org.geotools.xs.bindings.XSTypeDerivationControlBinding;
import org.geotools.xs.bindings.XSUnsignedByteBinding;
import org.geotools.xs.bindings.XSUnsignedIntBinding;
import org.geotools.xs.bindings.XSUnsignedLongBinding;
import org.geotools.xs.bindings.XSUnsignedShortBinding;
import org.geotools.xs.bindings.XSWildcardBinding;


/**
 * Parser configuration for xml schema schema.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class XSConfiguration extends Configuration {
    public XSConfiguration() {
        super(XS.getInstance());
    }

    protected final void registerBindings(MutablePicoContainer container) {
        container.registerComponentImplementation(XS.ALL, XSAllBinding.class);
        container.registerComponentImplementation(XS.ALLNNI, XSAllNNIBinding.class);
        container.registerComponentImplementation(XS.ANNOTATED, XSAnnotatedBinding.class);
        container.registerComponentImplementation(XS.ANYSIMPLETYPE, XSAnySimpleTypeBinding.class);
        container.registerComponentImplementation(XS.ANYTYPE, XSAnyTypeBinding.class);
        container.registerComponentImplementation(XS.ANYURI, XSAnyURIBinding.class);
        container.registerComponentImplementation(XS.ATTRIBUTE, XSAttributeBinding.class);
        container.registerComponentImplementation(XS.ATTRIBUTEGROUP, XSAttributeGroupBinding.class);
        container.registerComponentImplementation(XS.ATTRIBUTEGROUPREF,
            XSAttributeGroupRefBinding.class);
        container.registerComponentImplementation(XS.BASE64BINARY, XSBase64BinaryBinding.class);
        container.registerComponentImplementation(XS.BLOCKSET, XSBlockSetBinding.class);
        container.registerComponentImplementation(XS.BOOLEAN, XSBooleanBinding.class);
        container.registerComponentImplementation(XS.BYTE, XSByteBinding.class);
        container.registerComponentImplementation(XS.COMPLEXRESTRICTIONTYPE,
            XSComplexRestrictionTypeBinding.class);
        container.registerComponentImplementation(XS.COMPLEXTYPE, XSComplexTypeBinding.class);
        container.registerComponentImplementation(XS.DATE, XSDateBinding.class);
        container.registerComponentImplementation(XS.DATETIME, XSDateTimeBinding.class);
        container.registerComponentImplementation(XS.DECIMAL, XSDecimalBinding.class);
        container.registerComponentImplementation(XS.DERIVATIONCONTROL,
            XSDerivationControlBinding.class);
        container.registerComponentImplementation(XS.DERIVATIONSET, XSDerivationSetBinding.class);
        container.registerComponentImplementation(XS.DOUBLE, XSDoubleBinding.class);
        container.registerComponentImplementation(XS.DURATION, XSDurationBinding.class);
        container.registerComponentImplementation(XS.ELEMENT, XSElementBinding.class);
        container.registerComponentImplementation(XS.ENTITIES, XSENTITIESBinding.class);
        container.registerComponentImplementation(XS.ENTITY, XSENTITYBinding.class);
        container.registerComponentImplementation(XS.EXPLICITGROUP, XSExplicitGroupBinding.class);
        container.registerComponentImplementation(XS.EXTENSIONTYPE, XSExtensionTypeBinding.class);
        container.registerComponentImplementation(XS.FACET, XSFacetBinding.class);
        container.registerComponentImplementation(XS.FLOAT, XSFloatBinding.class);
        container.registerComponentImplementation(XS.FORMCHOICE, XSFormChoiceBinding.class);
        container.registerComponentImplementation(XS.FULLDERIVATIONSET,
            XSFullDerivationSetBinding.class);
        container.registerComponentImplementation(XS.GDAY, XSGDayBinding.class);
        container.registerComponentImplementation(XS.GMONTH, XSGMonthBinding.class);
        container.registerComponentImplementation(XS.GMONTHDAY, XSGMonthDayBinding.class);
        container.registerComponentImplementation(XS.GROUP, XSGroupBinding.class);
        container.registerComponentImplementation(XS.GROUPREF, XSGroupRefBinding.class);
        container.registerComponentImplementation(XS.GYEAR, XSGYearBinding.class);
        container.registerComponentImplementation(XS.GYEARMONTH, XSGYearMonthBinding.class);
        container.registerComponentImplementation(XS.HEXBINARY, XSHexBinaryBinding.class);
        container.registerComponentImplementation(XS.ID, XSIDBinding.class);
        container.registerComponentImplementation(XS.IDREF, XSIDREFBinding.class);
        container.registerComponentImplementation(XS.IDREFS, XSIDREFSBinding.class);
        container.registerComponentImplementation(XS.INT, XSIntBinding.class);
        container.registerComponentImplementation(XS.INTEGER, XSIntegerBinding.class);
        container.registerComponentImplementation(XS.KEYBASE, XSKeybaseBinding.class);
        container.registerComponentImplementation(XS.LANGUAGE, XSLanguageBinding.class);
        container.registerComponentImplementation(XS.LOCALCOMPLEXTYPE,
            XSLocalComplexTypeBinding.class);
        container.registerComponentImplementation(XS.LOCALELEMENT, XSLocalElementBinding.class);
        container.registerComponentImplementation(XS.LOCALSIMPLETYPE, XSLocalSimpleTypeBinding.class);
        container.registerComponentImplementation(XS.LONG, XSLongBinding.class);
        container.registerComponentImplementation(XS.NAME, XSNameBinding.class);
        container.registerComponentImplementation(XS.NAMEDATTRIBUTEGROUP,
            XSNamedAttributeGroupBinding.class);
        container.registerComponentImplementation(XS.NAMEDGROUP, XSNamedGroupBinding.class);
        container.registerComponentImplementation(XS.NAMESPACELIST, XSNamespaceListBinding.class);
        container.registerComponentImplementation(XS.NARROWMAXMIN, XSNarrowMaxMinBinding.class);
        container.registerComponentImplementation(XS.NCNAME, XSNCNameBinding.class);
        container.registerComponentImplementation(XS.NEGATIVEINTEGER, XSNegativeIntegerBinding.class);
        container.registerComponentImplementation(XS.NMTOKEN, XSNMTOKENBinding.class);
        container.registerComponentImplementation(XS.NMTOKENS, XSNMTOKENSBinding.class);
        container.registerComponentImplementation(XS.NOFIXEDFACET, XSNoFixedFacetBinding.class);
        container.registerComponentImplementation(XS.NONNEGATIVEINTEGER,
            XSNonNegativeIntegerBinding.class);
        container.registerComponentImplementation(XS.NONPOSITIVEINTEGER,
            XSNonPositiveIntegerBinding.class);
        container.registerComponentImplementation(XS.NORMALIZEDSTRING,
            XSNormalizedStringBinding.class);
        container.registerComponentImplementation(XS.NOTATION, XSNOTATIONBinding.class);
        container.registerComponentImplementation(XS.NUMFACET, XSNumFacetBinding.class);
        container.registerComponentImplementation(XS.OPENATTRS, XSOpenAttrsBinding.class);
        container.registerComponentImplementation(XS.POSITIVEINTEGER, XSPositiveIntegerBinding.class);
        container.registerComponentImplementation(XS.PUBLIC, XSPublicBinding.class);
        container.registerComponentImplementation(XS.QNAME, XSQNameBinding.class);
        container.registerComponentImplementation(XS.REALGROUP, XSRealGroupBinding.class);
        container.registerComponentImplementation(XS.REDUCEDDERIVATIONCONTROL,
            XSReducedDerivationControlBinding.class);
        container.registerComponentImplementation(XS.RESTRICTIONTYPE, XSRestrictionTypeBinding.class);
        container.registerComponentImplementation(XS.SHORT, XSShortBinding.class);
        container.registerComponentImplementation(XS.SIMPLEDERIVATIONSET,
            XSSimpleDerivationSetBinding.class);
        container.registerComponentImplementation(XS.SIMPLEEXPLICITGROUP,
            XSSimpleExplicitGroupBinding.class);
        container.registerComponentImplementation(XS.SIMPLEEXTENSIONTYPE,
            XSSimpleExtensionTypeBinding.class);
        container.registerComponentImplementation(XS.SIMPLERESTRICTIONTYPE,
            XSSimpleRestrictionTypeBinding.class);
        container.registerComponentImplementation(XS.SIMPLETYPE, XSSimpleTypeBinding.class);
        container.registerComponentImplementation(XS.STRING, XSStringBinding.class);
        container.registerComponentImplementation(XS.TIME, XSTimeBinding.class);
        container.registerComponentImplementation(XS.TOKEN, XSTokenBinding.class);
        container.registerComponentImplementation(XS.TOPLEVELATTRIBUTE,
            XSTopLevelAttributeBinding.class);
        container.registerComponentImplementation(XS.TOPLEVELCOMPLEXTYPE,
            XSTopLevelComplexTypeBinding.class);
        container.registerComponentImplementation(XS.TOPLEVELELEMENT, XSTopLevelElementBinding.class);
        container.registerComponentImplementation(XS.TOPLEVELSIMPLETYPE,
            XSTopLevelSimpleTypeBinding.class);
        container.registerComponentImplementation(XS.TYPEDERIVATIONCONTROL,
            XSTypeDerivationControlBinding.class);
        container.registerComponentImplementation(XS.UNSIGNEDBYTE, XSUnsignedByteBinding.class);
        container.registerComponentImplementation(XS.UNSIGNEDINT, XSUnsignedIntBinding.class);
        container.registerComponentImplementation(XS.UNSIGNEDLONG, XSUnsignedLongBinding.class);
        container.registerComponentImplementation(XS.UNSIGNEDSHORT, XSUnsignedShortBinding.class);
        container.registerComponentImplementation(XS.WILDCARD, XSWildcardBinding.class);
    }
}
