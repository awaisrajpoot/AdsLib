package com.example.mylibrary.LibHelpers;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SavedFileChecker {

    AppCompatActivity compatActivity;
    String fileName;
    File jsonFile;

    public SavedFileChecker(AppCompatActivity compatActivity, String fileName){
        this.compatActivity = compatActivity;
        this.fileName = fileName;
    }

    public boolean checkFileExist(){
        File file = new File(compatActivity.getFilesDir(), fileName);

        if (file.exists()){

            byte[] bytes = readFileToBytes(file);

            Log.i("file_data","file bytes : "+ bytes.length);

            if (bytes.length >1){
                jsonFile = file;
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }

    }

    public boolean delJsonFile(){
        File file = new File(compatActivity.getFilesDir(), fileName);

        if (file.exists()){
            if (file.delete()){
                Log.i("file_data","file delete successfully !");
                return true;
            }else {
                Log.i("file_data","SaveFileChecker : something wend wrong with file");
                return false;
            }
        }

        return false;
    }
    public File getSavedJsonFile(){
        return jsonFile;
    }

    private static byte[] readFileToBytes(File file) {

        byte[] bytes = new byte[(int) file.length()];

        // funny, if can use Java 7, please uses Files.readAllBytes(path)
        try(FileInputStream fis = new FileInputStream(file)){
            fis.read(bytes);
            return bytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
