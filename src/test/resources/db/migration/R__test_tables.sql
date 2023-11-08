CREATE TABLE IF NOT EXISTS ${flyway:defaultSchema}.test_data (
  id uuid PRIMARY KEY,
  event_id uuid NOT NULL,
  correlation_id varchar(256) NOT NULL
);
