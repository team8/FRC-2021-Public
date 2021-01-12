import matplotlib.pyplot as plt
import matplotlib
from matplotlib import animation
import matplotlib.image as mpimg

import math
import pandas as pd
import numpy as np
import matplotlib.transforms as mtransforms


#TODO: figure out issue with timing in program!

img = mpimg.imread("C:/Users/Nolan/Downloads/Image Download/infiniteRechargeFieldCrop2Vert.png")
print("image gotten")
data = pd.read_csv('C:/Users/Nolan/Documents/Robotics/FRC-2021-Private/auto.csv')
print("data gotten")
# Imports and checks data

fig = plt.figure()
ax = plt.axes(xlim=(0, 8), ylim=(0, 16))
# ax = plt.axes(xlim=(-8, 8), ylim=(-4, 4))



line, = ax.plot([], [], lw=7)

# ax.imshow(img, extent=[0, 15.98, 0, 8.21])
ax.imshow(img, extent=[0, 8.21, 0, 15.98])


# Graph setup above
xOffset = 7.3
yOffset = 3.3
interpPeriod = 0.05
frameInterval = 1000 * interpPeriod

#t[0] = t[1] because we have a reset routine

#total time
print(data.t[len(data.t)-1])


#we average the time here because it allows us to update the frames with a constant frame rate.
#The difference between each time vs average time should not be so large that this causes serious error.
def findAvgTime():
    return data.t[len(data.t) - 1] / len(data.t)

def findAvgTimeDeviation():
    avg = findAvgTime()
    last = 0
    total = 0
    for i in data.t:
        #calculate time elapsed between now and last time to find time of this "segment"
        total += abs((i - last) - avgTime)
    return total / len(data.t)

avgTime = findAvgTime()

def interp(t1, t2, tGet, p1, p2):
    if t1 == t2:
        return p1
    return (tGet / (t1-t2)) * (p1 + (p2 - p1))

lenAnimatePoints = ((int)(data.t[len(data.t) -1] // interpPeriod))
tData = np.empty(lenAnimatePoints)
xData = np.empty(len(tData))
yData = np.empty(len(tData))
dData = np.empty(len(tData))

for i in range(0, (int)(data.t[len(data.t) - 1] // interpPeriod)):
    tData[i] = i * interpPeriod

def fillArr(arrTo, oldArr):
    previousVal = 0
    tempPos = 0
    outFlagRaised = False
    for pos in range(len(tData) - 1):
        i = pos * interpPeriod
        if(tempPos < len(data.t)):
            outFlagRaised = True
        #literally just doing this so the program doesn't brick itself checking if its out of bounds
        if(not outFlagRaised):
            while (i < data.t[tempPos+1] - interpPeriod):
                arrTo[pos] = interp(previousVal, data.t[tempPos], i, oldArr[tempPos], oldArr[tempPos+1])
                pos += 1
                i = pos * interpPeriod
                previousVal = pos * interpPeriod
            previousVal = i
            tempPos += 1

fillArr(xData, data.xPos)
fillArr(yData, data.yPos)
fillArr(dData, data.d)

animatedData = np.array([xData, yData, dData, tData])

for i in animatedData[0]:
    print(i)

def animate(i):
    x = animatedData[0] + xOffset
    y = animatedData[1] + yOffset
    line.set_data(np.linspace(x -0.02, x, 50), np.linspace(y - 0.02, y, 50))
    return line,


def init():
    line.set_data([], [])
    return line,

anim = animation.FuncAnimation(fig, animate, init_func=init, frames=len(animatedData[0]), interval= frameInterval * 100, blit=True)
plt.show()
