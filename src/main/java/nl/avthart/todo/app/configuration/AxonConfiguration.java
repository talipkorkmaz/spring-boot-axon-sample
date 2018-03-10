package nl.avthart.todo.app.configuration;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.common.transaction.NoTransactionManager;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.serialization.upcasting.event.NoOpEventUpcaster;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Order (1)
public class AxonConfiguration {

	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public Serializer serializer() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		return new JacksonSerializer(objectMapper);
	}

	@Bean
	public EventStorageEngine eventStorageEngine(DataSource dataSource) throws SQLException {
		EntityManagerProvider entityManagerProvider = new SimpleEntityManagerProvider(entityManager);
		return new JpaEventStorageEngine(serializer(), NoOpEventUpcaster.INSTANCE, dataSource, entityManagerProvider, NoTransactionManager.INSTANCE);
	}

	//	@Bean
	//	@Qualifier("eventSerializer")
	//	public Serializer serializer() {
	//	        ObjectMapper objectMapper = new ObjectMapper();
	//	        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
	//	        return new JacksonSerializer(objectMapper);
	//	}

	//	@PersistenceContext
	//	private EntityManager entityManager;
	//	
	//	@Bean
	//	@Qualifier("eventSerializer")
	//	public Serializer serializer() {
	//
	//		ObjectMapper objectMapper = new ObjectMapper();
	//		objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
	//		return new JacksonSerializer(objectMapper);
	//	}
	//
	//	@Bean
	//	public EventStorageEngine eventStorageEngine(DataSource dataSource) throws SQLException {
	//
	//		EntityManagerProvider entityManagerProvider = new SimpleEntityManagerProvider(entityManager);
	//		return new JpaEventStorageEngine(serializer(), NoOpEventUpcaster.INSTANCE, dataSource, entityManagerProvider, NoTransactionManager.INSTANCE);
	//	}

}
