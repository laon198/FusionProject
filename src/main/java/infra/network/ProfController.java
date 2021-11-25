package infra.network;

import java.io.IOException;

public class ProfController
{
    private Protocol sendPt;

    public ProfController()
    {}

    public void setSendPt(Protocol pt)
    {
        sendPt = pt;
    }

    public Protocol getSendPt()
    {
        return sendPt;
    }

    public void handler(Protocol recvPt) throws IOException
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
            default:
                sendPt.setCode(Protocol.T2_CODE_FAIL);
                setSendPt(sendPt);
        }
    }

    // 생성 요청
    private void createReq (Protocol recvPt) throws IOException
    {
        switch (recvPt.getEntity())
        {
            case Protocol.ENTITY_PLANNER:
                createLecturePlanner(recvPt);
                break;
            default:
                sendPt.setCode(Protocol.T2_CODE_FAIL);
                setSendPt(sendPt);
        }
    }

    // 조회 요청
    private void readReq (Protocol recv) throws IOException
    {
        switch (recv.getEntity())
        {
            case Protocol.ENTITY_ACCOUNT: // 계정정보 조회
                readAccount();
                break;
            case Protocol.ENTITY_COURSE:  // 교과목 조회
                readCourse();
                break;
            case Protocol.ENTITY_LECTURE:  // 개설교과목 조회
                readLecture(recv);
                break;
            case Protocol.ENTITY_PLANNER:  // 강의계획서 조회
                readLecturePlanner(recv);
                break;
            case Protocol.ENTITY_PROF_TIMETABLE:  // 교수 강의시간표 조회
                readProfTimetable();
                break;
            case Protocol.ENTITY_LECTURE_STUD_LIST:  // 수강신청학생목록 조회
                readLectureStudList(recv);
                break;
            default:
                sendPt.setCode(Protocol.T2_CODE_FAIL);
                setSendPt(sendPt);
        }
    }

    // 변경 요청
    private void updateReq (Protocol recv) throws IOException
    {
        switch (recv.getEntity())
        {
            case Protocol.ENTITY_ACCOUNT: // 계정정보 변경
                updateAccount(recv);
                break;
            case Protocol.ENTITY_PLANNER:  // 강의계획서 변경
                updatePlanner(recv);
                break;
            default:
                sendPt.setCode(Protocol.T2_CODE_FAIL);
                setSendPt(sendPt);
        }
    }

    /*
    < 강의계획서 생성 >
    클라이언트
    - 개설교과목 조회 (해당 교수의 교과목)
    - 개설 교과목 선택 (강의계획서 존재X ->) 강의계획서 입력 -> 개설교과목 pk 및 강의계획서 내용 전송
     */
    private void createLecturePlanner(Protocol recvPt) throws IOException
    {
        Object data = recvPt.getBody();
        /*
         강의계획서 생성하는 기능 수행
         */
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);  
        
        // 강의계획서 생성 실패
        // sendPt.setCode(Protocol.T2_CODE_FAIL);  
    }

    /*
    < 개인정보 조회 >
     */
    private void readAccount() throws IOException
    {
        /*
         개인정보 조회 기능 수행
         */
        Object sndData = null;
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setBody(sndData);

        //sendPt.setCode(Protocol.T2_CODE_FAIL);
    }

    /*
    < 교과목 조회 (전체) >
     */
    private void readCourse() throws IOException
    {
        // 교과목 전체 조회 기능 수행
        Object data = null;
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setBody(data);

        // 교과목 조회 실패 - 존재하는 교과목 없음
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
    }

    /*
    < 개설 교과목 정보 조회 >
     */
    private void readLecture(Protocol recvPt) throws IOException
    {
        Object option = recvPt.getBody();  // 학년,학과,교수 option
        /*
        개설 교과목 정보 조회하는 기능 수행
         */
        //if 옵션 잘못됨
        //sendPt.setCode(Protocol.T2_CODE_FAIL);
        Object sndData = null;
        if (sndData != null)
        {
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.setBody(sndData);
        }
        else // 실패 - 개설 교과목 없는 경우
            sendPt.setCode(Protocol.T2_CODE_FAIL);
    }


    /*
    < 강의계획서 조회 >
    클라이언트
    - 개설교과목 조회 -> 사용자가 개설교과목 선택 -> 개설교과목 pk 전송
    */
    private void readLecturePlanner(Protocol recvPt) throws IOException
    {
        // 받은 packet body에 담긴 data -> lecture_pk
        long lecture_pk = (long) recvPt.getBody();
        // pk로 강의계획서 조회 기능 수행

        // if (강의계획서 등록O)
        Object sndData = null;
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setBody(sndData);
        // else (강의계획서 등록X)
        // sendPt.setCode(Protocol.T2_CODE_FAIL);

    }

    /*
    < 교수 강의시간표 조회 >
     */
    private void readProfTimetable() throws IOException
    {
        // 강의시간표 조회 기능 수행
        Object sndData = null;
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setBody(sndData);
        //시간표 조회 실패 - 담당 교과목 없음
        //sendPt.setCode(Protocol.T2_CODE_FAIL);
    }

    /*
     < 개설교과목 수강신청 학생목록 조회 >
     클라이언트에서 해당 교수의 개설 교과목 조회 -> 교수가 개설교과목 선택 -> 개설교과목 pk전송
     */
    private void readLectureStudList(Protocol recvPt) throws IOException
    {
        long pk = (long) recvPt.getBody();
        // 학생 목록 조회 기능 수행
        Object sndData = null;
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setBody(sndData);
        // 조회 실패
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
    }

    /*
     < 개인정보(전화번호) 수정 및 비밀번호 수정 >
     */
    private void updateAccount(Protocol recvPt) throws IOException
    {
        /*
         받은 body 형식
         - 전화번호 수정인 경우
            : 1 010-0000-0000
         - 비밀번호 수정인 경우
            : 2 currentPassword newPassword
         */

        Object data = recvPt.getBody();

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
     < 강의계획서 수정 >
     클라이언트에서 해당 교수의 개설교과목 목록 조회
     -> 개설교과목 선택 -> (생성된 강의계획서가 없다면 강의계획서 변경 요청 불가능)
     개설교과목 pk및 강의계획서 내용 전송
     */
    private void updatePlanner(Protocol recvPt) throws IOException
    {
        Object data = recvPt.getBody();
        // 강의계획서 수정 기능 수행
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);

        // 수정 실패
        sendPt.setCode(Protocol.T2_CODE_FAIL);

    }
}
