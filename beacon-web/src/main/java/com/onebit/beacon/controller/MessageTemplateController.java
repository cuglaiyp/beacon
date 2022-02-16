package com.onebit.beacon.controller;

import cn.hutool.core.util.StrUtil;
import com.onebit.beacon.domain.MessageTemplate;
import com.onebit.beacon.service.MessageTemplateService;
import com.onebit.beacon.util.ConvertToMap;
import com.onebit.beacon.vo.BasicResultVO;
import com.onebit.beacon.vo.MessageTemplateParam;
import com.onebit.beacon.vo.MessageTemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class MessageTemplateController {

    private static final List<String> flatFieldName = Arrays.asList("messageContent");

    @Autowired
    private MessageTemplateService messageTemplateService;

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
        return BasicResultVO.success(ConvertToMap.flatSingle(messageTemplateService.queryById(id), flatFieldName));
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    public BasicResultVO queryList(MessageTemplateParam messageTemplateParam) {
        List<MessageTemplate> messageTemplates = messageTemplateService.queryList(messageTemplateParam);
        List<Map<String, Object>> maps = ConvertToMap.flatList(messageTemplates, flatFieldName);
        Long count = messageTemplateService.count();
        MessageTemplateVo messageTemplateVo = MessageTemplateVo.builder()
                .count(count)
                .rows(maps)
                .build();
        return BasicResultVO.success(messageTemplateVo);
    }
}
