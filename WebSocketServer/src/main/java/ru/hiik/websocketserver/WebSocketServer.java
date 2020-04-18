/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.hiik.websocketserver;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author vaganovdv
 */
@ApplicationScoped
@ServerEndpoint("/websocket")

public class WebSocketServer
{

    private static final Logger LOG = Logger.getLogger(WebSocketServer.class.getSimpleName());
    private static ConcurrentMap<String, Session> users = new ConcurrentHashMap<String, Session>();
    
    
    
    @OnOpen
    public void onOpen(Session session) 
    {
       
       LOG.log(Level.INFO, "Подключено к серверу: "+session.getId());
        users.putIfAbsent(session.getId().toUpperCase(), session);
    }

    @OnClose
    public void onClose(Session session) {
        
        users.remove(session.getId().toUpperCase());
       
        
    }

    @OnError
    public void onError(Throwable t) 
    {

    }

    @OnMessage
    public void onMessage(String message, Session session) 
    {
        try
        {
       
            session.getBasicRemote().sendText(message);
       
        } catch (IOException ex)
        {
       
        }
    }
    
}
