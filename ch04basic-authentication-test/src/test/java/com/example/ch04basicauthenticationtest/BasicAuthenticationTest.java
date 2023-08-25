package com.example.ch04basicauthenticationtest;


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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 랜덤 포트로 테스트 서버를 띄움 : 이미 사용 중인 포트에 의한 충돌을 방지
public class BasicAuthenticationTest {

    @LocalServerPort
    int port;

    RestTemplate client = new RestTemplate(); // RestTemplate : HTTP 요청을 보내고 응답을 받는 클래스

    private String greetingUrl(){ // 테스트 서버의 /greeting 경로를 리턴
        return "http://localhost:"+port+"/greeting";
    }

    /**
     *  Basic Authentication 없이 "/greeting" URL에 접근하려 할 때,
     *  401(Unauthorized) 오류가 발생하는지 확인하는 테스트
     */
    @DisplayName("1. 인증 실패")
    @Test
    void test_1(){
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            client.getForObject(greetingUrl(), String.class);
        });
        assertEquals(401, exception.getRawStatusCode());
    }


    /**
     * 유저 이름과 비밀번호를 Base64로 인코딩하여 HTTP 헤더에 추가한 후,
     * "/greeting" URL에 접근하려 할 때, 정상적으로 "hello"라는 응답이 오는지 확인하는 테스트
     */
    @DisplayName("2. 인증 성공")
    @Test
    void test_2() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic "+ Base64.getEncoder().encodeToString(
                "user1:1111".getBytes()
        ));
        HttpEntity entity = new HttpEntity(null, headers);
        ResponseEntity<String> resp = client.exchange(greetingUrl(), HttpMethod.GET, entity, String.class);
        assertEquals("hello", resp.getBody());
    }

    /**
     * TestRestTemplate 객체를 사용하여 유저 이름과 비밀번호로 인증한 후,
     * "/greeting" URL에 접근하려 할 때, 정상적으로 "hello"라는 응답이 오는지 확인하는 테스트
     */
    @DisplayName("3. 인증성공2 ")
    @Test
    void test_3() {
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        String resp = testClient.getForObject(greetingUrl(), String.class);
        assertEquals("hello", resp);
    }

    /**
     * POST 요청을 보내며 유저 이름과 비밀번호로 인증한 후,
     * 응답 결과가 "hello jongwon"인지 확인하는 테스트
     */
    @DisplayName("4. POST 인증")
    @Test
    void test_4() {
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        ResponseEntity<String> resp = testClient.postForEntity(greetingUrl(), "jongwon", String.class);
        assertEquals("hello jongwon", resp.getBody());
    }
    
}
