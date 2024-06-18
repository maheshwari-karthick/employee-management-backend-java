INSERT INTO "user"("id", "username", "password")
SELECT 1, 'ADMIN', '$2a$10$GfLnS.3kM/vAK8cMSDWGi.8817Ot.1bLFiARiakbHD1d8dlLS/nJ6'
WHERE NOT EXISTS (SELECT 1 FROM "user" WHERE "id" = 1);

INSERT INTO "users_roles"("user_id","roles")
SELECT 1,0 WHERE NOT EXISTS (SELECT 1 FROM "users_roles" WHERE "user_id" =1)
