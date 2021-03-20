/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.carmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import trongns.car.CarDTO;
import trongns.feature.FeatureDTO;
import trongns.feedback.FeedbackDTO;

/**
 *
 * @author TrongNS
 */
public class CarModelDTO implements Serializable{
    private int carModelId;
    private String carName;
    private String smallImageLink;
    private String bigImageLink;
    private String color;
    private int seat;
    private String fuel;
    private int typeId;
    private String typeName;
    private int quantity;
    private int year;
    private BigDecimal price;
    private String address;
    private String description;
    private String userRentalDate;
    private String userReturnDate;
    private int userRentalTime;
    private int userReturnTime;
    private int rentDays;
    private BigDecimal userPrice;
    private ArrayList<FeatureDTO> featureList;
    private ArrayList<CarDTO> carList;
    private ArrayList<FeedbackDTO> feedbackList;

    public CarModelDTO() {
    }

    public CarModelDTO(int carModelId, String carName, String smallImageLink, String bigImageLink, String color, int seat, String fuel, int typeId, int quantity, int year, BigDecimal price, String address, String description) {
        this.carModelId = carModelId;
        this.carName = carName;
        this.smallImageLink = smallImageLink;
        this.bigImageLink = bigImageLink;
        this.color = color;
        this.seat = seat;
        this.fuel = fuel;
        this.typeId = typeId;
        this.quantity = quantity;
        this.year = year;
        this.price = price;
        this.address = address;
        this.description = description;
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
     * @return the smallImageLink
     */
    public String getSmallImageLink() {
        return smallImageLink;
    }

    /**
     * @param smallImageLink the smallImageLink to set
     */
    public void setSmallImageLink(String smallImageLink) {
        this.smallImageLink = smallImageLink;
    }

    /**
     * @return the bigImageLink
     */
    public String getBigImageLink() {
        return bigImageLink;
    }

    /**
     * @param bigImageLink the bigImageLink to set
     */
    public void setBigImageLink(String bigImageLink) {
        this.bigImageLink = bigImageLink;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the seat
     */
    public int getSeat() {
        return seat;
    }

    /**
     * @param seat the seat to set
     */
    public void setSeat(int seat) {
        this.seat = seat;
    }

    /**
     * @return the fuel
     */
    public String getFuel() {
        return fuel;
    }

    /**
     * @param fuel the fuel to set
     */
    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    /**
     * @return the typeId
     */
    public int getTypeId() {
        return typeId;
    }

    /**
     * @param typeId the typeId to set
     */
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the featureList
     */
    public ArrayList<FeatureDTO> getFeatureList() {
        return featureList;
    }

    /**
     * @param featureList the featureList to set
     */
    public void setFeatureList(ArrayList<FeatureDTO> featureList) {
        this.featureList = featureList;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CarModelDTO other = (CarModelDTO) obj;
        if (this.carModelId != other.carModelId) {
            return false;
        }
        if (this.seat != other.seat) {
            return false;
        }
        if (this.typeId != other.typeId) {
            return false;
        }
        if (this.quantity != other.quantity) {
            return false;
        }
        if (this.year != other.year) {
            return false;
        }
        if (!Objects.equals(this.carName, other.carName)) {
            return false;
        }
        if (!Objects.equals(this.smallImageLink, other.smallImageLink)) {
            return false;
        }
        if (!Objects.equals(this.bigImageLink, other.bigImageLink)) {
            return false;
        }
        if (!Objects.equals(this.color, other.color)) {
            return false;
        }
        if (!Objects.equals(this.fuel, other.fuel)) {
            return false;
        }
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.carModelId;
        return hash;
    }

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
     * @return the carList
     */
    public ArrayList<CarDTO> getCarList() {
        return carList;
    }

    /**
     * @param carList the carList to set
     */
    public void setCarList(ArrayList<CarDTO> carList) {
        this.carList = carList;
    }

    /**
     * @return the feedbackList
     */
    public ArrayList<FeedbackDTO> getFeedbackList() {
        return feedbackList;
    }

    /**
     * @param feedbackList the feedbackList to set
     */
    public void setFeedbackList(ArrayList<FeedbackDTO> feedbackList) {
        this.feedbackList = feedbackList;
    }

    
}
