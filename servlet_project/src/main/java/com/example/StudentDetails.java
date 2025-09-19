package com.example;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.gson.Gson;

import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public class StudentDetails extends HttpServlet   {
    private static final Logger logger = Logger.getLogger(StudentDetails.class.getName());
    private static String url;
    private static String user;
    private static String password;
    static List<Map<String, Object>> rows = new ArrayList<>();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
   

        try(Connection conn = DriverManager.getConnection("jdbc:h2:file:D:/Koushik/Java/Servlet_Task/servlet_project/data/testdb;DB_CLOSE_ON_EXIT=FALSE;", "sa", "")){
                String selectSql = "SELECT * FROM BOOK";
                try (PreparedStatement ps = conn.prepareStatement(selectSql);
                    ResultSet rs = ps.executeQuery()) {
                    ResultSetMetaData meta = rs.getMetaData();
                    int columnCount = meta.getColumnCount();
                    
                    while (rs.next()) {
                        Map<String, Object> row = new LinkedHashMap<>(); 
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = meta.getColumnName(i);
                            Object value = rs.getObject(i);
                            row.put(columnName, value);
                        }
                    
                        rows.add(row);
                    }
                     logger.info(rows+"meta output");
                }
    }catch (Exception e) {
       logger.info("dont able to connect DB output");
    } 
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(Map.of(
            "message", "Welcome to my Student Details portal",
            "details", rows
        ));

        PrintWriter out = resp.getWriter();
        out.print(jsonResponse);
        out.flush();
    }

}
