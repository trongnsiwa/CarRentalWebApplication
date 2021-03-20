/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.cart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author TrongNS
 */
public class CartDTO implements Serializable{
    private String userId;
    private int carModelId;
    private int amount;
    private String userRentalDate;
    private int userRentalTime;
    private String userReturnDate;
    private int userReturnTime;
    private int rentDays;
    private BigDecimal userPrice;

    public CartDTO() {
    }

    public CartDTO(String userId, int carModelId, int amount, String userRentalDate, int userRentalTime, String userReturnDate, int userReturnTime, int rentDays, BigDecimal userPrice) {
        this.userId = userId;
        this.carModelId = carModelId;
        this.amount = amount;
        this.userRentalDate = userRentalDate;
        this.userRentalTime = userRentalTime;
        this.userReturnDate = userReturnDate;
        this.userReturnTime = userReturnTime;
        this.rentDays = rentDays;
        this.userPrice = userPrice;
    }

    

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the carModelId
     */
    public int getCarModelId() {
        return carModelId;
    }

    /**
     * @param carModelId the carModelId to set
     */
    public void setCarModelId(int carModelId) {
        this.carModelId = carModelId;
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * @return the userRentalDate
     */
    public String getUserRentalDate() {
        return userRentalDate;
    }

    /**
     * @param userRentalDate the userRentalDate to set
     */
    public void setUserRentalDate(String userRentalDate) {
        this.userRentalDate = userRentalDate;
    }

    /**
     * @return the userReturnDate
     */
    public String getUserReturnDate() {
        return userReturnDate;
    }

    /**
     * @param userReturnDate the userReturnDate to set
     */
    public void setUserReturnDate(String userReturnDate) {
        this.userReturnDate = userReturnDate;
    }

    /**
     * @return the userRentalTime
     */
    public int getUserRentalTime() {
        return userRentalTime;
    }

    /**
     * @param userRentalTime the userRentalTime to set
     */
    public void setUserRentalTime(int userRentalTime) {
        this.userRentalTime = userRentalTime;
    }

    /**
     * @return the userReturnTime
     */
    public int getUserReturnTime() {
        return userReturnTime;
    }

    /**
     * @param userReturnTime the userReturnTime to set
     */
    public void setUserReturnTime(int userReturnTime) {
        this.userReturnTime = userReturnTime;
    }

    /**
     * @return the rentDays
     */
    public int getRentDays() {
        return rentDays;
    }

    /**
     * @param rentDays the rentDays to set
     */
    public void setRentDays(int rentDays) {
        this.rentDays = rentDays;
    }

    /**
     * @return the userPrice
     */
    public BigDecimal getUserPrice() {
        return userPrice;
    }

    /**
     * @param userPrice the userPrice to set
     */
    public void setUserPrice(BigDecimal userPrice) {
        this.userPrice = userPrice;
    }

    
}
