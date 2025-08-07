def decdeg_to_ddmmss(inp):
    fl=float(inp)
    d = int(fl)
    m = round(((fl - d) * 60.0), 2)
    s = round(((fl - d - m/60.0) * 3600.0), 2)
    return [d,m,s]

def uglomer(dms):
    res = (dms[0]*60+dms[1])/3.6
    return str(res)[:2]+"-"+str(int(res))[2:]