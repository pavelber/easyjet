package org.easyjet.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * Created by Pavel on 10/8/2015.
 */
@Entity
@Table(name="events")
class   Event {
    @Id
    int id
    String company
    String destination
    String stored
}
