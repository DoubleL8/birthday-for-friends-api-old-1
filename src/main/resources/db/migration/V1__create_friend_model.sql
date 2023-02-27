CREATE TABLE IF NOT EXISTS `user` (
    `id` INT(5) NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(50) NOT NULL,
    `password` VARCHAR(50) NOT NULL,
    `role` CHAR(4) NOT NULL DEFAULT 'USER',
    `name` VARCHAR(50) NOT NULL,
    `created_date` datetime DEFAULT NULL,
    `last_modified_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email_unique` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `friend` (
    `id` INT(5) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `birthdate` DATE NOT NULL,
    `gender` VARCHAR(2) DEFAULT NULL,
    `email` VARCHAR(50) DEFAULT NULL,
    `phone` VARCHAR(10) DEFAULT NULL,
    `note` TEXT DEFAULT NULL,
    `user_id` INT(5) NOT NULL,
    `created_by` varchar(50) DEFAULT NULL,
    `created_date` datetime DEFAULT NULL,
    `last_modified_by` varchar(50) DEFAULT NULL,
    `last_modified_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email_unique` (`email`),
    KEY `fk_friend__user_idx` (`user_id`),
    CONSTRAINT `fk_friend__user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;