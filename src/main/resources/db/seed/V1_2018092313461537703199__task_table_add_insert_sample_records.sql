insert into TASK (name, description, status, date_time, user_id)
 values ('Lucier task 1', '1st task for Lucier', 'pending', '2016-05-25 14:25:00', 1);

insert into TASK (name, description, status, date_time, user_id)
 values ('task 2', '2nd task', 'pending', dateadd('MINUTE', 2, now()), 1);

insert into TASK (name, description, status, date_time, user_id)
 values ('Task 3', 'Scheduled at every application start', 'pending', now(), 2);

insert into TASK (name, description, status, date_time, user_id)
 values ('Task 4', 'Scheduled 1 min after application start', 'pending', dateadd('MINUTE', 1, now()), 2);

insert into TASK (name, description, status, date_time, user_id)
 values ('Task 5', 'Scheduled 2 min after application start', 'pending', dateadd('MINUTE', 2, now()), 2);
