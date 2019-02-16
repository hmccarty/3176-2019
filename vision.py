#!/usr/bin/env python3
#Wesley Karkiewicz--6060's Snake eyes
#
#
#
#This Frankenstein code is The demo Python script that came with
#the FRCVison image mashed with my Target tracking code. It Does
#Vision Processing exclusively on the Pi, should run faster
#then the Driver Station Version. Hopefully this script is just
#plug and play for you but if not you will need to edit tjhis script
#yourself to. work with your set up. I Have comments below on where
#you should edit if need be.
#
#
#
import json
import time
import sys
import numpy as np
import cv2
import math

from cscore import CameraServer, VideoSource, CvSource, VideoMode, CvSink, UsbCamera
from networktables import NetworkTablesInstance

#

configFile = "/boot/frc.json"

class CameraConfig: pass

team = None
server = False
cameraConfigs = []
yPixPerInch = 0
xPixPerInch = 0
distPixPerInch = 0

"""Report parse error."""
def parseError(str):
    print("config error in '" + configFile + "': " + str, file=sys.stderr)

"""Read single camera configuration."""
def readCameraConfig(config):
    cam = CameraConfig()

    # name
    try:
        cam.name = config["name"]
    except KeyError:
        parseError("could not read camera name")
        return False

    # path
    try:
        cam.path = config["path"]
    except KeyError:
        parseError("camera '{}': could not read path".format(cam.name))
        return False
		
    # stream properties
    cam.streamConfig = config.get("stream")

    cam.config = config

    cameraConfigs.append(cam)
    return True

"""Read configuration file."""
def readConfig():
    global team
    global server

    # parse file
    try:
        with open(configFile, "rt") as f:
            j = json.load(f)
    except OSError as err:
        print("could not open '{}': {}".format(configFile, err), file=sys.stderr)
        return False

    # top level must be an object
    if not isinstance(j, dict):
        parseError("must be JSON object")
        return False

    # team number
    try:
        team = j["team"]
    except KeyError:
        parseError("could not read team number")
        return False

    # ntmode (optional)
    if "ntmode" in j:
        str = j["ntmode"]
        if str.lower() == "client":
            server = False
        elif str.lower() == "server":
            server = True
        else:
            parseError("could not understand ntmode value '{}'".format(str))

    # cameras
    try:
        cameras = j["cameras"]
    except KeyError:
        parseError("could not read cameras")
        return False
    for camera in cameras:
        if not readCameraConfig(camera):
            return False

    return True

#image size ratioed to 16:9
kImageWidth = 160
kImageHeight = 120

#Lifecam 3000 from datasheet
#Datasheet: https://dl2jx7zfbtwvr.cloudfront.net/specsheets/WEBC1010.pdf
diagonalView = math.radians(68.5)

#16:9 aspect ratio
horizontalAspect = 4
verticalAspect = 3

#Reasons for using diagonal aspect is to calculate horizontal field of view.
diagonalAspect = math.hypot(horizontalAspect, verticalAspect)
#Calculations: http://vrguy.blogspot.com/2013/04/converting-diagonal-field-of-view-and.html
horizontalView = math.atan(math.tan(diagonalView/2) * (horizontalAspect / diagonalAspect)) * 2
verticalView = math.atan(math.tan(diagonalView/2) * (verticalAspect / diagonalAspect)) * 2

#Focal Length calculations: https://docs.google.com/presentation/d/1ediRsI-oR3-kwawFJZ34_ZTlQS2SDBLjZasjzZ-eXbQ/pub?start=false&loop=false&slide=id.g12c083cffa_0_165
H_FOCAL_LENGTH = kImageWidth / (2*math.tan((horizontalView/2)))
V_FOCAL_LENGTH = kImageHeight / (2*math.tan((verticalView/2)))

def translateRotation(rotation, width, height):
    if (width < height):
        rotation = -1 * (rotation - 90)
    if (rotation > 90):
        rotation = -1 * (rotation - 180)
    rotation *= -1
    return round(rotation)

def getEllipseRotation(image, cnt):
    try:
        # Gets rotated bounding ellipse of contour
        ellipse = cv2.fitEllipse(cnt)
        centerE = ellipse[0]
        # Gets rotation of ellipse; same as rotation of contour
        rotation = ellipse[2]
        # Gets width and height of rotated ellipse
        widthE = ellipse[1][0]
        heightE = ellipse[1][1]
        # Maps rotation to (-90 to 90). Makes it easier to tell direction of slant
        rotation = translateRotation(rotation, widthE, heightE)

        cv2.ellipse(image, ellipse, (23, 184, 80), 3)
        return rotation
    except:
        # Gets rotated bounding rectangle of contour
        rect = cv2.minAreaRect(cnt)
        # Creates box around that rectangle
        box = cv2.boxPoints(rect)
        # Not exactly sure
        box = np.int0(box)
        # Gets center of rotated rectangle
        center = rect[0]
        # Gets rotation of rectangle; same as rotation of contour
        rotation = rect[2]
        # Gets width and height of rotated rectangle
        width = rect[1][0]
        height = rect[1][1]
        # Maps rotation to (-90 to 90). Makes it easier to tell direction of slant
        rotation = translateRotation(rotation, width, height)
        return rotation

def calculateYaw(pixelX, centerX, hFocalLength):
    yaw = math.degrees(math.atan((pixelX - centerX) / hFocalLength))
    return round(yaw)

