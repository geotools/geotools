# IP Review

STATUS: CLEAN

## Initial Review

Review conducted by:

 - Gabriel Roldan, November 26, 2006

 - Adrian Custer, June 6, 2008

Packages reviewed:

```
org.geotools.filter.cql
org.geotools.filter.function (including test)
```

This module was created by Axios Engineering as an extension of the
existing org.geotools.filter.ExpressionBuilder class, which used to
parse a SQL like constraint into a geotools Filter object.
ExpressionBuilder was already under LGPL. Axios made this extension in
order to fully support the parsing of the OGC Common Query Language
defined in the OGC CSW spec, version 2.0.1, as a small part of a
contracted job by Gipuzkoako Foru Aldundia - Diputación Foral de
Bizkaia, Departamento deOrdenación Territorial
(http://b5m.gipuzkoa.net).

## Code refactor to allow parsers

Review conducted by:

 - Mauricio Pazos, Februray 04, 2008

Packaged reviewed:

```
org.geotools.filter.function
```

Was done a code refactoring to allow the easy composition of a new
parsers generated with javacc with that class responsible to build
filters and its components (Expressions, Literals, etc).

This first refactoring solution was strongly limited by javacc maven
plugin. This solution maintains all the generated parsers in the same
package, a better solution should allow to separate different parsers in
different packages (better cohesion). The javacc plugin produces
spurious parser classes when they are in separated packages which
produces compile errors.

Now the module has the following distribution:

```
  src/main/java org.geotools.filter.text.cql2
  org.geotools.filter.text.txt (work in progress)
  src/main/jjtree CQLGrammar.jjt TXTGrammar.jjt

  modified-src
  org.geotools.filter.text.generated.parsers (simple node modified)

  test/java (unit tests)
```