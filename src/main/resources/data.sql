
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID) VALUES ( 'CMMROT', '최상위 코드', '', 0, NOW(), 'SYSTEM' );

INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMBAK', '은행 코드', 'CMMROT', 1, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMPRD', '상품 코드', 'CMMROT', 1, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMARZ', '권한 코드', 'CMMROT', 1, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMUSR', '회원 구분 코드', 'CMMROT', 1, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMUSE', '사용 유무 코드', 'CMMROT', 1, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMAPR', '승인 유무 코드', 'CMMROT', 1, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMJON', '가입 구분 코드', 'CMMROT', 1, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMTRD', '거래 구분 코드', 'CMMROT', 1, NOW(), 'SYSTEM' );

INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMBAK001', '한국 은행', 'CMMBAK', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMBAK002', '산업 은행', 'CMMBAK', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMBAK003', '기업 은행', 'CMMBAK', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMBAK004', '국민 은행', 'CMMBAK', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMBAK005', '외환 은행', 'CMMBAK', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMBAK007', '수협중앙회', 'CMMBAK', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMBAK008', '수출입 은행', 'CMMBAK', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMBAK011', '농협 은행', 'CMMBAK', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMBAK012', '지역농축협', 'CMMBAK', 2, NOW(), 'SYSTEM' );

INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMPRD001', '예금', 'CMMPRD', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMPRD002', '적금', 'CMMPRD', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMPRD003', '입출금', 'CMMPRD', 2, NOW(), 'SYSTEM' );

INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'USER', '사용자', 'CMMARZ', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'ADMIN', '관리자', 'CMMARZ', 2, NOW(), 'SYSTEM' );

INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMUSR001', '개인', 'CMMUSR', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMUSR002', '법인', 'CMMUSR', 2, NOW(), 'SYSTEM' );

INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMUSE001', '사용', 'CMMUSE', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMUSE002', '미사용', 'CMMUSE', 2, NOW(), 'SYSTEM' );

INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMAPR001', '승인', 'CMMAPR', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMAPR002', '미승인', 'CMMAPR', 2, NOW(), 'SYSTEM' );

INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMJON001', '제한 없음', 'CMMJON', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMJON002', '개인', 'CMMJON', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMJON003', '법인', 'CMMJON', 2, NOW(), 'SYSTEM' );

INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMTRD001', '입금', 'CMMTRD', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMTRD002', '출금', 'CMMTRD', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMTRD003', '계좌 이체', 'CMMTRD', 2, NOW(), 'SYSTEM' );
INSERT INTO COMMON_CODE ( CODE, CODE_NAME, PARENT_CODE, LEVEL, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMTRD004', '타행 계좌 이체', 'CMMTRD', 2, NOW(), 'SYSTEM' );


INSERT INTO MEMBER (USER_ID, PASSWORD, NAME, ADDRESS, TEL_NO, BRITH_DATE, USE_STATUS, AUTHORIZATION_CODE) VALUES ( 'user', '$2a$10$kjA7F7u/s4sbmdPK3PlFE.iSgce01CPO7hCbLvy95nbtnzaUhyKWy', '사용자', '서울 금천구 디지털로9길 68', '00000000000', '20250101', 'CMMUSE001', 'USER' );
INSERT INTO MEMBER (USER_ID, PASSWORD, NAME, ADDRESS, TEL_NO, BRITH_DATE, USE_STATUS, AUTHORIZATION_CODE) VALUES ( 'admin', '$2a$10$WA224eDkARqK7aoYDgONmOuda.MnrPKW/kDmb1FNpoyNHSm0ubQIS', '관리자', '서울 금천구 디지털로9길 68', '01012341234', '20250102', 'CMMUSE001', 'ADMIN' );

INSERT INTO PRODUCT ( PRODUCT_CODE, NAME, DESCRIPTION, WITHDRAWAL_BOUNDS, TRANSFER_BOUNDS, INTEREST_RATE, JOIN_PERIOD, JOIN_TARGET, USE_STATUS, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMPRD001', '정기예금', '목돈을 일정기간 동안 약정금리로 예치하는 목돈굴리기 상품', 1000000, 3000000, 3.3, 36, 'CMMJON001', 'CMMUSE001', NOW(), 'SYSTEM' );
INSERT INTO PRODUCT ( PRODUCT_CODE, NAME, DESCRIPTION, WITHDRAWAL_BOUNDS, TRANSFER_BOUNDS, INTEREST_RATE, JOIN_PERIOD, JOIN_TARGET, USE_STATUS, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMPRD002', '자유적립적금', '가입기간내 금액 및 횟수에 제한 없이 자유롭게 불입하는 목돈 만들기 상품', 1000000, 3000000, 1.3, 36, 'CMMJON001', 'CMMUSE001', NOW(), 'SYSTEM' );
INSERT INTO PRODUCT ( PRODUCT_CODE, NAME, DESCRIPTION, WITHDRAWAL_BOUNDS, TRANSFER_BOUNDS, INTEREST_RATE, JOIN_PERIOD, JOIN_TARGET, USE_STATUS, REGISTRATION_DATE, REGISTRATION_ID ) VALUES ( 'CMMPRD003', '보통예금', '가입대상, 예치금액, 예치기간, 입출근 횟수에 상관 없이 자유롭게 거래 할 수 있는 입출금식 예금', 1000000, 3000000, 0.3, -1, 'CMMJON001', 'CMMUSE001', NOW(), 'SYSTEM' );


INSERT INTO ACCOUNT (INTEREST_RATE,BALANCE,REGISTRATION_DATE,TRANSFER_BOUNDS,UPDATE_DATE,WITHDRAWAL_BOUNDS,APPROVAL_STATUS,BANK_CODE,PRODUCT_CODE,USE_STATUS,ACCOUNT_NUMBER,REGISTRATION_ID,UPDATE_ID,USER_ID,ACCOUNT_PASSWORD,REJECT_REASON) VALUES
(0.3,10010,'2025-01-09 02:46:04.318',3000000,'2025-01-10 02:50:41.323',1000000,'CMMAPR001','CMMBAK001','CMMPRD003','CMMUSE001','001052307616','user',NULL,'user','$2a$10$YJ1akMqh9qYa95f4sdy4YOMyvKZfJLx41LMee/GCCBSnexRLIFwOK',NULL)
,(0.3,994860,'2025-01-10 01:24:51.973',3000000,'2025-01-10 03:21:09.297',1000000,'CMMAPR001','CMMBAK001','CMMPRD003','CMMUSE001','001820009681','user',NULL,'user','$2a$10$.IlW.6PkXxqinSJn2D9pcuYIeLQd7dEzsiU8ELmKdRRWxUWF9tZYO',NULL)
,(0.3,15970000,'2025-01-07 21:47:35.801',3000000,'2025-01-10 12:58:45.245',1000000,'CMMAPR001','CMMBAK002','CMMPRD003','CMMUSE001','002517613820','user',NULL,'user','$2a$10$ZDOsf5NvDtExrrGQy0m1r./dj56tqt3ntidu9BKe8Rh6h1E1bIuNC',NULL)
;

INSERT INTO TRADE (AMOUNT,BALANCE,FEE,REGISTRATION_DATE,BANK_CODE,TARGET_BANK_CODE,TRADE_TYPE,ACCOUNT_NUMBER,TARGET_ACCOUNT_NUMBER,REGISTRATION_ID,TRADE_RESULT) VALUES
(10,10,0,'2025-01-09 02:48:36.734','CMMBAK001',NULL,'CMMTRD001','001052307616',NULL,'user','200')
,(5,5,0,'2025-01-09 02:52:33.141','CMMBAK001',NULL,'CMMTRD004','001052307616',NULL,'user','200')
,(100,105,0,'2025-01-10 01:22:06.171','CMMBAK001',NULL,'CMMTRD001','001052307616',NULL,'user','200')
,(10,95,0,'2025-01-10 01:22:11.112','CMMBAK001',NULL,'CMMTRD004','001052307616',NULL,'user','200')
;
INSERT INTO TRADE (AMOUNT,BALANCE,FEE,REGISTRATION_DATE,BANK_CODE,TARGET_BANK_CODE,TRADE_TYPE,ACCOUNT_NUMBER,TARGET_ACCOUNT_NUMBER,REGISTRATION_ID,TRADE_RESULT) VALUES
(95,0,0,'2025-01-10 01:23:06.905','CMMBAK001',NULL,'CMMTRD002','001052307616',NULL,'user','200')
,(5000,5000,0,'2025-01-10 01:25:13.053','CMMBAK001',NULL,'CMMTRD001','001820009681',NULL,'user','200')
,(10,4990,0,'2025-01-10 01:25:44.349','CMMBAK001',NULL,'CMMTRD003','001820009681',NULL,'user','200')
,(10,10,0,'2025-01-10 01:25:44.360','CMMBAK001',NULL,'CMMTRD001','001052307616',NULL,'user','200')
,(1000000,1004990,0,'2025-01-10 02:50:27.892','CMMBAK001',NULL,'CMMTRD001','001820009681',NULL,'user','200')
,(10000,994890,100,'2025-01-10 02:50:41.285','CMMBAK001',NULL,'CMMTRD003','001820009681',NULL,'user','200')
,(10000,10010,0,'2025-01-10 02:50:41.318','CMMBAK001',NULL,'CMMTRD001','001052307616',NULL,'user','200')
,(10,994880,0,'2025-01-10 03:20:08.807','CMMBAK001',NULL,'CMMTRD002','001820009681',NULL,'user','200')
,(10,994870,0,'2025-01-10 03:20:40.645','CMMBAK001',NULL,'CMMTRD002','001820009681',NULL,'user','200')
;
INSERT INTO TRADE (AMOUNT,BALANCE,FEE,REGISTRATION_DATE,BANK_CODE,TARGET_BANK_CODE,TRADE_TYPE,ACCOUNT_NUMBER,TARGET_ACCOUNT_NUMBER,REGISTRATION_ID,TRADE_RESULT) VALUES
(10,994860,0,'2025-01-10 03:21:09.249','CMMBAK001',NULL,'CMMTRD002','001820009681',NULL,'user','200')
,(10000000,10000000,0,'2025-01-10 12:58:34.740','CMMBAK002',NULL,'CMMTRD001','002517613820',NULL,'user','200')
,(10000000,20000000,0,'2025-01-10 12:58:36.091','CMMBAK002',NULL,'CMMTRD001','002517613820',NULL,'user','200')
,(1000000,19000000,0,'2025-01-10 12:58:38.021','CMMBAK002',NULL,'CMMTRD002','002517613820',NULL,'user','200')
,(3000000,15970000,30000,'2025-01-10 12:58:45.239','CMMBAK002',NULL,'CMMTRD004','002517613820',NULL,'user','200')
;