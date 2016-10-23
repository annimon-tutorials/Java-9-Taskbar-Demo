package com.example.taskbar;

import java.awt.Taskbar;
import java.util.Arrays;

public final class Main {

    public static void main(String[] args) {
        if (!Taskbar.isTaskbarSupported()) {
            System.out.println("Taskbar is not supported");
            return;
        }

        final Taskbar taskbar = Taskbar.getTaskbar();
        showSupportedTaskbarFeatures(taskbar);

        new TaskbarConfig(taskbar).setVisible(true);
    }

    private static void showSupportedTaskbarFeatures(Taskbar taskbar) {
        System.out.println("Supported taskbar features:");
        Arrays.stream(Taskbar.Feature.values())
                .filter(taskbar::isSupported)
                .forEach(System.out::println);
    }
}
