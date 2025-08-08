def decdeg_to_ddmmss(inp):
    fl=float(inp)
    d = int(fl)
    m = round(((fl - d) * 60.0), 2)
    s = round(((fl - d - m/60.0) * 3600.0), 2)
    if s >= 30:
        m+=1
    return [d,m]

def count(dms):
    if dms[1] == 0:
        res = round(dms[0]/6.0, 2)
    else:
        res=round(((dms[0]*60+dms[1])/3.6)/100.0, 2)
    return format_res(res)

def format_res(res):
    # Округляем до 2 знаков после запятой и превращаем в строку
    st = f"{res:.2f}"  # например, 5.5 → '5.50'
    s = st.replace(".", "")  # убираем точку → '550'
    # Всегда берём первые 4 символа, добавляем нули при необходимости
    s = s.ljust(4, "0")  # если короткая, дополняем нулями

    if res < 10:
        return f"0{s[0]}-{s[1]}{s[2]}"
    else:
        return f"{s[0]}{s[1]}-{s[2]}{s[3]}"

def uglomer(inp):
    return count(decdeg_to_ddmmss(inp))