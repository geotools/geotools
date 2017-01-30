/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.css;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.styling.css.Value.Literal;
import org.geotools.styling.css.selector.Data;
import org.geotools.styling.css.selector.Id;
import org.geotools.styling.css.selector.PseudoClass;
import org.geotools.styling.css.selector.ScaleRange;
import org.geotools.styling.css.selector.Selector;
import org.geotools.styling.css.selector.TypeName;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.parboiled.Action;
import org.parboiled.BaseParser;
import org.parboiled.Context;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.ValueStack;

/**
 * Parser for the cartographic CSS. In order to parse a CSS either get a parser using the
 * {@link #getInstance()} method, or directly call {@link #parse(String)}
 * 
 * @author Andrea Aime - GeoSolutions
 */
@BuildParseTree
public class CssParser extends BaseParser<Object> {

    static CssParser INSTANCE;

    static final Object MARKER = new Object();
    
    /**
     * Quick key/value storage
     */
    static final class KeyValue {
        
        String key;
        Value value;

        public KeyValue(String key, Value value) {
            this.key = key;
            this.value = value;
        }
        
    }
    
    static final class Prefix {
        String prefix;

        public Prefix(java.lang.String prefix) {
            super();
            this.prefix = prefix;
        }
        
    }

    /**
     * Allows Parboiled to do its magic, while disallowing normal users from instantiating this
     * class
     */
    protected CssParser() {

    }

    /**
     * Returns the single instance of the CSS parser. The CSSParser should not be instantiated
     * directly, Parboiled needs to do it instead.
     * 
     * @return
     */
    public static CssParser getInstance() {
        // we need to lazily create it, otherwise Parboiled won't be able to instrument the class
        if (INSTANCE == null) {
            INSTANCE = Parboiled.createParser(CssParser.class);
        }

        return INSTANCE;
    }

    /**
     * Turns the CSS provided into a {@link Stylesheet} object, will throw a
     * {@link CSSParseException} in case of syntax errors
     * 
     * @return
     * @throws IOException
     */
    public static Stylesheet parse(String css) throws CSSParseException {
        CssParser parser = getInstance();
        ParseRunner<Stylesheet> runner = new ReportingParseRunner<Stylesheet>(parser.StyleSheet());
        ParsingResult<Stylesheet> result = runner.run(css);
        if (result.hasErrors()) {
            throw new CSSParseException(result.parseErrors);
        }
        Stylesheet ss = result.parseTreeRoot.getValue();
        return ss;
    }

    Rule StyleSheet() {
        return Sequence(ZeroOrMore(Directive(), OptionalWhiteSpace()), OneOrMore(CssRule()),
                WhiteSpaceOrIgnoredComment(), EOI, push(new Stylesheet(popAll(CssRule.class),
                        popAll((Directive.class)))));
    }

    Rule Directive() {
        return Sequence("@", Identifier(), push(match()), WhiteSpace(), String(), Ch(';'), swap(),
                push(new Directive((String) pop(), ((Literal) pop()).toLiteral())));
    }

    Rule CssRule() {
        return Sequence(WhiteSpaceOrComment(), Selector(), OptionalWhiteSpace(),//
                '{', OptionalWhiteSpace(), //
                RuleContents(), WhiteSpaceOrIgnoredComment(), '}', new Action() {

                    @Override
                    public boolean run(Context ctx) {
                        List contents = (List) pop();
                        Selector selector = (Selector) pop();
                        String comment = null;
                        if (!ctx.getValueStack().isEmpty() && peek() instanceof String) {
                            comment = (String) pop();
                            comment = comment.trim();
                            // get rid of the extra comments between rules
                            while (!ctx.getValueStack().isEmpty() && peek() instanceof String) {
                                pop();
                            }
                        }
                        
                        final Stream stream = contents.stream();
                        Map<Boolean, List> splitContents = (Map<Boolean, List>) stream.collect(Collectors.partitioningBy(x -> x instanceof CssRule));
                        List<Property> properties = splitContents.get(Boolean.FALSE);
                        List<CssRule> subRules = splitContents.get(Boolean.TRUE);
                        
                        final CssRule rule = new CssRule(selector, properties, comment);
                        rule.nestedRules = subRules;
                        push(rule);

                        return true;
                    }

                });
    }
    
