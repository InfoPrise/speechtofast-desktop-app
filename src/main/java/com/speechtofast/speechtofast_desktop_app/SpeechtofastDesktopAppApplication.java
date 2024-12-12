package com.speechtofast.speechtofast_desktop_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpeechtofastDesktopAppApplication {

	public static void main(String[] args) {
        if (args.length > 0 && "shell".equalsIgnoreCase(args[0])) {
            // Remove "shell" do array de argumentos e passe os restantes para o Spring Boot
            String[] newArgs = new String[args.length - 1];
            System.arraycopy(args, 1, newArgs, 0, newArgs.length);      
            SpringApplication.run(SpeechtofastDesktopAppApplication.class, newArgs);
        } else {
            GuiApp.man(args);
        }
	}
}
