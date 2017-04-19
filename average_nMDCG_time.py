import pandas as pd
import numpy as np
import math
import sys
import glob,os
import csv

gt=pd.DataFrame.from_csv("Time_DATASET/time_Ground_Truth.tsv",sep="\t")

def file_list():
    l=[]
    os.chdir(".")
    for file in glob.glob("collection-time/*text.tsv*"):
        l.append(file)
    return l

docs=file_list()

def maxMDCG(k,a):
    somma=0
    try:
        lun=len(gt.iloc[:,0][a].values)
        so=[]
        if k==1:
            somma=1
        if lun >=k:
            for j in range(1,k):
                s=1/math.log(j+1,2)
                so.append(s)
                somma=1+sum(so)
        else:
            for j in range(1,lun):
                s=1/math.log(j+1,2)
                so.append(s)
                somma=1+sum(so)
    except:
        pass
    return(somma)

n=len(docs)
k=int(sys.argv[1])

def nMDCG(query,k):
    rel=[0]
    finale=[0]
    z=pd.DataFrame.from_csv(docs[i],sep="\t",index_col=None)
    for a in range(1,len(z.iloc[:,0].unique())+1):
        m=maxMDCG(k,a)
        if m==0:
            m=m+0.00001
        so=[]
        s=0
        try:
            iniz=0
            if query.get_value(a,"Doc_ID")[0] in gt.iloc[:,0][a].values:
                iniz=1
            else:
                iniz=0
        except:
            pass
        rel.append(iniz)

        for j in range (1,k):
            try:
                if query.get_value(a,"Doc_ID")[j] in gt.iloc[:,0][a].values:
                    s=1/math.log(j+1,2)
                    so.append(s)
            except:
                pass
        fin=rel[a]+sum(so)
        finale.append(fin/m)
    return(np.mean(finale[1:]))

for i in range(n):
    query=pd.DataFrame.from_csv(docs[i],sep="\t")
    print(docs[i],nMDCG(query,k))


with open('collection-time/'+'nMDCG-' + str(k) + '.csv','w') as f1:
    writer=csv.writer(f1, delimiter='\t',lineterminator='\n',)
    writer.writerow(["file","average nMDCG"])
    for i in range(n):
        query=pd.DataFrame.from_csv(docs[i],sep="\t")
        row = docs[i],nMDCG(query,k)
        writer.writerow(row)
