package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpEmail(String toEmail, String subject, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            // Extract OTP from message
            String otp = message.replace("Your OTP is: ", "").replace(". Valid for 5 minutes.", "");
            String htmlContent = buildOtpEmailTemplate(otp);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            logger.info("✅ OTP email sent successfully to: {}", toEmail);
        } catch (MessagingException e) {
            logger.error("❌ Failed to send OTP email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    @Override
    public void sendOrderConfirmationEmail(String toEmail, String customerName, String orderId,
                                           BigDecimal totalAmount, List<String> productNames, String mobile) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Order Placed Successfully - The Home Bakery");

            String htmlContent = buildOrderConfirmationEmailTemplate(customerName, orderId, totalAmount, productNames, mobile);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("✅ Order confirmation email sent successfully to: {} for order: {}", toEmail, orderId);
        } catch (MessagingException e) {
            logger.error("❌ Failed to send order confirmation email to {} for order {}: {}", toEmail, orderId, e.getMessage());
            throw new RuntimeException("Failed to send order confirmation email: " + e.getMessage());
        }
    }

    @Override
    public void sendOrderCancellationEmail(String toEmail, String customerName, String orderId,
                                           BigDecimal totalAmount, String mobile) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Order Cancelled - The Home Bakery");

            String htmlContent = buildOrderCancellationEmailTemplate(customerName, orderId, totalAmount, mobile);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("✅ Order cancellation email sent successfully to: {} for order: {}", toEmail, orderId);
        } catch (MessagingException e) {
            logger.error("❌ Failed to send order cancellation email to {} for order {}: {}", toEmail, orderId, e.getMessage());
            throw new RuntimeException("Failed to send order cancellation email: " + e.getMessage());
        }
    }

    private String buildOtpEmailTemplate(String otp) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>OTP Verification - The Home Bakery</title>" +
                "<style>" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }" +
                ".header { text-align: center; padding: 40px 30px 30px 30px; background-color: white; }" +
                ".company-name { font-size: 28px; font-weight: bold; color: #8B2635; margin-bottom: 20px; }" +
                ".title { font-size: 28px; font-weight: 600; color: #8B2635; margin: 15px 0 10px 0; }" +
                ".subtitle { font-size: 16px; color: #A53860; margin: 5px 0 20px 0; opacity: 0.9; }" +
                ".content { padding: 0 30px; }" +
                ".greeting { color: #666; font-size: 16px; margin: 20px 0; line-height: 1.6; }" +
                ".otp-section { background-color: #fef9f9; border-left: 4px solid #8B2635; padding: 25px; margin: 25px 0; border-radius: 8px; box-shadow: 0 2px 8px rgba(139,38,53,0.1); }" +
                ".otp-title { font-size: 20px; font-weight: 600; color: #8B2635; margin-bottom: 20px; text-decoration: underline; }" +
                ".otp-box { background: linear-gradient(135deg, #8B2635 0%, #A53860 100%); color: white; padding: 20px; border-radius: 8px; text-align: center; margin: 20px 0; }" +
                ".otp-code { font-size: 24px; font-weight: 700; letter-spacing: 2px; }" +
                ".info { color: #666; font-size: 15px; line-height: 1.5; margin: 15px 0; }" +
                ".info ul { padding-left: 20px; }" +
                ".info li { margin-bottom: 10px; }" +
                ".footer { text-align: center; background-color: #f8f9fa; padding: 25px 30px; color: #6c757d; font-size: 12px; line-height: 1.4; }" +
                ".footer p { margin: 5px 0; }" +
                "@media (max-width: 600px) {" +
                ".container { margin: 10px; }" +
                ".header, .content { padding: 20px; }" +
                ".title { font-size: 24px; }" +
                ".otp-box { padding: 15px; }" +
                ".otp-code { font-size: 20px; }" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<div class='company-name'>The Home Bakery</div>" +
                "<div class='title'>OTP Verification</div>" +
                "<div class='subtitle'>Secure your account with this one-time password</div>" +
                "</div>" +
                "<div class='content'>" +
                "<div class='greeting'>" +
                "Hello," +
                "<br>" +
                "You have requested an OTP for verification. Please use the following code to proceed." +
                "</div>" +
                "<div class='otp-section'>" +
                "<div class='otp-title'>Your OTP Code:</div>" +
                "<div class='otp-box'>" +
                "<div class='otp-code'>" + otp + "</div>" +
                "</div>" +
                "<div class='info'>" +
                "<p><strong>Important:</strong></p>" +
                "<ul>" +
                "<li>This OTP is valid for <strong>5 minutes</strong> only</li>" +
                "<li>Please do not share this code with anyone</li>" +
                "<li>If you didn't request this OTP, please ignore this email</li>" +
                "</ul>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2025 The Home Bakery. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String buildOrderConfirmationEmailTemplate(String customerName, String orderId,
                                                       BigDecimal totalAmount, List<String> productNames, String mobile) {

        StringBuilder productDetailsList = new StringBuilder();
        for (int i = 0; i < productNames.size(); i++) {
            productDetailsList.append("<div class='product-item'>")
                    .append("<div class='product-row'>")
                    .append("<span class='product-label'>Product:</span>")
                    .append("<span class='product-value'>").append(productNames.get(i)).append("</span>")
                    .append("</div>")
                    .append("<div class='product-row'>")
                    .append("<span class='product-label'>Quantity:</span>")
                    .append("<span class='product-value'>1</span>")
                    .append("</div>")
                    .append("</div>");
        }

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Order Confirmation - The Home Bakery</title>" +
                "<style>" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }" +
                ".header { text-align: center; padding: 40px 30px 30px 30px; background-color: white; }" +
                ".logo { max-width: 120px; height: auto; margin-bottom: 25px; }" +
                ".company-name { font-size: 32px; font-weight: bold; color: #8B2635; margin-bottom: 15px; letter-spacing: 1px; }" +
                ".title { font-size: 28px; font-weight: 600; color: #8B2635; margin: 15px 0 10px 0; }" +
                ".subtitle { font-size: 16px; color: #A53860; margin: 5px 0 20px 0; opacity: 0.9; font-weight: 500; }" +
                ".content { padding: 0 30px; }" +
                ".greeting { color: #666; font-size: 16px; margin: 20px 0; line-height: 1.6; }" +
                ".customer-greeting { color: #666; font-size: 16px; margin: 15px 0; }" +
                ".customer-name { color: #8B2635; font-weight: 600; }" +
                ".sparkle { color: #A53860; }" +
                ".order-section { background-color: #fff; border-left: 4px solid #8B2635; padding: 25px; margin: 25px 0; border-radius: 8px; box-shadow: 0 2px 8px rgba(139,38,53,0.1); }" +
                ".order-title { font-size: 20px; font-weight: 600; color: #8B2635; margin-bottom: 20px; text-decoration: underline; }" +
                ".product-list { margin: 15px 0; }" +
                ".product-item { background-color: #fef9f9; padding: 15px; margin-bottom: 15px; border-radius: 8px; border-left: 3px solid #8B2635; }" +
                ".product-row { display: flex; justify-content: flex-start; align-items: center; margin-bottom: 8px; }" +
                ".product-row:last-child { margin-bottom: 0; }" +
                ".product-label { font-weight: 600; color: #8B2635; min-width: 120px; font-size: 15px; }" +
                ".product-value { color: #333; font-size: 15px; margin-left: 10px; }" +
                ".total-section { background: linear-gradient(135deg, #8B2635 0%, #A53860 100%); color: white; padding: 20px; border-radius: 8px; margin: 20px 0; text-align: center; }" +
                ".total-amount { font-size: 22px; font-weight: 700; }" +
                ".order-link-section { text-align: center; margin: 25px 0; padding: 20px; background-color: #fef9f9; border-radius: 10px; border: 1px solid #8B2635; }" +
                ".order-link-text { font-size: 16px; color: #8B2635; margin-bottom: 15px; font-weight: 500; }" +
                ".order-link-btn { display: inline-block; background: linear-gradient(135deg, #8B2635 0%, #A53860 100%); color: white; padding: 14px 28px; text-decoration: none; border-radius: 25px; font-weight: 600; font-size: 15px; transition: all 0.3s ease; box-shadow: 0 3px 10px rgba(139, 38, 53, 0.3); }" +
                ".order-link-btn:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(139, 38, 53, 0.4); }" +
                ".contact-section { background: linear-gradient(135deg, #fef9f9 0%, #fcf3f3 100%); padding: 25px; border-radius: 10px; margin: 25px 0; text-align: center; border: 1px solid #8B2635; }" +
                ".contact-text { font-size: 16px; color: #8B2635; margin-bottom: 18px; font-weight: 500; }" +
                ".mobile-number { font-weight: 700; color: #8B2635; }" +
                ".contact-link { display: inline-block; background: linear-gradient(135deg, #8B2635 0%, #A53860 100%); color: white; padding: 14px 28px; text-decoration: none; border-radius: 25px; font-weight: 600; font-size: 15px; transition: all 0.3s ease; box-shadow: 0 3px 10px rgba(139, 38, 53, 0.3); }" +
                ".contact-link:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(139, 38, 53, 0.4); }" +
                ".divider { text-align: center; margin: 30px 0; color: #8B2635; font-size: 24px; }" +
                ".closing { text-align: center; margin: 30px 0; padding: 0 30px; }" +
                ".happy-shopping { font-size: 20px; font-weight: 600; margin: 20px 0; color: #8B2635; }" +
                ".shopping-bag { color: #A53860; }" +
                ".party-emoji { color: #A53860; }" +
                ".contact-info { font-size: 15px; color: #666; margin: 15px 0; line-height: 1.5; }" +
                ".footer { text-align: center; background-color: #f8f9fa; padding: 25px 30px; color: #6c757d; font-size: 12px; line-height: 1.4; }" +
                ".footer p { margin: 5px 0; }" +
                "@media (max-width: 600px) {" +
                ".container { margin: 10px; }" +
                ".header, .content, .closing { padding: 20px; }" +
                ".title { font-size: 24px; }" +
                ".product-row { flex-direction: column; align-items: flex-start; }" +
                ".product-label { min-width: auto; margin-bottom: 5px; }" +
                ".product-value { margin-left: 0; }" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<div class='company-name'>The Home Bakery</div>" +
                "<div class='title'>Order Placed Successfully! &#10004;</div>" +
                "<div class='subtitle'>Thank you for your order from The Home Bakery! &#127850;</div>" +
                "</div>" +

                "<div class='content'>" +
                "<div class='greeting'>" +
                "We're delighted to serve you fresh, homemade bakery items! Your satisfaction is our priority. &#10024;" +
                "</div>" +

                "<div class='customer-greeting'>" +
                "Hi <span class='customer-name'>" + customerName + "</span>," +
                "</div>" +

                "<div class='order-section'>" +
                "<div class='order-title'>Order Details:</div>" +
                "<div class='product-list'>" +
                productDetailsList.toString() +
                "</div>" +
                "<div class='total-section'>" +
                "<div class='total-amount'>Total Amount: &#8377;" + totalAmount.toPlainString() + "</div>" +
                "</div>" +
                "</div>" +

                "<div class='order-link-section'>" +
                "<div class='order-link-text'>Track your order status:</div>" +
                "<a href='https://thehomebakerypune.com/MY%20ORDERS/myorders.html' class='order-link-btn' target='_blank'>Check Your Order</a>" +
                "</div>" +

                "<div class='contact-section'>" +
                "<div class='contact-text'>For any queries about your order, please contact us at: <span class='mobile-number'>" + mobile + "</span></div>" +
                "<a href='tel:" + mobile.replace("+", "").replace(" ", "") + "' class='contact-link'>Call Us Now</a>" +
                "</div>" +
                "</div>" +

                "<div class='divider'>•••</div>" +

                "<div class='closing'>" +
                "<div class='happy-shopping'><span class='shopping-bag'>&#128722;</span> Enjoy your baked goods! <span class='party-emoji'>&#127881;&#127881;</span></div>" +
                "<div class='contact-info'>If you have any questions, feel free to reach out anytime.</div>" +
                "</div>" +

                "<div class='footer'>" +
                "<p>&copy; 2025 The Home Bakery. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String buildOrderCancellationEmailTemplate(String customerName, String orderId,
                                                       BigDecimal totalAmount, String mobile) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Order Cancellation - The Home Bakery</title>" +
                "<style>" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }" +
                ".header { text-align: center; padding: 40px 30px 30px 30px; background-color: white; }" +
                ".company-name { font-size: 32px; font-weight: bold; color: #8B2635; margin-bottom: 15px; letter-spacing: 1px; }" +
                ".title { font-size: 28px; font-weight: 600; color: #8B2635; margin: 15px 0 10px 0; }" +
                ".subtitle { font-size: 16px; color: #A53860; margin: 5px 0 20px 0; opacity: 0.9; font-weight: 500; }" +
                ".content { padding: 0 30px; }" +
                ".greeting { color: #666; font-size: 16px; margin: 20px 0; line-height: 1.6; }" +
                ".customer-greeting { color: #666; font-size: 16px; margin: 15px 0; }" +
                ".customer-name { color: #8B2635; font-weight: 600; }" +
                ".order-section { background-color: #fff; border-left: 4px solid #8B2635; padding: 25px; margin: 25px 0; border-radius: 8px; box-shadow: 0 2px 8px rgba(139,38,53,0.1); }" +
                ".order-title { font-size: 20px; font-weight: 600; color: #8B2635; margin-bottom: 20px; text-decoration: underline; }" +
                ".order-details { background-color: #fef9f9; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 3px solid #8B2635; }" +
                ".detail-row { display: flex; margin-bottom: 10px; }" +
                ".detail-label { font-weight: 600; color: #8B2635; min-width: 150px; font-size: 15px; }" +
                ".detail-value { color: #333; font-size: 15px; margin-left: 10px; }" +
                ".cancellation-section { background: linear-gradient(135deg, #8B2635 0%, #A53860 100%); color: white; padding: 25px; border-radius: 10px; margin: 25px 0; text-align: center; }" +
                ".cancellation-text { font-size: 20px; font-weight: 700; margin-bottom: 15px; }" +
                ".cancellation-icon { font-size: 36px; margin-bottom: 15px; }" +
                ".order-link-section { text-align: center; margin: 25px 0; padding: 20px; background-color: #fef9f9; border-radius: 10px; border: 1px solid #8B2635; }" +
                ".order-link-text { font-size: 16px; color: #8B2635; margin-bottom: 15px; font-weight: 500; }" +
                ".order-link-btn { display: inline-block; background: linear-gradient(135deg, #8B2635 0%, #A53860 100%); color: white; padding: 14px 28px; text-decoration: none; border-radius: 25px; font-weight: 600; font-size: 15px; transition: all 0.3s ease; box-shadow: 0 3px 10px rgba(139, 38, 53, 0.3); }" +
                ".order-link-btn:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(139, 38, 53, 0.4); }" +
                ".contact-section { background: linear-gradient(135deg, #fef9f9 0%, #fcf3f3 100%); padding: 25px; border-radius: 10px; margin: 25px 0; text-align: center; border: 1px solid #8B2635; }" +
                ".contact-text { font-size: 16px; color: #8B2635; margin-bottom: 18px; font-weight: 500; }" +
                ".mobile-number { font-weight: 700; color: #8B2635; }" +
                ".contact-link { display: inline-block; background: linear-gradient(135deg, #8B2635 0%, #A53860 100%); color: white; padding: 14px 28px; text-decoration: none; border-radius: 25px; font-weight: 600; font-size: 15px; transition: all 0.3s ease; box-shadow: 0 3px 10px rgba(139, 38, 53, 0.3); }" +
                ".contact-link:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(139, 38, 53, 0.4); }" +
                ".divider { text-align: center; margin: 30px 0; color: #8B2635; font-size: 24px; }" +
                ".closing { text-align: center; margin: 30px 0; padding: 0 30px; }" +
                ".sorry-message { font-size: 20px; font-weight: 600; margin: 20px 0; color: #8B2635; }" +
                ".sad-emoji { color: #A53860; font-size: 24px; }" +
                ".contact-info { font-size: 15px; color: #666; margin: 15px 0; line-height: 1.5; }" +
                ".footer { text-align: center; background-color: #f8f9fa; padding: 25px 30px; color: #6c757d; font-size: 12px; line-height: 1.4; }" +
                ".footer p { margin: 5px 0; }" +
                "@media (max-width: 600px) {" +
                ".container { margin: 10px; }" +
                ".header, .content, .closing { padding: 20px; }" +
                ".title { font-size: 24px; }" +
                ".detail-row { flex-direction: column; align-items: flex-start; }" +
                ".detail-label { min-width: auto; margin-bottom: 5px; }" +
                ".detail-value { margin-left: 0; }" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<div class='company-name'>The Home Bakery</div>" +
                "<div class='title'>Order Cancelled &#10060;</div>" +
                "<div class='subtitle'>We're sorry to see you go!</div>" +
                "</div>" +

                "<div class='content'>" +
                "<div class='greeting'>" +
                "We hope to serve you again with our fresh, homemade bakery items! &#127850;" +
                "</div>" +

                "<div class='customer-greeting'>" +
                "Hi <span class='customer-name'>" + customerName + "</span>," +
                "</div>" +

                "<div class='order-section'>" +
                "<div class='order-title'>Cancelled Order Details:</div>" +
                "<div class='order-details'>" +
                "<div class='detail-row'><span class='detail-label'>Order ID:</span> <span class='detail-value'>#" + orderId + "</span></div>" +
                "<div class='detail-row'><span class='detail-label'>Order Status:</span> <span class='detail-value'>Cancelled</span></div>" +
                "<div class='detail-row'><span class='detail-label'>Order Amount:</span> <span class='detail-value'>&#8377;" + totalAmount.toPlainString() + "</span></div>" +
                "</div>" +
                "</div>" +

                "<div class='cancellation-section'>" +
                "<div class='cancellation-icon'>&#10060;</div>" +
                "<div class='cancellation-text'>Your order has been cancelled</div>" +
                "</div>" +

                "<div class='order-link-section'>" +
                "<div class='order-link-text'>View your order history:</div>" +
                "<a href='https://thehomebakerypune.com/MY%20ORDERS/myorders.html' class='order-link-btn' target='_blank'>Check Your Orders</a>" +
                "</div>" +

                "<div class='contact-section'>" +
                "<div class='contact-text'>If this cancellation was a mistake or if you have any questions, please contact us at: <span class='mobile-number'>" + mobile + "</span></div>" +
                "<a href='tel:" + mobile.replace("+", "").replace(" ", "") + "' class='contact-link'>Call Us Now</a>" +
                "</div>" +
                "</div>" +

                "<div class='divider'>•••</div>" +

                "<div class='closing'>" +
                "<div class='sorry-message'><span class='sad-emoji'>😔</span> We hope to serve you again soon! <span class='sad-emoji'>🍞</span></div>" +
                "<div class='contact-info'>If you have any questions, feel free to reach out anytime.</div>" +
                "</div>" +

                "<div class='footer'>" +
                "<p>&copy; 2025 The Home Bakery. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body></html>";
    }
}