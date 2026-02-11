create table template (
    id bigint auto_increment primary key,
    name varchar(128) not null,
    major varchar(64) not null,
    sub_major varchar(64) not null,
    network_element_type varchar(64),
    risk_level int,
    operation_level int,
    template_type varchar(32),
    structure_type varchar(8),
    content_json clob,
    owner_id bigint,
    latest_version int,
    updated_at timestamp
);

create table template_version (
    id bigint auto_increment primary key,
    template_id bigint not null,
    version_no int not null,
    content_json clob,
    release_note varchar(255),
    created_at timestamp
);

create table plan (
    id bigint auto_increment primary key,
    name varchar(128) not null,
    major varchar(64),
    sub_major varchar(64),
    operation_content varchar(500),
    operation_target varchar(500),
    network_scope varchar(500),
    risk_level int,
    operation_level int,
    business_interrupted boolean,
    business_impact varchar(1000),
    status varchar(32),
    template_version_id bigint,
    owner_id bigint,
    cutover_start timestamp,
    cutover_end timestamp,
    precheck_time timestamp,
    verify_time timestamp,
    rollback_time timestamp,
    updated_at timestamp
);

create table plan_person (
    id bigint auto_increment primary key,
    plan_id bigint,
    role varchar(64),
    department varchar(64),
    name varchar(64),
    phone varchar(20)
);

create table plan_device (
    id bigint auto_increment primary key,
    plan_id bigint,
    device_name varchar(128),
    ip varchar(64)
);

create table audit_record (
    id bigint auto_increment primary key,
    plan_id bigint,
    version_no int,
    rule_result_json clob,
    ai_suggestion_json clob,
    conclusion varchar(16),
    created_at timestamp
);

create table dictionary_item (
    id bigint auto_increment primary key,
    dict_type varchar(64),
    dict_code varchar(64),
    dict_name varchar(128)
);

create table white_list_item (
    id bigint auto_increment primary key,
    scenario varchar(128),
    operation_target varchar(255),
    operation_content varchar(255),
    note varchar(255)
);
