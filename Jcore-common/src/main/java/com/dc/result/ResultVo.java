package com.dc.result;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;

/**
 * 统一返回值类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVo<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 跟踪ID
     */
    private String traceId;

    /**
     * 成功
     */
    public static <T> ResultVo<T> success() {
        return new ResultVo<>(200, "success", null, System.currentTimeMillis(), MDC.get("traceId"));
    }

    /**
     * 成功带数据
     */
    public static <T> ResultVo<T> success(T data) {
        return new ResultVo<>(200, "success", data, System.currentTimeMillis(), MDC.get("traceId"));
    }

    /**
     * 成功带消息
     */
    public static <T> ResultVo<T> success(String message, T data) {
        return new ResultVo<>(200, message, data, System.currentTimeMillis(), MDC.get("traceId"));
    }

    /**
     * 失败
     */
    public static <T> ResultVo<T> error() {
        return new ResultVo<>(500, "error", null, System.currentTimeMillis(), MDC.get("traceId"));
    }

    /**
     * 失败带消息
     */
    public static <T> ResultVo<T> error(String message) {
        return new ResultVo<>(500, message, null, System.currentTimeMillis(), MDC.get("traceId"));
    }

    /**
     * 失败带状态码和消息
     */
    public static <T> ResultVo<T> error(Integer code, String message) {
        return new ResultVo<>(code, message, null, System.currentTimeMillis(), MDC.get("traceId"));
    }

    /**
     * 失败带状态码、消息和数据
     */
    public static <T> ResultVo<T> error(Integer code, String message, T data) {
        return new ResultVo<>(code, message, data, System.currentTimeMillis(), MDC.get("traceId"));
    }

    /**
     * 是否成功
     */
    public boolean isSuccess() {
        return code != null && code.equals(200);
    }

    /**
     * 是否失败
     */
    public boolean isError() {
        return !isSuccess();
    }
}
