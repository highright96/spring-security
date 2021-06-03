package dev.highright96.springsecurity;

import dev.highright96.server.ServerApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {ServerApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicAuthenticationFilterTest {

    @LocalServerPort
    int port;

    RestTemplate client = new RestTemplate();

    private String greetingUrl() {
        return "http://localhost:" + port + "/greeting";
    }

    @DisplayName("1. 인증 실패")
    @Test
    void test_1() {
        assertThatThrownBy(() -> client.getForObject(greetingUrl(), String.class))
                .isInstanceOf(HttpClientErrorException.class);
    }

    @DisplayName("2. 인증 성공1")
    @Test
    void test_2() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(
                "user1:1111".getBytes()
        ));
        HttpEntity httpEntity = new HttpEntity(null, httpHeaders);

        ResponseEntity<String> resp = client.exchange(greetingUrl(), HttpMethod.GET, httpEntity, String.class);

        assertThat(resp.getBody()).isEqualTo("hello");
    }

    @DisplayName("3. 인증 성공2")
    @Test
    void test_3() {
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        String resp = testClient.getForObject(greetingUrl(), String.class);
        assertThat(resp).isEqualTo("hello");
    }

    @DisplayName("4. POST 인증")
    @Test
    void test_4() {
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        String resp = testClient.postForObject(greetingUrl(), "sangwoo", String.class);
        assertThat(resp).isEqualTo("hello sangwoo");
    }

}
