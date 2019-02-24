CREATE TABLE `gary_faq` (
  `id` SMALLINT NOT NULL AUTO_INCREMENT,
  `author` BIGINT NULL,
  `key` TEXT NULL,
  `value` TEXT NULL,
  PRIMARY KEY (`id`)
) COLLATE = 'utf8_general_ci' ENGINE = InnoDB;

-

CREATE TABLE `gary_messages` (
	`id` MEDIUMINT NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT NULL,
	`message_id` BIGINT NULL,
	`previous_message` TEXT NULL,
	`current_message` TEXT NULL,
	PRIMARY KEY (`id`)
) COLLATE='utf8_general_ci' ENGINE=InnoDB;

-

CREATE TABLE `gary_stats` (
  `id` SMALLINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NULL,
  `wins` SMALLINT NULL,
  PRIMARY KEY (`id`)
) COLLATE = 'utf8_general_ci' ENGINE = InnoDB;

-

CREATE TABLE `gary_settings` (
  `id` SMALLINT(6) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NULL,
  `global_announcements` TINYINT NULL,
  `plugin_updates` TINYINT NULL,
  `papi_git` TINYINT NULL,
  `clip_ping` TINYINT NULL,
  `chat_reaction` TINYINT NULL,
  PRIMARY KEY (`id`)
) COLLATE = 'utf8_general_ci' ENGINE = InnoDB;

-

CREATE TABLE `gary_warnings` (
  `id` SMALLINT(6) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NULL,
  `strikes` TINYINT NULL,
  PRIMARY KEY (`id`)
) COLLATE = 'utf8_general_ci' ENGINE = InnoDB;

-

CREATE TABLE `gary_giveaways` (
	`id` TINYINT NOT NULL AUTO_INCREMENT,
	`message_id` BIGINT NULL,
	`author_id` BIGINT NULL,
	`prize` TEXT NULL,
	`emote` TEXT NULL,
	`time` TEXT NULL,
	`time_left` BIGINT NULL,
	PRIMARY KEY (`id`)
) COLLATE='utf8_general_ci' ENGINE=InnoDB;

-

CREATE TABLE `gary_bumps` (
	`id` SMALLINT(6) NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT NULL,
	`message` TEXT NULL,
	PRIMARY KEY (`id`)
) COLLATE='utf8_general_ci' ENGINE=InnoDB;

-

CREATE TABLE `gary_levels` (
	`id` SMALLINT(6) NOT NULL AUTO_INCREMENT,
	`user_id` TEXT NULL,
	`xp` MEDIUMINT(9) NULL,
	`lvl` SMALLINT(6) NULL,
	PRIMARY KEY (`id`)
) COLLATE='utf8_general_ci' ENGINE=InnoDB;