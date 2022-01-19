# -*- coding: utf-8 -*-
"""
Created on Wed Jan 12 09:11:48 2022

@author: Hawra
"""

import matplotlib.pyplot as plt
import numpy as np
from scipy import signal
import requests
import json

import wfdb


# Read the two ECG signals (raw, filtered) and read 
# the related information.
#indlæs signalet (råt ufilteret)
signals, info = wfdb.rdsamp('rec_1', channels=[0, 1], 
                              sampfrom=0, sampto=10000)



Fs=500
Ts=1/Fs


t=np.arange(0,10000*Ts,Ts)


#ecg 0 er rå signal
#ecg 1 er filtreret signal
ecg0 = signals[:,0]
ecg1 = signals[:,1]


#plot signal i tidsdomænet 

#plt.title('EKG med støj')
#plt.xlabel('tid(s)')
#plt.ylabel('mV')
#plt.plot(t,ecg0)
#plt.show()


#ekg uden støj ecg1
plt.title('EKG uden støj')
plt.xlabel('tid(s)')
plt.ylabel('mV')
plt.plot(t,ecg1)
plt.show()

numtaps = 301


# HighPass FIR - Filter basewander filter


signal.firwin
Filter =  signal.firwin(numtaps, cutoff=1.3, fs = 1/Ts, pass_zero=False)
Filteretsignal = signal.filtfilt (Filter,1 , ecg0)
#plt.plot(t,Filteretsignal)
#plt.show()


#Plot signal i frekvensdomænet
#plt.magnitude_spectrum(ecg0, Fs,color="black" )
#plt.title('Frekvensdomænet')
#plt.show()



#Baseline wander

#f0 & f1 står for fnotch fitler (for baseline wander og powerline interference)
f0=0.6
f1=60



#notch filter til at fjerne baseline wander 
#Q = w0/bw. 0,6/5 (Q værdi)
Q1=0.6/5


#Fc (cutt-off frekvens)
Fc=0.002



#notch filter til at fjerne baseline wander støj
b1, a1 = signal.iirnotch(f0, Q1, Fs)
notchfilter, h1 = signal.freqz(b1, a1, fs=Fs)
FilteretUBWS = signal.filtfilt (b1,a1 , Filteretsignal)

#plt.title("EKG uden basline wander støj")
#plt.plot(FilteretUBWS)


#powerline


Q2=f1/5

# notch filter til at fjerne Powerline interference støj

b2, a2 = signal.iirnotch(f1, Q2, Fs)
freq2, h2 = signal.freqz(b2, a2, fs=Fs)
FilteretUBP = signal.filtfilt (b2, a2 , FilteretUBWS)

#plt.title("EKG uden basline wander støj+ Powerline støj")
#plt.plot(FilteretUBP)

#plt.show()

b2, a2 = signal.iirnotch(f1, Q2, Fs)
freq2, h2 = signal.freqz(b2, a2, fs=Fs)
basewanderfiltermednotch = signal.filtfilt (b2, a2 , Filteretsignal)




#spectrum for EKG uden baseline wander støj

#plt.magnitude_spectrum(FilteretUBWS,Fs, color='red')
#plt.title("EKG uden basline støj")
#plt.xlabel("frekvensdommænet")
#plt.show()



#spectrum for EKG uden basline wander støj+ Powerline støj

#plt.magnitude_spectrum(FilteretUBP,Fs, color="green")
#plt.title("EKG uden basline wander støj+ Powerline støj")
#plt.ylabel("frekvensdommænet")
#plt.show()




#Ma filter til at fjerne EMG støj
#array på 1/8 forekommer 8 gange.

Z=np.array((1/8,1/8,1/8,1/8,1/8,1/8,1/8,1/8))

FilteretEMG=signal.filtfilt(Z,1,FilteretUBP)
FilteretEMG2=signal.filtfilt(Z,1,basewanderfiltermednotch)

#plotte filteret ecg1 =pink

plt.plot(t,FilteretEMG)
plt.title('Tidsdomæne')
plt.xlabel('tid(s)')
plt.xlabel('mV')
plt.show()

plt.plot(t,FilteretEMG2)
plt.title('Tidsdomæne')
plt.xlabel('tid(s)')
plt.xlabel('mV')
plt.show()
#signal i frekvensdomænet
#plt.magnitude_spectrum(FilteretEMG,Fs)
#plt.title('EKG uden EMG støj')
#plt.show()



#der hvor data tilsendes
endpoint = "http://ekg2.diplomportal.dk:8080/data/ekgSessions"


cpr= "2222222222" #CPR
Headers={'Authorization': 'Bearer hemmeliglogin', #Loginkode 
         'Identifier': cpr} 

  
#Post request, med endpoint, dataen og headers til genkendelse og sikkerhed.
r = requests.post(endpoint,  data = json.dumps(FilteretEMG2.tolist()), headers = Headers )
  
#Modtage responbesked 
Response = r.text
print(Response)

