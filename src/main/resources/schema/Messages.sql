CREATE TABLE IF NOT EXISTS `gary_messages` (
  `id` mediumint(9) NOT NULL AUTO_INCREMENT,
  `discord_id` bigint(20) DEFAULT NULL,
  `message_id` bigint(20) DEFAULT NULL,
  `previous_message` longtext,
  `current_message` longtext,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;