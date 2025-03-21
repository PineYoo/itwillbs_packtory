
# 2조 프로젝트 (Packtory_DE)

*작성일 : 25.03.20*

설명글이 길어지는 것을 줄이기 위해, 말 끝을 모두 짧게 작성 하였습니다.
미리 양해를 구하지 못해한점, 또 프로젝트 파일 업로드가 늦어진 점 사과드립니다.

\'/src/main/resources\' 에 application.yml 이 빠진채 공유되었음
조원님들 메일 계정으로 보냄
받은 내용을 위의 경로 혹은 \'application.properties_\'가 있는 곳에 가져다 놓으면 됨


## 1. application.yml

  1-1. 부트 시작 전 확인해야 할 것
  
```java
profiles:
  active: dev1

group:
  dev1 : local,common
  dev2 : classroom,common
  prod : itwill,common
```
> profiles.active : \'dev1\' 에 입력될 수 있는 것은 아래의 \'그룹(group)\'이며, [ \'dev1\', \'dev2\', \'prod\' ] 가 가능함
> >\'dev1\'은  구글 ERD 시트에 입력된 테이블이 생성되어 있음
> >\'dev2\'는 수업에 사용되었던 자기 컴퓨터 MySQL로 설정이 되어 있음(테이블 입력은 당연히 되어 있지 않음)
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

## 2. 샘플 코드 확인

부트 프로젝트가 무사히 실행 되었다면 브라우져에 \'http://localhost:port\' 만 입력 할 시 (메인 컨트롤러의 널스트링 매퍼 -> index.html 로 전달)해당 페이지에서 확인 가능함

\'아이템\'은 수업시간에 진행한 JPA-CRUD,
\'샘플\'은 Mybais로 만들어둔 CRUD,
\'부트스트랩 템플릿 페이지\'는 BS님께서 고생하신 Thymeleaf 레이아웃으로 번안(?)된 것 이렇게 세개로 이루어져 있음

## 3. 패키지 생성

3-1. 받은 프로젝트를 클릭 -> 우클릭 -> (맨 아래의) Properties 혹은 Alt+Enter
3-2. [Properties for packtory_de] 팝업 -> 창 오른쪽 \'Resource\' 화면 -> \'Location\' 에서 시선을 오른쪽 끝으로 이동 -> 아이콘 두개 중 왼쪽의 \'[Show in System Explorer]\' 클릭
3-3. 프로젝트가 보이는 경로에 탐색기가 실행 된 후 -> \'packtory_de(우리프로젝트명)\' 폴더를 클릭하여 들어감
3-4. \'src\\main\\java\\kr\\co\\itwillbs\\de\\\' 까지 계속 탐색!
3-5. \'templates\' 폴더를 복사해서 붙여 놓은 뒤 각자 맡은 파트 또는 메뉴로 이름을 바꿔놓음
3-6. 다시 STS로 돌아와 프로젝트 클릭 -> 우클릭 -> Refresh 혹은 F5
3-7. 자기만의 공간에서 클래스 파일들을 자유롭게 작성하여 개발 시작!🙊

## 99. 그 외 기타

### 99-1. Git Ignore
STS에서 application.yml 파일을 git-ignore에 등록하여 외부에 노출되지 않도록 조금만 노력 부탁드립니다. 시간이 도움을 줄 수 있을 때, 방법을 강구하여 업로드 해도 문제 없도록 개선하겠습니다.
그동안 구글링을 하여 조심! 부탁드립니다.🙈

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