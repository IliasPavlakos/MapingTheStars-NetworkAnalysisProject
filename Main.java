package com.company;

import java.io.*;
import java.nio.file.StandardOpenOption;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws IOException {
        //Variables------------------------------------------------------------------------------//
        String name = null;
        int nextIndex,lastIndex;
        String x= null,y= null,z= null,dist= null,bright= null,temp = null,classification = null;
        Vector<String> vec = new Vector<String>(2208*9);

        //Read data------------------------------------------------------------------------------//
        File file = new File("C:\\Users\\Ilias\\Desktop\\stars_dataset.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str = br.readLine(); //Skip naming row
        System.out.print("Reading data...");
        int id = 1;

        //filter nodes and catalog them
        while ((str = br.readLine()) != null) {
            String tempStr = null;
            //name
            lastIndex = 0;
            nextIndex = str.indexOf(",");
            if (nextIndex != -1) name = str.substring(0, nextIndex);
            if(name != null && name.length() == 0) continue;
            //x
            lastIndex = nextIndex + 1;
            nextIndex = str.indexOf(",", lastIndex);
            if (nextIndex != -1) x = str.substring(lastIndex, nextIndex);
            try{ Double.parseDouble(x); }
            catch (Exception e){continue;}
            //y
            lastIndex = nextIndex + 1;
            nextIndex = str.indexOf(",", lastIndex);
            if (nextIndex != -1) y = str.substring(lastIndex, nextIndex);
            try{ Double.parseDouble(y); }
            catch (Exception e){continue;}
            //z
            lastIndex = nextIndex + 1;
            nextIndex = str.indexOf(",", lastIndex);
            if (nextIndex != -1) z = str.substring(lastIndex, nextIndex);
            try{ Double.parseDouble(z); }
            catch (Exception e){continue;}
            //dist
            lastIndex = nextIndex + 1;
            nextIndex = str.indexOf(",", lastIndex);
            if (nextIndex != -1) dist = str.substring(lastIndex, nextIndex);
            try{ Double.parseDouble(dist); }
            catch (Exception e){continue;}
            //bright
            lastIndex = nextIndex + 1;
            nextIndex = str.indexOf(",", lastIndex);
            if (nextIndex != -1) bright = str.substring(lastIndex, nextIndex);
            try{ Double.parseDouble(bright); }
            catch (Exception e){continue;}
            //temp
            lastIndex = nextIndex + 1;
            nextIndex = str.length();
            temp = str.substring(lastIndex, nextIndex);
            try{ Double.parseDouble(temp); }
            catch (Exception e){continue;}

            //Calculate classification
            double tempK = Double.parseDouble(temp);
            if(tempK < 3700) classification = "M";
            else if(tempK < 5200) classification = "K";
            else if(tempK < 6000) classification = "G";
            else if(tempK < 7500) classification = "F";
            else if(tempK < 10000) classification = "A";
            else if(tempK < 30000) classification = "B";
            else if(tempK >= 30000) classification = "O";

            //Catalog node
            vec.add(String.valueOf(id));
            id++;
            vec.add(name);
            vec.add(x);
            vec.add(y);
            vec.add(z);
            vec.add(dist);
            vec.add(bright);
            vec.add(temp);
            vec.add(classification);
        }
        br.close();
        System.out.println("Done");

        //Create gml file------------------------------------------------------------------------//
        try{
        File myGml = new File("C:\\Users\\Ilias\\Desktop\\myGml.gml");
            if (myGml.createNewFile()) {
                System.out.println("File created: " + myGml.getName());
            }else{
                System.out.println("File overwritten");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //Initialize myGml file------------------------------------------------------------------//
        try {
            FileWriter myWriter = new FileWriter("C:\\Users\\Ilias\\Desktop\\myGml.gml");
            myWriter.write("graph [\n");
            myWriter.flush();
            System.out.println("Successfully initialized the file");

            //Create nodes-----------------------------------------------------------------------//
            System.out.print("Creating nodes...");
            for(int i = 0; i < vec.size(); i = i + 9){
                myWriter.write("  node [\n");
                myWriter.write("    id " + vec.get(i) + "\n");
                myWriter.write("    label " + "\"" + vec.get(i+1) + "\"" + "\n");
                myWriter.write("    name " + "\"" + vec.get(i+1) + "\"" + "\n");
                myWriter.write("    x " + vec.get(i+2) + "\n");
                myWriter.write("    y " + vec.get(i+3) + "\n");
                myWriter.write("    z " + vec.get(i+4) + "\n");
                myWriter.write("    distanceFromSun " + vec.get(i+5) + "\n");
                myWriter.write("    brightness " + vec.get(i+6) + "\n");
                myWriter.write("    temperature " + vec.get(i+7) + "\n");
                myWriter.write("    classification " + vec.get(i+8) + "\n");
                myWriter.write("  ]" + "\n");
                myWriter.flush();
            }
            System.out.println("Done");

            //Create edges-----------------------------------------------------------------------//
            System.out.print("Calculating edges...");
            for(int i = 0; i < vec.size(); i = i + 9){
                for(int j = 0; j < vec.size(); j = j + 9) {
                    if(i == j) continue;
                    //Calculate light year distance of stars
                    double ly = calculateDistance(
                            Double.parseDouble(vec.get(i+2)),
                            Double.parseDouble(vec.get(i+3)),
                            Double.parseDouble(vec.get(i+4)),
                            Double.parseDouble(vec.get(j+2)),
                            Double.parseDouble(vec.get(j+3)),
                            Double.parseDouble(vec.get(j+4))
                    );
                    //If distance > 12 light years continue
                    if(ly > 12) continue;
                    //else
                    myWriter.write("  edge [\n");
                    myWriter.write("    source " + vec.get(i) + "\n");
                    myWriter.write("    target " + vec.get(j) + "\n");
                    myWriter.write("    label " + ly + "\n");
                    myWriter.write("    distance " + ly + "\n");
                    myWriter.write("  ]" + "\n");
                }
            }
            System.out.println("Done");

            //End of gml file--------------------------------------------------------------------//
            myWriter.write("]");
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("Task completed");
    }



    //Calculates the distance of 2 objects in 3d space
    public static double calculateDistance(double x1, double y1, double z1, double x2, double y2, double z2){
        return Math.sqrt(Math.pow(x1-(x2),2) + Math.pow(y1-(y2),2) + Math.pow(z1-(z2),2));
    }


}

