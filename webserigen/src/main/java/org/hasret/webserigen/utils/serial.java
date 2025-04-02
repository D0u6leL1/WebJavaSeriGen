package org.hasret.webserigen.utils;

import java.io.*;
import java.util.Base64;


public class serial {

    public static String serializeBytesToB64(Object obj) throws IOException {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }
    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ser.bin"));
        oos.writeObject(obj);
    }
    public static Object unserialize(String Filename) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Filename));
        Object o = ois.readObject();
        return o;
    }
}
