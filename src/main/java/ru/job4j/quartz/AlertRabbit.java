package ru.job4j.quartz;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    private static Connection initConnection(Properties properties) throws ClassNotFoundException, SQLException {
        Class.forName(properties.getProperty("driver-class-name"));
        Connection connection = DriverManager.getConnection(
                properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password")
        );
        return connection;
    }

    private static Properties load() {
        Properties config = new Properties();
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
        config.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public static void main(String[] args) {
        int interval = Integer.parseInt(load().getProperty("rabbit.interval"));
        try {
            Connection connection = initConnection(load());
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            try {
                Connection connection =
                        (Connection) context.getJobDetail().getJobDataMap().get("connection");
                Statement statement = connection.createStatement();
                String sql = String.format(
                        "insert into rabbit (created_date) values ('%s')",
                        new Timestamp(System.currentTimeMillis())
                );
                statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}