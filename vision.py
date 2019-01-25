#!/usr/bin/env python3
#Wesley Karkiewicz--6060's Snake eyes
#
#
#
#This Frankenstein code is The demo Python script that came with
#the FRCVison image mashed with my ball tracking code. It Does
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
def TrackTheBall(frame, sd):
    BallLower= (0,103,105)
    BallUpper = (150,255,255)
    #if no frame arrives, the vid is over or camera is unavalible
    if frame is None:
        sd.putNumber('GettingFrameData',False)
    else:
        sd.putNumber('GettingFrameData',True)


    #frame = cv2.flip(frame, 1)

    #Blur out the Image
    #blurred = cv2.GaussianBlur(frame, (11,11), 0)
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    
    #Make a mask for the pixals that meet yhe HSV filter 
    #then run a bunch of dolations and
    #erosions to remove any small blobs still in the mask
    mask = cv2.inRange(hsv, BallLower, BallUpper)
    mask = cv2.erode(mask, None, iterations = 2)
    mask= cv2.dilate(mask, None, iterations = 2)
    
    #find the Contours in the mask and initialize the
    #current (x,y) center of the ball
    a, cnts , b= cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE)
    center = None
    #only do stuff if a single contor was found
    for c in cnts:
        #find the largest contour in the mask, then use it
        #to compute the minimum enclosing circle and centroid
        #c = max(cnts, key=cv2.contourArea)
        rect=cv2.minAreaRect(c)
        box = cv2.boxPoints(rect)
        box = np.int0(box)
        print(box)
        ((x,y), radius) = cv2.minEnclosingCircle(c)
        M = cv2.moments(c)
        center = (int(M["m10"] / M["m00"]), int (M["m01"] / M["m00"]))

        #if the dectected contour has a radius big enough, we will send it
        if radius > 10:
            cv2.drawContours(img, [box], -1, (0, 0, 255), 3)
            #draw a circle around the target and publish values to smart dashboard
            #cv2.circle(frame, (int(x), int(y)), int(radius), (255,255,8), 2)
            #cv2.circle(frame, center, 3, (0,0,225), -1)
            sd.putNumber('All', box)
            sd.putNumber('X',x)
            sd.putNumber('Y',y)
            sd.putNumber('R', radius)
        else:
            #let the RoboRio Know no target has been detected with -1
            sd.putNumber('X', -x)
            sd.putNumber('Y', -y)
            sd.putNumber('R', -radius)
            
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
        img = TrackTheBall(img, SmartDashboardValues)
        #print(img)
        outputStream.putFrame(img)


