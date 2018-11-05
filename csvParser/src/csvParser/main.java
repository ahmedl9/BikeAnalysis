package csvParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class main {
	
	
	//Read from csv file and create 2D array
    public static void main(String[] args) throws IOException {
    	BufferedReader b = null;
    	ArrayList<String[]> file = new ArrayList<String[]>();
        try {
        	b = new BufferedReader(new FileReader("metro-bike-share-trip-data.csv"));
            String line = "";
            while ((line = b.readLine()) != null) {
                String[] sections = line.split(",");
                file.add(sections);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	b.close();
        }
        
        //Initalize various metrics & heuristics for later analysis
        HashMap<String, Integer> startFreqs = new HashMap<String, Integer>();
        HashMap<String, Integer> stopFreqs = new HashMap<String, Integer>();
        HashMap<String, Integer> combFreqs = new HashMap<String, Integer>();
        double distances = 0;
        double dcount = 0;
        int shareCount = 0;
        int roundCount = 0;
        int oneCount = 0;
        int NroundCount = 0;
        int NoneCount = 0;
        
        for (int x = 1; x < file.size(); x++) {
        	String Sstation = file.get(x)[4];
        	String Fstation = file.get(x)[7];

        	if (startFreqs.containsKey(Sstation)) {
        		startFreqs.put(Sstation, startFreqs.get(Sstation)+1);
        	}
        	else {
        		startFreqs.put(Sstation, 0);
        	}
        	
        	if (stopFreqs.containsKey(Fstation)) {
        		stopFreqs.put(Fstation, stopFreqs.get(Fstation)+1);
        	}
        	else {
        		stopFreqs.put(Fstation, 0);
        	}
        	
        	if (!file.get(x)[5].equals("") && !file.get(x)[6].equals("") && !file.get(x)[8].equals("") && !file.get(x)[9].equals("")) {
        		dcount++;
        		double slatitude = Double.valueOf(file.get(x)[5]);
        		double slongitude = Double.valueOf(file.get(x)[6]);
        		double flatitude = Double.valueOf(file.get(x)[8]);
        		double flongitude = Double.valueOf(file.get(x)[9]);
        		distances = distances + Math.sqrt((flatitude-slatitude)*(flatitude-slatitude) + (flongitude-slongitude)*(flongitude-slongitude));
        	}
        	
        	if (!file.get(x)[13].equals("Walk-up")) {
        		shareCount++;
        	}
        	
        	boolean morning = (Integer.valueOf(file.get(x)[2].substring(11, 13)) < 12);
        	
        	if (file.get(x)[12].equals("One Way") && morning) {
        		oneCount++;
        	}
        	if (file.get(x)[12].equals("Round Trip") && morning) {
        		roundCount++;
        	}
        	
        	if (file.get(x)[12].equals("One Way") && !morning) {
        		NoneCount++;
        	}
        	if (file.get(x)[12].equals("Round Trip") && !morning) {
        		NroundCount++;
        	}
        	
        	String combo = file.get(x)[12] + " " + file.get(x)[13];
        	if (combFreqs.containsKey(combo)) {
        		combFreqs.put(combo, combFreqs.get(combo)+1);
        	}
        	else {
        		combFreqs.put(combo, 0);
        	}
        	
        }
        
        String maxSStation ="";
        String maxFStation = "";
        for (String key : startFreqs.keySet()) {
        	if (maxSStation == "" || startFreqs.get(key) > startFreqs.get(maxSStation)) {
        		maxSStation = key;
        	}
        }
        for (String key : stopFreqs.keySet()) {
        	if (maxFStation == "" || stopFreqs.get(key) > stopFreqs.get(maxFStation)) {
        		maxFStation = key;
        	}
        }
        
        double avgDistance = distances/(dcount);
        
        System.out.println("the stations are: " + maxSStation + " " + maxFStation);
        System.out.println("the distance is : " + avgDistance);
        System.out.println("the regular rider count is : " + shareCount);
        
        for (String key : combFreqs.keySet()) {
        	System.out.println(key + " has " + combFreqs.get(key));
        }
        
        System.out.println("In the morning the one-way is : " + oneCount + " and the round trip count is : " + roundCount);
        System.out.println("In the night the one-way is : " + NoneCount + " and the round trip count is : " + NroundCount);

        FileWriter towrite = new FileWriter("processed.csv");
        StringBuilder writer = new StringBuilder();
        writer.append(maxSStation);
        writer.append(",");
        writer.append(maxFStation);
        writer.append(",");
        writer.append(String.valueOf(avgDistance));
        writer.append(",");
        writer.append(String.valueOf(shareCount));
        writer.append(",");

        for (String key : combFreqs.keySet()) {
            writer.append(String.valueOf(combFreqs.get(key)));
            writer.append(",");
        }
        writer.append(String.valueOf(oneCount));
        writer.append(",");
        writer.append(String.valueOf(roundCount));
        writer.append(",");
        writer.append(String.valueOf(NoneCount));
        writer.append(",");
        writer.append(String.valueOf(NroundCount));
        writer.append(",");
        writer.append("\n");
        
        towrite.write(writer.toString());
        towrite.close();


        
        
        

    }

}

