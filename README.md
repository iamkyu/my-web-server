# my-web-server [![CircleCI](https://circleci.com/gh/iamkyu/my-web-server/tree/master.svg?style=svg)](https://circleci.com/gh/iamkyu/my-web-server/tree/master)

- [박재성 교수님](https://github.com/javajigi)의 자바 교육(2017.04.03 ~ 2017.06.01) - 웹서버 구현 실습
- Forked from [https://github.com/slipp/web-application-server](https://github.com/slipp/web-application-server)

### Reference
- [자바 웹 프로그래밍 Next Step 하나씩 벗겨가는 양파껍질 학습법(박재성 저. 로드북. 2016)](http://book.naver.com/bookdb/book_detail.nhn?bid=11037465)
- [HTTP 완벽 가이드(데이빗고울리 외 5명 저, 이응준 외 1명 역, 인사이트, 2014)](http://book.naver.com/bookdb/book_detail.nhn?bid=8509980)
- [웹프로그래머를 위한 서블릿 컨테이너의 이해(최희탁 저, 한빛미디어, 2012)](http://book.naver.com/bookdb/book_detail.nhn?bid=7082377)

# 각 요구사항별 학습 내용 정리
| 구분     | 내용                                       |
| :----- | ---------------------------------------- |
| 요구사항 1 | http://localhost:8080/index.html로 접속시 응답 |
| 요구사항 2 | get 방식으로 회원가입                            |
| 요구사항 3 | post 방식으로 회원가입                           |
| 요구사항 4 | redirect 방식으로 이동                         |
| 요구사항 5 | cookie                                   |
| 요구사항 6 | stylesheet 적용                            |
| 배포 후   | -                                        |

* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 



## 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
``` shell
# 메이븐으로 main 메소드를 실행하는 법 
$ mvn clean compile exec:java -Dexec.mainClass="webserver.WebServer"
$ curl http://localhost:8080/index.html
$ curl -d "userId=user&password=mypass&name=myname&email=myemail" http://localhost:8080/user/create
```

HTTP메시지는 단순한 줄 단위의 문자열으로, 클라이언트에서 서버로 보내는 요청(Requests) 메시지와 서버에서 클라이언트로 보내는 응답(Responses) 메시지로 나뉜다. 각 줄은 캐리지 리턴과 개행 문자로(CRLF) 구성 된 두 글자의 줄바꿈 문자열으로 끝난다.

- [RFC2616#4. HTTP Message](https://tools.ietf.org/html/rfc2616#section-4)

### 메시지 형식

> SP = 스페이스, CRLF = 줄바꿈, [] = 포함되거나 포함되지 않을 수 있음

- 요청메시지 형식

| Method SP Request-URI SP HTTP-Version CRLF |
| :--------------------------------------- |
| (general-header \| request-header \| entity-header) CRLF |
| [ message-body ]                         |

- 응답메시지 형식

| HTTP-Version SP Status-Code SP Reason-Phrase CRLF |
| :--------------------------------------- |
| (general-header \| request-header \| entity-header) CRLF |
| [ message-body ]                         |

HTTP 메시지는 크게 세 부분으로 나뉜다.

- 시작줄(or 상태줄) : 요청이라면 무엇을 해야 하는지, 응답이라면 무슨 일이 나타났는지를 나타낸다.
- 헤더: 시작줄 다음에 위치한 0..* 개의 필드. 각 헤더 필드는 구문 분석을 위해 쌍점(:)으로 구분하여 [이름 : 값] 형식으로 구성된다. 헤더는 빈줄로 끝난다.
- 본문: 빈 줄 다음에는 어떤 종류의 데이터든 들어갈 수 있는 메시지 본문이 **필요에 따라** 올 수 있다.


시작줄과 헤더는 아스키(ASCII)문자열이고, 본문은 어떤 종류의 데이터든 들어갈 수 있다 (임의의 이진 데이터 또는 텍스트).