    Rule RuleContents() {
        return Sequence(
                FirstOf(CssRule(), Property()),
                ZeroOrMore(Sequence(WhitespaceOrIgnoredComment(), ';',
                        OptionalWhiteSpace(), FirstOf(CssRule(), Property()))), Optional(';'),
                push(popAll(Property.class, CssRule.class)));
    }

    Rule Selector() {
        return FirstOf(OrSelector(), AndSelector(), BasicSelector());
    }

    Rule BasicSelector() {
        return FirstOf(CatchAllSelector(), ECQLSelector(), MinScaleSelector(), MaxScaleSelector(),
                IdSelector(), PseudoClassSelector(), NumberedPseudoClassSelector(),
                TypenameSelector());
    }

    Rule AndSelector() {
        return Sequence(BasicSelector(), OptionalWhiteSpace(),
                FirstOf(AndSelector(), BasicSelector()), //
                swap() && push(Selector.and((Selector) pop(), (Selector) pop(), null)));
    }

    Rule OrSelector() {
        return Sequence(FirstOf(AndSelector(), BasicSelector()), OptionalWhiteSpace(), ',',
                OptionalWhiteSpace(), Selector(), //
                swap() && push(Selector.or((Selector) pop(), (Selector) pop(), null)));
    }

    @SuppressSubnodes
    Rule PseudoClassSelector() {
        return Sequence(':', ClassName(), push(PseudoClass.newPseudoClass((String) pop())));
    }

    @SuppressSubnodes
    Rule NumberedPseudoClassSelector() {
        return Sequence(
                ":nth-",
                ClassName(),
                '(',
                Number(),
                push(match()),
                ')',
                swap()
                        && push(PseudoClass.newPseudoClass((String) pop(),
                                Integer.valueOf((String) pop()))));
    }

    Rule ClassName() {
        return Sequence(FirstOf("mark", "stroke", "fill", "symbol", "shield"), push(match()));
    }

    @SuppressSubnodes
    Rule TypenameSelector() {
        return Sequence(QualifiedIdentifier(), push(new TypeName(
                match())));
    }
    
    Rule QualifiedIdentifier() {
        return Sequence(Identifier(), Optional(':', Identifier()));
    }

    @SuppressSubnodes
    Rule IdSelector() {
        return Sequence(
                '#',
                Sequence(Identifier(), Optional(':', Identifier()),
                        Optional('.', Sequence(TestNot(AnyOf("\"'[]")), ANY))),
                push(new Id(match())));
    }

    Rule CatchAllSelector() {
        return Sequence('*', push(Selector.ACCEPT));
    }

    Rule MaxScaleSelector() {
        return Sequence("[", OptionalWhiteSpace(), "@scale", OptionalWhiteSpace(),
                FirstOf("<=", "<"), OptionalWhiteSpace(), Number(), push(new ScaleRange(0, true,
                        Double.valueOf(match()), false)), //
                OptionalWhiteSpace(), "]");
    }

    Rule MinScaleSelector() {
        return Sequence(
                "[",
                OptionalWhiteSpace(),
                "@scale",
                OptionalWhiteSpace(),
                FirstOf(">=", ">"),
                OptionalWhiteSpace(),
                Number(),
                push(new ScaleRange(Double.valueOf(match()), true, Double.POSITIVE_INFINITY, true)), //
                OptionalWhiteSpace(), "]");
    }

    Rule WhitespaceOrIgnoredComment() {
        return ZeroOrMore(FirstOf(WhiteSpace(), IgnoredComment()));
    }

