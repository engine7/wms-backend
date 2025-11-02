# wms-backend

1. Maven으로 바로 실행 (권장)
mvn spring-boot:run

2. 패키징 후 실행 (프로덕션/배포용)
mvn clean package -DskipTests
java -jar target/*.jar

3. IDE에서 실행
VS Code / Eclipse 등에서 egovframework.EgovBootApplication 클래스를 선택해 Run as -> Java Application / Spring Boot 실행.

실행 시 프로파일/포트 지정 예:
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
# 또는
java -jar target/*.jar --spring.profiles.active=local --server.port=8081