package com.example.webhookyt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        RestTemplate restTemplate = new RestTemplate();


        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, String> request = new HashMap<>();
        request.put("name", "Yana Trivedi");
        request.put("regNo", "ADT23SOCB1335");
        request.put("email", "yanatrivedi111@gmail.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, entity, Map.class);

        Map body = response.getBody();

        String webhookUrl = (String) body.get("webhook");
        String accessToken = (String) body.get("accessToken");

        System.out.println("Webhook: " + webhookUrl);


        String finalQuery = "SELECT p.amount AS SALARY, CONCAT(e.first_name, ' ', e.last_name) AS NAME, TIMESTAMPDIFF(YEAR, e.dob, CURDATE()) AS AGE, d.department_name AS DEPARTMENT_NAME FROM payments p JOIN employee e ON p.emp_id = e.emp_id JOIN department d ON e.department = d.department_id WHERE DAY(p.payment_time) != 1 ORDER BY p.amount DESC LIMIT 1";


        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_JSON);
        headers2.set("Authorization", accessToken);

        Map<String, String> request2 = new HashMap<>();
        request2.put("finalQuery", finalQuery);

        HttpEntity<Map<String, String>> entity2 = new HttpEntity<>(request2, headers2);

        ResponseEntity<String> finalResponse =
                restTemplate.postForEntity(webhookUrl, entity2, String.class);

        System.out.println("FINAL RESPONSE: " + finalResponse.getBody());
    }
}