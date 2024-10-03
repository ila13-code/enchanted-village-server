package unical.demacs.enchantedvillage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Paths;
import java.io.File;

@SpringBootApplication
public class EnchantedVillageServerApplication {

	private static void createFolder(String path) {
		File logFolder = new File(path);
		if (!logFolder.exists()) {
			if (logFolder.mkdirs()) {
				System.out.println("Cartella di log in: " + path + " creata con successo.");
			} else {
				System.err.println("Impossibile creare la cartella di log in: " + path + ".");
			}
		}
	}

	public static void main(String[] args) {
		String baseDir;
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			baseDir = "C:\\EnchantedVillageLogs";
		} else {
			baseDir = "/EnchantedVillageLogs";
		}

		String backendDir = Paths.get(baseDir, "Backend").toString();

		createFolder(baseDir);
		createFolder(backendDir);
		SpringApplication.run(EnchantedVillageServerApplication.class, args);
	}

}
