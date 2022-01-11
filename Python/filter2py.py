# -*- coding: utf-8 -*-
"""
Created on Fri Jan  7 12:24:38 2022

@author: Hawra
"""


import matplotlib.pyplot as plt
import numpy as np
from scipy import signal

import wfdb

# Read the two ECG signals (raw, filtered) and read 
# the related information.
signals, info = wfdb.rdsamp('rec_2', channels=[0, 1], 
                              sampfrom=0, sampto=2000)

Fs = 500
Ts = 1/Fs
t = np.arange(0,2000*Ts,Ts)

ecg0 = signals[:,0]
ecg1 = signals[:,1]


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
notchfilter= signal.freqz(b1, a1, fs=Fs)
FilteretUBWS = signal.filtfilt (notchfilter,1 , ecg0)

plt.title("EKG uden basline wander støj")
plt.plot(FilteretUBWS)
FilteretUBWS


outputsignal1=FilteretUBWS




#powerline


Q2=60/120

# fjern Powerline interference støj

b2, a2 = signal.iirnotch(f1, Q2, Fs)
freq, h = signal.freqz(b2, a2, fs=Fs)
FilteretUBP = signal.filtfilt (notchfilter,1 , outputsignal1)

plt.title("EKG uden basline wander støj+ Powerline støj")
plt.plot(FilteretUBP)

plt.show()


outputsignal2=FilteretUBP





#frekvensdomænet



#spectrum for EKG uden baseline wander støj

plt.magnitude_spectrum(FilteretUBWS,Fs, color='red')
plt.title("EKG uden basline støj")
plt.xlabel("frekvensdommænet")



#spectrum for EKG uden basline wander støj+ Powerline støj

plt.magnitude_spectrum(FilteretUBP,Fs, color="green")
plt.title("EKG uden basline wander støj+ Powerline støj")
plt.ylabel("frekvensdommænet")