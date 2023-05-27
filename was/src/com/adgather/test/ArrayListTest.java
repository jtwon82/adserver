package com.adgather.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.adgather.util.DateUtils;
import com.adgather.util.StringUtils;

class aa{
	String a, b, c= null;
	public aa(String []_a){
		setA(_a[0]);
		setB(_a[1]);
		setC(_a[2]);
	}
	public void setA(String _a){		a= _a;		}
	public String getA(){			return a;		}
	public void setB(String _a){		b= _a;		}
	public String getB(){			return b;		}
	public void setC(String _a){		c= _a;		}
	public String getC(){			return c;		}
}
public class ArrayListTest {
	public static void main(String []ar){
		
		String aa = StringUtils.gAt1("aaa", 1, "_");
		
		System.out.println( aa );
	}
	public void test5(){
		if( DateUtils.getAdTimeZone(5000, 0, "a,b,c,d,e,f,g")==0 ){
			System.out.println(1);
		}
		System.out.println(2);
	}
	public void test4(){
		String hm1="UM_610871=1#UM_610872=1#";
//		HashMap<String,ArrayList> r= 
//		
//		Iterator<String> it = r.keySet().iterator();
//		
//		while ( it.hasNext() ){
//			ArrayList arr = (ArrayList)it.next();
//			System.out.println("key="+arr );
//		}
		
		
		
//		 Iterator<String> iterator = r.keySet().iterator();
//		    while (iterator.hasNext()) 
//		    {
//		        String key = (String) iterator.next();
//		        System.out.print("key="+key);
//		        System.out.println(" value="+r.get(key).toString());
//		        
//		    }
		
	}
	public void test3(){
		ArrayList<Integer> rnd= StringUtils.getRandList( 10 );
		
		Iterator it = rnd.iterator();
		
		for( int i=0; it.hasNext(); i++  )
			System.out.println( it.next() +" "+ i);
		
	}
	public void test2(){
		String ic_ki= "aaa|||bbb";
		String scTxt= "bbb";
		if(ic_ki!=null && ic_ki.length()>0){//기존쿠키가 있고..
			if(ic_ki.indexOf(scTxt)<0){//같은 값이 없으면..
				ic_ki=scTxt+"|||"+ic_ki;
			}else{//같은 값이 있으면..
				if(ic_ki.indexOf(scTxt)>0){
					ic_ki= ic_ki.replace("|||"+scTxt,"");
					ic_ki= scTxt+"|||"+ic_ki;
					System.out.println( ic_ki.replaceAll("|||"+scTxt,""));
				}
			}
			if(ic_ki.indexOf("|||"+scTxt)<0){
				if(ic_ki.length()<100)	ic_ki+="|||"+scTxt;
			}
		}else{
			ic_ki=scTxt;
		}
		if(ic_ki.length()>100){
			ic_ki=ic_ki.substring(0,100);
		}
		System.out.println( ic_ki);
	}
	public void test1(){
		ArrayList<aa> a1= new ArrayList<aa>();
		a1.add(new aa(new String[]{"value a1","value b1","value c1"}));
		a1.add(new aa(new String[]{"value a2","value b2","value c2"}));
		a1.add(new aa(new String[]{"value a3","value b3","value c3"}));
		
		for(int i=0; i<a1.size(); i++){
			System.out.println( a1.get(i).getA() );
			System.out.println( a1.get(i).getB() );
			System.out.println( a1.get(i).getC() );
		}
		
		Stack<aa> s1= new Stack<aa>();
		s1.add(new aa(new String[]{"1","2","3"}));
		s1.add(new aa(new String[]{"4","2","3"}));
		s1.add(new aa(new String[]{"5","2","3"}));

		System.out.println( s1.pop().getA());
		System.out.println( s1.pop().getA());
		System.out.println( s1.pop().getA());
		
		ArrayList a= StringUtils.getRandList(10);
		for(int i=0;i<a.size();i++)
			System.out.println( "getRandList="+ a.get(i));
		
	}
}
