import os
import csv
from sklearn.ensemble import RandomForestClassifier
from joblib import dump, load
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from sklearn.decomposition import PCA
from sklearn.preprocessing import StandardScaler
import numpy as np
from sklearn import svm
import matplotlib.pyplot as plt

def threshold(Xtest,ytest):
    count = 0
    for i,j in zip(Xtest,ytest):
        s = sum(i)
        if s < 0 :  #Threshold 1  
            pred = 2
        elif s<6 and s > 0: #Threshold 2
            pred = 3
        else:
            pred = 1
        if pred == j :
            count = count+1
    return count/len(Xtest)


TrainData="training"
label={}
lval=[]
Features=[]
count=0
for (dirpath,dirnames,filenames) in os.walk(TrainData):
    for dirname in dirnames:
        count=count+1
        label[count] = dirname
        for(direcpath,direcnames,files) in os.walk(TrainData+"\\"+dirname):
            for file in files:
                if 'txt' in file:
                    G=[]
                    path=TrainData+"\\\\"+dirname+"\\\\"+file
                    f=open(path)
                    lval.append(count)
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
D={}                    
X_train, X_test, y_train, y_test = train_test_split(Features, lval, test_size=0.90, random_state=42)
rf = RandomForestClassifier(n_estimators=100, max_depth=2,random_state=0)
rf.fit(X_train,y_train)
y_pred= rf.predict(X_test)
print("RF predict",y_pred)
D['Random Forest'] = (accuracy_score(y_test,y_pred))
dump(rf, 'RF.Model')
#clf.fit(Features, lval)

#dump(label,'Activity.Dict')
svc = svm.SVC(gamma='scale')
svc.fit(X_train,y_train)
y_pred= svc.predict(X_test)
print("SVC predict",y_pred)
D['SVC'] = (accuracy_score(y_test,y_pred))
dump(svc, 'SVC.Model')

D['Threshold Classifier']= threshold(X_test,y_test)
print("label",label)
dump(label,'Activity.Dict')
print('dict',D)
best=max(D,key=D.get)
print(best)
if best=='SVC':
    dump(svc,'best.MODEL')
elif best=='Random Forest':
    dump(rf,'best.MODEL')
else:
    dump(threshold(X_test,y_test),'best.MODEL')
plt.figure();
plt.bar(range(len(D)), list(D.values()), align='center')
plt.xticks(range(len(D)), list(D.keys()))
plt.title("Algorithm Comparision")
plt.show()


