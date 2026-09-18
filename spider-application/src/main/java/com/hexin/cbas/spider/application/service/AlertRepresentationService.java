package com.hexin.cbas.spider.application.service;

import com.alibaba.fastjson.JSON;
import com.hexin.cbas.spider.application.assembler.AlertAssembler;
import com.hexin.cbas.spider.application.dto.DashboardAlertDTO;
import com.hexin.cbas.spider.application.dto.DashboardAlertResultDTO;
import com.hexin.cbas.spider.config.SpiderConfig;
import com.hexin.cbas.spider.constants.DateFormatConstant;
import com.hexin.cbas.spider.constants.MarkConstant;
import com.hexin.cbas.spider.dal.mapper.spider.AlertRecordMapper;
import com.hexin.cbas.spider.dal.mapper.spider.PassAlertInstanceMapper;
import com.hexin.cbas.spider.dal.po.spider.AlertRecordPO;
import com.hexin.cbas.spider.dal.po.spider.DashboardAlertPO;
import com.hexin.cbas.spider.utils.HttpUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/19
 * @version: v1.0
 */
@Service
@SuppressWarnings("all")
public class AlertRepresentationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertRepresentationService.class);


    private static final String PATTERN_START = "您负责的";

    private static final String ALERT_CONTENT = "%s节点,指标%s异常,当前值:%s,链路:%s";
    private static final String ALERT_CONTENT_WITH_INSTANCE = "%s节点%s实例,指标%s异常,当前值:%s,链路:%s";

    private static final String ALERT_PATTERN = "您负责的\\{(.+)\\}链路->\\{(.+)\\}节点:\\{(.+)\\}指标异常  metricKey\\{(.+)\\} 当前值:\\{(.+)\\} 告警阈值:\\{(.+)\\} 请关注链路情况！";
    private static final String ALERT_PATTERN_WITH_INSTANCE = "您负责的\\{(.+)\\}链路->\\{(.+)\\}节点\\{(.+)\\}实例:\\{(.+)\\}指标异常  metricKey\\{(.+)\\} 当前值:\\{(.+)\\} 告警阈值:\\{(.+)\\} 请关注链路情况！";

    private static final String DEFUALT_ALLERT_INSTANCE = "Qr16CmRbBlOmQDftfRrSD";

    @Autowired
    AlertRecordMapper alertRecordMapper;

    @Autowired
    SpiderConfig config;

    @Autowired
    AlertAssembler alertAssembler;

    @Autowired
    PassAlertInstanceMapper passAlertInstanceMapper;

    /**
     * vanish 告警
     */
    public void alertVanish(String alertPerson, String alertContent) {
        try {
            LOGGER.info("开始发送vanish告警");
            String vanishUrl = config.getVanishAddress() + "?to=%s&message=%s";
            HttpUtil.httpGet(String.format(vanishUrl, URLEncoder.encode(alertPerson, "utf-8")
                    , URLEncoder.encode(alertContent, "utf-8")));
        } catch (Exception e) {
            LOGGER.error("alert vanish error:", e);
        }

    }

    public void alertPaas(String alertInstance, String alertContent) {
        doPaasAlter(alertInstance, alertContent, 2);
    }

    public void recoverPaas(String alertInstance, String alertContent) {
        doPaasAlter(alertInstance, alertContent, 0);
    }

    private void doPaasAlter(String alertInstance, String alertContent, Integer status) {
        String api = SpiderConfig.MONITOR_API;
        try {
            Map<String, String> params = new HashMap<>();
            params.put("mode", "agent");
            params.put("JSONDATA", JSON.toJSONString(new PaasAlert(SpiderConfig.HOST_NAME, "spider", alertContent)
                    .buildMetrics(alertInstance, status)));
            LOGGER.info("开始发送paas告警:  api:{} ,params:{} ", api, JSON.toJSONString(params));
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            String result = HttpUtil.httpPost(api, params, headers);
            LOGGER.info("发送paas告警结束  result:{} ", result);
        } catch (IOException e) {
            LOGGER.error("api:{} ,data:{} ,发送paas告警失败：", api, e);
        }


    }

    public void alert() {
        try {
            if (LocalTime.now().isBefore(config.getStartTime()) || LocalTime.now().isAfter(config.getEndTime())) {
                return;
            }

            List<AlertRecordPO> alertRecords = alertRecordMapper.selectUnAlertRecord();
            if (CollectionUtils.isEmpty(alertRecords)) {
                List<String> recoverInstance = passAlertInstanceMapper.selectAll();
                recoverInstance.forEach(p -> reoverPassAlert(p));
                return;
            }

            AlertHelper helper = new AlertHelper();
            alertRecords.forEach(record -> {
                if (StringUtils.isNotEmpty(record.getPaasAlertInstance())) {
                    helper.appendPaas(record.getAlertPerson(), record.getAlertContent());
                } else if (StringUtils.isNotEmpty(record.getChannelName())) {
                    helper.appendPaas(DEFUALT_ALLERT_INSTANCE, record.getAlertContent());
                } else {
                    helper.appendVanish(record.getAlertPerson(), record.getAlertContent());
                }
            });
            helper.alert();
            Set<String> unHelthInstance = helper.getPaasInstance();
            List<String> unHelthBeforeTimeInstance = passAlertInstanceMapper.selectAll();
            //对比实例id进行对应的恢复通知
            unHelthBeforeTimeInstance.forEach(i -> {
                if (!unHelthInstance.contains(i)) {
                    reoverPassAlert(i);
                } else {
                    //原本就存在还未恢复
                    unHelthInstance.remove(i);
                }
            });
            //剩下的是本次新增的告警实例
            unHelthInstance.forEach(p -> {
                passAlertInstanceMapper.insert(p);
            });

            alertRecordMapper.batchUpdateAlertStatus(alertRecords.stream().map(AlertRecordPO::getId).collect(Collectors.toList()));
        } catch (Exception e) {
            LOGGER.error("alert exception", e);
        }

    }


    private void reoverPassAlert(String paasAlertInstance) {
        recoverPaas(paasAlertInstance, String.format("告警实例%s 日志采集恢复正常", paasAlertInstance));
        //本次校验不存在了，需要发送恢复通知
        passAlertInstanceMapper.deleteByPrimaryKey(paasAlertInstance);
    }

    /**
     * 清除已告警记录
     */
    public void clearAlertedRecord() {
        Date alertDate = DateUtils.addDays(new Date(), -7);
        alertRecordMapper.clearAlertedRecord(alertDate);
    }

    /**
     * 获取运维告警信息
     *
     * @param persons
     */
    @Transactional(rollbackFor = Exception.class)
    public List<String> opsAlertInfo(String persons) {
        List<AlertRecordPO> recordPOS = alertRecordMapper.selectOpsAlertInfo(Arrays.asList(persons.split(MarkConstant.SPLIT)));
        for (AlertRecordPO recordPO : recordPOS) {
            alertRecordMapper.updateOpsAlertInfo(recordPO.getId());
        }
        return recordPOS.stream().map(p -> matchAlertInfo(p.getAlertContent())).collect(Collectors.toList());
    }

    private String matchAlertInfo(String content) {

        if (!content.startsWith(PATTERN_START)) {
            return content;
        }
        try {
            Pattern compile = Pattern.compile(ALERT_PATTERN);
            Matcher matcher = compile.matcher(content);
            if (matcher.find()) {
                return String.format(ALERT_CONTENT, matcher.group(2), matcher.group(4), matcher.group(5), matcher.group(1));
            }

            compile = Pattern.compile(ALERT_PATTERN_WITH_INSTANCE);
            matcher = compile.matcher(content);
            if (matcher.find()) {
                return String.format(ALERT_CONTENT_WITH_INSTANCE,
                        matcher.group(2), matcher.group(3), matcher.group(5), matcher.group(6), matcher.group(1));
            }
        } catch (Exception e) {
            LOGGER.error("matchAlertInfo exception", e);
        }
        return "";
    }

    public DashboardAlertResultDTO dashboardAlertTop(String date) {
        DashboardAlertResultDTO result = new DashboardAlertResultDTO();
        try {
            Date startDate = DateUtils.parseDate(date, DateFormatConstant.YMD);
            Date endDate = DateUtils.addDays(startDate, 1);
            List<DashboardAlertPO> dashboardAlerts = alertRecordMapper.dashboardAlert(startDate, endDate);
            List<DashboardAlertDTO> alerts = dashboardAlerts.stream()
                    .map(p -> alertAssembler.toDTO(p.computeAlertDuration())).collect(Collectors.toList());
            result.setAlerts(alerts);
            result.setTop(alerts.size() > 10 ? alerts.subList(0, 9).stream().map(DashboardAlertDTO::getChannelName)
                    .collect(Collectors.toList()) : alerts.stream().map(DashboardAlertDTO::getChannelName).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    class AlertHelper {
        private final Map<String, StringBuilder> vanishAlertMap = new HashMap<>();
        private final Map<String, StringBuilder> paasAlertMap = new HashMap<>();

        public void appendVanish(String person, String content) {
            append(vanishAlertMap, person, content);
        }

        public void appendPaas(String person, String content) {
            content = content.replaceAll(":", "").replaceAll("\\{", "").replaceAll("\\}", "");
            append(paasAlertMap, person, content);
        }

        public void alert() {
            this.doAlert(vanishAlertMap, (AlertRepresentationService.this::alertVanish));
            this.doAlert(paasAlertMap, (AlertRepresentationService.this::alertPaas));
        }

        public Set<String> getPaasInstance() {
            return paasAlertMap.keySet();
        }

        private void doAlert(Map<String, StringBuilder> alertMap, BiConsumer<String, String> consumer) {
            alertMap.forEach((person, content) -> {
                if (StringUtils.isNotEmpty(content.toString())) {
                    consumer.accept(person, content.toString());
                }
            });
        }

        private void append(Map<String, StringBuilder> alertMap, String person, String content) {
            alertMap.putIfAbsent(person, new StringBuilder());
            alertMap.get(person).append(";").append(content);
        }

    }

    @Data
    class PaasAlert {
        private String hostname;
        private String servicename;
        private String output;
        private List<Object> metrics;

        public PaasAlert(String hostname, String servicename, String output) {
            this.hostname = hostname;
            this.servicename = servicename;
            this.output = output;
        }

        public PaasAlert buildMetrics(String alertUuid, Integer exitStatus) {
            Map<String, Object> data = new HashMap<>();
            data.put("tag", new HashMap<String, String>() {{
                put("alert_uuid", alertUuid);
            }});
            data.put("data", new HashMap<String, Integer>() {{
                put("_exit_status", exitStatus);
            }});
            this.metrics = Collections.singletonList(data);
            return this;
        }

    }


}
