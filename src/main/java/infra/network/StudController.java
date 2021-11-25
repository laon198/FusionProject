package infra.network;

import java.io.IOException;

public class StudController
{
    private Protocol sendPt;

    public StudController()
    {}

    public void setSendPt(Protocol pt)
    {
        sendPt = pt;
    }

    public Protocol getSendPt()
    {
        return sendPt;
    }

    public void handler(Protocol recvPt) throws Exception
    {
        sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch (recvPt.getEntity())
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
            default:
                sendPt.setCode(Protocol.T2_CODE_FAIL);
                setSendPt(sendPt);
        }
    }

    // 생성 요청
    private void createReq (Protocol recvPt) throws Exception
    {
        switch (recvPt.getEntity())
        {
            case Protocol.ENTITY_REGISTRATION:
                createRegistration(recvPt);
                break;
            default:
                sendPt.setCode(Protocol.T2_CODE_FAIL);
                setSendPt(sendPt);
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
            case Protocol.ENTITY_COURSE:  // 교과목 조회
                readCourse();
                break;
            case Protocol.ENTITY_LECTURE:  // 개설교과목 조회
                readLecture();
                break;
            case Protocol.ENTITY_REGIS_PERIOD: // 수강신청기간 조회
                readRegisteringPeriod();
                break;
            case Protocol.ENTITY_PLANNER:  // 강의계획서 조회
                readLecturePlanner(recvPt);
                break;
            case Protocol.ENTITY_STUD_TIMETABLE: // 학생 시간표 조회
                readStudTimetable(recvPt);
                break;
            default:
                sendPt.setCode(Protocol.T2_CODE_FAIL);
                setSendPt(sendPt);
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
                sendPt.setCode(Protocol.T2_CODE_FAIL);
                setSendPt(sendPt);
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
                sendPt.setCode(Protocol.T2_CODE_FAIL);
                setSendPt(sendPt);
        }
    }

    /*
     < 수강신청 >
     클라이언트
     - 수강신청 전 수강신청기간 조회하여 수강신청 가능한지 확인
     - 학생이 수강신청할 개설교과목 선택(수강신청기간에 해당하는 교과목만 출력 및 선택 가능)
     - 수강신청 요청 메시지 전송
     */
    private void createRegistration(Protocol recvPt) throws Exception
    {
        Object data = recvPt.getObject();
        // 수강신청 기능 수행
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);

        // 실패 - 수강신청인원초과 or 시간표중복
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
    }

    /*
    < 개인정보 조회 >
     */
    private void readAccount() throws Exception
    {
        // 개인정보 조회 기능 수행
        Object sndData = null;
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setObject(sndData);

        //sendPt.setCode(Protocol.T2_CODE_FAIL);
    }

    /*
    < 교과목 조회 (전체) >
     */
    private void readCourse() throws Exception
    {
        // 교과목 전체 조회 기능 수행
        Object[] sndData = null;
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setObjectArray(sndData);

        // 교과목 조회 실패 - 존재하는 교과목 없음
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
    }

    /*
    < 개설교과목 조회 (전학년) >
     */
    private void readLecture() throws Exception
    {
        // 개설교과목 전체 조회 기능 수행
        Object[] sndData = null;
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setObjectArray(sndData);
        // 개설교과목 조회 실패 - 존재하는 개설교과목 없음
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
    }

    /*
    < 수강신청기간 조회 >
    createRegistration 할 때 필요
    수강신청기간을 보내주는 것이 아니라 현재 날짜가 수강신청 가능한 날짜인지 yes(success)/no(fail)로 답변
     */
    private void readRegisteringPeriod() throws Exception
    {
        // 수강신청 기간 조회 기능 수행

        // if (수강신청기간임)
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        // else
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
    }

    /*
    < 강의계획서 조회 >
    클라이언트
    - 개설교과목 조회 -> 사용자가 개설교과목 선택 -> 개설교과목 pk 전송
    */
    private void readLecturePlanner(Protocol recvPt) throws Exception
    {
        Object data = recvPt.getObject();

        // pk로 강의계획서 조회 기능 수행

        // if (강의계획서 등록O)
        Object sndData = null;
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setObject(sndData);
        // else (강의계획서 등록X)
        // sendPt.setCode(Protocol.T2_CODE_FAIL);

    }

    /*
     < 학생 시간표 조회 >
     */
    private void readStudTimetable(Protocol recvPt) throws Exception
    {
        // 시간표 조회 기능 수행
        Object[] sndData = null;
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setObjectArray(sndData);
        //시간표 조회 실패 - 수강신청 기록 없음
        //sendPt.setCode(Protocol.T2_CODE_FAIL);
    }



    /*
     < 개인정보(전화번호) 수정 및 비밀번호 수정 >
     */
    private void updateAccount(Protocol recvPt) throws Exception
    {
        /*
         받은 body 형식
         - 전화번호 수정인 경우
            : 1 010-0000-0000
         - 비밀번호 수정인 경우
            : 2 currentPassword newPassword
         */

        Object data = recvPt.getObject();

        // if (전화번호 수정)
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        // else (비밀번호 수정)
        try {
            // 현재 비밀번호 일치하는지 확인
            // 비밀번호 수정 기능 수행
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        } catch (IllegalArgumentException e) { // 현재 비밀번호 불일치
            sendPt.setCode(Protocol.T2_CODE_FAIL);
        }
    }

    /*
     < 수강신청 취소 >
     클라이언트
      - 학생의 시간표 조회 -> 수강하는 교과목 목록 중 선택 -> 개설교과목 pk 전송
     */
    private void deleteRegistration(Protocol recvPt) throws Exception {

        Object data = recvPt.getObject();
        // 수강신청 취소 기능 수행

        try {
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        } catch (IllegalArgumentException e) { // 삭제 실패 (존재하지 않는 pk) - 이런 경우가 있을진 모르겠음
            sendPt.setCode(Protocol.T2_CODE_FAIL);
        }
    }

}
