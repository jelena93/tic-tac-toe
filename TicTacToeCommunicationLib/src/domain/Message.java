/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;

/**
 *
 * @author Jelena
 */
public class Message implements Serializable {

    private int messageType;
    private Object message;

    public Message(int messageType) {
        this.messageType = messageType;
    }

    public Message(int messageType, Object message) {
        this.messageType = messageType;
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" + "messageType=" + messageType + ", message=" + message + '}';
    }

}
