DROP TABLE IF EXISTS notes;

CREATE TABLE notes (
  note_id bigserial,
  note_creator_id varchar(50) NULL,
  note_creator_external_id varchar(50) NULL,
  note_type varchar(20) NULL, -- note_types: note (tied to entity), memo (recipients), announcement (company-wide)
  note_description varchar(100) NULL,
  note_value text NULL,
  note_archived boolean DEFAULT false,
  linked_to_entity_type varchar(50) NULL,
  linked_to_entity_id varchar(50) NULL,
  recipients varchar(50)[],
  note_create_date TIMESTAMP WITH TIME ZONE DEFAULT now(),
  note_last_update_date TIMESTAMP WITH TIME ZONE DEFAULT now(),
  PRIMARY KEY (note_id)
);

DROP TABLE IF EXISTS notes_audit;

CREATE TABLE notes_audit (
  note_id bigint,
  note_viewer_id varchar(50) NULL,
  note_first_retrieved_date TIMESTAMP WITH TIME ZONE DEFAULT now(),
  note_last_retrieved_date TIMESTAMP WITH TIME ZONE DEFAULT now(),
  note_acknowledged boolean DEFAULT false,
  note_pinned boolean DEFAULT false,
  PRIMARY KEY (note_id, note_viewer_id)
);

--
-- SELECT * FROM notes n
-- LEFT JOIN notes_audit na
-- ON	n.note_id = na.note_id
-- WHERE
-- (
--     n.note_type = 'announcement'
-- 		OR
--     (
--         n.note_type = 'memo'
--             AND
--         (  n.note_creator_id = '1' OR '1'=ANY(n.recipients) )
-- 	)
-- )
