package com.ztesoft.iot.cqmessage.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;














import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.ztesoft.iot.cqmessage.domain.MsgCommand;
import com.ztesoft.iot.cqmessage.domain.MsgConnect;
import com.ztesoft.iot.cqmessage.domain.MsgHead;
import com.ztesoft.iot.cqmessage.domain.MsgSubmit;
/**
 * 短信接口容器，单例获得链接对象
 * @author 张科伟
 * 2011-08-22 14:20
 */
public class MsgContainer{
	public MsgConfig msgConfig;
	private Log logger = LogFactory.getLog(this.getClass());
	public Socket msgSocket;
	public DataInputStream in;
	public DataOutputStream out;
	/**public   DataInputStream in{
		if(in==null||null==msgSocket||msgSocket.isClosed()||!msgSocket.isConnected()){
			try {
				in=new DataInputStream(this.getSocketInstance().getInputStream());
			} catch (IOException e) {
				in=null;
			}
		}
		return in;
	}
	public    DataOutputStream out{
		if(out==null||null==msgSocket||msgSocket.isClosed()||!msgSocket.isConnected()){
			try {
				out=new DataOutputStream(this.getSocketInstance().getOutputStream());
			} catch (IOException e) {
				out=null;
			}
		}
		return out;
	}*/
	public  Socket getSocketInstance(){
		//if(null==msgSocket||msgSocket.isClosed()||!msgSocket.isConnected()){
			System.out.println("**********************开始建立socket链接");
			logger.info("**********************开始建立socket链接");
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
				logger.info("**********************建立socket链接成功");
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
				logger.info("Socket链接短信网关失败："+e.getMessage());
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
		logger.info("************************注册请求===========");
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
			logger.info("************************注册请求完毕===========");
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
	
	/*public   boolean sendMsg(String msg,String cusMsisdn){
		
		try{
			if(msg.getBytes("utf-8").length<140){//短短信
				boolean result=sendShortMsg(msg,cusMsisdn);
				int count=0;
				while(!result){
					count++;
					result=sendShortMsg(msg,cusMsisdn);
					if(count>=(msgConfig.getConnectCount()-1)){//如果再次连接次数超过两次则终止连接
						break;
					}
				}
				return result;
			}else{//长短信
				boolean result=sendLongMsg(msg,cusMsisdn);
				int count=0;
				while(!result){
					count++;
					result=sendLongMsg(msg,cusMsisdn);
					if(count>=(msgConfig.getConnectCount()-1)){//如果再次连接次数超过两次则终止连接
						break;
					}
				}
				return result;
			}
		}catch(Exception e){
			try {
				out.close();
			} catch (IOException e1) {
				out=null;
			}
			return false;
		}
	}*/
	
	
	/**
	 * 发送web push短信
	 * @param url wap网址
	 * @param desc 描述
	 * @param cusMsisdn 短信
	 * @return
	 */
	public   boolean sendWapPushMsg(String url,String desc,String cusMsisdn){
		try{
			int msgContent=12+9+9+url.getBytes("utf-8").length+3+desc.getBytes("utf-8").length+3;
			if(msgContent<140){
				boolean result=sendShortWapPushMsg(url,desc,cusMsisdn);
				int count=0;
				while(!result){
					count++;
					result=sendShortWapPushMsg(url,desc,cusMsisdn);
					if(count>=(msgConfig.getConnectCount()-1)){//如果再次连接次数超过两次则终止连接
						break;
					}
				}
				return result;
			}else{
				boolean result=sendLongWapPushMsg(url,desc,cusMsisdn);
				int count=0;
				while(!result){
					count++;
					result=sendLongWapPushMsg(url,desc,cusMsisdn);
					if(count>=(msgConfig.getConnectCount()-1)){//如果再次连接次数超过两次则终止连接
						break;
					}
				}
				return result;
			}
		}catch(Exception e){
			try {
				out.close();
			} catch (IOException e1) {
				out=null;
			}
			System.out.println("发送web push短信:"+e.getMessage());
			return false;
		}
	}
	/**
	 * 发送短短信
	 * @return
	 */
	private  boolean sendShortMsg(String msg,String cusMsisdn){
		logger.info("*****************开始发送短短信*****************");
		System.out.println("*****************开始发送短短信*****************");
		try {			
			int seq=MsgUtils.getSequence();
			MsgSubmit submit=new MsgSubmit();
			submit.setTotalLength(12+8+1+1+1+1+10+1+32+1+1+1+1+6+2+6+17+17+21+1+32+1+1+msg.length()*2+20);
			submit.setCommandId(MsgCommand.CMPP_SUBMIT);
			submit.setSequenceId(seq);			
			submit.setPkTotal((byte)0x01);
			submit.setPkNumber((byte)0x01);			
			submit.setRegisteredDelivery((byte)0x00);
			submit.setMsgLevel((byte)0x01);
			submit.setFeeUserType((byte)0x00);
			submit.setFeeTerminalId("");			
			submit.setFeeTerminalType((byte)0x00);			
			submit.setTpPId((byte)0x00);
			submit.setTpUdhi((byte)0x00);
			submit.setMsgFmt((byte)0x0f);
			submit.setMsgSrc(msgConfig.getSpId());
			submit.setSrcId(msgConfig.getSpCode());
			submit.setDestTerminalId(cusMsisdn);
			submit.setMsgLength((byte)(msg.length()*2));
			submit.setMsgContent(msg.getBytes("gb2312"));
			
//			List<byte[]> dataList=new ArrayList<byte[]>();
//			dataList.add(submit.toByteArry());
			CmppSender sender=new CmppSender(out,in,submit.toByteArry());
			sender.start();
			logger.info("*****************开始发送短短信完毕*****************");
			System.out.println("*****************开始发送短短信完毕*****************");
			//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"向"+cusMsisdn+"下发短短信，序列号为:"+seq);
			return true;
		} catch (Exception e) {
			try {
				out.close();
			} catch (IOException e1) {
				out=null;
			}
			logger.info("发送短短信失败："+e.getMessage());
			System.out.println("发送短短信失败："+e.getMessage());
			return false;
		}
	}
	
	/**
	 * 发送长短信
	 * @return
	 */
	public Map sendLongMsg(String msg,String cusMsisdn,int seqId){
		
		logger.info("进入接口类：开始发送短信***************************");
		logger.info("序列数为："+seqId);
		
		Map result = new HashMap();
		result.put("code", "1000");//1000：请求成功；1100：请求失败
		try {
			byte[] allByte = this.utf8ToUsc2(msg);
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
			//int seqId=MsgUtils.getSequence();
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
				logger.info("===sendLongMsg====fs:"+fs);
				System.out.println("===sendLongMsg====fs:"+fs);
				
				CmppSender sender=new CmppSender(out,in,submit.toByteArry());
				boolean a = sender.start();
				logger.info("===sendLongMsg====fs完毕...");
				System.out.println("===sendLongMsg====fs完毕...");
				result.put("sequence_id", seqId+"");
			}
			return result;
		} catch (Exception e) {
			try {
				result.put("code", "1100");
				result.put("sequence_id", "000");
				out.close();
			} catch (IOException e1) {
				out=null;
			}
			logger.info("发送长短信"+e.getMessage());
			System.out.println("发送长短信"+e.getMessage());
			return result;
		}
	}
	
	
	public boolean testGateWayValid(String msg,String cusMsisdn,int seqId){
		
		logger.info("进入接口类：开始发送短信***************************");
		logger.info("序列数为："+seqId);
		
		boolean result = false;
		try {
			byte[] allByte = this.utf8ToUsc2(msg);
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
			//int seqId=MsgUtils.getSequence();
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
				
				String fs = bytesToHexString(submit.toByteArry());
				logger.info("===sendLongMsg====fs:"+fs);
				System.out.println("===sendLongMsg====fs:"+fs);
				
				CmppSender sender=new CmppSender(out,in,submit.toByteArry());
				result = sender.testSend();
				logger.info("===sendLongMsg====fs完毕...");
				System.out.println("===sendLongMsg====fs完毕...");
			}
			return result;
		} catch (Exception e) {
			logger.info("发送长短信"+e.getMessage());
			System.out.println("发送长短信"+e.getMessage());
			return result;
		}
	}
	
	
	/**
	 * 发送长短信
	 * @return
	 */
	private  boolean sendLongMsg2(String msg){
		try {
			byte[] allByte = this.utf8ToUsc2(msg);
			
//			byte[] msgHead=new byte[6];
//			msgHead[0]=0x05;
//			msgHead[1]=0x00;
//			msgHead[2]=0x03;
//			msgHead[3]=(byte)MsgUtils.getSequence();
//			msgHead[4]=(byte)0x01;
//			msgHead[5]=0x01;
			
			System.out.println("开始拼接短信消息体....");
			byte[] msgBody = getMsgBody(allByte.length);//消息体中短信内容前面的字节
			
			System.out.println("开始拼接短信消息头....");
			byte[] msgHeader = getMsgHeader(msgBody.length+allByte.length+20);//消息头
			
			System.out.println("开始合并短信消息头和消息体....");
			byte[] msgAll = new byte[msgHeader.length + msgBody.length + allByte.length+20];//合并
			
			System.out.println("=======整个包长度:"+msgAll.length);
			for (int k = 0; k < msgHeader.length; k++) {
				msgAll[k] = msgHeader[k];
			}
			for (int k = 0; k < msgBody.length; k++) {
				msgAll[msgHeader.length + k] = msgBody[k];
			}
			for (int k = 0; k < allByte.length; k++) {
				msgAll[msgHeader.length + msgBody.length + k] = allByte[k];
			}
			
			byte[] link = new byte[20];
			//LinkID
			/*link[0] = 0x00;
			link[1] = 0x00;
			link[2] = 0x00;
			link[3] = 0x00;
			link[4] = 0x00;
			link[5] = 0x00;
			link[6] = 0x00;
			link[7] = 0x00;
			link[8] = 0x00;
			link[9] = 0x00;
			link[10] = 0x00;
			link[11] = 0x00;
			link[12] = 0x00;
			link[13] = 0x00;
			link[14] = 0x00;
			link[15] = 0x00;
			link[16] = 0x00;
			link[17] = 0x00;
			link[18] = 0x00;
			link[19] = 0x00;*/
			
			link[0] = 0x30;
			link[1] = 0x31;
			link[2] = 0x32;
			link[3] = 0x33;
			link[4] = 0x34;
			link[5] = 0x35;
			link[6] = 0x36;
			link[7] = 0x37;
			link[8] = 0x38;
			link[9] = 0x39;
			link[10] = 0x30;
			link[11] = 0x31;
			link[12] = 0x32;
			link[13] = 0x33;
			link[14] = 0x34;
			link[15] = 0x35;
			link[16] = 0x36;
			link[17] = 0x37;
			link[18] = 0x38;
			link[19] = 0x39;
			
			for (int k = 0; k < link.length; k++) {
				msgAll[msgHeader.length + msgBody.length + allByte.length + k] = link[k];
			}

						
			String fs = bytesToHexString(msgAll);
			System.out.println("fs:"+fs);
			
			CmppSender sender=new CmppSender(out,in,msgAll);
			sender.start();
			
			//System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"向"+cusMsisdn+"下发长短信，序列号为:"+seqId);
			return true;
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
	
	//获得消息体中短信内容前面的字节
	public byte[] getMsgBody(int msgConLength){
			
		byte[] msgbody = new byte[163];
		//Msg_id
		/*String Msg_id = "00000000000000000001";
		byte[] a = Msg_id.getBytes();
		for (int i = 0; i < a.length; i++) {
			msgbody[i] = a[i];
		}*/
		msgbody[0] = 0x00;
		msgbody[1] = 0x00;
		msgbody[2] = 0x00;
		msgbody[3] = 0x00;
		msgbody[4] = 0x00;
		msgbody[5] = 0x00;
		msgbody[6] = 0x00;
		msgbody[7] = 0x01;
		
		msgbody[8] = 0x01;//Pk_total
		msgbody[9] = 0x01;//Pk_number
		
		msgbody[10] = 0x01;//Registered_Delivery
		msgbody[11] = 0x00;//Msg_level
		
		String Service_Id = "szxx";//Service_Id
		byte[] b = Service_Id.getBytes();
		for (int i = 0; i < b.length; i++) {
			msgbody[i+12] = b[i];
		}
		
		msgbody[16] = 0x00;
		msgbody[17] = 0x00;
		msgbody[18] = 0x00;
		msgbody[19] = 0x00;
		msgbody[20] = 0x00;
		msgbody[21] = 0x00;
		
		msgbody[22] = 0x00;//Fee_UserType
		//Fee_terminal_Id
		msgbody[23] = 0x00;
		msgbody[24] = 0x00;
		msgbody[25] = 0x00;
		msgbody[26] = 0x00;
		msgbody[27] = 0x00;
		msgbody[28] = 0x00;
		msgbody[29] = 0x00;
		msgbody[30] = 0x00;
		msgbody[31] = 0x00;
		msgbody[32] = 0x00;
		msgbody[33] = 0x00;
		msgbody[34] = 0x00;
		msgbody[35] = 0x00;
		msgbody[36] = 0x00;
		msgbody[37] = 0x00;
		msgbody[38] = 0x00;
		msgbody[39] = 0x00;
		msgbody[40] = 0x00;
		msgbody[41] = 0x00;
		msgbody[42] = 0x00;
		msgbody[43] = 0x00;
		msgbody[44] = 0x00;
		msgbody[45] = 0x00;
		msgbody[46] = 0x00;
		msgbody[47] = 0x00;
		msgbody[48] = 0x00;
		msgbody[49] = 0x00;
		msgbody[50] = 0x00;
		msgbody[51] = 0x00;
		msgbody[52] = 0x00;
		msgbody[53] = 0x00;
		msgbody[54] = 0x00;
		
		msgbody[55] = 0x00;//Fee_terminal_type
		
		msgbody[56] = 0x00;//TP_pId
		msgbody[57] = 0x00;//TP_udhi
		
		msgbody[58] = 0x08;//Msg_Fmt
		
		//Msg_src
		msgbody[59] = 0x30;
		msgbody[60] = 0x39;
		msgbody[61] = 0x31;
		msgbody[62] = 0x34;
		msgbody[63] = 0x31;
		msgbody[64] = 0x36;
		
		//String FeeType = "01";//FeeType
		msgbody[65] = 0x30;
		msgbody[66] = 0x31;
		
		//String FeeCode = "000000";//FeeCode
		msgbody[67] = 0x30;
		msgbody[68] = 0x30;
		msgbody[69] = 0x30;
		msgbody[70] = 0x30;
		msgbody[71] = 0x30;
		msgbody[72] = 0x30;
		
		//ValId_Time(17)
		msgbody[73] = 0x00;
		msgbody[74] = 0x00;
		msgbody[75] = 0x00;
		msgbody[76] = 0x00;
		msgbody[77] = 0x00;
		msgbody[78] = 0x00;
		msgbody[79] = 0x00;
		msgbody[80] = 0x00;
		msgbody[81] = 0x00;
		msgbody[82] = 0x00;
		msgbody[83] = 0x00;
		msgbody[84] = 0x00;
		msgbody[85] = 0x00;
		msgbody[86] = 0x00;
		msgbody[87] = 0x00;
		msgbody[88] = 0x00;
		msgbody[89] = 0x00;
		//At_Time(17)
		msgbody[90] = 0x00;
		msgbody[91] = 0x00;
		msgbody[92] = 0x00;
		msgbody[93] = 0x00;
		msgbody[94] = 0x00;
		msgbody[95] = 0x00;
		msgbody[96] = 0x00;
		msgbody[97] = 0x00;
		msgbody[98] = 0x00;
		msgbody[99] = 0x00;
		msgbody[100] = 0x00;
		msgbody[101] = 0x00;
		msgbody[102] = 0x00;
		msgbody[103] = 0x00;
		msgbody[104] = 0x00;
		msgbody[105] = 0x00;
		msgbody[106] = 0x00;
		
		//Src_Id1064899091416
		//String Src_Id = "1064899091416";
		msgbody[107] = 0x31;
		msgbody[108] = 0x30;
		msgbody[109] = 0x36;
		msgbody[110] = 0x34;
		msgbody[111] = 0x38;
		msgbody[112] = 0x39;
		msgbody[113] = 0x39;
		msgbody[114] = 0x30;
		msgbody[115] = 0x39;
		msgbody[116] = 0x31;
		msgbody[117] = 0x34;
		msgbody[118] = 0x31;
		msgbody[119] = 0x36;
		msgbody[120] = 0x00;
		msgbody[121] = 0x00;
		msgbody[122] = 0x00;
		msgbody[123] = 0x00;
		msgbody[124] = 0x00;
		msgbody[125] = 0x00;
		msgbody[126] = 0x00;
		msgbody[127] = 0x00;
		
		msgbody[128] = 0x01;//DestUsr_tl
		
		//Dest_terminal_Id接收短信的MSISDN号码//1064853187600
		msgbody[129] = 0x31;
		msgbody[130] = 0x30;
		msgbody[131] = 0x36;
		msgbody[132] = 0x34;
		msgbody[133] = 0x38;
		msgbody[134] = 0x35;
		msgbody[135] = 0x33;
		msgbody[136] = 0x31;
		msgbody[137] = 0x38;
		msgbody[138] = 0x37;
		msgbody[139] = 0x36;
		msgbody[140] = 0x30;
		msgbody[141] = 0x30;
		msgbody[142] = 0x00;
		msgbody[143] = 0x00;
		msgbody[144] = 0x00;
		msgbody[145] = 0x00;
		msgbody[146] = 0x00;
		msgbody[147] = 0x00;
		msgbody[148] = 0x00;
		msgbody[149] = 0x00;
		msgbody[150] = 0x00;
		msgbody[151] = 0x00;
		msgbody[152] = 0x00;
		msgbody[153] = 0x00;
		msgbody[154] = 0x00;
		msgbody[155] = 0x00;
		msgbody[156] = 0x00;
		msgbody[157] = 0x00;
		msgbody[158] = 0x00;
		msgbody[159] = 0x00;
		msgbody[160] = 0x00;
		
		
		msgbody[161] = 0x00;//Dest_terminal_type
		
		msgbody[162] = 0x08;//Msg_Length
		
				
		return msgbody;
	}
	
	//获得消息头
	public byte[] getMsgHeader(int msgLength){
		
		//计算整个消息的长度
		byte[] msgHead = new byte[12];
		
		int Total_Length = 12 + msgLength;//
		byte[] a = int2byteG(Total_Length);//Total_Length
		
		byte[] b = int2byteG(4);//Command_Id//0X00000004
		
		byte[] c = int2byteG(3);
		
		
		msgHead[0] = a[0];
		msgHead[1] = a[1];
		msgHead[2] = a[2];
		msgHead[3] = a[3];
		msgHead[4] = 0x00;
		msgHead[5] = 0x00;
		msgHead[6] = 0x00;
		msgHead[7] = 0x04;
		msgHead[8] = c[0];
		msgHead[9] = c[1];
		msgHead[10] = c[2];
		msgHead[11] = c[3];
		
		return msgHead;
	}
	
	public static byte[] int2byteG(int res) {  
		byte[] targets = new byte[4];  
		targets[0] = (byte) ((res >> 24) & 0xff);// 最高位,无符号右移。  
		targets[1] = (byte) ((res >> 16) & 0xff);// 次高位   
		targets[2] = (byte) ((res >> 8)  & 0xff);// 次低位   
		targets[3] = (byte) (res & 0xff);// 最低位   
		
		return targets;   
	} 
	
	/**
	 * 拆除与ISMG的链接
	 * @return
	 */
	public   boolean cancelISMG(){
		try {
			MsgHead head=new MsgHead();
			head.setTotalLength(12);//消息总长度，级总字节数:4+4+4(消息头)+6+16+1+4(消息主体)
			head.setCommandId(MsgCommand.CMPP_TERMINATE);//标识创建连接
			head.setSequenceId(MsgUtils.getSequence());//序列，由我们指定
			
//			List<byte[]> dataList=new ArrayList<byte[]>();
//			dataList.add(head.toByteArry());
			CmppSender sender=new CmppSender(out,in,head.toByteArry());
			sender.start();
			getSocketInstance().close();
			out.close();
			in.close();
			return true;
		} catch (Exception e) {
			try {
				out.close();
				in.close();
			} catch (IOException e1) {
				in=null;
				out=null;
			}
			System.out.println("拆除与ISMG的链接"+e.getMessage());
			return false;
		}
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
			System.out.println("链路检查"+e.getMessage());	
			return false;
		}
	}
	/**
	 * 发送web push 短短信
	 * @param url wap网址
	 * @param desc 描述
	 * @param cusMsisdn 短信
	 * @return
	 */
	private  boolean sendShortWapPushMsg(String url,String desc,String cusMsisdn){
		try {
			//length 12
			byte[] szWapPushHeader1={0x0B, 0x05, 0x04, 0x0B,(byte)0x84, 0x23, (byte)0xF0, 0x00, 0x03,0x03, 0x01, 0x01};
			//length 9
			byte[] szWapPushHeader2={0x29, 0x06, 0x06, 0x03, (byte)0xAE,(byte) 0x81, (byte)0xEA, (byte)0x8D, (byte)0xCA};
			//length 9
			byte[] szWapPushIndicator ={0x02, 0x05, 0x6A, 0x00, 0x45,(byte)0xC6,0x08,0x0C, 0x03};
			//去除了http://前缀的UTF8编码的Url地址"的二进制编码 
			byte[] szWapPushUrl = url.getBytes("utf-8");
			//length 3
			byte[] szWapPushDisplayTextHeader={0x00, 0x01, 0x03};
			//想在手机上显示的关于这个URL的文字说明,UTF8编码的二进制 
			byte szMsg[] =desc.getBytes("utf-8");
			//length 3
			byte[] szEndOfWapPush={0x00, 0x01, 0x01};
			int msgLength=12+9+9+szWapPushUrl.length+3+szMsg.length+3;
			int seq=MsgUtils.getSequence();
			MsgSubmit submit=new MsgSubmit();
			submit.setTotalLength(12+8+1+1+1+1+10+1+32+1+1+1+1+6+2+6+17+17+21+1+32+1+1+msgLength+20);
			submit.setCommandId(MsgCommand.CMPP_SUBMIT);
			submit.setSequenceId(seq);			
			submit.setPkTotal((byte)0x01);
			submit.setPkNumber((byte)0x01);			
			submit.setRegisteredDelivery((byte)0x00);
			submit.setMsgLevel((byte)0x01);
			submit.setFeeUserType((byte)0x00);
			submit.setFeeTerminalId("");			
			submit.setFeeTerminalType((byte)0x00);			
			submit.setTpPId((byte)0x00);
			submit.setTpUdhi((byte)0x01);
			submit.setMsgFmt((byte)0x04);
			submit.setMsgSrc(msgConfig.getSpId());
			submit.setSrcId(msgConfig.getSpCode());
			submit.setDestTerminalId(cusMsisdn);
			submit.setMsgLength((byte)msgLength);
			byte[] sendMsg=new byte[12+9+9+szWapPushUrl.length+3+szMsg.length+3];	
			System.arraycopy(szWapPushHeader1, 0, sendMsg,0,12);
			System.arraycopy(szWapPushHeader2, 0, sendMsg,12,9);
			System.arraycopy(szWapPushIndicator,0,sendMsg,12+9,9);
			System.arraycopy(szWapPushUrl,0,sendMsg,12+9+9,szWapPushUrl.length);
			System.arraycopy(szWapPushDisplayTextHeader,0,sendMsg,12+9+9+szWapPushUrl.length,3);
			System.arraycopy(szMsg,0,sendMsg,12+9+9+szWapPushUrl.length+3,szMsg.length);
			System.arraycopy(szEndOfWapPush,0,sendMsg,12+9+9+szWapPushUrl.length+3+szMsg.length,3);
			submit.setMsgContent(sendMsg);
//			List<byte[]> dataList=new ArrayList<byte[]>();
//			dataList.add(submit.toByteArry());
			CmppSender sender=new CmppSender(out,in,submit.toByteArry());
			sender.start();
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"向"+cusMsisdn+"下发web push短短信，序列号为:"+seq);
			return true;
		} catch (Exception e) {
			try {
				out.close();
			} catch (IOException e1) {
				out=null;
			}
			System.out.println("发送web push短短信"+e.getMessage());
			return false;
		}
	}
	/**
	 * 发送web push 长短信
	 * @param url wap网址
	 * @param desc 描述
	 * @param cusMsisdn 短信
	 * @return
	 */
	private  boolean sendLongWapPushMsg(String url,String desc,String cusMsisdn){
		try {
//			List<byte[]> dataList=new ArrayList<byte[]>();
			//length 12
			byte[] wdp={0x0B, 0x05, 0x04, 0x0B,(byte)0x84, 0x23, (byte)0xF0, 0x00, 0x03,0x03, 0x01, 0x01};
			//需要拆分的部分
			//length 9
			byte[] wsp={0x29, 0x06, 0x06, 0x03, (byte)0xAE,(byte) 0x81, (byte)0xEA, (byte)0x8D, (byte)0xCA};
			//length 9
			byte[] szWapPushIndicator ={0x02, 0x05, 0x6A, 0x00, 0x45,(byte)0xC6,0x08,0x0C, 0x03};
			//去除了http://前缀的UTF8编码的Url地址"的二进制编码 
			byte[] szWapPushUrl = url.getBytes("utf-8");
			//length 3
			byte[] szWapPushDisplayTextHeader={0x00, 0x01, 0x03};
			//想在手机上显示的关于这个URL的文字说明,UTF8编码的二进制 
			byte szMsg[] =desc.getBytes("utf-8");
			//length 3
			byte[] szEndOfWapPush={0x00, 0x01, 0x01};
			byte[] allByte=new byte[9+9+szWapPushUrl.length+3+szMsg.length+3];
			
			System.arraycopy(wsp,0, allByte,0,9);
			System.arraycopy(szWapPushIndicator,0, allByte,9,9);
			System.arraycopy(szWapPushUrl,0, allByte,18,szWapPushUrl.length);
			System.arraycopy(szWapPushDisplayTextHeader,0, allByte,18+szWapPushUrl.length,3);
			System.arraycopy(szMsg,0, allByte,18+szWapPushUrl.length+3,szMsg.length);
			System.arraycopy(szEndOfWapPush,0,allByte,18+szWapPushUrl.length+3+szMsg.length,3);
			int msgMax=140;
			int msgCount=allByte.length%(msgMax-wdp.length)==0?allByte.length/(msgMax-wdp.length):allByte.length/(msgMax-wdp.length)+1;
			wdp[10]=(byte)msgCount;
			int seqId=MsgUtils.getSequence();
			for(int i=0;i<msgCount;i++){
				wdp[11]=(byte)(i+1);
				byte[] needMsg=null;
				//消息头+消息内容拆分
				if(i!=msgCount-1){
					int start=(msgMax-wdp.length)*i;
					int end=(msgMax-wdp.length)*(i+1);
					needMsg=MsgUtils.getMsgBytes(allByte,start,end);
				}else{
					int start=(msgMax-wdp.length)*i;
					int end=allByte.length;
					needMsg=MsgUtils.getMsgBytes(allByte,start,end);
				}
				int msgLength=needMsg.length+wdp.length;
				MsgSubmit submit=new MsgSubmit();
				submit.setTotalLength(12+8+1+1+1+1+10+1+32+1+1+1+1+6+2+6+17+17+21+1+32+1+1+msgLength+20);
				submit.setCommandId(MsgCommand.CMPP_SUBMIT);
				submit.setSequenceId(seqId);	
				submit.setPkTotal((byte)msgCount);
				submit.setPkNumber((byte)(i+1));			
				submit.setRegisteredDelivery((byte)0x00);
				submit.setMsgLevel((byte)0x01);
				submit.setFeeUserType((byte)0x00);
				submit.setFeeTerminalId("");			
				submit.setFeeTerminalType((byte)0x00);			
				submit.setTpPId((byte)0x00);
				submit.setTpUdhi((byte)0x01);
				submit.setMsgFmt((byte)0x04);
				submit.setMsgSrc(msgConfig.getSpId());
				submit.setSrcId(msgConfig.getSpCode());
				submit.setDestTerminalId(cusMsisdn);
				submit.setMsgLength((byte)msgLength);
				byte[] sendMsg=new byte[wdp.length+needMsg.length];	
				System.arraycopy(wdp,0, sendMsg, 0, wdp.length);
				System.arraycopy(needMsg,0,sendMsg,wdp.length,needMsg.length);
				submit.setMsgContent(sendMsg);				
//				dataList.add(submit.toByteArry());
				
				CmppSender sender=new CmppSender(out,in,submit.toByteArry());
				sender.start();
			}
			
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"向"+cusMsisdn+"下发web pus长短信，序列号为:"+seqId);
			return true;
		} catch (Exception e) {
			try {
				out.close();
			} catch (IOException e1) {
				out=null;
			}
			System.out.println("发送web push长短信"+e.getMessage());
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
	
	
	public void disConnetion(){
		try {
			in.close();
			out.close();
			msgSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public MsgConfig getMsgConfig() {
		return msgConfig;
	}
	public void setMsgConfig(MsgConfig msgConfig) {
		this.msgConfig = msgConfig;
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
	
	/**public static void main(String[] args) throws Exception{
		MsgContainer mc =new MsgContainer();
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
		//Thread receiveThread = new Thread(new CmppReceive(mc,));
        //receiveThread.start();
        
	}*/
}
