package com.ztesoft.iot.cqmessage.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.ztesoft.iot.cqmessage.domain.MsgActiveTestResp;
import com.ztesoft.iot.cqmessage.domain.MsgCommand;
import com.ztesoft.iot.cqmessage.domain.MsgConnectResp;
import com.ztesoft.iot.cqmessage.domain.MsgDeliver;
import com.ztesoft.iot.cqmessage.domain.MsgDeliverResp;
import com.ztesoft.iot.cqmessage.domain.MsgHead;
import com.ztesoft.iot.cqmessage.domain.MsgSubmitResp;

/**
 * 启动一个线程去接收和发送数据，如果队列处理完毕就关闭线程
 * @author 张科伟
 * 2011-09-03 12:01
 */
public class CmppSender  {
	
	private Log logger = LogFactory.getLog(this.getClass());
//	private List<byte[]> sendData=new ArrayList<byte[]>();//需要发出的二进制数据队列
//	private List<byte[]> getData=new ArrayList<byte[]>();//需要接受的二进制队列
	private byte[] sendData;
	private byte[] getData;
	private DataOutputStream out;
	private DataInputStream in;
	public CmppSender(DataOutputStream out,DataInputStream in,byte[] sendData) {
		super();
		this.sendData=sendData;
		this.out=out;
		this.in=in;
	}
	
