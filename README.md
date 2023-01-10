# [MutsaSNS](http://ec2-43-200-180-90.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/#/) 

![image](https://user-images.githubusercontent.com/68420044/211499056-2a4186dd-1dab-4899-9204-129353a608c0.png)

**Mutsa-SNS(멋사스네스)** : 회원가입, 로그인, 글쓰기, 피드, 댓글, 좋아요, 알림 기능이 있는 SNS API 


## 미션 요구사항 분석 & 체크리스트
- [x] ec2, Docker 설정
- [x] GitLab CI/CD 구현
- [x] Swagger API 문서 자동화
- [x] 회원가입, 로그인 구현
- [x] 회원 인증, 인가 필터 구현
- [x] Exception 처리
- [x] Post(게시글) CRUD 구현
- [x] 게시글에 댓글 CRUD 구현
- [x] 게시글에 좋아요 기능
- [x] 마이피드 기능
- [x] 알림 기능능
- [x] Controller, Service Test 코드


## 개발 환경

- 에디터 : Intellij Ultimate
- 개발 툴 : SpringBoot 2.7.5
- 자바 : JAVA 11
- 빌드 : Gradle 6.8
- 서버 : AWS EC2
- 배포 : Docker
- 데이터베이스 : MySql 8.0
- 필수 라이브러리 : SpringBoot Web, MySQL, Spring Data JPA, Lombok, Spring Security

## 시스템 아키텍쳐

![image](https://user-images.githubusercontent.com/68420044/211499356-8920f69b-f9f0-4f94-889f-ef2d91706d3d.png)

## 데이터베이스 아키텍쳐

![image](https://user-images.githubusercontent.com/68420044/211499486-c881f81e-b4d3-4b1a-8679-038756596f39.png)

## 기능 설명

### 1️⃣ **회원 인증·인가**

- 모든 회원은 회원가입을 통해 회원이 됩니다.
- 로그인을 하지 않으면 SNS 기능 중 피드를 보는 기능만 가능합니다.
- 로그인한 회원은 글쓰기, 수정, 댓글, 좋아요, 알림 기능이 가능합니다.

### 2️⃣ 글쓰기

- 포스트를 쓰려면 회원가입 후 로그인(Token받기)을 해야 합니다.
- 포스트의 길이는 총 300자 이상을 넘을 수 없습니다.
- 포스트의 한 페이지는 20개씩 보이고 총 몇 개의 페이지인지 표시가 됩니다.
- 로그인 하지 않아도 글 목록을 조회 할 수 있습니다.
- 수정 기능은 글을 쓴 회원만이 권한을 가집니다.
- 포스트의 삭제 기능은 글을 쓴 회원만이 권한을 가집니다.

### 3️⃣ 피드

- 로그인 한 회원은 자신이 작성한 글 목록을 볼 수 있습니다.

### 4️⃣ 댓글

- 댓글은 회원만이 권한을 가집니다.
- 글의 길이는 총 100자 이상을 넘을 수 없습니다.
- 회원은 다수의 댓글을 달 수 있습니다.

### 5️⃣ 좋아요

- 좋아요는 회원만 권한을 가집니다.
- 좋아요 기능은 취소가 가능합니다.

### 6️⃣ 알림

- 알림은 회원이 자신이 쓴 글에 대해 다른회원의 댓글을 올리거나 좋아요시 받는 기능입니다.
- 알림 목록에서 자신이 쓴 글에 달린 댓글과 좋아요를 확인할 수 있습니다.

## API 명세서

Base URL = `/api/v1`

| 구분  | Method | URL | 설명 |
| --- | --- | --- | --- |
| User | Post | /users/join | 회원가입 |
|  | Post | /users/login | 로그인  |
| Post | Post | /posts | 게시글 작성  |
|  | Get | /posts/{postId} | 게시글 조회 |
|  | Get | /posts | 게시글 리스트 조회 |
|  | Put | /posts/{postId} | 게시글 수정 |
|  | Delete  | /posts/{postId} | 게시글 삭제  |
|  | Get  | /posts/my | 특정 사용자 게시글 조회  |
| Comment  | Post | /posts/{postId}/comments | 댓글 작성 |
|  | Get | /posts/{postId}/comments/{commentId} | 댓글 조회  |
|  | Get | /posts/{postId}/comments | 댓글 리스트 조회  |
|  | Put | /posts/{postId}/comments/{commentId} | 댓글 수정  |
|  | Delete  | /posts/{postId}/comments/{commentId} | 댓글 삭제  |
| Like | Post | /posts/{postsId}/likes | 좋아요 누르기 |
|  | Get | /posts/{postsId}/likes | 좋아요 개수 조회 |
|  | Delete | /posts/{postsId}/likes | 좋아요 취소 |
| Alarm | Get | /alarms | 알림 조회 |

## Error 처리

ErrorCode에서 정의한 HttpStatus를 StatusCode로 다음과 같이 반환한다. 

```json
{
  "resultCode":"ERROR",
  "result":{
     "errorCode":"POST_NOT_FOUND",
     "message":"Post not founded"
  }
}
```

| ErrorCode | HTTP status | Description |
| --- | --- | --- |
| DUPLICATED_USER_NAME | 409 - CONFLICT | 중복된 userName으로 join하는 경우 |
| USERNAME_NOT_FOUND | 404 - NOT_FOUND | 회원가입 되지 않은 userName으로 login 하는 경우  |
| INVALID_PASSWORD | 401 - UNAUTHORIZED | login 시 비밀번호가 틀린 경우  |
| INVALID_TOKEN | 401 - UNAUTHORIZED | 잘못된 토큰으로 요청한 경우  |
| INVALID_PERMISSION | 401 - UNAUTHORIZED | 사용자가 권한이 없는 경우  |
| POST_NOT_FOUND | 404 - NOT_FOUND | 존재하지 않는 게시글을 요청한 경우  |
| COMMENT_NOT_FOUND | 404 - NOT_FOUND | 존재하지 않는 댓글을 요청한 경우  |
| DUPLICATED_LIKES | 409 - CONFLICT | 이미 좋아요를 누른 게시물에 좋아요을 누른 경우 |
| LIKES_NOT_FOUND | 404 - NOT_FOUND | 좋아요를 누르지 않은 게시물에 좋아요 취소를 요청한 경우  |
| DATABASE_ERROR | 500 - INTERNAL_SERVER_ERROR | 데이터베이스에서 에러가 난 경우  |

## 회고
- 개선하고 추가해야 할 부분이 아직 많다. 앞으로 개발을 할 때 계획을 세우고 테스트 코드와 문서를 작성해가면서 개발해야 겠다고 느꼈다.   
