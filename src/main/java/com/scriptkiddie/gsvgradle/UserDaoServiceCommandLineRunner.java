package com.scriptkiddie.gsvgradle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.scriptkiddie.gsvgradle.service.ProcessSpreadsheets;
import com.scriptkiddie.gsvgradle.service.UserDAOService;
import com.scriptkiddie.gsvgradle.util.GsvConstants;

@Component
public class UserDaoServiceCommandLineRunner implements CommandLineRunner {

	@Autowired
	private UserDAOService daoService;
	@Autowired
	private ProcessSpreadsheets processSpreadsheets;

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;

	private static final Logger log = LoggerFactory.getLogger(UserDaoServiceCommandLineRunner.class);
	private static final String COMMA_DELIMITTER = ",";

	@Override
	public void run(String... args) throws Exception {

		readFileSourceCSV();
		log.info("GsvConstants records" + GsvConstants.records);
		for (List<String> list : GsvConstants.records) {
			fetchFilePaths(list.get(0));
			if (!GsvConstants.filePaths.isEmpty()) {
				for (String file : GsvConstants.filePaths) {
					log.info("Reading from file:" + file);
					if (file.contains(".xlsx")) {
						GsvConstants.TABLENAME = null;
						processSpreadsheets.getSpreadsheetData(file, list.get(1));
						daoService.createTable(GsvConstants.tableData.get(0),file);
						handle();
						log.info("New Job User is Created");
					}
				}
			}
		}
	}

	private void readFileSourceCSV() {
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/fileSource.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(COMMA_DELIMITTER);
				GsvConstants.records.add(Arrays.asList(values));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handle() throws Exception {
		jobLauncher.run(job, new JobParameters());
	}

	private void fetchFilePaths(String filePathLoc) {
		log.info("File Path Location" + filePathLoc);
		try (Stream<Path> filePathStream = Files.walk(Paths.get(filePathLoc))) {
			filePathStream.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					GsvConstants.filePaths.add(filePath.toString());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
