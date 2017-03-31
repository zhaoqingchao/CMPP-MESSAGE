package com.ztesoft.iot.cqmessage.domain;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * CMPP_DELIVER操作的目的是ISMG把从短信中心或其它ISMG转发来的短信送交SP，SP以CMPP_DELIVER_RESP消息回应。
 * @author 张科伟
 * 2011-09-04 11:42
 */
public class MsgDeliver extends MsgHead {
	
	private Log logger = LogFactory.getLog(this.getClass());
	private long msg_Id;
	private String dest_Id;//21 目的号码 String 
	private String service_Id;//10 业务标识  String 
	private byte tP_pid = 0;
	private byte tP_udhi = 0;
	private byte msg_Fmt = 15;
	private String src_terminal_Id;//源终端MSISDN号码
	private byte src_terminal_type = 0;//源终端号码类型，0：真实号码；1：伪码
	private byte registered_Delivery = 0;//是否为状态报告 0：非状态报告1：状态报告
	private int msg_Length;//消息长度
	//private byte msg_Length;//消息长度
	private String msg_Content;//消息长度
	private String linkID;
	
	private long msg_Id_report;
	private String stat;
	private String submit_time;
	private String done_time;
	private String dest_terminal_Id;
	private int sMSC_sequence;
	private int result;//解析结果
	public MsgDeliver(byte[] data){
		if(data.length>8+8+21+10+1+1+1+32+1+1+1+20){//+Msg_length+
			String fmtStr="gb2312";
			ByteArrayInputStream bins=new ByteArrayInputStream(data);
			DataInputStream dins=new DataInputStream(bins);
			try {
				this.setTotalLength(data.length+4);
				this.setCommandId(dins.readInt());
				this.setSequenceId(dins.readInt());
				this.msg_Id=dins.readLong();//Msg_Id
				byte[] destIdByte=new byte[21];
				dins.read(destIdByte);
				this.dest_Id=new String(destIdByte);//21 目的号码 String 
				byte[] service_IdByte=new byte[10];
				dins.read(service_IdByte);
				this.service_Id=new String(service_IdByte);//10 业务标识  String 
				this.tP_pid = dins.readByte();
				this.tP_udhi = dins.readByte();
				this.msg_Fmt = dins.readByte();
				fmtStr=this.msg_Fmt==8?"UnicodeBigUnmarked":"gb2312";
				byte[] src_terminal_IdByte=new byte[32];
				dins.read(src_terminal_IdByte);
				this.src_terminal_Id=new String(src_terminal_IdByte);//源终端MSISDN号码
				this.src_terminal_type = dins.readByte();//源终端号码类型，0：真实号码；1：伪码
				this.registered_Delivery = dins.readByte();//是否为状态报告 0：非状态报告1：状态报告
				//this.msg_Length=dins.readByte();//消息长度
				msg_Length = dins.read();
				byte[] msg_ContentByte=new byte[msg_Length];
				dins.read(msg_ContentByte);
				if(registered_Delivery==1){
					this.msg_Content=new String(msg_ContentByte,fmtStr);//消息长度
					if(msg_Length==8+7+10+10+21+4){
						ByteArrayInputStream binsC=new ByteArrayInputStream(data);
						DataInputStream dinsC=new DataInputStream(binsC);
						this.msg_Id_report=dinsC.readLong();
						byte[] startByte=new byte[7];
						dinsC.read(startByte);
						this.stat=new String(startByte,fmtStr);
						byte[] submit_timeByte=new byte[10];
						dinsC.read(submit_timeByte);
						this.submit_time=new String(submit_timeByte,fmtStr);
						byte[] done_timeByte=new byte[7];
						dinsC.read(done_timeByte);
						this.done_time=new String(done_timeByte,fmtStr);
						byte[] dest_terminal_IdByte=new byte[21];
						dinsC.read(dest_terminal_IdByte);
						this.dest_terminal_Id=new String(dest_terminal_IdByte,fmtStr);
						this.sMSC_sequence=dinsC.readInt();
						dinsC.close();
						binsC.close();
						this.result=0;//正确
					}else{
						this.result=1;//消息结构错
					}
				}else{
					this.msg_Content=new String(msg_ContentByte,fmtStr);//消息长度
				}
				byte[] linkIDByte=new byte[20];
				this.linkID=new String(linkIDByte);
				this.result=0;//正确
				dins.close();
				bins.close();
			} catch (IOException e){
				this.result=8;//消息结构错
			}
		}else{
			this.result=1;//消息结构错
			System.out.println("短信网关CMPP_DELIVER,解析数据包出错，包长度不一致。长度为:"+data.length);
			logger.info("短信网关CMPP_DELIVER,解析数据包出错，包长度不一致。长度为:"+data.length);
		}
	}
	public long getMsg_Id() {
		return msg_Id;
	}

