package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getBalanceByAccountId(Long accountId);

    BigDecimal getBalanceByUserId(int userId);

    Accounts getAccountBy(int userId);

    void updateBalance(Long accountId);


}
