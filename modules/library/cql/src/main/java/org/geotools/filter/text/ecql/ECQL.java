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
package org.geotools.filter.text.ecql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.geotools.filter.FilterTransformer;
import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;


/**
 * <b>Extended Common Query Language (ECQL)</b> is an extension of <b>CQL</b>. This class presents the operations available 
 * to parse the ECQL language and generates the correspondent filter.
 * </p>
 * <p>
 * <h2>Usage</h2>
 * Here are some usage examples. Refer to the <a href="http://docs.codehaus.org/display/GEOTOOLS/CQL+Parser+Design">complete
 * grammar</a> to see what exactly you can do.
 *
 * <pre>
 * <code>
 *       Filter filter = ECQL.toFilter(<b>"POP_RANK  &gt;  6"</b>);
 *        
 *       Filter filter = ECQL.toFilter(<b>"POP_RANK &gt; 3 AND POP_RANK &lt; 6"</b>);
 *        
 *       Filter filter = ECQL.toFilter(<b>"area(the_geom) &gt; 3000"</b>);
 *        
 *       Filter filter = ECQL.toFilter(<b>"Name LIKE '%omer%'"</b>);
 *       
 *       Filter filter = ECQL.toFilter(<b>"RELATE( the_geom1,the_geom2) like 'T**F*****'"</b>);
 *
 *       Filter filter = ECQL.toFilter(<b>"DISJOINT(buffer(the_geom, 10) , POINT(1 2))"</b>);
 *
 *       Filter filter = ECQL.toFilter(<b>"IN ('river.1', 'river.2')"</b>);
 *       
 *       Filter filter = ECQL.toFilter(<b>"LENGHT IN (4100001,4100002, 4100003 )"</b>);
 *
 *       List &lt;Filter&gt; list = ECQL.toFilterList(<b>"LENGHT = 100; NAME like '%omer%'"</b>);
 *
 *       Expression expression = ECQL.toExpression(<b>"LENGHT + 100"</b>);
 *
 * </code>
 * </pre>
 * </p>
 * @author Jody Garnett
 * @author Mauricio Pazos (Axios Engineering)
 * 
 * @since 2.6
 *
 * @source $URL$
 */
public class ECQL {

    private ECQL(){
        // do nothing, private constructor
        // to indicate it is a pure utility class
    }

    /**
     * Parses the input string in ECQL format into a Filter, using the
     * systems default FilterFactory implementation.
     *
     * @param ECQLPredicate
     *            a string containing a query predicate in ECQL format.
     * @return a {@link Filter} equivalent to the constraint specified in
     *         <code>ecqlPredicate</code>.
     */
    public static Filter toFilter(final String ecqlPredicate)
        throws CQLException {
        Filter filter = ECQL.toFilter(ecqlPredicate, null);

        return filter;
    }

    /**
     * Parses the input string in ECQL format into a Filter, using the
     * provided FilterFactory.
     *
     * @param ecqlPredicate
     *            a string containing a query predicate in ECQL format.
     * @param filterFactory
     *            the {@link FilterFactory} to use for the creation of the
     *            Filter. If it is null the method finds the default implementation.
     * @return a {@link Filter} equivalent to the constraint specified in
     *         <code>Predicate</code>.
     */
    public static Filter toFilter(final String ecqlPredicate, final FilterFactory filterFactory)
        throws CQLException {

        ECQLCompilerFactory compilerFactory = new ECQLCompilerFactory();
        Filter result = CompilerUtil.parseFilter(ecqlPredicate, compilerFactory, filterFactory);

        return result;
    }
    

    /**
     * Parses the input string in ECQL format into an Expression, using the
     * systems default {@link FilterFactory}  implementation.
     *
     * @param ecqlExpression  a string containing an ECQL expression.
     * @return a {@link Expression} equivalent to the one specified in
     *         <code>ecqlExpression</code>.
     */
    public static Expression toExpression(String ecqlExpression)
        throws CQLException {
        return toExpression(ecqlExpression, null);
    }

    /**
     * Parses the input string in ECQL format and makes the correspondent Expression , 
     * using the provided FilterFactory.
     *
     * @param ecqlExpression
     *            a string containing a ECQL expression.
     *
     * @param filterFactory
     *            the {@link FilterFactory} to use for the creation of the
     *            Expression. If it is null the method finds the default implementation.    
     * @return a {@link Filter} equivalent to the constraint specified in
     *         <code>ecqlExpression</code>.
     */
    public static Expression toExpression(final String ecqlExpression,
            final FilterFactory filterFactory) throws CQLException {

        ECQLCompilerFactory compilerFactory = new ECQLCompilerFactory();

        Expression expression = CompilerUtil.parseExpression(ecqlExpression, compilerFactory, filterFactory);

        return expression;
    }

    /**
     * Parses the input string, which has to be a list of ECQL predicates
     * separated by "<code>;</code>" into a {@link List} of
     * {@link Filter}, using the provided FilterFactory.
     *
     * @param ecqlSequencePredicate
     *            a list of ECQL predicates separated by "<code>;</code>"
     *
     * @return a List of {@link Filter}, one for each input ECQL statement
     */
    public static List<Filter> toFilterList(final String ecqlSequencePredicate)
        throws CQLException {

        return toFilterList(ecqlSequencePredicate, null);
    }
    
    /**
     * Parses the input string, which has to be a list of ECQL predicates
     * separated by "<code>;</code>" into a {@link List} of
     * {@link Filter}, using the provided FilterFactory.
     *
     * @param ecqlSequencePredicate a ECQL predicate sequence
     * @param filterFactory the factory used to make the filters
     * @return a List of {@link Filter}, one for each input ECQL statement
     * @throws CQLException
     */
    public static List<Filter> toFilterList(final String ecqlSequencePredicate, FilterFactory filterFactory)
        throws CQLException {

        ECQLCompilerFactory compilerFactory = new ECQLCompilerFactory();

        List<Filter> filters = CompilerUtil.parseFilterList(ecqlSequencePredicate, compilerFactory, filterFactory);
        
        return filters;
    
    }
    

    /**
     * WARNING THIS IS A WORK IN PROGRESS.
     * 
     * @param filterList
     * @return
     */
    public static String toCQL( List<Filter> filterList ){
        return CQL.toCQL( filterList );        
    }
    
    /**
     * WARNING THIS IS A WORK IN PROGRESS.
     * 
     * @param filter
     * @return
     */
    public static String toCQL( Filter filter ){
        return CQL.toCQL( filter );        
    }
    /**
     * WARNING THIS IS A WORK IN PROGRESS.
     * 
     * @param filter
     * @return
     */
    public static String toCQL( Expression expression ){
        return CQL.toCQL( expression );        
    }
    
    /**
     * Command line expression tester used to try out filters and expressions.
     * @param args
     */
    public static final void main(String[] args) {
        System.out.println("ECQL Filters Tester");
        System.out.println("Seperate with \";\" or \"quit\" to finish)");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        FilterTransformer filterTransformer = new FilterTransformer();
        filterTransformer.setIndentation(4);

        while (true) {
            System.out.print(">");

            String line = null;

            try {
                line = reader.readLine();

                if (line.equals("quit")) {
                    System.out.println("Bye!");
                    break;
                }

                List<Filter> filters = ECQL.toFilterList(line);
                for( Filter filter : filters ){
                    System.out.println();
                    filterTransformer.transform( filter, System.out);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (CQLException cqlex) {
                System.out.println(cqlex.getSyntaxError());
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }
    }
}
