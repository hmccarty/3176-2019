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

from cscore import CameraServer, VideoSource, CvSource, VideoMode, CvSink, UsbCamera
from networktables import NetworkTablesInstance

#   JSON format:
#   {
#       "team": <team number>,
#       "ntmode": <"client" or "server", "client" if unspecified>
#       "cameras": [
#           {
#               "name": <camera name>
#               "path": <path, e.g. "/dev/video0">
#               "pixel format": <"MJPEG", "YUYV", etc>   // optional
#               "width": <video mode width>              // optional
#               "height": <video mode height>            // optional
#               "fps": <video mode fps>                  // optional
#               "brightness": <percentage brightness>    // optional
#               "white balance": <"auto", "hold", value> // optional
#               "exposure": <"auto", "hold", value>      // optional
#               "properties": [                          // optional
#                   {
#                       "name": <property name>
#                       "value": <property value>
#                   }
#               ]
#           }
#       ]
#   }

configFile = "/boot/frc.json"

class CameraConfig: pass

team = None
server = False
cameraConfigs = []

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

#This should be a class lowkey but it'll work
def TrackTheTarget(frame, sd):
    TargetLower = (40,103,103)
    TargetUpper = (110,255,255)
    #try:
     #   HL = sd.getNumber('HL', 0)
      #  HU = sd.getNumber('HU', 36)
       # SL = sd.getNumber('SL', 103)
        #SU = sd.getNumber('SU', 255)
        #VL = sd.getNumber('VL', 105)
        #VU = sd.getNumber('VU', 255)
        #TargetLower = (HL,SL,VL)
        #TargetUpper = (HU,SU,VU)
        #print("HSV lower:%s HSV Upper:%s" % (TargetLower, TargetUpper))
    #except:
     #   print("Unable to grab network table values, going to default values")

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
    blockIdx = 0

    for block in blocks:
        target = cv2.minAreaRect(block)
        box = cv2.boxPoints(target)
        box = np.int0(box)

        M = cv2.moments(block)
        xcent = int(M['m10']/M['m00'])
        ycent = int(M['m01']/M['m00'])

        #if the dectected contour has a radius big enough, we will send it
        if M['m00'] > 10:
            cv2.drawContours(img, [box], -1, (0, 0, 255), 3)
            sd.putNumber('Block ' + str(blockIdx) + ' Center X', xcent)
            sd.putNumber('Block ' + str(blockIdx) + ' Center Y', ycent)
        else:
            #let the RoboRio Know no target has been detected with -1
            sd.putNumber('Block ' + str(blockIdx) + ' Center X', -1)
            sd.putNumber('Block ' + str(blockIdx) + ' Center Y', -1)
        blockIdx = blockIdx + 1

    print("Sent processed frame")
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
    Camera.setResolution(160,120)
    cs.addCamera(Camera)

    print("connected")

    #This Is the object we pull the imgs for OpenCV magic
    CvSink = cs.getVideo()

    #This will send the process frames to the Driver station
    #allowing the us to see what OpenCV sees
    outputStream = cs.putVideo("Processed Frames", 160,120)

    #buffer to store img data
    img = np.zeros(shape=(160,120,3), dtype=np.uint8)
    # loop forever
    while True:
        #Quick little FYI, This will throw a Unicode Decode Error first time around
        #Something about a invalid start byte. This is fine, the Program will continue
        # and after a few loops and should start grabing frames from the camera
        GotFrame, img = CvSink.grabFrame(img)
        if GotFrame  == 0:
            outputStream.notifyError(CvSink.getError())
            continue
        img = TrackTheTarget(img, SmartDashboardValues)
        #print(img)
        outputStream.putFrame(img)
