DROP TABLE IF EXISTS notes;

CREATE TABLE notes (
  note_id bigserial,
  note_creator_id varchar(50) NULL,
  note_creator_external_id varchar(50) NULL,
  note_description varchar(100) NULL,
  note_value text NULL,
  note_archived boolean DEFAULT false,
  linked_to_entity_type varchar(50) NULL,
  linked_to_entity_id varchar(50) NULL,
  note_create_date TIMESTAMP WITH TIME ZONE DEFAULT now(),
  note_last_update_date TIMESTAMP WITH TIME ZONE DEFAULT now(),
  PRIMARY KEY (note_id)
);


INSERT INTO public.notes(
	note_creator_id, note_creator_external_id, note_description, note_value)
	VALUES ('test', 'test', 'test', 'test');
