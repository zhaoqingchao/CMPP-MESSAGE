package com.ztesoft.iot.cqmessage.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import com.ztesoft.iot.cqmessage.domain.MsgCommand;
import com.ztesoft.iot.cqmessage.domain.MsgConnect;
import com.ztesoft.iot.cqmessage.domain.MsgSubmit;

public class MsgContainer_1 {
	
	public MsgConfig msgConfig;
	public  Socket msgSocket;
	public  DataInputStream in;
	public  DataOutputStream out;
	
	public  Socket getSocketInstance(){
		//if(null==msgSocket||msgSocket.isClosed()||!msgSocket.isConnected()){
			System.out.println("**********************开始建立socket链接");
			try {
				in=null;
				out=null;
				msgSocket=new Socket(msgConfig.getIsmgIp(),msgConfig.getIsmgPort());
				msgSocket.setKeepAlive(true);
				//in=in;
				//out=out;
				in=new DataInputStream(msgSocket.getInputStream());
				out=new DataOutputStream(msgSocket.getOutputStream());
				System.out.println("**********************建立socket链接成功");
				connectISMG();
				/*int count=0;
				boolean result=connectISMG();
				while(!result){
					count++;
					result=connectISMG();
					if(count>=(msgConfig.getConnectCount()-1)){//如果再次连接次数超过两次则终止连接
						break;
					}
				}*/
			} catch (Exception e) {
				System.out.println("Socket链接短信网关失败："+e.getMessage());
			}
		//}
		return msgSocket;
	}
	
	/**
	 * 创建Socket链接后请求链接ISMG
	 * @return
	 */
	public   boolean connectISMG(){
		System.out.println("************************注册请求===========");
		MsgConnect connect=new MsgConnect();
		connect.setTotalLength(12+6+16+1+4);//消息总长度，级总字节数:4+4+4(消息头)+6+16+1+4(消息主体)
		connect.setCommandId(MsgCommand.CMPP_CONNECT);//标识创建连接
		connect.setSequenceId(MsgUtils.getSequence());//序列，由我们指定
		connect.setSourceAddr(msgConfig.getSpId());//我们的企业代码
		connect.setAuthenticatorSource(MsgUtils.md5(msgConfig.getSpId(),msgConfig.getSharedSecret()));//md5(企业代码+密匙+时间戳)
		connect.setTimestamp(Integer.parseInt(MsgUtils.getTimestamp()));//时间戳(MMDDHHMMSS)
		connect.setVersion((byte)0x30);//版本号 高4bit为3，低4位为0
//		List<byte[]> dataList=new ArrayList<byte[]>();
//		dataList.add(connect.toByteArry());
		CmppSender sender=new CmppSender(out,in,connect.toByteArry());
		try {
			sender.start();
			System.out.println("************************注册请求完毕===========");
			return true;
		} catch (Exception e) {
			try {
				out.close();
			} catch (IOException e1) {
				out=null;
			}
			return false;
		}
	}
	
