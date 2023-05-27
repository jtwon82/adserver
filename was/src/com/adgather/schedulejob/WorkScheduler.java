/*
 * AdSapient - Open Source Ad Server
 * http://www.sourceforge.net/projects/adsapient
 * http://www.adsapient.com
 *
 * Copyright (C) 2001-06 Vitaly Sazanovich
 * Vitaly.Sazanovich@gmail.com
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License  as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */
package com.adgather.schedulejob;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.xml.JobSchedulingBundle;
import org.quartz.xml.JobSchedulingDataProcessor;

public class WorkScheduler extends JobSchedulingDataProcessor {
    
	private static Logger logger = Logger.getLogger(WorkScheduler.class);
    private Properties quartzProperties;
    private String pathToJobs;
    private Map<String, String> quartzPropertiesMap;

    public Scheduler getSched() {
        return sched;
    }

    public void setSched(Scheduler sched) {
        this.sched = sched;
    }

    private Scheduler sched;
    //public WorkScheduler(){
    	//setup();
    //}
    public void setup() {
        try {
            SchedulerFactory schedFact = new StdSchedulerFactory();
            ((StdSchedulerFactory) schedFact).initialize(quartzProperties);
            sched = schedFact.getScheduler();
            String prefix = String.valueOf(System.currentTimeMillis());
            this.processFile(pathToJobs);
            for (Object j : jobsToSchedule) {
                JobSchedulingBundle jsb = (JobSchedulingBundle) j;
                String newJobName = prefix + jsb.getJobDetail().getName();
                jsb.getJobDetail().setName(newJobName);
                List<Trigger> l = jsb.getTriggers();
                for (Trigger t : l) {
                    t.setName(prefix + t.getName());
                    t.setJobName(newJobName);
                }
                this.addScheduledJob(jsb);
            }

            this.scheduleJobs(this.getScheduledJobs(), sched, true);
            sched.start();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public String getPathToJobs() {
        return pathToJobs;
    }

    public void setPathToJobs(String pathToJobs) {
        this.pathToJobs = pathToJobs;
    }

    public Properties getQuartzProperties() {
        return quartzProperties;
    }

    public void setQuartzProperties(Properties quartzProperties) {
        this.quartzProperties = quartzProperties;
    }

    public Map<String, String> getQuartzPropertiesMap() {
        return quartzPropertiesMap;
    }

    public void setQuartzPropertiesMap(Map<String, String> quartzPropertiesMap) {
        this.quartzPropertiesMap = quartzPropertiesMap;
    }
}
