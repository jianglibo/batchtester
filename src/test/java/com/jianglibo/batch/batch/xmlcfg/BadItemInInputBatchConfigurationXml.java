package com.jianglibo.batch.batch.xmlcfg;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.jianglibo.batch.batch.BatchCfgBase;
import com.jianglibo.batch.batch.Person;

@Configuration
@ImportResource({"classpath:/com/jianglibo/batch/batch/xmlcfg/register.xml"})
public class BadItemInInputBatchConfigurationXml extends BatchCfgBase {
	
	public static String JOB_NAME = "ioSampleJob"; // must match name in xml file.

	public BadItemInInputBatchConfigurationXml() {
		super(JOB_NAME);
	}
	
	@Bean(name="ioSampleJobItemReaderLineMapper")
	public LineMapper<Person> lineMapper() {
		return new DefaultLineMapper<Person>() {{
	        setLineTokenizer(new DelimitedLineTokenizer() {{
	            setNames(new String[] { "firstName", "lastName" });
	        }});
	        setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
	            setTargetType(Person.class);
	        }});
	    }};
	}
	
	
    @Bean(name="ioSampleJobItemWriter")
    public JdbcBatchItemWriter<Person> itemWriter() {
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
        writer.setSql("INSERT INTO people1 (first_name, last_name) VALUES (:firstName, :lastName)");
        writer.setDataSource(dataSource);
        return writer;
    }

}
