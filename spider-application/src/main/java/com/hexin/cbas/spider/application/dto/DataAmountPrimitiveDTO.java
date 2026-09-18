package com.hexin.cbas.spider.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
public class DataAmountPrimitiveDTO {

    @NotNull(message = "channelId不能为空")
    @PositiveOrZero(message = "channelId必须是正数或零")
    private Long channelId;

    @NotBlank(message = "instanceId不能为空")
    private String instanceId;

    @NotBlank(message = "pDate不能为空")
    @Size(min = 8, max = 8, message = "pDate格式不符合yyyyMMdd")
    @JsonProperty(value = "pDate")
    private String dateStr;

    @NotNull(message = "logAmount不能为空")
    @PositiveOrZero(message = "logAmount必须是正数或零")
    private Long logAmount;
}