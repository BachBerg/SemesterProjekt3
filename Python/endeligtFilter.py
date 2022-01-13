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
signals, info = wfdb.rdsamp('rec_2', channels=[0, 1],
                              sampfrom=0, sampto=2000)

Fs=500
Ts=1/Fs


t=np.arange(0,2000*Ts,Ts)



ecg0 = signals[:,0]
ecg1 = signals[:,1]


#plot signal i tidsdomænet

plt.title('ekg med støj')
plt.xlabel('tid(s)')
plt.ylabel('mV')
plt.plot(t,ecg0)
plt.show()


#ekg uden støj ecg1
plt.title('ekg uden støj')
plt.xlabel('tid(s)')
plt.ylabel('mV')
plt.plot(t,ecg1)
plt.show()

numtaps = 301


# HighPass FIR - Filter


signal.firwin
Filter =  signal.firwin(numtaps, cutoff=0.667, fs = 1/Ts, pass_zero=False)
Filteretsignal = signal.filtfilt (Filter,1 , ecg0)
plt.plot(t,Filteretsignal)
plt.show()


#frekvensdomænet
plt.magnitude_spectrum(ecg0, Fs)
plt.title('Frekvensdomænet')
#plt.plot(ecg0, Fs)

plt.show()



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

plt.title("EKG uden basline wander støj")
plt.plot(FilteretUBWS)
FilteretUBWS




#powerline


Q2=f1/5

# fjern Powerline interference støj

b2, a2 = signal.iirnotch(f1, Q2, Fs)
freq2, h2 = signal.freqz(b2, a2, fs=Fs)
FilteretUBP = signal.filtfilt (b2, a2 , FilteretUBWS)

plt.title("EKG uden basline wander støj+ Powerline støj")
plt.plot(FilteretUBP)

plt.show()





#spectrum for EKG uden baseline wander støj

plt.magnitude_spectrum(FilteretUBWS,Fs, color='red')
plt.title("EKG uden basline støj")
plt.xlabel("frekvensdommænet")
plt.show()



#spectrum for EKG uden basline wander støj+ Powerline støj

plt.magnitude_spectrum(FilteretUBP,Fs, color="green")
plt.title("EKG uden basline wander støj+ Powerline støj")
plt.ylabel("frekvensdommænet")
plt.show()




#Ma filter til at fjerne EMG støj
#array på 1/8 forekommer 8 gange.

Z=np.array((1/8,1/8,1/8,1/8,1/8,1/8,1/8,1/8))

FilteretEMG=signal.filtfilt(Z,1,FilteretUBP)


plt.plot(t,FilteretEMG)
plt.plot(t,ecg1,color='pink')
plt.title('Tidsdomæne')
plt.xlabel('tid(s)')
plt.xlabel('mV')
plt.show()


#frekvensdomænet
plt.magnitude_spectrum(FilteretEMG,Fs)
plt.title('EKG uden EMG støj')
plt.show()





endpoint = "http://localhost:8080/Semesterprojekt3_war/data/ekgSessions"


cpr= "1234567890" #CPR
Headers={'Authorization': 'Bearer hemmeliglogin', #Loginkode
         'Identifier': cpr}


# Post request, med endpoint, dataen og headers til genkendelse og sikkerhed.
r = requests.post(endpoint,  data = json.dumps(FilteretEMG.tolist()), headers = Headers )

#Modtage responbesked
Response = r.text
print(Response)