package com.rain.monitor.trade.okCoin.entity;

/**
 * Created by chendengyu on 2016/3/30.
 */
public class Order {
    private double amount;
    private double avg_price;
    private long create_date;
    private double deal_amount;
    private String order_id;
    private double price;
    private int status;
    //ltc_cny
    private String symbol;
    //buy
    private String type;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(double avg_price) {
        this.avg_price = avg_price;
    }

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public double getDeal_amount() {
        return deal_amount;
    }

    public void setDeal_amount(double deal_amount) {
        this.deal_amount = deal_amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Order{" +
                "amount='" + amount + '\'' +
                ", avg_price=" + avg_price +
                ", create_date=" + create_date +
                ", deal_amount=" + deal_amount +
                ", order_id='" + order_id + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", symbol='" + symbol + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

}
