package com.t1.openschool.atumanov.log_http_boot_starter.api;


import com.t1.openschool.atumanov.log_http_boot_starter.model.TestEntity;
import com.t1.openschool.atumanov.log_http_boot_starter.service.TestEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ExampleApi {

    public enum ParameterType{
        NAME, TYPE
    }

    private final TestEntityService service;

    @GetMapping("/entity")
    public ResponseEntity<List<TestEntity>> all() {
        return ResponseEntity.ok(new ArrayList<>(service.getAll()));
    }

    @GetMapping("/entity/{param}")
    public ResponseEntity<List<TestEntity>> getByName(@PathVariable("param") String param, @RequestParam (name = "parameterType") ParameterType paramType) {
        List<TestEntity> entities = new ArrayList<>();
        if(paramType.equals(ParameterType.NAME)) {
            TestEntity entity = service.getByName(param);
            if(entity != null) {
                entities.add(entity);
            }
        }

        if(paramType.equals(ParameterType.TYPE)) {
            TestEntity.EntityType type;
            try {
                type = TestEntity.EntityType.valueOf(param);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
            entities = service.getByType(type);
            if(!entities.isEmpty()) {
                return ResponseEntity.ok(entities);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        if(!entities.isEmpty()) {
            return ResponseEntity.ok(entities);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/entity")
    public ResponseEntity<TestEntity> create(@RequestBody TestEntity body) {
        return ResponseEntity.ok().body(service.save(body));
    }

    @PutMapping("/entity")
    public ResponseEntity<TestEntity> updateByName(@RequestBody TestEntity body) {
        TestEntity updated = service.update(body);
        if(updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/entity/{name}")
    public ResponseEntity deleteByName(@PathVariable("name") String name) {
        service.deleteByName(name);
        return ResponseEntity.ok().build();
    }

}
