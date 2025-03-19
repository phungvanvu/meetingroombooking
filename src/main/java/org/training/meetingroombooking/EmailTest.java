//package org.training.meetingroombooking;
//
//import jakarta.mail.MessagingException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.training.meetingroombooking.service.EmailService;
//
//@SpringBootApplication
//public class EmailTest implements CommandLineRunner {
//
//    @Autowired
//    private EmailService emailService;
//
//    public static void main(String[] args) {
//        SpringApplication.run(EmailTest.class, args);
//    }
//
//    @Override
//    public void run(String... args) {
//        try {
//            String to = "phungvanvu04@gmail.com";
//            String subject = "Test Email";
//            String htmlBody = "<h1 style='color: blue;'>Xin chào!</h1>"
//                    + "<p>This is a ***REMOVED*** email from <b>Spring Boot</b>.</p>";
//
//            emailService.sendHtmlEmail(to, subject, htmlBody);
//            System.out.println("Email sent successfully!");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            System.out.println("Sending email failed!");
//        }
//    }
//}
