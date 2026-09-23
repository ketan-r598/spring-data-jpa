create table task_audit_entries (
                                    created_timestamp timestamp(6) not null,
                                    last_modified_timestamp timestamp(6),
                                    description varchar(255),
                                    task_action varchar(255) check ((task_action in ('CREATED','UPDATED'))),
                                    task_audit_entry_id varchar(255) not null,
                                    task_id varchar(255), primary key (task_audit_entry_id)
);

alter table if exists task_audit_entries add constraint fk_task_audit_entries_task_id foreign key (task_id) references tasks on delete set null;
