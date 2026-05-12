# Optimal Patrol Path Around Machines (Java)

## 1) Mathematical Explanation

### (a) Rectangle problem -> point set problem
Each axis-aligned rectangle with opposite corners `(x1, y1)` and `(x2, y2)` contributes exactly four corner points:

- `(min(x1, x2), min(y1, y2))`
- `(min(x1, x2), max(y1, y2))`
- `(max(x1, x2), min(y1, y2))`
- `(max(x1, x2), max(y1, y2))`

So with `N` rectangles, we build a point set of size `4N`.

### (b) Why convex hull is optimal
Any valid patrol loop must enclose every rectangle, so it must enclose every rectangle corner.
For any point set, the smallest convex region containing all points is the convex hull.
Its boundary is the shortest possible enclosing convex polygonal boundary for those points.
Given the quiz assumption that the optimal patrol path equals the boundary of the convex hull of all corners, computing that hull gives the optimal path.

### (c) Algorithm (Andrew's monotone chain)
1. Create all `4N` corner points.
2. Sort points lexicographically by `(x, y)`.
3. Build lower hull using orientation checks.
4. Build upper hull using orientation checks.
5. Concatenate lower + upper (without duplicate endpoint).
6. Sum Euclidean distances of consecutive hull vertices cyclically.

Why sorting is needed:
Monotone chain relies on left-to-right ordering to construct hull edges in one pass for lower hull and one reverse pass for upper hull.

How orientation/cross-product is used:
For three points `O, A, B`, the orientation value is:

`cross(O, A, B) = (A.x - O.x) * (B.y - O.y) - (A.y - O.y) * (B.x - O.x)`

- `cross > 0`: left turn
- `cross < 0`: right turn
- `cross = 0`: collinear

During hull construction, while the last turn is not a left turn, remove the middle point.

Overall time complexity:
- Point generation: `O(N)`
- Sorting `4N` points: `O(N log N)`
- Hull construction: `O(N)`
- Perimeter computation: `O(N)`

Total: `O(N log N)`.

## 2) Required formulas

- Cross product/orientation:
  `cross(O, A, B) = (A.x - O.x)(B.y - O.y) - (A.y - O.y)(B.x - O.x)`
- Euclidean distance:
  `dist(P, Q) = sqrt((Px - Qx)^2 + (Py - Qy)^2)`
- Hull perimeter:
  If hull vertices are `H0, H1, ..., Hk-1` in order,
  `Perimeter = sum(dist(Hi, H(i+1) mod k))` for `i = 0..k-1`.

## 3) Code solution

`Main.java`:
- Reads input.
- Generates rectangle corners.
- Computes convex hull with Andrew's monotone chain.
- Computes and prints perimeter with high precision (`%.10f`).

## 4) Compile and run

```bash
javac Main.java
java Main < input.txt
```
