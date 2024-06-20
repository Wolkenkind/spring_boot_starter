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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-console.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BootStarterLogApplicationTestsConsole {

	private static final String API_BASE_URL_PART = "entity";
	private static final String TEST_HEADER = "testheader";
	private static final String TEST_HEADER_VALUE = "THV";

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
	void consoleTest() throws Exception {
		String text = tapSystemOutNormalized(() -> {
			template.getForEntity(baseServiceUrl, Object.class);
		});
		assert text.contains("Incoming request to '/" + API_BASE_URL_PART + "', request method: 'GET'");
		assert text.contains("\nHeader '" + TEST_HEADER + "' value: '" + TEST_HEADER_VALUE + "';");
	}

}
