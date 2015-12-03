package org.easyjet.services

import groovy.json.JsonSlurper
import org.easyjet.entity.Event
import org.easyjet.entity.IEventRepository
import org.easyjet.entity.IUserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import javax.mail.internet.MimeMessage

/**
 * Created by Pavel on 9/29/2015.
 */
@Service
class CheckAndSend implements Runnable {

    static Logger logger = LoggerFactory.getLogger(CheckAndSend.class)

    @Autowired
    IEventRepository eventRepository
    @Autowired
    IUserRepository userRepository
    @Autowired
    MailSender sender

    Date date


    String letter = '''
        Дешевые билеты ждут вас на <a href="http://easyjet.com">easyjet.com</a>
        <br>
        <b>Помните</b>, что не все серверы апдейтятся одновременно.
        Если Вы зашли на сайт easyjet и не увидели новых полетов, попробуйте через 5-10 минут - они уже тут!
        Мы почти никогда не ошибаемся!
        <br>
        А если Вы закажете дешевые билеты пожертвуйте 5-10 долларов на продолжение работы сервиса:


    <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
        <input type="hidden" name="cmd" value="_s-xclick">
        <input type="hidden" name="hosted_button_id" value="PDJ3MD7N62AXE">
        <input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif" border="0" name="submit"
               alt="PayPal - The safer, easier way to pay online!">
        <img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">
    </form>
    <p>
    <i>Ваша добрая фея</i>
    </p>

        '''
    private def users

    @PostConstruct
    def init() {
        users = userRepository.findAll()
    }

    @Override
    public void run() {
        String stored = eventRepository.findFirstByOrderByIdDesc().stored
        def data = new URL('http://www.easyjet.com/EN/linkedAirportsJSON').getText(useCaches:false)
        def jsonSlurper = new JsonSlurper()
        List<String> list = jsonSlurper.parseText(data.substring(12, data.length() - 2).replace('\'', '"'))
        String str = list.findAll { it.startsWith("TLV") }

        if (str != stored) {
            logger.info("Found diff")
            users.each {
                try {
                    logger.info("Sending to {}", it.email)
                    MimeMessage message = sender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
                    helper.setText(letter, true);
                    helper.setFrom("easyjet.informer@gmail.com");
                    helper.setTo(it.email);
                    helper.setSubject("New flights by EasyJet from TLV");

                    sender.send(message)
                    Thread.sleep(10000L)
                } catch (Exception e) {
                    logger.error("Can't send email to " + it.email, e)
                }
            }
            eventRepository.save(new Event(stored: str))
        }
        date = new Date()
    }
}
