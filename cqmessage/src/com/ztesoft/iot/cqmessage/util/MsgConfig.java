package com.ztesoft.iot.cqmessage.util;


public class MsgConfig{
	
	 String ismgIp;
	 int ismgPort;
	 String spId;
	 String spCode;
	 String sharedSecret;
	 int connectCount;
	 int timeOut;
	 String serviceId;
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getSharedSecret() {
		return sharedSecret;
	}
	/**
	 * 获取互联网短信网关IP
	 * @return
	 */
	public  String getIsmgIp(){
		return ismgIp;
	}
	/**
	 * 获取互联网短信网关端口号
	 * @return
	 */
	public  int getIsmgPort(){
		return ismgPort;
	}
	/**
	 * 获取sp企业代码
	 * @return
	 */
	public  String getSpId(){
		return spId;
	}
	/**
	 * 获取sp下发短信号码
	 * @return
	 */
	public  String getSpCode(){
		return spCode;
	}
	/**
	 * 获取sp sharedSecret
	 * @return
	 */
	public  String getSpSharedSecret(){
		return spCode;
	}
	/**
	 * 获取链接的次数
	 * @return
	 */
	public  int getConnectCount(){
		return connectCount;
	}
	/**
	 * 获取链接的超时时间
	 * @return
	 */
	public  int getTimeOut(){
		return timeOut;
	}
	
	

	public void setSharedSecret(String sharedSecret) {
		this.sharedSecret = sharedSecret;
	}

	public void setIsmgIp(String ismgIp) {
		this.ismgIp = ismgIp;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}
	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}
	public void setConnectCount(int connectCount) {
		this.connectCount = connectCount;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	
	public void setIsmgPort(int ismgPort) {
		this.ismgPort = ismgPort;
	}
}