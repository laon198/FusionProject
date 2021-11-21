package infra.network;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deserializer {
    //TODO : 예외처리필요
    public static Object bytesToObject(byte[] bytes) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String className = new String(
                bytes, 4, 32
        ).trim();

        //class의 전체 필드정보를 가져온다
        //빈생성자를 통해 객체를 만든다
        //class의 변수들을 순회하면서 byte에서 값을 변형시킨다(타입에맞게)
        //생성된 객체에 값을 세팅한다.

        //class의 전체 필드정보를 가져온다 / 해당클래스의 변수 개수를 가져온다
        //해당클래스의 변수개수만큼 Object배열을생성한ㄷ.
        //Object배열에 바이트배열에서 가져온 필드를 집어넣는다.
        //object배열로 인스턴스를 생성한다.

        //TODO : prefix설정 필요
        Class clazz = Class.forName(className);
        Constructor constructor = clazz.getDeclaredConstructors()[0]; //TODO : 각객체는 빈생성자 가져야함

        Object obj;
        if(constructor.getParameterCount()>0){
            Object[] params = new Object[constructor.getParameterCount()];
            int cursor = 0;
            for(Class c : constructor.getParameterTypes()){
                String typeName = c.getSimpleName();
                switch (typeName){
                    case "byte":
                    case "char":
                    case "short":
                    case "int":
                    case "long":
                        params[cursor++]=0;
                        break;
                    case "boolean":
                        params[cursor++]=false;
                        break;
                    default:
                        params[cursor++]=new Object();
                        break;
                }
            }
            obj = constructor.newInstance(params);
        }else{
            obj = constructor.newInstance();
        }

        int cursor = 36;
        for(Field f : getAllFields(clazz)){
            int length = bytesToInt(bytes, cursor);
            cursor += 4;
            setData(obj, f, bytes, cursor, length);
            cursor += length;
        }

        return obj;
    }

    private static List<Field> getAllFields(Class clazz){
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

    private static void setData(Object obj, Field f, byte[] bytes, int cursor, int length) throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        String typeName = f.getType().getSimpleName();
        f.setAccessible(true);

        //TODO : primitive 타입 추가필요
        switch(typeName){
            case "byte":
                f.setByte(obj, bytes[cursor]);
                return;
            case "char":
                f.setChar(obj, bytesToChar(bytes, cursor));
                return;
            case "short":
                f.setShort(obj, bytesToShort(bytes, cursor));
                return;
            case "int":
                f.setInt(obj, bytesToInt(bytes, cursor));
                return;
            case "long":
                f.setLong(obj, bytesToLong(bytes, cursor));
                return;
            case "String":
                f.set(obj, new String(bytes, cursor, length));
                return;
            default:
                byte[] a = new byte[length];
                System.arraycopy(bytes, cursor, a, 0, length);
                f.set(obj, bytesToObject(a));
                return;
        }
    }

    public static char bytesToChar(byte[] bytes, int cursor){
        byte[] charBytes = new byte[2];
        System.arraycopy(bytes, cursor, charBytes, 0, 2);

        return (char)(((charBytes[0] & 0xFF) << 8) |
                (charBytes[1] & 0xFF));
    }

    public static short bytesToShort(byte[] bytes, int cursor){
        byte[] shortBytes = new byte[2];
        System.arraycopy(bytes, cursor, shortBytes, 0, 2);

        return (short)(((shortBytes[0] & 0xFF) << 8) |
                (shortBytes[1] & 0xFF));
    }

    public static int bytesToInt(byte[] bytes, int cursor){
        byte[] intBytes = new byte[4];
        System.arraycopy(bytes, cursor, intBytes, 0, 4);

        return ((intBytes[0] & 0xFF) << 24) |
                ((intBytes[1] & 0xFF) << 16) |
                ((intBytes[2] & 0xFF) << 8) |
                (intBytes[3] & 0xFF);
    }

    public static long bytesToLong(byte[] bytes, int cursor){
        byte[] longBytes = new byte[8];
        System.arraycopy(bytes, cursor, longBytes, 0, 8);

        return (long)((long)(longBytes[0] & 0xFF) << 56) |
                ((long)(longBytes[1] & 0xFF) << 48) |
                ((long)(longBytes[2] & 0xFF) << 40) |
                ((long)(longBytes[3] & 0xFF) << 32) |
                ((long)(longBytes[4] & 0xFF) << 24) |
                ((long)(longBytes[5] & 0xFF) << 16) |
                ((long)(longBytes[6] & 0xFF) << 8) |
                ((long)longBytes[7] & 0xFF);
    }
}
