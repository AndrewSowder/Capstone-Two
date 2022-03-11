package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Accounts;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;

public class AccountService {


    private static final String API_BASE_URL = "http://localhost:8080/accounts";
    private RestTemplate restTemplate = new RestTemplate();


    public BigDecimal getBalance(String authToken) throws AuthServiceException {
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "/balance", HttpMethod.GET, makeAuthEntity(authToken), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException e) {
            throw new AuthServiceException(e.getMessage());
        }
        return balance;
    }

    public Accounts updateBalance(Long id, String authToken) throws AuthServiceException {
        Accounts updatedAccounts = null;
        try {
             updatedAccounts = restTemplate.postForObject(API_BASE_URL + "/balance/" + id, makeAuthEntity(authToken), Accounts.class);
            }catch (RestClientResponseException e){
            throw new AuthServiceException(e.getMessage());
        }
        return updatedAccounts;
    }

    public Accounts getAccountsByUserId(int id, String authToken) throws AuthServiceException {
        Accounts accounts = null;
        try {
            ResponseEntity<Accounts> response = restTemplate.exchange(API_BASE_URL + "/" + id, HttpMethod.GET, makeAuthEntity(authToken), Accounts.class);
            accounts = response.getBody();
        }catch (RestClientResponseException e) {
            throw new AuthServiceException(e.getMessage());
        }
        return accounts;
    }





    private HttpEntity<Void> makeAuthEntity(String AUTH_TOKEN) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        return new HttpEntity<>(headers);
    }
}
