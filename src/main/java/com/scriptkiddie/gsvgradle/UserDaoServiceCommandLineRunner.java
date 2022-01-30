package com.scriptkiddie.gsvgradle;

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

	@Override
	public void run(String... args) throws Exception {
		processSpreadsheets.getSpreadsheetData();
		daoService.createTable(GsvConstants.tableData.get(0));
		handle();
		log.info("New User is Created");
	}
	
	public void handle() throws Exception{
        jobLauncher.run(job, new JobParameters());
    }

}
