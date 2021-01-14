import sys
import threading
import time
import matplotlib.transforms as transforms
import matplotlib.pyplot as plt
import matplotlib
from matplotlib import animation
import matplotlib.image as mpimg
import scipy as scp
import random
import math
import pandas as pd
import numpy as np
import matplotlib.transforms as mtransforms
import matplotlib.patches as patches



try:
    sys.argv[1]
except IndexError:
    print("No option chosen - would you like to save or show simulation?")
    print("Add the word show after the python run command to show a visualization of the auto")
    print("Add the word save after the python run command to save the simulation as a gif in resources/")
    exit()

#TODO: need to fix 0s in time from other non drive path routines, figure out how to display graphics using intellij, implement average time take

timeDif = 0.05
#Time between frames

img = mpimg.imread("./resources/infiniteRechargeField.png")
print("Field Image Found")
data = pd.read_csv('./resources/auto.csv')
print("Auto Path Found")

# Imports and checks data
fig = plt.figure()
ax = plt.axes(xlim=(0, 8), ylim=(0, 16))
# ax = plt.axes(xlim=(-8, 8), ylim=(-4, 4))

line, = ax.plot([], [], lw=7)

# ax.imshow(img, extent=[0, 15.98, 0, 8.21])
ax.imshow(img, extent=[0, 8.21, 0, 15.98])

#total time
autoDuration = data.t[len(data.t) - 1]


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
normalizedDataAngle = np.array([0.0] * normalizedLength)

#robot = patches.Rectangle((normalizedDataX - 0.25, normalizedDataY - 0.25), 50, 50, fc='y') 

robot = patches.Rectangle((0, 0), 0, 0, fc='y') 
elapsedTime = ax.text(0.05, 0.9, '', transform=ax.transAxes, color='white', fontsize=14)
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
        normalizedDataAngle[i] = (data.d[pos1] + data.d[pos2])/2
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

frame_generator = [1]
def animate(i):
    global frame_generator
    x = normalizedDataX[i] + xOffset
    y = normalizedDataY[i] + yOffset
    d = normalizedDataAngle[i]
    t = normalizedDataTimes[i]
    # print(x, y, d)
    robot.set_width(0.5)
    robot.set_height(0.5)
    robot.set_xy([x, y])
    robot.set_transform(transforms.Affine2D().rotate_deg_around(x,y,d) + ax.transData)
    timePassed = round(t, 2)
    print(timePassed)
    if timePassed > autoDuration:
        elapsedTime.set_color('red')
    else:
        elapsedTime.set_text('{:.2f}'.format(normalizedDataTimes[i]))
        frame_generator.append(frame_generator[-1] + 1)
    return robot, elapsedTime

def init():
    ax.add_patch(robot)
    return robot,

if (sys.argv[1] == 'save'):
    anim = animation.FuncAnimation(fig, animate, init_func=init, frames=250,interval =  timeDif * 1000, blit=True, repeat=False)
    anim.save('resources/Autonomous Simulation.gif',writer=animation.PillowWriter(fps=30))
elif (sys.argv[1] == 'show'):
    anim = animation.FuncAnimation(fig, animate, init_func=init, frames=frame_generator,interval =  timeDif * 1000, blit=True, repeat=False)
    plt.show()
else:
    print("Invalid Option")
