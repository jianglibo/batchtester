package com.jianglibo.batch.batch.integration;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import com.jianglibo.batch.batch.TbatchBase;

@Configuration
public class Fint {
	
	public static String IN_FOLDER = Paths.get(TbatchBase.BATCH_FIXTURE_BASE).resolve("fint").normalize().toAbsolutePath().toString();
	
	public class CountingChannelInterceptor extends ChannelInterceptorAdapter {

	    private final AtomicInteger sendCount = new AtomicInteger();

	    @Override
	    public Message<?> preSend(Message<?> message, MessageChannel channel) {
	        sendCount.incrementAndGet();
	        return message;
	    }

		public AtomicInteger getSendCount() {
			return sendCount;
		}
	}
	
	
	public class Mh implements MessageHandler {
		@Override
		public void handleMessage(Message<?> message) throws MessagingException {
			System.out.println(message.getPayload());
		}
	}
	
	@Bean
	public CountingChannelInterceptor cci() {
		return new CountingChannelInterceptor();
	}
	
    @Bean
    public MessageChannel fileInputChannel() {
    	DirectChannel mc = new DirectChannel();
    	mc.addInterceptor(cci());
    	return mc;
    }
    
	
    @Bean
    @InboundChannelAdapter(value = "fileInputChannel", poller=@Poller(fixedDelay="500")) //send to "fileInputChannel"
    public MessageSource<File> fileReadingMessageSource() {
		CompositeFileListFilter<File> filters = new CompositeFileListFilter<>();
    	filters.addFilter(new SimplePatternFileListFilter("*.txt"));
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File(IN_FOLDER));
        source.setUseWatchService(true);
        source.setFilter(filters);
        return source;
    }
    
	
    @Bean
    public IntegrationFlow fileReadingFlow() {
         return IntegrationFlows
        		 	.from(fileInputChannel())
                  .transform(Transformers.fileToString())
                  .handle(new Mh())
                  .get();
        }

}
