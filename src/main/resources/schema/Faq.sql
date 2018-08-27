CREATE TABLE `gary_faq` (
	`id` TINYINT(4) NOT NULL AUTO_INCREMENT,
	`identifier` TEXT NULL,
	`message` TEXT NULL,
	`user_id` BIGINT(20) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=MyISAM
;