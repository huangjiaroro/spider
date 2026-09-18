package com.hexin.cbas.spider.application.schedule;

import com.hexin.cbas.spider.application.dto.DashboardTpcDTO;
import com.hexin.cbas.spider.application.schedule.pojo.UploadResponse;
import com.hexin.cbas.spider.application.service.DataDashboardService;
import com.hexin.cbas.spider.constants.DateFormatConstant;
import com.hexin.cbas.spider.utils.HttpUtil;
import com.hexin.cbas.spider.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2022/1/6
 * @version: v1.0
 */
@Service
public class TpcUploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpcUploadService.class);
    private static final Integer MAX_RETRY = 3;

    @Value("${spider.tpc.address}")
    private String tpcAddress;
    @Value("${spider.tpc.metric-id}")
    private String metricId;
    @Value("${spider.tpc.arrival-id}")
    private String arrivalId;
    @Value("${spider.tpc.lag-id}")
    private String lagId;
    @Value("${spider.tpc.app}")
    private String app;

    @Autowired
    DataDashboardService dashboardService;

    /**
     * 每天凌晨6点上报前一天的tpc指标
     */
    public void uploadTpcTarget() {
        //上报前一天的tpc指标
        uploadTpcTarget(LocalDate.now().plusDays(-1));
    }

    public void uploadTpcTarget(LocalDate uploadDay) {

        String time = uploadDay.format(DateTimeFormatter.ofPattern(DateFormatConstant.YEAR_MONTH_DAY));
        DashboardTpcDTO dashboardTpc = dashboardService.dashboardTpcData(uploadDay.format(DateTimeFormatter.ofPattern(DateFormatConstant.YMD)));
        //上报采集准确率率
        doUpload(buildAddress(time, arrivalId, String.valueOf(new BigDecimal(dashboardTpc.getArrivalRate()).divide(new BigDecimal(100)))));
    }

    private String buildAddress(String time, String index, String indexValue) {
        return String.format(tpcAddress, time, index, indexValue);
    }

    private void doUpload(String address) {
        doUpload(address, 0);
    }

    private void doUpload(String address, int retry) {
        Map<String, String> header = new HashMap<>();
        header.put("X-Arsenal-Auth", app);
        header.put("Host", "cbas-tpc-cockpit.cbas");
        try {
            UploadResponse response = JsonUtil.parseObject(HttpUtil.httpGet(address, header), UploadResponse.class);
            if (!response.success()) {
                throw new RuntimeException(response.getMsg());
            }
        } catch (Exception e) {
            LOGGER.error("tpc target upload exception", e);
            if (retry < MAX_RETRY) {
                doUpload(address, ++retry);
            }
        }
    }

}
