package org.example.services;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.Ticket;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.util.userUtilService;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
//import java.time.LocalDateTime;

public class UserBookingService {

    User user;
     private List<User> userList;
     public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
     /*public static final String USERS_PATH =  "users.json";//"../src/main/java/org/example/localDb/users.json";
     File f = new File(USERS_PATH);*/

    public  static final String USER_FILE_NAME="users.json";
    public  static final String WRITABLE_USER_PATH = System.getProperty("user.home")+File.separator+ USER_FILE_NAME;



    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUserListFromFile();
        String str = String.format("this is login of user %s", this.user.getUserName());
        System.out.println(str);


    }

    public UserBookingService() throws IOException {
        ensureUserFileExists();
        loadUserListFromFile();
    }

     public void ensureUserFileExists()
        {
           File userFile = new File(WRITABLE_USER_PATH);
           if(userFile.exists())
           {
               return;
           }
           try(InputStream in = getClass().getClassLoader().getResourceAsStream(USER_FILE_NAME);
               OutputStream out = new FileOutputStream(userFile)) {

               if (in == null) {
                   throw new FileNotFoundException("Resource" + USER_FILE_NAME + "not found");
               }
               byte[] buffer = new byte[1024];
               int bytesread;
               while ((bytesread = in.read(buffer)) != -1) {
                   out.write(buffer, 0, bytesread);
               }
               System.out.println("Copied train.json to writeable path:" + WRITABLE_USER_PATH);
           }catch (IOException e)
           {
               e.printStackTrace();
           }

        }
    private void loadUserListFromFile() throws IOException {
     /*-----without input stream-----   userList = OBJECT_MAPPER.readValue(new File(USERS_PATH), new TypeReference<List<User>>() {});   */

     /*---------When Input Stream is used to read the file form class path
        with resource url and inputstream you are loading a copy of file from complied target/classes directory which is generated when you build or run the project.
        so even if you write, it runs without error and updates your in memory data, the update happens in a temp copy not in original src/main/resources/users.json
        if you are packaging this into a jar folder, these files will be inside the jar and read only.


        InputStream is = getClass().getClassLoader().getResourceAsStream("users.json");
        System.out.println(is);
        try{
        userList = OBJECT_MAPPER.readValue(is , new TypeReference<List<User>>() {});
        List<User> userList1 = new ArrayList<>();
        for(User u : userList)
        {
            System.out.println(u);
        }}
        catch (IOException e)
        {
            System.out.println("Error Message while loading" + e.getMessage());
        }
         */
      //  OBJECT_MAPPER.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
       // OBJECT_MAPPER.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);

        File f = new File(WRITABLE_USER_PATH);
        try {
           userList = OBJECT_MAPPER.readValue(f, new TypeReference<List<User>>() {
           });
       }
       catch (IOException e)
       {
          e.printStackTrace();
       }


    }


    private void saveUserListToFile() throws IOException, URISyntaxException {
      /* When the code was running from the specified path itself and we did not use classpath to get the resource. --------------------------------------------------------
        File usersFile = new File(USERS_PATH);
        OBJECT_MAPPER.writeValue(usersFile, userList);*/

      /* When Input Stream is used to get the path from resource and
        URL resource = getClass().getClassLoader().getResource("users.json");
        File f = new File(resource.toURI());   //safer than resource.getFile()
        OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(f, userList);*/

        /* getters and setters are only called when you access fields or jackson reads/write object.*/
        File f = new File(WRITABLE_USER_PATH);
        OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(f, userList);
        System.out.println("Saved user json data to userlist:" + WRITABLE_USER_PATH);

    }


    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(user2 -> {
            //user2 is having the value after iterating the list every time
            //user is the current object that needs to be searched.
            return user2.getUserName().equals(user.getUserName()) && userUtilService.checkpassword(user2.getPassword(), user.getHashPassword());
        }).findFirst();
        return foundUser.isPresent();


    }

    public Boolean signUp(User user1) {
        try {
            //  System.out.println(user1);
            userList.add(user1);
            //  System.out.println("this is signed in and user is added." +user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            return Boolean.FALSE;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void fetchBooking() {
        Optional<User> userFetched = userList.stream().filter(user1 -> {
            return user1.getUserName().equals(user.getUserName()) && userUtilService.checkpassword(user.getPassword(), user1.getHashPassword());
        }).findFirst();
        if (userFetched.isPresent()) {
            userFetched.get().printickets();
        }
    }

    public Boolean cancelBooking(String ticketId){

        //     Scanner s = new Scanner(System.in);
        //    System.out.println("Enter the ticket id to cancel");
        //    ticketId = s.next();

        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty.");
            return Boolean.FALSE;
        }
        OptionalInt index = IntStream.range(0, userList.size()).filter(i -> userList.get(i).getUserName().equals(user.getUserName()) && userUtilService.checkpassword(user.getPassword(), userList.get(i).getHashPassword())).findFirst();
        String finalTicketId1 = ticketId;  //Because strings are immutable
        boolean removed;
        if (index.isPresent()) {
            //ticket is a local variable represting each item in the list ticket, it's each object inside getTicketBooked(); so it comes from user's ticketbooked list.
            removed = userList.get(index.getAsInt()).getTicketBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketId1));
            if (removed){
                System.out.println("Ticket with ID " + ticketId + " has been canceled.");

            }
            try {
                saveUserListToFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            return Boolean.TRUE;
        } else {
            System.out.println("No ticket found with ID " + ticketId);
            return Boolean.FALSE;
        }
    }





    public List<Train> getTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException ex) {
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchseats(Train train) {
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat) {
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                    return true; // Booking successful
                } else {
                    return false; // Seat is already booked
                }
            } else {
                return false; // Invalid row or seat index
            }
        } catch (IOException ex) {
            return Boolean.FALSE;
        }
    }



    public void userticketmapped(User user, List<Ticket> allTickets) throws IOException {
        List<Ticket> userTickets = new ArrayList<>();
        for (Ticket ticket : allTickets) {
            if (ticket.getUserId().equals((user.getUserId()))) {

                userTickets.add(ticket);
            }
          //  boolean b = user.getTicketBooked().isEmpty();
            for(User u : userList)
            {
                if(u.getUserName().equals(user.getUserName()) && userUtilService.checkpassword(user.getPassword(), u.getHashPassword()))
                {
                    boolean b = u.getTicketBooked().isEmpty();
                    System.out.println(b);
                    if(b){
                        u.setTicketBooked(userTickets);
                        update(u);
                    }
                    else {
                        //u.getticketbooked()==null check, ensure you never call .add() on a null list.
                        //Always call u.getTicketBooked().add(ticket) whether it was empty or already had tickets, you are simply appending the new one.
                        u.getTicketBooked().add(ticket);
//                        System.out.println(u.getTicketBooked().add(ticket));
                        update(u);
                    }
                }
            }



           /* System.out.println(b);
            if(b){
                user.setTicketBooked(userTickets);
                update(user);
            }
           else {
               user.getTicketBooked().add(ticket);
         //      System.out.println(user.getTicketBooked().add(ticket));
               update(user);
            }
*/
        }
    }
    void update(User user) {
     OptionalInt index = IntStream.range(0,userList.size()).filter(i->userList.get(i).getUserName().equals(user.getUserName()) && userUtilService.checkpassword(user.getPassword(), userList.get(i).getHashPassword())).findFirst();
     if(index.isPresent())
     {
         userList.set(index.getAsInt(), user);
         try {
             saveUserListToFile();
         } catch (IOException e) {
             throw new RuntimeException(e);
         } catch (URISyntaxException e) {
             throw new RuntimeException(e);
         }
     }
     else {
         System.out.println("User cannot be updated");
     }
    }
}