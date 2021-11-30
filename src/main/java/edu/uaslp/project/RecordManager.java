package edu.uaslp.project;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecordManager{
    private final int numMaxRecords;
    private int countRecords;
    private final String nameFile;

    public RecordManager(int maxRecords, String name){
        numMaxRecords=maxRecords;
        nameFile=name;
        countRecords=0;
    }

    public void save(Record record) {
        if (record.getNamePlayer() == "")
            throw new RecordsException("Empty name not allowed");

        countRecords++;
        if (countRecords <= numMaxRecords){
            try (FileWriter fileWriter = new FileWriter(nameFile, true);
                 PrintWriter printWriter = new PrintWriter(fileWriter)) {
                printWriter.append(record.getNamePlayer()).append(" ").print(record.getScorePlayer() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(countRecords > numMaxRecords){
            this.saveInFullCase(record);
        }
    }

    public void saveInFullCase(Record record){
        int i;
        List<Record> auxRecords = this.getRecords();

        countRecords=0;

        try (FileWriter fileWriter = new FileWriter(nameFile);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        auxRecords.remove(0);

        this.save(record);
        for(i=0;i<numMaxRecords-1;i++){
            this.save(auxRecords.get(i));
        }
    }

    public List<Record> getRecords(){
        List<Record> records = new ArrayList<Record>();

        try(BufferedReader reader = new BufferedReader(new FileReader(nameFile))){
            String currentLine;

            while((currentLine=reader.readLine()) != null){
                String[] items = currentLine.split(" ");

                records.add(new Record(items[0], Integer.parseInt(items[1])));
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        /*Collections.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record record1, Record record2) {
                if( record1.getScorePlayer() > record2.getScorePlayer())
                    return 1;
                if( record1.getScorePlayer() < record2.getScorePlayer())
                    return -1;
                return 0;
            }
        });*/

        return records;
    }
}