package com.adgather.test;

import java.text.ParseException;

import org.apache.log4j.PropertyConfigurator;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class THJobMain
{
    private SchedulerFactory schedulFactoty = null;
    private Scheduler scheduler = null;
    private JobDetail jobDetail = null;
    private CronTrigger trigger = null;
    private String className = null;
    private String expression = null;
    THPropertyLoader ThProp = new THPropertyLoader();
  
    public THJobMain()
    {
        JobInit();
    }
 
    private void JobInit()
    {
        try
        {
            schedulFactoty = new StdSchedulerFactory();
            scheduler = schedulFactoty.getScheduler();
            scheduler.start();
            JobRegist();
        }
        catch (SchedulerException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void JobRegist()
    {
        String taskGroups = ThProp.read("TaskGroups");
        String taskList = ThProp.read( taskGroups + ".TaskNames");
        String[] arTaskGroups = taskList.split(",");
     
        for ( int i = 0; i < arTaskGroups.length; i++ )
        {
            Class c = null;
            className = ThProp.read( arTaskGroups[i] + ".class" );
            expression = ThProp.read( arTaskGroups[i] + ".Expression" );

            try
            {
                c = Class.forName( className );
            }
            catch (ClassNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jobDetail = new JobDetail( arTaskGroups[i], arTaskGroups[i], c );
            trigger = new CronTrigger( arTaskGroups[i], arTaskGroups[i] );
            try
            {
                trigger.setCronExpression( expression );
                scheduler.scheduleJob(jobDetail, trigger);
            }
            catch (SchedulerException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch ( ParseException e2 )
            {
                e2.printStackTrace();
            }
        }
    }
 
 
    public static void main(String[] args)
    {
        PropertyConfigurator.configure("D:/work/enliple/www/public_html/WEB-INF/classes/log4j.properties");
        new THJobMain();
    }
}