package it.flaminiovilla.mechanicalappointment.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class FilterDay implements Serializable {

    //region Fields
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Long garageId;
    //endregion

    //region Constructors
    public FilterDay() {
    }

    public FilterDay(LocalDate dateFrom, LocalDate dateTo, Long garageId) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.garageId = garageId;
    }
    //endregion

    //region Getters & Setters

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public Long getGarageId() {
        return garageId;
    }

    public void setGarageId(Long garageId) {
        this.garageId = garageId;
    }
    //endregion
}
