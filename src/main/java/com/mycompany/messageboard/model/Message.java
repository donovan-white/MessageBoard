package com.mycompany.messageboard.model;

import java.io.Serializable;

/**
 *
 * @author ubuntu
 */
public class Message implements Serializable {   
    private String msg;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * @return the msg
     */    
    public String getMsg() {
        return msg;
    }
    
    /**
     * @param msg the message to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
    //</editor-fold>
}
