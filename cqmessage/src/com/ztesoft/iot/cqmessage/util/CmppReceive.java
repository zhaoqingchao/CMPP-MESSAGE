package com.ztesoft.iot.cqmessage.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ztesoft.iot.cqmessage.domain.CQActiveTestResp;
import com.ztesoft.iot.cqmessage.domain.MsgActiveTestResp;
import com.ztesoft.iot.cqmessage.domain.MsgCommand;
import com.ztesoft.iot.cqmessage.domain.MsgConnectResp;
import com.ztesoft.iot.cqmessage.domain.MsgDeliver;
import com.ztesoft.iot.cqmessage.domain.MsgDeliverResp;
import com.ztesoft.iot.cqmessage.domain.MsgHead;
import com.ztesoft.iot.cqmessage.domain.MsgSubmitResp;

public class CmppReceive implements Runnable{
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	private boolean runnabled = true;
	private DataOutputStream out;
	private DataInputStream in;
	private MsgContainer tmc;
	private String tdk;
	private ICQSmsAccept tsmsAccept;
	private ICQSmsSend tsmsSend;
	public CmppReceive(MsgContainer mc,String dk,ICQSmsAccept smsAccept,ICQSmsSend smsSend) {
		this.out=mc.out;
		this.in=mc.in;
		tmc = mc;
		tdk = dk;
		this.tsmsAccept = smsAccept;
		this.tsmsSend = smsSend;
	}
	
