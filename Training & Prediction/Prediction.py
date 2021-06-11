import os
import csv
from sklearn.ensemble import RandomForestClassifier
from joblib import dump, load
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from sklearn.decomposition import PCA
from sklearn.preprocessing import StandardScaler
import numpy as np
import time
import warnings
warnings.filterwarnings("ignore")

TestData="Test"
while True:
    for(direcpath,direcnames,files) in os.walk(TestData):
        for file in files:
            if 'txt' in file:
                Features=[]
                time.sleep(1)
                f=open(TestData+'/'+file)
                fet = ''
                for i in f.readlines():
                    fet = fet+'#' + i.replace('\n','')
                #print('fet',fet)
                fet=fet[1:]
                #print('fet',fet)
                val = fet.replace('#',' ')
                #print('val',val)
                v=val.split()
                #print('v',v)
                vl=[]
                for x in v:
                    vl.append(float(x))
                #print('vl',vl,len(vl))
                na=np.array(vl)
                #print(na)
                #val = np.array(val[1:])
                #print('next val',val)
                val = np.reshape(na,(9,3))
                #print('reshaped val',val)
                x = StandardScaler().fit_transform(val)
                pca = PCA(n_components=1)
                principalComponents = pca.fit_transform(x)
                principalComponents=np.reshape(principalComponents,(3,3))
                Features.append(principalComponents[0])
                best = load('best.Model')
                
                label = load('Activity.Dict')
                print('label',label)
                best_p = best.predict(Features)
                print(best_p,best_p[0])
                Activity = label[best_p[0]]
                
                print('activity',Activity)
                #rf_p = rf.predict(Features)
                #Activity = label[rf_p[0]]
                #print('RF',Activity)
                f.close()
                f=open('output.txt','w')
                f.write(Activity)
                f.close()
                os.remove(TestData+'/'+file)
                
            
