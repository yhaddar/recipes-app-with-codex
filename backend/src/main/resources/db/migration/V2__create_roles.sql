CREATE TABLE roles (
    id UUID NOT NULL,
    name VARCHAR(20) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id),
    CONSTRAINT uk_roles_name UNIQUE (name),
    CONSTRAINT ck_roles_name CHECK (name IN ('GUEST', 'USER', 'ADMIN'))
);
