package com.example.bookingapp;

public class Court {
    private String courtName, time, date, calendarDate;
    private int courtImg, price, hour;

    public Court(String courtName, String time, String date, String calendarDate, int courtImg, int price, int hour) {
        this.courtName = courtName;
        this.time = time;
        this.date = date;
        this.calendarDate = calendarDate;
        this.courtImg = courtImg;
        this.price = price;
        this.hour = hour;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public int getCourtImg() {
        return courtImg;
    }

    public void setCourtImg(int courtImg) {
        this.courtImg = courtImg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(String calendarDate) {
        this.calendarDate = calendarDate;
    }
}
