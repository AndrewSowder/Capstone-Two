package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {


    private static final String API_BASE_URL = "http://localhost:8080/accounts";
    private RestTemplate restTemplate = new RestTemplate();



    public User[] listAllUsers(String authToken) throws AuthServiceException {
        User[] userList = null;
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL + "/users", HttpMethod.GET, makeAuthEntity(authToken), User[].class );
            userList = response.getBody();
        }catch (RestClientResponseException e) {
            throw new AuthServiceException(e.getMessage());
        }
        return userList;

    }

    public User getUserByUserId(String authToken, Long userId) throws AuthServiceException {
        User user = null;
        try {
            ResponseEntity<User> response = restTemplate.exchange(API_BASE_URL + "/users/" + userId , HttpMethod.GET, makeAuthEntity(authToken), User.class );
            user = response.getBody();
        }catch (RestClientResponseException e) {
            throw new AuthServiceException(e.getMessage());
        }
        return user;
    }

    public User findByUsername(String authToken, String username) throws AuthServiceException {
        User user = null;
        try {
            ResponseEntity<User> response = restTemplate.exchange(API_BASE_URL + "/users/" + username , HttpMethod.GET, makeAuthEntity(authToken), User.class );
            user = response.getBody();
        }catch (RestClientResponseException e) {
            throw new AuthServiceException(e.getMessage());
        }
        return user;
    }



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

    public Transfers updateBalances(Long transferId, Transfers transfers, String authToken) throws AuthServiceException {
        Transfers newTransfers = null;
        try {
             newTransfers = restTemplate.postForObject(API_BASE_URL + "/balance/update/" + transferId, makeTransferEntity(transfers, authToken), Transfers.class);
            }catch (RestClientResponseException e){
            throw new AuthServiceException(e.getMessage());
        }
        return newTransfers;
    }

    public Accounts getAccountsByUserId(Long id, String authToken) throws AuthServiceException {
        Accounts accounts = null;
        try {
            ResponseEntity<Accounts> response = restTemplate.exchange(API_BASE_URL + "/" + id, HttpMethod.GET, makeAuthEntity(authToken), Accounts.class);
            accounts = response.getBody();
        }catch (RestClientResponseException e) {
            throw new AuthServiceException(e.getMessage());
        }
        return accounts;
    }

    public Accounts getAccountByUserName(String userName, String authToken) throws AuthServiceException {
        Accounts account = null;
        try {
            ResponseEntity<Accounts> response = restTemplate.exchange(API_BASE_URL + "/users/username/" + userName, HttpMethod.GET, makeAuthEntity(authToken), Accounts.class);
            account = response.getBody();
        }catch (RestClientResponseException e) {
            throw new AuthServiceException(e.getMessage());
        }
        return account;
    }
    public String getUsernameByAccountId(Long id, String authToken) throws AuthServiceException {
        String userName;
        try {
            ResponseEntity<String> response = restTemplate.exchange(API_BASE_URL + "/username/" + id, HttpMethod.GET, makeAuthEntity(authToken), String.class);
            userName = response.getBody();
        }catch (RestClientResponseException e) {
            throw new AuthServiceException(e.getMessage());
        }
        return userName;
    }



    private HttpEntity<Transfers> makeTransferEntity(Transfers transfers, String Auth_Token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Auth_Token);
        return new HttpEntity<>(transfers, headers);
    }


    private HttpEntity<Void> makeAuthEntity(String AUTH_TOKEN) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        return new HttpEntity<>(headers);
    }

}
