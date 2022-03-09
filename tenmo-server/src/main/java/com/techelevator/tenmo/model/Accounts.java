package com.techelevator.tenmo.model;

public class Accounts {

    private Long accountId;

    private int userId;

    private double balance;

    public  Accounts(Long accountId, int userId, double balance){
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;

    }

    public Accounts(){

    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "account=" + accountId +
                ", userid='" +  userId + '\'' +
                ", balance=" + balance +
                '}';
    }
}
