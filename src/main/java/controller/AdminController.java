package controller;

import infra.network.Protocol;

import java.io.InputStream;
import java.io.OutputStream;

public class AdminController implements Controller {
    private InputStream is;
    private OutputStream os;

    public AdminController(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    @Override
    public int handler(Protocol recvPt) throws Exception  {

        switch (recvPt.getCode()) {
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
        }
        return 0;
    }


    // 생성 요청 받았을 때 수행할 일
    private void createReq (Protocol recvPt) throws Exception
    {
        switch (recvPt.getEntity())
        {
            case Protocol.ENTITY_ACCOUNT:
                createAccount(recvPt);
                break;
            case Protocol.ENTITY_COURSE:
                createCourse(recvPt);
                break;
            case Protocol.ENTITY_LECTURE:
                createLecture(recvPt);
                break;
            case Protocol.ENTITY_REGIS_PERIOD:
                createRegisteringPeriod(recvPt);
                break;
            case Protocol.ENTITY_PLANNER_PERIOD:
                createPlannerPeriod(recvPt);
                break;
            default:
        }
    }

    // 조회 요청
    private void readReq (Protocol recvPt) throws Exception
    {
        switch (recvPt.getEntity())
        {
            case Protocol.ENTITY_ACCOUNT: // 계정정보 조회
                readAccount(recvPt);
                break;
            case Protocol.ENTITY_COURSE:  // 교과목 조회
                readCourse(recvPt);
                break;
            case Protocol.ENTITY_LECTURE:  // 개설교과목 조회
                readLecture(recvPt);
                break;
            default:
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
            case Protocol.ENTITY_COURSE:  // 교과목 변경
                updateCourse(recvPt);
                break;
            case Protocol.ENTITY_LECTURE:  // 개설교과목 변경
                updateLecture(recvPt);
                break;
            default:
        }
    }

    // 삭제 요청
    private void deleteReq (Protocol recvPt) throws Exception
    {
        switch (recvPt.getEntity())
        {
            case Protocol.ENTITY_COURSE:  // 교과목 삭제
                deleteCourse(recvPt);
                break;
            case Protocol.ENTITY_LECTURE:  // 개설교과목 삭제
                deleteLecture(recvPt);
                break;
            default:
        }
    }

    /*
     < 계정 생성 >
     */
    private void createAccount(Protocol recvPt) throws Exception
    {
        //Object data = recvPt.getObject();
        /*
         Account 생성하는 기능 수행
         */
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        // 계정 생성 실패 - 중복 계정 존재
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);

    }

