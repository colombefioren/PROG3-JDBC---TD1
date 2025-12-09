-- create the database
CREATE DATABASE product_management_db;

-- connect to the database
\c product_management_db

-- create a group role
CREATE ROLE product_manager_role NOLOGIN;

-- create a custom schema owned by the group role
CREATE SCHEMA product_management AUTHORIZATION product_manager_role;

-- create a user and assign it to the group role
CREATE USER product_manager_user WITH PASSWORD '123456';
GRANT product_manager_role TO product_manager_user;

-- grant database connection to the user
GRANT CONNECT ON DATABASE product_management_db TO product_manager_user;

-- grant schema privileges to the role
GRANT USAGE, CREATE ON SCHEMA product_management TO product_manager_role;

-- grant privileges on existing tables and sequences (if any)
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES
    IN SCHEMA product_management TO product_manager_role;

GRANT USAGE, SELECT ON ALL SEQUENCES
    IN SCHEMA product_management TO product_manager_role;

-- run as the creator role and set default privileges for future tables and sequences
SET ROLE product_manager_role;

ALTER DEFAULT PRIVILEGES IN SCHEMA product_management
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO product_manager_role;

ALTER DEFAULT PRIVILEGES IN SCHEMA product_management
    GRANT USAGE, SELECT ON SEQUENCES TO product_manager_role;

RESET ROLE;

-- set search_path for the login user so tables default to custom schema
ALTER ROLE product_manager_user SET search_path = product_management;

-- connect as the new login user
\c product_management_db product_manager_user
