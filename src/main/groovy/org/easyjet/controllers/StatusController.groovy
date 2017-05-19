package org.easyjet.controllers

import org.easyjet.entity.IUserRepository
import org.easyjet.services.EasyJetCheckAndSend
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import java.text.SimpleDateFormat

/**
 * Created by Pavel on 10/10/2015.
 */
@Controller
class StatusController {

    @Autowired
    IUserRepository repo

    @Autowired
    EasyJetCheckAndSend checkAndSend

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")


    @RequestMapping("/status")
    @ResponseBody String get() {
        def all = repo.findAll()
        Date date = checkAndSend.date

        return format.format(date)+" for ${all.size()} emails"
    }
}