def calculatePitch(pixelY, centerY, vFocalLength):
    pitch = math.degrees(math.atan((pixelY - centerY) / vFocalLength))
    # Just stopped working have to do this:
    pitch *= -1
    return round(pitch)

def calculateDistance(kTargetWidth, kFocalLength, targetPixelWidth):
    return (kTargetWidth*174)/targetPixelWidth

def calculateAngle(kTargetWidth, distanceFromCenter, lengthOfTarget):
    return math.atan((kTargetWidth * distanceFromCenter) / lengthOfTarget)
	
def calculateX(kTargetWidth, distanceFromCenter, lengthOfTarget):
    return (kTargetWidth * distanceFromCenter) / lengthOfTarget

def determineBlockType(target):
    corners = [[-1,-1],[-1,-1]]
    pidx = 0
    for points in target:
        cidx = 0
        if pidx != 0 and pidx != 1:
            for coords in points:
                corners[pidx-2][cidx] = coords
                cidx += 1
        pidx += 1
    if corners[0][1] - corners[1][1] < 0:
        return 'right'
    else:
        return 'left'


#This should be a class lowkey but it'll work
def TrackTheTarget(frame, sd):
    TargetLower = (10,25,70)
    TargetUpper = (120,255,255)

    #Tells Smartdashbord if rpi is receiving frames
    if frame is None:
        sd.putNumber('GettingFrameData',False)
    else:
        sd.putNumber('GettingFrameData',True)

    #Convert frame from RGB to HSV Format
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

    #Creates a mask for all values within the HSV Filter
    mask = cv2.inRange(hsv, TargetLower, TargetUpper)

    #Erodes and dilates the mask to highlight objects
    mask = cv2.erode(mask, None, iterations = 2)
    mask = cv2.dilate(mask, None, iterations = 2)

    #find the Contours in the mask and initialize the
    #current (x,y) center of the Target
    a, blocks , b = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE)
    center = None
    areas = []
    if len(blocks) > 0:
        blocks = sorted(blocks, key = cv2.contourArea, reverse = True)[:3] # get largest five contour area
        target = cv2.minAreaRect(blocks[0])
        box = cv2.boxPoints(target)
        box = np.int0(box)
        M = cv2.moments(blocks[0])

        xcent = int(M['m10']/M['m00'])
        ycent = int(M['m01']/M['m00'])
        width = min(target[1][0], target[1][1])
        length = max(target[1][0], target[1][1])
        distance = calculateDistance(2, H_FOCAL_LENGTH, width)
        angle = calculateAngle(2.0, (80-xcent), length)
        x = calculateX(2.0, (80-xcent), length)
        print(x)
        #print("Width: " + str(target[1][0]) + " Height: " + str(target[1][1]))
        #print("Distance: " + str(distance)) 
        try:
            otherTarget = blocks[1]
            for block in blocks:
                otherPointArray = cv2.moments(block)
                otherXCent = int(otherPointArray['m10']/otherPointArray['m00'])
                #if determineBlockType(box) == 'left' and xcent > otherXCent:
                    
                #elif determineBlockType(box) == 'right' and xcent < otherXCent:
        except:
            print('Only one block detected') 
        rotation = getEllipseRotation(frame, blocks[0])

        if determineBlockType(box) == 'left':
            sd.putNumber('Center X', xcent+10)
        else:
            sd.putNumber('Center X', xcent-10)

        cv2.drawContours(img, [box], -1, (125, 0, 125), 3)
        sd.putNumber('Block Area', M['m00'])
        sd.putNumber('distance', distance) 
        sd.putNumber('Block Center X', xcent)
        sd.putNumber('Block Center Y', ycent)
        sd.putNumber('Angle', angle) 
        #sd.putNum

    return frame


if __name__ == "__main__":
    if len(sys.argv) >= 2:
        configFile = sys.argv[1]

    # read configuration
    if not readConfig():
        sys.exit(1)

    # start NetworkTables to send to smartDashboard
    ntinst = NetworkTablesInstance.getDefault()

    print("Setting up NetworkTables client for team {}".format(team))
    ntinst.startClientTeam(team)

    SmartDashboardValues = ntinst.getTable('SmartDashboard')

    #Start up camera stuff
    print("Connecting to camera")
    cs = CameraServer.getInstance()
    cs.enableLogging()
    Camera = UsbCamera('RPi Camero 0', 0)
    Camera2 = UsbCamera('RPi Camero 1', 1)
    Camera.setResolution(160,120)
    Camera2.setResolution(160,120)
    cs.addCamera(Camera)
    cs.addCamera(Camera2)

    print("connected")

    #This Is the object we pull the imgs for OpenCV magic
    CvSink = cs.getVideo()

    #This will send the process frames to the Driver station
    #allowing the us to see what OpenCV sees
    outputStream = cs.putVideo("Processed Frames", 160,120)

    #buffer to store img data
    img = np.zeros(shape=(640,480,3), dtype=np.uint8)
    # loop forever
    while True:
        start = time.time()
        #Quick little FYI, This ll throw a Unicode Decode Error first time around
        #Something about a invalid start byte. This is fine, the Program will continue
        # and after a few loops and should start grabing frames from the camera
        GotFrame, img = CvSink.grabFrame(img)
        if GotFrame  == 0:
            outputStream.notifyError(CvSink.getError())
            continue
        img = TrackTheTarget(img, SmartDashboardValues)
        end = time.time()
        #print(img)
        outputStream.putFrame(img)

        #print(end-start)
