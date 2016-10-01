# my-web-server
[![Build Status](https://travis-ci.org/iamkyu/my-web-server.svg?branch=master)](https://travis-ci.org/iamkyu/my-web-server) [![codecov](https://codecov.io/gh/iamkyu/my-web-server/branch/master/graph/badge.svg)](https://codecov.io/gh/iamkyu/my-web-server)

```shell
$ mvn clean install
$ java -cp target/classes:target/dependency/* webserver.WebServer 8080 &
```

- [자바 웹 프로그래밍 Next Step 하나씩 벗겨가는 양파껍질 학습법(박재성 저. 로드북. 2016)](http://book.naver.com/bookdb/book_detail.nhn?bid=11037465) 교재를 따라 자바 웹 프로그래밍을 실습
- Forked from [https://github.com/slipp/web-application-server](https://github.com/slipp/web-application-server)



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
> HTTP에 관한 내용은 [HTTP 완벽 가이드(데이빗고울리 외 5명 저, 이응준 외 1명 역, 인사이트, 2014)](http://book.naver.com/bookdb/book_detail.nhn?bid=8509980)를 참고했다.

HTTP메시지는 단순한 줄 단위의 문자열이다.  각 줄은 캐리지 리턴과 개행 문자로(CRLF) 구성 된 두 글자의 줄바꿈 문자열으로 끝난다.

- 요청메시지: 클라이언트 -> 서버
- 응답메시지: 서버 -> 클라이언트



#### 메시지 문법

- 요청메시지

| <메서드> <요청URL> <버전> |
| :----------------- |
| <헤더>               |
| <엔티티 본문>           |

- 응답메시지

| <버전><상태코드><사유구절> |
| :--------------- |
| <헤더>             |
| <엔티티 본문>         |

HTTP 메시지는 크게 세 부분으로 나뉜다. 시작줄과 헤더는 아스키(ASCII)문자열이고, 본문은 어떤 종류의 데이터든 들어갈 수 있다(임의의 이진 데이터 또는 텍스트).

- **시작줄**: 요청이라면 무엇을 해야 하는지, 응답이라면 무슨 일이 나타났는지를 나타낸다.
- **헤더**: 시작줄 다음에 위차한 0개 이상의 헤더 필드. 각 헤더 필드는 구문 분석을 위해 쌍점(:)으로 구분하여 <이름>:<값> 형식으로 구성된다. 헤더는 빈줄로 끝난다.
- **본문**: 빈 줄 다음에는 어떤 종류의 데이터든 들어갈 수 있는 메시지 본문이 *필요에 따라* 올 수 있다.



#### 요청메시지 예시

| GET /index.html HTTP/1.1                 |
| :--------------------------------------- |
| Host: localhost:8080<br>Connection: keep-alive<br>Cache-Control: max-age=0<br>Upgrade-Insecure-Requests: 1<br>User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36<br>Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8<br>Accept-Encoding: gzip, deflate, sdch<br>Accept-Language: ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4<br>Cookie: Idea-5b5ff5ea=af3e68fa-3356-4dc3-8a2b-bb6b9d738798 |
| Hi! I'm a message (사실 GET 메서드의 메시지는 본문이 없다.) |

- HTTP/1.1에 관한 내용은 [W3C의 RFC2616문서](https://tools.ietf.org/html/rfc2616)를 참고한다.





## 요구사항 2 - get 방식으로 회원가입

> HTTP의 GET 메서드에 대한 내용은 [HTTP 완벽 가이드(데이빗고울리 외 5명 저, 이응준 외 1명 역, 인사이트, 2014)](http://book.naver.com/bookdb/book_detail.nhn?bid=8509980)와 [웹프로그래머를 위한 서블릿 컨테이너의 이해(최희탁 저, 한빛미디어, 2012)](http://book.naver.com/bookdb/book_detail.nhn?bid=7082377)를 참고했다.

GET은 가장 흔히 쓰이는 메서드로 주로 서버에게 리소스를 달라고 요청하기 위해 쓰인다. HTTP는 안전한 메서드라 불리는 메서드 집합을 정의하는데,  GET과 HEAD 메서드가 안전하다고 할 수 있다. 이는 이 메서드들을 사용하는 HTTP 요청의 결과로 서버에 어떤 작용도 없음을 의미한다(참고 [RFC2616#9.1.1 Safe Methhods](https://tools.ietf.org/html/rfc2616#section-9.1.1)). 작용이 없다는 것은 HTTP 요청의 결과로 인해 서버에서 일어나는 일은 아무것도 없다는 의미이다. 하지만 안전한 메서드가 서버에 작용을 유발하지 않는다는 보장은 없다. 이는 개발자의 구현 방식에 달렸다.

#### GET 요청 HTTP 메시지 예시

| GET /user/create?userId=myid&password=pass HTTP/1.1 |
| :--------------------------------------- |
| <헤더>                                     |

GET 메서드의 HTTP 메시지에는 본문이 없다. 따라서 URL에 매개변수를 추가해 서버에 부가 정보를 같이 전달한다. URL의 뒤에 '?이름1=값1&이름2=값2' 형태로 추가하며, 이 추가 정보를 쿼리스트링 이라고 한다. 서버로 넘어갔을 때는 HTTP 요청 시작행(Request-Line)의 URL 부분에 추가한 매개 변수가 전송 된다.

1. '?' 문자를 사용해 쿼리스트링을 파악한 후,
2. '&'로 끊어내 매개 변수를 나누고,
3. '='로 이름/값 쌍을 분리 한다.

위 절차를 통해 URL에 추가해 전송 받은 매개변수를 서버에서 획득할 수 있다.

- HTTP GET 메서드에 관한 내용은 [W3C의 RFC2616#9.3](https://tools.ietf.org/html/rfc2616#section-9.3)을 참고한다.




## 요구사항 3 - post 방식으로 회원가입

> HTTP의 POST 메서드에 대한 내용은 [HTTP 완벽 가이드(데이빗고울리 외 5명 저, 이응준 외 1명 역, 인사이트, 2014)](http://book.naver.com/bookdb/book_detail.nhn?bid=8509980)와 [웹프로그래머를 위한 서블릿 컨테이너의 이해(최희탁 저, 한빛미디어, 2012)](http://book.naver.com/bookdb/book_detail.nhn?bid=7082377)를 참고했다.

POST 메서드는 서버에 입력 데이터를 전송 하기 위해 설계되었다 (PUT은 서버에 있는 리소스에 데이터를 입력 하기 위해 사용). HTML Form 엘리먼트에서 POST 방식으로 서버에 전송하면 Content-Type이 application/x-www-form-urlencoded로 지정되고, 메시지 바디에 전달한 매개변수의 이름/값이 '='로 구분되며, 각 쌍은 &로 나뉘어 전달 된다. 매개변수의 이름/값은 URL인코딩이라는 특정한 인코딩 처리를 하는데, 이는 이름이나 값에 '=' 또는 '&'가 포함되어도 값을 구분하는데 착오가 없게 해준다. URL인코딩에 관해서는 [W3School](http://www.w3schools.com/tags/ref_urlencode.asp)을 참고하면 좋다.



#### POST 요청 HTTP 메시지 예시

| POST /user/create HTTP/1.1               |
| :--------------------------------------- |
| Host: localhost:8080<br/>Connection: keep-alive<br/>Content-Length: 53<br/>Cache-Control: max-age=0<br/>Origin: http://localhost:8080<br/>Upgrade-Insecure-Requests: 1<br/>User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36<br/>Content-Type: application/x-www-form-urlencoded<br/>Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8<br/>Referer: http://localhost:8080/user/form.html<br/>Accept-Encoding: gzip, deflate<br/>Accept-Language: ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4<br/>Cookie: Idea-5b5ff5ea=af3e68fa-3356-4dc3-8a2b-bb6b9d738798<br/> |
| <엔티티 본문 (매개변수가 포함 된다)>                   |

1. 헤더에서 Content-Length 를 파악한다.
2. 헤더와 엔티티 본문은 빈 줄로 구분 된다.
3. 해당 길이 만큼 본문을 읽는다.
4. '&'로 끊어내 매개 변수를 나누고,
5. '='로 이름/값 쌍을 분리 한다.

위 절차를 통해 POST 방식으로 전송 받은 매개변수를 서버에서 획득할 수 있다.

- HTTP POST 메서드에 관한 내용은 [W3C의 RFC2616#9.5](https://tools.ietf.org/html/rfc2616#section-9.5)을 참고한다.



## 요구사항 4 - redirect 방식으로 이동
요구사항 4번은 회원 가입을 완료 후, `/index.html` 로 리다이렉트 시키기 위하여 HTTP 302(Fount) 상태코드로 응답하도록 구현하는 내용이다. 나는 요구사항 3번을 구현하면서 Created를 의미하는 201 코드로 응답하고, `/user/success.html` 를 본문에 실어 보내도록 구현해 놨었다. 사실 두 방법 모두 마음에 들지는 않았다. 

- 302로 응답할 때 문제: 가입이 완료 되었으면 201 Created 로 코드로 응답하고 그 응답 코드를 확인한 후에 다시 후처리(리다이렉틀를 하던 무엇을 하던) 하는 것이 맞지 않을까? 또한, 리다이렉트에 관해 비슷한 듯 다른 301과 302 코드에 대해서도 알아보면 좋다.
- 201로 응답할 때 문제: 응답코드는 201로 하고 본문에 success 페이지 컨텐츠를 실어 보냈는데, 이 경우에 브라우저의 url도 변경되지 않고 올바른 URL-리소스 매칭이 아닌 것 같았다.

고민 끝에, 회원이 성공적으로 등록 되면 201 코드로 응답하고 별도의 컨텐츠를 실어 보내지는 않았다. 단, 클라이언트 단에서는 `ajax` 를 통해 비동기로 서버에 form 데이터를 전송하고 응답코드가 201인 걸 확인하면 `/user/success.html`  페이지로 리다이렉트로 하도록 처리하였다.

- HTTP 상태 코드에 관한 내용은 [W3C의 HTTP/1.1: Status Code Definitions](https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html) 을 참고한다.
- 웹 아키텍쳐에서  URI는 어떻게 설계하고 HTTP 메서드와 상태코드를 어떻게 사용할지 등을 정의한 REST 아키텍쳐에 관한 내용은 [Info Q: RESTful HTTP in practice](https://www.infoq.com/articles/designing-restful-http-apps-roth) 를 참고한다.



## 요구사항 5 - cookie
> 쿠키(Cookie) 대한 내용은 [HTTP 완벽 가이드(데이빗고울리 외 5명 저, 이응준 외 1명 역, 인사이트, 2014)](http://book.naver.com/bookdb/book_detail.nhn?bid=8509980)를 참고했다.

요구사항 5번에서는 로그인을 구현한다. 이 과정에서 쿠키를 사용한다. 쿠키(Cookie) 또는 세션(Session)은 클라이언트-서버 간에 정보 유지를 위해 사용하는데, 가장 큰 차이라면 쿠키는 클라이언트측에 보관하고 세션은 서버측에 보관한다는 차이가 있다. 자바EE 기술인 jsp나 Servlet 에서는 Session 객체를 기본적으로 제공한다. 

쿠키는 다시 크게 세션쿠키와 지속쿠키 두가지 타입으로 나뉜다.

- 세션쿠키: 사용자가 브라우저를 닫으면 삭제된다. 
- 지속쿠키: 삭제되지 않고 더 길게 유지 될 수 있다.

세션쿠키와 지속쿠키의 다른 점은 생명주기 뿐이다. `Discard` 파라미터가 설정되어 있거나  `Expires` 혹은 `Max-Age` 파라미터가 없으면 세션 쿠키 된다.



## 요구사항 6 - stylesheet 적용
stylesheet 를 html 페이지 내에 링크 시켰을 경우, 이 컨텐츠를 얻기 위해 다시 서버에 요청을 보낸다. 이 때, 해당 요청 (*.css)을 처리하는 로직을 추가하고, HTTP 응답을 할 때에는 Content-Type 헤더 값을 text/html이 아닌 text/css로 응답하여야 한다.



## 서버에 배포 후
HTTP에 기본에 대한 간단한 정리는 [https://www3.ntu.edu.sg/home/ehchua/programming/webprogramming/HTTP_Basics.html](https://www3.ntu.edu.sg/home/ehchua/programming/webprogramming/HTTP_Basics.html) 이 곳을 참고하면 좋다.



#### 자동화

나의 경우 위 과정을 수행하면서 코드에 새로운 기능을 더해 커밋을 하면 자동으로 빌드를 진행하고 다시 배포 하며, 메신저로 빌드 결과를 알려 주는 환경 구성을 실습하였다. 관련 내용은 블로그에 작성하였다.

- [Jenkins CI 삽질기](http://www.iamkyu.com/119)
- [Jenkins-Github-Slack 연동 설정](http://www.iamkyu.com/121)