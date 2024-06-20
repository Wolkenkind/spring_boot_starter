package com.t1.openschool.atumanov.log_http_boot_starter.repository;

import com.t1.openschool.atumanov.log_http_boot_starter.model.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestEntityRepository extends JpaRepository<TestEntity, String> {
    TestEntity findByName(String name);
    List<TestEntity> findByType(TestEntity.EntityType type);
}
