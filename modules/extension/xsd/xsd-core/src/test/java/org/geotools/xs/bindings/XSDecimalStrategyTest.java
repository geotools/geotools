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
package org.geotools.xs.bindings;

import java.math.BigDecimal;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDMaxExclusiveFacet;
import org.eclipse.xsd.XSDMaxInclusiveFacet;
import org.eclipse.xsd.XSDMinExclusiveFacet;
import org.eclipse.xsd.XSDMinInclusiveFacet;
import org.eclipse.xsd.XSDTotalDigitsFacet;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.impl.XSDElementDeclarationImpl;
import org.eclipse.xsd.impl.XSDMaxExclusiveFacetImpl;
import org.eclipse.xsd.impl.XSDMaxInclusiveFacetImpl;
import org.eclipse.xsd.impl.XSDMinExclusiveFacetImpl;
import org.eclipse.xsd.impl.XSDMinInclusiveFacetImpl;
import org.eclipse.xsd.impl.XSDSimpleTypeDefinitionImpl;
import org.eclipse.xsd.impl.XSDTotalDigitsFacetImpl;
import org.geotools.xs.TestSchema;
import org.geotools.xs.XS;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.impl.ElementImpl;
import org.junit.Assert;
import org.junit.Test;

public class XSDecimalStrategyTest extends TestSchema {

    @Override
    protected QName getQName() {
        return XS.DECIMAL;
    }

    /*
     * Test method for 'org.geotools.xml.strategies.xs.XSDecimalStrategy.parse(Element, Node[], Object)'
     */
    @Test
    public void testParse() throws Exception {
        // Valid values
        validateValues("50", 5, 0, 0, 100, 100);
        validateValues("50", 3, 49, 50, 50, 51);

        validateValues("50.0", 3, 49, 50, 51, 52);
        validateValues("50.8", 4, 49, 50, 51, 52);
        validateValues("51.0", 4, 49, 50, 51, 52);

        // Invalid values
        // TODO: enable these test when facets have been properly implemented
        //		try {
        //			validateValues("50", 1, 49, 50, 51, 52);
        //			assertTrue("Exception should have been thrown.", false);
        //		} catch (ValidationException e) {
        //
        //		}
        //
        //		try {
        //			validateValues("48", 2, 49, 50, 51, 52);
        //			assertTrue("Exception should have been thrown.", false);
        //		} catch (ValidationException e) {
        //
        //		}
        //
        //		try {
        //			validateValues("49", 2, 49, 50, 51, 52);
        //			assertTrue("Exception should have been thrown.", false);
        //		} catch (ValidationException e) {
        //
        //		}
        //
        //		try {
        //			validateValues("52", 2, 49, 50, 51, 52);
        //			assertTrue("Exception should have been thrown.", false);
        //		} catch (ValidationException e) {
        //
        //		}
        //
        //		try {
        //			validateValues("53", 2, 49, 50, 51, 52);
        //			assertTrue("Exception should have been thrown.", false);
        //		} catch (ValidationException e) {
        //
        //		}
    }

    /**
     * GEOT-7072: Non-comformant WFS implementations tend to send empty elements (e.g. {@code
     * <value></value>})
     */
    @Test
    public void testParseEmptyStringAsNull() throws Exception {
        validateValues("", null);
        validateValues("\t", null);
    }

    public void validateValues(
            String elementText,
            int totalDigits,
            double minExc,
            double minInc,
            double maxInc,
            double maxExc)
            throws Exception {
        XSDecimalBinding strat = new XSDecimalBinding();

        XSDElementDeclaration declaration =
                makeDeclaration(
                        totalDigits,
                        BigDecimal.valueOf(minExc),
                        BigDecimal.valueOf(minInc),
                        BigDecimal.valueOf(maxInc),
                        BigDecimal.valueOf(maxExc));

        ElementInstance element = new ElementImpl(declaration);
        element.setText(elementText);

        BigDecimal decimal = (BigDecimal) strat.parse(element, element.getText().trim());

        Assert.assertNotNull(decimal);
    }

    private XSDElementDeclaration makeDeclaration(
            final int digits,
            final BigDecimal minExc,
            final BigDecimal minInc,
            final BigDecimal maxInc,
            final BigDecimal maxExc) {
        return new XSDElementDeclarationImpl() {
            @Override
            public XSDTypeDefinition getTypeDefinition() {
                return new XSDSimpleTypeDefinitionImpl() {
                    @Override
                    public XSDTotalDigitsFacet getTotalDigitsFacet() {
                        return new XSDTotalDigitsFacetImpl() {
                            @Override
                            public int getValue() {
                                return digits;
                            }
                        };
                    }

                    @Override
                    public XSDMinInclusiveFacet getMinInclusiveFacet() {
                        return new XSDMinInclusiveFacetImpl() {
                            @Override
                            public Object getValue() {
                                return minInc;
                            }
                        };
                    }

                    @Override
                    public XSDMinExclusiveFacet getMinExclusiveFacet() {
                        return new XSDMinExclusiveFacetImpl() {
                            @Override
                            public Object getValue() {
                                return minExc;
                            }
                        };
                    }

                    @Override
                    public XSDMaxInclusiveFacet getMaxInclusiveFacet() {
                        return new XSDMaxInclusiveFacetImpl() {
                            @Override
                            public Object getValue() {
                                return maxInc;
                            }
                        };
                    }

                    @Override
                    public XSDMaxExclusiveFacet getMaxExclusiveFacet() {
                        return new XSDMaxExclusiveFacetImpl() {
                            @Override
                            public Object getValue() {
                                return maxExc;
                            }
                        };
                    }
                };
            }
        };
    }
}
