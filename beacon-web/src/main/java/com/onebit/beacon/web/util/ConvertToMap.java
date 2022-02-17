package com.onebit.beacon.web.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * amis框架在【表单】回显的时候，不支持嵌套动态语法!!
 * 编写工具类将 List/Object 铺平成 Map
 * https://baidu.gitee.io/amis/zh-CN/components/form/index#%E8%A1%A8%E5%8D%95%E9%A1%B9%E6%95%B0%E6%8D%AE%E5%88%9D%E5%A7%8B%E5%8C%96
 *
 * @Author: Onebit
 * @Date: 2022/2/16
 */
public class ConvertToMap {

    /**
     * 将 List 对象转换成 Map 对象（无嵌套）
     * @param param
     * @param fieldName
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, Object>> flatList(List<T> param, List<String> fieldName) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (T t : param) {
            Map<String, Object> map = flatSingle(t, fieldName);
            result.add(map);
        }
        return result;
    }

    /**
     * 将单个对象转换成 map
     * @param t
     * @param fieldName
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> flatSingle(T t, List<String> fieldName) {
        HashMap<String, T> result = MapUtil.newHashMap(32);
        Field[] fields = ReflectUtil.getFields(t.getClass());
        for (Field field : fields) {
            if (fieldName.contains(field.getName())) {
                JSONObject jsonObject;
                Object value = ReflectUtil.getFieldValue(t, field);
                if (value instanceof String) {
                    jsonObject = JSON.parseObject((String) value);
                } else {
                    jsonObject = JSONObject.parseObject(JSON.toJSONString(value));
                }
                for (String key : jsonObject.keySet()) {
                    result.put(key, (T) jsonObject.getString(key));
                }
            }
            result.put(field.getName(), (T) ReflectUtil.getFieldValue(t, field));
        }
        return result;
    }


}
