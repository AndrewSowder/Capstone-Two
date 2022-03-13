package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfers {

    private Long transferId;
    private int transferTypeId;
    private int transferStatusId;
    private Long accountFrom;
    private Long accountTo;
    private BigDecimal amount;

    public Transfers() {

    };

    public Transfers(Long transferId, int transferType, int transferStatus, Long accountFrom, Long accountTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferTypeId = transferType;
        this.transferStatusId = transferStatus;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public Long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "\n--------------------------------------------" +
                "\n Transfer Details" +
                "\n--------------------------------------------" +
                "\n Id: " + transferId +
                "\n From: " + accountFrom +
                "\n To: " + accountTo +
                "\n Type: " + transferTypeId +
                "\n Status: " + transferStatusId +
                "\n Amount: $" + amount ;
    }
}
