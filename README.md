# MVVM Templete
## 기본 구조
- MVVM 모델 차용으로 ViewModel과 LiveData를 이용해 REST API로 전달받은 데이터를 실시간으로 갱신 용이
- Retrofit2와 Hilt를 이용해 ViewModel에 Singleton 객체로 REST API의 Repository 주입
- Activity, ViewModel, RecyclerViewAdapter 공통 클래스 사용
- Foreground Service 설정으로 Background Service 유지

## 항목
- common
  - 공통적으로 사용하는 함수 및 클래스
  - base
    - Activity, ViewModel, RecyclerViewAdapter base 클래스
- di
  - Hilt를 이용한 Retrofit2 모듈 객체
- remote
  - REST API 통신을 위한 ApiInterface 및 Repository 정의
  - data
    - 통신 결과를 담는 data 클래스 모음
  - interceptor
    - REST API 통신 시 공통적으로 들어가는 헤더값 삽입
- service
  - Service 관련 클래스 모음
- ui
  - 화면과 관련된 클래스 관리
  
