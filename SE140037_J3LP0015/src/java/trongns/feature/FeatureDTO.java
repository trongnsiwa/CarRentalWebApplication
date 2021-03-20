/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.feature;

import java.io.Serializable;

/**
 *
 * @author TrongNS
 */
public class FeatureDTO implements Serializable {
    private int featureId;
    private String featureName;

    public FeatureDTO() {
    }

    public FeatureDTO(int featureId, String featureName) {
        this.featureId = featureId;
        this.featureName = featureName;
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

    /**
     * @return the featureName
     */
    public String getFeatureName() {
        return featureName;
    }

    /**
     * @param featureName the featureName to set
     */
    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }
    
    
}
