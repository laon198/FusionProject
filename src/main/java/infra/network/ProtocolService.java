package infra.network;
import java.io.IOException;
import java.io.OutputStream;

public class StudentProtocolService {
    private OutputStream os;

    public StudentProtocolService(OutputStream os) {
        this.os = os;
    }

    // 실패 메시지 응답
    public void resFailMessage() throws IOException {
        Protocol pt = new Protocol(Protocol.TYPE_RESPONSE, Protocol.T2_CODE_FAIL);
        pt.send(os);
    }

    // 개인 정보 조회에 대한 응답
    public void resReadPersonalInfo(Object data) throws IOException, IllegalAccessException {
        if (data == null)
            resFailMessage();
        else
        {
            Protocol pt = new Protocol(Protocol.TYPE_RESPONSE, Protocol.T2_CODE_SUCCESS);
            pt.setObject(data);
            pt.send(os);
        }
    }

}
