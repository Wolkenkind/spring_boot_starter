package com.t1.openschool.atumanov.log_http_boot_starter.service;

import com.t1.openschool.atumanov.log_http_boot_starter.model.TestEntity;
import com.t1.openschool.atumanov.log_http_boot_starter.repository.TestEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TestEntityService {

    private final TestEntityRepository repository;

    public List<TestEntity> getAll() {
        return repository.findAll();
    }

    public TestEntity getByName(String name) {
        return repository.findByName(name);
    }

    public List<TestEntity> getByType(TestEntity.EntityType type) {
        return repository.findByType(type);
    }

    public TestEntity save(TestEntity entity) {
        return repository.save(entity);
    }

    public TestEntity update(TestEntity entity) {
        TestEntity existing = repository.findByName(entity.getName());
        if(existing != null) {
            return repository.save(entity);
        } else {
            return null;
        }
    }

    public void deleteByName(String name) {
        repository.deleteById(name);
    }
}