	public void run() {
		int i = 0;
		int j = 0;
		
		while (runnabled) {
			try{
				logger.info("************"+tmc.getMsgConfig().getSpCode()+"接收线程开始*************");
				byte[] returnData=getInData();
				logger.info("*************************短信接入码："+tmc.getMsgConfig().getSpCode());
				System.out.println("*************************短信接入码："+tmc.getMsgConfig().getSpCode());
				logger.info("***短信接入码<"+tmc.getMsgConfig().getSpCode()+">===CmppReceive 读取到字节流:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====："+bytesToHexString(returnData));
				System.out.println("***短信接入码<"+tmc.getMsgConfig().getSpCode()+">===CmppReceive 读取到字节流:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====："+bytesToHexString(returnData));
				
				if(returnData!=null && returnData.length>=8){
					MsgHead head=new MsgHead(returnData);
					
					logger.info("****************************************");
					logger.info("head.getCommandId():"+head.getCommandId());
					logger.info("****************************************");
					
					switch(head.getCommandId()){
						case MsgCommand.CMPP_CONNECT_RESP:
							MsgConnectResp connectResp=new MsgConnectResp(returnData);
							logger.info("===CmppReceive："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：链接短信网关,返回状态:"+connectResp.getStatusStr()+"      序列号："+connectResp.getSequenceId());
							System.out.println("===CmppReceive："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：链接短信网关,返回状态:"+connectResp.getStatusStr()+"      序列号："+connectResp.getSequenceId());
							if(connectResp.getStatus()==0){
								//b = true;
							}
							try{
								byte[] authenticatorISMG = connectResp.getAuthenticatorISMG();
								String auth = MsgUtils.bytes2hex(authenticatorISMG);
								System.out.println(auth);
								logger.info("************************************");
								logger.info("认证响应auth:"+auth);
								logger.info("************************************");
							}catch (Exception e) {
								e.printStackTrace();
							}
							break;
						case MsgCommand.CMPP_ACTIVE_TEST:
							logger.info("===CmppReceive："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：接收到短信网关连接检查请求---");
							System.out.println("===CmppReceive："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：接收到短信网关连接检查请求---");
							
							byte[] seqs = new byte[4];
							System.arraycopy(returnData, 4, seqs, 0, 4);
							
							CQActiveTestResp cQActiveTestResp = new CQActiveTestResp();
							cQActiveTestResp.setTotalLength(12+1);
							cQActiveTestResp.setCommandId(MsgCommand.CMPP_ACTIVE_TEST_RESP);
							cQActiveTestResp.setSeq_Id(seqs);
							
							String fss = bytesToHexString(cQActiveTestResp.toByteArry());
							
							logger.info("===CmppReceive："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：CMPP_ACTIVE_TEST_RESP 回复网关："+fss);
							sendMsg(cQActiveTestResp.toByteArry());//进行回复
							
							logger.info("---CmppReceive："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：CMPP_ACTIVE_TEST_RESP 回复网关完毕...");
							break;
						case MsgCommand.CMPP_ACTIVE_TEST_RESP:
							MsgActiveTestResp activeResp=new MsgActiveTestResp(returnData);
							logger.info("===CmppReceive："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：发送给短信网关进行连接检查的返回---"+" 序列号："+activeResp.getSequenceId());
							System.out.println("===CmppReceive："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：发送给短信网关进行连接检查的返回---"+" 序列号："+activeResp.getSequenceId());
							break;
						case MsgCommand.CMPP_SUBMIT_RESP:
							
							MsgSubmitResp submitResp=new MsgSubmitResp(returnData);
							logger.info("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"向用户下发短信，返回状态:"+submitResp.getResult()+"      序列号："+submitResp.getSequenceId());
							System.out.println("===CmppSender："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"向用户下发短信，返回状态:"+submitResp.getResult()+"      序列号："+submitResp.getSequenceId());
							System.out.println(submitResp.getResult());
							if(submitResp.getResult()==0){//短信返回成功
								logger.info("********************************************************");
								logger.info("***************接收到短信发送成功响应*******************");
								logger.info("***************序列号："+head.getSequenceId());
								logger.info("********************************************************");
							}
							
							try{
								Map map = new HashMap();
								map.put("status_code", submitResp.getResult());
								map.put("sequence_id", submitResp.getSequenceId());
								tsmsSend.process(map);
							}catch(Exception e){
								e.printStackTrace();
								logger.info("********************************************************");
								logger.info("************更新短信状态发生异常*****************");
								logger.info(e.getMessage());
								logger.info("********************************************************");
							}
							break;
							
						case MsgCommand.CMPP_DELIVER:
							byte[] b = new byte[8];
							System.arraycopy(returnData, 8, b, 0, 8);
							
							byte[] seq = new byte[4];
							System.arraycopy(returnData, 4, seq, 0, 4);
							
							MsgDeliver msgDeliver=new MsgDeliver(returnData);
							if(msgDeliver.getResult()==0){
								logger.info("===CmppReceive："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：CMPP_DELIVER接收短信    序列号："+head.getSequenceId()+"，是否上行短信："+(msgDeliver.getRegistered_Delivery()==0?"是,消息内容："+msgDeliver.getMsg_Content()+"，手机号码是："+msgDeliver.getSrc_terminal_Id():"不是，是状态报告。"));
								System.out.println("===CmppReceive："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：CMPP_DELIVER接收短信    序列号："+head.getSequenceId()+"，是否上行短信："+(msgDeliver.getRegistered_Delivery()==0?"是,消息内容："+msgDeliver.getMsg_Content()+"，手机号码是："+msgDeliver.getSrc_terminal_Id():"不是，是状态报告。"));
								
								MsgDeliverResp msgDeliverResp=new MsgDeliverResp();
								msgDeliverResp.setTotalLength(12+8+4);
								msgDeliverResp.setCommandId(MsgCommand.CMPP_DELIVER_RESP);
								msgDeliverResp.setSeq_Id(seq);
								msgDeliverResp.setMsg_Id(b);
								msgDeliverResp.setResult(msgDeliver.getResult());
								
								String fs = bytesToHexString(msgDeliverResp.toByteArry());
								logger.info("===CmppReceive："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：CMPP_DELIVER_RESP 回复网关："+fs);
								System.out.println("===CmppReceive："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：CMPP_DELIVER_RESP 回复网关："+fs);
								sendMsg(msgDeliverResp.toByteArry());//进行回复
								logger.info("===CmppReceive："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：CMPP_DELIVER_RESP 回复网关完毕...");
								System.out.println("===CmppReceive："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：CMPP_DELIVER_RESP 回复网关完毕...");
								
								//接收处理
								logger.info("---进入处理逻辑(上行)---");
								System.out.println("---进入处理逻辑(上行)---");
								String phone = msgDeliver.getSrc_terminal_Id();
								String content = msgDeliver.getMsg_Content();
								SmsInfo smsInfo = new SmsInfo();
								smsInfo.setDk(tdk);
								smsInfo.setPhone(phone);
								smsInfo.setSmsContent(content);
								tsmsAccept.process(smsInfo);
							}else{
								logger.info("===CmppReceive：消息结构错；"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：CMPP_DELIVER接收短信    序列号："+head.getSequenceId()+"，是否上行短信："+(msgDeliver.getRegistered_Delivery()==0?"是,消息内容："+msgDeliver.getMsg_Content()+"，手机号码是："+msgDeliver.getSrc_terminal_Id():"不是，是状态报告。"));
								System.out.println("===CmppReceive：消息结构错；"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：CMPP_DELIVER接收短信    序列号："+head.getSequenceId()+"，是否上行短信："+(msgDeliver.getRegistered_Delivery()==0?"是,消息内容："+msgDeliver.getMsg_Content()+"，手机号码是："+msgDeliver.getSrc_terminal_Id():"不是，是状态报告。"));
								
								MsgDeliverResp msgDeliverResp=new MsgDeliverResp();
								msgDeliverResp.setTotalLength(12+8+4);
								msgDeliverResp.setCommandId(MsgCommand.CMPP_DELIVER_RESP);
								msgDeliverResp.setSeq_Id(seq);
								msgDeliverResp.setMsg_Id(b);
								msgDeliverResp.setResult(msgDeliver.getResult());
								String fs = bytesToHexString(msgDeliverResp.toByteArry());
								logger.info("===CmppReceive_1："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：CMPP_DELIVER_RESP 回复网关："+fs);
								System.out.println("===CmppReceive_1："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：CMPP_DELIVER_RESP 回复网关："+fs);
								sendMsg(msgDeliverResp.toByteArry());//进行回复
								logger.info("===CmppReceive_1："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：CMPP_DELIVER_RESP 回复网关完毕...");
								System.out.println("===CmppReceive_1："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：CMPP_DELIVER_RESP 回复网关完毕...");
							}
							break;
						default:
							logger.info("===CmppReceive_1："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：无法解析IMSP返回的包结构：包长度为"+head.getTotalLength());
							System.out.println("===CmppReceive_1："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：无法解析IMSP返回的包结构：包长度为"+head.getTotalLength());
							break;
					}
					Thread.sleep(1000);//休息1秒，继续读取
					i = 0;
				}else{
					logger.info("======"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：没有读取到数据,或长度不正确，休息10秒====i:"+i);
					System.out.println("======"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"=====：没有读取到数据,或长度不正确，休息10秒====i:"+i);
					Thread.sleep(10000);
					i++;
					if(i==15){
						i = 0;
					}
				}
				j = 0;
			}catch(SocketException e){
				logger.error(e.getMessage());
				logger.info("*******************************"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：短信接收socket链接出现异常*************************************");
				System.out.println("*******************************"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：短信接收socket链接出现异常*************************************");
				e.printStackTrace();
				j++;
				if(j>=3){
					runnabled = false;
				}else{
					logger.info("*******************************"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：短信接收重新建立socket链接*************************************");
					System.out.println("*******************************"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：短信接收重新建立socket链接*************************************");
					tmc.getSocketInstance();
					this.out=tmc.out;
					this.in=tmc.in;
				}
			}catch(InterruptedException e){
				logger.error(e.getMessage());
				logger.info("*******************************"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：短信接收线程出现异常*************************************");
				System.out.println("*******************************"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：短信接收线程出现异常*************************************");
				j++;
				if(j>=3){
					runnabled = false;
				}
			}catch(IOException e){
				logger.error(e.getMessage());
				logger.info("*******************************"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：短信接收发送数据流出现异常*************************************");
				System.out.println("*******************************"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：短信接收发送数据流出现异常*************************************");
				j++;
				if(j>=3){
					runnabled = false;
				}
			}catch(Exception e){
				logger.info(e.getMessage());
				logger.info("*******************************短信链接发生未知异常*******************************"+TimeUtil.getNow(TimeUtil.PATTERN_YYYY_MM_DD_HH_MM_SS));
				System.out.println("*******************************短信链接发生未知异常*******************************"+TimeUtil.getNow(TimeUtil.PATTERN_YYYY_MM_DD_HH_MM_SS));
				System.out.println(e.getMessage());
			}
		}
		System.out.println("************************************************************************************************");
		System.out.println("*************"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：警告，重庆短信接收线程终止,发生终止的链接接入码是："+tmc.getMsgConfig().getSpCode());
		System.out.println("runnabled状态："+runnabled+"    "+"i : "+i+"   "+"j : "+j);
		System.out.println("************************************************************************************************");
	
		logger.info("************************************************************************************************");
		logger.info("*************"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：警告，重庆短信接收线程终止,发生终止的链接接入码是："+tmc.getMsgConfig().getSpCode());
		logger.info("runnabled状态："+runnabled+"    "+"i : "+i+"   "+"j : "+j);
		logger.info("************************************************************************************************");
	}
	
	
	
	
	/**
	 * 链路检查
	 * @return
	 */
	public   boolean activityTestISMG(){
		try {
			MsgHead head=new MsgHead();
			head.setTotalLength(12);//消息总长度，级总字节数:4+4+4(消息头)+6+16+1+4(消息主体)
			head.setCommandId(MsgCommand.CMPP_ACTIVE_TEST);//标识创建连接
			head.setSequenceId(MsgUtils.getSequence());//序列，由我们指定
			
//			List<byte[]> dataList=new ArrayList<byte[]>();
//			dataList.add(head.toByteArry());
			CmppSender sender=new CmppSender(out,in,head.toByteArry());
			sender.start();
			return true;
		} catch (Exception e) {
			try {
				out.close();
			} catch (IOException e1) {
				out=null;
			}
			logger.info("链路检查"+e.getMessage());
			System.out.println("链路检查"+e.getMessage());	
			return false;
		}
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
			logger.info("在本连结上接受字节消息:无流输入");
			System.out.println("在本连结上接受字节消息:无流输入");			
			return null;
		 }catch(EOFException eof){
			 logger.info("在本连结上接受字节消息:"+eof.getMessage());
			 System.out.println("在本连结上接受字节消息:"+eof.getMessage());			
			 return null;
		 }
	}
	
	
	/**
	   * 在本连结上发送已打包后的消息的字节
	   * @param data:要发送消息的字节
	   */
	private  boolean sendMsg(byte[] data)throws IOException {
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
}
