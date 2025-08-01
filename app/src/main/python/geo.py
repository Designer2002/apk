def inverse_geo(x1, y1, x2, y2):
    from sympy import Point2D
    from pygeoguz.simplegeo import ogz
    p1 = Point2D(x=200, y=400)
    p2 = Point2D(x=286.34, y=349.54)
    line = ogz(point_a=p1, point_b=p2)
    length = line.length
    direction = line.direction
    return [length, direction]
