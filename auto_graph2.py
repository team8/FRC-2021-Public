import matplotlib.pyplot as plt
import matplotlib
from matplotlib import animation
import matplotlib.image as mpimg

import math
import pandas as pd
import numpy as np
import matplotlib.transforms as mtransforms

#TODO: need to fix 0s in time from other non drive path routines, figure out how to display graphics using intellij,

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

def animate(i):
    # x = np.linspace(0, 2, 1000)
    # y = np.sin(2 * np.pi * (x - 0.01 * i))
    # line.set_data(x, y)
    # return line,
    x = data.x[i] + xOffset
    y = data.y[i] + yOffset
    line.set_data(np.linspace(x -0.02, x, 50), np.linspace(y - 0.02, y, 50))
    return line,


def init():
    line.set_data([], [])
    return line,

anim = animation.FuncAnimation(fig, animate, init_func=init, frames=len(data.x), interval= 25, blit=True)
plt.show()
