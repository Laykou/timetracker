CREATE TABLE r_department_settings_level_1(
	id INT NOT NULL IDENTITY(1,1)  PRIMARY KEY,
	department_setting_id INT REFERENCES department_settings(id),
	level_id INT REFERENCES levels(id)
);

CREATE TABLE r_department_settings_level_2(
	id INT NOT NULL IDENTITY(1,1)  PRIMARY KEY,
	department_setting_id INT REFERENCES department_settings(id),
	level_id INT REFERENCES levels(id)
);

CREATE TABLE r_department_settings_level_3(
	id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	department_setting_id INT REFERENCES department_settings(id),
	level_id INT REFERENCES levels(id)
);

-- User has default levels (these will be set by startup)
AlTER TABLE users ADD default_level_1_id INT REFERENCES levels(id);
AlTER TABLE users ADD default_level_2_id INT REFERENCES levels(id);
AlTER TABLE users ADD default_level_3_id INT REFERENCES levels(id);

-- API requests table
CREATE TABLE api_requests(id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	request INT,
	request_get TEXT,
	request_post TEXT,
	requested_at DATETIME,
	user_id INT REFERENCES users(id),
	created_at DATETIME);
	
CREATE INDEX index_api_requests_request ON api_requests(user_id, request);

-- Permissions for departments
CREATE TABLE r_department_user_permissions(
	id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	department_id INT REFERENCES departments(id),
	user_id INT REFERENCES users(id),
);
CREATE INDEX index_r_department_user_permissions_department_id ON r_department_user_permissions(department_id);
CREATE INDEX index_r_department_user_permissions_user_id ON r_department_user_permissions(user_id);

-- Add from-to date range for department settings
ALTER TABLE department_settings ADD start date;
ALTER TABLE department_settings ADD finish date;
ALTER TABLE department_settings ADD allow_activity_editing BIT DEFAULT 0 NOT NULL;

-- What tracker is user using
ALTER TABLE users ADD tracker_version VARCHAR(255);

ALTER TABLE department_settings ADD creator_id INT REFERENCES users(id);
CREATE INDEX index_department_settings_creator_id ON department_settings(creator_id);

-- Tracked actions indexes
CREATE INDEX index_tracked_actions_department_setting_id ON tracked_actions(department_setting_id);

-------------------------------
-- User DASHBOARD
-------------------------------

-- API requests table
CREATE TABLE charts(id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	label VARCHAR(255),
	graph_type INT,
	start DATE,
	finish DATE,
	finished_only BIT DEFAULT NULL,
	confirmed_only BIT DEFAULT NULL,
	
	limit_type VARCHAR(200),
	limit_count INT,
	
	sort INT DEFAULT 0,
	user_id INT REFERENCES users(id),
	created_at DATETIME,
	updated_at DATETIME);
	
CREATE INDEX index_charts_user_id_sort ON charts(user_id, sort);

CREATE TABLE r_chart_department(
	id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	department_id INT REFERENCES departments(id),
	chart_id INT REFERENCES charts(id),
);

CREATE TABLE r_chart_department_setting(
	id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	department_setting_id INT REFERENCES department_settings(id),
	chart_id INT REFERENCES charts(id),
);

CREATE TABLE r_chart_level_1(
	id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	level_id INT REFERENCES levels(id),
	chart_id INT REFERENCES charts(id),
);

CREATE TABLE r_chart_level_2(
	id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	level_id INT REFERENCES levels(id),
	chart_id INT REFERENCES charts(id),
);

CREATE TABLE r_chart_level_3(
	id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	level_id INT REFERENCES levels(id),
	chart_id INT REFERENCES charts(id),
);

CREATE TABLE r_chart_main_activity(
	id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	activity_id INT REFERENCES activities(id),
	chart_id INT REFERENCES charts(id),
);

CREATE TABLE r_chart_subactivity(
	id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	activity_id INT REFERENCES activities(id),
	chart_id INT REFERENCES charts(id),
);

CREATE TABLE r_chart_final_activity(
	id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	activity_id INT REFERENCES activities(id),
	chart_id INT REFERENCES charts(id),
);

-- Add sorting to Department levels
ALTER TABLE r_department_settings_level_1 ADD sort_order INT;
CREATE INDEX index_r_department_settings_level_1_sort_order ON r_department_settings_level_1(sort_order);
ALTER TABLE r_department_settings_level_2 ADD sort_order INT;
CREATE INDEX index_r_department_settings_level_2_sort_order ON r_department_settings_level_2(sort_order);
ALTER TABLE r_department_settings_level_3 ADD sort_order INT;
CREATE INDEX index_r_department_settings_level_3_sort_order ON r_department_settings_level_3(sort_order);

-- Tracker versions
 CREATE TABLE tracker_versions(
	id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	version FLOAT,
	filename TEXT,
	is_current BIT DEFAULT 0,
	created_at datetime,
	updated_at datetime,
	creator_id INT references users(id)
);

ALTER TABLE tracked_actions ADD inactive_duration_add INT DEFAULT 0;
ALTER TABLE tracked_actions ADD master_action_id INT;