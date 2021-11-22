package infra.network;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Serializer {
//    public static byte[] ObjectMetaToBytes(Object obj, int count){
//    }

    //TODO : primitive멤버만 가지는거 직렬화가능, 참조형태 직렬화 테스트필요
    public static byte[] objectToBytes(Object obj) throws IllegalAccessException {
        List<byte[]> byteList = new LinkedList<>();

        byte[] classNameBytes = new byte[32];
        String className = obj.getClass().getName();
        System.out.println("className = " + className);
        System.arraycopy(
                className.getBytes(StandardCharsets.UTF_8), 0,
                classNameBytes, 0, className.getBytes(StandardCharsets.UTF_8).length
        );
        byteList.add(classNameBytes);

        int objLength = 32;
        for(Field f : getAllFields(obj.getClass())){

            byte[] fieldBytes = getBytesFrom(obj, f);
            byte[] fieldLength = intToBytes(fieldBytes.length);

            byteList.add(fieldLength);
            byteList.add(fieldBytes);
            objLength+=fieldBytes.length+4;
        }
        byteList.add(0, intToBytes(objLength+4));

        byte[] result = new byte[objLength+4];
        int cursor = 0;
        for(byte[] b : byteList){
            System.arraycopy(b, 0, result, cursor, b.length);
            cursor+=b.length;
        }

        return result;
    }

    public static List<Field> getAllFields(Class clazz){
        if(clazz==Object.class){
            return Collections.emptyList();
        }

        List<Field> result = new ArrayList<>(getAllFields(clazz.getSuperclass()));
        for(Field f : clazz.getDeclaredFields()){
            if(Modifier.isStatic(f.getModifiers())){
                continue;
            }

            result.add(f);
        }

        return result;
    }

    public static byte[] getBytesFrom(Object obj, Field f) throws IllegalAccessException {
        String typeName = f.getType().getSimpleName();
        f.setAccessible(true);

        //TODO : primitive 타입 추가필요
        switch(typeName){
            case "byte":
                return new byte[]{f.getByte(obj)};
            case "char":
                return charToBytes(f.getChar(obj));
            case "short":
                return shortToBytes(f.getShort(obj));
            case "int":
                return intToBytes(f.getInt(obj));
            case "long":
                return longToBytes(f.getLong(obj));
            case "String":
                return new String((String)f.get(obj)).getBytes(StandardCharsets.UTF_8);
            default:
                return objectToBytes(f.get(obj));
        }
    }

    public static byte[] longToBytes(long l) {
        byte[] bytes = new byte[8];

        for(int i=0, shift=56; i<bytes.length; i++, shift-=8){
            bytes[i] = (byte) (l>>shift);
        }

        return bytes;
    }

    public static byte[] charToBytes(char c){
        byte[] bytes = new byte[2];

        for(int i=0, shift=8; i<bytes.length; i++, shift-=8){
            bytes[i] = (byte) (c>>shift);
        }

        return bytes;
    }

    public static byte[] shortToBytes(short s){
        byte[] bytes = new byte[2];

        for(int i=0, shift=8; i<bytes.length; i++, shift-=8){
            bytes[i] = (byte) (s>>shift);
        }

        return bytes;
    }

    public static byte[] intToBytes(int i){
        byte[] bytes = new byte[4];

        for(int j=0, shift=24; j<bytes.length; j++, shift-=8){
            bytes[j] = (byte) (i>>shift);
        }

        return bytes;
    }
}