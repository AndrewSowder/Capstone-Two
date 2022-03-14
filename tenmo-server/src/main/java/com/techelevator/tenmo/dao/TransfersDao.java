package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;

public interface TransfersDao {

    List<Transfers> getAllTransfers();

    Transfers getTransfersByTransferId(long transferId);

    List<Transfers> getTransfersByUserId(int userid);

    List<Transfers> getTransfersByAccount(Long accountId);

    void sendTransfer(Transfers transfers);

    void updateTransfer(Transfers transfer);

    Long getNewTransferId();

    String getTransferTypeDesc(int id);

    String getTransferStatusDesc(int id);






}
