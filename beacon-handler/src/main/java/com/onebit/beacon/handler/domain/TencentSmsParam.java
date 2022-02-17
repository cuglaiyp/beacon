package com.onebit.beacon.handler.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Onebit
 * @Date: 2022/1/17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TencentSmsParam {
    // api相关
    private String url;
    private String region;

    // 账号相关
    private String secretId;
    private String secretKey;
    private String smsSdkAppId;
    private String templateId;
    private String signName;

    // 渠道商相关
    private Integer supplierId;
    private String supplierName;
}
