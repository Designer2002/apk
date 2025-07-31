from pyproj import Transformer, CRS

def get_zone_byDict(zone):
    zone_dict={

        "28404": "+proj=tmerc +lat_0=0 +lon_0=21 +k=1 +x_0=4500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28405": "+proj=tmerc +lat_0=0 +lon_0=27 +k=1 +x_0=5500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28406": "+proj=tmerc +lat_0=0 +lon_0=33 +k=1 +x_0=6500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28407": "+proj=tmerc +lat_0=0 +lon_0=39 +k=1 +x_0=7500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28408": "+proj=tmerc +lat_0=0 +lon_0=45 +k=1 +x_0=8500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28409": "+proj=tmerc +lat_0=0 +lon_0=51 +k=1 +x_0=9500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28410": "+proj=tmerc +lat_0=0 +lon_0=57 +k=1 +x_0=10500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28411": "+proj=tmerc +lat_0=0 +lon_0=63 +k=1 +x_0=11500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28412": "+proj=tmerc +lat_0=0 +lon_0=69 +k=1 +x_0=12500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28413": "+proj=tmerc +lat_0=0 +lon_0=75 +k=1 +x_0=13500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28414": "+proj=tmerc +lat_0=0 +lon_0=81 +k=1 +x_0=14500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28415": "+proj=tmerc +lat_0=0 +lon_0=87 +k=1 +x_0=15500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28416": "+proj=tmerc +lat_0=0 +lon_0=93 +k=1 +x_0=16500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28417": "+proj=tmerc +lat_0=0 +lon_0=99 +k=1 +x_0=17500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28418": "+proj=tmerc +lat_0=0 +lon_0=105 +k=1 +x_0=18500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28419": "+proj=tmerc +lat_0=0 +lon_0=111 +k=1 +x_0=19500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28420": "+proj=tmerc +lat_0=0 +lon_0=117 +k=1 +x_0=20500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28421": "+proj=tmerc +lat_0=0 +lon_0=123 +k=1 +x_0=21500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28422": "+proj=tmerc +lat_0=0 +lon_0=129 +k=1 +x_0=22500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28423": "+proj=tmerc +lat_0=0 +lon_0=135 +k=1 +x_0=23500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28424": "+proj=tmerc +lat_0=0 +lon_0=141 +k=1 +x_0=24500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28425": "+proj=tmerc +lat_0=0 +lon_0=147 +k=1 +x_0=25500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28426": "+proj=tmerc +lat_0=0 +lon_0=153 +k=1 +x_0=26500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28427": "+proj=tmerc +lat_0=0 +lon_0=159 +k=1 +x_0=27500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28428": "+proj=tmerc +lat_0=0 +lon_0=165 +k=1 +x_0=28500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28429": "+proj=tmerc +lat_0=0 +lon_0=171 +k=1 +x_0=29500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28430": "+proj=tmerc +lat_0=0 +lon_0=177 +k=1 +x_0=30500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28431": "+proj=tmerc +lat_0=0 +lon_0=-177 +k=1 +x_0=31500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28432": "+proj=tmerc +lat_0=0 +lon_0=-171 +k=1 +x_0=32500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28462": "+proj=tmerc +lat_0=0 +lon_0=9 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28463": "+proj=tmerc +lat_0=0 +lon_0=15 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28464": "+proj=tmerc +lat_0=0 +lon_0=21 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28465": "+proj=tmerc +lat_0=0 +lon_0=27 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28466": "+proj=tmerc +lat_0=0 +lon_0=33 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28467": "+proj=tmerc +lat_0=0 +lon_0=39 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28468": "+proj=tmerc +lat_0=0 +lon_0=45 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28469": "+proj=tmerc +lat_0=0 +lon_0=51 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28470": "+proj=tmerc +lat_0=0 +lon_0=57 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28471": "+proj=tmerc +lat_0=0 +lon_0=63 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28472": "+proj=tmerc +lat_0=0 +lon_0=69 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28473": "+proj=tmerc +lat_0=0 +lon_0=75 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28474": "+proj=tmerc +lat_0=0 +lon_0=81 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28475": "+proj=tmerc +lat_0=0 +lon_0=87 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28476": "+proj=tmerc +lat_0=0 +lon_0=93 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28477": "+proj=tmerc +lat_0=0 +lon_0=99 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28478": "+proj=tmerc +lat_0=0 +lon_0=105 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28479": "+proj=tmerc +lat_0=0 +lon_0=111 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28480": "+proj=tmerc +lat_0=0 +lon_0=117 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28481": "+proj=tmerc +lat_0=0 +lon_0=123 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28482": "+proj=tmerc +lat_0=0 +lon_0=129 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28483": "+proj=tmerc +lat_0=0 +lon_0=135 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28484": "+proj=tmerc +lat_0=0 +lon_0=141 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28485": "+proj=tmerc +lat_0=0 +lon_0=147 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28486": "+proj=tmerc +lat_0=0 +lon_0=153 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28487": "+proj=tmerc +lat_0=0 +lon_0=159 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28488": "+proj=tmerc +lat_0=0 +lon_0=165 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28489": "+proj=tmerc +lat_0=0 +lon_0=171 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28490": "+proj=tmerc +lat_0=0 +lon_0=177 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28491": "+proj=tmerc +lat_0=0 +lon_0=-177 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs",
        "28492": "+proj=tmerc +lat_0=0 +lon_0=-171 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=25,-141,-78.5,0,-0.35,-0.736,0 +units=m +no_defs +type=crs"

    }
    return zone_dict[zone]

def get_epsg284xx_by_longitude(lon):
    if not (0 <= lon <= 180):
        raise ValueError("Долгота должна быть в пределах от 0 до 180 для СК-42")

    zone_number = int(lon / 6) + 1
    epsg_code = 28400 + zone_number
    return str(epsg_code)

def convert(lat, lon):
    try:
        zone = get_epsg284xx_by_longitude(lon)
        crs = CRS.from_proj4(get_zone_byDict(zone))
        crs_wsg = CRS.from_epsg(4326)
        transformer = Transformer.from_crs(crs_wsg, crs)
        res = (transformer.transform(xx=lat, yy=lon))
    except:
          return [0,0]
    return [res[1], res[0]]



#lat = 		57.326521225217064
#lon = 	146.25000000000003




#print(get_epsg284xx_by_longitude(lon))
#convert(lat, lon)