package com.onebit.beacon.cron.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.csv.CsvReadConfig;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRowHandler;
import cn.hutool.core.text.csv.CsvUtil;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Onebit
 * @Date: 2022/2/18
 */
@Slf4j
public class ReadFileUtil {

    public static final String RECEIVER_KEY = "userId";

    /**
     * 读取csv文件，每读取一行都会调用 csvRowHandler 对应的方法
     *
     * @param path
     * @param csvRowHandler
     */
    public static void getCsvRow(String path, CsvRowHandler csvRowHandler) {
        try {
            // 把首行当做是标题，获取reader
            CsvReader reader = CsvUtil.getReader(new FileReader(path),
                    new CsvReadConfig().setContainsHeader(true));
            reader.read(csvRowHandler);
        } catch (Exception e) {
            log.error("ReadFileUtils#getCsvRow fail!{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 从文件的每一行数据获取到params信息
     * [{key:value},{key:value}]
     * @param fieldMap
     * @return
     */
    public static HashMap<String, String> getParamFromLine(Map<String, String> fieldMap) {
        HashMap<String, String> params = MapUtil.newHashMap();
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            if (!ReadFileUtil.RECEIVER_KEY.equals(entry.getKey())) {
                params.put(entry.getKey(), entry.getValue());
            }
        }
        return params;
    }
}
