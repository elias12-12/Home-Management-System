Smart Home Management System (Java)
Overview

The Smart Home Management System is a console-based Java application that simulates a real smart home environment with full administrative and user control.

The system supports:

Admin and User authentication

Room management

Device management (Lights and Appliances)

Power consumption control with multiple power modes

Day/Night operating modes

Device waiting lists (Power and Time restrictions)

Adjustable lights and multi-level appliances

This project demonstrates strong application of Object-Oriented Programming (OOP) principles and system design.

Authentication System

At startup, the system requires:

Admin password

User password

Passwords must:

Be at least 8 characters

Contain at least one uppercase letter

Contain at least one lowercase letter

Contain at least one digit

The system runs in two modes:

Admin Mode

User Mode

Admin Mode Features

Admin has full control over the system.

Password Management

Change admin password

Change user password

Power Mode Control

Admin can switch between:

LOW Power Mode

NORMAL Power Mode

HIGH Power Mode

Each mode defines the maximum allowed power consumption.

Time Mode Control

Set Day Mode

Set Night Mode

Some devices behave differently depending on the selected time mode.

Room Management

Add Room (Code and Description)

Remove Room

Search Room

Display All Rooms

Device Management

Add Device to Room

Remove Device

Search Device

Display All Devices

Supported device types:

Light

Appliance

User Mode Features

Users can monitor and control the system within defined constraints.

View all rooms

View all devices

View running devices

View Day Waiting List

View Power Waiting List

Search room

Search device

Turn device ON or OFF

Turn off all devices in a room

Turn off all devices

Check current total power consumption

Change Day/Night mode

Device Types
Light

A Light can be:

Adjustable (intensity from 0 to 100 percent)

Non-adjustable

If adjustable, the user must provide intensity when turning it on.

Appliance

An Appliance includes:

Multiple power levels (percentage-based)

Optional noisy property

Critical or non-critical flag

When turning on an appliance, the user selects a power level index.

Power Management System

The system continuously tracks:

Current total power consumption

Maximum allowed power (based on selected power mode)

If turning on a device exceeds the allowed power:

The device is placed in the Power Waiting List

It will not start until sufficient power becomes available

Day/Night Restriction

If a device cannot run due to time restrictions:

It is placed in the Day Waiting List

OOP Concepts Applied

This project demonstrates:

Encapsulation (private attributes with getters and setters)

Inheritance (Light and Appliance extend Device)

Polymorphism (Device references used for different device types)

Abstraction

Composition (Rooms contain Devices)

Menu-driven system architecture

Project Structure
SmartHome/
│
├── Main.java
├── ManagementSystem.java
├── Room.java
├── Device.java
├── Light.java
└── Appliance.java
How to Run

Open the project in IntelliJ IDEA, Eclipse, or VS Code.

Compile and run:

Main.java

Follow the console instructions.

The system automatically adds sample data for testing purposes, including multiple rooms and devices.

Future Improvements

Possible enhancements include:

Graphical User Interface (GUI)

Database integration

Real-time device monitoring

Scheduling system

Mobile application integration

Logging system
