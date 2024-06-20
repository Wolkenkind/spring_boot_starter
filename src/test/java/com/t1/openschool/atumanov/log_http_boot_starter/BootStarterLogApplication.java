package com.t1.openschool.atumanov.log_http_boot_starter;

import com.t1.openschool.atumanov.log_http_boot_starter.model.TestEntity;
import com.t1.openschool.atumanov.log_http_boot_starter.service.TestEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@RequiredArgsConstructor
@EnableJpaRepositories
@SpringBootApplication
public class BootStarterLogApplication {

	private final TestEntityService service;

	public static void main(String[] args) {
		SpringApplication.run(BootStarterLogApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onReady() {
		service.save(new TestEntity("e1", TestEntity.EntityType.TYPE_A, 5, 10d));
		service.save(new TestEntity("e2", TestEntity.EntityType.TYPE_B, 15, 110d));
		service.save(new TestEntity("ent", TestEntity.EntityType.TYPE_A, 0, 0d));
	}
}
