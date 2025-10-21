CREATE TABLE page_config (
    config_key VARCHAR(100) NOT NULL,
    config_value VARCHAR(255) NOT NULL,
    use_yn CHAR(1) NOT NULL DEFAULT 'Y',
    PRIMARY KEY (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO page_config (config_key, config_value, use_yn) VALUES
('pageIndex', '1', 'Y'),
('pageSize', '10', 'Y');