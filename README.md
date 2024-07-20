# Whataplabs Task - PRODUCT
상품 API를 제공하는 애플리케이션입니다. ```PORT: 9090```
- [API 문서](http://localhost:9090/swagger-ui/index.html)

## 사용 기술
- Java 17
- SpringBoot 2.7.18
- Maven

## 프로젝트 구조
```
.
├── application
│   └── service                              # 비즈니스 로직을 처리하는 서비스
│       ├── ProductService.java
│       └── StockManager.java
├── domain                                   # 상품에 대한 도메인 영역
│   ├── OrderedProduct.java
│   ├── Product.java
│   ├── ProductRepository.java
│   ├── ProductWithPageInfo.java
│   ├── SortType.java
│   ├── StockRepository.java
│   └── exception                            # 도메인 관련 예외 정의
│       ├── ErrorType.java
│       ├── InsufficientStockException.java
│       ├── ProductBusinessException.java
│       └── ProductNotFoundException.java
├── infrastructure
│   ├── config                                # 설정 파일 
│   ├── handler
│   │   ├── CustomLockException.java
│   │   └── LockHandler.java
│   └── repository                            # repository 및 entity 
│       ├── ProductEntity.java
│       ├── ProductJpaRepository.java
│       ├── ProductRepositoryImpl.java
│       ├── StockJpaRepository.java
│       └── StockRepositoryImpl.java
└── interfaces
    └── web                                   # controller 및 요청, 응답 객체
        ├── ProductRestController.java
        ├── StockRestController.java
        ├── exception
        │   ├── ErrorResponse.java
        │   └── GlobalExceptionHandler.java
        ├── request
        └── response
```
## 동시성 이슈 처리
상품 재고(amount)는 추가, 차감 기능이 있는데 여러 요청이 동시에 발생하여 데이터가 정확하지 않는 경우가 발생할 수 있습니다.
이를 해결하기 위해 다음과 같은 처리를 시도하였습니다.

### @version을 이용한 낙관적 락 적용 (1차)
- 먼저 상품 테이블 자체에 version 정보를 넣어 두고 조회 시점의 version과 수정 시점의 version이 동일한지 확인하는 방식으로 처리하였습니다.
- ```@Version``` 애노테이션을 사용하여 낙관적 락을 구현하였습니다.
- 이 경우 동시에 요청이 오면 처음 요청에 대해서만 처리가 되고 나머지는 모두 예외가 발생하는 방식으로 처리가 되지 않도록 합니다.
- 장점
  - 조회 시점부터 락을 거는 것이 아니라 데드락 위험이 없고
  - 같은 버전이 아닌 경우 수정 작업이 처리 되지 않아 데이터 정합성을 보장할 수 있습니다.
- 단점
  - DB에 수정 작업을 요청할 때 확인하는 방식으로 요청이 많은 경우 DB에 부하가 집중될 수 있고
  - 재고가 남아 있음에도 단순 동일 요청으로 인해 요청 자체가 실패합니다. (수동 재시도 빈도 증가)

### Redisson을 통한 분산락 적용 (2차)
- DB에 요청을 전달하기 전에 서비스 영역에서 먼저 redis lock을 사용하여 동시에 온 요청을 처리할 수 있도록 수정하였습니다.
- 동시에 요청이 오는 경우 lock을 선점한 요청이 먼저 DB에 접근해 요청을 처리하고 lock을 선점하지 못한 경우 일정 시간 동안 대기 후 lock을 선점하여 요청을 처리하는 방식입니다.
- 장점
  - DB에 요청을 보내기 전 먼저 동시에 온 요청들을 처리하기 때문에 DB에 부하가 집중되는 것을 줄일 수 있습니다.
  - lock 대기 시간 내에 lock을 선점하게 되면 요청이 처리될 수 있기 때문에 재고가 유효하다면 동시에 온 요청 모두 처리가 가능합니다. (수동 재시도 빈도 감소)
  - 처리 이후 lock을 해제하기 때문에 데드락 위험이 없습니다.
- Redisson을 사용한 이유
  - 일반 Redis의 Lock은 SpinLock을 사용하는데 이는 lock을 획득하지 못했을 때 일정 시간 대기 후 재시도를 반복하여 CPU 사용량이 증가할 수 있고, TTL 시간 연장 등의 기능을 수동으로 처리해야 합니다.
  - Redisson은 pub/sub 방식으로 lock 해제 시 구독 중인 클라이언트에게 lock 획득을 시도해도 된다고 알려주는 방식으로 동작해 부하가 생기지 않고 자동 TTL 연장 및 성능 최적화, 고가용성 등을 제공합니다.

## 도메인 정의
- 상품 (Product)
  + 상품은 ```상품 아이디(id)```, ```상품명(name)```, ```가격(price)```, ```수량(amount)```, ```등록 일시(createdAt)```, ```수정 일시(lastModifiedAt)```를 가집니다.
  + 상품 아이디는 자동 증가하는 임의의 숫자이며 상품을 식별하는 키입니다.
  + 상품명, 가격, 수량은 수정이 가능합니다.
  + 상품명은 빈 값일 수 없고 가격 및 수량은 빈 값이거나 음수일 수 없습니다.
- 제외된 내용
  + 상품을 등록, 수정하는 사람(관리자 또는 회원)에 대한 내용은 제외하였습니다.

## 주요 API
- 상품 단건 조회
  - ```GET /products/{id}```: id를 이용하여 상품의 id, name, price, amount를 조회합니다.
  - response
    ```json
    {
      "code": "OK",
      "message": "OK",
      "data": {
        "id": 101,
        "name": "item1",
        "price": 10000.00,
        "amount": 5
      }
    }
    ```
- 상품 목록 조회
  - ```GET /products?page=0&size=3&sort=price```: page, size, sort 정보를 입력 받아 페이징 된 상품 정보를 조회합니다.
  - response
    ```json
    {
      "code": "OK",
      "message": "OK",
      "data": {
        "totalPages": 2,
        "page": 0,
        "size": 3,
        "products": [
          {
            "id": 105,
            "name": "item5",
            "price": 100000.00,
            "amount": 17
          },
          {
            "id": 104,
            "name": "item4",
            "price": 50000.00,
            "amount": 20
          },
          {
            "id": 102,
            "name": "item2",
            "price": 20000.00,
            "amount": 3
          }
        ]
      }
    }
    ```
- 상품 등록
  - ```POST /products```: name, price, amount 정보를 입력 받아 상품을 등록합니다.
  - request
    ```json
    {
      "name": "item 1234",
      "price": 12000,
      "amount": 30
    }
    ```
  - response
    ```json
    {
      "code": "OK",
      "message": "OK",
      "data": {
        "id": 1,
        "name": "item 1234",
        "price": 12000,
        "amount": 30
      }
    }
    ```
- 상품 수정
  - ```PUT /products/{id}```: name, price, amount 정보를 입력 받아 id에 해당하는 상품을 수정합니다.
  - request
    ```json
    {
      "name": "item 1234",
      "price": 32000,
      "amount": 7
    }
    ```
  - response
    ```json
    {
      "code": "OK",
      "message": "OK",
      "data": 105
    }
    ```
- 상품 삭제
  - ```DELETE /products/{id}```: id에 해당하는 상품을 삭제합니다.
  - response
    ```json
    {
      "code": "OK",
      "message": "OK",
      "data": 104
    }
    ```