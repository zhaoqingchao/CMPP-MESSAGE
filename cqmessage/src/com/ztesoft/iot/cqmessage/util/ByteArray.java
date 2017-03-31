package com.ztesoft.iot.cqmessage.util;

public class ByteArray {
	private byte[] data = null;
	private int used = 0;
	
	public ByteArray addByte(byte c) {
		if (data == null) {
			data = new byte[16];
		} else if (used>= data.length) {
			byte[] temp = new byte[data.length * 2];
			System.arraycopy(data, 0, temp, 0, used);
			data = temp;
		}
		data[used++] = c;
		return this;
	}

	public byte[] toArray() {
		byte[] chars = new byte[used];
		System.arraycopy(data, 0, chars, 0, used);
		return chars;
	}
}