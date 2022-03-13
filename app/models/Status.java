package models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * POJO class for user statuses
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Status {
    private Boolean paymentVerified;
    private Boolean emailVerified;
    private Boolean depositMade;
    private Boolean profileComplete;
    private Boolean phoneVerified;
    private Boolean identityVerified;
    private Boolean facebookConnected;
    private Boolean freelancerVerifiedUser;
    private Boolean linkedinConnected;

    public Boolean getPaymentVerified() {
        return paymentVerified;
    }

    public void setPaymentVerified(Boolean paymentVerified) {
        this.paymentVerified = paymentVerified;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Boolean getDepositMade() {
        return depositMade;
    }

    public void setDepositMade(Boolean depositMade) {
        this.depositMade = depositMade;
    }

    public Boolean getProfileComplete() {
        return profileComplete;
    }

    public void setProfileComplete(Boolean profileComplete) {
        this.profileComplete = profileComplete;
    }

    public Boolean getPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(Boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public Boolean getIdentityVerified() {
        return identityVerified;
    }

    public void setIdentityVerified(Boolean identityVerified) {
        this.identityVerified = identityVerified;
    }

    public Boolean getFacebookConnected() {
        return facebookConnected;
    }

    public void setFacebookConnected(Boolean facebookConnected) {
        this.facebookConnected = facebookConnected;
    }

    public Boolean getFreelancerVerifiedUser() {
        return freelancerVerifiedUser;
    }

    public void setFreelancerVerifiedUser(Boolean freelancerVerifiedUser) {
        this.freelancerVerifiedUser = freelancerVerifiedUser;
    }

    public Boolean getLinkedinConnected() {
        return linkedinConnected;
    }

    public void setLinkedinConnected(Boolean linkedinConnected) {
        this.linkedinConnected = linkedinConnected;
    }
}
