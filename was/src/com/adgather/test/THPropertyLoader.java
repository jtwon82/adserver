package com.adgather.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
public class THPropertyLoader
{
 
    public THPropertyLoader()
    {}
 
    /**
     * properties 파일 읽기
     */
     public String read(String propKey)
     {
         Properties  pp  = new Properties();
         try
         {
              FileInputStream fis  = new FileInputStream("WEB-INF/classes/quartz.properties");
              pp.load(fis);
         }
         catch (IOException ioe)
         {
              ioe.printStackTrace();
              System.exit(-1);
         }
        return pp.getProperty(propKey);
     }
}