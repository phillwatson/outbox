CREATE SCHEMA IF NOT EXISTS ${flyway:defaultSchema};

CREATE TABLE IF NOT EXISTS ${flyway:defaultSchema}.events (
  id uuid PRIMARY KEY,
  correlation_id varchar(256) NOT NULL,
  retry_count smallint NOT NULL DEFAULT 0,
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  scheduled_for timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  topic varchar(256) NOT NULL,
  type text NULL,
  payload text NULL
);

CREATE TABLE IF NOT EXISTS ${flyway:defaultSchema}.message_hospital (
  id uuid PRIMARY KEY,
  event_id uuid NOT NULL,
  correlation_id varchar(256) NOT NULL,
  retry_count smallint NOT NULL DEFAULT 0,
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  topic varchar(256) NOT NULL,
  type text NULL,
  payload text NULL,
  reason text NOT NULL,
  cause text NOT NULL
);
