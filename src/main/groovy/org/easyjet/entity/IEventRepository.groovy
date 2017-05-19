package org.easyjet.entity

import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by Pavel on 9/29/2015.
 */
interface IEventRepository extends JpaRepository<Event,String> {
    List<Event> findByCompanyAndDestinationOrderByIdDesc(String company, String destination)
}
