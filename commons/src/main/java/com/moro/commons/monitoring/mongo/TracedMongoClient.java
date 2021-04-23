package com.moro.commons.monitoring.mongo;

import org.springframework.stereotype.Component;
import com.mongodb.client.MongoClients;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import io.opentracing.Tracer;
import io.opentracing.contrib.mongo.common.TracingCommandListener;

/**
 * Creates mongo client with tracing capabilities
 */
@Component
public class TracedMongoClient {
    public MongoClient createTracedMongoClient(Tracer tracer, String mongoUri) {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        // Instantiate TracingCommandListener
        TracingCommandListener listener = new TracingCommandListener.Builder(tracer).build();
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .addCommandListener(listener)
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }
}
