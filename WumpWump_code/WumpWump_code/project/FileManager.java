package project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileManager {
    public static <T> T read(Class<T> type, String fileName) {
        T object = null;
        try {
            FileInputStream fi = new FileInputStream(fileName + ".txt");
            ObjectInputStream oi = new ObjectInputStream(fi);

            object = (T)oi.readObject();

            oi.close();
            fi.close();

        } catch (FileNotFoundException e) { System.out.println("File not found"); }
        catch (IOException e) { System.out.println("Error initializing stream "); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }

        return object;
    }

    public static <T> void write(T object, String fileName) {
        try {
            FileOutputStream f = new FileOutputStream(fileName + ".txt");
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(object);

            o.close();
            f.close();
        } catch (FileNotFoundException e) { System.out.println("File not found"); }
        catch (IOException e) { System.out.println("Error initializing stream " ); }
    }
}
