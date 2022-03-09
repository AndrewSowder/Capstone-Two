package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;

public interface AccountDao {

    double getBalanceByAccountId(Long accountId);

    Accounts getAccountBy(int userId);

    void updateBalance(Long accountId);


}
