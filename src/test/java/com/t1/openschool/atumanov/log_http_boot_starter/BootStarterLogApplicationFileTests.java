package com.t1.openschool.atumanov.log_http_boot_starter;

import com.t1.openschool.atumanov.log_http_boot_starter.model.TestEntity;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.t1.openschool.atumanov.log_http_boot_starter.filter.utility.FilterConfigParams.*;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-file.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BootStarterLogApplicationFileTests {

    private static final String API_BASE_URL_PART = "entity";
    private static final String TEST_HEADER = "testheader";
    private static final String TEST_HEADER_VALUE = "THV";
    private static final String RESPONSE_200 = "Outgoing response with status code '200'";
    private static final String RESPONSE_404 = "Outgoing response with status code '404'";

    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

    private static String baseServiceUrl;

    @LocalServerPort
    private int serverTestPort;
    @Autowired
    private TestRestTemplate template;
    @Value("${" + CONFIG_PREFIX + "." + LOGGING_FILE + "}")
    private String logFile;

    @Test
    @Order(1)
    void contextLoads() {
        baseServiceUrl = "http://localhost:" + serverTestPort + "/" + API_BASE_URL_PART;
        template.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add(TEST_HEADER, TEST_HEADER_VALUE);
                    return execution.execute(request, body);
                }));
    }

    private boolean assertLogEntriesFound(String logFilePathname, ArrayList<String> entries) throws IOException {
        Path filePath = Path.of(logFilePathname);
        List<String> lines = Files.readAllLines(filePath);
        String[] toFind = entries.toArray(new String[0]);
        int findCounter = 0;
        for(String line: lines) {
            if(line.equals(toFind[findCounter])) {
                if(++findCounter == toFind.length-1) break;
            }
        }
        if(findCounter == toFind.length-1) {
            return true;
        } else {
            System.out.println("Entry '" + toFind[findCounter] + "' was not found");
            return false;
        }
    }

    @Test
    @Order(2)
    void getTest() throws Exception {
        template.getForEntity(baseServiceUrl, Object.class);

        ArrayList<String> entries = new ArrayList<>();
        entries.add("Incoming request to '/" + API_BASE_URL_PART + "', request method: 'GET'");
        entries.add("Header '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");
        entries.add(RESPONSE_200);

        assert assertLogEntriesFound(logFile, entries);
    }

    @Test
    @Order(3)
    void postTest() throws Exception {
        String entityName = "testPost";
        TestEntity.EntityType entityType = TestEntity.EntityType.TYPE_C;
        int entityIntValue = 20;
        double entityDoubleValue = 40d;

        TestEntity entity = new TestEntity(entityName, entityType, entityIntValue, entityDoubleValue);
        HttpEntity<TestEntity> request = new HttpEntity<>(entity, null);
        template.postForEntity(baseServiceUrl, request, TestEntity.class);

        ArrayList<String> entries = new ArrayList<>();
        entries.add("Incoming request to '/" + API_BASE_URL_PART + "', request method: 'POST'");
        entries.add("Header '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");
        entries.add(RESPONSE_200);
        entries.add("Response body: '{\"name\":\"" + entityName + "\",\"type\":\"" + entityType + "\",\"intValue\":" + entityIntValue + ",\"doubleValue\":" + entityDoubleValue + "}'");

        assert assertLogEntriesFound(logFile, entries);
    }

    @Test
    @Order(4)
    void putTest() throws Exception {
        String entityName = "testPost";
        TestEntity.EntityType entityType = TestEntity.EntityType.TYPE_A;
        int entityIntValue = 2;
        double entityDoubleValue = 4d;

        TestEntity entity = new TestEntity(entityName, entityType, entityIntValue, entityDoubleValue);
        HttpEntity<TestEntity> request = new HttpEntity<>(entity, null);
        template.exchange(baseServiceUrl, HttpMethod.PUT, request, TestEntity.class);

        ArrayList<String> entries = new ArrayList<>();
        entries.add("Incoming request to '/" + API_BASE_URL_PART + "', request method: 'PUT'");
        entries.add("Header '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");
        entries.add(RESPONSE_200);
        entries.add("Response body: '{\"name\":\"" + entityName + "\",\"type\":\"" + entityType + "\",\"intValue\":" + entityIntValue + ",\"doubleValue\":" + entityDoubleValue + "}'");

        assert assertLogEntriesFound(logFile, entries);
    }

    @Test
    @Order(5)
    void getByNameTest() throws Exception {
        Map<String, String> params = new HashMap<>();
        String entityName = "testPost";
        String paramType = "NAME";
        params.put("param", entityName);
        params.put("type", paramType);
        template.getForEntity(baseServiceUrl + "/{param}?parameterType={type}", Object.class, params);

        ArrayList<String> entries = new ArrayList<>();
        entries.add("Incoming request to '/" + API_BASE_URL_PART + "/" + entityName + "', request method: 'GET'");
        entries.add("Header '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");
        entries.add(RESPONSE_200);
        entries.add("Response body: '[{\"name\":\"testPost\",\"type\":\"TYPE_A\",\"intValue\":2,\"doubleValue\":4.0}]';");

        assert assertLogEntriesFound(logFile, entries);
    }

    @Test
    @Order(6)
    void deleteTest() throws Exception{
        template.exchange(baseServiceUrl + "/testPost", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        ArrayList<String> entries = new ArrayList<>();
        entries.add("Incoming request to '/" + API_BASE_URL_PART + "/testPost', request method: 'DELETE'");
        entries.add("Header '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");
        entries.add(RESPONSE_200);

        assert assertLogEntriesFound(logFile, entries);
    }

    @Test
    @Order(7)
    void notFoundTest() throws Exception {
        Map<String, String> params = new HashMap<>();
        TestEntity.EntityType type = TestEntity.EntityType.TYPE_C;
        params.put("param", type.name());
        params.put("type", "TYPE");
        template.getForEntity(baseServiceUrl + "/{param}?parameterType={type}", Object.class, params);

        ArrayList<String> entries = new ArrayList<>();
        entries.add("Incoming request to '/" + API_BASE_URL_PART + "/" + type + "', request method: 'GET'");
        entries.add("Header '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");
        entries.add(RESPONSE_404);

        assert assertLogEntriesFound(logFile, entries);
    }

    @Test
    @Order(8)
    void exceptionTest() throws Exception {
        String nonExistentPath = "_not_exists";
        template.getForEntity(baseServiceUrl + nonExistentPath, Object.class);

        ArrayList<String> entries = new ArrayList<>();
        entries.add("Incoming request to '/" + API_BASE_URL_PART + nonExistentPath + "', request method: 'GET'");
        entries.add("Header '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");
        entries.add(RESPONSE_404);
        entries.add("Outgoing response with status code '404' from \"ERROR\" dispatch");

        assert assertLogEntriesFound(logFile, entries);
    }
}
