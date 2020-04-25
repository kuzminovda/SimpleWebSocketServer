/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.hiik.websocketserver;

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
//  Класс доступен на протяжении всего жизненного цикла приложения
//   |
// 
@ApplicationScoped
//
//    Сервер, реализующий протокол Websocket
//      |
@ServerEndpoint("/websocket")
public class WebSocketServer
{

    private static final Logger LOG = Logger.getLogger(WebSocketServer.class.getSimpleName());
    
    //   Структура для Map (карта) хранения сведений о подключенных пользователях (сесии пользователей)
    //   
    //   Пример карты (связанные пары):
    //        ключ                          значение (экземпляр класса Session)
    //         |                                            |
    //      EF91381F-B90C-4593-8886-C0F03C91AE24 - сессия пользователя 1 
    //      8632E55F-4BB8-468C-89AF-4886E597016B - сессия пользователя 2 
    //      6CBAB35C-A89E-4952-AB0A-5256ED0279E6 - сессия пользователя 3 
    //
    //                              ключ  значение   имя структуры   может использоваться в режиме работы нескольких нитей
    //                               |      |        |               /
    private static ConcurrentMap <String, Session> users = new ConcurrentHashMap<String, Session>();
    
    
    
    // Событие OnOpen возникает в случае подключения к серверу клиента
    // и создания новой  сессии с уникальным идентификатором
    @OnOpen
    //                          экземпляр сесси с клиентом Websocket
    //                            |
    public void onOpen(Session session)
    {
        // Печать идентификатора сесиии
        LOG.log(Level.INFO, "Подключено к серверу: " + session.getId().toUpperCase());
        //                     ключ        преобразовать в заглавные   экземпляр класса сессии
        //                      |           /                          /
        users.putIfAbsent(session.getId().toUpperCase(),             session);
    }

    
    
    @OnClose
    public void onClose(Session session)
    {

        users.remove(session.getId().toUpperCase());

    }

    @OnError
    public void onError(Throwable t)
    {

    }

    
    
    /*
     OnMessage - обработка события получения сервером сообщения от клиента
    */
    @OnMessage
    //                         сообщение       экземпляр класса сессии с клиентом
    //                             |           /
    public void onMessage(String message, Session session)
    {
        LOG.log(Level.INFO,"Получено сообщение от клиента: " + message);
    }
    
}
