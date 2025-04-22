package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.Train;
import org.example.entities.User;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {

       Train train;
       private List<Train> trainList;
       private static final ObjectMapper objectMapper = new ObjectMapper();
    //   private static final String TRAIN_DB_PATH = "trains.json"; //"app/src/main/java/org/example/localDb/trains.json";

    public  static final String TRAIN_FILE_NAME="trains.json";
    public  static final String WRITABLE_TRAIN_PATH = System.getProperty("user.home")+File.separator+ TRAIN_FILE_NAME;




    public TrainService() throws IOException {
        ensureUserFileExists();
        loadTrainListFromFile();

    }


    public TrainService(Train train) throws IOException
    {
        this.train=train;
        loadTrainListFromFile();
        System.out.println("this is train " +this.train);


    }

    public void ensureUserFileExists(){

        File userFile = new File(WRITABLE_TRAIN_PATH);
        if(userFile.exists())
        {
            return;
        }
        try(InputStream in = getClass().getClassLoader().getResourceAsStream(TRAIN_FILE_NAME);
            OutputStream out = new FileOutputStream(userFile)) {

            if (in == null) {
                throw new FileNotFoundException("Resource" + TRAIN_FILE_NAME + "not found");
            }
            byte[] buffer = new byte[1024];
            int bytesread;
            while ((bytesread = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesread);
            }
            System.out.println("Copied train.json to writeable path:" + WRITABLE_TRAIN_PATH);
        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    private void loadTrainListFromFile() throws IOException {
       /*----------without inputstream
       trainList = objectMapper.readValue(new File(TRAIN_DB_PATH), new TypeReference<List<Train>>() {});


        -------------------Withinputstream
        InputStream is = getClass().getClassLoader().getResourceAsStream("trains.json");
        System.out.println(is);
        try{
            trainList = objectMapper.readValue(is , new TypeReference<List<Train>>() {});
            List<Train> trainList1 = new ArrayList<>();
            for(Train T : trainList1)
            {
                System.out.println(T);
            }}
        catch (IOException e)
        {
            System.out.println("Error Message while loading" + e.getMessage());
        }  */
        File f = new File(WRITABLE_TRAIN_PATH);
        trainList=objectMapper.readValue(f,new TypeReference<List<Train>>() {});

    }

    private void saveTrainListToFile() {


          /*-----------------------------without refernce url-------------------
            objectMapper.writeValue(new File(TRAIN_DB_PATH), trainList);


           -----------------------------with reference url---------------------------------------
           try{
            URL resource = getClass().getClassLoader().getResource("trains.json");
            File f = new File(resource.toURI());   //safer than resource.getFile()
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(f, trainList);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception based on your application's requirements
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
           */

        File f = new File(WRITABLE_TRAIN_PATH);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(f, trainList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Saved train json data to trainlist:" + WRITABLE_TRAIN_PATH);
    }


    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
    }

        public void addTrain(Train newTrain) {
        // Check if a train with the same trainId already exists
        Optional<Train> existingTrain = trainList.stream()
                .filter(train -> train.getTrainId().equalsIgnoreCase(newTrain.getTrainId()))
                .findFirst();

        if (existingTrain.isPresent()) {
            // If a train with the same trainId exists, update it instead of adding a new one
            updateTrain(newTrain);
        } else {
            // Otherwise, add the new train to the list
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

        public void updateTrain(Train updatedTrain) {
        // Find the index of the train with the same trainId
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            // If found, replace the existing train with the updated one
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        } else {
            // If not found, treat it as adding a new train
            addTrain(updatedTrain);
        }
    }



        private boolean validTrain(Train train, String source, String destination) {
        List<String> stationOrder = train.getStations();

        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destinationIndex = stationOrder.indexOf(destination.toLowerCase());

        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }


    }

