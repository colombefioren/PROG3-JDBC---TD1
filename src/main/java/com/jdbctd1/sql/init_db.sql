CREATE DATABASE product_management_db;

\c product_management_db

CREATE ROLE product_manager_role NOLOGIN;

CREATE SCHEMA product_management AUTHORIZATION product_manager_role;

CREATE USER product_manager_user WITH PASSWORD '123456';

GRANT product_manager_role TO product_manager_user;

GRANT CONNECT ON DATABASE product_management_db TO product_manager_user;

GRANT USAGE, CREATE ON SCHEMA product_management TO product_manager_role;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES
    IN SCHEMA product_management TO product_manager_role;

GRANT USAGE, SELECT ON ALL SEQUENCES
    IN SCHEMA product_management TO product_manager_role;

SET ROLE product_manager_role;

ALTER DEFAULT PRIVILEGES IN SCHEMA product_management
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO product_manager_role;

ALTER DEFAULT PRIVILEGES IN SCHEMA product_management
    GRANT USAGE, SELECT ON SEQUENCES TO product_manager_role;

RESET ROLE;

ALTER ROLE product_manager_role SET search_path = product_management;

\c product_management_db product_manager_user

