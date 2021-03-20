/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.feedback;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author TrongNS
 */
public class FeedbackDTO implements Serializable{
    private String orderId;
    private int carModelId;
    private String nameOfUser;
    private int rating;
    private String feedback;
    private Date createdDate;
    private int daysSinceCreatedDate;

    public FeedbackDTO(int rating, String feedback) {
        this.rating = rating;
        this.feedback = feedback;
    }

    public FeedbackDTO(String orderId, int carModelId, String nameOfUser, int rating, String feedback, Date createdDate) {
        this.orderId = orderId;
        this.carModelId = carModelId;
        this.nameOfUser = nameOfUser;
        this.rating = rating;
        this.feedback = feedback;
        this.createdDate = createdDate;
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
     * @return the nameOfUser
     */
    public String getNameOfUser() {
        return nameOfUser;
    }

    /**
     * @param nameOfUser the nameOfUser to set
     */
    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
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

    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the daysSinceCreatedDate
     */
    public int getDaysSinceCreatedDate() {
        return daysSinceCreatedDate;
    }

    /**
     * @param daysSinceCreatedDate the daysSinceCreatedDate to set
     */
    public void setDaysSinceCreatedDate(int daysSinceCreatedDate) {
        this.daysSinceCreatedDate = daysSinceCreatedDate;
    }
    
    
}
