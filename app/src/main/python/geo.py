def inverse_geo(x1, y1, x2, y2):
    from sympy import Point2D
    from pygeoguz.simplegeo import ogz
    p1 = Point2D(float(x1), float(y1))
    p2 = Point2D(float(x2), float(y2))
    line = ogz(point_a=p1, point_b=p2)
    length = line.length
    direction = line.direction
    return [length, direction]
