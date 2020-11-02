create table IF NOT EXISTS PRODUCTS
(
	ID INTEGER not null
		primary key,
	EXTERNAL_ID INTEGER not null
		constraint UK_G14E0CK7NG1YITXTKYA2V9DED
			unique,
	NAME VARCHAR(255) not null,
	UID VARCHAR(255) not null,
    WITH_SIMILARITY INTEGER not null
);

create table IF NOT EXISTS TAGS
(
	ID INTEGER not null
		primary key,
	NAME VARCHAR(255) not null unique
);

create table IF NOT EXISTS SIMILAR_PRODUCTS
(
	ID INTEGER not null
		primary key,
	SIMILARITY DOUBLE not null,
	TOKEN VARCHAR(255) not null
);

create table IF NOT EXISTS PROD_TAG_REL
(
	PRODUCT_ID INTEGER not null,
	TAG_ID INTEGER not null,
	constraint FK1RSFP2LY1H2EM6B71V2F2H5CF
		foreign key (TAG_ID) references TAGS (ID),
	constraint FKPLABP13BXJK6IM7XOBWWLBVLS
		foreign key (PRODUCT_ID) references PRODUCTS (ID)
);

