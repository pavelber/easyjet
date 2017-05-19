package org.easyjet.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.mail.MailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

import javax.mail.internet.MimeMessage

/**
 * Created by pavelb on 19/05/2017.
 */
@Service
class FlightsMailSender {
    static Logger logger = LoggerFactory.getLogger(FlightsMailSender.class)

    @Autowired
    MailSender sender

    void send(users, subject, text ) {
        users.each {
            try {
                logger.info("Sending to {}", it)
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
                helper.setText(text, true);
                helper.setFrom("easyjet.informer@gmail.com")
                helper.setTo(it.email)
                helper.setSubject(subject)

                sender.send(message)
                Thread.sleep(10000L)
            } catch (Exception e) {
                logger.error("Can't send email to " + it.email, e)
            }
        }
    }
}