	public boolean start()throws Exception {
		
		if(out!=null&&null!=sendData){
			logger.info("******************CmppSender start。。。******************");
			logger.info("******************开始发送字节流******************");
			out.write(sendData);
			out.flush();
			logger.info("******************发送完毕，等待接收线程对响应进行处理*************");
			//logger.info("============开始接收返回字节流====");
			//byte[] returnData=getInData();
			//logger.info("==CmppSender====js:"+bytesToHexString(returnData));
			//System.out.println("==CmppSender====js:"+bytesToHexString(returnData));
			//getData=returnData;
		}
		
		/*if(in!=null&&null!=getData){
			if(getData.length>=8){
				MsgHead head=new MsgHead(getData);
				
				logger.info("****************************************");
				logger.info("head.getCommandId():"+head.getCommandId());
				logger.info("****************************************");
				
				switch(head.getCommandId()){
					case MsgCommand.CMPP_CONNECT_RESP:
						MsgConnectResp connectResp=new MsgConnectResp(getData);
						
						logger.info("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"链接短信网关,返回状态:"+connectResp.getStatusStr()+"      序列号："+connectResp.getSequenceId());
						System.out.println("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"链接短信网关,返回状态:"+connectResp.getStatusStr()+"      序列号："+connectResp.getSequenceId());
						
						//System.out.println("====请求连接返回值md5散列码：" + MsgUtils.bytes2hex(connectResp.getAuthenticatorISMG()));
						if(connectResp.getStatus()==0){
							b = true;
						}
						break;
					case MsgCommand.CMPP_ACTIVE_TEST_RESP:
						MsgActiveTestResp activeResp=new MsgActiveTestResp(getData);
						logger.info("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"短信网关与短信网关进行连接检查"+" 序列号："+activeResp.getSequenceId());
						System.out.println("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"短信网关与短信网关进行连接检查"+" 序列号："+activeResp.getSequenceId());
						break;
					case MsgCommand.CMPP_SUBMIT_RESP:
						MsgSubmitResp submitResp=new MsgSubmitResp(getData);
						logger.info("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"向用户下发短信，返回状态:"+submitResp.getResult()+"      序列号："+submitResp.getSequenceId());
						System.out.println("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"向用户下发短信，返回状态:"+submitResp.getResult()+"      序列号："+submitResp.getSequenceId());
						System.out.println(submitResp.getResult());
						if(submitResp.getResult()==0){
							b = true;
							logger.info("===CmppSender："+"--------msgId:"+submitResp.getMsgId());
							System.out.println("===CmppSender："+"--------msgId:"+submitResp.getMsgId());
						}
						break;
					case MsgCommand.CMPP_TERMINATE_RESP:
						logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"拆除与ISMG的链接"+" 序列号："+head.getSequenceId());
						System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"拆除与ISMG的链接"+" 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_CANCEL_RESP:
						logger.info("CMPP_CANCEL_RESP 序列号："+head.getSequenceId());
						System.out.println("CMPP_CANCEL_RESP 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_CANCEL:
						logger.info("CMPP_CANCEL 序列号："+head.getSequenceId());
						System.out.println("CMPP_CANCEL 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_DELIVER:
						byte[] m = new byte[8];
						System.arraycopy(getData, 8, m, 0, 8);
						MsgDeliver msgDeliver=new MsgDeliver(getData);
						if(msgDeliver.getResult()==0){
							//System.out.println("===CmppSender："+"CMPP_DELIVER 序列号："+head.getSequenceId()+"，是否消息回复"+(msgDeliver.getRegistered_Delivery()==0?"不是,消息内容："+msgDeliver.getMsg_Content():"是，目的手机号："+msgDeliver.getDest_terminal_Id()));
							logger.info("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：CMPP_DELIVER接收短信    序列号："+head.getSequenceId()+"，是否上行短信："+(msgDeliver.getRegistered_Delivery()==0?"是,消息内容："+msgDeliver.getMsg_Content()+"，手机号码是："+msgDeliver.getSrc_terminal_Id():"不是，是状态报告。"));
							System.out.println("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：CMPP_DELIVER接收短信    序列号："+head.getSequenceId()+"，是否上行短信："+(msgDeliver.getRegistered_Delivery()==0?"是,消息内容："+msgDeliver.getMsg_Content()+"，手机号码是："+msgDeliver.getSrc_terminal_Id():"不是，是状态报告。"));
						}else{
							logger.info("===CmppSender："+"CMPP_DELIVER 序列号："+head.getSequenceId());
							System.out.println("===CmppSender："+"CMPP_DELIVER 序列号："+head.getSequenceId());
						}
						MsgDeliverResp msgDeliverResp=new MsgDeliverResp();
						msgDeliverResp.setTotalLength(12+8+4);
						msgDeliverResp.setCommandId(MsgCommand.CMPP_DELIVER_RESP);
						msgDeliverResp.setSequenceId(MsgUtils.getSequence());
						msgDeliverResp.setMsg_Id(m);
						msgDeliverResp.setResult(msgDeliver.getResult());
						sendMsg(msgDeliverResp.toByteArry());//进行回复
						break;
					case MsgCommand.CMPP_DELIVER_RESP:
						logger.info("CMPP_DELIVER_RESP 序列号："+head.getSequenceId());
						System.out.println("CMPP_DELIVER_RESP 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_QUERY:
						logger.info("CMPP_QUERY 序列号："+head.getSequenceId());
						System.out.println("CMPP_QUERY 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_QUERY_RESP:
						logger.info("CMPP_QUERY_RESP 序列号："+head.getSequenceId());
						System.out.println("CMPP_QUERY_RESP 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_TERMINATE:
						logger.info("CMPP_TERMINATE 序列号："+head.getSequenceId());
						System.out.println("CMPP_TERMINATE 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_CONNECT:
						logger.info("CMPP_CONNECT 序列号："+head.getSequenceId());
						System.out.println("CMPP_CONNECT 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_ACTIVE_TEST:
						logger.info("CMPP_ACTIVE_TEST 序列号："+head.getSequenceId());
						System.out.println("CMPP_ACTIVE_TEST 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_SUBMIT:
						logger.info("CMPP_SUBMIT 序列号："+head.getSequenceId());
						System.out.println("CMPP_SUBMIT 序列号："+head.getSequenceId());
						break;
					default:
						logger.info("===CmppSender："+"无法解析IMSP返回的包结构：包长度为"+head.getTotalLength());
						System.out.println("===CmppSender："+"无法解析IMSP返回的包结构：包长度为"+head.getTotalLength());
						break;
				}
			}
		}*/
		return true;
	}
	
	
	/**
	   * 在本连结上发送已打包后的消息的字节
	   * @param data:要发送消息的字节
	   */
	private  boolean sendMsg(byte[] data)throws Exception {
	   try{
		  out.write(data);
		  out.flush();
		  return true;
	   }catch(NullPointerException ef){
		  logger.info("在本连结上发送已打包后的消息的字节:无字节输入");
		  System.out.println("在本连结上发送已打包后的消息的字节:无字节输入");
	   }
	   return false;
	}
	
	private  byte[] getInData() throws IOException{
		 try{
			 int len=in.readInt();
			 if(null!=in&&0!=len){
			   byte[] data=new byte[len-4];
		   	   in.read(data);
		   	   return data;
			 }else{
				 return null;
			 }
		 }catch(NullPointerException ef){
			System.out.println("在本连结上接受字节消息:无流输入");	
			logger.info("在本连结上接受字节消息:无流输入");
			return null;
		 }catch(EOFException eof){
			 logger.info("在本连结上接受字节消息:"+eof.getMessage());
			 System.out.println("在本连结上接受字节消息:"+eof.getMessage());			
			 return null;
		 }
	}
	
