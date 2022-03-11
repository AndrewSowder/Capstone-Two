package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransfersDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


@PreAuthorize("isAuthenticated()")
@RestController

public class AppApiController {

    private UserDao userDao;
    private AccountDao accountDao;
    private TransfersDao transfersDao;

    public AppApiController(UserDao userDao, AccountDao accountDao, TransfersDao transfersDao){
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transfersDao = transfersDao;
    }

    @GetMapping("accounts/balance")
    public BigDecimal getBalance(Principal principal){
       int userId =  userDao.findIdByUsername(principal.getName());
       return this.accountDao.getBalanceByUserId(userId);
    }

    @PutMapping("accounts/balance/{id}")
    public void updateBalance(@RequestBody Accounts accounts, @PathVariable long id) throws AccountNotFoundException {
        this.accountDao.updateBalance(accounts.getAccountId());
    }

    @GetMapping("accounts/{id}")
    public Accounts getAccountByUserId(@PathVariable int id) throws AccountNotFoundException{
        return this.accountDao.getAccountBy(id);
    }



    @GetMapping("transfers")
    public List<Transfers> getAllTransfers(Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return this.transfersDao.getTransfersByUserId(userId);

    }

    @PostMapping("transfers")
    public void sendTransfer(@RequestBody Transfers newTransfer){
        this.transfersDao.sendTransfer(newTransfer);
    }

    @GetMapping("transfers/{id}")
    public Transfers getTransfersByTransferId(@PathVariable long id) throws AccountNotFoundException {
        return this.transfersDao.getTransfersByTransferId(id);
    }






}
