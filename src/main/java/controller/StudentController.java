package controller;

import infra.network.Protocol;
import infra.network.ProtocolService;
import java.io.IOException;
import java.io.OutputStream;

public class StudentController implements DefinedController {

    private ProtocolService ps;

    public StudentController(OutputStream os)
    {
        ps = new ProtocolService(os);
    }

    @Override
    public int handler(Protocol recvPt) throws Exception
    {
        switch (recvPt.getCode())
        {
            case Protocol.T1_CODE_CREATE: // 등록
                createReq(recvPt);
                break;
            case Protocol.T1_CODE_READ:   // 조회
                readReq(recvPt);
                break;
            case Protocol.T1_CODE_UPDATE:  // 변경
                updateReq(recvPt);
                break;
            case Protocol.T1_CODE_DELETE:  // 삭제
                deleteReq(recvPt);
                break;
            case Protocol.T1_CODE_LOGOUT:   // 로그아웃
                logoutReq();
                return MainController.USER_UNDEFINED;
            default:
                ps.responseFail();
        }
        return MainController.STUD_TYPE;
    }


    // 로그아웃 요청
    private void logoutReq() throws IOException {
        ps.reponseSuccess();
    }

    // 생성 요청
    private void createReq (Protocol recvPt) throws Exception
    {
        switch (recvPt.getEntity())
        {
            case Protocol.ENTITY_REGISTRATION: // 수강신청
                createRegistration(recvPt);
                break;
            default:
                ps.responseFail();

        }
    }

    // 조회 요청
    private void readReq (Protocol recvPt) throws Exception
    {
        switch (recvPt.getEntity())
        {
            case Protocol.ENTITY_ACCOUNT: // 계정정보 조회
                readAccount();
                break;
            case Protocol.ENTITY_LECTURE:  // 개설교과목 조회
                readLecture();
                break;
            case Protocol.ENTITY_REGIS_PERIOD: // 수강신청기간 조회
                readRegisteringPeriod();
                break;
            case Protocol.ENTITY_REGISTRATION: // 수강신청 현황 조회 요청
                readRegisteringList();
                break;
            default:
                ps.responseFail();


        }
    }


    // 변경 요청
    private void updateReq (Protocol recvPt) throws Exception
    {
        switch (recvPt.getEntity())
        {
            case Protocol.ENTITY_ACCOUNT: // 계정정보 변경
                updateAccount(recvPt);
                break;
            default:
                ps.responseFail();

        }
    }

    // 삭제 요청
    private void deleteReq (Protocol recvPt) throws Exception
    {
        switch (recvPt.getEntity())
        {
            case Protocol.ENTITY_REGISTRATION:  // 수강신청 취소
                deleteRegistration(recvPt);
                break;
            default:
                ps.responseFail();

        }
    }

    /*
     < 수강신청 >
     */
    private void createRegistration(Protocol recvPt) throws Exception
    {
        Object data = recvPt.getObject();
        // 수강신청 기능 수행

        // 성공이면
        ps.reponseSuccess();
        // 실패면
        // ps.responseFail();
    }

    /*
    < 개인정보 조회 >
     */
    private void readAccount() throws Exception
    {
        // 개인정보 조회 기능 수행
        Object sndData = null;
        ps.responseObject(sndData);
    }


    /*
    < 개설교과목 조회 (전학년) >
     */
    private void readLecture() throws Exception
    {
        // 개설교과목 전체 조회 기능 수행
        Object[] sndData = null;
        ps.responseObjectArray(sndData);
    }

    /*
    < 수강신청기간 조회 >
    createRegistration 할 때 필요
    수강신청기간을 보내주는 것이 아니라 현재 날짜가 수강신청 가능한 날짜인지 yes(success)/no(fail)로 답변
     */
    private void readRegisteringPeriod() throws Exception
    {
        // 성공이면
        ps.reponseSuccess();
        // 실패면
        // ps.responseFail();
    }


    /*
    < 강의계획서 조회 >
    */
    private void readLecturePlanner(Protocol recvPt) throws Exception
    {
    }

    /*
     < 학생 시간표 조회 >
     */
    private void readStudTimetable(Protocol recvPt) throws Exception
    {
    }

    /*
     < 수강 신청 현황 조회>
     */
    private void readRegisteringList() throws IOException, IllegalAccessException {
        Object[] data = null;
        ps.responseObjectArray(data);
    }


    /*
     < 개인정보(전화번호) 수정 및 비밀번호 수정 >
     */
    private void updateAccount(Protocol recvPt) throws Exception
    {
        Object data = recvPt.getObject();
        // 성공이면
        ps.reponseSuccess();
        // 실패면
        // ps.responseFail();
    }

    /*
     < 수강신청 취소 >
     */
    private void deleteRegistration(Protocol recvPt) throws Exception {

        Object data = recvPt.getObject();
        // 성공이면
        ps.reponseSuccess();
        // 실패면
        // ps.responseFail();
    }

}
