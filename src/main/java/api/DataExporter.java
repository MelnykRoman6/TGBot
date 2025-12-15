// src/main/java/api/DataExporter.java
//not needed for now
package api;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataExporter {

    private final WitcherApiClient apiClient;

    public DataExporter(WitcherApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Recupera tutti i dati delle armi dall'API e li salva in un file JSON locale.
     * (Получает все данные об оружии из API и сохраняет их в локальный JSON-файл.)
     * * @param filename Il nome del file da creare nel progetto. (Имя файла для создания в проекте.)
     * @return Il percorso assoluto del file creato. (Абсолютный путь к созданному файлу.)
     * @throws IOException Se fallisce la connessione o il salvataggio.
     */
    public String exportAllWeapons(String filename) throws IOException {

        // 1. Получаем все данные в виде одной JSON-строки
        System.out.println("DEBUG: Tentativo di recuperare tutti i dati delle armi dall'API...");
        String allWeaponsJson = apiClient.getAllWeaponsJson();

        // 2. Определяем путь к файлу (в папке проекта)
        Path filePath = Paths.get(filename);

        // 3. Сохраняем строку в файл
        try (FileWriter file = new FileWriter(filePath.toFile())) {
            file.write(allWeaponsJson);
            file.flush(); // Garantisce che i dati siano scritti su disco
        }

        System.out.println("DEBUG: Dati scritti con successo nel file: " + filePath.toAbsolutePath());
        return filePath.toAbsolutePath().toString();
    }
}