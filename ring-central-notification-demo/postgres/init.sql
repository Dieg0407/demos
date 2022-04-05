CREATE SCHEMA IF NOT EXISTS  dialer;
SET search_path TO dialer;

CREATE TABLE IF NOT EXISTS  "rc_call_event" (
"id" uuid,
"external_event_id" varchar(100),
"session_id" varchar(100),
"telephony_id" varchar(100),
"sequence" smallint,
"hook_extension_id" varchar(30),
"party_extension_id" varchar(30),
"status" varchar(30),
"reason" varchar(30),
"direction" varchar(30),
"event_time" timestamptz,
"party_id" varchar(50),
"to_phone_number" varchar(50),
"to_extension_id" varchar(50),
"to_name" varchar(50),
"to_device_id" varchar(50),
"from_phone_number" varchar(50),
"from_extension_id" varchar(50),
"from_name" varchar(50),
"from_device_id" varchar(50),
"missed_call" boolean,
"event" jsonb,
"created_date" timestamptz,
 PRIMARY KEY ("id")
);

create index "rc_call_event_telephony_id_idx" on "rc_call_event" ("telephony_id", "sequence" asc);
create index "rc_call_event_session_id_idx" on "rc_call_event" ("session_id", "sequence" asc);
create unique index "rc_call_event_external_id_idx" on "rc_call_event" ("external_event_id");
create index "rc_call_event_finder_tid_idx" on "rc_call_event" ("telephony_id","status","sequence" ASC);
create index "rc_call_event_finder_sid_idx" on "rc_call_event" ("session_id","status","sequence" ASC);

create table if not exists "call" (
"id" uuid,
"from_email" varchar(40),
"from_number" varchar(40),
"user_email" varchar(40),
"user_number" varchar(40),
"direction" varchar(30),
"duration" int,
"is_sent" boolean,
"rc_session_id" varchar(100),
"created_date" timestamptz,
PRIMARY KEY ("id")
);

