package com.speechtofast.speechtofast_desktop_app.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class AppCommands {

    @ShellMethod("Say hello to the user.")
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }

    @ShellMethod("Print current status.")
    public String status() {
        return "Application is running.";
    }
}