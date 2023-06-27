package it.flaminiovilla.mechanicalappointment.repository;

import it.flaminiovilla.mechanicalappointment.model.entity.Appointment;
import it.flaminiovilla.mechanicalappointment.model.entity.OpenDay;
import it.flaminiovilla.mechanicalappointment.model.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByOpenDay(OpenDay openDay);
    List<Appointment> findAllByVehicleIn(List<Vehicle> vehicle);




}