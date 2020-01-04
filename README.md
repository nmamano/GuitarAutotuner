# GuitarAutotuner
Group project for embedded systems class at UCI.

We made a device to tune a guitar automatically.
The device consists of an arduino connected to a step motor, which is in turn attached to a peg winder.
To use it, the peg winder is held on the guitar peg, and a human plucks the string.
An Android app picks up the frequency with the phone's microphone and calculates the rotation needed to tune the string based a kind of binary search. The app then communicates the needed action to the arduino via bluetooth, which then rotates the peg winder.

![Image of device](device.jpg "The device")
