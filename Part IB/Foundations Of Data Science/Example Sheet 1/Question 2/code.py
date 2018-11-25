import matplotlib.pyplot as plt  # for plotting (MATLAB-style)
import numpy as np               # for numerical computation (MATLAB-style)
import scipy.optimize            # for numerical optimization
import pandas                    # for data import
import csv

reader = csv.DictReader(open("wasp.csv"), delimiter=',')
print("Opened File")
day1 = []
day2 = []
day3 = []
for line in reader:
    if line["day"] == "1":
        day1.append(line["t_sec"])
    elif line["day"] == "2":
        day2.append(line["t_sec"])
    elif line["day"] == "3":
        day3.append(line["t_sec"])
    else:
        print("wtf: "+line["day"]+", "+line["t_sec"])
print("Read file into arrays")
plt.plot(range(10), day1, "Day 1")
plt.plot(range(10), day2, "Day 2")
plt.plot(range(10), day3, "Day 3")
plt.show


NOTE  INCOMPLETE AND NON-FUNCTIONAL, GO TO AZURE NOTEBOOK FOR "WORKING" VERSION
