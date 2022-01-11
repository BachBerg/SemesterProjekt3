# -*- coding: utf-8 -*-
"""
Created on Thu Jan  6 13:15:37 2022

@author: Hawra
"""


import matplotlib.pyplot as plt
import numpy as np
from scipy import signal


Fs = 500
Ts = 1/Fs
t = np.arange(0,2000*Ts,Ts)


import wfdb



# Read the two ECG signals (raw, filtered) and read 
# the related information.
signals, info = wfdb.rdsamp('rec_1', channels=[0, 1], 
                              sampfrom=0, sampto=2000)


 # ecg0 er råt signal, 1 er filteret signal
ecg0 = signals[:,0]
ecg1 = signals[:,1]



#plot signal i tidsdomænet 


plt.title('ekg med støj')
plt.xlabel('tid(s)')
plt.ylabel('milivolts')
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
plt.magnitude_spectrum(Filteretsignal, Fs)
plt.plot(Filteretsignal, Fs)

plt.show()