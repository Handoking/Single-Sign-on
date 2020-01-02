package com.handoking.pojo;


import lombok.Data;

/**
 * @ClassName ClientInfoVo
 * @Description TODO
 * @Author Handoking
 * @Date 2019/12/30 11:59
 **/
@Data
public class ClientInfoVo {
    /**客户端登出地址*/
    private String clientUrl;
    /**客户端对应的的jessionId*/
    private  String jessionId;
}
