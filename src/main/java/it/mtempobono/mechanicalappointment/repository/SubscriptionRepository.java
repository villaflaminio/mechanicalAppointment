package it.mtempobono.mechanicalappointment.repository;

import com.flaminiovilla.obd.model.Subscription;
import com.flaminiovilla.obd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByUserAndActive(User user, boolean active);
}