    Rule Property() {
        return Sequence(WhiteSpaceOrIgnoredComment(), Identifier(),
                push(match()),
                OptionalWhiteSpace(),
                Colon(),
                OptionalWhiteSpace(), //
                Sequence(Value(), OptionalWhiteSpace(),
                        ZeroOrMore(',', OptionalWhiteSpace(), Value())), //
                push(popAll(Value.class)) && swap()
                        && push(new Property(pop(String.class), pop(List.class))));
    }
    
    Rule KeyValue() {
        return Sequence(Identifier(), push(match()),
                OptionalWhiteSpace(),
                Colon(),
                OptionalWhiteSpace(), 
                Value(),
                swap()
                && push(new KeyValue(pop(String.class), pop(Value.class))));
    }

    @SuppressNode
    Rule Colon() {
        return Ch(':');
    }

    Rule Value() {
        return FirstOf(MultiValue(), SimpleValue());
    }

    Rule SimpleValue() {
        return FirstOf(URLFunction(), TransformFunction(), Function(), Color(), NamedColor(), Measure(),
                ValueIdentifier(), MixedExpression());
    }

    Rule MixedExpression() {
        return Sequence(push(MARKER), OneOrMore(FirstOf(ECQLExpression(), String())), new Action() {

            @Override
            public boolean run(Context ctx) {
                Object value = pop();
                List<Expression> expressions = new ArrayList<>();
                Object firstValue = null;
                while (value != MARKER) {
                    firstValue = value;
                    if (value instanceof Value) {
                        expressions.add(((Value) value).toExpression());
                    }
                    value = pop();
                }

                if (expressions.size() == 0) {
                    return false;
                } else if (expressions.size() == 1) {
                    push(firstValue);
                } else {
                    Collections.reverse(expressions);
                    org.opengis.filter.expression.Function function = Data.FF.function(
                            "Concatenate", expressions.toArray(new Expression[expressions.size()]));
                    push(new Value.Expression(function));
                }
                return true;
            }

        });
    }

    Rule MultiValue() {
        return Sequence(push(MARKER), SimpleValue(), OneOrMore(WhiteSpace(), SimpleValue()),
                push(new Value.MultiValue(popAll(Value.class))));
    }

    Rule Function() {
        return Sequence(Identifier(), push(match()), '(', Value(),
                ZeroOrMore(OptionalWhiteSpace(), ',', OptionalWhiteSpace(), Value()), ')',
                push(buildFunction(popAll(Value.class), (String) pop())));
    }
    
    Value.Function buildFunction(List<Value> values, String name) {
        return new Value.Function(name, values);
    }
    
    Rule TransformFunction() {
        return Sequence(QualifiedIdentifier(), push(new Prefix(match())), '(', Optional(OptionalWhiteSpace(), KeyValue()),
                ZeroOrMore(OptionalWhiteSpace(), ',', OptionalWhiteSpace(), KeyValue()), ')',
                push(buildTransformFunction(popAll(KeyValue.class), pop(Prefix.class))));
    }
    
    Value.TransformFunction buildTransformFunction(List<KeyValue> values, Prefix name) {
        Map<String, Value> parameters = new LinkedHashMap<>();
        for (KeyValue keyValue : values) {
            parameters.put(keyValue.key, keyValue.value);
        }
        return new Value.TransformFunction(name.prefix, parameters);
    }
    
    Rule URLFunction() {
        return Sequence("url", OptionalWhiteSpace(), "(", OptionalWhiteSpace(), URL(),
                OptionalWhiteSpace(), ")", push(new Value.Function("url", (Value) pop())));
    }

    /**
     * Very relaxed URL matcher, as we need to match also relative urls
     * 
     * @return
     */
    Rule URL() {
        return FirstOf(QuotedURL(), SimpleURL());
    }

