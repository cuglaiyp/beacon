package com.onebit.beacon.web.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.onebit.beacon.api.domain.MessageParam;
import com.onebit.beacon.support.domain.MessageTemplate;
import com.onebit.beacon.api.domain.SendRequest;
import com.onebit.beacon.api.domain.SendResponse;
import com.onebit.beacon.api.enums.BusinessCode;
import com.onebit.beacon.common.enums.RespStatusEnum;
import com.onebit.beacon.web.service.MessageTemplateService;
import com.onebit.beacon.api.service.SendService;
import com.onebit.beacon.web.util.ConvertToMap;
import com.onebit.beacon.common.vo.BasicResultVO;
import com.onebit.beacon.web.vo.MessageTemplateParam;
import com.onebit.beacon.web.vo.MessageTemplateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */

@RestController
@RequestMapping("/messageTemplate")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
@Slf4j
public class MessageTemplateController {

    private static final List<String> FLAT_FIELD_NAME = Arrays.asList("messageContent");

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private SendService sendService;

    @Value("${beacon.business.upload.crown.path}")
    private String dataPath;

    /**
     * 插入，如果存在则修改
     */
    @PostMapping("/save")
    public BasicResultVO saveOrUpdate(@RequestBody MessageTemplate messageTemplate) {
        MessageTemplate info = messageTemplateService.saveOrUpdate(messageTemplate);
        return BasicResultVO.success(info);

    }

    /**
     * 根据 id 复制
     */
    @PostMapping("copy/{id}")
    public BasicResultVO copyById(@PathVariable("id") Long id) {
        messageTemplateService.copy(id);
        return BasicResultVO.success();
    }

    /**
     * 根据 id 删除
     */
    @DeleteMapping("/delete/{id}")
    public BasicResultVO deleteById(@PathVariable("id") String id) {
        if (StrUtil.isNotBlank(id)) {
            List<Long> ids = Arrays.stream(id.split(StrUtil.COMMA)).map(s -> Long.valueOf(s)).collect(Collectors.toList());
            messageTemplateService.deleteByIds(ids);
            return BasicResultVO.success();
        }
        return BasicResultVO.fail();
    }

    /**
     * 根据 id 查询
     */
    @GetMapping("/query/{id}")
    public BasicResultVO queryById(@PathVariable("id") Long id) {
        return BasicResultVO.success(ConvertToMap.flatSingle(messageTemplateService.queryById(id), FLAT_FIELD_NAME));
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    public BasicResultVO queryList(MessageTemplateParam messageTemplateParam) {
        List<MessageTemplate> messageTemplates = messageTemplateService.queryList(messageTemplateParam);
        List<Map<String, Object>> maps = ConvertToMap.flatList(messageTemplates, FLAT_FIELD_NAME);
        Long count = messageTemplateService.count();
        MessageTemplateVo messageTemplateVo = MessageTemplateVo.builder()
                .count(count)
                .rows(maps)
                .build();
        return BasicResultVO.success(messageTemplateVo);
    }

    /**
     * 测试发送接口
     */
    @PostMapping("test")
    public BasicResultVO test(@RequestBody MessageTemplateParam messageTemplateParam) {
        Map<String, String> variables = JSON.parseObject(messageTemplateParam.getMsgContent(), Map.class);
        MessageParam messageParam = MessageParam.builder().receiver(messageTemplateParam.getReceiver()).variables(variables).build();
        SendRequest sendRequest = SendRequest.builder().code(BusinessCode.COMMON_SEND.getCode()).messageTemplateId(messageTemplateParam.getId()).messageParam(messageParam).build();
        SendResponse response = sendService.send(sendRequest);
        if (response.getCode() != RespStatusEnum.SUCCESS.getCode()) {
            return BasicResultVO.fail(response.getMsg());
        }
        return BasicResultVO.success(response);
    }

    /**
     * 启动模板的定时任务
     */
    @PostMapping("start/{id}")
    public BasicResultVO start(@RequestBody @PathVariable("id") Long id) {
        return messageTemplateService.startCronTask(id);
    }

    /**
     * 暂停模板的定时任务
     */
    @PostMapping("stop/{id}")
    public BasicResultVO stop(@RequestBody @PathVariable("id") Long id) {
        return messageTemplateService.stopCronTask(id);
    }

    /**
     * 上传人群文件
     */
    @PostMapping("upload")
    public BasicResultVO upload(@RequestParam("file") MultipartFile file) {
        String filePath = new StringBuilder(dataPath)
                .append(IdUtil.fastSimpleUUID())
                .append(file.getOriginalFilename())
                .toString();
        try {
            File localFile = new File(filePath);
            if (!localFile.exists()) {
                localFile.mkdirs();
            }
            file.transferTo(localFile);


        } catch (Exception e) {
            log.error("MessageTemplateController#upload fail! e:{},params{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(file));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR);
        }
        return BasicResultVO.success(MapUtil.of(new String[][]{{"value", filePath}}));
    }
}
