INSERT INTO template(name, profession, sub_profession, network_type, scenario_category, risk_level, operation_level, template_type, version_no, content_json, online_flag, author, created_at, updated_at)
VALUES
('核心网夜间割接模板', '无线', '核心网', 'MME', '夜间割接', 4, 3, 'SHARED', 1, '{"summary":"标准割接流程"}', true, 'admin', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('传输设备巡检模板', '传输', 'OTN', 'OTN设备', '例行巡检', 2, 2, 'PERSONAL', 1, '{"summary":"巡检流程"}', true, 'userA', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
