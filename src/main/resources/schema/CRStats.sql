CREATE TABLE IF NOT EXISTS `gary_cr_stats` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT,
  `discord_id` bigint(20) NOT NULL,
  `wins` mediumint(9) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;