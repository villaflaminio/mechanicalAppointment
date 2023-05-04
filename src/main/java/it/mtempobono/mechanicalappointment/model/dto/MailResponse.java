package it.mtempobono.mechanicalappointment.model.dto;

/**
 * This class represents the response of the email.
 */
public class MailResponse {
    //region Fields
    private String message;
    private Boolean status;
    //endregion

    //region Constructors
    public MailResponse() {
    }

    public MailResponse(String message, Boolean status) {
        this.message = message;
        this.status = status;
    }
    //endregion

    //region Getters & Setters methods
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
    //endregion
}
