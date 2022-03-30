# GeoTools Tutorials

GeoTools standalone tutorials included in the GeoTools User Guide as examples of how to
use the library.

* [Quickstart](https://docs.geotools.org/latest/userguide/tutorial/quickstart/index.html)

# Source code

The implementations provided here are spliced into the tutorial documentation using sphinx
by making use of "markers" placed into the files such as shown below.

```java
public void sample(){
     // sample start
     featureCollection.accepts( new FeatureVisitor(){
         public void visit( Feature feature ){
             System.out.println( feature.getID() );
         }
     }, null );
     // sample end
}
```
        
With this in mind please consider the source code in the context of the documentation.
Such code may not always show best practice (if part of an example leading up to best practice).

## CC0 License

These tutorials are provided with [CC0 licence](LICENSE.md):

> GeoTools Tutorials
> 
> 2022 Open Source Geospatial Foundation, and others
>
> To the extent possible under law, the author(s) have dedicated all copyright
> and related and neighboring rights to this software to the public domain worldwide.
> This software is distributed without any warranty.
>
> You should have received a copy of the CC0 Public Domain Dedication along with this
> software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.

Use of CC0 license is in keeping with our original public domain policy (that was primarily intended
for the united states). The selection of [CC0 licence](LICENSE.md) allows example code to be freely
cut and pasted into your own applications.

## Building

Tutorials are standalone projects, each with their own ``pom.xml``.

To build:
```
mvn clean install
```

To run:
```
mvn exec:java
```

Select examples have additional quality assurance checks:
```
mvn verify -Dqa
```