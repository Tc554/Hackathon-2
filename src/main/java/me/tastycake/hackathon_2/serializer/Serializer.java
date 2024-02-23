package me.tastycake.hackathon_2.serializer;

import java.io.*;

public class Serializer {
    public static byte[] byteSerialize(Object serializable) throws Exception {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(serializable);
            return bos.toByteArray();
        }
    }

    public static Object byteDeserialize(byte[] data) throws Exception {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        }
    }
}
