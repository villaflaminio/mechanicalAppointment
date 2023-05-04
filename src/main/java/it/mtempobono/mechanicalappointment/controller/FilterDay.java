package it.mtempobono.mechanicalappointment.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDay implements Serializable {

    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Long garageId;

}
