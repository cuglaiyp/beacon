package com.onebit.beacon.pojo.vo;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */


import com.onebit.beacon.enums.RespStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * vo: view object 视图层对象
 * 用来定义统一的视图结果
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public final class BasicResultVO<T> {
    /**
     * 响应状态码
     */
    private String code;
    /**
     * 响应消息
     */
    private String msg;
    // 将 code、msg 用枚举封装起来

    /**
     * 响应数据
     */
    private T data;

    public BasicResultVO(RespStatusEnum status) {
        this(status, null);
    }

    public BasicResultVO(RespStatusEnum status, T data) {
        this(status, status.getMsg(), data);
    }

    public BasicResultVO(RespStatusEnum status, String msg, T data) {
        this.code = status.getCode();
        this.msg = msg;
        this.data = data;
    }

    /**
     * @return 默认成功的响应
     */
    public static BasicResultVO<Void> success() {
        return new BasicResultVO<>(RespStatusEnum.SUCCESS);
    }

    /**
     * @param msg
     * @return 自定义消息的成功响应
     */
    public static <T> BasicResultVO<T> success(String msg) {
        return new BasicResultVO<>(RespStatusEnum.SUCCESS, msg, null);
    }

    /**
     * @param msg
     * @param data
     * @param <T>
     * @return 带数据和自定义消息的成功响应
     */
    public static <T> BasicResultVO<T> success(String msg, T data) {
        return new BasicResultVO<>(RespStatusEnum.SUCCESS, msg, data);
    }


    /**
     * @return 默认失败的响应
     */
    public static BasicResultVO<Void> fail() {
        return new BasicResultVO<>(RespStatusEnum.FAIL);
    }

    /**
     * @param msg
     * @return 自定义消息的失败响应
     */
    public static <T> BasicResultVO<T> fail(String msg) {
        return new BasicResultVO<>(RespStatusEnum.FAIL, msg, null);
    }


}
