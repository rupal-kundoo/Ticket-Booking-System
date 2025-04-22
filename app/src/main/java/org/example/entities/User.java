package org.example.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonNaming (PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Cloneable {

    private String userId;
    private String userName;
    private String password;
    private String hashPassword;
    @JsonProperty("ticketbooked")
    private List<Ticket> ticketBooked;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public User(String userId,String userName,String password,String hashPassword,List<Ticket> ticketBooked){
        this.userId = userId;
        this.userName=userName;
        this.password=password;
        this.hashPassword=hashPassword;
        this.ticketBooked=ticketBooked;
        String str1 = String.format("The value via constructior: username is %s and userid is %s",this.userName,this.userId);
        System.out.println(str1);
    }

    public User()
    {

    }



    public String getUserId() {
        System.out.println("the value of userd is "+userId);
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        System.out.println("the value of username is "+userName);
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public List<Ticket> getTicketBooked() {
        return ticketBooked;
    }

    public void setTicketBooked(List<Ticket> ticketBooked) {
        this.ticketBooked = ticketBooked;
    }

    public void printickets()
    {
        for(int i=0;i<ticketBooked.size();i++)
        System.out.println("the ticket details are"+ticketBooked.get(i).getticketinfo());
    }
}
