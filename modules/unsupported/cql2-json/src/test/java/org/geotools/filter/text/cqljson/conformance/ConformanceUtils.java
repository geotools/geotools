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

    private static Schema schema = SchemaLoader.forURL("classpath:/cql2-schema.json").load();
    private static Validator validator =
            Validator.create(schema, new ValidatorConfig(FormatValidationPolicy.ALWAYS));

    public static ValidationFailure jsonSchemaValidate(String criteria) {
        JsonValue functionsJSON = new JsonParser(criteria).parse();
        ValidationFailure failure = validator.validate(functionsJSON);
        return failure;
    }
}
