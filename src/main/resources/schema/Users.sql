CREATE TABLE IF NOT EXISTS `gary_users` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT,
  `discord_id` bigint(20) NOT NULL,
  `username` text NOT NULL,
  `discriminator` tinytext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;