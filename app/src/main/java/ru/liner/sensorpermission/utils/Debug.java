package ru.liner.sensorpermission.utils;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 29.08.2023, 9:21
 */
public class Debug {
    public static void log(String message, Object... objects){
        System.out.printf("[DEBUG]: %s%n", String.format(message, objects));
    }
}
