[![Build Status](https://travis-ci.org/artkuznetsov101/capture.svg?branch=master)](https://travis-ci.org/artkuznetsov101/capture)
# capture (hidden windows to a file)

## Quick start
capture.jar is a Windows only java console app which allows you to take screenshots of entire desktop or a particular window and save it to a file. App provides functionality to capture separate window even if it is fully hidden or overlapped by other windows. 

It uses WinAPI over JNA and support regular expression syntax in window caption name option.

You can build this utility by yourself or download latest release from 'releases' section.

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

Captures all windows with captions started with "Skype":
     
    java -jar capture.jar -c "Skype.*"                       
 
Capture desktop with jpg file extension:

    java -jar capture.jar -c "Program Manager" -e "jpg"    
