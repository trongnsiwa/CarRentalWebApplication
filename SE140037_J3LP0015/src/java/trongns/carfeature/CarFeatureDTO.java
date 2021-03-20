/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.carfeature;

import java.io.Serializable;

/**
 *
 * @author TrongNS
 */
public class CarFeatureDTO implements Serializable{
    private int carModelId;
    private int featureId;

    public CarFeatureDTO() {
    }

    public CarFeatureDTO(int carModelId, int featureId) {
        this.carModelId = carModelId;
        this.featureId = featureId;
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
     * @return the featureId
     */
    public int getFeatureId() {
        return featureId;
    }

    /**
     * @param featureId the featureId to set
     */
    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }
    
    
}
