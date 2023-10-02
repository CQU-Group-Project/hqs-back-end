//package com.cqu.hqs.config;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//@Component
//@Configuration
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class EmailConfig {
//
//    @Value("${email.server}")
//    private String smtpServer;
//
//    @Value("${email.port}")
//    private int smtpPort;
//
//    @Value("${email.username}")
//    private String smtpUsername;
//
//    @Value("${email.password}")
//    private String smtpPassword;
//    
//    private EmailNotification emailNotification;
//
//    public EmailConfig() {
//       
//       emailNotification.smtpServer=smtpServer;
//    }
//}
