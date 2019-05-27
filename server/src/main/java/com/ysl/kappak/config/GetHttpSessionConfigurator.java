package com.ysl.kappak.config;

import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/20 22:21
 * @modifyTime :
 * @description : 解决获取不到httpSession问题
 */
@Component
public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession)request.getHttpSession();
        Map<String, List<String>> headers = request.getHeaders();
        String name = headers.get("clientName") == null ? "" : headers.get("clientName").get(0);
        sec.getUserProperties().put("clientName", name);
        //解决httpSession为null的情况
        if (httpSession == null){
            httpSession = new HttpSession() {
                @Override
                public long getCreationTime() {
                    return 0;
                }

                @Override
                public String getId() {
                    return null;
                }

                @Override
                public long getLastAccessedTime() {
                    return 0;
                }

                @Override
                public ServletContext getServletContext() {
                    return null;
                }

                @Override
                public void setMaxInactiveInterval(int i) {

                }

                @Override
                public int getMaxInactiveInterval() {
                    return 0;
                }

                @Override
                public HttpSessionContext getSessionContext() {
                    return null;
                }

                @Override
                public Object getAttribute(String s) {
                    return null;
                }

                @Override
                public Object getValue(String s) {
                    return null;
                }

                @Override
                public Enumeration<String> getAttributeNames() {
                    return null;
                }

                @Override
                public String[] getValueNames() {
                    return new String[0];
                }

                @Override
                public void setAttribute(String s, Object o) {

                }

                @Override
                public void putValue(String s, Object o) {

                }

                @Override
                public void removeAttribute(String s) {

                }

                @Override
                public void removeValue(String s) {

                }

                @Override
                public void invalidate() {

                }

                @Override
                public boolean isNew() {
                    return false;
                }
            };
        }
        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}
