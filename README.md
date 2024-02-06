# 나작길 | 나만의 작은 총장님 만들기

## Skills
- <img src="https://img.shields.io/badge/Framework-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/3.1.4-515151?style=for-the-badge">
- <img src="https://img.shields.io/badge/Build-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"><img src="https://img.shields.io/badge/8.1.2-515151?style=for-the-badge">
- <img src="https://img.shields.io/badge/Language-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/java-%23ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"><img src="https://img.shields.io/badge/17-515151?style=for-the-badge">
- <img src="https://img.shields.io/badge/Project Encoding-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/UTF 8-EA2328?style=for-the-badge">
  
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"> <img src="https://img.shields.io/badge/Jwt-6DB33F?style=for-the-badge&logo=Jwt&logoColor=white">
<img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=JPA&logoColor=white">
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white">
<img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=for-the-badge&logo=Amazon AWS&logoColor=white">
<img src="https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=NGINX&logoColor=white">
<img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white">
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white">  

## Server Architecture
![image](https://github.com/najakgil/najakgil-server/assets/87763333/e2aaac34-2258-4e70-a1eb-3854bb3494e1)  

## Directory structure
```
├── controller
├── dto
├── entity
├── exception
├── global
├── mapper
├── repository
└── service
```

#### 단일 모듈 사용 이유
- 프로젝트가 크지 않아 모듈을 여러 개로 나누는 것보다 단일 모듈로 유지하는 것이 코드를 더 쉽게 관리할 수 있기 때문에 모듈이 적을수록 파일 간의 이동과 파일 관리에 드는 번거로움을 줄이고자 사용하였습니다.

- 필요한 기능을 한 곳에서 찾을 수 있어 개발 단계에서의 작업이 단순화 되기 때문에 하나의 모듈에서 모든 기능을 구현하고 테스트 하기 위해 사용하였습니다.

- 단일 모듈은 전체 코드베이스가 작고 읽기 쉽고 모든 기능이 한 곳에 모여 있어 버그를 수정하거나 새로운 기능을 추가할 때 발생할 수 있는 문제를 신속하게 해결하고자 하였습니다. 즉, 유지보수를 간소화 하기 위해 사용하였습니다.
