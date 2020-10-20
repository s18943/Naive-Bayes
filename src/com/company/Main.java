package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        NaiveClassifier<String, String> bayes = new NaiveClassifier<>();
        String line;
        String TrainFile = "../trainingset.csv";
        String TestFile = "../testset.csv";
        try (BufferedReader br = Files.newBufferedReader(Paths.get(TrainFile), StandardCharsets.US_ASCII)) {
            line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                bayes.learn(new ByesClass<>(Arrays.asList(attributes).subList(0, attributes.length - 1), attributes[attributes.length - 1]));
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        try (BufferedReader br = Files.newBufferedReader(Paths.get(TestFile), StandardCharsets.US_ASCII)) {
            line = br.readLine();
            while (line != null) {
                System.out.print(line + " = ");
                System.out.println(bayes.classify(Arrays.asList(line.split(","))).getCategory());
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        String input;
        Scanner keyboard = new Scanner(System.in);
        do {
            System.out.println("Input all parameters using ','");
            System.out.println("type 'exit' to leave");
            input = keyboard.nextLine();
            System.out.println(bayes.classify(Arrays.asList(input.split(","))).getCategory());
        } while (!input.equals("exit"));
    }
}
