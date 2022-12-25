# xcape-business-sdk

## 프로젝트 구성

* xcape-admin :8000
    * 어드민에서 필요한 api 로직을 만듦
      ex) Theme 등록은 어드민에서만 사용하는 기능
* xcape-api :8900
    * 공홈 FE 에서 필요한 api 로직을 만듦 ex) 예약하기, 클라이언트 회원인증
* xcape-batch
    * 정해진 시간에 예약테이블을 참조해서 예약 리스트를 만들기
* xcape-core
    * DTO, VO, Entity
    * Util 이나 공통으로 사용하는 비즈니스 로직
    * getMerchantList, getThemeList, getReservationList
* portal (React)
    * xcape-api 를 통해서 데이터 접근