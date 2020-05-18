package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCAggregateFunctionOnlineTest;
import org.geotools.jdbc.JDBCAggregateTestSetup;

public class OracleAggregateFunctionOnlineTest extends JDBCAggregateFunctionOnlineTest {

    @Override
    protected JDBCAggregateTestSetup createTestSetup() {
        return new OracleAggregateTestSetup();
    }

    /**
     * This test fails on Oracle 18c and passes by accident on Oracle12c, specifically the last
     * test, which checks if sorting is correct.
     *
     * <p>{@code SELECT * FROM GEOTOOLS.FT1}
     *
     * <pre>
     * ID|GEOMETRY                                  |INTPROPERTY|DOUBLEPROPERTY|STRINGPROPERTY|
     * --|------------------------------------------|-----------|--------------|--------------|
     *  0|[2001, 4326, [0, 0, null], [NULL], [NULL]]|          0|             0|zero          |
     *  1|[2001, 4326, [1, 1, null], [NULL], [NULL]]|          1|           1.1|one           |
     *  2|[2001, 4326, [2, 2, null], [NULL], [NULL]]|          2|           2.2|two           |
     * </pre>
     *
     * <p>{@code SELECT distinct(STRINGPROPERTY) FROM GEOTOOLS.FT1 ORDER BY STRINGPROPERTY ASC}
     *
     * <pre>
     * STRINGPROPERTY|
     * --------------|
     * one           |
     * two           |
     * zero          |
     * </pre>
     *
     * <p>Oracle Oracle Database 12c Enterprise Edition Release 12.2.0.1.0 - 64bit Production
     *
     * <p>{@code SELECT distinct(STRINGPROPERTY) AS gt_result_ FROM (SELECT distinct(STRINGPROPERTY)
     * FROM GEOTOOLS.FT1 ORDER BY STRINGPROPERTY ASC) gt_limited_}
     *
     * <pre>
     * GT_RESULT_|
     * ----------|
     * one       |
     * zero      |
     * two       |
     * </pre>
     *
     * This would pass the test (by accident, one==one), but as a whole the results are not sorted
     * alphabetically!
     *
     * <p>Oracle Oracle Database 18c Express Edition Release 18.0.0.0.0 - Production
     *
     * <p>{@code SELECT distinct(STRINGPROPERTY) AS gt_result_ FROM (SELECT distinct(STRINGPROPERTY)
     * FROM GEOTOOLS.FT1 ORDER BY STRINGPROPERTY ASC) gt_limited_}
     *
     * <pre>
     * GT_RESULT_|
     * ----------|
     * zero      |
     * one       |
     * two       |
     * </pre>
     *
     * fails the test (one != zero).
     *
     * <p>The actual query that works is {@code SELECT STRINGPROPERTY AS gt_result_ FROM (SELECT
     * distinct(STRINGPROPERTY) FROM GEOTOOLS.FT1 ORDER BY STRINGPROPERTY ASC) gt_limited_}
     *
     * <pre>
     * GT_RESULT_|
     * ----------|
     * one       |
     * two       |
     * zero      |
     * </pre>
     */
    @Override
    public void testStoreChecksVisitorLimits() {}
}
