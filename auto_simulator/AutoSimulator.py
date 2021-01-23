import json
import sys
import threading
import time
import matplotlib.transforms as transforms
import matplotlib.pyplot as plt
import matplotlib
from matplotlib import animation, pylab
import matplotlib.image as mpimg
import scipy as scp
import random
import math
import pandas as pd
import numpy as np
import matplotlib.transforms as mtransforms
import matplotlib.patches as patches


kRobotWidth = 0.40
kRobotHeight = 0.55

try:
    sys.argv[1]
except IndexError:
    print("No option chosen - would you like to save or show simulation?")
    print("Add the word show after the python run command to show a visualization of the auto")
    print("Add the word save after the python run command to save the simulation as a gif in resources/")
    print("Add the word create after the python run command to find field coordinates for auto")
    exit()

#TODO: need to fix 0s in time from other non drive path routines, figure out how to display graphics using intellij, implement average time take

timeDif = 0.05
autoCSVPath = ""
selectedAuto = ""
with open("resources/AutoConstants.json") as f:
    auto_constants = json.load(f)
    selectedAuto = auto_constants['SelectedAuto']
    autoCSVPath = auto_constants[selectedAuto]['autoCSVPath']
    selectedField = auto_constants[selectedAuto]['SelectedField']
with open("resources/FieldConstants.json") as g:
    auto_field_constants = json.load(g)
    autoFieldImagePath = auto_field_constants[selectedField]['fieldPath']
    fieldDimensionsX = auto_field_constants[selectedField]['fieldDimensionsX']
    fieldDimensionsY = auto_field_constants[selectedField]['fieldDimensionsY']

    # Graph setup above
    xOffset = auto_constants[selectedAuto]['xPosInit']
    yOffset = auto_constants[selectedAuto]['yPosInit']

img = mpimg.imread("." + autoFieldImagePath)
print("Field Image Found")
data = pd.read_csv('.' + autoCSVPath)
print("Auto Path Found")



autoDuration = data.t[len(data.t) - 1]

default_dpi = matplotlib.rcParamsDefault['figure.dpi']
matplotlib.rcParams['figure.dpi'] = default_dpi*1.5 #scaling window to 1.5x the default size
# Imports and checks data
fig = plt.figure()
ax = plt.axes(xlim=(0, fieldDimensionsX), ylim=(0, fieldDimensionsY))

line, = ax.plot([], [], lw=7)

ax.imshow(img, extent=[0, fieldDimensionsX, 0, fieldDimensionsY])
#ax.imshow(img, extent=[0, 16.42, 0, 31.98])


#t[0] = t[1] because we have a reset routine
def displayMousePosition(event):
    print(event.xdata, event.ydata)
    fig.canvas.set_window_title("Mouse Click @ (" + str(int(event.ydata * 100) / 100) + ", " + str(fieldDimensionsX - int(event.xdata * 100) / 100) + ")")

fig.canvas.mpl_connect('button_press_event', displayMousePosition)
if (sys.argv[1] == 'create'):
    fig.canvas.mpl_connect('button_press_event', displayMousePosition)
    fig.show()
    plt.show()

#we average the time here because it  allows us to update the frames with a constant frame rate.
#The difference between each time vs average time should not be so large that this causes serious error.
normalizedLength = (int)(len(data.t) // timeDif)
normalizedDataTimes = np.array([0.0] * normalizedLength)
normalizedDataX = np.array([0.0] * normalizedLength)
normalizedDataY = np.array([0.0] * normalizedLength)
normalizedDataAngle = np.array([0.0] * normalizedLength)
#dtype here says that the string length can be any size
normalizedDataRoutines = np.array([""] * normalizedLength,  dtype=object)


robot = patches.Rectangle((0, 0), 0, 0, fc='y')
robotPointer = patches.Rectangle((0,0),0,0, fc = 'y', color= 'white')
elapsedTime = ax.text(0.05, 0.9, '', transform=ax.transAxes, color='black', fontsize=14)
runningRoutine = ax.text(0.5, 0.9, '', transform=ax.transAxes, color='black', fontsize=14)
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

def interpLinear(x1, x2, t1, t2, tInput):
    if(t1 == t2):
        return x1
    xout = ((tInput - t1) / (t2 - t1) *(x2 - x1) + x1)
    return xout

def interpData2Pts(x1, x2, y1, y2, t1, t2, tInput):
    xout,yout = interpLinear(x1,x2,t1,t2,tInput), interpLinear(y1,y2,t1,t2,tInput)
    return xout, yout

def interpData2Angles(r1, r2, t1, t2, tInput):
    if(t1 == t2 or r1 == r2):
        return r1
    shortestAngle = (r2 - r1 + 180) % 360 - 180
    amount = (tInput - t1) / (t2 - t1)
    return (r1 + (shortestAngle * amount)) % 360

def createProperData():
    for i in range(0, normalizedLength - 1):
        pos1, pos2 = searchPts(normalizedDataTimes[i])
        normalizedDataX[i], normalizedDataY[i] = interpData2Pts(data.xPos[pos1], data.xPos[pos2], data.yPos[pos1], data.yPos[pos2], data.t[pos1], data.t[pos2], normalizedDataTimes[i])
        normalizedDataAngle[i] = interpData2Angles(data.d[pos1], data.d[pos2], data.t[pos1], data.t[pos2], normalizedDataTimes[i])
    for j in range(0, normalizedLength - 1):
        pos1, pos2 = searchPts(normalizedDataTimes[j])
        normalizedDataRoutines[j] = data.a[pos2] if data.a[pos1] != data.a[pos2] else data.a[pos1]


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
    x = fieldDimensionsX - (normalizedDataY[i] + yOffset)
    y = normalizedDataX[i] + xOffset
    print(x)
    print(y)
    d = normalizedDataAngle[i]
    t = normalizedDataTimes[i]

    robot.set_width(kRobotWidth)
    robot.set_height(kRobotHeight)
    robot.set_xy([x - kRobotWidth/2, y - kRobotHeight/2])
    robot.set_transform(transforms.Affine2D().rotate_deg_around(x,y,d) + ax.transData)

    robotPointer.set_width(kRobotWidth / 2)
    robotPointer.set_height(kRobotHeight / 2)
    robotPointer.set_xy([x - kRobotWidth/4, y - kRobotHeight/2])
    robotPointer.set_transform(transforms.Affine2D().rotate_deg_around(x,y,d) + ax.transData)


    timePassed = round(t, 2)
    print(timePassed)
    fig = pylab.gcf()
    fig.canvas.set_window_title(normalizedDataRoutines[i])
    # runningRoutine.set_text(normalizedDataRoutines[i])
    print(normalizedDataRoutines[i])
    if timePassed > autoDuration:
        elapsedTime.set_color('red')
    else:
        elapsedTime.set_text('{:.2f}'.format(normalizedDataTimes[i]))
        frame_generator.append(frame_generator[-1] + 1)
    return   robot, robotPointer, elapsedTime

def init():
    ax.add_patch(robotPointer)
    ax.add_patch(robot)
    return robot, robotPointer,


if (sys.argv[1] == 'save'):
    anim = animation.FuncAnimation(fig, animate, init_func=init, frames=350,interval =  timeDif * 1000, blit=True, repeat=False)
    anim.save('resources/' + selectedAuto + 'Simulation.gif',writer=animation.PillowWriter(fps=20))
elif (sys.argv[1] == 'show'):
    anim = animation.FuncAnimation(fig, animate, init_func=init, frames=frame_generator,interval =  timeDif * 1000, blit=True, repeat=False)
    plt.tight_layout()
    plt.show()
else:
    print("Invalid Option")
