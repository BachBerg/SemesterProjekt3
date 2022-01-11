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

fs = 500
Ts = 1/fs

t = np.arange(0,2000*Ts,Ts)


ecg0 = signals[:,0]
ecg1 = signals[:,1]


# Plot signal i tidsdomæne
plt.plot(t,ecg0)
plt.title('Tidsdomæne')
plt.xlabel('tid (s)')
plt.show()



#Baseline wander

#f0 & f1 står for fnotch fitler (for baseline wander og powerline interference)
f0=0.6
f1=60


#notch filter til at fjerne baseline wander 
#Q = w0/bw. 0,6/5 (Q værdi)
Q1=f0/5
Q2=f0/5

#Fc (cutt-off frekvens)
Fc=0.002



#notch filter

b1, a1 = signal.iirnotch(f0, Q1, fs)

freq1, h1= signal.freqz(b1, a1, fs=fs)

FilteretUBWS = signal.filtfilt (b1, a1 , ecg0)

plt.plot(FilteretUBWS, color='red')
plt.plot(ecg0, color='blue')
plt.show()


Q2=f1/5



b2, a2 = signal.iirnotch(f1, Q2, fs)

freq2, h2 = signal.freqz(b2, a2, fs=fs)

FilteretUBP = signal.filtfilt (b2, a2 , FilteretUBWS)

plt.plot(FilteretUBP, color='red')
plt.plot(ecg0, color='blue')
plt.show()