	public void setMsg_Id(long msg_Id) {
		this.msg_Id = msg_Id;
	}

	public String getDest_Id() {
		return dest_Id;
	}

	public void setDest_Id(String dest_Id) {
		this.dest_Id = dest_Id;
	}

	public String getService_Id() {
		return service_Id;
	}

	public void setService_Id(String service_Id) {
		this.service_Id = service_Id;
	}

	public byte getTP_pid() {
		return tP_pid;
	}

	public void setTP_pid(byte tp_pid) {
		tP_pid = tp_pid;
	}

	public byte getTP_udhi() {
		return tP_udhi;
	}

	public void setTP_udhi(byte tp_udhi) {
		tP_udhi = tp_udhi;
	}

	public byte getMsg_Fmt() {
		return msg_Fmt;
	}

	public void setMsg_Fmt(byte msg_Fmt) {
		this.msg_Fmt = msg_Fmt;
	}

	public String getSrc_terminal_Id() {
		return src_terminal_Id;
	}

	public void setSrc_terminal_Id(String src_terminal_Id) {
		this.src_terminal_Id = src_terminal_Id;
	}

	public byte getSrc_terminal_type() {
		return src_terminal_type;
	}

	public void setSrc_terminal_type(byte src_terminal_type) {
		this.src_terminal_type = src_terminal_type;
	}

	public byte getRegistered_Delivery() {
		return registered_Delivery;
	}

	public void setRegistered_Delivery(byte registered_Delivery) {
		this.registered_Delivery = registered_Delivery;
	}

	public int getMsg_Length() {
		return msg_Length;
	}
	public void setMsg_Length(int msg_Length) {
		this.msg_Length = msg_Length;
	}
	public String getMsg_Content() {
		return msg_Content;
	}
	public void setMsg_Content(String msg_Content) {
		this.msg_Content = msg_Content;
	}
	public String getLinkID() {
		return linkID;
	}
	public void setLinkID(String linkID) {
		this.linkID = linkID;
	}

	public long getMsg_Id_report() {
		return msg_Id_report;
	}

	public void setMsg_Id_report(long msg_Id_report) {
		this.msg_Id_report = msg_Id_report;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getSubmit_time() {
		return submit_time;
	}

	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
	}

	public String getDone_time() {
		return done_time;
	}

	public void setDone_time(String done_time) {
		this.done_time = done_time;
	}

	public String getDest_terminal_Id() {
		return dest_terminal_Id;
	}

	public void setDest_terminal_Id(String dest_terminal_Id) {
		this.dest_terminal_Id = dest_terminal_Id;
	}

	public int getSMSC_sequence() {
		return sMSC_sequence;
	}

	public void setSMSC_sequence(int smsc_sequence) {
		sMSC_sequence = smsc_sequence;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	
	/*public static byte[] hexToBytes(String hexString) {  
	    char[] hex = hexString.toCharArray();  
	    // 转rawData长度减半  
	    int length = hex.length / 2;  
	    byte[] rawData = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        // 先将hex转10进位数值  
	        int high = Character.digit(hex[i * 2], 16);  
	        int low = Character.digit(hex[i * 2 + 1], 16);  
	        // 將第一個值的二進位值左平移4位,ex: 00001000 => 10000000 (8=>128)  
	        // 然后与第二个值的二进位值作联集ex: 10000000 | 00001100 => 10001100 (137)  
	        int value = (high << 4) | low;  
	        // 与FFFFFFFF作补集  
	        if (value > 127) {  
	            value -= 256;  
	        }  
	        // 最后转回byte就OK  
	        rawData[i] = (byte) value;  
	    }  
	    return rawData;  
	}  */
	
	
//	public static void main(String[] args) {
//		String str = "00000005134da03c37bec1400002bd0e3130363438393930393031383700000000000000000000000000000000000000000031303634383533383235363037000000000000000000000000000000000000000000823c42534a2a443a3231312e3136322e3132352e3230302c373738382a543a302e302e302e302c302a413a434d4e45542a4e3a31343133303039363338372a47503a4f4b2a43475245473a312a4353513a32332a43533a3132302a433a33302f33302a4f3a3132302f3132302a483a3138302f3138302a4750533a372a5a3a3132303e0000000000000000000000000000000000000000";
//		String str1 = "00000005132ac25137bec080000f4bf5313036343839393039313431360000000000000000000000000000000000000000083130363438353331383736303000000000000000000000000000000000000000000008624b673a53d190010000000000000000000000000000000000000000";
//		byte[] b = hexToBytes(str1);
//		//byte[] b = str.getBytes();
//		MsgDeliver msgDeliver = new MsgDeliver(b);
//		System.out.println(msgDeliver.getCommandId());
//		System.out.println(msgDeliver.getMsg_Content());
//		
//	}
}
