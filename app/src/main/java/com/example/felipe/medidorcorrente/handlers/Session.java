package com.example.felipe.medidorcorrente.handlers;

import com.example.felipe.medidorcorrente.model.Device;

import java.util.ArrayList;

/**
 * Created by desenvolvedor2 on 14/09/16.
 */
public class Session {

    public static boolean scanRunning;

    public ArrayList<Device> devices;

    public static String ipAddress;

    public static String getIpAddress() {
        return ipAddress;
    }

    public static void setIpAddress(String ipAddress) {
        Session.ipAddress = ipAddress;
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    public static boolean isScanRunning() {
        return scanRunning;
    }

    public static void setScanRunning(boolean scanRunning) {
        Session.scanRunning = scanRunning;
    }
}
