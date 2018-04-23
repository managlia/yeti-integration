DROP TABLE IF EXISTS aos.team;
CREATE TABLE aos.team (
  team_id int(10) unsigned NOT NULL AUTO_INCREMENT,
  team_name varchar(50) DEFAULT NULL,
  team_description varchar(255) DEFAULT NULL,
  team_active tinyint(1) DEFAULT NULL,
  team_creator_id int(10) unsigned NOT NULL,
  team_create_date datetime NOT NULL,
  team_last_modified_date datetime DEFAULT CURRENT_TIMESTAMP,
  team_deactivation_date datetime DEFAULT NULL,
  PRIMARY KEY (team_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS contact_team;
CREATE TABLE contact_team (
  contact_team_id int(10) unsigned NOT NULL AUTO_INCREMENT,
  contact_id int(10) unsigned NOT NULL,
  team_id int(10) unsigned NOT NULL,
  contact_team_linkage_date datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (contact_team_id),
  KEY contact_fkt1 (contact_id),
  KEY team_fkc1 (team_id),
  CONSTRAINT contact_fkt1 FOREIGN KEY (contact_id) REFERENCES contact (contact_id),
  CONSTRAINT team_fkc1 FOREIGN KEY (team_id) REFERENCES team (team_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
