package it.mtempobono.mechanicalappointment.repository.projection;

import com.flaminiovilla.obd.model.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * A Projection for the {@link User} entity
 */
public interface UserMe {
    Long getId();

    String getName();

    String getEmail();

    String getImageUrl();

    List<SubscriptionInfo> getSubscriptions();

    List<DeviceInfo> getSharedDevice();

    FlespiTokenInfo getToken();

    Collection<RoleInfo> getRoles();



    /**
     * A Projection for the {@link Subscription} entity
     */
    interface SubscriptionInfo {
        Long getId();

        String getName();

        String getDescription();

        LocalDateTime getStartDateSubscription();

        LocalDateTime getEndDateSubscription();

        int getSubscriptionType();

        Boolean getActive();

        DeviceInfo getDevice();

        /**
         * A Projection for the {@link Device} entity
         */
        interface DeviceInfo {
            Long getId();

            String getModel();

            CarInfo getCarInstance();

            /**
             * A Projection for the {@link Car} entity
             */
            interface CarInfo {
                Long getId();

                String getModel();

                String getYearTo();

                String getProgramNumber();

                String getDateFrom();

                List<CarFunctionInfo> getCarFunctions();

                /**
                 * A Projection for the {@link CarFunction} entity
                 */
                interface CarFunctionInfo {
                    Long getId();

                    String getName();
                }
            }
        }
    }

    /**
     * A Projection for the {@link Device} entity
     */
    interface DeviceInfo {
        Long getId();

        String getModel();

        String getVin();

        String getSerialNumber();

        String getImei();

        String getSim();

        CarInfo getCarInstance();

        /**
         * A Projection for the {@link Car} entity
         */
        interface CarInfo {
            Long getId();

            String getModel();

            String getYearTo();

            String getProgramNumber();

            String getDateFrom();

            List<CarFunctionInfo> getCarFunctions();

            /**
             * A Projection for the {@link CarFunction} entity
             */
            interface CarFunctionInfo {
                Long getId();

                String getName();
            }
        }
    }

    /**
     * A Projection for the {@link FlespiToken} entity
     */
    interface FlespiTokenInfo {
        Long getId();

        String getToken_key();
    }

    /**
     * A Projection for the {@link Role} entity
     */
    interface RoleInfo {
        Long getId();

        String getName();
    }
}