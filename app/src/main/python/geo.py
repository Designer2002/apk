def inverse_geo(x1, y1, x2, y2):
    p1 = float(x1), float(y1)
    p2 = float(x2), float(y2)
    line = ogz(point_a=p1, point_b=p2)
    length = line[0]
    direction = line[1]
    return [length, direction]

def ogz(point_a,point_b):
    import math
    """
    Обратная геодезическая задача

    :param point_a: Начальная точка
    :param point_b: Конечная точка

    :return: Направление
    """
    delta_x = point_b[0] - point_a[0]
    delta_y = point_b[1] - point_a[1]
    length = math.sqrt(delta_x ** 2 + delta_y ** 2)

    direction = math.degrees(math.atan2(delta_y, delta_x))
    true_direction = true_angle(direction, 360)
    return (round(length, 4), true_direction)

def true_angle(angle: float, max_value: int) -> float:
    """
    Возвращает верное значение угла

    :param angle: Угол
    :param max_value: Максимальное допустимое значение (180, 360)

    :return: Верное значение угла
    """
    if angle > max_value:
        return angle - max_value
    elif angle < 0:
        return angle + max_value
    else:
        return angle
