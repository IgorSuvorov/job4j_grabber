package ru.job4j.quartz;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    public static Properties load() {
        Properties properties = new Properties();
        try (BufferedReader read = new BufferedReader(new FileReader("rabbit.properties"))) {
            read.lines().filter(line -> line.length() > 0)
                    .forEach(line -> {
                        if (line.startsWith("=") || line.endsWith("=")) {
                            throw new IllegalArgumentException("no key or value");
                        }
                        String[] keyValue = line.split("=");
                        properties.setProperty("interval", keyValue[1]);
                    });
        } catch (IOException e) {
            System.out.println("file not found");
            e.printStackTrace();
        }
        return properties;
    }
    public static void main(String[] args) {
        int interval = Integer.parseInt(load().getProperty("interval"));
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
        }
    }
}