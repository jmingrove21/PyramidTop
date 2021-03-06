# 배달ONE - 1인가구 배달주문 매칭 서비스

<div align=center>
  
[![HitCount](http://hits.dwyl.com/jmingrove21/PyramidTop.svg)](http://hits.dwyl.com/jmingrove21/PyramidTop)

</div>

2019년 봄학기 SW캡스톤디자인 프로젝트로 1인가구의 경제적 부담을 줄일 수 있는 서비스를 개발하였다.
본 서비스를 통해 1인가구는 기존 서비스보다 저렴한 서비스의 이용을, 가게 업주는 주문량의 증가, 배달업체 또한 배달량의 증가와 효율적인 동선으로 배달을 진행할 수 있기를 기대한다.

## 기존 유사 서비스와의 차별성
- 간결한 UI를 제공해 사용자가 쉽게 여러 서비스를 이용하고 사용할 수 있다.
- SKT Tmap Api와 TNavi를 이용하여 배달원은 경로검색시 본 서비스와 TNavi를 유동적으로 이용할 수 있다.
- 가게는 최소주문금액을 설정해 낮은 금액의 주문을 방지할 수 있고, 사용자 또한 주문시 일일이 가게에 문의하는 불편함을 최소화하였다.
- 가게검색시 가게정보와 메뉴정보, 영업시간 등 사용자는 한눈에 가게의 정보를 확인할 수 있다.
- 기존 유사 서비스는 가게 업주가 메뉴를 추가, 수정, 삭제할 시 서비스 제공자와 문의 후 변경하고, 이에 대한 반영이 즉각적이지 않은데 반해, 본 서비스를 이용할 시 가게 업주는 메뉴 추가, 변경, 삭제를 직접 진행할 수 있어 효율적이고 가게 운영에 부담을 주지 않는다.
- 사용자가 주문을 생성한 순간부터 주문 완료, 음식 준비, 배달 진행, 배달 완료를 사용자가 알람(notification)으로 확인할 수 있어 진행상황을 확인하는데 매우 편리하다.
- 가게 업주는 여러 주문의 진행사항(주문요청, 배달요청, 배달준비)을 한눈에 확인할 수 있는 UI를 제공받기 때문에 음식 판매에만 집중할 수 있다.

## 개발기술
- Android 27 (사용자, 배달원)
> gradle 3.4.1
>   circleimageview 2.2.0
>  okhttp 4.0.0
>  mockito 2.1
>  recyclerview / cardview 27.1.1

- PHP (Server)

- JS +Bootstrap (가게업주)
-----------------------------------
- MySQL
- AWS EC2
- Apache TomCat 8.5
-----------------------------------
- SKT TMap Api
- SKT TNavi Service
- BootPay Api (PG 연동 서비스)
-------------------------------------
- 이미지 Caching 적용
- 실시간 위치서비스 제공(GPS)	
- 배달 경로 실시간 업데이트
- 주문 진행 처리 notification


## 개발 분담 (가나다순)
|성명|main rule|sub rule|
|----------------|------------------------------|-----------------------------|
|김창희|사용자 서비스 개발(Android)|DB 구축, 일정관리|
|김하람|서버 개발|REST API 작성, Query 관리, 문서관리|
|이성훈|가게업주 서비스 개발(Web)|중간, 최종발표 시연, 문서관리|
|정민규|배달원 서비스 개발(Android)|프로젝트 기획, 관리|

## 개발 현황
### Version 1.0 - 2019.5.20
  - 사용자가 주문을 생성 가능
  - 사용자가 주문을 참가 가능
  - 현재 자신이 참가하고 있는 주문 목록 확인 가능
### Version 2.0 - 2019.6.7
  - 결제 시스템 도입 (계좌이체, 현장결제)
  - 과거 주문내역 조회
  - 주문 상세내용 확인 가능
  - 가게 검색기능 추가
  - 사용자 위치 정보 
### Version 2.1 - 2019.6.11
 - 배달 경로 오류 수정
 - GPS 오류 수정
 - UI 안정화
 - AGSC(Ajou Greative Software Concert) 발표용 배포

## 수상 실적
- Ajou Greative Software Concert 장려상 수상 (2019.6.11)