	public static int bytes4ToInt(byte mybytes[]) {
		int tmp = (0xff & mybytes[0]) << 24 | (0xff & mybytes[1]) << 16
				| (0xff & mybytes[2]) << 8 | 0xff & mybytes[3];
		return tmp;
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
	
	
	
	
	public boolean testSend() throws IOException{
		
		if(out!=null&&null!=sendData){
			logger.info("******************CmppSender start。。。******************");
			logger.info("******************开始发送字节流******************");
			out.write(sendData);
			out.flush();
			logger.info("******************发送完毕，等待接收线程对响应进行处理*************");
			
			byte[] returnData=getInData();
			//logger.info("==CmppSender====js:"+bytesToHexString(returnData));
			//System.out.println("==CmppSender====js:"+bytesToHexString(returnData));
			getData=returnData;
		}
		boolean b = false;
		if(in!=null&&null!=getData){
			if(getData.length>=8){
				MsgHead head=new MsgHead(getData);
				
				logger.info("****************************************");
				logger.info("head.getCommandId():"+head.getCommandId());
				logger.info("****************************************");
				
				switch(head.getCommandId()){
					case MsgCommand.CMPP_CONNECT_RESP:
						MsgConnectResp connectResp=new MsgConnectResp(getData);
						
						logger.info("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"链接短信网关,返回状态:"+connectResp.getStatusStr()+"      序列号："+connectResp.getSequenceId());
						System.out.println("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"链接短信网关,返回状态:"+connectResp.getStatusStr()+"      序列号："+connectResp.getSequenceId());
						
						//System.out.println("====请求连接返回值md5散列码：" + MsgUtils.bytes2hex(connectResp.getAuthenticatorISMG()));
						if(connectResp.getStatus()==0){
							b = true;
						}
						break;
					case MsgCommand.CMPP_ACTIVE_TEST_RESP:
						MsgActiveTestResp activeResp=new MsgActiveTestResp(getData);
						logger.info("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"短信网关与短信网关进行连接检查"+" 序列号："+activeResp.getSequenceId());
						System.out.println("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"短信网关与短信网关进行连接检查"+" 序列号："+activeResp.getSequenceId());
						break;
					case MsgCommand.CMPP_SUBMIT_RESP:
						MsgSubmitResp submitResp=new MsgSubmitResp(getData);
						logger.info("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"向用户下发短信，返回状态:"+submitResp.getResult()+"      序列号："+submitResp.getSequenceId());
						System.out.println("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"向用户下发短信，返回状态:"+submitResp.getResult()+"      序列号："+submitResp.getSequenceId());
						System.out.println(submitResp.getResult());
						if(submitResp.getResult()==0){
							b = true;
							logger.info("===CmppSender："+"--------msgId:"+submitResp.getMsgId());
							System.out.println("===CmppSender："+"--------msgId:"+submitResp.getMsgId());
						}
						break;
					case MsgCommand.CMPP_TERMINATE_RESP:
						logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"拆除与ISMG的链接"+" 序列号："+head.getSequenceId());
						System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"拆除与ISMG的链接"+" 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_CANCEL_RESP:
						logger.info("CMPP_CANCEL_RESP 序列号："+head.getSequenceId());
						System.out.println("CMPP_CANCEL_RESP 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_CANCEL:
						logger.info("CMPP_CANCEL 序列号："+head.getSequenceId());
						System.out.println("CMPP_CANCEL 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_DELIVER_RESP:
						logger.info("CMPP_DELIVER_RESP 序列号："+head.getSequenceId());
						System.out.println("CMPP_DELIVER_RESP 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_QUERY:
						logger.info("CMPP_QUERY 序列号："+head.getSequenceId());
						System.out.println("CMPP_QUERY 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_QUERY_RESP:
						logger.info("CMPP_QUERY_RESP 序列号："+head.getSequenceId());
						System.out.println("CMPP_QUERY_RESP 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_TERMINATE:
						logger.info("CMPP_TERMINATE 序列号："+head.getSequenceId());
						System.out.println("CMPP_TERMINATE 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_CONNECT:
						logger.info("CMPP_CONNECT 序列号："+head.getSequenceId());
						System.out.println("CMPP_CONNECT 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_ACTIVE_TEST:
						logger.info("CMPP_ACTIVE_TEST 序列号："+head.getSequenceId());
						System.out.println("CMPP_ACTIVE_TEST 序列号："+head.getSequenceId());
						break;
					case MsgCommand.CMPP_SUBMIT:
						logger.info("CMPP_SUBMIT 序列号："+head.getSequenceId());
						System.out.println("CMPP_SUBMIT 序列号："+head.getSequenceId());
						break;
					default:
						logger.info("===CmppSender："+"无法解析IMSP返回的包结构：包长度为"+head.getTotalLength());
						System.out.println("===CmppSender："+"无法解析IMSP返回的包结构：包长度为"+head.getTotalLength());
						break;
				}
			}
		}
		return b;
	}
	
	
	
	
}
