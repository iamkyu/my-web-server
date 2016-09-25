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
*

## 요구사항 4 - redirect 방식으로 이동
* ​

## 요구사항 5 - cookie
* ​

## 요구사항 6 - stylesheet 적용
* ​

## 서버에 배포 후
* ​
