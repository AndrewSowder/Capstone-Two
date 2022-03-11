package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();

    public static String AUTH_TOKEN = null;

    public Transfer[] getAllTransfers(String authToken) throws AuthServiceException {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(authToken), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException e) {
            throw new AuthServiceException(e.getMessage());
        }
        return transfers;
    }

    public Transfer getTransfersByTransferId(long transferId, String authToken) throws AuthServiceException{
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(API_BASE_URL + transferId, HttpMethod.GET, makeAuthEntity(authToken), Transfer.class);
            transfer = response.getBody();
        }catch (RestClientResponseException e){
            throw new AuthServiceException(e.getMessage());
        }
        return transfer;
    }

    public Transfer sendTransfer(Transfer transfer, String authToken) throws AuthServiceException{
        Transfer newTransfer = null;
        try {
            newTransfer = restTemplate.postForObject(API_BASE_URL, makeTransferEntity(transfer, authToken), Transfer.class);
        } catch (RestClientResponseException e){
            throw new AuthServiceException(e.getMessage());
        }
        return newTransfer;
    }





    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer, String Auth_Token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Auth_Token);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity(String AUTH_TOKEN) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        return new HttpEntity<>(headers);
    }
}
