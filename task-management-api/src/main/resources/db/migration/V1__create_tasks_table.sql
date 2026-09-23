create table tasks (
                       created_timestamp timestamp(6) not null,
                       last_modified_timestamp timestamp(6),
                       version bigint not null,
                       description varchar(255) not null,
                       status varchar(255) check ((status in ('PENDING','IN_PROGRESS','COMPLETED'))),
                       task_id varchar(255) not null,
                       title varchar(255) not null,
                       primary key (task_id)
);
