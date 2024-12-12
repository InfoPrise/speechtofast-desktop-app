package com.speechtofast.speechtofast_desktop_app.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.speechtofast.speechtofast_desktop_app.dto.QueueItemDTO;
import com.speechtofast.speechtofast_desktop_app.service.WatchWithStabilityService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;

@Service
@FxmlView("mainView.fxml")
public class MainView extends Controller {
	
	@Autowired
	WatchWithStabilityService watchWithStabilityService;

    @FXML
    private ListView<String> directoryListView;

    @FXML
    private Button addDirectoryButton;

    @FXML
    private Button removeDirectoryButton;

    @FXML
    private Button saveDirectoriesButton;
    
    @FXML
    private ToggleButton startMonitor;
    
    @FXML
    private TableView<QueueItemDTO> tableView;

    @FXML
    private TableColumn<QueueItemDTO, String> colDiretorio;

    @FXML
    private TableColumn<QueueItemDTO, String> colNomeArquivo;

    @FXML
    private TableColumn<QueueItemDTO, String> colDataInsercao;

    @FXML
    private TableColumn<QueueItemDTO, String> colProgresso;

    @FXML
    private TableColumn<QueueItemDTO, String> colStatus;

    @FXML
    private TableColumn<QueueItemDTO, String> colDataFinalizacao;
    
    // Lista observável para armazenar os diretórios
    private final ObservableList<String> directoryList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Conectar a lista observável ao ListView
        directoryListView.setItems(directoryList);

        // Configurar o botão para adicionar diretórios
        addDirectoryButton.setOnAction(event -> openDirectoryChooser());

        // Configurar o botão para remover o diretório selecionado
        removeDirectoryButton.setOnAction(event -> removeSelectedDirectory());

        // Configurar o botão para salvar os diretórios em um arquivo
        saveDirectoriesButton.setOnAction(event -> saveDirectoriesToFile());
        
        startMonitor.setOnAction(event -> {
        for (String directory : directoryList) {
        	watchWithStabilityService.startMonitoringInBackground(Paths.get(directory));
		}});
        
            
        /*
        colDiretorio.setCellValueFactory(cellData -> cellData.getValue().getDiretorio());
        colNomeArquivo.setCellValueFactory(cellData -> cellData.getValue().getNomeArquivo());
        colDataInsercao.setCellValueFactory(cellData -> cellData.getValue().getDataInsercao());
        colProgresso.setCellValueFactory(cellData -> cellData.getValue().getProgresso()); 
        colStatus.setCellValueFactory(cellData -> cellData.getValue().getStatus());
        colDataFinalizacao.setCellValueFactory(cellData -> cellData.getValue().getDataFinalizacao());
        */
    }

    private void openDirectoryChooser() {
        // Criar o seletor de diretório
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecione um diretório");

        // Define um diretório inicial (opcional)
        File initialDirectory = new File(System.getProperty("user.home"));
        if (initialDirectory.exists()) {
            directoryChooser.setInitialDirectory(initialDirectory);
        }

        // Abrir o seletor de diretório
        Stage stage = (Stage) addDirectoryButton.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);

        // Adicionar o caminho à lista se o diretório for válido
        if (selectedDirectory != null && !directoryList.contains(selectedDirectory.getAbsolutePath())) {
            directoryList.add(selectedDirectory.getAbsolutePath());
        }
    }

    private void removeSelectedDirectory() {
        // Remover o diretório selecionado na ListView
        String selectedItem = directoryListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            directoryList.remove(selectedItem);
        }
    }

    private void saveDirectoriesToFile() {
        // Escolher o local para salvar os diretórios
        File saveFile = new File(System.getProperty("user.home"), "directories.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            for (String directory : directoryList) {
                writer.write(directory);
                writer.newLine();
            }
            System.out.println("Lista de diretórios salva em: " + saveFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erro ao salvar os diretórios: " + e.getMessage());
        }
    }
}