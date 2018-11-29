CREATE TABLE `gary_faq` (
  `id` SMALLINT(6) NOT NULL AUTO_INCREMENT,
  `author` BIGINT(20) NULL DEFAULT NULL,
  `key` TEXT NULL,
  `value` TEXT NULL,
  PRIMARY KEY (`id`)
) COLLATE = 'utf8_general_ci' ENGINE = MyISAM;

-

CREATE TABLE `gary_messages` (
  `id` MEDIUMINT(9) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL DEFAULT,
  `message_id` BIGINT(20) NULL DEFAULT NULL,
  `previous_message` TEXT NULL,
  `current_message` TEXT NULL,
  PRIMARY KEY (`id`)
) COLLATE = 'utf8_general_ci' ENGINE = MyISAM;

-

CREATE TABLE `gary_stats` (
  `id` SMALLINT(6) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NULL DEFAULT NULL,
  `wins` SMALLINT(6) NULL DEFAULT NULL,
  `o` SMALLINT(6) NULL DEFAULT NULL,
  `bro` SMALLINT(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) COLLATE = 'utf8_general_ci' ENGINE = MyISAM;

-

CREATE TABLE `gary_settings` (
  `id` SMALLINT(6) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NULL DEFAULT NULL,
  `global_announcements` TINYINT NULL DEFAULT NULL,
  `plugin_updates` TINYINT NULL DEFAULT NULL,
  `papi_git` TINYINT NULL DEFAULT NULL,
  `clip_ping` TINYINT NULL DEFAULT NULL,
  `chat_reaction` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) COLLATE = 'utf8_general_ci' ENGINE = MyISAM;