import matplotlib.pyplot as plt
import matplotlib
from matplotlib import animation
import matplotlib.image as mpimg
import scipy as scp

import math
import pandas as pd
import numpy as np
import matplotlib.transforms as mtransforms

#TODO: need to fix 0s in time from other non drive path routines, figure out how to display graphics using intellij, implement average time take

timeDif = 0.05
#Time between frames

img = mpimg.imread("auto-grapher/infiniteRechargeFieldCrop2Vert.png.png")
print("image gotten")
data = pd.read_csv('auto.csv')
print("data gotten")
# Imports and checks data
fig = plt.figure()
ax = plt.axes(xlim=(0, 8), ylim=(0, 16))
# ax = plt.axes(xlim=(-8, 8), ylim=(-4, 4))

line, = ax.plot([], [], lw=7)

# ax.imshow(img, extent=[0, 15.98, 0, 8.21])
ax.imshow(img, extent=[0, 8.21, 0, 15.98])

#total time
print(data.t[len(data.t) - 1])


# Graph setup above
xOffset = 7.3
yOffset = 3.3


#t[0] = t[1] because we have a reset routine


#we average the time here because it  allows us to update the frames with a constant frame rate.
#The difference between each time vs average time should not be so large that this causes serious error.
normalizedLength = (int)(len(data.t) // timeDif)
normalizedDataTimes = np.array([0.0] * normalizedLength)
normalizedDataX = np.array([0.0] * normalizedLength)
normalizedDataY = np.array([0.0] * normalizedLength)


#returns both points surrounding a given time.
#also, it's pretty inefficient, but it doesnt matter and I think binary sort would be annoying to implement
def searchPts(timeIn):

    for i in range(0, len(data.t) - 1):
        if (timeIn == data.t[i]):
            return (i, i + 1)
        elif(data.t[i] <= timeIn < data.t[i + 1]):
            pos1, pos2 = i, i + 1
            return (pos1, pos2)
        elif timeIn >= data.t[len(data.t) - 1]:
            return i, i



for i in range(0, normalizedLength - 1):
    normalizedDataTimes[i] = timeDif * i

def interpData2Pts(x1, x2, y1, y2, t1, t2, tInput):
    if(t1 == t2):
        return x1, y1
    xout,yout = ((tInput - t1) / (t2 - t1) *(x2 - x1) + x1, (tInput - t1) / (t2 - t1) * (y2 - y1) + y1)
    return xout, yout

def createProperData():
     for i in range(0, normalizedLength - 2):
        pos1, pos2 = searchPts(normalizedDataTimes[i])
        normalizedDataX[i], normalizedDataY[i] = interpData2Pts(data.xPos[pos1], data.xPos[pos2], data.yPos[pos1], data.yPos[pos2], data.t[pos1], data.t[pos2], normalizedDataTimes[i])

def findAvgTime():
    return data.t[len(data.t) - 1] / len(data.t)

def findAvgTimeDeviation():
    avgTime = findAvgTime()
    last = 0
    total = 0
    for i in data.t:
        #calculate time elapsed between now and last time to find time of this "segment"
        total += abs((i - last) - avgTime)
        last = i
    return total / len(data.t)

createProperData()

def animate(i):
    # x = np.linspace(0, 2, 1000)
    # y = np.sin(2 * np.pi * (x - 0.01 * i))
    # line.set_data(x, y)
    # return line,
    x = normalizedDataX[i] + xOffset
    y = normalizedDataY[i] + yOffset
    line.set_data(np.linspace(x -0.02, x, 50), np.linspace(y - 0.02, y, 50))
    return line,


def init():
    line.set_data([], [])
    return line,

anim = animation.FuncAnimation(fig, animate, init_func=init, frames= normalizedLength, interval =  timeDif * 1000, blit=True)
plt.show()
