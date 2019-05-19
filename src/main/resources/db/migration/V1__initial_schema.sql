create table history_record (
       id binary not null,
        actual_query varchar,
        date_time timestamp,
        exercise_id varchar(255),
        passed boolean not null,
        reset boolean not null,
        was_error boolean not null,
        primary key (id)
);