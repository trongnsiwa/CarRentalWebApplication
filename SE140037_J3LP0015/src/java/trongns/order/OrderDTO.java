/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import trongns.orderdetail.OrderDetailDTO;

/**
 *
 * @author TrongNS
 */
public class OrderDTO implements Serializable{
    private String orderId;
    private String userId;
    private Date rentalDate;
    private String discountCode;
    private int discountPercent;
    private BigDecimal totalPrice;
    private boolean activate;
    private ArrayList<OrderDetailDTO> rentingItems;

    public OrderDTO() {
    }

    public OrderDTO(String orderId, String userId, Date rentalDate, String discountCode, BigDecimal totalPrice, boolean activate) {
        this.orderId = orderId;
        this.userId = userId;
        this.rentalDate = rentalDate;
        this.discountCode = discountCode;
        this.totalPrice = totalPrice;
        this.activate = activate;
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
     * @return the rentalDate
     */
    public Date getRentalDate() {
        return rentalDate;
    }

    /**
     * @param rentalDate the rentalDate to set
     */
    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    /**
     * @return the discountCode
     */
    public String getDiscountCode() {
        return discountCode;
    }

    /**
     * @param discountCode the discountCode to set
     */
    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    /**
     * @return the totalPrice
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return the activate
     */
    public boolean isActivate() {
        return activate;
    }

    /**
     * @param activate the activate to set
     */
    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    /**
     * @return the rentingItems
     */
    public ArrayList<OrderDetailDTO> getRentingItems() {
        return rentingItems;
    }

    /**
     * @param rentingItems the rentingItems to set
     */
    public void setRentingItems(ArrayList<OrderDetailDTO> rentingItems) {
        this.rentingItems = rentingItems;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.orderId);
        return hash;
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
        final OrderDTO other = (OrderDTO) obj;
        if (!Objects.equals(this.orderId, other.orderId)) {
            return false;
        }
        return true;
    }

    /**
     * @return the discountPercent
     */
    public int getDiscountPercent() {
        return discountPercent;
    }

    /**
     * @param discountPercent the discountPercent to set
     */
    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }
    
    
}
