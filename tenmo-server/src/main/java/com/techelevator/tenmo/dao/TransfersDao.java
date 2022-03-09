package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;

import java.util.List;

public interface TransfersDao {

    List<Transfers> getAllTransfers();

    List<Transfers> getTransfersByTransferId(long transferId);

    List<Transfers> getTransfersByUserId(int userid);

    void sendTransfer(Transfers transfers);



    

}
