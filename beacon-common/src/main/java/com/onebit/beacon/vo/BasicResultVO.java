package com.onebit.beacon.vo;

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
    private String status;
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
        this.status = status.getCode();
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
     * 自定义消息的成功响应
     *
     * @param msg
     * @return
     */
    public static <T> BasicResultVO<T> success(String msg) {
        return new BasicResultVO<>(RespStatusEnum.SUCCESS, msg, null);
    }

    /**
     * 带数据的成功响应
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BasicResultVO<T> success(T data) {
        return new BasicResultVO<>(RespStatusEnum.SUCCESS, data);
    }

    // 成功和失败定义的一些默认方法不一样，是因为成功的状态只有一种，我们可以全部列出来；而失败的状态太多，只能让用户传

    /**
     * @return 默认失败的响应
     */
    public static <T> BasicResultVO<T> fail() {
        return new BasicResultVO<>(RespStatusEnum.FAIL);
    }

    /**
     * 自定义失败状态的响应
     *
     * @param status
     * @return
     */
    public static <T> BasicResultVO<T> fail(RespStatusEnum status) {
        return new BasicResultVO<>(status);
    }


}
