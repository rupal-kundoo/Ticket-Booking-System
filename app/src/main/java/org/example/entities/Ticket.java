package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
/*  If the JSON contains fields that are not in this class, ignore them when deserializing(reading).
example ticket info is not a filed in your class, now jackson will not stop from writing extra files to json file, it will still serialize.


Serialization:
writeValue
List<User>  ---------->into JSON
{userid":"",
"username":""}

Deserialization()
readValue
reads the files and rebuilds the list of user objects in memoruy.


use : @JsonIgnoreProperties(ignoreUnknown = true)   :Deserialization(json has the field but class does not , it ignores)
use:  @JsonIgnore                                   :Serialization/Deserialization

*/

public class Ticket {

    private String ticketId;
    private String userId;
    private  String source;
    private String destination;
    private String ticketTime;
    public Train trains;

    public Ticket(String ticketId,String userId,String source,String destination,String ticketTime,Train trains) {
        this.ticketId = ticketId;
        this.userId=userId;
        this.source=source;
        this.destination=destination;
        this.ticketTime=ticketTime;
        this.trains=trains;
    }

     public Ticket(){
       //  getticketinfo();
     }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    public String getTicketTime() {
        return ticketTime;
    }

    public void setTicketTime(String ticketTime) {
        this.ticketTime = ticketTime;
    }


    public Train getTrains() {
        return trains;
    }

    public void setTrains(Train trains) {
        this.trains = trains;
    }

    public String getticketinfo(){
        return String.format("Ticket ID: %s belongs to User %s from %s to %s on %s", ticketId, userId, source, destination, ticketTime);
    }

}
