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

## 요구사항 2 - get 방식으로 회원가입

GET은 가장 흔히 쓰이는 메서드로 주로 서버에게 리소스를 요청하기 위해 쓰인다. HTTP는 안전한 메서드라 불리는 메서드 집합을 정의하는데,  GET과 HEAD 메서드가 안전하다고 할 수 있다. 이는 이 메서드들을 사용하는 HTTP 요청의 결과로 서버에 어떤 작용도 없음을 의미한다(참고 [RFC2616#9.1.1 Safe Methhods](https://tools.ietf.org/html/rfc2616#section-9.1.1)). 작용이 없다는 것은 HTTP 요청의 결과로 인해 서버에서 일어나는 일은 아무것도 없다는 의미이다. 하지만 안전한 메서드가 서버에 작용을 유발하지 않는다는 보장은 없다. 이는 개발자의 구현 방식에 달렸다.

- HTTP GET 메서드에 관한 내용은 [RFC2616#9.3 GET](https://tools.ietf.org/html/rfc2616#section-9.3) 을 참고한다.

#### GET 요청 HTTP 메시지 예시

| GET /user/create?userId=myid&password=pass HTTP/1.1 |
| :--------------------------------------- |
| Host: localhost:8080<br>Connection: keep-alive<br>User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36<br>Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8 |

GET 메서드의 HTTP 메시지에는 본문이 없고, 매개변수를 URL에 "?" 문자 이후로 포함한다. 따라서 HTTP 메시지의 첫째줄(상태줄)의 HTTP메소드 다음으로 오는 요청URI를 분석(Parsing) 하여 매개변수를 얻을 수 있다.

1. URI에서 '?' 문자로 쿼리스트링을 획득 후
2. '&' 문자로 각각의 매개변수를 분리하고
3. '=' 문자로 매개변수를 이름/값으로 분리 한다.

위 과정를 통해 GET 방식으로 전달받은 매개변수를 획득할 수 있다.


## 요구사항 3 - post 방식으로 회원가입
POST 메서드는 서버에 입력 데이터를 전송 하기 위해 설계되었다. HTML Form 엘리먼트에서 POST 방식으로 서버에 전송하면 Content-Type이 application/x-www-form-urlencoded로 지정되고, 메시지 바디에 전달한 매개변수의 이름/값이 '=' 로 구분되며, 각 쌍은 &로 나뉘어 전달 된다. 매개변수의 이름/값은 URL인코딩 처리를 하는데, 이는 이름이나 값에 '=' 또는 '&'가 포함되어도 매개변수를 구분하는데 착오가 없게 한다. URL인코딩에 관해서는 [W3School](http://www.w3schools.com/tags/ref_urlencode.asp)을 참고하면 좋다.

- HTTP POST 메서드에 관한 내용은 [RFC2616#9.5](https://tools.ietf.org/html/rfc2616#section-9.5) 을 참고한다.

#### POST 요청 HTTP 메시지 예시

| POST /user/create HTTP/1.1               |
| :--------------------------------------- |
| Host: localhost:8080<br/>Connection: keep-alive<br/>Content-Length: 53<br/>Cache-Control: max-age=0<br/>Origin: http://localhost:8080<br/>Upgrade-Insecure-Requests: 1<br/>User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36<br/>Content-Type: application/x-www-form-urlencoded<br/>Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8<br/>Referer: http://localhost:8080/user/form.html<br/>Accept-Encoding: gzip, deflate<br/>Accept-Language: ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4<br/>Cookie: Idea-5b5ff5ea=af3e68fa-3356-4dc3-8a2b-bb6b9d738798<br/> |
| userId=user&password=mypass&name=myname&email=myemail |

 1. 헤더에서 Content-Length 를 파악함으로서 메시지 본문에 데이터가 존재 하는지, 데이터가 존재한다면 크기가 얼마나 되는지 알 수 있다.
 2. 헤더와 엔티티 본문은 빈 줄(CRLF) 로 구분 된다. Content-Length 만큼 본문을 읽는다.
 3. '&' 문자로 각각의 매개변수를 분리하고
 4. '=' 문자로 매개변수를 이름/값으로 분리 한다.

 위 과정을 통해 POST 방식으로 전송 받은 매개변수를 서버에서 획득할 수 있다.
 
 ## 요구사항 4 - redirect 방식으로 이동
 
 * ​
 
 ## 요구사항 5 - cookie
 쿠키(Cookie) 또는 세션(Session)은 클라이언트-서버 간에 정보 유지를 위해 사용하는데, 가장 큰 차이라면 쿠키는 클라이언트측에 보관하고 세션은 서버측에 보관한다는 차이가 있다. 자바EE 기술인 Servlet 에서는 Session 객체를 기본적으로 제공한다.
 ### ​쿠키(Cookie)
 - 무상태인 HTTP에서 상태를 보관하기 위해 고안한 방법.
 - HTTP 메시지에 Cookie 헤더와 값을 담아 보내고 서버 측에서 쿠키를 확인.
 
 #### 쿠키의 문제
 - 보안 취약. HTTP 메시지를 통해 쿠키를 주고 받으므로 쉽게 엿볼 수 있음.
 
 ### 세션(Session)의 등장
 HTTP 규격이 아니라 애플리케이션 서버가 자의적으로 구현한 기능. 쿠키의 보안 단점을 보완하기 위해 클라이언트의 상태를 서버에서 관리. 쿠키와 완전히 별개의 기술이 아니라, 쿠키를 통해 세션ID를 주고 받고, 이 세션ID를 통해 서버 측에서 데이터를 참조.
 
 #### 세션의 문제
 - 쿠키보다 상대적으로 안전성이 높긴 하지만, 세션 ID를 훔친다면 역시 문제.
 - 세션에 보존한 정보를 언제 해제해야 할 지 알 수 없음. 세션 정보는 서버의 메모리에 보존되므로 계속해서 세션이 늘어나면 메모리 부족이 발생.
 
 ## 요구사항 6 - stylesheet 적용
 HTTP 응답 메시지를 보낼 때 헤더에 Content-Type 이라는 필드를 포함한다. 이 컨텐트 타입은 MIME 타입으로 나타낸다.
 > MIME 타입이란 클라이언트에게 전송된 문서의 다양성을 알려주기 위한 메커니즘입니다: 웹에서 파일의 확장자는 별 의미가 없습니다. 그러므로, 각 문서와 함께 올바른 MIME 타입을 전송하도록, 서버가 정확히 설정하는 것이 중요합니다. 브라우저들은 리소스를 내려받았을 때 해야 할 기본 동작이 무엇인지를 결정하기 위해 대게 MIME 타입을 사용합니다. - [MDN: MIME 타입](https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types)