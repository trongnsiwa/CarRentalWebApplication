/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.user;

/**
 *
 * @author TrongNS
 */
public class CreateAnAccountError {
    private String duplicateEmail;
    private String invalidEmail;
    private String emptyPassword;
    private String confirmNotMatchPassword;
    private String emptyFirstName;
    private String emptyLastName;
    private String invalidPhone;
    private String emptyAddress;

    public CreateAnAccountError() {
    }

    public CreateAnAccountError(String duplicateEmail, String invalidEmail, String emptyPassword, String confirmNotMatchPassword, String emptyFirstName, String emptyLastName, String invalidPhone, String emptyAddress) {
        this.duplicateEmail = duplicateEmail;
        this.invalidEmail = invalidEmail;
        this.emptyPassword = emptyPassword;
        this.confirmNotMatchPassword = confirmNotMatchPassword;
        this.emptyFirstName = emptyFirstName;
        this.emptyLastName = emptyLastName;
        this.invalidPhone = invalidPhone;
        this.emptyAddress = emptyAddress;
    }

    

    /**
     * @return the invalidEmail
     */
    public String getInvalidEmail() {
        return invalidEmail;
    }

    /**
     * @param invalidEmail the invalidEmail to set
     */
    public void setInvalidEmail(String invalidEmail) {
        this.invalidEmail = invalidEmail;
    }

    /**
     * @return the confirmNotMatchPassword
     */
    public String getConfirmNotMatchPassword() {
        return confirmNotMatchPassword;
    }

    /**
     * @param confirmNotMatchPassword the confirmNotMatchPassword to set
     */
    public void setConfirmNotMatchPassword(String confirmNotMatchPassword) {
        this.confirmNotMatchPassword = confirmNotMatchPassword;
    }

    /**
     * @return the duplicateEmail
     */
    public String getDuplicateEmail() {
        return duplicateEmail;
    }

    /**
     * @param duplicateEmail the duplicateEmail to set
     */
    public void setDuplicateEmail(String duplicateEmail) {
        this.duplicateEmail = duplicateEmail;
    }

    /**
     * @return the emptyPassword
     */
    public String getEmptyPassword() {
        return emptyPassword;
    }

    /**
     * @param emptyPassword the emptyPassword to set
     */
    public void setEmptyPassword(String emptyPassword) {
        this.emptyPassword = emptyPassword;
    }

    /**
     * @return the emptyFirstName
     */
    public String getEmptyFirstName() {
        return emptyFirstName;
    }

    /**
     * @param emptyFirstName the emptyFirstName to set
     */
    public void setEmptyFirstName(String emptyFirstName) {
        this.emptyFirstName = emptyFirstName;
    }

    /**
     * @return the emptyLastName
     */
    public String getEmptyLastName() {
        return emptyLastName;
    }

    /**
     * @param emptyLastName the emptyLastName to set
     */
    public void setEmptyLastName(String emptyLastName) {
        this.emptyLastName = emptyLastName;
    }

    /**
     * @return the invalidPhone
     */
    public String getInvalidPhone() {
        return invalidPhone;
    }

    /**
     * @param invalidPhone the invalidPhone to set
     */
    public void setInvalidPhone(String invalidPhone) {
        this.invalidPhone = invalidPhone;
    }

    /**
     * @return the emptyAddress
     */
    public String getEmptyAddress() {
        return emptyAddress;
    }

    /**
     * @param emptyAddress the emptyAddress to set
     */
    public void setEmptyAddress(String emptyAddress) {
        this.emptyAddress = emptyAddress;
    }
    
    
}
