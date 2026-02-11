CREATE TABLE IF NOT EXISTS template (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(128),
  profession VARCHAR(64),
  sub_profession VARCHAR(64),
  network_type VARCHAR(64),
  scenario_category VARCHAR(64),
  risk_level INT,
  operation_level INT,
  template_type VARCHAR(32),
  version_no INT,
  content_json CLOB,
  online_flag BOOLEAN,
  author VARCHAR(64),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS plan (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(128),
  profession VARCHAR(64),
  sub_profession VARCHAR(64),
  network_type VARCHAR(64),
  scenario_category VARCHAR(64),
  operation_content CLOB,
  operation_object VARCHAR(256),
  operation_scope CLOB,
  risk_level INT,
  operation_level INT,
  business_interrupted BOOLEAN,
  business_impact CLOB,
  status VARCHAR(32),
  template_version_id BIGINT,
  cutover_start TIMESTAMP,
  cutover_end TIMESTAMP,
  precheck_time TIMESTAMP,
  test_time TIMESTAMP,
  rollback_time TIMESTAMP,
  personnel_json CLOB,
  device_json CLOB,
  ai_suggestion CLOB,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS audit_record (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  plan_id BIGINT,
  rule_issues_json CLOB,
  ai_issues_json CLOB,
  passed BOOLEAN,
  operator VARCHAR(64),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);
