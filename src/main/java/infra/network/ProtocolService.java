package infra.network;
import java.io.IOException;
import java.io.OutputStream;

public class ProtocolService {
    private OutputStream os;

    public ProtocolService(OutputStream os) {
        this.os = os;
    }

    // 실패 메시지 응답
    public void resFailMessage() throws IOException {
        Protocol pt = new Protocol(Protocol.TYPE_RESPONSE, Protocol.T2_CODE_FAIL);
        pt.send(os);
    }

    public void reponseHeaderOnly(boolean succeed) throws IOException {
        if (succeed)
        {
            Protocol pt = new Protocol(Protocol.TYPE_RESPONSE, Protocol.T2_CODE_SUCCESS);
            pt.send(os);
        }
        else
            resFailMessage();
    }

    public void responseObject(Object data) throws IOException, IllegalAccessException {
        if (data == null)
            resFailMessage();
        else
        {
            Protocol pt = new Protocol(Protocol.TYPE_RESPONSE, Protocol.T2_CODE_SUCCESS);
            pt.setObject(data);
            pt.send(os);
        }
    }

    public void responseObjectArray(Object[] data) throws IOException, IllegalAccessException {
        if (data == null)
            resFailMessage();
        else
        {
            Protocol pt = new Protocol(Protocol.TYPE_RESPONSE, Protocol.T2_CODE_SUCCESS);
            pt.setObjectArray(data);
            pt.send(os);
        }
    }
}
