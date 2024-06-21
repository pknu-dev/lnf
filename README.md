# 부경대학교 분실물 게시판

인터넷DB응용 수업 팀프로젝트로 개발한 부경대학교 분실물 게시판입니다.

## 사용 기술

### 개발 환경

- Java 11
- JSP/Servlet (Jakarta EE)
- Tomcat 10
- IntelliJ IDEA

### 라이브러리

- OJDBC11
- Java Mail
- PrettyTime
- Bootstrap 5
- Axios

### 배포 환경

- AWS EC2
  - Ubuntu 24.04 x64
- AWS RDS
  - Oracle Database 11g
- AWS SES

## 디렉토리 구조

```
.
├── create_tables.sql
└── src
    └── main
        ├── java
        │   └── org
        │       └── pknudev
        │           ├── common
        │           ├── model
        │           ├── repository
        │           ├── service
        │           └── servlet
        ├── resources
        │   ├── application.properties
        │   └── application.template.properties
        └── webapp
            ├── WEB-INF
            │   ├── jsp
            │   │   └── include
            │   │       └── head.jsp
            │   ├── tags.tld
            │   └── web.xml
            └── static
```
