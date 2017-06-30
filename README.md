[![Build Status](https://travis-ci.org/artkuznetsov101/capture.svg?branch=master)](https://travis-ci.org/artkuznetsov101/capture)
# capture (hidden windows to a file)

## Quick start
capture.jar is a Windows only console utility which allows you to take screenshots of entire screen, or a particular window, and save it to a file. Utility provides functions to capture separate window even if it is fully hidden (overlapped) by other windows. 

It uses WinAPI over JNA and support regular expression syntax in window caption name option.

You can build this utility by yourself or download latest release from 'Release' section.

## Compiling

If you want to build all by youself you will need to install a JDK and maven. Then run the following command: 

    mvn package

## Usage

All options:

    *-c,   --caption         Window caption name(s), regex expression (matched = captured). Required parameter
     -p,   --path            Path for screenshots. Default: .
     -e,   --extension       Screenshot file extension. Default: png
     -w,   --wait            Seconds to wait before taking a screenshot. Default: 5
     -h,   --help            Show this help message.

Examples:

Captures all windows in current directory with captions started with "Skype":
     
    java -jar capture.jar -c "Skype.*"                       
 
Capture desktop in current directory with jpg file extension:

    java -jar capture.jar -c "Program Manager" -e "jpg"    
