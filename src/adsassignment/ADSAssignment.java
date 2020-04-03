/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsassignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class ADSAssignment {

    HashMap<String, String> store = new HashMap<String, String>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException, IOException {
        // TODO code application logic here
        ADSAssignment obj = new ADSAssignment();
        obj.loadfiledata();
        Scanner sc = new Scanner(System.in);
        String filename,oldvalue="0";
        String hashedfile;
        String hashedfilename;
        int choice=0;
        while(choice!=6){
        System.out.println("ENTER CHOICE:: 1.ADD NEW FILE 2.CHECK FILE 3.UPDATE FILE 4.DELETE FILE 5.WRITE TO DISK FILE 6.EXIT");
        choice = sc.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Enter file name");
                filename = sc.next();
                hashedfile = obj.calculatehash(filename);
                hashedfilename = obj.calculatfilename(filename);
                obj.store.put(hashedfilename, hashedfile);
                break;
            case 2:
                System.out.println("Enter file name");
                filename = sc.next();
                hashedfilename = obj.calculatfilename(filename);
                oldvalue = obj.store.get(hashedfilename);
                System.out.println("OLD VALUE:: "+ oldvalue);
                if (oldvalue.equals("0")) {
                    System.out.println("NO such file recorded");
                } else {
                    hashedfile = obj.calculatehash(filename);
                    if (oldvalue.equals(hashedfile)) {
                        System.out.println("NO TAMPERING");
                    } else {
                        System.out.println("TAMPERED");
                    }
                }
            break;
        case 3: System.out.println("Enter file name");
                filename = sc.next();
                hashedfilename = obj.calculatfilename(filename);
                hashedfile = obj.calculatehash(filename);
                obj.store.replace(hashedfilename,hashedfile);
            break;
        case 4:System.out.println("Enter file name");
                filename = sc.next();
                hashedfilename = obj.calculatfilename(filename);
                obj.store.remove(hashedfilename);
                System.out.println("Successfully deleted !!");
        case 5: obj.storefiledata();
        }
    }
    }

    public int storefiledata() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("DATA.txt");
        writer.print("");
        writer.close();
        try {
            FileWriter myWriter = new FileWriter("DATA.txt");
            store.forEach((key,value) -> {
                try {
                    myWriter.write(key+" "+value+"\n");
                } catch (IOException ex) {
                    Logger.getLogger(ADSAssignment.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            myWriter.close();
            System.out.println("Successfully updated to the disk file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return -1;
    }

    public int loadfiledata() throws FileNotFoundException {
        try {
            String s, x = "0";
            Scanner scanner = new Scanner(new File("DATA.txt"));
            while (scanner.hasNextLine()) {
                s = scanner.nextLine();
                String splitvalues[] = s.split(" ");
                store.put(splitvalues[0], splitvalues[1]);
            }
            scanner.close();
            System.out.println(Arrays.asList(store));
            return 1;
        } catch (Exception e) {
            System.out.println("ERROR WHILE LOADING" + e);
            return -1;
        }
    }

    public String calculatfilename(String filename) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashInBytes = md.digest(filename.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        System.out.println("HASHED FILENAME VALUE::  "+sb.toString());
        return sb.toString();
    }

    private static String bytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();

    }

    private static byte[] checksum(String filepath, MessageDigest md) throws IOException {

        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(filepath), md)) {
            while (dis.read() != -1) ; //empty loop to clear the data
            md = dis.getMessageDigest();
        }

        return md.digest();

    }

    public String calculatehash(String filename) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashInBytes = checksum("G:\\" + filename, md);
        System.out.println("NEW HASHED FILE VALUE:: "+bytesToHex(hashInBytes));
        return bytesToHex(hashInBytes);
    }

}