	/**
	 * 发送长短信
	 * @return
	 */
	public  boolean sendLongMsg(String msg,String cusMsisdn){
		boolean a = false;
		try {
			byte[] allByte = this.utf8ToUsc2(msg);
//			byte[] allByte=msg.getBytes("UTF-16BE");
//			List<byte[]> dataList=new ArrayList<byte[]>();
			int msgLength=allByte.length;
			int maxLength=140;
			int msgSendCount=msgLength%(maxLength-6)==0?msgLength/(maxLength-6):msgLength/(maxLength-6)+1;
			//短信息内容头拼接
			byte[] msgHead=new byte[6];
			msgHead[0]=0x05;
			msgHead[1]=0x00;
			msgHead[2]=0x03;
			msgHead[3]=(byte)MsgUtils.getSequence();
			msgHead[4]=(byte)msgSendCount;
			msgHead[5]=0x01;
			int seqId=MsgUtils.getSequence();
			for(int i=0;i<msgSendCount;i++){
				//msgHead[3]=(byte)MsgUtils.getSequence();
				msgHead[5]=(byte)(i+1);
				byte[] needMsg=null;
				//消息头+消息内容拆分
				if(i!=msgSendCount-1){
					int start=(maxLength-6)*i;
					int end=(maxLength-6)*(i+1);
					needMsg=MsgUtils.getMsgBytes(allByte,start,end);
				}else{
					int start=(maxLength-6)*i;
					int end=allByte.length;
					needMsg=MsgUtils.getMsgBytes(allByte,start,end);
				}
				int subLength=needMsg.length+msgHead.length;
				byte[] sendMsg=new byte[needMsg.length+msgHead.length];
				System.arraycopy(msgHead,0,sendMsg,0,6);
				System.arraycopy(needMsg,0,sendMsg,6,needMsg.length);
				MsgSubmit submit=new MsgSubmit();
				submit.setTotalLength(12+8+1+1+1+1+10+1+32+1+1+1+1+6+2+6+17+17+21+1+32+1+1+subLength+20);
				submit.setCommandId(MsgCommand.CMPP_SUBMIT);
				submit.setSequenceId(seqId);			
				submit.setPkTotal((byte)msgSendCount);
				submit.setPkNumber((byte)(i+1));		
				submit.setRegisteredDelivery((byte)0x00);
				submit.setMsgLevel((byte)0x00);
				submit.setServiceId(msgConfig.getServiceId());
				submit.setFeeUserType((byte)0x00);
				submit.setFeeTerminalId("");			
				submit.setFeeTerminalType((byte)0x00);			
				submit.setTpPId((byte)0x00);
				submit.setTpUdhi((byte)0x01);
				submit.setMsgFmt((byte)0x08);
				submit.setMsgSrc(msgConfig.getSpId());
				submit.setSrcId(msgConfig.getSpCode());
				submit.setDestTerminalId(cusMsisdn);
				submit.setMsgLength((byte)subLength);
				submit.setMsgContent(sendMsg);
//				dataList.add(submit.toByteArry());
				
				String fs = bytesToHexString(submit.toByteArry());
				System.out.println("===sendLongMsg====fs:"+fs);
				
				CmppSender sender=new CmppSender(out,in,submit.toByteArry());
				a = sender.start();
				//out.write(submit.toByteArry());
				//out.flush();
				System.out.println("===sendLongMsg====fs完毕...");
			}
			
			
			//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"向"+cusMsisdn+"下发长短信，序列号为:"+seqId);
			return a;
		} catch (Exception e) {
			try {
				out.close();
			} catch (IOException e1) {
				out=null;
			}
			System.out.println("发送长短信"+e.getMessage());
			return false;
		}
	}
	
	//转换橙Usc2
		public  byte[] utf8ToUsc2(String str){
			try {
				ByteArray byteArr = new ByteArray();
				byte[] data = str.getBytes("utf-8");
				for(int i=0; i<data.length; i++){
					byte b = data[i];
					if(0==(b&0x80)){
						byteArr.addByte((byte) 0x00);
						byteArr.addByte(b);
					}else if(0==(b&0x20)){
						byte a = data[++i];
						byteArr.addByte((byte) ((b & 0x3F)>>2));
						byteArr.addByte((byte) (a & 0x3F | b<<6));
					}else if(0==(b&0x10)){
						byte a = data[++i];
						byte c = data[++i];
						byteArr.addByte((byte) (b<<4 | (a & 0x3F)>>2));
						byteArr.addByte((byte) (a<<6 | c &0x3F));
					}
				}
				return byteArr.toArray();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
	    }
	
	public static String bytesToHexString(byte[] src){  
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	}  
	
	
	public MsgConfig getMsgConfig() {
		return msgConfig;
	}
	public void setMsgConfig(MsgConfig msgConfig) {
		this.msgConfig = msgConfig;
	}
	
	public static void main(String[] args) throws Exception{
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
		
		mc.getSocketInstance();
		//mc.connectISMG();
		System.out.println("发送号码："+ph+" 内容 ：   "+sms);	
//		b= mc.sendMsg(ph, sms);
		b= mc.sendLongMsg(sms,ph);
//		b= mc.sendLongMsg2(sms);
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
}
