import matplotlib.pyplot as plt
import matplotlib.colors
import xml.etree.ElementTree as ET
from ast import literal_eval

def parse1Darray(s):
    
    l = s.split(",")
    try:
        return [float(elt) for elt in l]
    except ValueError:
        return []

def parse2Darray(s):
    
    l = s.split(";")
    return [parse1Darray(elt) for elt in l]

def retrieve(s):
    
    try:
        return literal_eval(s)
    except (ValueError, SyntaxError):
        return s

def parseTuple(s):
    
    s = s[1:-1]
    return [float(x) for x in s.split(",")]

def plot(filename,show=True,save=False):
    
    tree = ET.parse(filename)
    root = tree.getroot()
    
    datas = {}
    
    # First we need to recover the data
    
    for elt in root:
                
        if elt.tag == "data":
            datas[elt.get("name")] = parse1Darray(elt.text)
        elif elt.tag == "data2D":
            datas[elt.get("name")] = parse2Darray(elt.text) 
    
    plt.rcParams['font.size'] = '15'
    plt.rcParams['text.usetex'] = 'True'
    #plt.rcParams["font.family"] = "CMU"
    fig,ax = plt.subplots(figsize=(10,6))
    ax.set(**root.attrib)
    
    
    # Next we can plot
    needLegend = False
    
    for elt in root:
        
        params = elt.attrib
            
        for key in params:
            params[key] = retrieve(params[key])
        
        
        if elt.tag == "plot":
            
            x = params.pop("x")
            y = params.pop("y")
            
            X,Y = [],[]
            
            if (len(x) == 0) :
                Y = datas[y]
                X = [i for i in range(len(Y))]
                
            elif (len(y) == 0) :
                X = datas[x]
                Y = [i for i in range(len(X))]
            else :
                X = datas[x]
                Y = datas[y]
            
            ax.plot(X,Y,**params)
            
            if "label" in params:
                needLegend = True
        
        if elt.tag == "scatter":
            
            x = params.pop("x")
            y = params.pop("y")
            
            if "c" in params and params["c"] in datas:
                params["c"] = datas[params["c"]]
            
            X,Y = [],[]
            
            if (len(x) == 0) :
                Y = datas[y]
                X = [i for i in range(len(Y))]
            elif (len(y) == 0) :
                X = datas[x]
                Y = [i for i in range(len(X))]
            else :
                X = datas[x]
                Y = datas[y]
            
            ax.scatter(X,Y,**params)
            
            if "label" in params:
                needLegend = True
        
        if elt.tag == "axvline":
            
            x = params.pop("x")
            ax.axvline(float(x),**params)
        
        if elt.tag == "imshow":
            
            X = params.pop("X")
            
            if "extent" in params:
                params["extent"] = parseTuple(params["extent"])
            
            if "norm" in params:
                tuple = parseTuple(params["norm"])
                params["norm"] = matplotlib.colors.Normalize(vmin=tuple[0],vmax=tuple[1])
            
            im = ax.imshow(datas[X],**params)
            plt.colorbar(im)
        
    if needLegend:
        plt.legend()
    
    
    if save:
        i = 0
        while filename[i] != '.':
            i += 1
        plt.savefig(filename[:i]+".png",dpi=300)
    
    if show:
        plt.show()
    else:
        plt.close()
    

if __name__ == "__main__":

    if len(sys.argv) == 1:
        
        plot("tmp.xml",save=False)

    if len(sys.argv) == 2:

        plot(sys.argv[1],save=False)

    if len(sys.argv) == 3 and (sys.argv[1] == "-s" or sys.argv[1] == "--save"):

        plot(sys.argv[2],save=True)














