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
print(data.t[len(data.t) - 1])


avgTime = data.t
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

xData = np.empty(len(data.t))
yData = np.empty(len(data.t))
dData = np.empty(len(data.t))
tData = np.empty(len(data.t))
animatedData = np.array(xData,yData, dData, tData)

print()

animatedData.x = ()

def animate(i):
    x = animatedData.xData[i] + xOffset
    y = animatedData.yData[i] + yOffset
    line.set_data(np.linspace(x -0.02, x, 50), np.linspace(y - 0.02, y, 50))
    return line,


def init():
    line.set_data([], [])
    return line,

anim = animation.FuncAnimation(fig, animate, init_func=init, frames=len(animatedData.x), interval= frameInterval, blit=True)
plt.show()
