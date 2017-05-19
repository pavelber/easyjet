package org.easyjet.services

import groovy.json.JsonSlurper
import org.easyjet.entity.Event
import org.easyjet.entity.IEventRepository
import org.easyjet.entity.IUserRepository
import org.easyjet.entity.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by Pavel on 9/29/2015.
 */
@Service
class WowCheckAndSend implements Runnable {

    static Logger logger = LoggerFactory.getLogger(WowCheckAndSend.class)

    final String DESTINATION="ORD"

    @Autowired
    IEventRepository eventRepository
    @Autowired
    IUserRepository userRepository
    @Autowired
    FlightsMailSender mailSender

    Date date


    String letter = '''
        Дешевые билеты ждут вас на <a href="http://https://wowair.co.il/">wowair.co.il</a>
        <br>
        <b>Помните</b>, что не все серверы апдейтятся одновременно.
        Если Вы зашли на сайт wowair и не увидели новых полетов, попробуйте через 5-10 минут - они уже тут!
        ''' + LetterConsts.letterSuffinx
    private String COMPANY

    @Override
    void run() {
        COMPANY = "WOW"
        def desc = eventRepository.findByCompanyAndDestinationOrderByIdDesc(COMPANY, DESTINATION)
        String stored = desc.size()==0?"":desc.head().stored
        def data = new URL('https://booking.wowair.co.uk/SearchBoxUserControl/GetAllDatesWithNoFlights?originAirportCode=TLV&destinationAirportCode='+DESTINATION).getText(useCaches:false)
        def jsonSlurper = new JsonSlurper()
        String str = ((Map)jsonSlurper.parseText(data)).get("departurePeriodEnd")

        if (str != stored) {
            logger.info("Found diff")
            def users = [new User(email:"javaap@gmail.com")]//userRepository.findAll()
            mailSender.send(users,"New flights by WOW from TLV to Chicago",letter)
            eventRepository.save(new Event(stored: str, company: COMPANY, destination: DESTINATION))
        }
        date = new Date()
    }
}
