# Notes

# Assumptions

* When creating a user `first name`, `last name` and `username` are compulsory
* After a user is created, her username can't be changed
    * Any attempt to update username will be silently ignored
* When creating a task `name`, `description` & `date_time` are compulsory
* After a task is created, we can't change its date time
    * Any attempt to update a task date time will be silently ignored
* Task deletion is a soft delete i.e. task is mark as non-active, & record not deleted
* Job to expire pending task with `date time` in the past run after a delay of 1 min
    * Since we can't insert task with a time in the past, we use the seed migration to test this
        * You can add entries to the file `/tasks/src/main/resources/db/seed/V1_2018092313461537703199__task_table_add_insert_sample_records.sql`
        * You can create a separate migration file - See (./README.md > `Seed and Migration`) for how to

# TODOs

* Update libraries used (low priority)
* Move frequency of task expiry job run into an environment variable i.e. `application.yaml` (medium priority)
* Localise error messages (out of scope)
* Improve failed validation error messages (medium priority)
* Paginate listing all users (medium priority)

* Obfuscate ids in url (out of scope)
* Optimise task expiry job run for high volume (out of scope)

* Use `@MockBean` in controller unit test (low priority)