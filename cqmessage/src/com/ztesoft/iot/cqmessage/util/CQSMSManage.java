package com.ztesoft.iot.cqmessage.util;

import java.util.HashMap;
import java.util.Map;


public class CQSMSManage {
	public static Map mcMap;
	
	public CQSMSManage(){
		mcMap = new HashMap();
	}
	
	
	public MsgContainer createNewMc(MsgConfig mg,ICQSmsAccept smsAccept,ICQSmsSend smsSend){
		MsgContainer mc =new MsgContainer();
		
		mc.setMsgConfig(mg);
		mc.getSocketInstance();//建立socket链接
		
		String dk = mg.getSpCode();
        mcMap.put(dk, mc);
		
		//开启短信接收线程
		Thread receiveThread = new Thread(new CmppReceive(mc,dk,smsAccept,smsSend));
        receiveThread.start();
        return mc;
	}
	
	
	public MsgContainer createNewMcForValidate(MsgConfig mg,ICQSmsAccept smsAccept,ICQSmsSend smsSend){
		MsgContainer mc =new MsgContainer();
		
		mc.setMsgConfig(mg);
		mc.getSocketInstance();//建立socket链接
		
		String dk = mg.getSpCode();
        mcMap.put(dk, mc);
		
        return mc;
	}
	
	
	
	public Map getMcMap(){
		return mcMap;
	}
	
	
	/**public static void main(String[] args) throws Exception{
		MsgContainer mc =new MsgContainer();
		MsgConfig mg=new MsgConfig();
		mg.setIsmgIp("183.230.96.94");
		mg.setIsmgPort(17890);
		mg.setSharedSecret("091416");
		mg.setSpCode("1064899091416");
		mg.setSpId("091416");
		mg.setServiceId("szxx");
		mg.setTimeOut(60000);
		mc.setMsgConfig(mg);
		
		String ph="1064853187600";
		String sms="测试短信";
		
		mc.getSocketInstance();//建立socket链接
		mc.sendLongMsg(sms,ph);//发送短信
		
		//开启短信接收线程
		//Thread receiveThread = new Thread(new CmppReceive(mc.out,mc.in));
        //receiveThread.start();
	
	}*/
}
