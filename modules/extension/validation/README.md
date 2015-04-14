# Validation Extension

## Status

The validation module is stable.

## IP Check

[REVIEW.md](REVIEW.md) complete

Future Plans
------------

The validation module depends on an the definition of a "typeRef"
basically encoding a "datastoreId:::typeName" into a string for lookup
into a Registry. The registery interface is provided by GeoServer (and
also by uDig) as a way of letting integrity tests lookup a feature
source to verification.

The is a bad idea for two reasons:

-   it is not strongly typed
-   it causes confusion with the namespace:typeName used during GML
    output

There are two solutions:

-   use the TypeName (extends Name) from GeoAPI, as provided for in 2.3
    Feature Model development
-   use the Catalog System IGeoResource (an actual resource handle)

Both offer strong typing and avoid the possibility of confusion.