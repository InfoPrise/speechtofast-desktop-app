package com.speechtofast.speechtofast_desktop_app.controller;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.speechtofast.speechtofast_desktop_app.SpeechtofastDesktopAppApplication;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;

public class Controller {

	private ConfigurableApplicationContext contextoSpring;

	private Node ancora;

	@FXML
	public void initialize() {
		String[] args = new String[0];

		this.contextoSpring = new SpringApplicationBuilder().sources(SpeechtofastDesktopAppApplication.class).run(args);

	}

	public void carregarScene(Node parent, Class controller) {

		// Utiliza o parente para acessar a Stage
		Stage stage = (Stage) parent.getScene().getWindow();

		// Pegar o Bean do JavaFXWeaver
		FxWeaver fxWeaver = contextoSpring.getBean(FxWeaver.class);

		// O FXWeaver carrega o controlador na memória
		Parent root = (Parent) fxWeaver.loadView(controller);

		// Associa o controlador a uma cena
		Scene scene = new Scene(root);

		// Associa a cena à tela atual
		stage.setScene(scene);

		stage.show();

	}

	public ConfigurableApplicationContext getContextoSpring() {
		return contextoSpring;
	}

	public Node getAncora() {
		return ancora;
	}

	public void setAncora(Node ancora) {
		this.ancora = ancora;
	}

}