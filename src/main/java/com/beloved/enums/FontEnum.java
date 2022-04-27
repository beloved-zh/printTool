package com.beloved.enums;

/**
 * 字体定义枚举类
 *      注意：ttf 类型字体文件正常传入路径   ttc 需要在路径后拼接 ,0
 * @author beloved
 */
public enum FontEnum {

    MSYH(0, "./static/fonts/msyh.ttc,0", "微软雅黑"),
    SIMSUN(0, "./static/fonts/simsun.ttc,0", "宋体"),
    SIMFANG(0, "./static/fonts/simfang.ttf", "仿宋"),
    SIMHEI(0, "./static/fonts/simhei.ttf", "黑体"),
    SIMKAI(0, "./static/fonts/simkai.ttf", "楷体");

    private Integer code;
    private String path;
    private String name;


    FontEnum(Integer code, String path, String name) {
        this.code = code;
        this.path = path;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