    Rule SimpleURL() {
        return Sequence(OneOrMore(FirstOf(Alphanumeric(), AnyOf("-._]:/?#[]@|$&'*+,;="))),
                push(new Value.Literal(match())));
    }

    Rule QuotedURL() {
        // same as simple url, but with ' surrounding it, and not within the url itlsef
        return Sequence(
                "'",
                Sequence(OneOrMore(FirstOf(Alphanumeric(), AnyOf("-._]:/?#[]@|$&*+,;="))),
                        push(new Value.Literal(match()))), "'");
    }

    Rule ValueIdentifier() {
        return Sequence(Identifier(), push(new Value.Literal(match())));
    }
    
    Rule String() {
        return FirstOf(
                Sequence('\'', ZeroOrMore(Sequence(TestNot(AnyOf("'\\")), ANY)),
                        push(new Value.Literal(match())), '\''),
                Sequence('"', ZeroOrMore(Sequence(TestNot(AnyOf("\"\\")), ANY)),
                        push(new Value.Literal(match())), '"'));
    }

    Rule Measure() {
        return Sequence(
                Sequence(
                        Number(),
                        Optional(FirstOf(String("px"), String("m"), String("ft"), String("%"),
                                String("deg")))), push(new Value.Literal(match())));
    }

    Rule ECQLExpression() {
        return ECQL(new Action() {
            @Override
            public boolean run(Context ctx) {
                String expression = match();
                try {
                    org.opengis.filter.expression.Expression e = ECQL.toExpression(expression);
                    ctx.getValueStack().push(new Value.Expression(e));
                    return true;
                } catch (CQLException e) {
                    return false;
                }
            }
        });
    }

    Rule ECQLSelector() {
        return ECQL(new Action() {
            @Override
            public boolean run(Context ctx) {
                String expression = match();
                try {
                    Filter f = ECQL.toFilter(expression);
                    ctx.getValueStack().push(new Data(f));
                    return true;
                } catch (CQLException e) {
                    return false;
                }
            }
        });
    }

    Rule ECQL(Action parserChecker) {
        return Sequence(
                '[',
                OneOrMore(FirstOf(SingleQuotedString(), DoubleQuotedString(),
                        Sequence(TestNot(AnyOf("\"'[]")), ANY))), //
                parserChecker, ']');
    }

    Rule DoubleQuotedString() {
        return Sequence('"', ZeroOrMore(Sequence(TestNot(AnyOf("\r\n\"\\")), ANY)), '"');
    }

    Rule SingleQuotedString() {
        return Sequence('\'', ZeroOrMore(Sequence(TestNot(AnyOf("\r\n'\\")), ANY)), '\'');
    }

    Rule IntegralNumber() {
        return OneOrMore(Digit());
    }

    Rule Number() {
        return Sequence(Optional(AnyOf("-+")), OneOrMore(Digit()),
                Optional('.', ZeroOrMore(Digit())));
    }

    @SuppressSubnodes
    Rule Color() {
        return Sequence(
                Sequence(
                        '#',
                        FirstOf(Sequence(Hex(), Hex(), Hex(), Hex(), Hex(), Hex()),
                                Sequence(Hex(), Hex(), Hex()))), push(new Value.Literal(
                        toHexColor(match()))));
    }

    String toHexColor(String hex) {
        if (hex.length() == 7) {
            return hex;
        } else {
            char r = hex.charAt(1);
            char g = hex.charAt(2);
            char b = hex.charAt(3);
            return "#" + r + r + g + g + b + b;
        }
    }

