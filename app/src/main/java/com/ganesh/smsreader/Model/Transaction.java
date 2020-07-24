package com.ganesh.smsreader.Model;


public class Transaction {
    private String  id;
    private String address;
    private TransactionType transactionType;
    private Long timestamp;
    private long amount;
    private String tags;

    public Transaction(String id,
                       String address,
                       TransactionType transactionType,
                       Long  timestamp,
                       String amount) {
        this.address = address;
        this.id = id;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
        try {
            this.amount = (long) Double.parseDouble(
                    amount
                            .replaceAll("Rs.","")
                            .replaceAll("INR","")
                            .replaceAll("Rs", "")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "address='" + address + '\'' +
                ", transactionType=" + transactionType +
                ", timestamp='" + timestamp + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
