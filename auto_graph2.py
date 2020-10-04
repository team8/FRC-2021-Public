import matplotlib.pyplot as plt
import matplotlib
from matplotlib import animation
import matplotlib.image as mpimg

import math
import pandas as pd
import numpy as np
import matplotlib.transforms as mtransforms


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
print(data.t[2] - data.t[1])

def frame_generator():
    prevTime = 0
    for frame in range(len(data.t)):
        # Yield the frame first
        yield frame
        for _ in range(data.t[frame] - prevTime):
            yield None
        prevTime = data.t[frame]

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

anim = animation.FuncAnimation(fig, animate, init_func=init, frames=frame_generator(), interval= 5, blit=True)
plt.show()
import matplotlib.pyplot as plt
import matplotlib
from matplotlib import animation
import matplotlib.image as mpimg

import math
import pandas as pd
import numpy as np
import matplotlib.transforms as mtransforms


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
print(data.t[2] - data.t[1])

def frame_generator():
    prevTime = 0
    for frame in range(len(data.t)):
        # Yield the frame first
        yield frame
        for _ in range(data.t[frame] - prevTime):
            yield None
        prevTime = data.t[frame]

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

anim = animation.FuncAnimation(fig, animate, init_func=init, frames=frame_generator(), interval= 5, blit=True)
plt.show()
