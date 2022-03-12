package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getBalanceByAccountId(Long accountId);

    BigDecimal getBalanceByUserId(int userId);

    Accounts getAccountById(int userId);

    Accounts getAccountByUsername(String userName);

    void updateBalances(Transfers transfers);



}
