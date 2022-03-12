package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfers;
import org.bouncycastle.jce.provider.symmetric.Grainv1;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();

    public static String AUTH_TOKEN = null;

    public Transfers[] getAllTransfers(String authToken) throws AuthServiceException {
        Transfers[] transfers = null;
        try {
            ResponseEntity<Transfers[]> response =
                    restTemplate.exchange(API_BASE_URL + "transfers", HttpMethod.GET, makeAuthEntity(authToken), Transfers[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException e) {
            throw new AuthServiceException(e.getMessage());
        }
        return transfers;
    }

    public Transfers getTransfersByTransferId(long transferId, String authToken) throws AuthServiceException{
        Transfers transfers = null;
        try {
            ResponseEntity<Transfers> response =
                    restTemplate.exchange(API_BASE_URL + "transfers/" + transferId, HttpMethod.GET, makeAuthEntity(authToken), Transfers.class);
            transfers = response.getBody();
        }catch (RestClientResponseException e){
            throw new AuthServiceException(e.getMessage());
        }
        return transfers;
    }

    public Transfers sendTransfer(Long transferId, Transfers transfer, String authToken) throws AuthServiceException {
        Transfers newTransfer = null;
        try {
            newTransfer = restTemplate.postForObject(API_BASE_URL + "transfers/" + transferId, makeTransferEntity(transfer, authToken), Transfers.class);
        } catch (RestClientResponseException e){
            System.out.println(e.getMessage());

        }
        return newTransfer;
    }

    public Transfers[] getTransfersByUserId(long userId, String authToken) throws AuthServiceException {
        Transfers[] transfers = null;
        try {
            ResponseEntity<Transfers[]> response =
                    restTemplate.exchange(API_BASE_URL+ "transfers/user/" + userId,HttpMethod.GET, makeAuthEntity(authToken), Transfers[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException e) {
            throw new AuthServiceException(e.getMessage());
        }
        return transfers;
    }

    public Transfers[] getTransfersByAccount(Long accountId, String authToken) throws AuthServiceException {
        Transfers[] transfers = null;
        try {
            ResponseEntity<Transfers[]> response =
                    restTemplate.exchange(API_BASE_URL+ "accounts/" + accountId + "/transfers" ,HttpMethod.GET, makeAuthEntity(authToken), Transfers[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException e) {
            throw new AuthServiceException(e.getMessage());
        }
        return transfers;
    }

    public Long getNewTransferId(String authToken) throws AuthServiceException {
        Long newTransferId = null;
        try {
            ResponseEntity<Long> response = restTemplate.exchange(API_BASE_URL + "transfers/next", HttpMethod.GET, makeAuthEntity(authToken), Long.class);
            newTransferId = response.getBody();
        } catch (RestClientResponseException e) {
            throw new AuthServiceException(e.getMessage());
        }
        return newTransferId;
    }





    private HttpEntity<Transfers> makeTransferEntity(Transfers transfers, String Auth_Token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Auth_Token);
        HttpEntity<Transfers> entity = new HttpEntity<>(transfers, headers);
        return entity;
    }

    private HttpEntity<Void> makeAuthEntity(String AUTH_TOKEN) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        return new HttpEntity<>(headers);
    }
}
