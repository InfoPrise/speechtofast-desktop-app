package com.speechtofast.speechtofast_desktop_app.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

public class RecursiveFileWatcherService {
	private WatchService watchService;
	private Map<WatchKey, Path> keys;

	public RecursiveFileWatcherService(Path startDir) throws IOException {
	        this.watchService = FileSystems.getDefault().newWatchService();
	        this.keys = new HashMap<>();

	        // Registra todos os subdiretórios
	        registerAll(startDir);
	    }

	private void registerAll(final Path start) throws IOException {
		Files.walk(start).filter(Files::isDirectory).forEach(this::register);
	}

	private void register(Path dir) {
		try {
			WatchKey key = dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
			keys.put(key, dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void processEvents() {
		while (true) {
			WatchKey key;
			try {
				key = watchService.take();
			} catch (InterruptedException e) {
				return;
			}

			Path dir = keys.get(key);
			if (dir == null) {
				System.err.println("Chave não reconhecida!");
				continue;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();

				@SuppressWarnings("unchecked")
				Path name = (Path) event.context();
				Path child = dir.resolve(name);

				System.out.println("Evento detectado: " + kind + " em " + child);

				// Registra novos diretórios dinamicamente
				if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
					try {
						if (Files.isDirectory(child)) {
							registerAll(child);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			boolean valid = key.reset();
			if (!valid) {
				keys.remove(key);
				if (keys.isEmpty()) {
					break;
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			Path dir = Paths.get("/home/luisangelorjr/.larjr/tech/dev/src/playground/java/poc-recursive-file-watcher/");
			RecursiveFileWatcherService watcher = new RecursiveFileWatcherService(dir);
			watcher.processEvents();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}