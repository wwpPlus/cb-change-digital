insert into dictionary_item(dict_type, dict_code, dict_name) values
('major', 'wireless', '无线'),
('major', 'ip', 'IP承载'),
('sub_major', 'ran', '接入网'),
('sub_major', 'core', '核心网'),
('operation_mode', '4a', '4A自动化'),
('operation_mode', 'manual', '人工操作');

insert into white_list_item(scenario, operation_target, operation_content, note) values
('核心网维护窗', '核心路由器A', 'BGP邻居重建', '历史验证通过');
