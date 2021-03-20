/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.orderdetail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import trongns.ordercardetail.OrderCarDetailDTO;

/**
 *
 * @author TrongNS
 */
public class OrderDetailDTO implements Serializable{
    private String orderId;
    private int carModelId;
    private String carName;
    private String imageLink;
    private int amount;
    private BigDecimal subTotal;
    private Date pickUpDate;
    private Date dropOffDate;
    private int days;
    private ArrayList<OrderCarDetailDTO> orderCarDetailList;
    private boolean renting;
    private int rating;
    private String feedback;

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(String orderId, int carModelId, String carName, String imageLink, int amount, BigDecimal subTotal, Date pickUpDate, Date dropOffDate, int days) {
        this.orderId = orderId;
        this.carModelId = carModelId;
        this.carName = carName;
        this.imageLink = imageLink;
        this.amount = amount;
        this.subTotal = subTotal;
        this.pickUpDate = pickUpDate;
        this.dropOffDate = dropOffDate;
        this.days = days;
    }

    

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
     * @return the subTotal
     */
    public BigDecimal getSubTotal() {
        return subTotal;
    }

    /**
     * @param subTotal the subTotal to set
     */
    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    /**
     * @return the pickUpDate
     */
    public Date getPickUpDate() {
        return pickUpDate;
    }

    /**
     * @param pickUpDate the pickUpDate to set
     */
    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    /**
     * @return the dropOffDate
     */
    public Date getDropOffDate() {
        return dropOffDate;
    }

    /**
     * @param dropOffDate the dropOffDate to set
     */
    public void setDropOffDate(Date dropOffDate) {
        this.dropOffDate = dropOffDate;
    }

    /**
     * @return the carName
     */
    public String getCarName() {
        return carName;
    }

    /**
     * @param carName the carName to set
     */
    public void setCarName(String carName) {
        this.carName = carName;
    }

    /**
     * @return the imageLink
     */
    public String getImageLink() {
        return imageLink;
    }

    /**
     * @param imageLink the imageLink to set
     */
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    /**
     * @return the orderCarDetailList
     */
    public ArrayList<OrderCarDetailDTO> getOrderCarDetailList() {
        return orderCarDetailList;
    }

    /**
     * @param orderCarDetailList the orderCarDetailList to set
     */
    public void setOrderCarDetailList(ArrayList<OrderCarDetailDTO> orderCarDetailList) {
        this.orderCarDetailList = orderCarDetailList;
    }

    /**
     * @return the days
     */
    public int getDays() {
        return days;
    }

    /**
     * @param days the days to set
     */
    public void setDays(int days) {
        this.days = days;
    }

    /**
     * @return the renting
     */
    public boolean isRenting() {
        return renting;
    }

    /**
     * @param renting the renting to set
     */
    public void setRenting(boolean renting) {
        this.renting = renting;
    }

    /**
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * @return the feedback
     */
    public String getFeedback() {
        return feedback;
    }

    /**
     * @param feedback the feedback to set
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    
}
