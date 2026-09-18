package com.hexin.cbas.spider.application.schedule;

import com.hexin.cbas.spider.application.schedule.pojo.UploadResponse;
import com.hexin.cbas.spider.constants.DateFormatConstant;
import com.hexin.cbas.spider.dal.mapper.spider.DataAmountSummaryMapper;
import com.hexin.cbas.spider.utils.HttpUtil;
import com.hexin.cbas.spider.utils.JsonUtil;
import com.hexin.cbas.spider.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/12/16
 * @version: v1.0
 */
@Component
public class DataUploadService {

    private static final Integer MAX_RETRY = 3;

    @Value("${spider.upload.address}")
    private String uploadAddress;
    @Value("${spider.upload.index-code}")
    private String indexCode;
    @Value("${spider.upload.email}")
    private String email;

    @Autowired
    DataAmountSummaryMapper mapper;


    public void dataUpload(LocalDate date) {
        String uploadDate = date.format(DateTimeFormatter.ofPattern(DateFormatConstant.YEAR_MONTH_DAY));
        Map<String, String> parameter = new HashMap<>();
        parameter.put("indexCode", indexCode);
        parameter.put("dataDate", uploadDate);
        parameter.put("indexValue", mapper.uploadCount(uploadDate).toString());
        Map<String, String> header = new HashMap<>();
        header.put("cbas_email", email);
        header.put("Content-Type", "application/json");
        uploadWithRetry(JsonUtil.parseString(parameter), header, 0);
    }

    public void uploadWithRetry(String parameter, Map<String, String> header, int retry) {
        try {
            UploadResponse uploadResponse = JsonUtil.parseObject(HttpUtil.httpPostJson(uploadAddress, parameter, header), UploadResponse.class);
            if (!uploadResponse.success()) {
                if (retry < MAX_RETRY) {
                    LogUtil.error("upload data exception starting retry" + uploadResponse.getStatusMsg());
                    uploadWithRetry(parameter, header, ++retry);
                }
                uploadWithRetry(parameter, header, ++retry);
            }
        } catch (Exception e) {
            if (retry < MAX_RETRY) {
                LogUtil.error("upload data exception starting retry", e);
                uploadWithRetry(parameter, header, ++retry);
            }
            LogUtil.error("upload data exception upload failed", e);
        }

    }
}
