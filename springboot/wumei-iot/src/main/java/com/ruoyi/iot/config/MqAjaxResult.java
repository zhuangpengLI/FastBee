package com.ruoyi.iot.config;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;

/**
 * 操作消息提醒
 * 
 * @author ruoyi
 */
public class MqAjaxResult extends AjaxResult
{
    private static final long serialVersionUID = 1L;
    
    
    private String fun;
    private String au;
    private String gatewaySn;
    
    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public MqAjaxResult()
    {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     * 
     * @param code 状态码
     * @param msg 返回内容
     */
    public MqAjaxResult(int code, String msg,String fun,String au,String gatewaySn)
    {
    	this.au = au;
    	this.fun = fun;
    	this.gatewaySn = gatewaySn;
    	super.put("au", au);
    	super.put("fun", fun);
    	super.put("gatewaySn", gatewaySn);
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public MqAjaxResult(int code, String msg, Object data,String fun,String au,String gatewaySn)
    {
    	this.au = au;
    	this.fun = fun;
    	this.gatewaySn = gatewaySn;
    	super.put("au", au);
    	super.put("fun", fun);
    	super.put("gatewaySn", gatewaySn);
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data))
        {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功数据
     * 
     * @return 成功消息
     */
    public static MqAjaxResult success(Object data,String fun,String au,String gatewaySn)
    {
        return MqAjaxResult.success("操作成功", data, fun, au, gatewaySn);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static MqAjaxResult success(String msg, Object data,String fun,String au,String gatewaySn)
    {
        return new MqAjaxResult(HttpStatus.SUCCESS, msg, data, fun, au, gatewaySn);
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @return 警告消息
     */
    public static MqAjaxResult error(String msg,String fun,String au,String gatewaySn)
    {
        return MqAjaxResult.error(msg, null, fun, au, gatewaySn);
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static MqAjaxResult error(String msg, Object data,String fun,String au,String gatewaySn)
    {
        return new MqAjaxResult(HttpStatus.ERROR, msg, data, fun, au, gatewaySn);
    }

    /**
     * 返回错误消息
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @return 警告消息
     */
    public static MqAjaxResult error(int code, String msg,String fun,String au,String gatewaySn)
    {
        return new MqAjaxResult(code, msg, null, fun, au, gatewaySn);
    }

    /**
     * 方便链式调用
     *
     * @param key 键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public MqAjaxResult put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }

	public String getFun() {
		return fun;
	}

	public void setFun(String fun) {
		this.fun = fun;
	}

	public String getAu() {
		return au;
	}

	public void setAu(String au) {
		this.au = au;
	}

	public String getGatewaySn() {
		return gatewaySn;
	}

	public void setGatewaySn(String gatewaySn) {
		this.gatewaySn = gatewaySn;
	}
}
