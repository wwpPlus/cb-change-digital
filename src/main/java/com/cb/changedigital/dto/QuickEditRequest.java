package com.cb.changedigital.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuickEditRequest {
    private Long templateVersionId;
    @NotBlank
    private String name;
    @NotBlank
    private String major;
    @NotBlank
    private String subMajor;
    @NotBlank
    private String operationContent;
    @NotBlank
    private String operationTarget;
    @NotBlank
    private String networkScope;
    @NotNull
    private Integer riskLevel;
    @NotNull
    private Integer operationLevel;
    @NotNull
    private Boolean businessInterrupted;
    private String businessImpact;
    @NotNull
    private LocalDateTime cutoverStart;
    @NotNull
    private LocalDateTime cutoverEnd;
    private LocalDateTime precheckTime;
    private LocalDateTime verifyTime;
    private LocalDateTime rollbackTime;
    private List<PersonDTO> persons;
    private List<DeviceDTO> devices;

    @Data
    public static class PersonDTO {
        private String role;
        private String department;
        private String name;
        private String phone;
    }

    @Data
    public static class DeviceDTO {
        private String deviceName;
        private String ip;
    }
}
