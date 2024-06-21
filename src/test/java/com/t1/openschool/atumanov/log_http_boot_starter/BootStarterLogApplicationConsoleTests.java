package com.t1.openschool.atumanov.log_http_boot_starter;

import com.t1.openschool.atumanov.log_http_boot_starter.model.TestEntity;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-console.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BootStarterLogApplicationConsoleTests {

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

	@Test
	@Order(2)
	void getTest() throws Exception {
		String text = tapSystemOutNormalized(() -> {
			template.getForEntity(baseServiceUrl, Object.class);
		});

		assert text.contains("Incoming request to '/" + API_BASE_URL_PART + "', request method: 'GET'");
		assert text.contains("Header '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");

		assert text.contains(RESPONSE_200);
	}

	@Test
	@Order(2)
	void postTest() throws Exception {
		String entityName = "testPost";
		TestEntity.EntityType entityType = TestEntity.EntityType.TYPE_C;
		int entityIntValue = 20;
		double entityDoubleValue = 40d;

		TestEntity entity = new TestEntity(entityName, entityType, entityIntValue, entityDoubleValue);
		HttpEntity<TestEntity> request = new HttpEntity<>(entity, null);
		String text = tapSystemOutNormalized(() -> {
			template.postForEntity(baseServiceUrl, request, TestEntity.class);
		});

		assert text.contains("Incoming request to '/" + API_BASE_URL_PART + "', request method: 'POST'");
		assert text.contains("Header '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");

		assert text.contains(RESPONSE_200);
		assert text.contains("Response body: '{\"name\":\"" + entityName + "\",\"type\":\"" + entityType + "\",\"intValue\":" + entityIntValue + ",\"doubleValue\":" + entityDoubleValue + "}'");
	}

	@Test
	@Order(3)
	void putTest() throws Exception {
		String entityName = "testPost";
		TestEntity.EntityType entityType = TestEntity.EntityType.TYPE_A;
		int entityIntValue = 2;
		double entityDoubleValue = 4d;

		TestEntity entity = new TestEntity(entityName, entityType, entityIntValue, entityDoubleValue);
		HttpEntity<TestEntity> request = new HttpEntity<>(entity, null);
		String text = tapSystemOutNormalized(() -> {
			template.exchange(baseServiceUrl, HttpMethod.PUT, request, TestEntity.class);
		});

		assert text.contains("Incoming request to '/" + API_BASE_URL_PART + "', request method: 'PUT'");
		assert text.contains("Header '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");

		assert text.contains(RESPONSE_200);
		assert text.contains("Response body: '{\"name\":\"" + entityName + "\",\"type\":\"" + entityType + "\",\"intValue\":" + entityIntValue + ",\"doubleValue\":" + entityDoubleValue + "}'");
	}

	@Test
	@Order(4)
	void getByNameTest() throws Exception {
		Map<String, String> params = new HashMap<>();
		String entityName = "testPost";
		String paramType = "NAME";
		params.put("param", entityName);
		params.put("type", paramType);
		String text = tapSystemOutNormalized(() -> {
			template.getForEntity(baseServiceUrl + "/{param}?parameterType={type}", Object.class, params);
		});

		assert text.contains("Incoming request to '/" + API_BASE_URL_PART + "/" + entityName + "', request method: 'GET'");
		assert text.contains("Query parameter: 'parameterType' value: '" + paramType + "';");
		assert text.contains("Header '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");

		assert text.contains(RESPONSE_200);
		assert text.contains("Response body: '[{\"name\":\"testPost\",\"type\":\"TYPE_A\",\"intValue\":2,\"doubleValue\":4.0}]';");
	}

	@Test
	@Order(5)
	void deleteTest() throws Exception{
		String text = tapSystemOutNormalized(() -> {
			template.exchange(baseServiceUrl + "/testPost", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
		});

		assert text.contains("Incoming request to '/" + API_BASE_URL_PART + "/testPost', request method: 'DELETE'");
		assert text.contains("Header '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");

		assert text.contains(RESPONSE_200);
	}

	@Test
	@Order(6)
	void notFoundTest() throws Exception {
		Map<String, String> params = new HashMap<>();
		TestEntity.EntityType type = TestEntity.EntityType.TYPE_C;
		params.put("param", type.name());
		params.put("type", "TYPE");
		String text = tapSystemOutNormalized(() -> {
			template.getForEntity(baseServiceUrl + "/{param}?parameterType={type}", Object.class, params);
		});

		assert text.contains("Incoming request to '/" + API_BASE_URL_PART + "/" + type + "', request method: 'GET'");
		assert text.contains("Query parameter: 'parameterType' value: 'TYPE';");
		assert text.contains("Header '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");

		assert text.contains(RESPONSE_404);
	}

	@Test
	@Order(7)
	void exceptionTest() throws Exception {
		String nonExistentPath = "_not_exists";
		String text = tapSystemOutNormalized(() -> {
			template.getForEntity(baseServiceUrl + nonExistentPath, Object.class);
		});

		System.out.println(text);

		assert text.contains("Incoming request to '/" + API_BASE_URL_PART + nonExistentPath + "', request method: 'GET'");
		assert text.contains("Header '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");

		assert text.contains(RESPONSE_404);
		assert text.contains(RESPONSE_404);
	}
}
