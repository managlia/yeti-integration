DROP TABLE IF EXISTS aos.contact_phone;
DROP TABLE IF EXISTS aos.contact_phone_type;

DROP TABLE IF EXISTS aos.company_phone;
DROP TABLE IF EXISTS aos.company_phone_type;

CREATE TABLE aos.contact_phone_type (
  contact_phone_type_id varchar(2) NOT NULL,
  contact_phone_type_name varchar(50) NOT NULL,
  contact_phone_type_description varchar(255) DEFAULT NULL,
  contact_phone_type_create_date datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (contact_phone_type_id),
  UNIQUE KEY contact_phone_type_un (contact_phone_type_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO aos.contact_phone_type VALUES ('HQ','Headquarters','Headquarters','2017-10-03 16:39:32');
INSERT INTO aos.contact_phone_type VALUES ('OF','Office','Office','2017-10-28 18:01:24');
INSERT INTO aos.contact_phone_type VALUES ('BR','Branch','Branch','2017-10-03 16:39:32');
INSERT INTO aos.contact_phone_type VALUES ('MO','Mobile','Mobile','2017-10-03 16:39:32');
INSERT INTO aos.contact_phone_type VALUES ('FX','Fax','Fax','2017-10-03 16:39:32');
INSERT INTO aos.contact_phone_type VALUES ('OT','Other','Other','2017-10-03 16:39:32');

CREATE TABLE aos.contact_phone (
  contact_phone_id int(10) unsigned NOT NULL AUTO_INCREMENT,
  contact_id int(10) unsigned NOT NULL,
  contact_phone_type_id varchar(2) NOT NULL,
  contact_phone_value varchar(30) DEFAULT NULL,
  contact_phone_description varchar(100) DEFAULT NULL,
  contact_phone_create_date datetime DEFAULT NULL,
  contact_phone_last_modified_date datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (contact_phone_id),
  KEY contact_phonefk2 (contact_id),
  KEY contact_phone_contact_phone_type_fk (contact_phone_type_id),
  CONSTRAINT contact_phonefk2 FOREIGN KEY (contact_id) REFERENCES contact (contact_id),
  CONSTRAINT contact_phone_contact_phone_type_fk FOREIGN KEY (contact_phone_type_id) REFERENCES contact_phone_type (contact_phone_type_id)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;


CREATE TABLE aos.company_phone_type (
  company_phone_type_id varchar(2) NOT NULL,
  company_phone_type_name varchar(50) NOT NULL,
  company_phone_type_description varchar(255) DEFAULT NULL,
  company_phone_type_create_date datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (company_phone_type_id),
  UNIQUE KEY company_phone_type_un (company_phone_type_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO aos.company_phone_type VALUES ('OF','Office','Office','2017-10-28 18:01:24');
INSERT INTO aos.company_phone_type VALUES ('MO','Mobile','Mobile','2017-10-03 16:39:32');
INSERT INTO aos.company_phone_type VALUES ('HO','Home','Home','2017-10-03 16:39:32');
INSERT INTO aos.company_phone_type VALUES ('FX','Fax','Fax','2017-10-03 16:39:32');
INSERT INTO aos.company_phone_type VALUES ('OT','Other','Other','2017-10-03 16:39:32');

CREATE TABLE aos.company_phone (
  company_phone_id int(10) unsigned NOT NULL AUTO_INCREMENT,
  company_id int(10) unsigned NOT NULL,
  company_phone_type_id varchar(2) NOT NULL,
  company_phone_value varchar(30) DEFAULT NULL,
  company_phone_description varchar(100) DEFAULT NULL,
  company_phone_create_date datetime DEFAULT NULL,
  company_phone_last_modified_date datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (company_phone_id),
  KEY company_phonefk2 (company_id),
  KEY company_phone_company_phone_type_fk (company_phone_type_id),
  CONSTRAINT company_phonefk2 FOREIGN KEY (company_id) REFERENCES company (company_id),
  CONSTRAINT company_phone_company_phone_type_fk FOREIGN KEY (company_phone_type_id) REFERENCES company_phone_type (company_phone_type_id)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
