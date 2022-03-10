package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getBalanceByAccountId(Long accountId);

    Accounts getAccountBy(int userId);

    void updateBalance(Long accountId);


}
