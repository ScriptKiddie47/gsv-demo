package com.scriptkiddie.gsvgradle.batchprocessing;

import org.json.JSONObject;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public ItemReader<JSONObject> reader(){
		return new JSONObjectReader();
	}
	
	@Bean
	public JSONObjectProcessor processor() { 
		return new JSONObjectProcessor();
	}
	
	@Bean
	public ItemWriter<JSONObject> writer(){
		return new JSONObjectWriter();
	}
	
	@Bean
	public Job importUser(JobCompletionNotificationListener listener,Step step1) {
		return jobBuilderFactory.get("importTableData")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1)
				.end()
				.build();
	}
	
	@Bean
	public Step step1(ItemWriter<JSONObject> itemWriter) {
		return stepBuilderFactory
				.get("step1")
				.<JSONObject,JSONObject> chunk(20)
				.reader(reader())
				.processor(processor())
				.writer(itemWriter)
				.build();
	}
}
