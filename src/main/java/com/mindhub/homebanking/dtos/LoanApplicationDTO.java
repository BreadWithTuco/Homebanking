package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    private Long idLoan;
    private Double amount;
    private Integer payment;
    private String accountNumber;

    public LoanApplicationDTO(){}

    public Long getIdLoan() {
        return idLoan;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPayment() {
        return payment;
    }
    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
