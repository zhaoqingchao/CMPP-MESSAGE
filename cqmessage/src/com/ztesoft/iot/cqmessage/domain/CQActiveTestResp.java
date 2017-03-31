package com.ztesoft.iot.cqmessage.domain;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CQActiveTestResp extends MsgHead {
	
	public byte[] toByteArry(){
		ByteArrayOutputStream bous=new ByteArrayOutputStream();
		DataOutputStream dous=new DataOutputStream(bous);
		try {
			dous.writeInt(this.getTotalLength());
			dous.writeInt(this.getCommandId());
			dous.write(this.getSeq_Id());
			dous.write(0x00);//Reserved
			dous.close();
		} catch (IOException e) {
			System.out.println("封装链接二进制数组失败。");
		}
		return bous.toByteArray();
	}
	
private byte[] seq_Id;
	
	public byte[] getSeq_Id() {
		return seq_Id;
	}
	public void setSeq_Id(byte[] seq_Id) {
		this.seq_Id = seq_Id;
	}
}
