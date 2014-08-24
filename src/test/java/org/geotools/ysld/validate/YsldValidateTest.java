package org.geotools.ysld.validate;

import org.geotools.ysld.Ysld;
import org.junit.Test;
import org.yaml.snakeyaml.error.MarkedYAMLException;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class YsldValidateTest {

    @Test
    public void testMalformed() throws Exception {
        String ysld =
            "foo:\n" +
            "  foo: bar\n" +
            "   foo: bar\n";
        List<MarkedYAMLException> errors = validate(ysld);
        assertEquals(1, errors.size());

        MarkedYAMLException e = errors.get(0);
        assertEquals(2, e.getProblemMark().getLine());
    }

    @Test
    public void testColor() throws Exception {
        String ysld =
            "fill-color: 21xyz";

        List<MarkedYAMLException> errors = validate(ysld);
        assertEquals(1, errors.size());

        MarkedYAMLException e = errors.get(0);
        assertEquals(0, e.getProblemMark().getLine());
        assertEquals(12, e.getProblemMark().getColumn());
    }

    @Test
    public void testFilter() throws Exception {
        String ysld = "filter: foo";

        List<MarkedYAMLException> errors = validate(ysld);
        assertEquals(1, errors.size());

        MarkedYAMLException e = errors.get(0);
        assertEquals(0, e.getProblemMark().getLine());
        assertEquals(8, e.getProblemMark().getColumn());
    }

    List<MarkedYAMLException> dump(List<MarkedYAMLException> errors) {
        for (MarkedYAMLException e : errors) {
            System.out.println(e.toString());
        }
        return errors;
    }

    List<MarkedYAMLException> validate(String ysld) throws IOException {
        //return dump(Ysld.validate(ysld));
        return Ysld.validate(ysld);
    }
}
