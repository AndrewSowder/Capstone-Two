package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/transfers";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public Transfer[] getAllTransfers() throws AuthServiceException {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException e) {
            throw new AuthServiceException(e.getMessage());
        }
        return transfers;
    }



    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
