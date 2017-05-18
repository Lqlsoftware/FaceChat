package com.lqlsoftware.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MsgEncoder implements Encoder.BinaryStream<String>, Decoder.BinaryStream<ByteBuffer> {  
 
    @Override  
    public void init(EndpointConfig config) {     }  
  
    @Override  
    public void destroy() {    }  
  
    //°æ3°ø µœ÷encoder   
    @Override  
    public void encode(String Msg, OutputStream outputStream) throws EncodeException, IOException {  
    	System.out.print("1");
    }

	@Override
	public ByteBuffer decode(InputStream inputStream) throws DecodeException, IOException {
		return null;
	}
}  