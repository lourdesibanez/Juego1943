package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.filechooser.FileSystemView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import javax.xml.stream.XMLStreamException;

public class Ranking {

    public static final String SCORE_PATH = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +
            "\\ProyectoFinal\\data.json"; 

    public static ArrayList<ScoreData> readFile() throws FileNotFoundException, XMLStreamException {
        ArrayList<ScoreData> dataList = new ArrayList<ScoreData>();

        File file = new File(SCORE_PATH);

        if (!file.exists() || file.length() == 0) {
            return dataList;
        }

        String extension = getFileExtension(file.getName());

        if (extension.equalsIgnoreCase("json")) {
            return readJSONFile(file);
        } else {
            throw new IllegalArgumentException("Invalid file extension");
        }
    }

    public void writeFile(ArrayList<ScoreData> dataList) throws IOException, XMLStreamException {
        File outputFile = new File(SCORE_PATH);
        String extension = getFileExtension(outputFile.getName());

        if (extension.equalsIgnoreCase("json")) {
            writeJSONFile(outputFile, dataList);
        } else {
            throw new IllegalArgumentException("Invalid file extension");
        }
    }

    private static ArrayList<ScoreData> readJSONFile(File file) throws FileNotFoundException {
        ArrayList<ScoreData> dataList = new ArrayList<ScoreData>();

        JSONTokener parser = new JSONTokener(new FileInputStream(file));
        JSONArray jsonList = new JSONArray(parser);

        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject obj = (JSONObject) jsonList.get(i);
            ScoreData data = new ScoreData();
            data.setScore(obj.getInt("score"));
            data.setDate(obj.getString("date"));
            data.setName(obj.getString("name"));
            dataList.add(data);
        }

        return dataList;
    }

    public void writeJSONFile(File file, ArrayList<ScoreData> dataList) throws IOException {
        JSONArray jsonList = new JSONArray();

        for (ScoreData data : dataList) {
            JSONObject obj = new JSONObject();
            obj.put("score", data.getScore());
            obj.put("date", data.getDate());
            obj.put("name", data.getName());
            jsonList.put(obj);
        }

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.toURI()));
        jsonList.write(writer);
        writer.close();
    }

    private static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    public static class ScoreData {
        private String date;
        private int score;
        private String name;

        public ScoreData(int score, String name) {
            this.score = score;
            this.name = name;

            Date today = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date = format.format(today);
        }

        public ScoreData() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name){
            this.name = name;
        }

        public String getDate() {
            return date;
        }


        public void setDate(String date) {
            this.date = date;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}

