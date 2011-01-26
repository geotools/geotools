/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.cql2;

import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.PropertyName;

/**
 * Test Attribute
 * <p>
 *
 * <pre>
 *  &lt;attribute name &gt; ::=
 *          &lt;simple attribute name &gt;
 *      |   &lt;compound attribute name &gt;
 *      
 *  &lt;simple attribute name &gt; ::=  &lt;identifier&gt;| &lt;double quote&gt; &lt;any character&gt; &lt;double quote&gt;
 *  
 *  &lt;compound attribute name &gt; ::=  &lt;identifier &gt; &lt;period &gt; [{ &lt;identifier &gt; &lt;period &gt;}...] &lt;simple attribute name &gt;
 *  
 *  &lt;identifier &gt; ::=  &lt;identifier start &gt; [ {  &lt;colon &gt; |  &lt;identifier part &gt; }... ]
 *  &lt;identifier start &gt; ::=  &lt;simple Latin letter &gt;
 *  &lt;identifier part &gt; ::=  &lt;simple Latin letter &gt; |  &lt;digit &gt;
 * </pre>
 * 
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.7
 * 
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/library/cql/src/test/java/org/geotools/filter/text/cql/CQLAttributeNameTest $
 * </p>
 */
public class CQLAttributeNameTest  {
	
    protected final Language language;
    
    

    public CQLAttributeNameTest(){
        
        this(Language.CQL);
    }
    public CQLAttributeNameTest(final Language language){
        
        assert language != null: "language cannot be null value";
        
        this.language = language;
    }


    @Test
    public void simpleAttribute() throws CQLException {
        testAttribute("startPart");
    }
    
    @Test
    public void simpleAttribureWithColon() throws CQLException {
        testAttribute("startpart:part1:part2");
    }
    
    @Test
    public void compoundAttributeName() throws CQLException {
        testAttribute("s11:p12:p13.s21:p22.s31:p32");
        
        testAttribute(
        	"gmd:MD_Metadata.gmd:identificationInfo.gmd:MD_DataIdentification.gmd:abstract");

    }
    

    /**
     * Invalid attribute names
     * @throws CQLException 
     */
    @Test(expected = CQLException.class)    
    public void invalidAttribute() throws CQLException  {
        	
    	//Invalid Attribute is expected: the identifier can not begin with number. 
    	testAttribute("1startPart");

    	//Invalid Attribute is expected: the compound attribute should have \":\"
    	testAttribute("startpart part1");             
    	
    }

	
	/**
	 * Using a CQL Keyword as property name 
	 * 
	 * 
	 * @throws Exception
	 */
    @Test 
    public void keywordAsAttribute() throws CQLException {

    	testAttributeBetweenDoubleQuotes("\"LIKE\"");
    	
    	testAttributeBetweenDoubleQuotes("\"AND\"");

    	testAttributeBetweenDoubleQuotes("\"OR\"");
    }

    /**
     * Using different local characters as property name.
     * 
     * 
     * @throws Exception
     */
    @Test
    public void localCharactersetInAttributeName() throws CQLException {
        
    	testAttributeBetweenDoubleQuotes("\"población\"");

    	testAttributeBetweenDoubleQuotes("\"reconnaître\"");

    	testAttributeBetweenDoubleQuotes("\"können\"");
    	
    	// Russian
    	testAttributeBetweenDoubleQuotes("\"ДОБРИЧ\"");
    	testAttributeBetweenDoubleQuotes("\"название\"");
    	testAttributeBetweenDoubleQuotes("\"фамилия\"");
    	testAttributeBetweenDoubleQuotes("\"среды\"");
    	
    	// Japanese
    	testAttributeBetweenDoubleQuotes("\"名\"");
    	testAttributeBetweenDoubleQuotes("\"姓\"");
    	testAttributeBetweenDoubleQuotes("\"環境\"");
    	
    }


    private void testAttributeBetweenDoubleQuotes(final String attSample) throws CQLException {
        PropertyIsLike result;
        PropertyName attResult = null;

        result = (PropertyIsLike) CompilerUtil.parseFilter(this.language, attSample + " LIKE 'abc%'");

        attResult = (PropertyName) result.getExpression();

        String expected = attSample.replace('.', '/');
        expected = expected.substring(1, expected.length()-1);

        String propertyName = attResult.getPropertyName();
		Assert.assertEquals(expected, propertyName);
    } 

    private void testAttribute(final String attSample) throws CQLException {
        PropertyIsLike result;
        PropertyName attResult = null;

        result = (PropertyIsLike) CompilerUtil.parseFilter(this.language, attSample + " LIKE 'abc%'");

        attResult = (PropertyName) result.getExpression();

        final String expected = attSample.replace('.', '/');

        Assert.assertEquals(expected, attResult.getPropertyName());
    } 

}
