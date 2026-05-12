import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

public class Main {
    private static final class FastScanner {
        private final BufferedInputStream in = new BufferedInputStream(System.in);
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0;
        private int len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) {
                    return -1;
                }
            }
            return buffer[ptr++];
        }

        String next() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c;
            do {
                c = read();
            } while (c != -1 && c <= ' ');

            if (c == -1) {
                return null;
            }

            while (c > ' ') {
                sb.append((char) c);
                c = read();
            }
            return sb.toString();
        }

        int nextInt() throws IOException {
            String s = next();
            if (s == null) {
                throw new IOException("Unexpected end of input");
            }
            return Integer.parseInt(s);
        }

        double nextDouble() throws IOException {
            String s = next();
            if (s == null) {
                throw new IOException("Unexpected end of input");
            }
            return Double.parseDouble(s);
        }
    }

    private static final class Point implements Comparable<Point> {
        final double x;
        final double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point other) {
            int cx = Double.compare(this.x, other.x);
            if (cx != 0) {
                return cx;
            }
            return Double.compare(this.y, other.y);
        }
    }

    private static double cross(Point o, Point a, Point b) {
        return (a.x - o.x) * (b.y - o.y) - (a.y - o.y) * (b.x - o.x);
    }

    private static double distance(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.hypot(dx, dy);
    }

    private static Point[] convexHull(Point[] points) {
        if (points.length <= 1) {
            return points;
        }

        Arrays.sort(points);

        // Deduplicate equal points to keep the hull logic stable.
        Point[] unique = new Point[points.length];
        int m = 0;
        for (Point p : points) {
            if (m == 0 || Double.compare(p.x, unique[m - 1].x) != 0 || Double.compare(p.y, unique[m - 1].y) != 0) {
                unique[m++] = p;
            }
        }

        if (m <= 1) {
            return Arrays.copyOf(unique, m);
        }

        Point[] hull = new Point[2 * m];
        int k = 0;

        // Lower hull.
        for (int i = 0; i < m; i++) {
            while (k >= 2 && cross(hull[k - 2], hull[k - 1], unique[i]) <= 0.0) {
                k--;
            }
            hull[k++] = unique[i];
        }

        // Upper hull.
        int lowerSizePlusOne = k + 1;
        for (int i = m - 2; i >= 0; i--) {
            while (k >= lowerSizePlusOne && cross(hull[k - 2], hull[k - 1], unique[i]) <= 0.0) {
                k--;
            }
            hull[k++] = unique[i];
        }

        if (k > 1) {
            k--; // Remove duplicate start point.
        }

        return Arrays.copyOf(hull, k);
    }

    private static double perimeter(Point[] hull) {
        if (hull.length <= 1) {
            return 0.0;
        }
        double sum = 0.0;
        for (int i = 0; i < hull.length; i++) {
            Point a = hull[i];
            Point b = hull[(i + 1) % hull.length];
            sum += distance(a, b);
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        String first = fs.next();
        if (first == null) {
            return;
        }

        int n = Integer.parseInt(first);
        Point[] points = new Point[4 * n];
        int idx = 0;

        for (int i = 0; i < n; i++) {
            double x1 = fs.nextDouble();
            double y1 = fs.nextDouble();
            double x2 = fs.nextDouble();
            double y2 = fs.nextDouble();

            double minX = Math.min(x1, x2);
            double maxX = Math.max(x1, x2);
            double minY = Math.min(y1, y2);
            double maxY = Math.max(y1, y2);

            points[idx++] = new Point(minX, minY);
            points[idx++] = new Point(minX, maxY);
            points[idx++] = new Point(maxX, minY);
            points[idx++] = new Point(maxX, maxY);
        }

        Point[] hull = convexHull(points);
        double answer = perimeter(hull);

        System.out.printf(Locale.US, "%.10f%n", answer);
    }
}
