package org.easyjet.services

import groovy.json.JsonSlurper
import org.easyjet.entity.Event
import org.easyjet.entity.IEventRepository
import org.easyjet.entity.IUserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by Pavel on 9/29/2015.
 */
@Service
class EasyJetCheckAndSend implements Runnable {

    static Logger logger = LoggerFactory.getLogger(EasyJetCheckAndSend.class)

    @Autowired
    IEventRepository eventRepository
    @Autowired
    IUserRepository userRepository
    @Autowired
    FlightsMailSender mailSender


    String letter = '''
        Дешевые билеты ждут вас на <a href="http://easyjet.com">easyjet.com</a>
        <br>
        <b>Помните</b>, что не все серверы апдейтятся одновременно.
        Если Вы зашли на сайт easyjet и не увидели новых полетов, попробуйте через 5-10 минут - они уже тут!
        ''' + LetterConsts.letterSuffinx
    private String COMPANY

    @Override
    void run() {
        COMPANY = "easyjet"
        def DESTINATION = "all"
        String stored = eventRepository.findByCompanyAndDestinationOrderByIdDesc(COMPANY, DESTINATION).head().stored
        def data = new URL('http://www.easyjet.com/EN/linkedAirportsJSON').getText(useCaches:false)
        def jsonSlurper = new JsonSlurper()
        List<String> list = jsonSlurper.parseText(data.substring(12, data.length() - 2).replace('\'', '"'))
        String str = list.findAll { it.startsWith("TLV") }

        if (str != stored) {
            logger.info("Found diff")
            def users = userRepository.findAll()
            mailSender.send(users,"New flights by EasyJet from TLV",letter)
            eventRepository.save(new Event(stored: str, company: COMPANY, destination: DESTINATION))
        }
    }
}
