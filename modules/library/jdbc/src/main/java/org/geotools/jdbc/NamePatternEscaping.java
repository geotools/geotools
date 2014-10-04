package org.geotools.jdbc;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

class NamePatternEscaping {
    private final String escape;
    private final String quotedReplacement;
    private final Pattern replacementPattern;

    public NamePatternEscaping(String escape) {
        this.escape = escape == null ? "" : escape;
        String quotedEscape = Pattern.quote(this.escape);
        quotedReplacement = Matcher.quoteReplacement(this.escape);
        replacementPattern = Pattern.compile("(" + quotedEscape + "|[_%])");
    }

    public String escape(String name) {
        if (needsEscaping(name)) {
            return replacementPattern.matcher(name).replaceAll(quotedReplacement + "$1");
        } else {
            return name;
        }
    }

    private boolean needsEscaping(String name) {
        if (name == null) {
            return false;
        }
        if (escape.isEmpty()) {
            return false;
        }
        return name.indexOf('_') != -1 || name.indexOf('%') != -1 || name.contains(escape);
    }
}