    /*
    < 교과목 생성 >
     */
    private void createCourse(Protocol recvPt) throws Exception
    {
       // Object data = recvPt.getObject();
        /*
        교과목 생성하는 기능 수행
         */
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        sendPt.setCode(Protocol.T2_CODE_SUCCESS);  // 성공
        // 교과목 생성 실패 - 중복 교과목 존재
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);

    }


    /*
    < 개설 교과목 생성 >
     클라이언트
    - 관리자가 교과목 목록 조회 (readCourse)
    - 개설할 교과목 선택 & 개설교과목 정보 입력 -> course_pk 및 정보 전송
     */
    private void createLecture(Protocol recvPt) throws Exception
    {
        Object data = recvPt.getObject();
        /*
         lecture 생성하는 기능 수행
         */
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        sendPt.setCode(Protocol.T2_CODE_SUCCESS);  // 성공
        // 개설교과목 생성 실패 - ?
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);

    }

    /*
    < 수강신청기간 생성 >
     */
    private void createRegisteringPeriod(Protocol recvPt) throws Exception
    {
//        Object data = recvPt.getObject();
        /*
         수강신청 기간 생성하는 기능 수행
         */
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        // 수강신청기간 등록 실패 - 중복 기간 존재 (시작, 끝 날짜가 완전히 중복)
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);

    }

    /*
    < 강의계획서 입력 기간 설정 >
     */
    private void createPlannerPeriod(Protocol recvPt) throws Exception
    {
//        Object data = recvPt.getObject();
        /*
         강의계획서 입력 기간 생성하는 기능 수행
         */
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        sendPt.setCode(Protocol.T2_CODE_SUCCESS);  // 성공
        // 실패 - 중복 기간 존재 (시작, 끝 날짜가 완전히 중복)
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);

    }


    /*
    < 교수/학생 정보 조회 >
     */
    private void readAccount(Protocol recvPt) throws Exception
    {
//        Object data = recvPt.getObject();
        /*
        개인 정보 조회하는 기능 수행
         */
        Object sndData = null;
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setObject(sndData);

        //실패 - 존재하지 않는 code
        //sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);

    }

    /*
    < 교과목 조회 (전체) >
    - createLecture 할 때 필요
     */
    private void readCourse(Protocol recvPt) throws Exception
    {
//        Object data = recvPt.getObject();
        /*
        교과목 목록 조회하는 기능 수행
         */
        Object[] sndData = null;
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        if (sndData != null)
        {
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.setObjectArray(sndData);
        }
        else // 실패 - 교과목 없는 경우
            sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);

    }

    /*
    < 개설 교과목 정보 조회 >
     */
    private void readLecture(Protocol recvPt) throws Exception
    {
//        Object data = recvPt.getObject();  // 학년,학과,교수 option
        /*
        개설 교과목 정보 조회하는 기능 수행
         */
        //if 옵션 잘못됨
        //sendPt.setCode(Protocol.T2_CODE_FAIL);
        Object[] sndData = null;
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        if (sndData != null)
        {
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.setObjectArray(sndData);
        }
        else // 실패 - 개설 교과목 없는 경우
            sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);

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

//        Object data = recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        // if (전화번호 수정)
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        // else (비밀번호 수정)
        try {
            // 현재 비밀번호 일치하는지 확인
            // 비밀번호 수정 기능 수행
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) { // 현재 비밀번호 불일치
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);

        }
    }

    /*
     < 교과목 수정 >
     클라이언트
      - 교과목 조회 (readCourse)
      - 관리자가 교과목 선택 -> 교과목 pk 및 변경할 내용을 담은 패킷 전송
     */
    private void updateCourse(Protocol recvPt) throws Exception
    {
//        Object data = recvPt.getObject();
        // 교과목 정보 수정하는 기능 수행
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        // if (변경 성공)
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        // else (변경실패)
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);

    }

    /*
    < 개설교과목 수정 >
     클라이언트
     - 개설교과목 조회 (readLecture)
     - 관리자가 개설교과목 선택 -> 개설교과목 pk 및 변경할 내용을 담은 패킷 전송
    */
    private void updateLecture(Protocol recvPt) throws Exception
    {
//        Object data = recvPt.getObject();
        // 개설교과목 정보 수정하는 기능 수행
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        // if (변경 성공)
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        // else (변경실패)
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);

    }

    /*
    < 교과목 삭제 >
    클라이언트
    - 교과목 목록 조회 (readCourse)
    - 관리자가 교과목 선택 -> 교과목 pk 전송
    */
    private void deleteCourse(Protocol recvPt) throws Exception
    {
//        Object data = recvPt.getObject();
        // 교과목 삭제하는 기능 수행
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        // if (삭제 성공)
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        // else (삭제 실패)
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);

    }

    /*
     < 개설교과목 삭제 >
     클라이언트
     - 개설교과목 목록 조회 (readLecture)
     - 관리자가 개설교과목 선택 -> 교과목 pk 전송
    */
    private void deleteLecture(Protocol recvPt) throws Exception
    {
//        Object data = recvPt.getObject();
        // 개설교과목 삭제하는 기능 수행
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        // if (삭제 성공)
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        // else (삭제 실패)
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);

    }

}

