DROP TABLE IF EXISTS files;

CREATE TABLE files (
  file_id bigserial,
  file_storage_path varchar(255) NOT NULL,
  file_original_name varchar(255) NOT NULL,
  file_type varchar(255) NOT NULL,
  file_size integer,
  file_uploader_id varchar(50) NULL,
  file_uploader_external_id varchar(50) NULL,
  linked_to_entity_type varchar(50) NULL,
  linked_to_entity_id varchar(50) NULL,
  file_storage_date TIMESTAMP WITH TIME ZONE DEFAULT now(),
  PRIMARY KEY (file_id)
);
