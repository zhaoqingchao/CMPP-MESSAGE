package com.ztesoft.iot.cqmessage.util;

import java.util.HashMap;
import java.util.Map;

public class Test {
	
	private static CQSMSManage cqmm;
	public static void createMcContain(){
		
		if(cqmm==null){
			cqmm = new CQSMSManage();
		}
		MsgConfig mg=new MsgConfig();
		mg.setIsmgIp("183.230.96.94");
		mg.setIsmgPort(17890);
		
		mg.setSharedSecret("091416");
		mg.setSpCode("1064899091416");
		mg.setSpId("091416");
		mg.setServiceId("szxx");
		mg.setTimeOut(60000);
		
		boolean result = true;
		try{
			//先是否存在该网关，如果存在则断开该网关
			//cqmm.createNewMc(mg, new CQSmsAcceptImpl());
		}catch(Exception e){
			result = false;
		}
		Map mcMap = cqmm.getMcMap();
		MsgContainer mc = (MsgContainer) mcMap.get("1064899091416");
//		MsgContainer mc = cqmm.getMCByKey("1064899091416");
		if(mc!=null&&mc.msgSocket!=null){
			if(mc.msgSocket.isConnected()){
				System.out.println("发送短信测试------------------------------");
				//mc.sendLongMsg("创建链接测试","1064853187600");
				System.out.println("发送短信测试------------------------------");
				result = true;
			}else{
				System.out.println("没有进行发送短信测试------------------------------");
				result = false;
			}
		}else{
			result = false;
		}
	}
	
	
	public void test2(){
		
		MsgContainer_1 mc =new MsgContainer_1();
		MsgConfig mg=new MsgConfig();
		mg.setConnectCount(1);
//		mg.setIsmgIp("211.136.110.98");
//		mg.setIsmgPort(8082);
		mg.setIsmgIp("183.230.96.94");
		mg.setIsmgPort(17890);
		
		mg.setSharedSecret("091416");
		mg.setSpCode("1064899091416");
		mg.setSpId("091416");
		mg.setServiceId("szxx");
		mg.setTimeOut(60000);
		mc.setMsgConfig(mg);
		
		boolean b;
		System.out.println("\r\n....发短信测试 1...\r\n");

		String ph="1064853187600";
		String sms="测试短信";
		
		CQSMSManage cm = new CQSMSManage();
		
		mc.getSocketInstance();
		Map mcMap = cm.getMcMap();
		mcMap.put("1064853187600", mc);
		
		MsgContainer_1 mc2 = new MsgContainer_1();
		mc2 = (MsgContainer_1) mcMap.get("1064853187600");
		
		System.out.println("发送号码："+ph+" 内容 ：   "+sms);	
		b= mc2.sendLongMsg(sms,ph);
		System.out.println(b);
		if(b){
			System.out.println("sendMsg 成功");			
		}else{
			System.out.println("sendMsg 失败");	
		}
		
		
		//开启短信接收线程
		Thread receiveThread = new Thread(new CmppReceive_1(mc));
        receiveThread.start();
	}
	
	
	public static void main(String[] args) {
//		Test.createMcContain();
		new Test().test2();
	}
}
