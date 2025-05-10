
# 2조 프로젝트 (Packtory_DE)

**작성일 : 25.03.20 / 최종수정일: 25.05.11**

설명글이 길어지는 것을 줄이기 위해, 말 끝을 모두 짧게 작성 하였습니다.  
미리 양해를 구하지 못해한점, 또 프로젝트 파일 업로드가 늦어진 점 사과드립니다.  

## 1. application.yml

  1-1. 부트 시작 전 확인해야 할 것
  
```java
profiles:
  active: dev

group:
  dev : local,common
  prod : itwill,common
```
> profiles.active : \'dev\' 에 입력될 수 있는 것은 아래의 \'그룹(group)\'이며, [ \'dev\', \'prod\' ] 가 가능함
> >\'dev\'은  구글 ERD 시트에 입력된 테이블이 생성되어 있음
> >\'prod\'는 나중에 프로젝트 결과물을 학원 서버에 업로드 할 때 사용하려고 만들어둔 틀이며, 이는 아직 사용하지 않길 바람
> >>향후 개인적으로 Oracle 디비에 접속이 가능 할 때, 이 yml을 수정/재 배포 하겠음

```java
server:
  port: ????
spring:
  config:
    activate:
      on-profile: ?!?!
```
> on-prifile: \'?!?! \'에 입력된 내용이 위의 그룹(K)값의 내용(V)이며, 바로 위의 server.port: \'????\'에 입력된 값이 부트 시작 후 브라우져에서 입력해야할 \'http://localhost:\' 다음의 포트 값을 나타냄

## 2. 패키지 생성

2-1. 받은 프로젝트를 클릭 -> 우클릭 -> (맨 아래의) Properties 혹은 Alt+Enter  
2-2. [Properties for packtory_de] 팝업 -> 창 오른쪽 \'Resource\' 화면 -> \'Location\' 에서 시선을 오른쪽 끝으로 이동 -> 아이콘 두개 중 왼쪽의 \'[Show in System Explorer]\' 클릭  
2-3. 프로젝트가 보이는 경로에 탐색기가 실행 된 후 -> \'packtory_de(우리프로젝트명)\' 폴더를 클릭하여 들어감  
2-4. \'src\\main\\java\\kr\\co\\itwillbs\\de\\\' 까지 계속 탐색!  
2-5. \'templates\' 폴더를 복사해서 붙여 놓은 뒤 각자 맡은 파트 또는 메뉴로 이름을 바꿔놓음  
2-6. 다시 STS로 돌아와 프로젝트 클릭 -> 우클릭 -> Refresh 혹은 F5  
2-7. 자기만의 공간에서 클래스 파일들을 자유롭게 작성하여 개발 시작!🙊  

## 99. 그 외 기타

### 99-1. Git Ignore
env.properties 가 있습니다. 이 파일은 application.yml 의 암호화 된 내용을 복호화 하기 위한 password 값이 들어 있기에 저에게 요청하여 받아주시기 바랍니다.  

### 99-2. spring-boot-devtools
```java
devtools:
  livereload:
    enabled: false
  restart:
    enabled: false
```
파일 저장 때마다 톰캣이 자동 재시작(Auto Deploy) 되는 것을 두려워해서 설정값을 false로 해두었습니다. 필요 하신 분들은 true로 변경하여 사용해주세요.

### 99-9. 그 외
작성되지 않았거나 그 외 궁금하신것은 언제든지 물어봐주시기 바랍니다.