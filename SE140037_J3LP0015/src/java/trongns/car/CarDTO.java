/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.car;

import java.io.Serializable;

/**
 *
 * @author TrongNS
 */
public class CarDTO implements Serializable{
    private int carModelID;
    private String licensePlates;

    public CarDTO() {
    }

    public CarDTO(int carModelID, String licensePlates) {
        this.carModelID = carModelID;
        this.licensePlates = licensePlates;
    }

    /**
     * @return the carModelID
     */
    public int getCarModelID() {
        return carModelID;
    }

    /**
     * @param carModelID the carModelID to set
     */
    public void setCarModelID(int carModelID) {
        this.carModelID = carModelID;
    }

    /**
     * @return the licensePlates
     */
    public String getLicensePlates() {
        return licensePlates;
    }

    /**
     * @param licensePlates the licensePlates to set
     */
    public void setLicensePlates(String licensePlates) {
        this.licensePlates = licensePlates;
    }
}
