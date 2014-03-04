package com.cloud.tomcat.servlet;

public class CloudServlet implements HttpServlet {

    @Override
    public String doGet() {
        return this.doPost();
    }

    @Override
    public String doPost() {
        return "<h1>Chicago at Cloud!!!</h1>";
    }

}