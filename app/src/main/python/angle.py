def dms(inp):
    if inp=="0" or inp=="":
        return [0,0,0]
    fl=float(inp)
    d = int(fl)
    m = round(((fl - d) * 60.0), 2)
    s = round(((fl - d - m/60.0) * 3600.0), 2)
    return [d,m,s]

def count(dms):
    res = (dms[0]*60+dms[1])/3.6
    s = str(int(res))
    while(len(s)<4):
        s="0"+s
    return s[:2]+"-"+s[2:]

def uglomer(inp):
    return count(dms(inp))