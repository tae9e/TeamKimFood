# :leaves: SpringBoot-TeamKim-YoriJori
스프링부트와 React.js를 이용한 레시피 공유 사이트
<br>
## :computer: 프로젝트 소개
1인가구의 증가! 배달료의 상승!으로 인해 나만 알고있는 꿀 레시피 공유 커뮤니티 입니다.
<br>

## :date: 개발기간
*2024.01.09. ~ 2024.02.02.

<br>

## :raising_hand: 멤버구성
김원성 - 레시피 등록 수정 삭제, 레시피 조회(회원-선호 태그 기준 조회 ,비회원-날짜순 DESC), 랭킹(조회수 기반, 추천수 기반), ERD설계, 관리자페이지<br>
김도원 - UI, header,footer, 댓글 추가 수정 삭제, 메인페이지, 마이페이지<br>
김태련 - 로그인, OAuth2.0 카카오 API, 멤버 가입 수정 탈퇴<br>
<br>

## :high_brightness: 개발환경
- JAVA 17
- IntelliJ Ultimate
- SpringBoot 3.2.0
- Node.js 20.10.0
- DB: MySQL 8
- REACT 18.2.0
- tomcat 9.0
- h2 2.2.224
- css, js, bootstrap
<br>

## :pushpin: 주요기능

회원가입
- 중복값 검사.(email은 unique로 설정)
- KAKAO API를 통한 회원가입(미구현)
<br>

로그인
- id찾기
- 로그인시 쿠키 및 세션 생성
<br>

메인페이지
- 회원 : 가입 후 선호 태그 설정을 통해 해당 태그와 연관된 레시피가 시간순(DESC)으로 정렬
- 비회원 : 모든 레시피 정렬
<br>

레시피 상세보기
- 본인이 작성한 레시피는 수정, 삭제 가능
- 로그인시 댓글작성 가능
- 추천하기 추천취소하기 가능
  <br>

랭킹
- 레시피 추천순 혹은 조회순으로 정렬 가능
- 홈페이지 이용 유저들의 레시피 추천수 총 합으로 순위 확인 가능
  <br>

