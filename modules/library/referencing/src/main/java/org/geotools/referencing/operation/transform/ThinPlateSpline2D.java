package org.geotools.referencing.operation.transform;

public class ThinPlateSpline2D {
    private final double[] x;
    private final double[] y;
    private final double[] weights;
    private final int n;

    public ThinPlateSpline2D(double[] xSource, double[] ySource, double[] values) {

        for (int i = 0; i < xSource.length; i++) {
            for (int j = i + 1; j < xSource.length; j++) {
                if (xSource[i] == xSource[j] && ySource[i] == ySource[j]) {
                    throw new IllegalArgumentException("Duplicate control points are not allowed.");
                }
            }
        }

        this.n = xSource.length;
        this.x = xSource;
        this.y = ySource;
        this.weights = solveSystem(xSource, ySource, values);
    }

    private double U(double r2) {
        if (r2 == 0.0) return 0.0;
        return r2 * Math.log(Math.sqrt(r2));
    }

    private double r2(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    private double[] solveSystem(double[] x, double[] y, double[] v) {
        int N = x.length;
        int M = N + 3;

        double[][] A = new double[M][M];
        double[] b = new double[M];

        // Fill K (radial basis kernel)
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j] = U(r2(x[i], y[i], x[j], y[j]));
            }
        }

        // Fill P (affine part)
        for (int i = 0; i < N; i++) {
            A[i][N] = 1.0;
            A[i][N + 1] = x[i];
            A[i][N + 2] = y[i];

            A[N][i] = 1.0;
            A[N + 1][i] = x[i];
            A[N + 2][i] = y[i];
        }

        // Fill b (target values)
        System.arraycopy(v, 0, b, 0, N);

        // Solve A * w = b
        return gaussianElimination(A, b);
    }

    private double[] gaussianElimination(double[][] A, double[] b) {
        int N = A.length;
        double[][] aug = new double[N][N + 1];

        // Build augmented matrix
        for (int i = 0; i < N; i++) {
            System.arraycopy(A[i], 0, aug[i], 0, N);
            aug[i][N] = b[i];
        }

        // Forward elimination
        for (int i = 0; i < N; i++) {
            // Pivot
            int maxRow = i;
            for (int k = i + 1; k < N; k++) {
                if (Math.abs(aug[k][i]) > Math.abs(aug[maxRow][i])) {
                    maxRow = k;
                }
            }
            double[] tmp = aug[i];
            aug[i] = aug[maxRow];
            aug[maxRow] = tmp;

            // Eliminate
            for (int k = i + 1; k < N; k++) {
                double f = aug[k][i] / aug[i][i];
                for (int j = i; j <= N; j++) {
                    aug[k][j] -= f * aug[i][j];
                }
            }
        }

        // Backward substitution
        double[] x = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            x[i] = aug[i][N] / aug[i][i];
            for (int k = i - 1; k >= 0; k--) {
                aug[k][N] -= aug[k][i] * x[i];
            }
        }

        return x;
    }

    public double interpolate(double xVal, double yVal) {
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            sum += weights[i] * U(r2(xVal, yVal, x[i], y[i]));
        }
        // Affine terms
        sum += weights[n]; // constant
        sum += weights[n + 1] * xVal;
        sum += weights[n + 2] * yVal;
        return sum;
    }
}
