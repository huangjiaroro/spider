package com.hexin.cbas.spider.application.assembler;

import com.hexin.cbas.spider.application.dto.DashboardAlertDTO;
import com.hexin.cbas.spider.core.pojo.AlertMessage;
import com.hexin.cbas.spider.dal.po.spider.AlertRecordPO;
import com.hexin.cbas.spider.dal.po.spider.DashboardAlertPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/14
 * @version: v1.0
 */
@Mapper(componentModel = "spring")
public interface AlertAssembler {
    /**
     * @param alertMessage
     * @return
     */
    AlertRecordPO toPO(AlertMessage alertMessage);

    /**
     * @param dashboardAlertPO
     * @return
     */
    @Mapping(target = "alertStart", expression = "java(com.hexin.cbas.spider.utils.DateUtil.format(dashboardAlertPO.getAlertStart(),\"yyyy-MM-dd HH:mm:ss\"))")
    @Mapping(target = "alertEnd", expression = "java(com.hexin.cbas.spider.utils.DateUtil.format(dashboardAlertPO.getAlertEnd(),\"yyyy-MM-dd HH:mm:ss\"))")
    DashboardAlertDTO toDTO(DashboardAlertPO dashboardAlertPO);
}
