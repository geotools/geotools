/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cqljson.conformance;

import com.github.erosb.jsonsKema.FormatValidationPolicy;
import com.github.erosb.jsonsKema.JsonParser;
import com.github.erosb.jsonsKema.JsonValue;
import com.github.erosb.jsonsKema.Schema;
import com.github.erosb.jsonsKema.SchemaLoader;
import com.github.erosb.jsonsKema.ValidationFailure;
import com.github.erosb.jsonsKema.Validator;
import com.github.erosb.jsonsKema.ValidatorConfig;

public class ConformanceUtils {

    /*
     * About the empty JSON filters and TODOs in the test classes from this package.
     *
     * in the current state of the cql2-text module, we are not able to translate
     * the text filters into JSON, hence leaving an empty JSON object here.
     *
     * If in the need to generate the JSON version, see the org.geotools.filter.text.cql_2.conformance.ConformanceTest17OnlineTest
     * test class and add a new test to translate the filter into JSON, then replace the empty object by the output, e.g.:
     *
     *   <code>
     *    String textCriteria ;
     *    [...]
     *
     *    @Test
     *    public void toJsonText() throws CQLException {
     *        Filter f = CQL2.toFilter(textCriteria);
     *
     *        System.out.println(CQL2Json.toCQL2(f));
     *    }
     *    </code>
     */

    private static Schema schema =
            SchemaLoader.forURL("classpath:/cql2-schema.json").load();
    private static Validator validator = Validator.create(schema, new ValidatorConfig(FormatValidationPolicy.ALWAYS));

    public static ValidationFailure jsonSchemaValidate(String criteria) {
        JsonValue functionsJSON = new JsonParser(criteria).parse();
        ValidationFailure failure = validator.validate(functionsJSON);
        return failure;
    }
}
