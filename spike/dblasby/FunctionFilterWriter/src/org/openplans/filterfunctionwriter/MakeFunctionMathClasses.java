/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.openplans.filterfunctionwriter;


import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Basic idea: 1. for each method in the Math class (or whatever class you
 * specify - see main() ) 2. make a .java file put the header in (ie. includes,
 * etc...) put the actual code in (see "emit()" below) put the footer in (ie.
 * finish the class "}") print the entries that should be put in the service
 * info file to the command line Create tests that show the functions work as
 * they should.
 * 
 * @author dblasby
 * @author Kasper Kaergaard, to adapt for the Math package, and make unit tests
 *         (this should be combined with the original makefunction for something
 *         more generic, but don't have the time right now).
 */
public class MakeFunctionMathClasses {
    private static final String PACKAGE_DECLARATION = "package org.geotools.filter.function.math;";

    private static final String PATH_AND_FILE_NAME_PREFIX = "src/org/geotools/filter/function/math/FilterFunction_";

    private static final String LICENSE = "/*\n"
            + " *    GeoTools - The Open Source Java GIS Toolkit\n"
            + " *    http://geotools.org\n"
            + " *\n"
            + " *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)\n"
            + " *    \n"
            + " *    This library is free software; you can redistribute it and/or\n"
            + " *    modify it under the terms of the GNU Lesser General Public\n"
            + " *    License as published by the Free Software Foundation;\n"
            + " *    version 2.1 of the License.\n"
            + " *\n"
            + " *    This library is distributed in the hope that it will be useful,\n"
            + " *    but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
            + " *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU\n"
            + " *    Lesser General Public License for more details.\n" + " */";


    public static void main(String[] args) {
        MakeFunctionMathClasses cg = new MakeFunctionMathClasses();

        cg.handleClass(Math.class); // parent of all geometry types
    }

    public void handleClass(Class c) {
        try {
            Method[] methods = c.getDeclaredMethods();

            File testFile = new File(PATH_AND_FILE_NAME_PREFIX + "Test"
                    + ".java");

            PrintStream testPrintStream = new PrintStream(new FileOutputStream(
                    testFile));

            createTestFileHeader(testPrintStream);

            for (int t = 0; t < methods.length; t++) {
                Method method = methods[t];
                Type type = method.getGenericReturnType();

                if (!type.toString().equals("void")) {
                    String name = method.getName();
                    File f = new File(PATH_AND_FILE_NAME_PREFIX + name
                            + ".java");
                    int count = 2;

                    while (f.exists()) {
                        name = method.getName() + "_" + count;
                        f = new File(PATH_AND_FILE_NAME_PREFIX + name + ".java");
                        count++;
                    }

                    PrintStream ps = new PrintStream(new FileOutputStream(f));

                    emitHeader(method, ps, name);
                    emitCode(method, ps);
                    emitFooter(method, ps);
                    ps.close();

                    PrintStream printStream = System.out;
                    writeServiceInfo(method, printStream, name);

                    writeTestMethodInTestFile(testPrintStream, method, name);
                }
            }

            writeFooterInTestFile(testPrintStream);
            testPrintStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFooterInTestFile(PrintStream ps) {
        ps.println("}");
    }

    private void writeTestMethodInTestFile(PrintStream ps, Method method,
            String name) {
        try {
            int argsCount = method.getParameterTypes().length;
            double[] values = new double[] { 1, -1, 2, -2, Math.PI,
                    0.5 * Math.PI };
            String[] literalNames = new String[] { "literal_1", "literal_m1",
                    "literal_2", "literal_m2", "literal_pi", "literal_05pi" };

            ps.println("");
            ps.println("public void test" + name + "(){");
            ps.println("	try {");
            ps.println("");
            ps.println("		FunctionExpression " + name
                    + "Function = filterFactory.createFunctionExpression(\""
                    + name + "\");");
            ps.println("		assertEquals(\"Name is, \",\"" + name + "\"," + name
                    + "Function.getName());");
            ps.println("		assertEquals(\"Number of arguments, \"," + argsCount
                    + "," + name + "Function.getArgCount());");
            ps.println("");
            ps.println("		Expression[] expressions = new Expression["
                    + argsCount + "];");

            if (method.getParameterTypes().length > 0) {
                for (int i = 0; i < 6; i++) {
                    String argument = "(" + values[i];
                    ps.println("		expressions[" + 0 + "] = " + literalNames[i]
                            + ";");

                    for (int j = 1; j < argsCount; j++) {
                        int k = i + j;

                        if (k > 5) {
                            k = 0;
                        }

                        argument = argument + "," + values[k];
                        ps.println("		expressions[" + j + "] = "
                                + literalNames[k] + ";");
                    }

                    argument = argument + ")";

                    ps.println("                " + name + "Function.setParameters(java.util.Arrays.asList(expressions));");

                    
                    if (isNumber(method.getReturnType())) {
                        if ((method.getReturnType() == int.class)) {
                            ps
                                    .println("		assertEquals(\""
                                            + method.getName()
                                            + " of "
                                            + argument
                                            + ":\" ,(int)Math."
                                            + method.getName()
                                            + argument
                                            + " ,((Integer) "
                                            + name
                                            + "Function.getValue(null)).intValue(),0.00001);");
                        } else if ((method.getReturnType() == double.class)) {
                            ps.println("		double good" + i + " = Math."
                                    + method.getName() + argument + ";");
                            ps.println("		if(Double.isNaN(good" + i + ")){");
                            ps
                                    .println("			assertTrue(\""
                                            + method.getName()
                                            + " of "
                                            + argument
                                            + ":\" ,Double.isNaN(((Double) "
                                            + name
                                            + "Function.getValue(null)).doubleValue()));");
                            ps.println("        }else{");
                            ps
                                    .println("			assertEquals(\""
                                            + method.getName()
                                            + " of "
                                            + argument
                                            + ":\" ,(double)Math."
                                            + method.getName()
                                            + argument
                                            + " ,((Double) "
                                            + name
                                            + "Function.getValue(null)).doubleValue(),0.00001);");
                            ps.println("		}");
                        } else if ((method.getReturnType() == long.class)) {
                            ps
                                    .println("		assertEquals(\""
                                            + method.getName()
                                            + " of "
                                            + argument
                                            + ":\" ,(long)Math."
                                            + method.getName()
                                            + argument
                                            + " ,((Long) "
                                            + name
                                            + "Function.getValue(null)).longValue(),0.00001);");
                        } else if ((method.getReturnType() == float.class)) {
                            ps
                                    .println("		assertEquals(\""
                                            + method.getName()
                                            + " of "
                                            + argument
                                            + ":\" ,(float)Math."
                                            + method.getName()
                                            + argument
                                            + " ,((Float) "
                                            + name
                                            + "Function.getValue(null)).floatValue(),0.00001);");
                        } else {
                            throw new IllegalArgumentException(
                                    "dont know how to handle this - "
                                            + method.getParameterTypes()[0]);
                        }
                    } else {
                        throw new IllegalArgumentException(
                                "dont know how to handle this - "
                                        + method.getParameterTypes()[0]);
                    }
                }
            }

            ps.println("} catch (FactoryConfigurationError e) {");
            ps.println("	e.printStackTrace();");
            ps.println("	fail(\"Unexpected exception: \"+e.getMessage());");
            ps.println("}");
            ps.println("}");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Method name: " + method.getName());
            System.out.println("Parameter Types: "
                    + method.getParameterTypes().length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTestFileHeader(PrintStream ps) {
        ps.println("package org.geotools.filter.function.math;");
        ps.println("");
        ps.println("");
        ps.println("import org.geotools.factory.FactoryConfigurationError;");
        ps.println("import org.geotools.filter.Expression;");
        ps.println("import org.geotools.filter.FilterFactoryFinder;");
        ps.println("import org.geotools.filter.FilterFactoryImpl;");
        ps.println("import org.geotools.filter.FunctionExpression;");
        ps.println("import org.geotools.filter.LiteralExpression;");
        ps.println("import org.geotools.filter.LiteralExpressionImpl;");
        ps.println("");
        ps.println("import junit.framework.TestCase;");
        ps.println("");
        ps.println("public class FilterFunction_Test extends TestCase{");
        ps.println("");
        ps.println("private LiteralExpressionImpl literal_1 = null;");
        ps.println("private LiteralExpression literal_m1;");
        ps.println("private LiteralExpression literal_2;");
        ps.println("private LiteralExpression literal_m2;");
        ps.println("private LiteralExpression literal_pi;");
        ps.println("private LiteralExpression literal_05pi;");
        ps.println("private FilterFactoryImpl filterFactory;");
        ps.println("protected void setUp() throws Exception {");
        ps.println("super.setUp();");
        ps
                .println("filterFactory = (FilterFactoryImpl) FilterFactoryFinder.createFilterFactory();");
        ps
                .println("literal_1 = (LiteralExpressionImpl) filterFactory.createLiteralExpression();");
        ps.println("literal_pi = filterFactory.createLiteralExpression();");
        ps.println("literal_05pi = filterFactory.createLiteralExpression();");
        ps.println("literal_m1 = filterFactory.createLiteralExpression();");
        ps.println("literal_2 = filterFactory.createLiteralExpression();");
        ps.println("literal_m2 = filterFactory.createLiteralExpression();");
        ps.println("");
        ps.println("literal_1.setLiteral(new Double(1));");
        ps.println("literal_m1.setLiteral(new Double(-1));");
        ps.println("literal_2.setLiteral(new Double(2));");
        ps.println("literal_m2.setLiteral(new Double(-2));");
        ps.println("literal_pi.setLiteral(new Double(Math.PI));");
        ps.println("literal_05pi.setLiteral(new Double(0.5*Math.PI));");
        ps
                .println("assertEquals(\"Literal Expression 0.0\",new Double(1.0), literal_1.getLiteral());");
        ps
                .println("assertEquals(\"Literal Expression pi\",new Double(Math.PI), literal_pi.getLiteral());");
        ps
                .println("assertEquals(\"Literal Expression 05pi\",new Double(0.5*Math.PI), literal_05pi.getLiteral());");
        ps.println("");
        ps.println("}");
        ps.println("");
        ps.println("protected void tearDown() throws Exception {");
        ps.println("	super.tearDown();");
        ps.println("}");
    }

    public void emitHeader(Method m, PrintStream printstream, String name) {
        printstream.println(PACKAGE_DECLARATION);
        printstream.println(LICENSE);
        printstream.println("");
        printstream.println("");
        printstream
                .println("//this code is autogenerated - you shouldnt be modifying it!");
        printstream.println("");

        printstream.println("import org.geotools.feature.Feature;");
        // printstream.println("import org.geotools.filter.Expression;");
        // /printstream.println("import org.geotools.filter.FilterFactory;");
        // /printstream.println("import
        // org.geotools.filter.FunctionExpression;");
        printstream
                .println("import org.geotools.filter.FunctionExpressionImpl;");
        // printstream.println("import org.geotools.filter.LiteralExpression;");
        printstream.println("");
        printstream.println("");
        printstream.println("public class " + "FilterFunction_" + name
                + " extends FunctionExpressionImpl");
        printstream.println("{");
        printstream.println("");
        printstream.println("");
        printstream.println("   public FilterFunction_" + name + "(){");
        printstream.println("           super(\"" + name + "\");");
        printstream.println("   }");
        printstream.println("");
        printstream.println("");
        printstream.println("   public int getArgCount(){");
        printstream.println("      return " + m.getParameterTypes().length
                + ";");
        printstream.println("   }");
        printstream.println("");
        printstream.println("");
    }

    public void emitFooter(Method m, PrintStream printstream) {
        printstream.println("}");
        printstream.println("");
    }

    public void emitCode(Method m, PrintStream printstream) {
        printstream.println("   public Object evaluate(Feature feature){");

        // variable decs
        for (int t = 0; t < m.getParameterTypes().length; t++) {
            printstream.println("        "
                    + formatClassName(m.getParameterTypes()[t]) + "  arg" + t
                    + ";");
        }

        printstream.println("");
        printstream.println("");

        // assignments
        for (int t = 0; t < m.getParameterTypes().length; t++) {
            printstream
                    .println("        try{  //attempt to get value and perform conversion");
            printstream.print("            arg" + t + " = ");

            if (isNumber(m.getParameterTypes()[t])) {
                if ((m.getParameterTypes()[t] == int.class)) {
                    printstream.println("((Number) getExpression(" + t
                            + ").evaluate(feature)).intValue();");
                } else if ((m.getParameterTypes()[t] == double.class)) {
                    printstream.println("((Number) getExpression(" + t
                            + ").evaluate(feature)).doubleValue();");
                } else if ((m.getParameterTypes()[t] == long.class)) {
                    printstream.println("((Number) getExpression(" + t
                            + ").evaluate(feature)).longValue();");
                } else if ((m.getParameterTypes()[t] == float.class)) {
                    printstream.println("((Number) getExpression(" + t
                            + ").evaluate(feature)).floatValue();");
                } else {
                    throw new IllegalArgumentException(
                            "dont know how to handle this - "
                                    + m.getParameterTypes()[t]);
                }
            } else if ((m.getParameterTypes()[t] == boolean.class)) {
                printstream.println("((Boolean) getExpression(" + t
                        + ").evaluate(feature)).booleanValue();");
            } else if ((m.getParameterTypes()[t] == String.class)) {
                printstream
                        .println("(getExpression("
                                + t
                                + ").evaluate(feature)).toString(); // extra protection for strings");
            } else // class
            {
                printstream.println("("
                        + formatClassName(m.getParameterTypes()[t])
                        + ") getExpression(" + t + ").evaluate(feature);");
            }

            printstream.println("      }");
            printstream.println("      catch (Exception e){");
            printstream.println("       // probably a type error");
            printstream
                    .println("            throw new IllegalArgumentException(\"Filter Function problem for function "
                            + m.getName()
                            + " argument #"
                            + t
                            + " - expected type "
                            + formatClassName(m.getParameterTypes()[t])
                            + "\");");
            printstream.println("      }");
            printstream.println("");
        }

        // perform computation
        if (isNumber(m.getReturnType())) {
            if (m.getReturnType() == int.class) {
                printstream.print("      return new Integer(Math."
                        + m.getName() + "(");
            }

            if (m.getReturnType() == double.class) {
                printstream.print("      return new Double(Math." + m.getName()
                        + "(");
            }

            if (m.getReturnType() == long.class) {
                printstream.print("      return new Long(Math." + m.getName()
                        + "(");
            }

            if (m.getReturnType() == float.class) {
                printstream.print("      return new Float(Math." + m.getName()
                        + "(");
            }
        } else if (m.getReturnType() == boolean.class) {
            printstream.print("      return new Boolean(Math." + m.getName()
                    + "(");
        } else // class
        {
            printstream.print("      return (Math." + m.getName() + "(");
        }

        for (int t = 0; t < m.getParameterTypes().length; t++) {
            if (t != 0) {
                printstream.print(",");
            }

            printstream.print("arg" + (t));
        }

        printstream.println(" ));");

        printstream.println("}");
    }

    public void writeServiceInfo(Method m, PrintStream printstream, String name) {
        printstream.println("org.geotools.filter.function.math.FilterFunction_"
                + name);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param class1
     * 
     * @return
     */
    private boolean isNumber(Class class1) {
        if ((class1 == int.class) || (class1 == double.class)
                || (class1 == float.class) || (class1 == long.class)) {
            return true;
        }

        return false;
    }

    public String formatClassName(Class c) {
        String fullName = c.getName();
        int indx = fullName.lastIndexOf('.');

        if (indx == -1) {
            return fullName;
        } else {
            return fullName.substring(indx + 1);
        }
    }
}
