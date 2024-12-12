package com.speechtofast.speechtofast_desktop_app.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WatchWithStabilityService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
    private static final long STABLE_DURATION_MS = 5000; // Tempo que considera o arquivo estável (5 segundos)
    private static final long CHECK_INTERVAL_MS = 1000; // Intervalo para verificar estabilidade (1 segundo)

    private final WatchService watchService;
    private final Map<Path, Long> fileLastModifiedTimes;
    private volatile boolean running = true; // Controle de execução do monitoramento

    public WatchWithStabilityService() throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.fileLastModifiedTimes = new HashMap<>();
    }

    public void registerDirectory(Path dir) throws IOException {
        dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
        logger.info("Monitorando diretório: {}", dir);
    }

    public void registerDirectoryRecursively(Path startDir) throws IOException {
        Files.walk(startDir)
             .filter(Files::isDirectory)
             .forEach(dir -> {
                 try {
                     registerDirectory(dir);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             });
        logger.info("Registro recursivo completo para: {}", startDir);
    }

    private boolean isFileStable(Path file) throws IOException {
        long currentModifiedTime = Files.getLastModifiedTime(file).toMillis();
        long currentTime = System.currentTimeMillis();

        long lastModifiedTime = fileLastModifiedTimes.getOrDefault(file, -1L);
        fileLastModifiedTimes.put(file, currentModifiedTime);

        return lastModifiedTime == currentModifiedTime && currentTime - lastModifiedTime >= STABLE_DURATION_MS;
    }

    public void processEvents() {
    	logger.info("Aguardando eventos...");
        while (running) { // Verifica se o monitoramento deve continuar
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                @SuppressWarnings("unchecked")
                Path fileName = (Path) event.context();
                Path dir = (Path) key.watchable();
                Path filePath = dir.resolve(fileName);
                
                logger.info("Evento detectado: {} em {}", kind, filePath);

                if (Files.isDirectory(filePath) && kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    try {
                        System.out.println("Novo diretório detectado, registrando: " + filePath);
                        registerDirectory(filePath); // Registrar o novo diretório
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    new Thread(() -> {
                        try {
                            if (Files.isDirectory(filePath)) {
                                logger.info("Evento detectado para diretório: {}", filePath);
                            } else {
                                monitorStability(filePath);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }

    private void monitorStability(Path filePath) throws IOException {
        while (running) { // Permite parar o monitoramento
            try {
                Thread.sleep(CHECK_INTERVAL_MS);
                if (isFileStable(filePath)) {
                    logger.info("O arquivo está estável: {}", filePath);
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void startMonitoringInBackground(Path dirToMonitor) {
        new Thread(() -> {
            try {
                registerDirectoryRecursively(dirToMonitor);
                processEvents();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "WatchService-Thread").start();
    }

    public void stopMonitoring() {
        running = false;
        try {
            watchService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
