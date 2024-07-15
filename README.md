# Whataplabs Task - PRODUCT
상품 API를 제공하는 애플리케이션입니다. ```PORT: 9090```

## 사용 기술
- Java 17
- SpringBoot 2.7.18
- Maven

## 프로젝트 구조

## 도메인 정의
- 상품 (Product)
  + 상품은 ```상품 아이디(id)```, ```상품명(name)```, ```가격(price)```, ```수량(amount)```, ```등록 일시(createdAt)```, ```수정 일시(lastModifiedAt)```를 가집니다.
  + 상품 아이디는 자동 증가하는 임의의 숫자이며 상품을 식별하는 키입니다.
  + 상품명, 가격, 수량은 수정이 가능합니다.
- 제외된 내용
  + 상품을 등록, 수정하는 사람(관리자 또는 회원)에 대한 내용은 제외하였습니다.
  + 수량을 별도로 재고 관리하는 방식은 고려하지 않았습니다.

## 주요 API
- 상품 단건 조회
- 상품 목록 조회
- 상품 등록
- 상품 수정
- 상품 삭제