    @SuppressSubnodes
    Rule NamedColor() {
        String[] colorNames = new String[Value.COLORS_TO_HEX.size()];
        int i = 0;
        for (String name : Value.COLORS_TO_HEX.keySet()) {
            colorNames[i++] = name;
        }
        // make sure the longer words come before the shorter ones (yellowgreen before yellow)
        Arrays.sort(colorNames, Collections.reverseOrder());

        Rule[] insensitiveColorNames = new Rule[colorNames.length];
        for (int j = 0; j < colorNames.length; j++) {
            insensitiveColorNames[j] = IgnoreCase(colorNames[j]);
        }

        return Sequence(FirstOf(insensitiveColorNames), new Action() {

            @Override
            public boolean run(Context ctx) {
                String hex = Value.COLORS_TO_HEX.get(match().toLowerCase());
                push(new Value.Literal(hex));
                return true;
            }

        });
    }

    @SuppressNode
    Rule Identifier() {
        return Sequence(Optional('-'), NameStart(), ZeroOrMore(NameCharacter()));
    }
    
//    Rule QualifiedIdentifier() {
//        return Sequence(Optional(Identifier(), push(new Prefix(match())), ':'), Identifier(), 
//                new Action() {
//                    @Override
//                    public boolean run(Context ctx) {
//                        String name = (java.lang.String) pop();
//                        if(peek() instanceof Prefix) {
//                            Prefix prefix = (Prefix) pop();
//                            name = prefix.prefix + ":" + name;
//                        }
//                        
//                        push(name);
//                    }
//                });
//    }


    @SuppressNode
    Rule NameStart() {
        return FirstOf('_', Alpha());
    }

    @SuppressNode
    Rule NameCharacter() {
        return FirstOf(AnyOf("-_"), Alphanumeric());
    }

    @SuppressNode
    Rule Hex() {
        return FirstOf(CharRange('a', 'f'), CharRange('A', 'F'), Digit());
    }

    @SuppressNode
    Rule Digit() {
        return CharRange('0', '9');
    }

    @SuppressNode
    Rule Alphanumeric() {
        return FirstOf(Alpha(), Digit());
    }

    @SuppressNode
    Rule Alpha() {
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'));
    }

    Rule IgnoredComment() {
        return Sequence("/*", ZeroOrMore(TestNot("*/"), ANY), "*/");
    }

    Rule RuleComment() {
        return Sequence("/*", ZeroOrMore(TestNot("*/"), ANY), push(match()), "*/");
    }

    @SuppressNode
    Rule WhiteSpaceOrIgnoredComment() {
        return ZeroOrMore(FirstOf(IgnoredComment(), WhiteSpace()));
    }

    @SuppressNode
    Rule WhiteSpaceOrComment() {
        return ZeroOrMore(FirstOf(RuleComment(), WhiteSpace()));
    }

    @SuppressNode
    Rule OptionalWhiteSpace() {
        return ZeroOrMore(AnyOf(" \r\t\f\n"));
    }

    @SuppressNode
    Rule WhiteSpace() {
        return OneOrMore(AnyOf(" \r\t\f\n"));
    }

    /**
     * We redefine the rule creation for string literals to automatically match trailing whitespace
     * if the string literal ends with a space character, this way we don't have to insert extra
     * whitespace() rules after each character or string literal
     */
    @Override
    protected Rule fromStringLiteral(String string) {
        return string.matches("\\s+$") ? Sequence(String(string.substring(0, string.length() - 1)),
                OptionalWhiteSpace()) : String(string);
    }

    <T> T pop(Class<T> clazz) {
        return (T) pop();
    }

    <T> List<T> popAll(Class... classes) {
        ValueStack<Object> valueStack = getContext().getValueStack();
        List<T> result = new ArrayList<T>();
        while (!valueStack.isEmpty() && isInstance(classes, valueStack.peek())) {
            result.add((T) valueStack.pop());
        }
        if (!valueStack.isEmpty() && valueStack.peek() == MARKER) {
            valueStack.pop();
        }
        Collections.reverse(result);

        return result;
    }

    private boolean isInstance(Class[] classes, Object peek) {
        for (int i = 0; i < classes.length; i++) {
            if(classes[i].isInstance(peek)) {
                return true;
            }
        }
        return false;
    }
}