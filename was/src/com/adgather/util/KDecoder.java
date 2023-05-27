package com.adgather.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.adgather.servlet.RFServlet;
public class KDecoder {
	static Logger logger = Logger.getLogger(RFServlet.class);
	public KDecoder() {
		// TODO Auto-generated constructor stub
	}  
	  
	public String decoderkeywordString(String keyword){
		String temp = "";		
		keyword = keyword.replace("%20", " ");
		if(isUnicode(keyword)) {		//유니코드인코딩			
			temp = GetUnicodeDecode(keyword);
		} else {
			if(!keyword.equals("none")) {
			 	byte [] bytes = getBytes(keyword);
				if(bytes==null) { 
					temp = keyword;
				} else {
					try {
						temp = URLDecoder.decode(keyword,utfOrEuc(bytes));
					}catch(UnsupportedEncodingException e) {
						temp = keyword;
					}
				}
			} 
		}		
		return temp;
	}
	
	public String encodingkeywordString(String encodingstr, String type) {
		String temp = "";
		try {
			temp = URLEncoder.encode(encodingstr,type);			
		}catch(UnsupportedEncodingException e) {
			temp = "";
		}
		return temp;
	}
	public String decodingkeywordString(String decodingstr, String type) {
		String temp = "";
		try {
			temp = URLDecoder.decode(decodingstr,type);			
		}catch(UnsupportedEncodingException e) {
			temp = "";
		}
		return temp;
	}
	
	public Boolean isUnicode(String str) {
		if(str.indexOf("%u") > -1) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean isUnKnowun(String str) {
		if(str.indexOf("\\x") > -1) {
			return true;
		} else {
			return false;
		}
	}
	
	public String GetUnicodeDecode(String uniStr) {
		StringBuffer str = new StringBuffer();
	    for( int i= uniStr.indexOf("%u") ; i > -1 ; i = uniStr.indexOf("%u") ){    // euc-kr(%u), utf-8(//u)	    	
	        str.append( uniStr.substring( 0, i ) );	        
	        str.append( String.valueOf( (char)Integer.parseInt( uniStr.substring( i + 2, i + 6 ) ,16) ) );
	        uniStr = uniStr.substring( i +6);	        
	    }
	    str.append( uniStr );	    
	   return str.toString();
	}	
	
	public byte[] getBytes(String s) {		
		int numChars = s.length();	
		int i = 0;
		char c;
		byte[] bytes = null;
		int loopBreak=0;
		while (i < numChars) {
			try {
				c = s.charAt(i);
		        switch (c) {
			    	case '+':		    
			    		i++;		
			    		break;
			    	case '%':			
			    		try {			    
			    			if (bytes == null) bytes = new byte[(numChars-i)/3];
			    			int pos = 0;			    
			    			while ( ((i+2) < numChars) && (c=='%')) {
			    				bytes[pos++] = (byte)Integer.parseInt(s.substring(i+1,i+3),16);
			    				i+= 3;
			    				if (i < numChars) c = s.charAt(i);
								if(i>10000) break;
			    			}
			    			if ((i < numChars) && (c=='%')) {
			    				System.out.println("getbyte 오류.." + s);
			    				bytes = null;
			    				i += 10000;
			    			}   
			    			
			    		} catch (NumberFormatException e) {}		    		  
			    		break;
			    	default: 
			    		//System.out.println(c); 
			    	i++;
			    	break;
		        }
				loopBreak++;
				if(loopBreak>10000) break;
			} catch (Exception e) {
				System.out.println("getbyte 오류2.." + e+":"+s);
                bytes = null;
                break;
			}
		}
		return bytes;
	}
	
	public String utfOrEuc(byte[] the_text) {			
		boolean all_7_bit_codes = true;
		int  the_char;
		int length = the_text.length;
		int MAX                = 3000;
		if (length > MAX) length = MAX;
		for (int i = 0; i < length; i++) {
			try{
				the_char = ((int)the_text[i] & 0xFF);
				if (the_char > 127) {
					all_7_bit_codes = false;            
					if (the_char >= 192 && the_char <= 223) {	               
						i++;
						the_char = ((int)the_text[i] & 0xFF);
						if (the_char < 128 || the_char > 191) {
							return "euc-kr";
						}
					}else if (the_char >= 224 && the_char <= 239) {//237 131 173
						i++;
						the_char = ((int)the_text[i] & 0xFF);
						if (the_char < 128 || the_char > 191) {
							return "euc-kr";
						}	               
						i++;
						the_char = ((int)the_text[i] & 0xFF);
						if (the_char < 128 || the_char > 191) {
							return "euc-kr";
						}
					}else {
						return "euc-kr";
					}
				}
			}catch(Exception e){
				break;
			}
		}
		if (all_7_bit_codes) return "euc-kr";
		return "utf-8";
    }
}
