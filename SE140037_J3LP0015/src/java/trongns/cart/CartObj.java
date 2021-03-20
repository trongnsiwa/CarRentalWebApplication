/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.cart;

import java.io.Serializable;
import java.util.HashMap;
import trongns.carmodel.CarModelDTO;
import trongns.discount.DiscountDTO;

/**
 *
 * @author TrongNS
 */
public class CartObj implements Serializable {

    private HashMap<CarModelDTO, Integer> items;
    private DiscountDTO discount;

    /**
     * @return the items
     */
    public HashMap<CarModelDTO, Integer> getItems() {
        return items;
    }

    /**
     * @return the discount
     */
    public DiscountDTO getDiscount() {
        return discount;
    }

    public void applyDiscount(DiscountDTO discount) {
        if (this.items == null) {
            return;
        }

        this.discount = discount;
    }

    public void removeDiscount() {
        if (this.discount != null) {
            this.discount = null;
        }
    }

    public void addCarToCart(CarModelDTO dto) {
        if (this.items == null) {
            this.items = new HashMap<>();
        }

        int quantity = 1;
        if (this.items.containsKey(dto)) {
            quantity += this.items.get(dto);
        }

        this.items.put(dto, quantity);
    }

    public void loadAmountCarToCart(CarModelDTO dto, int amount) {
        if (this.items == null) {
            this.items = new HashMap<>();
        }

        int quantity = amount;
        if (this.items.containsKey(dto)) {
            quantity += this.items.get(dto);
        }

        this.items.put(dto, quantity);
    }

    public void removeCarFromCart(CarModelDTO dto) {
        if (this.items == null) {
            return;
        }

        if (this.items.containsKey(dto)) {
            this.items.remove(dto);
            if (this.items.isEmpty()) {
                this.items = null;
            }
        }
    }

    public void clearCart() {
        if (this.items == null) {
            return;
        }

        this.items = null;
    }

    public void updateAmountCarInCart(CarModelDTO dto, int amount) {
        if (this.items == null) {
            return;
        }

        if (this.items.containsKey(dto)) {
            this.items.put(dto, amount);
        }
    }

    public CarModelDTO getCarInCartById(int id) {
        CarModelDTO car = null;
        if (this.items != null) {
            for (CarModelDTO dto : items.keySet()) {
                if (dto.getCarModelId() == id) {
                    car = dto;
                    break;
                }
            }
        }
        return car;
    }

    public void updateCarInCart(CarModelDTO oldCar, CarModelDTO newCar) {
        if (this.items == null) {
            return;
        }

        if (items.containsKey(oldCar)) {
            int oldAmount = items.get(oldCar);
            items.remove(oldCar);
            items.put(newCar, oldAmount);
        }
    }

}
