package org.hasret.webserigen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.hasret.webserigen.payloads.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.util.*;

@Controller
@RequestMapping
public class MainController {

    private final List<String> chains = Arrays.asList(
            "CommonsCollections1",
            "CommonsCollections2"
    );

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("chains", chains);
        model.addAttribute("selectedChain", "CommonsCollections1");
        return "index";
    }



    @PostMapping(value = "/generate", produces = "application/json")
    @ResponseBody
    public Map<String, String> generatePayload(@RequestBody PayloadRequest request) {

        String payload = generateActualPayload(request.getChain(), request.getCommand());
        return Collections.singletonMap("payload", payload);
    }

    public String generateActualPayload(String chain, String command) {
        //寻找chains and trans args
        try {
            String className = "org.hasret.webserigen.payloads."+ chain;
            Class<?> clazz = Class.forName(className);
            Object payloadInstance = clazz.getDeclaredConstructor().newInstance();
            //Object payloadInstance2 = clazz.newInstance();
            Method method = clazz.getDeclaredMethod("generatePayload",String.class);
            method.setAccessible(true);
            String payload = (String) method.invoke(payloadInstance, command);
            return payload;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/test")
    public String showForm() {
        return "test";
    }

    //TEST PAGE 测试
    @PostMapping("/test")
    public Map<String,Object> testDeserialization(@RequestParam String payload) throws IOException {
        Map<String,Object> map = new HashMap<>();
        try {
            //System.out.println(payload);
            byte[] payloadbytes = Base64.getDecoder().decode(payload);
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(payloadbytes))) {
                Object obj = ois.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }


    static class PayloadRequest {
        private String chain;
        private String command;
        public String getChain() {
            return chain;
        }
        public void setChain(String chain) {
            this.chain = chain;
        }
        public String getCommand() {
            return command;
        }
        public void setCommand(String command) {
            this.command = command;
        }
    }
}