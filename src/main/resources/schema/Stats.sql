CREATE TABLE IF NOT EXISTS `gary_stats` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT,
  `discord_id` bigint(20) NOT NULL,
  `win` mediumint(9) NOT NULL,
  `o` mediumint(9) NOT NULL,
  `bro` mediumint(9) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;