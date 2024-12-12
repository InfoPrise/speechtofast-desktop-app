package com.speechtofast.speechtofast_desktop_app;


import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.speechtofast.speechtofast_desktop_app.controller.MainView;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;

public class GuiApp extends Application {
	
	private ConfigurableApplicationContext applicationContext;

	@Override
    public void start(Stage primaryStage) {
		
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(MainView.class);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
	//TODO: Verificar se aqui quebra ao passar argumentos ao cli
	@Override
	public void init() {
		String[] args = getParameters().getRaw().toArray(new String[0]);
		this.applicationContext = new SpringApplicationBuilder()
				.sources(SpeechtofastDesktopAppApplication.class)
				.run(args);
	}
	
	@Override
	public void stop() {
		this.applicationContext.close();
		Platform.exit();
	}

    public static void man(String[] args) {
        launch(args);
    }

}
