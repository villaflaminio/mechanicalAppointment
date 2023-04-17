package it.mtempobono.mechanicalappointment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * A DTO for the {@link User} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMeDto implements Serializable {
    private  Long id;
    private  String name;
    @Email
    private  String email;
    private  String imageUrl;
    private  List<DeviceDto> sharedDevice;
    private  FlespiTokenDto token;
    private  Collection<RoleDto> roles;

    /**
     * A DTO for the {@link Device} entity
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeviceDto implements Serializable {
        private Long id;
        private String model;
        private CarDto carInstance;

        /**
         * A DTO for the {@link Car} entity
         */
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class CarDto implements Serializable {
            private Long id;
            private String model;
            private String yearTo;
            private String programNumber;
            private String dateFrom;
            private List<CarFunctionDto> carFunctions;

            /**
             * A DTO for the {@link CarFunction} entity
             */
            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class CarFunctionDto implements Serializable {
                private Long id;
                private String name;
            }
        }
    }

    /**
     * A DTO for the {@link FlespiToken} entity
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FlespiTokenDto implements Serializable {
        private Long id;
        private String token_key;
    }

    /**
     * A DTO for the {@link Role} entity
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleDto implements Serializable {
        private Long id;
        private String name;
    }
}