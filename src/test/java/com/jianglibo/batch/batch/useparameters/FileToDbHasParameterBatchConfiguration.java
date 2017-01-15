package com.jianglibo.batch.batch.useparameters;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jianglibo.batch.batch.BatchCfgBase;
import com.jianglibo.batch.batch.Person;
import com.jianglibo.batch.batch.PersonItemProcessor;

@Configuration
public class FileToDbHasParameterBatchConfiguration extends BatchCfgBase {

	public static final String JOB_NAME = "fileToDbHasParameterJob";
	
	public FileToDbHasParameterBatchConfiguration() {
		super(JOB_NAME);
	}
    
    @Bean
    public FlatFileItemReader<Person> fileToDbHasParameterJobItemReader() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
        reader.setResource(getFixtureResoruce());
        reader.setLineMapper(new DefaultLineMapper<Person>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "firstName", "lastName" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                setTargetType(Person.class);
            }});
        }});
        return reader;
    }
    
    @Bean
    public JdbcBatchItemWriter<Person> fileToDbHasParameterJobItemWriter() {
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
        writer.setSql("INSERT INTO people1 (first_name, last_name) VALUES (:firstName, :lastName)");
        writer.setDataSource(dataSource);
        return writer;
    }
    
    @Bean
    public PersonItemProcessor fileToDbHasParameterJobProcessor() {
        return new PersonItemProcessor();
    }
    
    @Bean
    public Job fileToDbHasParameterJobJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(fileToDbHasParameterJobStep1())
                .end()
                .build();
    }

    @Bean
    public Step fileToDbHasParameterJobStep1() {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10)
                .reader(fileToDbHasParameterJobItemReader())
                .processor(fileToDbHasParameterJobProcessor())
                .writer(fileToDbHasParameterJobItemWriter())
                .allowStartIfComplete(true)
                .build();
    }
}
