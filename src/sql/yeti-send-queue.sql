DROP TABLE IF EXISTS send_queue;

CREATE TABLE send_queue (
  send_queue_id bigserial,
  send_queue_creator_id varchar(50) NULL,
  send_queue_creator_external_id varchar(50) NULL,
  send_queue_item_type varchar(20) NULL, -- ical for now
  send_queue_status varchar(20) NULL, -- send_pending, sent, canceled, cancellation_complete
  send_queue_payload bytea NULL,
  linked_to_entity_type varchar(50) NULL,
  linked_to_entity_id varchar(50) NULL,
  recipients varchar(50)[],
  send_queue_create_date TIMESTAMP WITH TIME ZONE DEFAULT now(), -- when it is set to send pending
  send_queue_transmission_date TIMESTAMP WITH TIME ZONE, -- when it is sent
  send_queue_cancel_date TIMESTAMP WITH TIME ZONE, -- when a cancellation is requested
  send_queue_cancel_transmission_date TIMESTAMP WITH TIME ZONE, -- when a cancellation is sent
  PRIMARY KEY (send_queue_id)
);
