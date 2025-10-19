package com.kafka.springboot;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.launchdarkly.eventsource.ConnectStrategy;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import com.launchdarkly.eventsource.background.BackgroundEventSource;

import okhttp3.OkHttpClient;
import okhttp3.Request;

@Service
public class WikimediaChangesProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikimediaChangesProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public WikimediaChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage() throws InterruptedException {
        String topic = "wikimedia_recentchange";
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        BackgroundEventHandler eventHandler = new WikimediaChangesHandler(kafkaTemplate, topic);

        // Define a custom HTTP client setup using a lambda
        ConnectStrategy connectStrategy = ConnectStrategy.http(URI.create(url))
                .clientBuilderActions(builder -> {
                    builder.addInterceptor(chain -> {
                        Request originalRequest = chain.request();
                        Request newRequest = originalRequest.newBuilder()
                                .header("User-Agent", "MyWikimediaStreamApp/1.0 (YourName; YourContact@email.com)")
                                .build();
                        return chain.proceed(newRequest);
                    });
                });

        // Build the EventSource with the custom ConnectStrategy
        EventSource.Builder eventSourceBuilder = new EventSource.Builder(connectStrategy);

        // Build and start the background event source
        BackgroundEventSource source = new BackgroundEventSource.Builder(eventHandler, eventSourceBuilder)
                .build();

        source.start();
        LOGGER.info("Started Wikimedia stream...");

        // Keep the main thread alive
        TimeUnit.MINUTES.sleep(10);
    }

}
