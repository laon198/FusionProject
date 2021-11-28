package infra.network;
import java.io.IOException;
import java.io.OutputStream;

public class ProtocolService {
    private OutputStream os;

    public ProtocolService(OutputStream os) {
        this.os = os;
    }

    // 실패 메시지 응답
    public void responseFail() throws IOException {
        Protocol pt = new Protocol(Protocol.TYPE_RESPONSE, Protocol.T2_CODE_FAIL);
        pt.send(os);
    }

    public void reponseSuccess() throws IOException {
        Protocol pt = new Protocol(Protocol.TYPE_RESPONSE, Protocol.T2_CODE_SUCCESS);
        pt.send(os);
    }

    public void responseObject(Object data) throws IOException, IllegalAccessException {
        if (data == null)
            responseFail();
        else
        {
            Protocol pt = new Protocol(Protocol.TYPE_RESPONSE, Protocol.T2_CODE_SUCCESS);
            pt.setObject(data);
            pt.send(os);
        }
    }

    public void responseObjectArray(Object[] data) throws IOException, IllegalAccessException {
        if (data == null)
            responseFail();
        else
        {
            Protocol pt = new Protocol(Protocol.TYPE_RESPONSE, Protocol.T2_CODE_SUCCESS);
            pt.setObjectArray(data);
            pt.send(os);
        }
    }
}
