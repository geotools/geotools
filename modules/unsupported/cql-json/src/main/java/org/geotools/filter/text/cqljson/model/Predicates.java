/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cqljson.model;

public class Predicates {

    private And and;
    private Or or;
    private Predicates not;
    private Eq eq;
    private Lt lt;
    private Gt gt;
    private Lte lte;
    private Gte gte;
    private Between between;
    private Like like;
    private In in;
    private Equals equals;
    private Disjoint disjoint;
    private Touches touches;
    private Within within;
    private Overlaps overlaps;
    private Crosses crosses;
    private Intersects intersects;
    private Contains contains;
    private After after;
    private Before before;
    private Begins begins;
    private Begunby begunby;
    private TContains tcontains;
    private During during;
    private Endedby endedby;
    private Ends ends;
    private Tequals tequals;
    private Meets meets;
    private Metby metby;
    private Toverlaps toverlaps;
    private Overlappedby overlappedby;

    public Predicates getNot() {
        return not;
    }

    public void setNot(Predicates not) {
        this.not = not;
    }

    public Lt getLt() {
        return lt;
    }

    public void setLt(Lt lt) {
        this.lt = lt;
    }

    public Lte getLte() {
        return lte;
    }

    public void setLte(Lte lte) {
        this.lte = lte;
    }

    public Gte getGte() {
        return gte;
    }

    public void setGte(Gte gte) {
        this.gte = gte;
    }

    public Equals getEquals() {
        return equals;
    }

    public void setEquals(Equals equals) {
        this.equals = equals;
    }

    public Disjoint getDisjoint() {
        return disjoint;
    }

    public void setDisjoint(Disjoint disjoint) {
        this.disjoint = disjoint;
    }

    public Touches getTouches() {
        return touches;
    }

    public void setTouches(Touches touches) {
        this.touches = touches;
    }

    public Within getWithin() {
        return within;
    }

    public void setWithin(Within within) {
        this.within = within;
    }

    public Overlaps getOverlaps() {
        return overlaps;
    }

    public void setOverlaps(Overlaps overlaps) {
        this.overlaps = overlaps;
    }

    public Crosses getCrosses() {
        return crosses;
    }

    public void setCrosses(Crosses crosses) {
        this.crosses = crosses;
    }

    public Intersects getIntersects() {
        return intersects;
    }

    public void setIntersects(Intersects intersects) {
        this.intersects = intersects;
    }

    public Contains getContains() {
        return contains;
    }

    public void setContains(Contains contains) {
        this.contains = contains;
    }

    public After getAfter() {
        return after;
    }

    public void setAfter(After after) {
        this.after = after;
    }

    public Before getBefore() {
        return before;
    }

    public void setBefore(Before before) {
        this.before = before;
    }

    public Begins getBegins() {
        return begins;
    }

    public void setBegins(Begins begins) {
        this.begins = begins;
    }

    public Begunby getBegunby() {
        return begunby;
    }

    public void setBegunby(Begunby begunby) {
        this.begunby = begunby;
    }

    public TContains getTcontains() {
        return tcontains;
    }

    public void setTcontains(TContains tcontains) {
        this.tcontains = tcontains;
    }

    public During getDuring() {
        return during;
    }

    public void setDuring(During during) {
        this.during = during;
    }

    public Endedby getEndedby() {
        return endedby;
    }

    public void setEndedby(Endedby endedby) {
        this.endedby = endedby;
    }

    public Ends getEnds() {
        return ends;
    }

    public void setEnds(Ends ends) {
        this.ends = ends;
    }

    public Tequals getTequals() {
        return tequals;
    }

    public void setTequals(Tequals tequals) {
        this.tequals = tequals;
    }

    public Meets getMeets() {
        return meets;
    }

    public void setMeets(Meets meets) {
        this.meets = meets;
    }

    public Metby getMetby() {
        return metby;
    }

    public void setMetby(Metby metby) {
        this.metby = metby;
    }

    public Toverlaps getToverlaps() {
        return toverlaps;
    }

    public void setToverlaps(Toverlaps toverlaps) {
        this.toverlaps = toverlaps;
    }

    public Overlappedby getOverlappedby() {
        return overlappedby;
    }

    public void setOverlappedby(Overlappedby overlappedby) {
        this.overlappedby = overlappedby;
    }

    public Between getBetween() {
        return between;
    }

    public void setBetween(Between between) {
        this.between = between;
    }

    public In getIn() {
        return in;
    }

    public void setIn(In in) {
        this.in = in;
    }

    public Like getLike() {
        return like;
    }

    public void setLike(Like like) {
        this.like = like;
    }

    public Gt getGt() {
        return gt;
    }

    public void setGt(Gt gt) {
        this.gt = gt;
    }

    public Or getOr() {
        return or;
    }

    public void setOr(Or or) {
        this.or = or;
    }

    public And getAnd() {
        return and;
    }

    public void setAnd(And and) {
        this.and = and;
    }

    public Eq getEq() {
        return eq;
    }

    public void setEq(Eq eq) {
        this.eq = eq;
    }
}
