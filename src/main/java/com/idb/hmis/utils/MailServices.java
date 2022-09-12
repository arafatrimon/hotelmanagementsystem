///*
// 
// 
// 
// */
//package com.idb.hmis.utils;
//
//import java.io.IOException;
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Component;
//
///**
// *
// * @author Yasir Araphat
// */
//@Component
//public class MailServices {
//
//    @Autowired
//    public JavaMailSender javaMailSender;
//
//    public void sendTextMessage(String[] msgTo, String msgSubject, String msgBody) {
//
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(msgTo);
//        msg.setSubject(msgSubject);
//        msg.setText(msgBody);
//
//        javaMailSender.send(msg);
//
//    }
//
//    public void sendEmailWithAttachment(String msgTo, String msgSubject, String msgBody, String fileName, String attatchmentPath) throws MessagingException, IOException {
//
//        MimeMessage msg = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
//        helper.setTo(msgTo);
//        helper.setSubject(msgSubject);
//        helper.setText(msgBody, true);
//        if (fileName != null && attatchmentPath != null && !attatchmentPath.isEmpty()) {
//            helper.addAttachment(fileName, new ClassPathResource(attatchmentPath));
//        }
//        javaMailSender.send(msg);
//
//    }
//
//}
