# -*- coding: utf-8 -*-
"""
Created on Mon Jan 10 11:07:53 2022

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

Fs=500
Ts=1/Fs

t=np.arange(0,2000*Ts,Ts)



ecg0 = signals[:,0]
ecg1 = signals[:,1]


#Ma filter til at fjerne EMG støj
#array på 1/8 forekommer 8 gange.

Z=np.array((1/8,1/8,1/8,1/8,1/8,1/8,1/8,1/8))

FilteretEMG=signal.filtfilt(Z,1,ecg0)


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



#def running_maan[ecg0, N]:
   # """x == an array of data. N == number of samples per average """
    
  #  cumsum= np.cumsum(np.insert(ecg0, 0, 0))
   # return (cumsum[N:] - cumsum[:-N]) / float(N)
#print(running_mean(ecg0, 3))