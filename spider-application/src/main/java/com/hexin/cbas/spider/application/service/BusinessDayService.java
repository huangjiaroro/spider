package com.hexin.cbas.spider.application.service;

import com.hexin.cbas.spider.constants.DateFormatConstant;
import com.hexin.cbas.spider.dal.mapper.downcount.DimOthPubDateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/10/20
 * @version: v1.0
 */
@Service
public class BusinessDayService {

    /**
     * 缓存最近7天的交易日信息
     */
    private LinkedHashMap<LocalDate, Boolean> bussnessDayCache = new LinkedHashMap<LocalDate, Boolean>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<LocalDate, Boolean> eldest) {
            return size() > 7;
        }
    };

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    @Autowired
    DimOthPubDateMapper mapper;

    private boolean isBusinessDay(String day) {
        return mapper.selectBusinessDay(day) > 0;
    }


    /**
     * 判断是否是交易日
     *
     * @return
     */
    public boolean checkBusinessDay(LocalDate date) {
        //先从缓存获取
        Boolean isBusiness;
        lock.readLock().lock();
        try {
            isBusiness = bussnessDayCache.get(date);
        } finally {
            lock.readLock().unlock();
        }
        if (isBusiness == null) {
            lock.writeLock().lock();
            try {
                bussnessDayCache.put(date, isBusinessDay(date.format(DateTimeFormatter.ofPattern(DateFormatConstant.YEAR_MONTH_DAY))));
            } finally {
                lock.writeLock().unlock();
            }
            return checkBusinessDay(date);
        } else {
            return isBusiness;
        }
    }
}
