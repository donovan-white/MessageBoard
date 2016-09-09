package com.mycompany.messageboard.controller;

import com.mycompany.messageboard.model.Message;
import com.timgroup.statsd.StatsDClient;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

/**
 *
 * @author ubuntu
 */
@Named
@SessionScoped
public class IndexController implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
    private static final String STATSD_METRIC_NUMBER_SUBMISSIONS = "number_submissions";
    private static final String STATSD_METRIC_MESSAGE_LENGTH = "message_length";
    private static final String STATSD_METRIC_DURATION = "duration";
    private static final String STATSD_METRIC_UNIQUE_SUBMISSIONS = "unique_submissions";
    private StatsDClient statsdClient;
    private String dataDir;
    private Message inputMessage;

    public IndexController() {

    }

    public void processMessage() {
        // record starting time
        long startTime = System.currentTimeMillis();

        // log event
        LOGGER.info("User entered \"" + getInputMessage().getMsg() + "\"");

        // record stats
        recordStats(getInputMessage().getMsg(), System.currentTimeMillis() - startTime);

        // write file to dir
        writeToDir(getDataDir(), getInputMessage().getMsg());

        // clear user input
        setInputMessage(new Message());
    }

    private void recordStats(String message, long duration) {
        getStatsdClient().incrementCounter(IndexController.STATSD_METRIC_NUMBER_SUBMISSIONS);
        getStatsdClient().recordGaugeValue(IndexController.STATSD_METRIC_MESSAGE_LENGTH, message.length());
        getStatsdClient().recordExecutionTime(IndexController.STATSD_METRIC_DURATION, duration);
        getStatsdClient().recordSetEvent(IndexController.STATSD_METRIC_UNIQUE_SUBMISSIONS, message);
    }

    private void writeToDir(String dir, String message) {
        try (FileOutputStream fos = new FileOutputStream(File.createTempFile("msg-", ".txt", new File(dir)), true)) {
            fos.write(message.getBytes());
        } catch (IOException ex) {
            LOGGER.error("Exception writing message to disk.", ex);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">  
    /**
     * @return the inputMessage
     */
    public Message getInputMessage() {
        if (inputMessage == null) {
            inputMessage = new Message();
        }
        return inputMessage;
    }

    /**
     * @param inputMessage the inputMessage to set
     */
    public void setInputMessage(Message inputMessage) {
        this.inputMessage = inputMessage;
    }

    /**
     * @param statsdClient the statsdClient to set
     */
    public void setStatsdClient(StatsDClient statsdClient) {
        this.statsdClient = statsdClient;
    }

    /**
     * @return the statsdClient
     */
    public StatsDClient getStatsdClient() {
        if (statsdClient == null) {
            ApplicationContext springContext = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
            statsdClient = springContext.getBean("statsdClient", com.timgroup.statsd.StatsDClient.class);
        }
        return statsdClient;
    }

    /**
     * @return the dataDir
     */
    public String getDataDir() {
        if (dataDir == null) {
            ApplicationContext springContext = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
            dataDir = springContext.getBean("dataDir", java.lang.String.class);
        }        
        return dataDir;
    }

    /**
     * @param dataDir the dataDir to set
     */
    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }
    //</editor-fold> 

}
